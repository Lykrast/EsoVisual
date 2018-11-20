package lykrast.esovisual.core.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lykrast.esovisual.core.EmptyInterpreter;
import lykrast.esovisual.core.Interpreter;

public class InterpreterFrame extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = -3347010233798883713L;
	
	private JTextArea program, input, output;
	private JButton launch, stop;
	private JSlider speedSlider;
	public static int WAIT = 100;
	
	private Interpreter interpreter;
	private JPanel visualizer;
	private Thread thread;
	private PrintStream outputStream;

	public InterpreterFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setLocationByPlatform(true);
		setTitle("EsoVisual");
		
		//Contains all the main things
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BorderLayout());
		
		JMenuBar menu = new JMenuBar();
		menu.add(new InterpreterMenu(this));
		
		add(menu, BorderLayout.NORTH);
		
		//Speed slider
		speedSlider = new JSlider(0, 1000, WAIT);
		speedSlider.setMajorTickSpacing(100);
		speedSlider.setMinorTickSpacing(10);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.addChangeListener(this);
		speedSlider.setBorder(BorderFactory.createTitledBorder("Instruction delay (ms)"));
		
		wrapper.add(speedSlider, BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 1));
		
		//Visualizer
		visualizer = new JPanel();
		visualizer.setLayout(new BorderLayout());
		mainPanel.add(visualizer);
		
		//Program
		program = makeTextArea("Program", mainPanel);
		//Input
		input = makeTextArea("Input", mainPanel);
		//Output
		output = makeTextArea("Output", mainPanel);
		output.setEditable(false);
		outputStream = new PrintStream(new UIOutputStream(output));
		
		wrapper.add(mainPanel, BorderLayout.CENTER);
		
		//Buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		
		launch = new JButton("Launch");
		launch.addActionListener(this);
		buttons.add(launch);
		
		stop = new JButton("Stop");
		stop.addActionListener(this);
		buttons.add(stop);
		
		wrapper.add(buttons, BorderLayout.SOUTH);
		
		add(wrapper, BorderLayout.CENTER);
		
		setInterpreter(new EmptyInterpreter());
	}
	
	private JTextArea makeTextArea(String name, JPanel panel) {
		JTextArea area = new JTextArea();
		area.setLineWrap(true);

		JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(BorderFactory.createTitledBorder(name));
		panel.add(scroll);
		
		return area;
	}
	
	public void setInterpreter(Interpreter i) {
		stop();
		program.setText("");
		input.setText("");
		output.setText("");
		interpreter = i;
		visualizer.removeAll();
		visualizer.add(interpreter.getVisualizer());
		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == launch) {
			stop();
			thread = new Thread(this::interpret);
			thread.start();
		}
		else if (event.getSource() == stop) {
			stop();
		}
	}
	
	private void stop() {
		if (thread != null) {
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void interpret() {
		outputStream.flush();
		output.setText("");
		try {
			interpreter.interpret(program.getText(), input.getText(), outputStream);
		}
		catch (Exception e) {
			output.setText(e.toString());
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if (event.getSource() == speedSlider) WAIT = speedSlider.getValue();
	}

}
