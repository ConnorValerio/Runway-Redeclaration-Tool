package Views;

import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import Listeners.UserManualListener;
import Models.Airport;
import Services.LocalizationService;
import Controllers.*;

public class ConsoleGUI {
	private interface Action {
		public void doThing(Object[] objs);
	}

	private class Tuple<X, Y, Z> {
		public X fst;
		public Y snd;
		public Z thd;

		public Tuple(X f, Y s, Z t) {
			fst = f;
			snd = s;
			thd = t;
		}
	}

	private enum ParamType {
		DOUBLE, INT, STRING
	}

	private JDialog frame;
	private JTextArea console, entry;
	private GUI gui;
	private ArrayList<String> commands;
	private HashMap<String, Tuple<List<ParamType>, Action, String>> actions;
	private int currentSelect = 1;

	private void addActions() {
		actions = new HashMap<>();
		StringBuilder allCommands = new StringBuilder();
		actions.put("airportnew", new Tuple<List<ParamType>, Action, String>(Arrays.asList(ParamType.STRING, ParamType.STRING, ParamType.STRING), new Action() {
			@Override
			public void doThing(Object[] objs) {
				// strings used for validation
				String airportNameText = (String) objs[0];
				String runwaySourceText = (String) objs[1];
				String obstructionSourceText = (String) objs[2];

				// validation on the three necessary fields
				if (!(airportNameText.matches("([a-zA-Z]+ +)*[a-zA-Z]+")) || airportNameText.equals("") || airportNameText.equals("Example Text")) {
					updateDisplay(LocalizationService.localizeString("valid_airport"));
					return;
				}

				// serialize airport and write to airport
				// name-specific file
				Airport airport = new Airport(airportNameText, runwaySourceText, obstructionSourceText);

				// name-specific file
				String filename = airportNameText + ".ser";
				File check = new File(filename);

				// if the file doesn't exist...
				if (!check.exists()) {

					try {
						ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
						output.writeObject(airport);
						output.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					updateDisplay(LocalizationService.localizeString("dupe_airport") + airportNameText);
					return;
				}

				// close the WelcomeGUI and open the main
				// program
				frame.dispose();
				gui.dispose();
				GUI g = new GUI(airport.getAirportName(), airport.getRunwayFilename(), airport.getObstructionFilename());
				g.init();
			}
		}, "Usage: airportnew \"Name\" \"Path_To_Runway_File\" \"Path_To_Obstruction_File\"" + " \n(2 Parameters) Create a new airport with the 2 given .xml files."));

		actions.put("airport", new Tuple<List<ParamType>, Action, String>(Arrays.asList(ParamType.STRING), new Action() {
			@Override
			public void doThing(Object[] objs) {
				Airport airport;
				try {
					File chosenfile = new File((String) objs[0]);

					// deserialize chosen airport.ser file
					InputStream file = new FileInputStream(chosenfile.getName());
					InputStream buffer = new BufferedInputStream(file);
					ObjectInput input = new ObjectInputStream(buffer);

					airport = (Airport) input.readObject();
					input.close();

				} catch (IOException | ClassNotFoundException e1) {

					updateDisplay(LocalizationService.localizeString("not_ser"));

					airport = new Airport("EmptyAirport", "", "");
					return;
				}

				// close the WelcomeGUI and open the main program
				frame.dispose();
				gui.dispose();
				GUI g = new GUI(airport.getAirportName(), airport.getRunwayFilename(), airport.getObstructionFilename());
				g.init();
			}
		}, "Usage: airport \"Path_To_Airport_File\"" + " \n(1 Parameter) Loads an aiport from a .ser file."));
		actions.put("lang", new Tuple<List<ParamType>, Action, String>(Arrays.asList(ParamType.STRING), new Action() {
			@Override
			public void doThing(Object[] objs) {
				gui.changeLanguage((String) objs[0]);
			}
		}, "Usage: lang language_code \n(1 Parameter) Changes the language to the given country code."));

		actions.put("exportvis", new Tuple<List<ParamType>, Action, String>(new ArrayList<ParamType>(), new Action() {
			@Override
			public void doThing(Object[] objs) {
				gui.exportVisualisation.doClick();
			}
		}, "Usage: exportvis \n(0 Parameters) Saves a screenshot of the current visualisation to the working dir" + "ectory."));

		actions.put("resetvis", new Tuple<List<ParamType>, Action, String>(new ArrayList<ParamType>(), new Action() {
			@Override
			public void doThing(Object[] objs) {
				GUI.ResetVisualisationListener l = ConsoleGUI.this.gui.new ResetVisualisationListener();
				l.actionPerformed(null);
			}
		}, "Usage: resetvis\n(0 Parameters) Resets the visualisation pan, zoom, and rotation."));

		actions.put("manual", new Tuple<List<ParamType>, Action, String>(new ArrayList<>(), new Action() {
			@Override
			public void doThing(Object[] objs) {
				UserManualListener man = new UserManualListener();
				man.actionPerformed(null);
			}
		}, "Usage: manual\n(0 Parameters) Opens the user manual."));

		actions.put("runway", new Tuple<List<ParamType>, Action, String>(Arrays.asList(ParamType.STRING, ParamType.INT, ParamType.DOUBLE, ParamType.DOUBLE, ParamType.DOUBLE, ParamType.DOUBLE, ParamType.DOUBLE, ParamType.DOUBLE), new Action() {
			@Override
			public void doThing(Object[] objs) {
				Runway runway = new Runway((String) objs[0], (int) objs[1], (double) objs[2], (double) objs[3], (double) objs[4], (double) objs[5], (double) objs[6], (double) objs[7], (double) objs[7], (double) objs[8]);
				gui.changeRunway(runway);
			}
		}, "Usage: runway \"Name\" ID TODA_From_Left TODA_From_Right" + "TORA ASDA_From_Left ASDA_From_Right LDA" + " Plane_Blast_Distance\n(9 Parameters) Create a new runway with the specified parameters."));
		actions.put("obst", new Tuple<List<ParamType>, Action, String>(Arrays.asList(ParamType.STRING, ParamType.DOUBLE, ParamType.DOUBLE, ParamType.DOUBLE, ParamType.DOUBLE, ParamType.DOUBLE), new Action() {
			@Override
			public void doThing(Object[] objs) {
				Obstruction o = new Obstruction((String) objs[0], (double) objs[1], (double) objs[2], (double) objs[3], (double) objs[4], (double) objs[5]);
				gui.runway.addObstruction(o);
			}
		}, "Usage: obst \"Name\" Width Height Length Position_Along_Runway " + "Distance_From_Centreline" + "\n (6 Parameters) Create a new obstruction with the specified parameters."));

		actions.forEach((k, v) -> {
			allCommands.append(k + " (" + v.fst.size() + " parameters)\n");
		});
		actions.put("help", new Tuple<List<ParamType>, Action, String>(new ArrayList<ParamType>(), new Action() {
			@Override
			public void doThing(Object[] objs) {
				updateDisplay(LocalizationService.localizeString("further_info") + "\n" + allCommands.toString());
			}
		}, "no"));
	}

	public ConsoleGUI(Runway runway, GUI g) {
		this.gui = g;
		commands = new ArrayList<String>();
		frame = new JDialog();
		frame.setTitle("Runway Redeclaration Tool Console");

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new escapeListener());

		frame.setIconImage(new ImageIcon("src\\assets\\data\\logo.png").getImage());

		JPanel main = new JPanel();
		frame.setContentPane(main);
		console = new JTextArea();
		console.setPreferredSize(new Dimension(900, 250));
		entry = new JTextArea();
		entry.setPreferredSize(new Dimension(900, 50));
		console.setEditable(false);
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		entry.setEditable(true);
		addActions();
		entry.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					currentSelect = 1;
					updateDisplay(entry.getText());
					handleCommand(entry.getText());
					entry.setText("");
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					e.consume();
					try {
						entry.setText(commands.get(commands.size() - (1 + currentSelect)));
						currentSelect += 2;
					} catch (Exception ex) {
						currentSelect = 1;
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		main.add(new JScrollPane(console), BorderLayout.NORTH);
		main.add(entry, BorderLayout.SOUTH);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				frame.setVisible(false);
				frame.dispose();
			}
		});

		// Setting Colors
		ArrayList<Color> backgroundColors = gui.getColourScheme();

		frame.setBackground(backgroundColors.get(0));
		console.setBackground(backgroundColors.get(0));
		entry.setBackground(backgroundColors.get(0));
		main.setBackground(backgroundColors.get(0));

		frame.setModal(true);
		frame.setPreferredSize(new Dimension(900, 320));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		entry.requestFocus();
		updateDisplay("For help, type 'help'.");
	}

	private void handleCommand(String cmd) {
		ArrayList<String> broken = new ArrayList<String>();
		Pattern p = Pattern.compile("\\b(?:(?<=\")[^\"]*(?=\")|\\w+)\\b");
		Matcher m = p.matcher(cmd);
		while (m.find()) {
			broken.add(m.group(0));
		}
		boolean isUsage = false;
		if (broken.get(0).endsWith("usage")) {
			broken.set(0, broken.get(0).replace("usage", ""));
			isUsage = true;
		}

		Tuple<List<ParamType>, Action, String> act = actions.get(broken.get(0));
		if (act == null) {
			updateDisplay(LocalizationService.localizeString("unrecognised"));
			return;
		}
		if (isUsage) {
			updateDisplay(act.thd);
			return;
		}

		Object[] parsed = new Object[act.fst.size()];
		try {
			if (act.fst.size() == broken.size() - 1) {
				for (int i = 0; i < act.fst.size(); ++i) {
					switch (act.fst.get(i)) {
					case DOUBLE:
						parsed[i] = Double.parseDouble(broken.get(i + 1));
						break;
					case INT:
						parsed[i] = Integer.parseInt(broken.get(i + 1));
						break;
					case STRING:
						parsed[i] = broken.get(i + 1);
						break;
					}
				}
			} else {
				throw new Exception();
			}
			act.snd.doThing(parsed);
		} catch (Exception e) {
			handleCommand(broken.get(0) + "usage");
		}
	}

	public class escapeListener implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (frame != null) {
					frame.setVisible(false);
					frame.dispose();
					frame = null;
				}
			}
			return false;
		}
	}

	private void updateDisplay(String cmd) {
		commands.add(cmd);
		StringBuilder strb = new StringBuilder();
		for (int i = Math.max(0, commands.size() - 100); i < commands.size(); ++i) {
			strb.append(commands.get(i));
			strb.append('\n');
		}
		console.setText(strb.toString());
	}
}
