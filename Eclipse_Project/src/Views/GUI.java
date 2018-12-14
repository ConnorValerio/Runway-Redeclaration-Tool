package Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

import Controllers.Obstruction;
import Controllers.Runway;
import Listeners.UserManualListener;
import Models.LogicalRunway;
import Services.LocalizationService;
import Services.ScreenshotFactory;
import Services.XMLObstructionService;
import Services.XMLRunwayService;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	// where custom runways will be imported from / exported to (if any are
	// created)
	String RUNWAY_FILE_NAME;

	// where custom obstructions will be imported from / exported to (if any are
	// created)
	String OBSTRUCTION_FILE_NAME;

	JDialog calculationBreakdown = null;

	// self reference used for window listener
	GUI gui;

	// used so user can edit an obstruction previously added to the runway
	Obstruction currentObstruction;

	// visualisation and the panel it's stored on
	private Visualisation vis;
	JPanel visPanel, calculationsOutputPanel, panel;

	String airportName;
	Runway runway;

	JButton editObstructionButton, changeRunwayButton, addObstructionButton;

	ArrayList<Obstruction> obstructions;
	ArrayList<Runway> runways;
	ArrayList<BufferedImage> bufferedImages;
	ArrayList<Image> scaledBufferedImages;
	ArrayList<JLabel> images;

	String originalBreakdown = "", landLeftBreakdown = "", landRightBreakdown = "", takeOffLeftBreakdown = "", takeOffRightBreakdown = "";

	ArrayList<JTextArea> informationTextAreas;
	ArrayList<JPanel> informationTextAreaPanels;
	JScrollPane jsp;

	String language = LocalizationService.localizeString("language_name");

	ArrayList<Color> curretColors;
	JMenuItem newAirport, exit, createObstruction, exportVisualisation, screenShotJPEG, screenShotPNG, screenShotGIF, openConsole;

	// Constructor for a GUI
	public GUI(String airportName, String runwaySource, String obstructionSource) {
		gui = this;
		gui.addWindowListener(new myWindowListener());

		// import and set the frame icon
		this.setIconImage(new ImageIcon("src\\assets\\data\\logo.png").getImage());
		// initialise current obstruction as a dummy obstruction
		this.currentObstruction = new Obstruction("Empty Obstruction", 0.0, 0.0, 0.0, 0.0, 0.0);

		this.airportName = airportName;

		// assign the XML source file names as variables to be referred to later
		this.RUNWAY_FILE_NAME = runwaySource;
		this.OBSTRUCTION_FILE_NAME = obstructionSource;

		XMLObstructionService xmlObstruction = new XMLObstructionService();
		XMLRunwayService xmlRunway = new XMLRunwayService();

		try {
			runways = xmlRunway.importRunways(RUNWAY_FILE_NAME);
			obstructions = xmlObstruction.importObstructions(OBSTRUCTION_FILE_NAME);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		this.runway = new Runway("Example", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);

		runway.addObstruction(new Obstruction(LocalizationService.localizeString("custom_obstr"), 0.0, 0.0, 0.0, 0.0, 0.0));
		runway.redeclareParameters();

		curretColors = new ArrayList<Color>();
		curretColors.add(Color.decode("#d6d9df"));
		curretColors.add(Color.decode("#f0f0f0"));
		curretColors.add(Color.decode("#d8dbe1"));
	}

	public void init() {
		setTitle(LocalizationService.localizeString("title"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(createMenuBar());

		Container container = getContentPane();

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new MyKeyDispatcher());

		JPanel content = new JPanel();

		container.add(content);
		content.setLayout(new BorderLayout(0, 0));
		content.add(createToolBar(), BorderLayout.NORTH);

		vis = new Visualisation(runway);
		LwjglAWTCanvas canvas = new LwjglAWTCanvas(vis);
		vis.resize(810, 700);

		visPanel = new JPanel();
		visPanel.setLayout(new BorderLayout(0, 0));
		visPanel.setPreferredSize(new Dimension(810, 700));
		visPanel.add(canvas.getCanvas(), BorderLayout.CENTER);

		this.setResizable(false);
		this.setSize(1390, 740);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		// Right-Hand Panel Stuff (Swing)
		// ////////////////////////////////////////////
		calculationsOutputPanel = new JPanel(new GridBagLayout());

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(calculationsOutputPanel, BorderLayout.CENTER);

		images = new ArrayList<JLabel>();
		bufferedImages = new ArrayList<BufferedImage>();
		scaledBufferedImages = new ArrayList<Image>();
		try {
			bufferedImages.add(ImageIO.read(new File("assets/data/Swing/" + language + "/OriginalFromLeft.png")));
			bufferedImages.add(ImageIO.read(new File("assets/data/Swing/" + language + "/OriginalFromRight.png")));
			bufferedImages.add(ImageIO.read(new File("assets/data/Swing/" + language + "/LandFromLeft.png")));
			bufferedImages.add(ImageIO.read(new File("assets/data/Swing/" + language + "/LandFromRight.png")));
			bufferedImages.add(ImageIO.read(new File("assets/data/Swing/" + language + "/TakeOffFromLeft.png")));
			bufferedImages.add(ImageIO.read(new File("assets/data/Swing/" + language + "/TakeOffFromRight.png")));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int o = 0; o < bufferedImages.size(); ++o) {
			scaledBufferedImages.add(bufferedImages.get(o).getScaledInstance(110, 20, Image.SCALE_SMOOTH));
			images.add(new JLabel(new ImageIcon(scaledBufferedImages.get(o))));
		}

		informationTextAreas = new ArrayList<JTextArea>();
		informationTextAreaPanels = new ArrayList<JPanel>();

		for (int i = 0; i < 7; ++i) {
			informationTextAreas.add(new JTextArea());
			informationTextAreaPanels.add(new JPanel());

			informationTextAreas.get(i).setWrapStyleWord(true);

			Dimension dimension = new Dimension(100, 120);
			informationTextAreas.get(i).setPreferredSize(dimension);
			informationTextAreas.get(i).setMinimumSize(dimension);
			informationTextAreas.get(i).setMaximumSize(dimension);
			informationTextAreaPanels.get(i).setPreferredSize(dimension);
			informationTextAreaPanels.get(i).setMinimumSize(dimension);
			informationTextAreaPanels.get(i).setMaximumSize(dimension);
			informationTextAreaPanels.get(i).add(informationTextAreas.get(i));

			informationTextAreas.get(i).setWrapStyleWord(true);
			informationTextAreas.get(i).setBackground(SystemColor.control);
			informationTextAreas.get(i).setEditable(false);
		}

		informationTextAreas.get(2).addMouseListener(new CalculationBreakdownListener(false, true, true));
		informationTextAreas.get(3).addMouseListener(new CalculationBreakdownListener(false, false, true));
		informationTextAreas.get(4).addMouseListener(new CalculationBreakdownListener(false, true, false));
		informationTextAreas.get(5).addMouseListener(new CalculationBreakdownListener(false, false, false));

		informationTextAreas.get(6).setWrapStyleWord(true);
		informationTextAreas.get(6).setLineWrap(true);
		jsp = new JScrollPane(informationTextAreas.get(6));
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setPreferredSize(new Dimension(370, 150));
		informationTextAreas.get(6).setText("Your Airport: \"" + airportName + "\" was successfully loaded.\nWelcome to the Runway Re-Declaration Tool.\n\n");

		GridBagConstraints gbc = new GridBagConstraints();

		int spacing = 15;
		gbc.insets = new Insets(spacing, spacing, spacing, spacing);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.BOTH;
		calculationsOutputPanel.add(images.get(0), gbc);
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.weighty = 1;
		calculationsOutputPanel.add(informationTextAreaPanels.get(0), gbc);
		gbc.gridx++;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		calculationsOutputPanel.add(images.get(1), gbc);
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.weighty = 1;
		calculationsOutputPanel.add(informationTextAreaPanels.get(1), gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		calculationsOutputPanel.add(images.get(2), gbc);
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.weighty = 1;
		calculationsOutputPanel.add(informationTextAreaPanels.get(2), gbc);
		gbc.gridx++;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		calculationsOutputPanel.add(images.get(3), gbc);
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.weighty = 1;
		calculationsOutputPanel.add(informationTextAreaPanels.get(3), gbc);
		gbc.gridx++;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		calculationsOutputPanel.add(images.get(4), gbc);
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.weighty = 1;
		calculationsOutputPanel.add(informationTextAreaPanels.get(4), gbc);
		gbc.gridx++;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		calculationsOutputPanel.add(images.get(5), gbc);
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.weighty = 1;
		calculationsOutputPanel.add(informationTextAreaPanels.get(5), gbc);

		gbc.weightx = 2;
		gbc.weighty = 5;
		gbc.gridwidth = 4;
		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 6;
		calculationsOutputPanel.add(jsp, gbc);

		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 1;
		gbc.weighty = 3;
		calculationsOutputPanel.add(Box.createVerticalStrut(20), gbc);

		this.changeRunway(this.runway);

		// use this panel for all your GUI stuff
		content.add(panel, BorderLayout.EAST);
		content.add(visPanel, BorderLayout.WEST);
		// content.add(buttons, BorderLayout.NORTH);
	}

	public JToolBar createToolBar() {
		JToolBar tb = new JToolBar("hi");
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		String[] icons = { "newairport.png", "newobstacle.png", "editobstacle.png", "importobstacle.png", "exportobstacle.png", "reset.png", "console.png", "help.png", "reverse_plane_direction.png", "toggle_landing.png" };
		String[] tooltips = { LocalizationService.localizeString("new_airport"), LocalizationService.localizeString("new_obstr"), LocalizationService.localizeString("edit_obstr"), LocalizationService.localizeString("import_obstr"), LocalizationService.localizeString("export_obstr"), LocalizationService.localizeString("reset_vis"), LocalizationService.localizeString("console"), LocalizationService.localizeString("manual"), LocalizationService.localizeString("reverse_plane_direction"), LocalizationService.localizeString("toggle_landing") };
		ImageIcon[] imageIcons = new ImageIcon[icons.length];
		JButton[] buttons = new JButton[tooltips.length];
		for (int i = 0; i < tooltips.length; ++i) {
			imageIcons[i] = new ImageIcon(((new ImageIcon("assets/data/Swing/" + icons[i])).getImage()).getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH));
			buttons[i] = new JButton(imageIcons[i]);
			buttons[i].setToolTipText(tooltips[i]);
			if (i == 1 || i == 5)
				tb.addSeparator();
			tb.add(buttons[i]);
		}

		// Add Listeners to ImageIcons
		buttons[0].addActionListener(new NewAirportListener());
		buttons[1].addActionListener(new AddObstructionListener());
		buttons[2].addActionListener(new EditObstructionListener());
		buttons[3].addActionListener(new ImportObstructionListener());
		buttons[4].addActionListener(new ExportObstructionListener());
		buttons[5].addActionListener(new ResetVisualisationListener());
		buttons[6].addActionListener(new OpenConsoleListener());
		buttons[7].addActionListener(new UserManualListener());
		buttons[8].addActionListener(new ToggleDirectionListener());
		buttons[9].addActionListener(new ToggleLandingListener());

		tb.setFloatable(false);
		return tb;
	}

	// creates the Menu bar used in the GUI
	public JMenuBar createMenuBar() {
		JMenuBar jmb = new JMenuBar();
		Font menuFont = new Font("Arial", Font.PLAIN, 14);

		// creates JMenu objects and formats them
		JMenu file = new JMenu(LocalizationService.localizeString("file"));

		JMenu runway = new JMenu(LocalizationService.localizeString("runway"));
		JMenu obstruction = new JMenu(LocalizationService.localizeString("obstruction"));
		JMenu view = new JMenu(LocalizationService.localizeString("view"));
		JMenu help = new JMenu(LocalizationService.localizeString("help"));

		Arrays.asList(file, runway, obstruction, view, help).forEach(s -> {
			s.getPopupMenu().setLightWeightPopupEnabled(false);
			s.setFont(menuFont);
			s.setBorder(new EmptyBorder(8, 10, 8, 12));
			jmb.add(s);
		});

		// creates JMenuItems/JMenu for file
		newAirport = new JMenuItem(LocalizationService.localizeString("new_airport"));
		JMenu changeLanguage = new JMenu(LocalizationService.localizeString("change_lang"));
		exit = new JMenuItem(LocalizationService.localizeString("exit"));

		// creates JMenuItems for runway
		JMenuItem createRunway = new JMenuItem(LocalizationService.localizeString("create_runway"));
		JMenuItem importRunway = new JMenuItem(LocalizationService.localizeString("import_runway"));
		JMenuItem exportRunway = new JMenuItem(LocalizationService.localizeString("export_runway"));
		JMenuItem exportCalculations = new JMenuItem("Export Calculations");

		// creates JMenuItems for obstruction
		createObstruction = new JMenuItem(LocalizationService.localizeString("new_obstr"));
		JMenuItem editObstruction = new JMenuItem(LocalizationService.localizeString("edit_obstr"));
		JMenuItem importObstruction = new JMenuItem(LocalizationService.localizeString("import_obstr"));
		JMenuItem exportObstruction = new JMenuItem(LocalizationService.localizeString("export_obstr"));

		// creates JMenuItems for view
		JMenuItem resetVisualisation = new JMenuItem(LocalizationService.localizeString("reset_vis"));
		exportVisualisation = new JMenuItem(LocalizationService.localizeString("export_vis"));
		JMenu screenShot = new JMenu(LocalizationService.localizeString("screenshot"));
		JMenuItem changeColourScheme = new JMenuItem(LocalizationService.localizeString("colour_scheme"));
		openConsole = new JMenuItem(LocalizationService.localizeString("console"));

		// creates JMenuItem for screen shotting in different formats - adds
		// them to JMenu
		screenShotJPEG = new JMenuItem("Save as JPEG");
		screenShotPNG = new JMenuItem("Save as PNG");
		screenShotGIF = new JMenuItem("Save as GIF");

		screenShot.add(screenShotJPEG);
		screenShot.add(screenShotPNG);
		screenShot.add(screenShotGIF);

		// creates JMenuItems for help
		JMenuItem userManual = new JMenuItem(LocalizationService.localizeString("manual"));
		JMenuItem about = new JMenuItem(LocalizationService.localizeString("about"));

		// adds appropriate JMenuItem objects to JMenu objects
		file.add(newAirport);
		file.add(changeLanguage);
		file.add(exit);

		runway.add(createRunway);
		runway.add(importRunway);
		runway.add(exportRunway);
		runway.add(exportCalculations);

		obstruction.add(createObstruction);
		obstruction.add(editObstruction);
		obstruction.add(importObstruction);
		obstruction.add(exportObstruction);

		view.add(resetVisualisation);
		view.add(exportVisualisation);
		view.add(screenShot);
		view.add(changeColourScheme);
		view.add(openConsole);

		help.add(userManual);
		help.add(about);

		// creates JCheckBoxMenuItem for languages
		ButtonGroup languages = new ButtonGroup();
		JCheckBoxMenuItem languageOne = new JCheckBoxMenuItem(LocalizationService.localizeString("eng"));
		JCheckBoxMenuItem languageTwo = new JCheckBoxMenuItem(LocalizationService.localizeString("pol"));
		JCheckBoxMenuItem languageThree = new JCheckBoxMenuItem(LocalizationService.localizeString("spa"));
		languageOne.addActionListener(new langListener("EN"));
		languageTwo.addActionListener(new langListener("PL"));
		languageThree.addActionListener(new langListener("ES"));

		// adds button to ButtonGroup
		languages.add(languageOne);
		languages.add(languageTwo);
		languages.add(languageThree);

		// adds the languages to the JMenu
		changeLanguage.add(languageOne);
		changeLanguage.add(languageTwo);
		changeLanguage.add(languageThree);

		// sets the default language
		if (LocalizationService.getLanguage() == "EN") {
			languageOne.setSelected(true);
		} else if (LocalizationService.getLanguage() == "PL") {
			languageTwo.setSelected(true);
		} else {
			languageThree.setSelected(true);
		}
		// adds listeners to JMenuItems
		newAirport.addActionListener(new NewAirportListener());
		exit.addActionListener(new ExitListener());

		createRunway.addActionListener(new ChangeRunwayListener());
		importRunway.addActionListener(new ImportRunwayListener());
		exportRunway.addActionListener(new ExportRunwayListener());
		exportCalculations.addActionListener(new ExportCalculationsListener());

		createObstruction.addActionListener(new AddObstructionListener());
		editObstruction.addActionListener(new EditObstructionListener());
		importObstruction.addActionListener(new ImportObstructionListener());
		exportObstruction.addActionListener(new ExportObstructionListener());

		resetVisualisation.addActionListener(new ResetVisualisationListener());
		exportVisualisation.addActionListener(new ExportVisualisationListener());
		screenShotJPEG.addActionListener(new ScreenshotListener("JPEG"));
		screenShotPNG.addActionListener(new ScreenshotListener("PNG"));
		screenShotGIF.addActionListener(new ScreenshotListener("GIF"));
		changeColourScheme.addActionListener(new ColourSchemeListener());
		openConsole.addActionListener(new OpenConsoleListener());

		userManual.addActionListener(new UserManualListener());
		about.addActionListener(new AboutListener());

		return jmb;
	}

	public void changeColourScheme(ArrayList<Color> newColors) {
		calculationsOutputPanel.setBackground(newColors.get(0));

		for (int i = 0; i < informationTextAreas.size(); i += 1) {
			informationTextAreas.get(i).setBackground(newColors.get(1));
			informationTextAreaPanels.get(i).setBackground(newColors.get(0));
		}
		curretColors = newColors;
	}

	public ArrayList<Color> getColourScheme() {
		return curretColors;
	}

	public void changeLanguage(String code) {
		int reply = JOptionPane.showConfirmDialog(null, LocalizationService.localizeString("restart_prog"), LocalizationService.localizeString("new_airport"), JOptionPane.YES_NO_OPTION);

		if (reply == JOptionPane.YES_OPTION) {
			LocalizationService.setLanguage(code);
			gui.dispose();
			WelcomeGUI wg = new WelcomeGUI();
			wg.init();

		} else {
		}
	}

	public void addUserNotification(String s) {
		Date date = new Date();
		SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
		String time = sdftime.format(date);

		this.informationTextAreas.get(6).append(time + ": " + s + "\n");
	}

	public void addToLogs(String s) {

		// get todays log file
		SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String today = sdfdate.format(date);
		String filename = today + ".log";

		// creates timestamp
		SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
		String time = sdftime.format(date);

		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));

			pw.println(time + s);
			pw.close();

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void openObstructionEditor() {
		try {
			NewObstructionGUI nog = new NewObstructionGUI(runway, this, OBSTRUCTION_FILE_NAME);
			nog.init("");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	// used when editing a currently selected obstruction
	public void openEditObstructionEditor() {
		new EditObstructionGUI(currentObstruction, this);
	}

	// opens the console
	public void openConsole() {
		new ConsoleGUI(runway, this);
	}

	public void openRunwayEditor() {
		try {
			SelectRunwayGUI srg = new SelectRunwayGUI(runway, this, RUNWAY_FILE_NAME);
			srg.init("");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void changeRunway(Runway newRunway) {
		this.runway = newRunway;
		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				vis.update(runway);
			}
		});

		// change original parameter values
		LogicalRunway[] originalParameters = new LogicalRunway[2];
		originalParameters[0] = runway.getOriginalLogicalRunwayFromLeft();
		originalParameters[1] = runway.getOriginalLogicalRunwayFromRight();
		for (int i = 0; i < 2; i++) {
			try {
				informationTextAreas.get(i).setText("RESA: " + originalParameters[i].resa + "\nLDA: " + originalParameters[i].lda + "\nTODA: " + originalParameters[i].toda + "\nTORA: " + originalParameters[i].tora + "\nASDA: " + originalParameters[i].asda);

			} catch (Exception e) {
				informationTextAreas.get(i).setText(LocalizationService.localizeString("no_runway_info"));
			}
		}
	}

	// used so current obstructions can be edited
	public void setCurrentObstruction(Obstruction curr) {
		this.currentObstruction = curr;
	}

	// clear the text areas - except notification panel
	public void clearCalculations() {
		for (int i = 0; i < (informationTextAreas.size() - 1); i++) {
			informationTextAreas.get(i).setText("");
		}
	}

	/****** LISTENERS FOR JMENU - KEEP INSIDE STARRED AREA FOR NOW ******/

	// listener used on the JMenuBar to start a new Airport
	public class NewAirportListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			int reply = JOptionPane.showConfirmDialog(null, LocalizationService.localizeString("restart_prog"), LocalizationService.localizeString("new_airport"), JOptionPane.YES_NO_OPTION);

			if (reply == JOptionPane.YES_OPTION) {
				gui.dispose();
				WelcomeGUI wg = new WelcomeGUI();
				wg.init();
			} else {
			}
		}
	}

	// listener used on JMenuBar to close the program
	public class ExitListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			String stringMessage = LocalizationService.localizeString("exit_msg");

			int reply = JOptionPane.showConfirmDialog(null, stringMessage, LocalizationService.localizeString("exit"), JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {

				gui.dispose();

			} else {
			}
		}
	}

	// listener for Change Runway button & JMenu Option
	public class ChangeRunwayListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			openRunwayEditor();
		}
	}

	// listener for import runway(s) JMenu option
	public class ImportRunwayListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			try {

				SelectRunwayGUI srg = new SelectRunwayGUI(runway, gui, RUNWAY_FILE_NAME);

				srg.init("click import button"); // wtf is this

			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}

		}
	}

	// listener used on JMenu
	public class ExportRunwayListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			new ExportRunwayGUI(RUNWAY_FILE_NAME, gui);
		}

	}

	// listener used on JMenu
	public class ExportCalculationsListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {

			// Runway Details
			String runwayName = "Runway Name: " + runway.getRunwayName();
			String runwayID = "Runway ID: " + runway.getRunwayID();

			// Obstruction Details
			String obName = "Obstruction Name: " + currentObstruction.getName();
			String obWidth = "Obstruction Width: " + currentObstruction.getWidth() + "";
			String obHeight = "Obstruction Height: " + currentObstruction.getHeight() + "";
			String obLength = "Obstruction Length: " + currentObstruction.getLength() + "";
			String posX = "Obstruction Position Along Runway: " + currentObstruction.getPositionAlongRunway() + "";
			String posY = "Obstruction Distance From Runway: " + currentObstruction.getDistanceFromRunway() + "";

			// left original variables
			String leftOriginalRESA = "RESA: " + runway.getOriginalLogicalRunwayFromLeft().resa;
			String leftOriginalLDA = "LDA: " + runway.getOriginalLogicalRunwayFromLeft().lda;
			String leftOriginalTODA = "TODA: " + runway.getOriginalLogicalRunwayFromLeft().toda;
			String leftOriginalTORA = "TORA: " + runway.getOriginalLogicalRunwayFromLeft().tora;
			String leftOriginalASDA = "ASDA: " + runway.getOriginalLogicalRunwayFromLeft().asda;

			// right original variables
			String rightOriginalRESA = "RESA: " + runway.getOriginalLogicalRunwayFromRight().resa;
			String rightOriginalLDA = "LDA: " + runway.getOriginalLogicalRunwayFromRight().lda;
			String rightOriginalTODA = "TODA: " + runway.getOriginalLogicalRunwayFromRight().toda;
			String rightOriginalTORA = "TORA: " + runway.getOriginalLogicalRunwayFromRight().tora;
			String rightOriginalASDA = "ASDA: " + runway.getOriginalLogicalRunwayFromRight().asda;

			// String that makes up the content that is going to be exported
			String runwayDetails = runwayName + "\n" + runwayID + "\n";
			String obstructionDetails = obName + "\n" + obWidth + "\n" + obHeight + "\n" + obLength + "\n" + posX + "\n" + posY + "\n";
			String leftOriginal = leftOriginalRESA + "\n" + leftOriginalLDA + "\n" + leftOriginalTODA + "\n" + leftOriginalTORA + "\n" + leftOriginalASDA + "\n";
			String rightOriginal = rightOriginalRESA + "\n" + rightOriginalLDA + "\n" + rightOriginalTODA + "\n" + rightOriginalTORA + "\n" + rightOriginalASDA + "\n";
			String approachLeftLand = runway.getLeftLandLogicalRunway().getBreakdown();
			String approachLeftTakeOff = runway.getLeftTakeOffLogicalRunway().getBreakdown();
			String approachRightLand = runway.getRightLandLogicalRunway().getBreakdown();
			String approachRightTakeOff = runway.getRightLandLogicalRunway().getBreakdown();

			String content = "------------------------------\n" + "Runway Details:\n\n" + runwayDetails + "\n------------------------------\n" + "Obstruction Details:\n\n" + obstructionDetails + "\n------------------------------\n" + "Original Parameters from Left:\n\n" + leftOriginal + "\n------------------------------\n" + "Original Parameters from Right:\n\n" + rightOriginal + "\n------------------------------\n" + "Approach From Left Landing:\n\n" + approachLeftLand + "\n------------------------------\n" + "Approach From Right Landing:\n\n" + approachRightLand + "\n------------------------------\n" + "Take off From Left:\n\n" + approachLeftTakeOff + "\n------------------------------\n" + "Take off From Right:\n\n" + approachRightTakeOff + "\n------------------------------\n";

			new ExportCalculationsGUI(gui, content);

		}
	}

	// listener used on JMenuBar to open the obstruction editor, also used on
	// GUI button
	public class EditObstructionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (currentObstruction.getName().equals("Empty Obstruction")) {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("no_obstr_present"), LocalizationService.localizeString("no_curr_obstr"), JOptionPane.INFORMATION_MESSAGE);
			} else {
				openEditObstructionEditor();
			}
		}
	}

	// listener for import obstruction(s) JMenu option
	public class ImportObstructionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			try {
				NewObstructionGUI nog = new NewObstructionGUI(runway, gui, OBSTRUCTION_FILE_NAME);
				nog.init("import");

			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Used to change the colours scheme in the GUI
	public class ColourSchemeListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			ColourSchemeGUI colorGui = new ColourSchemeGUI(gui);
			colorGui.init();
		}
	}

	// listener used on JMenu
	public class ExportObstructionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			new ExportObstructionGUI(OBSTRUCTION_FILE_NAME, gui);
		}
	}

	// Used on JMenuBar to reset the visualisation back to the original start-up
	// visualisation
	public class ResetVisualisationListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// remove the obstruction
			currentObstruction = new Obstruction("Empty Obstruction", 0.0, 0.0, 0.0, 0.0, 0.0);

			// clear the text areas - except notification panel
			clearCalculations();

			// clear visualisation from panning, zoom etc.
			vis.panels[0].rotation = 0;
			vis.panels[0].panLevel = 0;
			vis.panels[0].tiltLevel = 0;
			vis.panels[0].zoomLevel = 1;

			// create the original runway again and its fake obstruction
			Runway runway = new Runway("Example", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);

			Obstruction o = new Obstruction("Empty Obstruction", 0.0, 0.0, 0.0, 0.0, 0.0);

			// changing the runway back to the 'example' runway
			gui.runway.addObstruction(o);
			gui.addUserNotification("Visualisation was reset");
			gui.changeRunway(runway);
		}
	}

	// Used on JMenuBar to export an image of the visualisation
	public class ExportVisualisationListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			vis.saveScreenshot();
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("vis_export_ok"), LocalizationService.localizeString("vis_export_tit"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Used on JMenu to export an image of the full screen
	public class ScreenshotListener implements ActionListener {

		String fileType;

		public ScreenshotListener(String filetype) {
			this.fileType = filetype;
		}

		public void actionPerformed(ActionEvent arg0) {
			ScreenshotFactory.saveFullScreenshot(fileType);
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("screen_export_ok"), LocalizationService.localizeString("screen_export_tit"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// listener usedto open the console for user help
	public class OpenConsoleListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openConsole();
		}
	}

	public class ToggleDirectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			vis.toggleIsFromLeft();
			gui.addUserNotification("The Plane's direction was changed");
		}
	}

	public class ToggleLandingListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			vis.toggleIsLanding();
		}
	}

	// Listener used on JMenu for 'About' option
	public class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			new AboutGUI();
		}
	}

	/*******************************************************************/

	public class AddObstructionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			openObstructionEditor();
		}

	}

	public class CalculationBreakdownListener extends MouseAdapter {

		String breakdown = "";
		Boolean originalParameters, approachFromLeft, landing;

		public CalculationBreakdownListener(Boolean originalParameters, Boolean approachFromLeft, Boolean landing) {
			this.originalParameters = originalParameters;
			this.approachFromLeft = approachFromLeft;
			this.landing = landing;
		}

		private void getBreakdown() {
			if (originalParameters) {
				this.breakdown = runway.getOriginalLogicalRunwayFromLeft().getBreakdown();
			} else {
				if (approachFromLeft) {
					this.breakdown = landing ? runway.getLeftLandLogicalRunway().getBreakdown() : runway.getLeftTakeOffLogicalRunway().getBreakdown();
				} else {
					this.breakdown = landing ? runway.getRightLandLogicalRunway().getBreakdown() : runway.getRightTakeOffLogicalRunway().getBreakdown();
				}

			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			getBreakdown();

			if (calculationBreakdown == null || !calculationBreakdown.isVisible()) {
				calculationBreakdown = new JDialog();
				calculationBreakdown.setModal(true);
				calculationBreakdown.setUndecorated(true);
				JPanel panel = new JPanel();
				JButton close = new JButton("Close");
				close.addMouseListener(new calculationBreakdownCloseListener());
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new calculationBreakdownEscapeListener());
				JTextArea jta = new JTextArea(breakdown);
				jta.setEditable(false);
				panel.add(jta);
				panel.add(close);
				panel.setBackground(new Color(86, 86, 86));
				calculationBreakdown.setContentPane(panel);
				calculationBreakdown.setIconImage(gui.getIconImage());
				calculationBreakdown.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				calculationBreakdown.pack();
				/* calculationBreakdown.setLocation(new Point(((int) (MouseInfo
				 * .getPointerInfo().getLocation()).getX()) - ((int)
				 * (calculationBreakdown.getWidth() * 0.5)), ((int)
				 * (MouseInfo.getPointerInfo().getLocation()) .getY()) - 20)); */
				calculationBreakdown.setLocationRelativeTo(null);
				calculationBreakdown.setVisible(true);
			}
		}
	}

	public void closeBreakdowns() {
		if (calculationBreakdown != null) {
			calculationBreakdown.setVisible(false);
			calculationBreakdown.dispose();
			calculationBreakdown = null;
		}
	}

	public class langListener implements ActionListener {
		private String code;

		public langListener(String code) {
			this.code = code;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			changeLanguage(code);
		}
	}

	public class calculationBreakdownCloseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			closeBreakdowns();
		}
	}

	public class calculationBreakdownEscapeListener implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				closeBreakdowns();
			}
			return false;
		}
	}

	public class myWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {

			/* Add to Log file */

			// get todays log file
			SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String today = sdfdate.format(date);
			String filename = today + ".log";

			// creates timestamp
			SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
			String time = sdftime.format(date);
			// creates String containing content
			String content = time + "      Runway Re-Declaration Tool Closed.";

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));

				// print + close
				pw.println(content);
				pw.close();

			} catch (Exception exc) {
				exc.printStackTrace();
			}
			gui.dispose();
		}
	}

	public class MyKeyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {

			// Open Obstruction GUI: X
			if ((e.getKeyCode() == KeyEvent.VK_X) && gui.isActive()) {
				createObstruction.doClick();
				// Open Console: C
			} else if ((e.getKeyCode() == KeyEvent.VK_C) && gui.isActive()) {
				openConsole.doClick();
				// Export Visualisation: PrtSc
			} else if ((e.getKeyCode() == KeyEvent.VK_PRINTSCREEN) && (e.getModifiers() == 0) && gui.isActive()) {
				exportVisualisation.doClick();
				// New Airport: Ctrl + N
			} else if ((e.getKeyCode() == KeyEvent.VK_N) && (e.getModifiers() == KeyEvent.CTRL_MASK) && gui.isActive()) {
				newAirport.doClick();
				// Exit Application: SHIFT + Esc
			} else if ((e.getKeyCode() == KeyEvent.VK_ESCAPE) && (e.getModifiers() == KeyEvent.SHIFT_MASK) && gui.isActive()) {
				exit.doClick();
			}
			return false;
		}
	}
}
