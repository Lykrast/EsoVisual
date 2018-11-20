package lykrast.esovisual.core.ui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class UIOutputStream extends OutputStream {
	private JTextArea area;
	
	public UIOutputStream(JTextArea area) {
		this.area = area;
	}
	
	@Override
	public void write(int c) throws IOException {
		area.append(Character.toString((char) c));
		area.setCaretPosition(area.getText().length());
		area.update(area.getGraphics());
	}

}
