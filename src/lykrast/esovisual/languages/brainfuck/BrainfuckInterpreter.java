package lykrast.esovisual.languages.brainfuck;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import lykrast.esovisual.core.Interpreter;
import lykrast.esovisual.core.ui.InterpreterFrame;

public class BrainfuckInterpreter implements Interpreter {
	private static final char 
			LEFT = '<', 
			RIGHT = '>', 
			ADD = '+', 
			SUBSTRACT = '-', 
			BLOCK_START = '[', 
			BLOCK_END = ']', 
			INPUT = ',', 
			OUTPUT = '.';
	
	private byte[] tape;
	private int instructionPointer, tapePointer;
	private char[] program;
	
	private byte[] stdin;
	private int stdinPointer;
	
	private PrintStream stdout;
	
	private BrainfuckVisualizer visualizer;
	
	public BrainfuckInterpreter() {
		visualizer = new BrainfuckVisualizer();
	}

	@Override
	public void interpret(String source, String input, PrintStream output) {
		program = source.toCharArray();
		instructionPointer = 0;

		tape = new byte[1000];
		tapePointer = 0;

		stdin = input.getBytes(StandardCharsets.US_ASCII);
		stdinPointer = 0;
		stdout = output;

		visualizer.init(tape, new String(program), new String(stdin, StandardCharsets.US_ASCII));
		try {
			while (instructionPointer < program.length) {
				Thread.sleep(InterpreterFrame.WAIT);
				interpretChar();
				instructionPointer++;
				visualizer.update(instructionPointer, tapePointer, stdinPointer);
			}

		} catch (InterruptedException e) {}
	}
	
	private void interpretChar() {
		switch (program[instructionPointer]) {
		case LEFT:
			//Move tape pointer left
			tapePointer--;
			break;
		case RIGHT:
			//Move tape pointer right
			tapePointer++;
			break;
		case ADD:
			//Increment pointed cell on the tape
			tape[tapePointer]++;
			break;
		case SUBSTRACT:
			//Decrement pointed cell on the tape
			tape[tapePointer]--;
			break;
		case BLOCK_START:
			//If pointed cell is 0, jump to the matching closing bracket
			if (tape[tapePointer] == 0) {
				int nest = 1;
				while (nest > 0) {
					instructionPointer++;
					if (program[instructionPointer] == BLOCK_START) nest++;
					else if (program[instructionPointer] == BLOCK_END) nest--;
				}
			}
			break;
		case BLOCK_END:
			//If pointed cell is not 0, jump back to the matching opening bracket
			if (tape[tapePointer] != 0) {
				int nest = 1;
				while (nest > 0) {
					instructionPointer--;
					if (program[instructionPointer] == BLOCK_START) nest--;
					else if (program[instructionPointer] == BLOCK_END) nest++;
				}
			}
			break;
		case INPUT:
			//Read 1 ASCII character from input and put its value in the pointed cell
			//EOF is 0
			if (stdinPointer >= stdin.length) tape[tapePointer] = 0;
			else tape[tapePointer] = stdin[stdinPointer++];
			break;
		case OUTPUT:
			//Write pointed cell as an ASCII character to output
			stdout.print((char)(tape[tapePointer] & 0xFF));
			break;
		}
	}

	@Override
	public JComponent getVisualizer() {
		return new JScrollPane(visualizer);
	}

}
