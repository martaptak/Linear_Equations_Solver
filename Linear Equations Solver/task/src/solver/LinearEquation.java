package solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

enum ResultType {
	NO_SOLUTIONS,
	SINGLE_SOLUTION,
	INFINITE_NUMBER_OF_SOLUTIONS
}

class LinearEquation {

	private Matrix matrix;

	private ComplexNumber[] x;

	private String resultString;

	private ResultType check;

	private List<Swap> swapHistory = new ArrayList<>();

	private static StringBuilder log = new StringBuilder();

	LinearEquation(String path) {

		this.matrix = Matrix.readFromFile(path);
	//	System.out.println(matrix.print());
		this.x = new ComplexNumber[Objects.requireNonNull(matrix).getNumberOfRows()];
		log.append("Rows manipulation:\n");
	}

	void applyGaussJordan() {

		transformMatrix();
		displaySolutionGaussJordan();
	}

	private void transformMatrix() {

		Row currentRow;
		Row tempRow;
		int numberOfRows = matrix.getRows().length;
	//	System.out.println("Row number: " + numberOfRows);
		for (int i = 0; i < numberOfRows; i++) {
			transformToOne(i);
			currentRow = matrix.getRow(i);

			for (int j = i + 1; j < numberOfRows; j++) {
				tempRow = matrix.getRow(j);
				transformToZero(currentRow, tempRow, i);

			}
		}


		if (matrix.isNoSolution()) {
			check = ResultType.NO_SOLUTIONS;
			return;
		}

		for (int i = numberOfRows - 1; i >= 0; i--) {
			transformToOne(i);
			currentRow = matrix.getRow(i);
			for (int j = i - 1; j >= 0; j--) {
				tempRow = matrix.getRow(j);
				transformToZero(currentRow, tempRow, i);
			}
		}

		check = check();
	}

	private boolean swapColumns(Row currentRow, int columnIndex) {

		for (int i = columnIndex + 1; i < matrix.getNumberOfArguments(); i++) {
			if (!currentRow.get(i).isZero()) {
				matrix.swapColumns(columnIndex, i);
				swapHistory.add(new Swap(columnIndex, i));
				log.append("C").append(columnIndex + 1);
				log.append(" <-> ");
				log.append("C").append(i + 1);
				log.append("\n");
				return true;
			}
		}

		return false;
	}

	private boolean swapRows(Row currentRow, int index) {

		boolean found = false;
		Row newRow;
		int newIndex;
		ComplexNumber coefficient;
		for (int i = index; i < matrix.getNumberOfRows(); i++) {
			newRow = matrix.getRow(i);
			coefficient = newRow.get(index);
			//	System.out.println("Swapping rows : " + coefficient);
			if (!coefficient.isZero()) {
				newIndex = i;
				matrix.swapRows(index, newIndex);
				log.append(newRow.getName());
				log.append(" <-> ");
				log.append(currentRow.getName());
				log.append("\n");
				found = true;
			}
		}
		return found;
	}

	private boolean searchNonZeroElement(int i) {

		for (int k = i + 1; k < matrix.getNumberOfArguments() - 1; k++) {
			for (int j = i + 1; j < matrix.getNumberOfRows(); j++) {
				if (!matrix.getValue(j, k).isZero()) {
					swapColumns(matrix.getRow(k), i);
					swapRows(matrix.getRow(j), i);
					return true;
				}
			}
		}
		return false;
	}

	private void transformToOne(int i) {

		Row currentRow = matrix.getRow(i);
		ComplexNumber coefficient = currentRow.get(i);
		if (coefficient.isZero()) {
			boolean foundNonZero;
			if (!swapRows(currentRow, i)) {
				foundNonZero = swapColumns(currentRow, i);
				if (!foundNonZero) {
					foundNonZero = searchNonZeroElement(i);
					if (!foundNonZero) {
						return;
					}
				}
			}
			transformToOne(i);
		}

		if (!coefficient.isOne() && !coefficient.isZero()) {
			currentRow.normalizeRow(i);
			log.append(coefficient.toString());
			log.append(" * ");
			log.append("R").append(i + 1);
			log.append(" -> ");
			log.append("R").append(i + 1);
			log.append("\n");

		}

		//	return newIndex == -1 ? i : newIndex;
	}

	private void transformToZero(Row currentRow, Row tempRow, int currentIndex) {

		ComplexNumber coefficient = tempRow.get(currentIndex);
		if (coefficient.isZero()) {
			return;
		}
		for (int i = 0; i < matrix.getNumberOfArguments(); i++) {
			ComplexNumber value = coefficient.negate().multiply(currentRow.get(i)).add(tempRow.get(i));
			matrix.saveValue(tempRow.getRowNumber() - 1, i, value);
		}

		log.append(coefficient.negate().toString());
		log.append(" * ");
		log.append(currentRow.getName());
		log.append(" + ");
		log.append(tempRow.getName());
		log.append(" -> ");
		log.append(tempRow.getName());
		log.append("\n");
	}

	private void displaySolutionGaussJordan() {


		String print = "";

		switch (check) {
			case INFINITE_NUMBER_OF_SOLUTIONS:
				setResultString("Infinitely many solutions");
				print = getResultString();
				break;
			case NO_SOLUTIONS:
				setResultString("No solutions");
				print = getResultString();
				break;
			case SINGLE_SOLUTION:
				singleSolution();
				print = "The solution is: (" +
						getResultString().replaceAll("\n", ", ") + ")";
		}

		System.out.println(log.toString());
	//System.out.println(matrix.print());
		System.out.println(print);


	}

	private void singleSolution() {

		int numberOfRows = matrix.getNumberOfRows();
		int numberOfArguments = matrix.getNumberOfArguments();


	//	System.out.println(matrix.print());
		x = new ComplexNumber[numberOfArguments - 1];

		int index;
		for (int i = 0; i < numberOfRows; i++) {
			if (matrix.getRow(i).isZeroFilled()) {
				continue;
			}
			index = matrix.getRow(i).getIndexOfFirstNonZeroElement();

			x[index] = matrix.getValue(i, numberOfArguments - 1);

		}

		StringBuilder result = new StringBuilder();
		for (ComplexNumber x : x) {
			result.append(x.toString());
			result.append("\n");
		}
		result.deleteCharAt(result.length() - 1);
		setResultString(result.toString());
	}

	String getResultString() {

		return this.resultString;

	}

	private void setResultString(String resultString) {

		this.resultString = resultString;
	}

	private ResultType check() {

		revokeSwap();
		matrix.deleteZeroRows();

		if (matrix.isNoSolution()) {
			return ResultType.NO_SOLUTIONS;
		} else if (matrix.ifInfiniteSolution()) {
			return ResultType.INFINITE_NUMBER_OF_SOLUTIONS;
		} else {
			return ResultType.SINGLE_SOLUTION;
		}
	}

	private void revokeSwap() {

		int[] swapIndexes;

		if (swapHistory != null && !swapHistory.isEmpty()) {
			for (int i = swapHistory.size() - 1; i >= 0; i--) {
				swapIndexes = swapHistory.get(i).getReversedSwap();
				matrix.swapColumns(swapIndexes[0], swapIndexes[1]);
				log.append("Reverse: ");
				log.append("C").append(swapIndexes[0] + 1);
				log.append(" <-> ");
				log.append("C").append(swapIndexes[1] + 1);
				log.append("\n");
			}
			swapHistory.clear();
		}
	}
}
