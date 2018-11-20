package lykrast.esovisual.languages.brainfuck;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import lykrast.esovisual.core.ui.StringReaderDisplayer;

public class BrainfuckVisualizer extends JLabel {
	private static final long serialVersionUID = 1322342102141922265L;

	private byte[] tape;
	private StringReaderDisplayer program, stdin;
	private int tapePointer;
	
	private Dimension size;
	
	public BrainfuckVisualizer() {
		size = new Dimension(0, 0);
		program = new StringReaderDisplayer();
		stdin = new StringReaderDisplayer();
		init(new byte[1], "", "");
	}
	
	public void init(byte[] tape, String program, String stdin) {
		this.tape = tape;
		this.program.setString(program).setPointer(0);
		this.stdin.setString(stdin).setPointer(0);
		tapePointer = 0;
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
		
		//Tape pointer
		g.fillRect(tapePointer*width, height + 5, width, 5);

		//Program
		program.draw(g, 0, height*2);
		//Input
		stdin.draw(g, 0, height*3);
		
		modified.dispose();
	}
	
	public void update(int instructionPointer, int tapePointer, int stdinPointer) {
		program.setPointer(instructionPointer);
		this.tapePointer = tapePointer;
		stdin.setPointer(stdinPointer);
		revalidate();
		repaint();
	}
}
