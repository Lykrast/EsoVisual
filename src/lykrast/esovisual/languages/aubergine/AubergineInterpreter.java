package lykrast.esovisual.languages.aubergine;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import lykrast.esovisual.core.Interpreter;
import lykrast.esovisual.core.ui.InterpreterFrame;

public class AubergineInterpreter implements Interpreter {
	private final static int
		VAL_A = 'a',
		VAL_B = 'b',
		POINT_A = 'A',
		POINT_B = 'B',
		INSTRUCTION = 'i',
		OUTSIDE = 'o',
		ONE = '1',
		ASSIGN = '=',
		INCREMENT = '+',
		DECREMENT = '-',
		JUMP = ':';
	
	private int[] tape;
	private int i, a, b;
	
	private byte[] stdin;
	private int stdinPointer;
	private PrintStream stdout;
	
	private AubergineVisualizer visualizer;
	
	public AubergineInterpreter() {
		visualizer = new AubergineVisualizer();
	}

	@Override
	public void interpret(String source, String input, PrintStream output) {
		byte[] ascii = source.getBytes(StandardCharsets.US_ASCII);
		tape = new int[ascii.length];
		for (int i = 0; i < ascii.length; i++) tape[i] = ascii[i];

		stdin = input.getBytes(StandardCharsets.US_ASCII);
		stdinPointer = 0;
		stdout = output;
		
		i = 0;
		a = 0;
		b = 0;
		
		visualizer.init(tape, input);
		
		try {
			while (i >= 0 && i < tape.length) {
				Thread.sleep(InterpreterFrame.WAIT);
				evaluate();
				i += 3;
				visualizer.update(i, a, b, stdinPointer);
			}
		} catch (InterruptedException e) {}
	}
	
	private void evaluate() {
		if (tape[i] != ASSIGN) {
			if (tape[i+1] == OUTSIDE) throw new IllegalArgumentException("Can't use o outside of an assignement, character " + (i+1));
			else if (tape[i+2] == OUTSIDE) throw new IllegalArgumentException("Can't use o outside of an assignement, character " + (i+2));
		}
		
		switch (tape[i]) {
		case ASSIGN:
			//Set value of the first parameter to the second parameter
			put(i+1, get(i+2));
			return;
		case INCREMENT:
			//Increases first parameter by the second parameter
			put(i+1, get(i+1) + get(i+2));
			return;
		case DECREMENT:
			//Decreases first parameter by the second parameter
			put(i+1, get(i+1) - get(i+2));
			return;
		case JUMP:
			//If second parameter is non zero, set i to the first parameter
			if (get(i+2) != 0) i = get(i+1);
			return;
		}
		
		throw new IllegalArgumentException("Invalid instruction " + (char)tape[i] + ", character " + i);
	}
	
	private void put(int index, int value) {
		switch (tape[index]) {
		case VAL_A:
			//First variable
			a = value;
			return;
		case VAL_B:
			//Second variable
			b = value;
			return;
		case POINT_A:
			//Value pointed by first variable
			tape[a] = value;
			return;
		case POINT_B:
			//Value pointed by second variable
			tape[b] = value;
			return;
		case INSTRUCTION:
			//Position of the instruction pointer
			i = value;
			return;
		case OUTSIDE:
			//Print the character
			stdout.print((char)value);
			return;
		}
		
		throw new IllegalArgumentException("Invalid first parameter " + (char)tape[index] + ", character " + index);
	}
	
	private int get(int index) {
		switch (tape[index]) {
		case VAL_A:
			//First variable
			return a;
		case VAL_B:
			//Second variable
			return b;
		case POINT_A:
			//Value pointed by first variable
			return tape[a];
		case POINT_B:
			//Value pointed by second variable
			return tape[b];
		case INSTRUCTION:
			//Position of the instruction pointer
			return i;
		case OUTSIDE:
			//Get character code from input
			if (stdinPointer >= stdin.length) return 0;
			else return stdin[stdinPointer++];
		case ONE:
			//1
			return 1;
		}
		
		throw new IllegalArgumentException("Invalid second parameter " + (char)tape[index] + ", character " + index);
	}

	@Override
	public JComponent getVisualizer() {
		return new JScrollPane(visualizer);
	}

}
