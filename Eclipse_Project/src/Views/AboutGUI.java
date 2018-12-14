package Views;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Listeners.EscapeListener;
import Services.LocalizationService;

public class AboutGUI {

	JDialog frame;
	String aboutMessage;

	public AboutGUI() {

		// intialises and sets the title of the frame
		frame = new JDialog();
		frame.setTitle(LocalizationService.localizeString("about"));

		// import and set the frame icon
		frame.setIconImage(new ImageIcon("src\\assets\\data\\logo.png").getImage());

		// creates the message displayed in the panel
		aboutMessage = LocalizationService.localizeString("about_msg");

		init();
	}

	public void init() {

		JPanel main = new JPanel();
		frame.setContentPane(main);
		main.setLayout(new BorderLayout());

		KeyboardFocusManager kf_m = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kf_m.addKeyEventDispatcher(new EscapeListener(frame));

		// initialises and reads in the logo
		BufferedImage tmpLogo = null;
		try {
			tmpLogo = ImageIO.read(new File("src\\assets\\data\\logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image logo = tmpLogo.getScaledInstance(280, 280, Image.SCALE_SMOOTH);
		JLabel logoLabel = new JLabel(new ImageIcon(logo));

		// creates and formats the text area to be used
		JTextArea textPanel = new JTextArea(20, 10);
		textPanel.setLineWrap(true);
		textPanel.setWrapStyleWord(true);
		textPanel.setBackground(SystemColor.control);
		textPanel.setEditable(false);
		textPanel.setBorder(new EmptyBorder(20, 20, 10, 10));
		textPanel.setText(aboutMessage);
		textPanel.setFont(new Font("Arial", Font.PLAIN, 14));

		// adds components to main panel
		main.add(logoLabel, BorderLayout.WEST);
		main.add(textPanel, BorderLayout.CENTER);

		// formats and makes the GUI
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setModal(true);
		frame.setPreferredSize(new Dimension(650, 370));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
