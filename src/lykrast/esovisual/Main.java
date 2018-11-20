package lykrast.esovisual;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import lykrast.esovisual.core.ui.InterpreterFrame;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error loading look and feel: " + e);
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run() {
				JFrame frame = new InterpreterFrame();
				//frame.pack();
				frame.setVisible(true);
			}
			
		});
	}

}
