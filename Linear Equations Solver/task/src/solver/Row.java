package solver;

class Row {  //slawek: czy Row to nie jest zbyt ogólna klasa? Może Row powinno być nested class w Matrix?

	private int rowNumber;

	private ComplexNumber[] arguments;

	Row(int rowNumber, ComplexNumber[] arguments, int numberOfArguments) {

		this.rowNumber = rowNumber;
		this.arguments = new ComplexNumber[numberOfArguments];
		System.arraycopy(arguments, 0, this.arguments, 0, arguments.length);
	}

	int getRowNumber() {

		return rowNumber;
	}

	void setRowNumber(int rowNumber) {

		this.rowNumber = rowNumber;
	}

	ComplexNumber[] getArguments() {

		return arguments;
	}

	String getName() {

		return "R" + this.rowNumber;
	}

	ComplexNumber get(int i) {

		return this.arguments[i];
	}

	private void divide(ComplexNumber value) {

		if (value.isZero()) {
			throw new ArithmeticException(("Division by zero"));
		}
		for (int i = 0; i < arguments.length; i++) {
			this.arguments[i] = this.arguments[i].divide(value);
		}
	}

	void normalizeRow(int index)  {
		try {
			divide(arguments[index]);
		}catch (ArithmeticException e){
			System.out.print("");
		}
	}

	void swapCells(int i, int j) {

		ComplexNumber temp = this.arguments[j];
		this.arguments[j] = this.arguments[i];
		this.arguments[i] = temp;
	}

	boolean isZeroFilled() {

		for (ComplexNumber argument : arguments) {
			if (!argument.isZero()) {
				return false;
			}
		}
		return true;
	}

	int getIndexOfFirstNonZeroElement() {
		for(int i = 0; i < arguments.length; i++) {
			if(!arguments[i].isZero() ) {
				return i;
			}
		}

		return -1;
	}
}
