package Views;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ExportCalculationsGUI {

	String content;

	GUI gui;
	JDialog frame;
	JTextField input;
	JButton export;

	public ExportCalculationsGUI(GUI passedGui, String filecontent) {

		this.content = filecontent;
		this.gui = passedGui;
		init();
	}

	public void init() {

		// Close frame: ESCAPE
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new escapeListener());

		// initialise frame and set the content pane as 'main'
		frame = new JDialog();
		frame.setTitle("Export Calculations");
		JPanel main = new JPanel(new FlowLayout());
		frame.setContentPane(main);

		// create title and format it
		Font titleFont = new Font("Arial", Font.BOLD, 14);
		JLabel title = new JLabel("Please give the name of the file you wish to export to: ");
		title.setFont(titleFont);
		title.setPreferredSize(new Dimension(400, 20));

		// creates the text field for user to input file name
		input = new JTextField(15);
		input.setFont(new Font("Arial", Font.PLAIN, 12));

		// creates export button
		export = new JButton("Export Calculations");
		export.setFont(new Font("Arial", Font.PLAIN, 12));
		export.setPreferredSize(new Dimension(180, 30));
		export.addActionListener(new ExportListener());

		// adds components to main panel
		main.add(title);
		main.add(input);
		main.add(export);

		// formats and makes the GUI
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setModal(true);
		frame.setPreferredSize(new Dimension(450, 110));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public class ExportListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String enteredFileName = input.getText();
			File file = new File(enteredFileName + ".txt");

			// checks for a legal file name
			if (!(enteredFileName.matches("^[\\w\\-. ]+$")) || enteredFileName.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a valid file name. A file name can not contain the following characters: \\ / : * ? \" < > |", "Invalid File Name", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// checks whether the file exists or not
			if (file.exists() == true) {
				JOptionPane.showMessageDialog(null, "Please ensure you provide a file name that does not already exist.", "File Already Exists", JOptionPane.INFORMATION_MESSAGE);
			} else {

				// if it doesn't exist, create the file...
				try {

					file.createNewFile();

					// write the content to the newly created file
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(content);
					bw.close();

					// inform user the export has been completed
					JOptionPane.showMessageDialog(null, "The calculations have been successfully exported to: " + file.getName() + ".", "Calculation Export", JOptionPane.INFORMATION_MESSAGE);

					// close the ExportCalculationsGUI
					gui.addUserNotification("The Current Calculations were exported to: \"" + file.getName() + "\"");
					frame.dispose();

				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "The calculations could not be exported, please try with a different file name.", "Calculation Export Error", JOptionPane.WARNING_MESSAGE);
					return;
				}

			}

		}
	}

	// shortcut to close the frame: Esc key
	public class escapeListener implements KeyEventDispatcher {
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

}
