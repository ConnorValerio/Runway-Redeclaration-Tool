package Views;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Controllers.Obstruction;
import Controllers.Runway;
import Listeners.EscapeListener;
import Models.LogicalRunway;
import Services.LocalizationService;
import Services.XMLObstructionService;

public class NewObstructionGUI {

	// where custom obstruction will be exported to
	String OBSTRUCTION_FILE_NAME;

	JDialog frame;

	JComboBox<String> preDefinedCombo;
	ArrayList<Obstruction> preDefined;
	ArrayList<String> comboListNames = new ArrayList<String>();

	Runway runway;

	JTextArea info;
	JScrollPane jsp;

	JCheckBox tb1;

	JTextField obstructionNameTextField, obstructionLengthTextField, obstructionWidthTextField, obstructionHeightTextField, obstructionPositionAlongRunwayTextField, obstructionDistanceFromRunwayTextField;

	JLabel obstructionNameLabel, obstructionLengthLabel, obstructionWidthLabel, obstructionHeightLabel, obstructionPositionAlongRunway, obstructionDistanceFromRunway;

	MouseListener mouseLength, mouseWidth, mouseHeight, mousePosAlongRunway, mouseDistFromRunway, mouseRotation, mouseHighPointX, mouseHighPointY;

	JButton importButton;

	GUI gui;

	public NewObstructionGUI(Runway runway, GUI gui, String obstructionSource) throws ParserConfigurationException, SAXException, IOException {
		this.runway = runway;
		this.gui = gui;
		this.OBSTRUCTION_FILE_NAME = obstructionSource;
		preDefined = new ArrayList<Obstruction>();

		// import the obstructions from the obstruction source file
		XMLObstructionService obstructionService = new XMLObstructionService();

		// Make custom obstruction hard coded in program so it doesn't rely on
		// user
		// having to include it in their XML file

		ArrayList<Obstruction> tmpPreDefined = obstructionService.importObstructions(OBSTRUCTION_FILE_NAME);

		// create Custom Obstruction
		Obstruction custom = new Obstruction("Custom Obstruction", 0.0, 0.0, 0.0, 0.0, 0.0);

		preDefined.add(custom);
		comboListNames.add(custom.getName());

		for (int i = 0; i < tmpPreDefined.size(); i++) {
			preDefined.add(tmpPreDefined.get(i));
			comboListNames.add(tmpPreDefined.get(i).getName());
		}
	}

	// parameter is to bypass the modality of the JDialog - allows the Import
	// Obstruction JMenu Option to work effectively
	public void init(String importOrNot) {

		frame = new JDialog();
		frame.setTitle("Create/Add Obstruction");
		JPanel main = new JPanel(new FlowLayout());

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new EscapeListener(frame));

		// import and set the frame icon
		frame.setIconImage(new ImageIcon("src\\assets\\data\\logo.png").getImage());

		frame.setContentPane(main);
		frame.addWindowListener(new MyWindowListener());

		preDefinedCombo = new JComboBox(comboListNames.toArray());
		preDefinedCombo.addItemListener(new ComboBoxItemListener());
		preDefinedCombo.addActionListener(new ComboBoxActionListener());

		obstructionNameLabel = new JLabel(LocalizationService.localizeString("obstr_name"));
		obstructionLengthLabel = new JLabel(LocalizationService.localizeString("obstr_length"));
		obstructionWidthLabel = new JLabel(LocalizationService.localizeString("obstr_width"));
		obstructionHeightLabel = new JLabel(LocalizationService.localizeString("obstr_height"));
		obstructionPositionAlongRunway = new JLabel(LocalizationService.localizeString("obstr_along"));
		obstructionDistanceFromRunway = new JLabel(LocalizationService.localizeString("obstr_from"));

		Obstruction o = runway.getFirstObstruction();

		if (o == null) {

			obstructionNameTextField = new JTextField(10);
			obstructionLengthTextField = new JTextField(10);
			obstructionWidthTextField = new JTextField(10);
			obstructionHeightTextField = new JTextField(10);

			obstructionPositionAlongRunwayTextField = new JTextField(10);
			obstructionDistanceFromRunwayTextField = new JTextField(10);
		} else {

			obstructionNameTextField = new JTextField(o.getName(), 10);
			obstructionLengthTextField = new JTextField(Double.toString(o.getLength()), 10);
			obstructionWidthTextField = new JTextField(Double.toString(o.getWidth()), 10);
			obstructionHeightTextField = new JTextField(Double.toString(o.getHeight()), 10);

			obstructionPositionAlongRunwayTextField = new JTextField(Double.toString(o.getPositionAlongRunway()), 10);
			obstructionDistanceFromRunwayTextField = new JTextField(Double.toString(o.getDistanceFromRunway()), 10);
		}

		importButton = new JButton(LocalizationService.localizeString("import_obstr"));
		info = new JTextArea(LocalizationService.localizeString("override_warning"));
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);

		// autoscroll to bottom
		info.setCaretPosition(info.getDocument().getLength());

		jsp = new JScrollPane(info);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setPreferredSize(new Dimension(425, 70));

		// top panel
		JPanel top = new JPanel(new FlowLayout());
		preDefinedCombo.setPreferredSize(new Dimension(210, 30));
		importButton.setPreferredSize(new Dimension(210, 30));
		top.add(preDefinedCombo);
		top.add(importButton);

		// main content panel
		JPanel contentPanel = new JPanel(new GridLayout(6, 2, 10, 5));

		contentPanel.add(obstructionNameLabel);
		contentPanel.add(obstructionNameTextField);
		contentPanel.add(obstructionWidthLabel);
		contentPanel.add(obstructionWidthTextField);
		contentPanel.add(obstructionHeightLabel);
		contentPanel.add(obstructionHeightTextField);
		contentPanel.add(obstructionLengthLabel);
		contentPanel.add(obstructionLengthTextField);
		contentPanel.add(obstructionPositionAlongRunway);
		contentPanel.add(obstructionPositionAlongRunwayTextField);
		contentPanel.add(obstructionDistanceFromRunway);
		contentPanel.add(obstructionDistanceFromRunwayTextField);

		// warning message panel
		JPanel warning = new JPanel(new FlowLayout());
		warning.setBorder(new EmptyBorder(10, 5, 0, 10));
		warning.add(jsp);

		// bottom panel - buttons
		JPanel bottom = new JPanel(new GridLayout(1, 2, 10, 5));
		JButton setObstruction = new JButton(LocalizationService.localizeString("save_set_obstr"));
		JButton exportButton = new JButton(LocalizationService.localizeString("save_export_obstr"));
		setObstruction.setPreferredSize(new Dimension(210, 30));
		exportButton.setPreferredSize(new Dimension(210, 30));

		bottom.add(setObstruction);
		bottom.add(exportButton);

		main.setBorder(new EmptyBorder(10, 10, 10, 10));
		main.add(top);
		main.add(contentPanel);
		main.add(warning);
		main.add(bottom);

		// Setting Colors
		ArrayList<Color> backgroundColors = gui.getColourScheme();

		setObstruction.setBackground(backgroundColors.get(2));
		exportButton.setBackground(backgroundColors.get(2));
		importButton.setBackground(backgroundColors.get(2));

		main.setBackground(backgroundColors.get(0));
		top.setBackground(backgroundColors.get(0));
		contentPanel.setBackground(backgroundColors.get(0));
		warning.setBackground(backgroundColors.get(0));
		bottom.setBackground(backgroundColors.get(0));

		obstructionNameTextField.setBackground(backgroundColors.get(1));

		obstructionLengthTextField.setBackground(backgroundColors.get(1));

		obstructionWidthTextField.setBackground(backgroundColors.get(1));

		obstructionHeightTextField.setBackground(backgroundColors.get(1));

		obstructionPositionAlongRunwayTextField.setBackground(backgroundColors.get(1));
		obstructionDistanceFromRunwayTextField.setBackground(backgroundColors.get(1));
		info.setBackground(backgroundColors.get(1));

		// add listeners
		mouseLength = new TextFieldSlider(obstructionLengthTextField);
		mouseWidth = new TextFieldSlider(obstructionWidthTextField);
		mouseHeight = new TextFieldSlider(obstructionHeightTextField);
		mousePosAlongRunway = new TextFieldSlider(obstructionPositionAlongRunwayTextField);
		mouseDistFromRunway = new TextFieldSlider(obstructionDistanceFromRunwayTextField);

		obstructionLengthLabel.addMouseListener(mouseLength);
		obstructionWidthLabel.addMouseListener(mouseWidth);
		obstructionHeightLabel.addMouseListener(mouseHeight);
		obstructionPositionAlongRunway.addMouseListener(mousePosAlongRunway);
		obstructionDistanceFromRunway.addMouseListener(mouseDistFromRunway);

		setObstruction.addActionListener(new SetObstructionListener());
		exportButton.addActionListener(new ExportObstructionListener());
		importButton.addActionListener(new ImportObstructionsListener());

		// makes sure the first entry in the combo list is selected
		preDefinedCombo.setSelectedIndex(0);

		// knows whether to open up the import obstructions pop-up before
		// setting the modality of this JDialog
		if (importOrNot.equals("import")) {
			this.importButton.doClick();
		}

		// formats and makes the GUI
		frame.setModal(true);
		frame.setPreferredSize(new Dimension(490, 435));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	// used when setting an obstruction or exporting one - validates each field
	// to
	// ensure the correct type of data has been entered.
	public boolean validateInput() {

		// creates variables for each input field, ready for validation

		String name = obstructionNameTextField.getText();
		String length = obstructionLengthTextField.getText();
		String width = obstructionWidthTextField.getText();
		String height = obstructionHeightTextField.getText();
		String posX = obstructionPositionAlongRunwayTextField.getText();
		String posY = obstructionDistanceFromRunwayTextField.getText();

		// obstruction name validation
		if (!(name.matches("([a-zA-Z]+ +)*[a-zA-Z]+")) || name.equals("")) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_name"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// obstruction width validation
		if (!(isDouble(width))) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (Double.parseDouble(width) <= 0) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_pos_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// obstruction height validation
		if (!(isDouble(height))) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if ((Double.parseDouble(height)) <= 0) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_pos_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// obstruction length validation
		if (!(isDouble(length))) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (Double.parseDouble(length) <= 0) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_pos_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// obstruction Position X validation
		if (!(isDouble(posX))) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// obstruction Position Y validation
		if (!(isDouble(posY))) {
			JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_num"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}

	// checks if a String can be parsed into a Double
	public boolean isDouble(String input) {

		try {
			Double.parseDouble(input);
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

	// checks to see if the new obstruction being added has a unique name
	public boolean hasUniqueName(String obstructionName) {

		String checkObstruction = obstructionName.toLowerCase();

		for (int i = 0; i < this.comboListNames.size(); i++) {
			if (comboListNames.get(i).toLowerCase().equals(checkObstruction)) {
				return false;
			}
		}
		return true;
	}

	public class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			frame.dispose();
		}
	}

	public class ExportObstructionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			Obstruction o;

			// used to make sure if a custom obstruction is being created, a
			// unique
			// name is assigned to it.
			int myIndex = preDefinedCombo.getSelectedIndex();
			String obstructionCheck = comboListNames.get(myIndex);

			if (obstructionCheck.equals("Custom Obstruction")) {
				if (hasUniqueName(obstructionNameTextField.getText()) == false) {
					JOptionPane.showMessageDialog(null, LocalizationService.localizeString("unique_name"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}

			if (validateInput() == false) {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("unable_obstr"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
				return;

			} else {

				info.append("\n\n" + LocalizationService.localizeString("override_warning"));

				o = new Obstruction(obstructionNameTextField.getText(), Double.parseDouble(obstructionWidthTextField.getText()), Double.parseDouble(obstructionHeightTextField.getText()), Double.parseDouble(obstructionLengthTextField.getText()), Double.parseDouble(obstructionPositionAlongRunwayTextField.getText()), Double.parseDouble(obstructionDistanceFromRunwayTextField.getText()));

				// if a new obstruction is created, make it available in the
				// xml file for next time too.
				if (obstructionCheck.equals("Custom Obstruction")) {

					XMLObstructionService xmlExportObstruction = new XMLObstructionService();

					try {
						xmlExportObstruction.exportObstruction(o, OBSTRUCTION_FILE_NAME);

						comboListNames.add(o.getName());
						preDefinedCombo.addItem(o.getName());
						preDefined.add(o);

					} catch (ParserConfigurationException | TransformerException | SAXException | IOException e1) {
						e1.printStackTrace();
						return;
					}

				}

				frame.dispose();

			}

			// open up the ExportRunwayGUI so the user can export the Runway
			// they have just created (or another one)
			new ExportObstructionGUI(OBSTRUCTION_FILE_NAME, gui);
		}
	}

	public class ImportObstructionsListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			ArrayList<Obstruction> importedObstructions;

			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
			filechooser.setFileFilter(xmlFilter);
			filechooser.setAcceptAllFileFilterUsed(false);
			filechooser.setDialogTitle("Open XML- Obstruction File");

			int returnVal = filechooser.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				XMLObstructionService xmlService = new XMLObstructionService();

				// try import the chosen xml file
				try {

					importedObstructions = xmlService.importObstructions(file.getName());

					// add in the new obstructions that were imported if they
					// have a unique name AND export the new obstructions into
					// the obstruction source file so they are ready to be
					// accessed next time the program is loaded up
					for (int i = 0; i < importedObstructions.size(); i++) {
						if ((hasUniqueName(importedObstructions.get(i).getName()) == true)) {
							preDefined.add(importedObstructions.get(i));
							comboListNames.add(importedObstructions.get(i).getName());
							xmlService.exportObstruction(importedObstructions.get(i), OBSTRUCTION_FILE_NAME);
						}
					}

					// empty the combo list array so it can be repopulated
					preDefinedCombo.removeAllItems();

					// repopulate the combo list array
					for (int i = 0; i < comboListNames.size(); i++) {
						preDefinedCombo.addItem(comboListNames.get(i));
					}

					/* Add to Log file */

					// creates Strings containing content
					StringBuilder data = new StringBuilder();
					data.append("      Obstruction Imported from File: ");
					data.append(System.lineSeparator() + System.lineSeparator() + "              File Name: " + file.getName());

					gui.addToLogs(data.toString());
					gui.addUserNotification("Obstruction \"" + file.getName() + "\" Imported from File");

					/********* done with log file *********/

				} catch (ParserConfigurationException | SAXException | IOException | TransformerException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, LocalizationService.localizeString("not_obstr"), LocalizationService.localizeString("error"), JOptionPane.WARNING_MESSAGE);
				}

			}

		}

	}

	// need action and item listeners to solve text-slider bug and so that the
	// initial selected obstruction is the "Custom Obstruction".
	public class ComboBoxActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			String obname = (String) preDefinedCombo.getSelectedItem();

			// finds the selected obstruction
			for (int i = 0; i < preDefined.size(); i++) {
				if (preDefined.get(i).getName().equals(obname)) {

					Obstruction selected = preDefined.get(i);

					// updates the text fields according to the selected
					// obstruction
					obstructionNameTextField.setText(selected.getName());
					obstructionLengthTextField.setText(Double.toString(selected.getLength()));
					obstructionWidthTextField.setText(Double.toString(selected.getWidth()));
					obstructionHeightTextField.setText(Double.toString(selected.getHeight()));
					obstructionPositionAlongRunwayTextField.setText(Double.toString(selected.getPositionAlongRunway()));
					obstructionDistanceFromRunwayTextField.setText(Double.toString(selected.getDistanceFromRunway()));

					// if its not a custom obstruction, dont allow editing of
					// length, width or height
					if (!(selected.getName().equals("Custom Obstruction"))) {
						obstructionNameTextField.setEditable(false);
						obstructionLengthTextField.setEditable(false);
						obstructionWidthTextField.setEditable(false);
						obstructionHeightTextField.setEditable(false);

					} else {
						obstructionNameTextField.setEditable(true);
						obstructionLengthTextField.setEditable(true);
						obstructionWidthTextField.setEditable(true);
						obstructionHeightTextField.setEditable(true);
					}
				}
			}

		}

	}

	public class ComboBoxItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent event) {

			if (event.getStateChange() == ItemEvent.SELECTED) {

				String obname = (String) preDefinedCombo.getSelectedItem();

				// finds the selected obstruction
				for (int i = 0; i < preDefined.size(); i++) {
					if (preDefined.get(i).getName().equals(obname)) {
						Obstruction selected = preDefined.get(i);

						// remove the listeners from lenght, height and width
						// labels if
						// it isn't the custom obstruction that has been
						// selected
						if (!(selected.getName().equals("Custom Obstruction"))) {

							obstructionLengthLabel.removeMouseListener(mouseLength);
							obstructionWidthLabel.removeMouseListener(mouseWidth);
							obstructionHeightLabel.removeMouseListener(mouseHeight);

						} else {

							obstructionLengthLabel.addMouseListener(mouseLength);
							obstructionWidthLabel.addMouseListener(mouseWidth);
							obstructionHeightLabel.addMouseListener(mouseHeight);
						}
					}
				}
			}

		}
	}

	public class TextFieldSlider extends MouseAdapter {

		ValueUpdateThread vut;
		JTextField sender;

		public TextFieldSlider(JTextField sender) {
			this.sender = sender;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			vut.kill();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			vut = new ValueUpdateThread(sender);
			vut.start();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			frame.setCursor(Cursor.getDefaultCursor());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}
	}

	public class SetObstructionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			Obstruction o;

			// used to make sure if a custom obstruction is being created, a
			// unique
			// name is assigned to it.
			int myIndex = preDefinedCombo.getSelectedIndex();
			String obstructionCheck = comboListNames.get(myIndex);

			if (obstructionCheck.equals("Custom Obstruction")) {
				if (hasUniqueName(obstructionNameTextField.getText()) == false) {
					JOptionPane.showMessageDialog(null, "Please give your Obstruction a unique name.", LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}

			if (validateInput() == false) {
				JOptionPane.showMessageDialog(null, "Unable to Set your Obstruction.", "Obstruction Error", JOptionPane.INFORMATION_MESSAGE);
			} else {

				info.append("\n\n" + LocalizationService.localizeString("override_warning"));
				try {
					o = new Obstruction(obstructionNameTextField.getText(), Double.parseDouble(obstructionWidthTextField.getText()), Double.parseDouble(obstructionHeightTextField.getText()), Double.parseDouble(obstructionLengthTextField.getText()), Double.parseDouble(obstructionPositionAlongRunwayTextField.getText()), Double.parseDouble(obstructionDistanceFromRunwayTextField.getText()));

					runway.addObstruction(o);

					// if a new obstruction is created, make it available in the
					// xml file for next time too.
					if (obstructionCheck.equals("Custom Obstruction")) {
						XMLObstructionService xmlExportObstruction = new XMLObstructionService();
						xmlExportObstruction.exportObstruction(o, OBSTRUCTION_FILE_NAME);

						comboListNames.add(o.getName());
						preDefinedCombo.addItem(o.getName());
						preDefined.add(o);
					}

					// make it the 'current obstruction' so its position can be
					// edited by the user if they wish
					gui.setCurrentObstruction(o);

					/* Add to Log file */

					// creates Strings containing content
					StringBuilder data = new StringBuilder();
					data.append("      Obstruction Added: ");
					data.append(System.lineSeparator() + System.lineSeparator() + "Runway: " + runway.getRunwayName());
					data.append(System.lineSeparator() + System.lineSeparator() + "Obstruction Name: " + o.getName());

					gui.addToLogs(data.toString());

					/********* done with log file *********/

					String[] parameterNames = new String[6];
					runway.redeclareParameters();
					LogicalRunway[] lr = new LogicalRunway[6];

					parameterNames[0] = LocalizationService.localizeString("orig_param") + LocalizationService.localizeString("left_param");
					lr[0] = gui.runway.getOriginalLogicalRunwayFromLeft();

					parameterNames[1] = LocalizationService.localizeString("orig_param") + LocalizationService.localizeString("right_param");
					lr[1] = gui.runway.getOriginalLogicalRunwayFromRight();

					parameterNames[2] = LocalizationService.localizeString("landing_param") + LocalizationService.localizeString("left_param");
					lr[2] = gui.runway.getLeftLandLogicalRunway();

					parameterNames[3] = LocalizationService.localizeString("landing_param") + LocalizationService.localizeString("right_param");
					lr[3] = gui.runway.getRightLandLogicalRunway();

					parameterNames[4] = LocalizationService.localizeString("takeoff_param") + LocalizationService.localizeString("left_param");
					lr[4] = gui.runway.getLeftTakeOffLogicalRunway();

					parameterNames[5] = LocalizationService.localizeString("takeoff_param") + LocalizationService.localizeString("right_param");
					lr[5] = gui.runway.getRightTakeOffLogicalRunway();
					gui.takeOffRightBreakdown = lr[5].getBreakdown();

					for (int u = 0; u < parameterNames.length; u++) {
						gui.informationTextAreas.get(u).setText("");

						LogicalRunway values = lr[u];
						if (values.resa != 0.0)
							gui.informationTextAreas.get(u).append("\nRESA: " + values.resa);
						if (values.lda != 0.0)
							gui.informationTextAreas.get(u).append("\nLDA: " + values.lda);
						if (values.toda != 0.0)
							gui.informationTextAreas.get(u).append("\nTODA: " + values.toda);
						if (values.tora != 0.0)
							gui.informationTextAreas.get(u).append("\nTORA: " + values.tora);
						if (values.asda != 0.0)
							gui.informationTextAreas.get(u).append("\nASDA: " + values.asda);
						gui.informationTextAreas.get(u).append("\n\n");
					}

					/* Add to Log file */

					// creates Strings containing content
					StringBuilder data2 = new StringBuilder();
					data2.append("      New Obstruction Added: ");
					data2.append(System.lineSeparator() + System.lineSeparator() + "              Obstruction Name: " + obstructionNameTextField.getText());
					data2.append(System.lineSeparator() + "              Obstruction Length: " + obstructionLengthTextField.getText());
					data2.append(System.lineSeparator() + "              Obstruction Width: " + obstructionWidthTextField.getText());
					data2.append(System.lineSeparator() + "              Obstruction Height: " + obstructionHeightTextField.getText());
					data2.append(System.lineSeparator() + "              Obstruction Position X: " + obstructionPositionAlongRunwayTextField.getText());
					data2.append(System.lineSeparator() + "              Obstruction Position Y: " + obstructionDistanceFromRunwayTextField.getText());
					data2.append(System.lineSeparator());

					gui.addToLogs(data2.toString());
					gui.addUserNotification("New Obstruction \"" + o.getName() + "\" Added to runway \"" + runway.getRunwayName() + "\"");

					/********* done with log file *********/

					frame.dispose();

				} catch (Exception e1) {
					info.append("\n\nAn error occurred.");
				}
			}
		}
	}
}
