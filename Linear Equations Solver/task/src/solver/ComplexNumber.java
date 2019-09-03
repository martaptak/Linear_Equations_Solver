package solver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ComplexNumber {

	private Double realPart;

	private Double imaginaryPart;

	ComplexNumber(String complexNumber) {

		if (complexNumber.contains("i")) {
			System.out.println(complexNumber);
			StringBuilder part = new StringBuilder();
			char[] complexNumberInChars = complexNumber.toCharArray();
			List<String> parts = new ArrayList<>();
			part.append(complexNumberInChars[0]);
			for (int i = 1; i < complexNumberInChars.length; i++) {
				char c = complexNumberInChars[i];
			//	System.out.println("c=" + c);
				if (c != '+' && c != '-') {
					part.append(c);
				} else {
					parts.add(part.toString());
					part = new StringBuilder();
					if (c == '-') {
						part.append(c);
					}

				}
			}
			if(part.toString().equals("i") || part.toString().equals("-i")){
				parts.add(part.toString().replace("i", "1"));
			}
			else {
				parts.add(part.toString().replace("i", ""));
			}


			if (parts.size() == 2) {
				this.realPart = Double.parseDouble(parts.get(0));
				this.imaginaryPart = Double.parseDouble(parts.get(1));
			} else {
				this.realPart = 0.0;
				this.imaginaryPart = Double.parseDouble(parts.get(0));
			}

		} else {
			this.realPart = Double.parseDouble(complexNumber);
			this.imaginaryPart = 0.0;
		}

	}

	private ComplexNumber(Double realPart, Double imaginaryPart) {

		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;

	}

	ComplexNumber add(ComplexNumber a) {

		return new ComplexNumber(this.realPart + a.realPart, this.imaginaryPart + a.imaginaryPart);
	}

	ComplexNumber subtract(ComplexNumber a) {

		return new ComplexNumber(this.realPart - a.realPart, this.imaginaryPart - a.imaginaryPart);
	}

	ComplexNumber multiply(ComplexNumber a) {

		Double realPart = this.realPart * a.realPart - (this.imaginaryPart * a.imaginaryPart);
		Double imaginaryPart = this.realPart * a.imaginaryPart + this.imaginaryPart * a.realPart;

		return new ComplexNumber(realPart, imaginaryPart);
	}

	ComplexNumber divide(ComplexNumber a) {

		ComplexNumber up = this.multiply(new ComplexNumber(a.realPart, -a.imaginaryPart));
		Double down = Math.pow(a.realPart, 2) + Math.pow(a.imaginaryPart, 2);

		return new ComplexNumber(up.realPart / down, up.imaginaryPart / down);
	}

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();
		if(realPart != 0 ) {
			stringBuilder.append(new DecimalFormat("#.####").format(realPart));
		}
		if (imaginaryPart != 0) {
			if (imaginaryPart > 0.0 && realPart != 0) {
				stringBuilder.append("+");
			}
			if(imaginaryPart != 1.0) {
				stringBuilder.append(new DecimalFormat("#.####").format(imaginaryPart));
			}
			stringBuilder.append("i");
		}
		if(realPart == 0 && imaginaryPart == 0){
			stringBuilder.append("0");
		}

		return stringBuilder.toString();
	}

	boolean isZero() {

		return realPart == 0 && imaginaryPart == 0;
	}

	boolean isOne() {

		return realPart == 1 && imaginaryPart == 0;
	}

	ComplexNumber negate() {

		return new ComplexNumber(-realPart, -imaginaryPart);
	}
}
