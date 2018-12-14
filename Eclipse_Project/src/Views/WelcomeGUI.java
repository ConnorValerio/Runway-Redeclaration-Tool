package Views;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Controllers.*;
import Models.Airport;
import Services.*;

public class WelcomeGUI {

	// Elements used by ActionListeners
	JFrame frame;
	JTextField selectedAirportText;
	JTextField runwaySourceTextField;
	JTextField obstructionSourceTextField;
	JTextField airportNameTextField;

	Airport airport;

	// used to hold imported runways and obstructions - never passed to next
	// object (GUI), just used to check that the files being supplied can
	// actually be
	// imported in
	ArrayList<Runway> runways;
	ArrayList<Obstruction> obstructions;

	public WelcomeGUI() {
		frame = new JFrame(LocalizationService.localizeString("title"));
		airport = new Airport("EmptyAirport", "", "");

		// import and set the frame icon
		ImageIcon img = new ImageIcon("src\\assets\\data\\logo.png");
		frame.setIconImage(img.getImage());

	}

	public void init() {
		JPanel main = new JPanel();
		frame.setContentPane(main);
		main.setLayout(new FlowLayout());
		main.setBorder(new EmptyBorder(20, 20, 20, 20));

		// title panel
		JPanel titlePanel = new JPanel();
		JLabel title = new JLabel(LocalizationService.localizeString("welcome_word"));
		title.setFont(new Font("Arial", Font.BOLD, 20));
		titlePanel.add(title);

		// welcome message panel
		JPanel welcomeMessage = new JPanel();
		welcomeMessage.setBorder(new EmptyBorder(10, 0, 10, 0));
		JTextArea message = new JTextArea(4, 40);
		message.setWrapStyleWord(true);
		message.setLineWrap(true);
		message.setFont(new Font("Arial", Font.PLAIN, 14));
		message.setText(LocalizationService.localizeString("welcome"));
		message.setEditable(false);
		message.setBackground(null);
		welcomeMessage.add(message);

		// content panel
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));

		// load airport section of the content panel
		JPanel loadPanel = new JPanel();
		loadPanel.setPreferredSize(new Dimension(440, 120));
		loadPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		loadPanel.setLayout(new GridLayout(3, 3, 5, 5));
		loadPanel.setBackground(Color.LIGHT_GRAY);

		JLabel loadTitle = new JLabel(LocalizationService.localizeString("load_airport"));
		loadTitle.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel selectedAirport = new JLabel(LocalizationService.localizeString("selected_airport"));
		selectedAirportText = new JTextField(10);

		JButton airportChooseFileButton = new JButton(LocalizationService.localizeString("choose_file"));
		JButton loadAirportButton = new JButton(LocalizationService.localizeString("load_airport"));

		// adds ActionListeners to buttons on this panel
		airportChooseFileButton.addActionListener(new AirportChooseFileListener());
		loadAirportButton.addActionListener(new LoadAirportListener());

		// formats JTextField
		Font smallFont = new Font("Arial", Font.PLAIN, 10);
		selectedAirportText.setFont(smallFont);
		selectedAirportText.setEditable(false);
		selectedAirportText.setText("ExampleAirport.ser");

		loadPanel.add(loadTitle);
		loadPanel.add(new JLabel(""));
		loadPanel.add(new JLabel(""));
		loadPanel.add(selectedAirport);
		loadPanel.add(selectedAirportText);
		loadPanel.add(airportChooseFileButton);
		loadPanel.add(loadAirportButton);
		loadPanel.add(new JLabel(""));
		loadPanel.add(new JLabel(""));

		// create airport section of the content panel
		JPanel createPanel = new JPanel();
		createPanel.setLayout(new GridLayout(5, 3, 5, 5));
		createPanel.setPreferredSize(new Dimension(440, 180));
		createPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		createPanel.setBackground(Color.LIGHT_GRAY);

		JLabel createTitle = new JLabel(LocalizationService.localizeString("new_airport"));
		createTitle.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel airportName = new JLabel("Airport Name:");
		airportNameTextField = new JTextField(10);
		JLabel runwaySource = new JLabel("Runway File:");
		runwaySourceTextField = new JTextField(10);
		JButton runwaySourceChooseFileButton = new JButton(LocalizationService.localizeString("choose_file"));
		JLabel obstructionSource = new JLabel("Obstruction File:");
		obstructionSourceTextField = new JTextField(10);
		JButton obstructionSourceChooseFileButton = new JButton(LocalizationService.localizeString("choose_file"));
		JButton createAirportButton = new JButton(LocalizationService.localizeString("new_airport"));

		// adds listeners to buttons on this panel
		runwaySourceChooseFileButton.addActionListener(new RunwayChooseFileListener());
		obstructionSourceChooseFileButton.addActionListener(new ObstructionChooseFileListener());
		createAirportButton.addActionListener(new CreateAirportListener());

		// formatting the JTextFields
		runwaySourceTextField.setFont(smallFont);
		runwaySourceTextField.setEditable(false);
		runwaySourceTextField.setText("example_runways.xml");

		obstructionSourceTextField.setFont(smallFont);
		obstructionSourceTextField.setEditable(false);
		obstructionSourceTextField.setText("example_obstructions.xml");

		Font exampleFont = new Font("Arial", Font.PLAIN, 12);
		airportNameTextField.setFont(exampleFont);
		airportNameTextField.setForeground(Color.GRAY);
		airportNameTextField.setText("Example Text");
		airportNameTextField.addMouseListener(new AirportNameTextListener());

		// adding components to createPanel
		createPanel.add(createTitle);
		createPanel.add(new JLabel(""));
		createPanel.add(new JLabel(""));
		createPanel.add(airportName);
		createPanel.add(airportNameTextField);
		createPanel.add(new JLabel(""));
		createPanel.add(runwaySource);
		createPanel.add(runwaySourceTextField);
		createPanel.add(runwaySourceChooseFileButton);
		createPanel.add(obstructionSource);
		createPanel.add(obstructionSourceTextField);
		createPanel.add(obstructionSourceChooseFileButton);
		createPanel.add(createAirportButton);
		createPanel.add(new JLabel(""));
		createPanel.add(new JLabel(""));

		// forming the content panel
		contentPanel.add(loadPanel);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		contentPanel.add(createPanel);

		// forming the main panel
		main.add(titlePanel);
		main.add(welcomeMessage);
		main.add(contentPanel);

		// sets all of the frames properties
		frame.setPreferredSize(new Dimension(500, 610));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	// used for .ser files (airport file)
	public class AirportChooseFileListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			// create JFileChooser for user to find airport .ser files
			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter serFilter = new FileNameExtensionFilter("SER files (*.SER)", "SER");

			filechooser.setFileFilter(serFilter);
			filechooser.setAcceptAllFileFilterUsed(false);
			filechooser.setDialogTitle("Open SER-Airport File");

			int returnVal = filechooser.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File chosenfile = filechooser.getSelectedFile();
				selectedAirportText.setText(chosenfile.getName());
			}
		}
	}

	// used for runway .xml files
	public class RunwayChooseFileListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			runways = new ArrayList<Runway>();

			// create JFileChooser for user to find runway .xml files
			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");

			filechooser.setFileFilter(xmlFilter);
			filechooser.setAcceptAllFileFilterUsed(false);
			filechooser.setDialogTitle("Open XML-Runway File");

			int returnVal = filechooser.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = filechooser.getSelectedFile();
				runwaySourceTextField.setText(file.getName());
			}

		}
	}

	// used for obstruction .xml files
	public class ObstructionChooseFileListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			obstructions = new ArrayList<Obstruction>();

			// create JFileChooser for user to find obstruction .xml files
			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");

			filechooser.setFileFilter(xmlFilter);
			filechooser.setAcceptAllFileFilterUsed(false);
			filechooser.setDialogTitle("Open XML-Obstruction File");

			int returnVal = filechooser.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = filechooser.getSelectedFile();
				obstructionSourceTextField.setText(file.getName());

			}

		}
	}

	// used on the "Load Airport" button
	public class LoadAirportListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			try {

				File chosenfile = new File(selectedAirportText.getText());

				// validation to ensure a file has been chosen before
				// attempting
				// to load in the airport

				String selectedAirport = selectedAirportText.getText();

				if (selectedAirport.equals("No File Selected")) {
					JOptionPane.showMessageDialog(null, LocalizationService.localizeString("pls_airport"), "File Not Chosen", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				// deserialize chosen airport.ser file
				InputStream file = new FileInputStream(chosenfile.getName());
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream(buffer);

				airport = (Airport) input.readObject();
				input.close();

				// update text field to show chosen filename
				selectedAirportText.setText(chosenfile.getName());

				// use xml services to assign the new airport's runways and
				// obstructions to this object's runway and
				// obstruction arrays

				// initialise the services
				XMLRunwayService runwayService = new XMLRunwayService();
				XMLObstructionService obstructionService = new XMLObstructionService();

				// assign the airport's xml file names to local string
				// variables
				String runwaySource = airport.getRunwayFilename();
				String obstructionSource = airport.getObstructionFilename();

				// updates the ArrayLists
				runways = runwayService.importRunways(runwaySource);
				obstructions = obstructionService.importObstructions(obstructionSource);

			} catch (IOException | ClassNotFoundException | ParserConfigurationException | SAXException e1) {

				JOptionPane.showMessageDialog(frame, LocalizationService.localizeString("not_ser"), "File Warning", JOptionPane.WARNING_MESSAGE);

				// set the airport to empty
				airport = new Airport("EmptyAirport", "", "");
				selectedAirportText.setText("No File Selected");

				return;
			}

			// close the WelcomeGUI and open the main program
			frame.dispose();
			GUI g = new GUI(airport.getAirportName(), airport.getRunwayFilename(), airport.getObstructionFilename());
			g.init();

		}

	}

	// used on the "Create Airport" button
	public class CreateAirportListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// strings used for validation
			String airportNameText = airportNameTextField.getText();
			String runwaySourceText = runwaySourceTextField.getText();
			String obstructionSourceText = obstructionSourceTextField.getText();

			// validation on the three necessary fields
			if (!(airportNameText.matches("([a-zA-Z]+ +)*[a-zA-Z]+")) || airportNameText.equals("") || airportNameText.equals("Example Text")) {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_airport"), "Invalid Airport Name", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (runwaySourceText.equals("No File Selected")) {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("not_runway"), LocalizationService.localizeString("no_file"), JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (obstructionSourceText.equals("No File Selected")) {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("not_obstr"), LocalizationService.localizeString("no_file"), JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// serialize airport and write to airport name-specific file
			Airport airport = new Airport(airportNameText, runwaySourceText, obstructionSourceText);

			// name-specific file
			String filename = airportNameText + ".ser";
			File check = new File(filename);

			// if the file doesn't exist...
			if (!check.exists()) {

				try {

					OutputStream file = new FileOutputStream(filename);
					OutputStream buffer = new BufferedOutputStream(file);
					ObjectOutput output = new ObjectOutputStream(buffer);
					output.writeObject(airport);
					output.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("dupe_airport") + airportNameText, "Duplicate Airport Name", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// creates services to be used
			XMLRunwayService xmlRunwayService = new XMLRunwayService();
			XMLObstructionService xmlObstructionService = new XMLObstructionService();

			// deals with runways
			File chosenRunwayFile = new File(runwaySourceTextField.getText());

			try {

				// import the runways
				runways = xmlRunwayService.importRunways(chosenRunwayFile.getName());

			} catch (ParserConfigurationException | SAXException | IOException e1) {

				runwaySourceTextField.setText("No File Selected");

				JOptionPane.showMessageDialog(frame, LocalizationService.localizeString("not_runway"), "File Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// deals with obstacles
			File chosenObstructionFile = new File(obstructionSourceTextField.getText());

			try {

				// import the obstructions
				obstructions = xmlObstructionService.importObstructions(chosenObstructionFile.getName());

			} catch (ParserConfigurationException | SAXException | IOException e1) {

				obstructionSourceTextField.setText(LocalizationService.localizeString("no_file"));

				JOptionPane.showMessageDialog(frame, LocalizationService.localizeString("not_obstr"), "File Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// close the WelcomeGUI and open the main program
			frame.dispose();
			GUI g = new GUI(airport.getAirportName(), airport.getRunwayFilename(), airport.getObstructionFilename());
			g.init();

		}

	}

	public class AirportNameTextListener implements MouseListener {

		public void mouseClicked(MouseEvent arg0) {
			Font exampleFont = new Font("Arial", Font.PLAIN, 12);
			airportNameTextField.setText("");
			airportNameTextField.setFont(exampleFont);
			airportNameTextField.setForeground(Color.BLACK);
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}

	}
}
