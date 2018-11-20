package lykrast.esovisual.core;

import java.io.PrintStream;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class EmptyInterpreter implements Interpreter {

	@Override
	public void interpret(String source, String input, PrintStream output) {
	}

	@Override
	public JComponent getVisualizer() {
		return new JPanel();
	}

}
