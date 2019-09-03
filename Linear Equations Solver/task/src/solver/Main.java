package solver;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

//TODO command pattern
public class Main {

	public static void main(String[] args) {

		String fileIn = null;
		String fileOut = null;


		for (int i = 0, j = 1; j < args.length; i += 2, j += 2) {
			if (args[i].equals("-in")) {
				fileIn = args[j];
			}
			if (args[i].equals("-out")) {
				fileOut = args[j];
			}

		}
		if(fileIn != null){
			LinearEquation linearEquation = new LinearEquation(fileIn);
			System.out.println("Start solving the equation.");
			linearEquation.applyGaussJordan();

			saveSolutionToFile(fileOut, linearEquation);

		}

	}

	private static  void saveSolutionToFile(String file, LinearEquation linearEquation) {

		try (PrintWriter printWriter = new PrintWriter(file)) {
		printWriter.print(linearEquation.getResultString());
			System.out.println("Saved to file " + file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
