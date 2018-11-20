package lykrast.esovisual.core;

import java.io.PrintStream;

import javax.swing.JComponent;

public interface Interpreter {
	/**
	 * Interpret the given program fed with the given standard input, returning anything put to the standard output.
	 * @param source Source code of the program to interpret
	 * @param input Text written in the standard input to be given to the program
	 * @param output OutputStream where standard output will be written
	 */
	void interpret(String source, String input, PrintStream output);
	
	/**
	 * Interpret the given program with no standard input, returning anything put to the standard output.
	 * @param source Source code of the program to interpret
	 * @param output OutputStream where standard output will be written
	 */
	default void interpret(String source, PrintStream output) {
		interpret(source, "", output);
	}
	
	JComponent getVisualizer();
}
