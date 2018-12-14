package Views;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import Services.LocalizationService;

public class ColourSchemeGUI {
	private GUI gui;
	private JDialog frame;
	private JComboBox preDefinedCombo;
	private JButton chooseColour;

	public ColourSchemeGUI(GUI currentGUI) {
		this.gui = currentGUI;
	}

	public void init() {

		// Close frame: ESCAPE
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new escapeListener());

		frame = new JDialog();
		frame.setTitle(LocalizationService.localizeString("colour_scheme"));
		JPanel main = new JPanel(new FlowLayout());
		frame.setContentPane(main);

		// create title and format it
		Font titleFont = new Font("Arial", Font.BOLD, 14);
		JLabel title = new JLabel(LocalizationService.localizeString("select_colour"));
		title.setFont(titleFont);

		String[] comboBoxArray = { "Default", "Green", "Orange" };
		preDefinedCombo = new JComboBox(comboBoxArray);
		preDefinedCombo.setPreferredSize(new Dimension(220, 30));
		preDefinedCombo.setFont(new Font("Arial", Font.PLAIN, 14));

		chooseColour = new JButton(LocalizationService.localizeString("choose_colour"));
		chooseColour.setFont(new Font("Arial", Font.PLAIN, 12));
		chooseColour.setPreferredSize(new Dimension(180, 30));
		chooseColour.addActionListener(new ChangeColourListener());

		// top panel
		JPanel content = new JPanel(new FlowLayout());
		content.setPreferredSize(new Dimension(450, 50));
		content.add(preDefinedCombo);
		content.add(chooseColour);

		main.add(title);
		main.add(content);

		// Setting Color
		ArrayList<Color> backgroundColors = gui.getColourScheme();

		chooseColour.setBackground(backgroundColors.get(2));

		main.setBackground(backgroundColors.get(0));
		title.setBackground(backgroundColors.get(0));
		content.setBackground(backgroundColors.get(0));

		// formats and makes the GUI
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setModal(true);
		frame.setPreferredSize(new Dimension(450, 120));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
		}
	}

	public class ChangeColourListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			ArrayList<Color> newColors = new ArrayList<Color>();

			String selectedScheme = preDefinedCombo.getSelectedItem().toString();

			if (selectedScheme == "Green") {
				newColors.add(Color.decode("#6CBB3C"));
				newColors.add(Color.decode("#85BB65"));
				newColors.add(Color.decode("#5AB950"));
			} else if (selectedScheme == "Orange") {
				newColors.add(Color.decode("#F2C249"));
				newColors.add(Color.decode("#E6772E"));
				newColors.add(Color.decode("#4DB3B3"));

			} else {
				newColors.add(Color.decode("#d6d9df"));
				newColors.add(Color.decode("#f0f0f0"));
				newColors.add(Color.decode("#d8dbe1"));
			}
			gui.changeColourScheme(newColors);

			frame.dispose();
		}

	}

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
