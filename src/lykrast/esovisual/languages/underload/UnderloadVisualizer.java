package lykrast.esovisual.languages.underload;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import javax.swing.JLabel;

import lykrast.esovisual.core.ui.StringReaderDisplayer;

public class UnderloadVisualizer extends JLabel {
	private static final long serialVersionUID = 7169832245163409582L;
	
	private Deque<String> stack;
	private StringReaderDisplayer program;
	
	private Dimension size;
	
	public UnderloadVisualizer() {
		size = new Dimension(0, 0);
		program = new StringReaderDisplayer();
		init(new ArrayDeque<>(), "");
	}
	
	public void init(Deque<String> stack, String program) {
		this.stack = stack;
		this.program.setString(program).setPointer(0);
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
		int padding = font.stringWidth("()");
		int ySpace = font.getMaxAscent(), yOffset = ySpace / 2 + ySpace, height = ySpace*2;
		
		size.height = height * 2;

		Rectangle bb = g.getClipBounds();
		g.setColor(Color.WHITE);
		g.fillRect(bb.x, bb.y, bb.width, bb.height);
		
		//Stack
		g.setColor(Color.BLACK);
		int totalWidth = 0, lastWidth = 0;
		Iterator<String> it = stack.descendingIterator();
		while (it.hasNext()) {
			String s = it.next();
			int width = font.stringWidth(s) + padding;
			
			g.drawRect(totalWidth, 0, width, height);
			g.drawString(s, totalWidth + padding / 2, yOffset);
			
			totalWidth += width;
			lastWidth = width;
		}
		size.width = Math.max(totalWidth, 100);
		
		//Stack pointer
		if (!stack.isEmpty()) g.fillRect(totalWidth - lastWidth, height + 5, lastWidth, 5);

		//Program
		program.draw(g, 0, height*2);
		size.width = Math.max(size.width, program.getLastLength());
		
		modified.dispose();
	}
	
	public void update(String code, int instructionPointer) {
		program.setString(code).setPointer(instructionPointer);
		revalidate();
		repaint();
	}
}
