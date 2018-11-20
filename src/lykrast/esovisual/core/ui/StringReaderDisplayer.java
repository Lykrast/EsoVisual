package lykrast.esovisual.core.ui;

import java.awt.FontMetrics;
import java.awt.Graphics;

public class StringReaderDisplayer {
	private String str;
	private int pointer;
	private int lastLength;
	
	public StringReaderDisplayer() {
		str = "";
		pointer = 0;
		lastLength = 0;
	}
	
	public StringReaderDisplayer setString(String str) {
		this.str = str;
		return this;
	}
	public StringReaderDisplayer setPointer(int pointer) {
		this.pointer = pointer;
		return this;
	}
	public int getLastLength() {
		return lastLength;
	}
	
	public void draw(Graphics g, int offsetX, int offsetY) {
		if (str.length() > 0) {
			//str
			g.drawString(str, 0, offsetY);
			
			//str pointer
			FontMetrics font = g.getFontMetrics();
			int width = font.charWidth(str.charAt(Math.min(pointer, str.length()-1)));
			int xOffset = font.stringWidth(str.substring(0, pointer)) - width;
			lastLength = font.stringWidth(str);
			
			g.fillRect(xOffset, offsetY + 5, width, 5);
		}
		else lastLength = 0;
	}
}
