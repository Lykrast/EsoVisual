package lykrast.esovisual.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import lykrast.esovisual.core.Interpreter;
import lykrast.esovisual.languages.aubergine.AubergineInterpreter;
import lykrast.esovisual.languages.brainfuck.BrainfuckInterpreter;
import lykrast.esovisual.languages.underload.UnderloadInterpreter;

public class InterpreterMenu extends JMenu implements ActionListener {
	private static final long serialVersionUID = 102709155177645842L;
	
	private InterpreterFrame frame;
	private Map<String, Interpreter> map;
	
	public InterpreterMenu(InterpreterFrame frame) {
		super("Language");
		this.frame = frame;
		
		map = new HashMap<>();
		initLanguages();
	}
	
	private void initLanguages() {
		List<String> languages = new ArrayList<>();
		
		initLanguage("brainfuck", new BrainfuckInterpreter(), languages);
		initLanguage("Underload", new UnderloadInterpreter(), languages);
		initLanguage("Aubergine", new AubergineInterpreter(), languages);
		
		Collections.sort(languages, String.CASE_INSENSITIVE_ORDER);
		for (String s : languages) {
			JMenuItem item = new JMenuItem(s);
			item.setName(s);
			item.addActionListener(this);
			add(item);
		}
	}
	
	private void initLanguage(String name, Interpreter interpreter, List<String> languages) {
		map.put(name, interpreter);
		languages.add(name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof JMenuItem) {
			Interpreter interpreter = map.get(((JMenuItem)source).getName());
			if (interpreter != null) frame.setInterpreter(interpreter);
		}
	}

}
