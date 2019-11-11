package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class Matrix {

	private int numberOfRows;

	private int numberOfArguments;

	private Row[] rows;

	private int[] columnMove;

	private Matrix(int numberOfRows, int numberOfArguments, Row[] rows) {

		this.numberOfRows = numberOfRows;
		this.numberOfArguments = numberOfArguments;
		this.rows = rows;
		this.columnMove = new int[numberOfArguments];

		for (int i = 0; i < numberOfArguments; i++) {
			this.columnMove[i] = i;
		}

	}

	int getNumberOfRows() {

		return numberOfRows;
	}

	Row[] getRows() {

		return rows;
	}

	static Matrix readFromFile(String file) {

		int variables;
		int equations;
		Row[] rows;

		try (Scanner scanner = new Scanner(new File(file))) {
			String[] matrix = scanner.nextLine().split(" ");
			variables = Integer.parseInt(matrix[0]);
			equations = Integer.parseInt(matrix[1]);
			rows = new Row[equations];
			for (int i = 0; i < equations; i++) {
				String[] line = scanner.nextLine().split(" ");
				ComplexNumber[] arguments = new ComplexNumber[variables + 1];
				int j = 0;
				for (String string : line) {
					arguments[j] =new ComplexNumber(string);
					j++;
				}
				rows[i] = new Row(i + 1, arguments, variables + 1);
			}


		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return null;

		}

		return new Matrix(equations, variables + 1, rows);
	}

	ComplexNumber getValue(int i, int j) {

		return this.getRows()[i].getArguments()[j];
	}

	void saveValue(int i, int j, ComplexNumber value) {

		this.getRows()[i].getArguments()[j] = value;
	}

	Row getRow(int index) {

		return this.rows[index];
	}

	void swapRows(int i, int j) {

		Row temp = rows[i];
		rows[i] = rows[j];
		rows[j] = temp;
		rows[i].setRowNumber(i+1);
		rows[j].setRowNumber(j+1);
	}

	void swapColumns(int i, int j) {

		for (Row row : rows) {
			row.swapCells(i, j);
		}
		int temp = columnMove[j];
		columnMove[j] = columnMove[i];
		columnMove[i] = temp;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (Row row : rows) {
			sb.append(Arrays.toString(row.getArguments()));
			sb.append("\n");
		}
		return sb.toString();
	}

	int getNumberOfArguments() {

		return numberOfArguments;
	}

	String print() {

		StringBuilder sb = new StringBuilder();
		ComplexNumber[] row;
		for (int i = 0; i < rows.length; i++) {
			row = getRow(i).getArguments();
			sb.append("[");
			for (ComplexNumber complexNumber : row) {
				sb.append(complexNumber.toString());
				sb.append(", ");
			}
			sb.delete(sb.length()-2, sb.length());
			sb.append("]");
			sb.append("\n");
		}
		return sb.toString();
	}

	private void deleteRow(int currentRow) {

		for (int i = currentRow; i < numberOfRows -1; i++) {
			for (int j = 0; j < numberOfArguments; j++) {
				rows[i].getArguments()[j] = rows[i + 1].getArguments()[j];
			}
		}
		this.numberOfRows--;
	}

	void deleteZeroRows() {

		for (int i = numberOfRows - 1; i >= 0; i--) {
			if(getRow(i).isZeroFilled()){
				deleteRow(i);
			}
		}
	}

	private boolean isNoSolutionRow(int currentRow){

		for (int i = 0; i < numberOfArguments - 1; i++) {
			if(!getValue(currentRow, i).isZero()){
				return false;
			}
		}

		return !getValue(currentRow, numberOfArguments - 1).isZero();
	}

	boolean isNoSolution() {  //slawek: czy macierz ma rozwiązanie? czy może chodzi o układ równań przedstawiony w
		// postaci macierzy? Może powinna być klasa SystemOfEquations, który zawiera w sobie Matrix?

		 for (int i = 0; i < numberOfRows; i++) {
		 	 if(isNoSolutionRow(i)){
		 	 	return true;
		     }
		 }
		 return false;
	 }

	 boolean ifInfiniteSolution(){

		 return numberOfRows < numberOfArguments - 1;
	 }
}



