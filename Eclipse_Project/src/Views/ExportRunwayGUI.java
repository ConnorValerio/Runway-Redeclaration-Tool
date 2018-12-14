package Views;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Controllers.Runway;
import Listeners.EscapeListener;
import Services.LocalizationService;
import Services.XMLRunwayService;

public class ExportRunwayGUI {

	JDialog frame;

	JLabel titleOne, titleTwo;

	JTextField chosenFile;
	JButton chooseFile, export;

	JComboBox<?> runwaySelection;

	Runway runway;
	GUI gui;
	ArrayList<Runway> listOfRunways;
	ArrayList<String> runwayNames;

	XMLRunwayService xmlService;

	public ExportRunwayGUI(String runwaySource, GUI currentGUI) {
		gui = currentGUI;
		// import the runways from the source
		xmlService = new XMLRunwayService();

		try {
			listOfRunways = xmlService.importRunways(runwaySource);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		// create an array for the runway names to be stored in
		runwayNames = new ArrayList<String>();

		// initialise JDialog and set it's title
		frame = new JDialog();
		frame.setTitle("Runway Export");

		// create titles and format them
		Font titleFont = new Font("Arial", Font.BOLD, 14);
		titleOne = new JLabel(LocalizationService.localizeString("export_select"));
		titleTwo = new JLabel(LocalizationService.localizeString("choose_export"));

		titleOne.setFont(titleFont);
		titleTwo.setFont(titleFont);

		// creates and populates the combo box
		for (int i = 0; i < listOfRunways.size(); i++) {
			runwayNames.add(listOfRunways.get(i).getRunwayName());
		}

		runwaySelection = new JComboBox<Object>(runwayNames.toArray());
		runwaySelection.setPreferredSize(new Dimension(350, 30));
		runwaySelection.setFont(new Font("Arial", Font.PLAIN, 14));

		Font textFont = new Font("Arial", Font.PLAIN, 12);
		chosenFile = new JTextField(14);
		chosenFile.setHorizontalAlignment(JTextField.CENTER);
		chosenFile.setPreferredSize(new Dimension(50, 30));
		chosenFile.setFont(textFont);
		chosenFile.setText(LocalizationService.localizeString("no_file"));
		chosenFile.setEditable(false);

		chooseFile = new JButton(LocalizationService.localizeString("choose_file"));
		chooseFile.setFont(new Font("Arial", Font.PLAIN, 12));
		chooseFile.setPreferredSize(new Dimension(100, 30));
		chooseFile.addActionListener(new ChooseFileListener());

		export = new JButton(LocalizationService.localizeString("export"));
		export.setFont(new Font("Arial", Font.PLAIN, 12));
		export.setPreferredSize(new Dimension(100, 30));
		export.addActionListener(new ExportListener());

		init();
	}

	public void init() {

		JPanel main = new JPanel(new GridLayout(3, 3, 5, 5));
		frame.setContentPane(main);
		main.setLayout(new FlowLayout());

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new EscapeListener(frame));

		// import and set the frame icon
		ImageIcon img = new ImageIcon("src\\assets\\data\\logo.png");
		frame.setIconImage(img.getImage());

		JPanel top = new JPanel(new FlowLayout());
		top.setPreferredSize(new Dimension(450, 50));
		top.add(runwaySelection);

		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setPreferredSize(new Dimension(450, 50));
		bottom.add(chosenFile);
		bottom.add(chooseFile);
		bottom.add(export);

		main.add(titleOne);
		main.add(top);
		main.add(titleTwo);
		main.add(bottom);

		// Setting color
		ArrayList<Color> backgroundColors = gui.getColourScheme();

		titleOne.setBackground(backgroundColors.get(0));
		top.setBackground(backgroundColors.get(0));
		titleTwo.setBackground(backgroundColors.get(0));
		bottom.setBackground(backgroundColors.get(0));
		main.setBackground(backgroundColors.get(0));

		chooseFile.setBackground(backgroundColors.get(2));
		export.setBackground(backgroundColors.get(2));

		chosenFile.setBackground(backgroundColors.get(1));
		;

		// formats and makes the GUI
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
			// create JFileChooser for user to find runway .xml files
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

			// checks that the file chosen is a Runway (.xml) file by
			// trying to import it
			try {
				xmlService.importRunways(chosenFile.getText());
			} catch (ParserConfigurationException | SAXException | IOException e2) {

				chosenFile.setText(LocalizationService.localizeString("no_file"));

				JOptionPane.showMessageDialog(frame, LocalizationService.localizeString("not_runway"), LocalizationService.localizeString("warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Strings & Runway variable to be used
			String chosenFileName = chosenFile.getText();
			String chosenRunway = (String) runwaySelection.getSelectedItem();
			Runway runwayForExport = null;

			// finding the chosen Runway ready for export
			for (int i = 0; i < listOfRunways.size(); i++) {
				if (listOfRunways.get(i).getRunwayName().equals(chosenRunway)) {
					runwayForExport = listOfRunways.get(i);
				}
			}

			// exporting the Runway
			try {

				xmlService.exportRunway(runwayForExport, chosenFileName);
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("export_runway_success"), LocalizationService.localizeString("success"), JOptionPane.INFORMATION_MESSAGE);

				gui.addUserNotification("The Runway: \"" + runwayForExport.getRunwayName() + "\" has been exported to: \"" + chosenFileName + "\"");
				frame.dispose();

			} catch (ParserConfigurationException | TransformerException | SAXException | IOException e1) {

				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("not_runway"), LocalizationService.localizeString("warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}
}
