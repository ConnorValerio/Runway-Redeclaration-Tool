package Views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Controllers.Obstruction;
import Listeners.EscapeListener;
import Services.LocalizationService;
import Services.XMLObstructionService;

public class ExportObstructionGUI {

	JDialog frame;

	JLabel titleOne, titleTwo;

	JTextField chosenFile;
	JButton chooseFile, export;

	JComboBox obstructionSelection;

	Obstruction obstruction;
	ArrayList<Obstruction> listOfObstructions;
	ArrayList<String> obstructionNames;

	XMLObstructionService xmlService;

	GUI gui;

	public ExportObstructionGUI(String obstructionSource, GUI currentGUI) {
		gui = currentGUI;
		// import the obstructions from the source
		xmlService = new XMLObstructionService();

		try {
			listOfObstructions = xmlService.importObstructions(obstructionSource);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		// create an array for the obstruction names to be stored in
		obstructionNames = new ArrayList<String>();

		// initialise JDialog and set it's title
		frame = new JDialog();
		frame.setTitle("Obstruction Export");

		// import and set the frame icon
		frame.setIconImage(new ImageIcon("src\\assets\\data\\logo.png").getImage());

		// create titles and format them
		Font titleFont = new Font("Arial", Font.BOLD, 14);
		titleOne = new JLabel(LocalizationService.localizeString("export_obstr_select"));
		titleTwo = new JLabel(LocalizationService.localizeString("choose_export"));

		titleOne.setFont(titleFont);
		titleTwo.setFont(titleFont);

		// creates and populates the combo box
		for (int i = 0; i < listOfObstructions.size(); i++) {
			obstructionNames.add(listOfObstructions.get(i).getName());
		}

		obstructionSelection = new JComboBox(obstructionNames.toArray());
		obstructionSelection.setPreferredSize(new Dimension(350, 30));
		obstructionSelection.setFont(new Font("Arial", Font.PLAIN, 14));

		Font textFont = new Font("Arial", Font.PLAIN, 12);
		chosenFile = new JTextField(14);
		chosenFile.setHorizontalAlignment(JTextField.CENTER);
		chosenFile.setPreferredSize(new Dimension(50, 30));
		chosenFile.setFont(textFont);
		chosenFile.setText("No File Selected");
		chosenFile.setEditable(false);

		chooseFile = new JButton(LocalizationService.localizeString("choose_file"));
		chooseFile.setFont(new Font("Arial", Font.PLAIN, 12));
		chooseFile.setPreferredSize(new Dimension(100, 30));
		chooseFile.addActionListener(new ChooseFileListener());

		export = new JButton(LocalizationService.localizeString("export"));
		export.setFont(new Font("Arial", Font.PLAIN, 12));
		export.setPreferredSize(new Dimension(100, 30));
		export.addActionListener((ActionListener) new ExportListener());

		init();

	}

	public void init() {

		JPanel main = new JPanel(new GridLayout(3, 3, 5, 5));
		frame.setContentPane(main);
		main.setLayout(new FlowLayout());

		KeyboardFocusManager kf_m = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kf_m.addKeyEventDispatcher(new EscapeListener(frame));

		JPanel top = new JPanel(new FlowLayout());
		top.setPreferredSize(new Dimension(450, 50));
		top.add(obstructionSelection);

		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setPreferredSize(new Dimension(450, 50));
		bottom.add(chosenFile);
		bottom.add(chooseFile);
		bottom.add(export);

		main.add(titleOne);
		main.add(top);
		main.add(titleTwo);
		main.add(bottom);

		// Setting Colors
		ArrayList<Color> backgroundColors = gui.getColourScheme();
		Arrays.asList(main, titleOne, top, titleTwo, bottom).forEach(s -> s.setBackground(backgroundColors.get(0)));

		chooseFile.setBackground(backgroundColors.get(2));
		export.setBackground(backgroundColors.get(2));

		chosenFile.setBackground(backgroundColors.get(1));

		frame.setLocationRelativeTo(null);
		frame.setModal(true);
		frame.setPreferredSize(new Dimension(450, 190));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public class ChooseFileListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// create JFileChooser for user to find obstruction .xml files
			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");

			filechooser.setFileFilter(xmlFilter);
			filechooser.setAcceptAllFileFilterUsed(false);
			filechooser.setDialogTitle("Open XML - Export File");

			int returnVal = filechooser.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = filechooser.getSelectedFile();
				chosenFile.setText(file.getName());
			}

		}

	}

	public class ExportListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// checks a file has been chosen before attempting to export
			if (chosenFile.getText().equals(LocalizationService.localizeString("no_file"))) {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("choose_export"), LocalizationService.localizeString("no_file"), JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// checks that the file chosen is an Obstruction (.xml) file by
			// trying to import it
			try {
				xmlService.importObstructions(chosenFile.getText());
			} catch (ParserConfigurationException | SAXException | IOException e2) {

				chosenFile.setText(LocalizationService.localizeString("no_file"));

				JOptionPane.showMessageDialog(frame, LocalizationService.localizeString("not_obstr"), LocalizationService.localizeString("warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Strings & Obstruction variable to be used
			String chosenFileName = chosenFile.getText();
			String chosenObstruction = (String) obstructionSelection.getSelectedItem();
			Obstruction obstructionForExport = null;

			// finding the chosen Obstruction ready for export
			for (int i = 0; i < listOfObstructions.size(); i++) {
				if (listOfObstructions.get(i).getName().equals(chosenObstruction)) {
					obstructionForExport = listOfObstructions.get(i);
				}
			}

			// exporting the Obstruction
			try {
				xmlService.exportObstruction(obstructionForExport, chosenFileName);
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("export_obstr_success"), LocalizationService.localizeString("success"), JOptionPane.INFORMATION_MESSAGE);

				frame.dispose();

			} catch (ParserConfigurationException | TransformerException | SAXException | IOException e1) {

				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("not_obstr"), LocalizationService.localizeString("warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}
}