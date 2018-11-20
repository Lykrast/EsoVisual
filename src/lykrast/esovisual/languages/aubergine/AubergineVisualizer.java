package lykrast.esovisual.languages.aubergine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import lykrast.esovisual.core.ui.StringReaderDisplayer;

public class AubergineVisualizer extends JLabel {
	private static final long serialVersionUID = 4995432604908433240L;
	
	private int[] tape;
	private StringReaderDisplayer stdin;
	private int i, a, b;
	
	private Dimension size;
	
	public AubergineVisualizer() {
		size = new Dimension(0, 0);
		stdin = new StringReaderDisplayer();
		init(new int[1], "");
	}
	
	public void init(int[] tape, String stdin) {
		this.tape = tape;
		this.stdin.setString(stdin).setPointer(0);
		i = 0;
		a = 0;
		b = 0;
		revalidate();
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return size;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return size;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics modified = g.create();
		
		FontMetrics font = g.getFontMetrics();
		int width = font.stringWidth("255") * 2;
		int ySpace = font.getMaxAscent(), yOffset = ySpace / 2 + ySpace, height = ySpace*3;
		
		size.height = height * 3;
		size.width = width * tape.length;

		Rectangle bb = g.getClipBounds();
		g.setColor(Color.WHITE);
		g.fillRect(bb.x, bb.y, bb.width, bb.height);
		
		//Tape
		g.setColor(Color.BLACK);
		for (int i = 0; i < tape.length; i++) {
			g.drawRect(i*width, 0, width, height);
			String number = Integer.toString(tape[i] & 0xFF);
			String character = Character.toString((char)(tape[i] & 0xFF));
			g.drawString(number, i*width + (width - font.stringWidth(number)) / 2, yOffset);
			g.drawString(character, i*width + (width - font.stringWidth(character)) / 2, ySpace + yOffset);
		}
		
		//Pointers
		g.fillRect(i*width, height + 5, width*3, 5);
		g.setColor(Color.RED);
		g.fillRect(a*width, height + 15, width, 5);
		g.drawString("a: " + a, 0, (height*7)/3);
		g.setColor(Color.BLUE);
		g.fillRect(b*width, height + 25, width, 5);
		g.drawString("b: " + b, 0, (height*8)/3);

		//Input
		g.setColor(Color.BLACK);
		stdin.draw(g, 0, height*3);
		
		modified.dispose();
	}
	
	public void update(int i, int a, int b, int stdinPointer) {
		this.i = i;
		this.a = a;
		this.b = b;
		stdin.setPointer(stdinPointer);
		revalidate();
		repaint();
	}
}
