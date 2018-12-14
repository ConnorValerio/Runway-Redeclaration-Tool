package Views;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Controllers.*;
import Listeners.EscapeListener;
import Services.*;

public class SelectRunwayGUI {

	// where custom runway will be exported to
	String RUNWAY_FILE_NAME;

	JDialog frame;

	JComboBox<String> preDefinedCombo;
	ArrayList<Runway> preDefined;
	ArrayList<String> comboListNames = new ArrayList<String>();

	Runway runway;
	Runway customRunway;

	JTextArea info;
	JScrollPane jsp;

	JCheckBox tb1;

	JLabel runwayNameLabel;
	JLabel runwayIDLabel;
	JLabel runwayCommonParameters;
	JLabel runwayFromLeftLabel;
	JLabel runwayFromRightLabel;
	JLabel runwayTodaLabel;
	JLabel runwayLdaToraLabel;
	JLabel planeBlastDistanceLabel;
	JLabel runwayAsdaLabel;
	JLabel blastDistanceLabel;
	JLabel runwayDisplacedThresholdLabel;

	JTextField runwayNameTextField;
	JTextField runwayIDTextField;

	JTextField runwayLdaToraTextField;

	JTextField runwayFromLeftTodaTextField;
	JTextField runwayFromLeftToraTextField;
	JTextField runwayFromLeftAsdaTextField;
	JTextField runwayFromLeftLdaTextField;

	JTextField runwayFromRightTodaTextField;
	JTextField runwayFromRightToraTextField;
	JTextField runwayFromRightAsdaTextField;
	JTextField runwayFromRightLdaTextField;

	JTextField planeBlastDistanceTextField;

	JButton importButton;

	Font myFont;

	GUI gui;

	public SelectRunwayGUI(Runway runway, GUI gui, String runwaySource) throws ParserConfigurationException, SAXException, IOException {
		this.runway = runway;
		this.gui = gui;
		this.RUNWAY_FILE_NAME = runwaySource;
		preDefined = new ArrayList<Runway>();
		myFont = new Font("Arial", Font.BOLD, 12);

		// import the runways from the runway source file
		XMLRunwayService runwayService = new XMLRunwayService();

		// Make custom runway hard coded in program so it doesn't rely on
		// user having to include it in their XML file

		ArrayList<Runway> tmpPreDefined = runwayService.importRunways(RUNWAY_FILE_NAME);

		// create Custom Obstruction
		Runway custom = new Runway("Custom Runway", 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0);
		preDefined.add(custom);

		for (int i = 0; i < tmpPreDefined.size(); i++) {
			preDefined.add(tmpPreDefined.get(i));
		}

		for (int i = 0; i < preDefined.size(); i++) {
			comboListNames.add(preDefined.get(i).getRunwayName());
		}
	}

	public void init(String importOrNot) {
		frame = new JDialog();
		frame.setTitle("Create/Change Runway");
		JPanel main = new JPanel(new FlowLayout());

		KeyboardFocusManager kf_m = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kf_m.addKeyEventDispatcher(new EscapeListener(frame));

		// import and set the frame icon
		ImageIcon img = new ImageIcon("src\\assets\\data\\logo.png");
		frame.setIconImage(img.getImage());

		frame.setContentPane(main);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				frame.setVisible(true);
				frame.dispose();
			}
		});

		preDefinedCombo = new JComboBox(comboListNames.toArray());
		preDefinedCombo.addActionListener(new ComboBoxListener());

		runwayNameLabel = new JLabel("Runway Name:");
		runwayIDLabel = new JLabel("Runway ID:");
		runwayCommonParameters = new JLabel("Common Runway Parameters");
		planeBlastDistanceLabel = new JLabel("Plane Blast Distance:");
		runwayFromLeftLabel = new JLabel("Parameters from Left");
		runwayFromRightLabel = new JLabel("Parameters from Right");
		runwayTodaLabel = new JLabel("Runway TODA:");
		runwayAsdaLabel = new JLabel("Runway ASDA:");
		runwayLdaToraLabel = new JLabel("Runway LDA / TORA:");
		blastDistanceLabel = new JLabel("Plane Blast Distance:");
		runwayDisplacedThresholdLabel = new JLabel("Runway Displaced Threshold:");

		// separates sections using bold font face
		runwayNameLabel.setFont(myFont);
		runwayIDLabel.setFont(myFont);
		runwayCommonParameters.setFont(myFont);
		runwayFromLeftLabel.setFont(myFont);
		runwayFromRightLabel.setFont(myFont);
		runwayDisplacedThresholdLabel.setFont(myFont);

		runwayNameTextField = new JTextField(10);
		runwayIDTextField = new JTextField(10);

		runwayLdaToraTextField = new JTextField(10);
		planeBlastDistanceTextField = new JTextField(10);

		runwayFromLeftTodaTextField = new JTextField(10);
		runwayFromLeftToraTextField = new JTextField(10);
		runwayFromLeftAsdaTextField = new JTextField(10);
		runwayFromLeftLdaTextField = new JTextField(10);

		runwayFromRightTodaTextField = new JTextField(10);
		runwayFromRightToraTextField = new JTextField(10);
		runwayFromRightAsdaTextField = new JTextField(10);
		runwayFromRightLdaTextField = new JTextField(10);

		runwayFromLeftToraTextField.setEditable(false);
		runwayFromLeftLdaTextField.setEditable(false);
		runwayFromRightToraTextField.setEditable(false);
		runwayFromRightLdaTextField.setEditable(false);

		runwayFromLeftToraTextField.setEnabled(false);
		runwayFromLeftLdaTextField.setEnabled(false);
		runwayFromRightToraTextField.setEnabled(false);
		runwayFromRightLdaTextField.setEnabled(false);

		runwayLdaToraTextField.getDocument().addDocumentListener(new changeListener(false));
		;

		/* Show the details of the current displaying runway if (runway == null)
		 * { obstacleNameTextField = new JTextField(10); } else {
		 * obstacleNameTextField = new JTextField(runway.getName(), 10); } */

		importButton = new JButton("Import Runway(s)");
		importButton.setPreferredSize(new Dimension(175, 30));

		info = new JTextArea("Warning: This will change the current viewing runway.");
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);

		// autoscroll to bottom
		info.setCaretPosition(info.getDocument().getLength());

		jsp = new JScrollPane(info);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setPreferredSize(new Dimension(345, 50));

		// main content panel
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

		// first panel
		JPanel firstpanel = new JPanel(new GridLayout(1, 2, 10, 2));

		firstpanel.add(preDefinedCombo);
		firstpanel.add(importButton);

		// second panel
		JPanel secondpanel = new JPanel(new GridLayout(2, 2, 10, 2));

		secondpanel.add(runwayNameLabel);
		secondpanel.add(runwayNameTextField);

		secondpanel.add(runwayIDLabel);
		secondpanel.add(runwayIDTextField);

		// third panel
		JPanel thirdpanel = new JPanel(new GridLayout(3, 3, 10, 2));

		thirdpanel.add(runwayCommonParameters);
		thirdpanel.add(new JLabel(""));

		thirdpanel.add(new JLabel(runwayLdaToraLabel.getText()));
		thirdpanel.add(runwayLdaToraTextField);

		thirdpanel.add(planeBlastDistanceLabel);
		thirdpanel.add(planeBlastDistanceTextField);

		// fourth panel
		JPanel fourthpanel = new JPanel(new GridLayout(5, 5, 10, 2));
		//
		fourthpanel.add(runwayFromLeftLabel);
		fourthpanel.add(new JLabel(""));

		fourthpanel.add(new JLabel("Ruwnay TORA:"));
		fourthpanel.add(runwayFromLeftToraTextField);

		fourthpanel.add(new JLabel(runwayTodaLabel.getText()));
		fourthpanel.add(runwayFromLeftTodaTextField);

		fourthpanel.add(new JLabel(runwayAsdaLabel.getText()));
		fourthpanel.add(runwayFromLeftAsdaTextField);

		fourthpanel.add(new JLabel("Runway LDA:"));
		fourthpanel.add(runwayFromLeftLdaTextField);

		// fifth panel
		JPanel fifthpanel = new JPanel(new GridLayout(5, 5, 10, 2));

		fifthpanel.add(runwayFromRightLabel);
		fifthpanel.add(new JLabel(""));

		fifthpanel.add(new JLabel("Runway TORA:"));
		fifthpanel.add(runwayFromRightToraTextField);

		fifthpanel.add(new JLabel(runwayTodaLabel.getText()));
		fifthpanel.add(runwayFromRightTodaTextField);

		fifthpanel.add(new JLabel(runwayAsdaLabel.getText()));
		fifthpanel.add(runwayFromRightAsdaTextField);

		fifthpanel.add(new JLabel("Runway LDA:"));
		fifthpanel.add(runwayFromRightLdaTextField);

		// sixth panel
		JPanel sixthpanel = new JPanel(new GridLayout(1, 2, 10, 2));

		// sixthpanel.add(runwayDisplacedThresholdLabel);
		// sixthpanel.add(runwayDisplacedThresholdTextField);

		// add contents to the content panel
		content.add(firstpanel);
		content.add(Box.createRigidArea(new Dimension(0, 10)));
		content.add(secondpanel);
		content.add(Box.createRigidArea(new Dimension(0, 5)));
		content.add(thirdpanel);
		content.add(Box.createRigidArea(new Dimension(0, 5)));
		content.add(fourthpanel);
		content.add(Box.createRigidArea(new Dimension(0, 5)));
		content.add(fifthpanel);
		content.add(Box.createRigidArea(new Dimension(0, 5)));
		content.add(sixthpanel);

		// creates warning panel
		JPanel warning = new JPanel(new FlowLayout());
		warning.setBorder(new EmptyBorder(10, 5, 0, 10));
		warning.add(jsp);

		// creates bottom panel
		JPanel bottom = new JPanel(new FlowLayout());
		JButton changeButton = new JButton("Save & Change Runway");
		JButton exportButton = new JButton("Save & Export Runway");
		changeButton.setPreferredSize(new Dimension(170, 30));
		exportButton.setPreferredSize(new Dimension(170, 30));

		bottom.add(changeButton);
		bottom.add(exportButton);

		// creates the GUI
		main.setBorder(new EmptyBorder(10, 10, 10, 10));
		main.add(content);
		main.add(warning);
		main.add(bottom);

		// Setting Colors
		ArrayList<Color> backgroundColors = gui.getColourScheme();

		main.setBackground(backgroundColors.get(0));
		content.setBackground(backgroundColors.get(0));
		warning.setBackground(backgroundColors.get(0));
		warning.setBackground(backgroundColors.get(0));
		bottom.setBackground(backgroundColors.get(0));
		firstpanel.setBackground(backgroundColors.get(0));
		secondpanel.setBackground(backgroundColors.get(0));
		thirdpanel.setBackground(backgroundColors.get(0));
		fourthpanel.setBackground(backgroundColors.get(0));
		fifthpanel.setBackground(backgroundColors.get(0));
		fifthpanel.setBackground(backgroundColors.get(0));
		sixthpanel.setBackground(backgroundColors.get(0));

		info.setBackground(backgroundColors.get(1));
		runwayNameTextField.setBackground(backgroundColors.get(1));
		runwayIDTextField.setBackground(backgroundColors.get(1));
		runwayLdaToraTextField.setBackground(backgroundColors.get(1));
		runwayFromLeftTodaTextField.setBackground(backgroundColors.get(1));
		runwayFromLeftLdaTextField.setBackground(backgroundColors.get(1));
		runwayFromLeftAsdaTextField.setBackground(backgroundColors.get(1));
		runwayFromLeftToraTextField.setBackground(backgroundColors.get(1));
		runwayFromRightTodaTextField.setBackground(backgroundColors.get(1));
		runwayFromRightToraTextField.setBackground(backgroundColors.get(1));
		runwayFromRightAsdaTextField.setBackground(backgroundColors.get(1));
		runwayFromRightLdaTextField.setBackground(backgroundColors.get(1));
		planeBlastDistanceTextField.setBackground(backgroundColors.get(1));

		info.setBackground(backgroundColors.get(1));

		importButton.setBackground(backgroundColors.get(2));
		changeButton.setBackground(backgroundColors.get(2));
		exportButton.setBackground(backgroundColors.get(2));

		// add listeners
		changeButton.addActionListener(new ChangeButtonListener());
		exportButton.addActionListener(new ExportRunwayListener());

		// changeButton.addActionListener(new SetRunwayListener());
		importButton.addActionListener(new ImportRunwayListener());

		// makes sure the first entry in the combo list is selected
		preDefinedCombo.setSelectedIndex(0);

		// knows whether to open up the import obstructions pop-up before
		// setting the modality of this JDialog
		if (importOrNot.equals("click import button")) {
			this.importButton.doClick();
		}

		// formats and makes the GUI
		frame.setModal(true);
		frame.setPreferredSize(new Dimension(450, 680));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public boolean validateInput() {

		// creates variables for each input field, ready for validation

		String name = runwayNameTextField.getText();
		String id = runwayIDTextField.getText();
		String blastDistance = planeBlastDistanceTextField.getText();
		String ldaTora = runwayLdaToraTextField.getText();
		String todaFromLeft = runwayFromLeftTodaTextField.getText();
		String asdaFromLeft = runwayFromLeftAsdaTextField.getText();
		String todaFromRight = runwayFromRightTodaTextField.getText();
		String asdaFromRight = runwayFromRightAsdaTextField.getText();

		if (name.length() > 20) {
			JOptionPane.showMessageDialog(null, "Please enter a runway name no longer than 20 characters.", "Invalid Runway Name", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(isDouble(id))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Runway ID number", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		double idInt = Double.parseDouble(id);

		// check ID isn't zero
		if (idInt == 0) {
			JOptionPane.showMessageDialog(null, "Please enter a number greater than zero.", "Invalid Runway ID number", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// check if the ID is already being used
		if (preDefinedCombo.getSelectedItem().equals("Custom Runway")) {
			for (int i = 0; i < preDefined.size(); i++) {
				if (idInt == preDefined.get(i).getRunwayID()) {
					JOptionPane.showMessageDialog(null, "Please enter a Runway ID that is not already in use.", "Invalid Runway ID number", JOptionPane.INFORMATION_MESSAGE);
					return false;

				}
			}
		}

		if (!(isDouble(todaFromLeft))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Runway TODA.", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(isDouble(todaFromRight))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Runway TODA.", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(isDouble(ldaTora))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Runway TORA", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(isDouble(asdaFromLeft))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Runway ASDA", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(isDouble(asdaFromRight))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Runway ASDA", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(isDouble(ldaTora))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Runway LDA", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(isDouble(blastDistance))) {
			JOptionPane.showMessageDialog(null, "Please enter a number.", "Invalid Plane Blast Distance", JOptionPane.INFORMATION_MESSAGE);
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

	// checks to see if the new runway being added has a unique name
	public boolean hasUniqueName(String runwayName) {

		String checkRunway = runwayName.toLowerCase();

		for (int i = 0; i < this.comboListNames.size(); i++) {
			if (comboListNames.get(i).toLowerCase().equals(checkRunway)) {
				return false;
			}
		}

		return true;
	}

	public class ImportRunwayListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			ArrayList<Runway> importedRunways;

			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");

			filechooser.setFileFilter(xmlFilter);
			filechooser.setAcceptAllFileFilterUsed(false);
			filechooser.setDialogTitle("Open XML- Runway File");

			int returnVal = filechooser.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				XMLRunwayService xmlService = new XMLRunwayService();

				// try import the chosen xml file
				try {

					importedRunways = xmlService.importRunways(file.getName());

					// add in the new runways that were imported if they
					// have a unique name AND export the new runways into
					// the runway source file so they are ready to be
					// accessed next time the program is loaded up
					for (int i = 0; i < importedRunways.size(); i++) {
						if ((hasUniqueName(importedRunways.get(i).getRunwayName()) == true)) {
							preDefined.add(importedRunways.get(i));
							comboListNames.add(importedRunways.get(i).getRunwayName());
							xmlService.exportRunway(importedRunways.get(i), RUNWAY_FILE_NAME);
						}
					}

					// empty the combo list array so it can be repopulated
					preDefinedCombo.removeAllItems();

					// repopulate the combo list array
					for (int i = 0; i < comboListNames.size(); i++) {
						preDefinedCombo.addItem(comboListNames.get(i));
					}

					gui.addUserNotification("New Runways were imported from: \"" + file.getName() + "\"");

				} catch (ParserConfigurationException | SAXException | IOException | TransformerException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "File couldn't be imported! Make sure it is an XML runway file.", "File Warning", JOptionPane.WARNING_MESSAGE);
				}

			}

		}

	}

	public class ExportRunwayListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// Top half similar to ChangeButtonListener - used to validate/save
			// the runway before exporting it

			Runway newRunway;

			// used to make sure if a custom runway is being created, a unique
			// name is assigned to it.
			int myIndex = preDefinedCombo.getSelectedIndex();
			String runwayCheck = comboListNames.get(myIndex);

			if (runwayCheck.equals("Custom Runway")) {
				if (hasUniqueName(runwayNameTextField.getText()) == false) {
					JOptionPane.showMessageDialog(null, "Please give your runway a unique name.", "Runway Error", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}

			if (validateInput() == false) {
				JOptionPane.showMessageDialog(null, "Unable to change to your runway.", "Runway Change Error", JOptionPane.INFORMATION_MESSAGE);
			} else {

				String runwayName = runwayNameTextField.getText();
				int runwayID = Integer.parseInt(runwayIDTextField.getText());
				Double todaFromLeft = Double.parseDouble(runwayFromLeftTodaTextField.getText());
				Double todaFromRight = Double.parseDouble(runwayFromRightTodaTextField.getText());
				Double ldaTora = Double.parseDouble(runwayLdaToraTextField.getText());
				Double asdaFromLeft = Double.parseDouble(runwayFromLeftAsdaTextField.getText());
				Double asdaFromRight = Double.parseDouble(runwayFromRightAsdaTextField.getText());
				Double takeOffPlaneBlastDistance = Double.parseDouble(planeBlastDistanceTextField.getText());

				newRunway = new Runway(runwayName, runwayID, todaFromLeft, todaFromRight, ldaTora, asdaFromLeft, asdaFromRight, ldaTora, ldaTora, takeOffPlaneBlastDistance);

				// if a new runway is created, make it available in the
				// xml file for next time too.
				if (runwayCheck.equals("Custom Runway")) {

					XMLRunwayService xmlExportRunway = new XMLRunwayService();

					try {

						xmlExportRunway.exportRunway(newRunway, RUNWAY_FILE_NAME);
						comboListNames.add(runwayNameTextField.getText());
						preDefinedCombo.addItem(runwayNameTextField.getText());
						preDefined.add(newRunway);

					} catch (ParserConfigurationException | TransformerException | SAXException | IOException e1) {
						e1.printStackTrace();
						return;
					}

				}

				frame.dispose();
			}

			// open up the ExportRunwayGUI so the user can export the Runway
			// they have just created (or another one)
			new ExportRunwayGUI(RUNWAY_FILE_NAME, gui);
		}
	}

	public class ChangeButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			Runway newRunway;

			// used to make sure if a custom runway is being created, a unique
			// name is assigned to it.
			int myIndex = preDefinedCombo.getSelectedIndex();
			String runwayCheck = comboListNames.get(myIndex);

			if (runwayCheck.equals("Custom Runway")) {
				if (hasUniqueName(runwayNameTextField.getText()) == false) {
					JOptionPane.showMessageDialog(null, "Please give your runway a unique name.", "Runway Error", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}

			if (validateInput() == false) {
				JOptionPane.showMessageDialog(null, "Unable to change to your runway.", "Runway Change Error", JOptionPane.INFORMATION_MESSAGE);
			} else {

				String runwayName = runwayNameTextField.getText();
				int runwayID = Integer.parseInt(runwayIDTextField.getText());
				Double todaFromLeft = Double.parseDouble(runwayFromLeftTodaTextField.getText());
				Double todaFromRight = Double.parseDouble(runwayFromRightTodaTextField.getText());
				Double ldaTora = Double.parseDouble(runwayLdaToraTextField.getText());
				Double asdaFromLeft = Double.parseDouble(runwayFromLeftAsdaTextField.getText());
				Double asdaFromRight = Double.parseDouble(runwayFromRightAsdaTextField.getText());
				Double takeOffPlaneBlastDistance = Double.parseDouble(planeBlastDistanceTextField.getText());

				newRunway = new Runway(runwayName, runwayID, todaFromLeft, todaFromRight, ldaTora, asdaFromLeft, asdaFromRight, ldaTora, ldaTora, takeOffPlaneBlastDistance);

				gui.clearCalculations();
				gui.changeRunway(newRunway);

				// if a new runway is created, make it available in the
				// xml file for next time too.
				if (runwayCheck.equals("Custom Runway")) {

					XMLRunwayService xmlExportRunway = new XMLRunwayService();

					try {
						xmlExportRunway.exportRunway(newRunway, RUNWAY_FILE_NAME);
						comboListNames.add(runwayNameTextField.getText());
						preDefinedCombo.addItem(runwayNameTextField.getText());
						preDefined.add(newRunway);
					} catch (ParserConfigurationException | TransformerException | SAXException | IOException e1) {
						e1.printStackTrace();
					}

				}

				gui.addUserNotification("Runway has been changed to: \"" + newRunway.getRunwayName() + "\"");
				frame.dispose();
			}
		}
	}

	public class ComboBoxListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			String selectedRunway = (String) preDefinedCombo.getSelectedItem();

			// finds the selected runway
			for (int i = 0; i < preDefined.size(); i++) {
				if (preDefined.get(i).getRunwayName() == selectedRunway) {
					Runway selected = preDefined.get(i);

					// updates the text fields according to the selected
					// runway

					runwayNameTextField.setText(selected.getRunwayName());
					runwayIDTextField.setText(selected.getRunwayID() + "");
					runwayFromLeftTodaTextField.setText(selected.getOriginalLogicalRunwayFromLeft().toda + "");
					runwayLdaToraTextField.setText(selected.getOriginalLogicalRunwayFromLeft().tora + "");
					planeBlastDistanceTextField.setText(selected.getBlastDistance() + "");
					runwayFromLeftAsdaTextField.setText(selected.getOriginalLogicalRunwayFromLeft().asda + "");
					runwayFromRightTodaTextField.setText(selected.getOriginalLogicalRunwayFromRight().toda + "");
					runwayFromRightAsdaTextField.setText(selected.getOriginalLogicalRunwayFromRight().asda + "");
					runwayFromRightLdaTextField.setText(selected.getOriginalLogicalRunwayFromRight().lda + "");

					runwayFromRightToraTextField.setText(selected.getOriginalLogicalRunwayFromRight().tora + "");
					runwayFromLeftToraTextField.setText(selected.getOriginalLogicalRunwayFromRight().tora + "");
					runwayFromRightLdaTextField.setText(selected.getOriginalLogicalRunwayFromRight().lda + "");
					runwayFromLeftLdaTextField.setText(selected.getOriginalLogicalRunwayFromRight().lda + "");

					// if its not a custom runway, dont allow editing of
					// length, width or height
					if (!(selected.getRunwayName().equals("Custom Runway"))) {
						runwayNameTextField.setEditable(false);
						runwayIDTextField.setEditable(false);
						runwayNameTextField.setEditable(false);
						runwayIDTextField.setEditable(false);
						runwayFromLeftTodaTextField.setEditable(false);
						runwayFromLeftToraTextField.setEditable(false);
						runwayFromLeftAsdaTextField.setEditable(false);
						runwayFromLeftLdaTextField.setEditable(false);
						runwayFromRightTodaTextField.setEditable(false);
						runwayFromRightToraTextField.setEditable(false);
						runwayFromRightAsdaTextField.setEditable(false);
						runwayFromRightLdaTextField.setEditable(false);
						planeBlastDistanceTextField.setEditable(true);
					} else {
						runwayIDTextField.setEditable(true);
						runwayNameTextField.setEditable(true);
						runwayIDTextField.setEditable(true);
						runwayFromLeftTodaTextField.setEditable(true);
						runwayFromLeftToraTextField.setEditable(true);
						runwayFromLeftAsdaTextField.setEditable(true);
						runwayFromLeftLdaTextField.setEditable(true);
						runwayFromRightTodaTextField.setEditable(true);
						runwayFromRightToraTextField.setEditable(true);
						runwayFromRightAsdaTextField.setEditable(true);
						runwayFromRightLdaTextField.setEditable(true);
						planeBlastDistanceTextField.setEditable(true);
					}

				}
			}
		}
	}

	public class changeListener implements DocumentListener {
		Boolean isLdaTora = false;

		public changeListener(Boolean isLdaTora) {
			this.isLdaTora = isLdaTora;
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			task();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			task();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			task();
		}

		public void task() {
			if (isLdaTora) {
				runwayFromLeftLdaTextField.setText(runwayLdaToraTextField.getText());
				runwayFromRightLdaTextField.setText(runwayLdaToraTextField.getText());
			} else {
				runwayFromLeftToraTextField.setText(runwayLdaToraTextField.getText());
				runwayFromRightToraTextField.setText(runwayLdaToraTextField.getText());
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
}
