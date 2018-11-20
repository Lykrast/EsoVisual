package lykrast.esovisual.languages.underload;

import java.io.PrintStream;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import lykrast.esovisual.core.Interpreter;
import lykrast.esovisual.core.ui.InterpreterFrame;

public class UnderloadInterpreter implements Interpreter {
	private static final char 
		SWAP = '~', 
		DUPLICATE = ':', 
		DISCARD = '!', 
		CONCATENATE = '*', 
		BLOCK_START = '(', 
		BLOCK_END = ')', 
		ENCLOSE = 'a', 
		EVAL = '^', 
		OUTPUT = 'S';
	
	private Stack<String> stack;
	private StringBuilder program;
	private PrintStream stdout;
	private int instructionPointer;
	
	private UnderloadVisualizer visualizer;
	
	public UnderloadInterpreter() {
		visualizer = new UnderloadVisualizer();
	}

	@Override
	public void interpret(String source, String input, PrintStream output) {
		stack = new Stack<>();
		program = new StringBuilder(source);
		instructionPointer = 0;
		
		stdout = output;
		
		visualizer.init(stack, program.toString());
		
		try {
			while (instructionPointer < program.length()) {
				Thread.sleep(InterpreterFrame.WAIT);
				interpretChar();
				instructionPointer++;
				visualizer.update(program.toString(), instructionPointer);
			}
		} catch (InterruptedException e) {}
	}
	
	private void interpretChar() {
		switch (program.charAt(instructionPointer)) {
		case SWAP:
			//Swap top 2 of stack
			String a = stack.pop();
			String b = stack.pop();
			stack.push(a);
			stack.push(b);
			break;
		case DUPLICATE:
			//Duplicate top of stack
			stack.push(stack.peek());
			break;
		case DISCARD:
			//Discard top of stack
			stack.pop();
			break;
		case CONCATENATE:
			//Concatenate top of stack to the end of the 2nd element from the top
			a = stack.pop();
			b = stack.pop();
			stack.push(b + a);
			break;
		case BLOCK_START:
			//Push everything between matching parentheses to the stack
			int nest = 1, end = instructionPointer;
			while (nest > 0) {
				end++;
				if (program.charAt(end) == BLOCK_START)
					nest++;
				else if (program.charAt(end) == BLOCK_END)
					nest--;
			}
			stack.push(program.substring(instructionPointer + 1, end));
			instructionPointer = end;
			break;
		case ENCLOSE:
			//Enclose top of stack in parentheses
			stack.push(BLOCK_START + stack.pop() + BLOCK_END);
			break;
		case EVAL:
			//Put top of the stack into the program, ready to be interpreted
			program.insert(instructionPointer + 1, stack.pop());
			break;
		case OUTPUT:
			//Output top of stack
			stdout.print(stack.pop());
			break;
		}
	}

	@Override
	public JComponent getVisualizer() {
		return new JScrollPane(visualizer);
	}

}
