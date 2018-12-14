package Views;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controllers.Obstruction;
import Listeners.EscapeListener;
import Models.LogicalRunway;
import Services.LocalizationService;

public class EditObstructionGUI {

	JDialog frame;
	Obstruction obstruction;
	GUI gui;

	// text fields and labels
	JLabel obstructionNameLabel, obstructionLengthLabel, obstructionWidthLabel, obstructionHeightLabel, obstructionPositionAlongRunway, obstructionDistanceFromRunway;

	JTextField obstructionNameTextField, obstructionLengthTextField, obstructionWidthTextField, obstructionHeightTextField, obstructionPositionAlongRunwayTextField, obstructionDistanceFromRunwayTextField;

	JButton doneButton, cancelButton;

	public EditObstructionGUI(Obstruction passedObstruction, GUI passedGUI) {

		this.obstruction = passedObstruction;
		this.gui = passedGUI;

		frame = new JDialog();
		frame.setTitle(LocalizationService.localizeString("edit_obstr_pos"));

		obstructionNameLabel = new JLabel(LocalizationService.localizeString("obstr_name"));
		obstructionLengthLabel = new JLabel(LocalizationService.localizeString("obstr_length"));
		obstructionWidthLabel = new JLabel(LocalizationService.localizeString("obstr_width"));
		obstructionHeightLabel = new JLabel(LocalizationService.localizeString("obstr_height"));
		obstructionPositionAlongRunway = new JLabel(LocalizationService.localizeString("obstr_along"));
		obstructionDistanceFromRunway = new JLabel(LocalizationService.localizeString("obstr_from"));

		obstructionNameTextField = new JTextField(10);
		obstructionLengthTextField = new JTextField(10);
		obstructionWidthTextField = new JTextField(10);
		obstructionHeightTextField = new JTextField(10);
		obstructionPositionAlongRunwayTextField = new JTextField(10);
		obstructionDistanceFromRunwayTextField = new JTextField(10);
		obstructionNameTextField.setEditable(false);
		obstructionLengthTextField.setEditable(false);
		obstructionWidthTextField.setEditable(false);
		obstructionHeightTextField.setEditable(false);

		obstructionNameTextField.setText(obstruction.getName());
		obstructionLengthTextField.setText(Double.toString(obstruction.getLength()));
		obstructionWidthTextField.setText(Double.toString(obstruction.getWidth()));
		obstructionHeightTextField.setText(Double.toString(obstruction.getHeight()));
		obstructionPositionAlongRunwayTextField.setText(Double.toString(obstruction.getPositionAlongRunway()));
		obstructionDistanceFromRunwayTextField.setText(Double.toString(obstruction.getDistanceFromRunway()));

		doneButton = new JButton(LocalizationService.localizeString("done"));
		cancelButton = new JButton(LocalizationService.localizeString("cancel"));

		doneButton.setPreferredSize(new Dimension(160, 25));
		cancelButton.setPreferredSize(new Dimension(160, 25));

		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String posX = obstruction.getPositionAlongRunway() + "";
				String posY = obstruction.getDistanceFromRunway() + "";

				if (obstructionPositionAlongRunwayTextField.getText().equals(posX) && obstructionDistanceFromRunwayTextField.getText().equals(posY)) {
					JOptionPane.showMessageDialog(null, LocalizationService.localizeString("no_change"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				if (!isDouble(obstructionPositionAlongRunwayTextField.getText())) {
					JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_along"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (!isDouble(obstructionDistanceFromRunwayTextField.getText())) {
					JOptionPane.showMessageDialog(null, LocalizationService.localizeString("valid_from"), LocalizationService.localizeString("error"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				String obName = obstructionNameTextField.getText();
				Double obWidth = Double.parseDouble(obstructionWidthTextField.getText());
				Double obHeight = Double.parseDouble(obstructionHeightTextField.getText());
				Double obLength = Double.parseDouble(obstructionLengthTextField.getText());
				Double obPositionAlongRunway = Double.parseDouble(obstructionPositionAlongRunwayTextField.getText());
				Double obDistanceFromRunway = Double.parseDouble(obstructionDistanceFromRunwayTextField.getText());

				Obstruction editedObstruction = new Obstruction(obName, obWidth, obHeight, obLength, obPositionAlongRunway, obDistanceFromRunway);

				gui.setCurrentObstruction(editedObstruction);
				gui.runway.addObstruction(editedObstruction);
				gui.runway.redeclareParameters();

				// update calculations
				String[] parameterNames = new String[6];
				gui.runway.redeclareParameters();
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
				gui.addUserNotification("The Position of the Current Obstruction (" + obstruction.getName() + ") has been edited");
				frame.dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});

		init();
	}

	public void init() {
		JPanel main = new JPanel();
		frame.setContentPane(main);
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		main.setBorder(new EmptyBorder(10, 20, 20, 20));

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new EscapeListener(frame));

		// import and set the frame icon
		frame.setIconImage(new ImageIcon("src\\assets\\data\\logo.png").getImage());

		// main content panel
		JPanel contentPanel = new JPanel(new GridLayout(6, 2, 10, 5));
		Arrays.asList(obstructionNameLabel, obstructionNameTextField, obstructionWidthLabel, obstructionWidthTextField, obstructionHeightLabel, obstructionHeightTextField, obstructionLengthLabel, obstructionLengthTextField, obstructionPositionAlongRunway, obstructionPositionAlongRunwayTextField, obstructionDistanceFromRunway, obstructionDistanceFromRunwayTextField).forEach(contentPanel::add);

		// bottom panel - buttons
		JPanel bottom = new JPanel(new GridLayout(1, 2, 10, 5));
		doneButton.setPreferredSize(new Dimension(160, 25));
		cancelButton.setPreferredSize(new Dimension(160, 25));
		bottom.add(doneButton);
		bottom.add(cancelButton);

		main.add(contentPanel);
		main.add(Box.createRigidArea(new Dimension(0, 10)));
		main.add(bottom);

		// Setting Colors
		ArrayList<Color> backgroundColors = gui.getColourScheme();

		main.setBackground(backgroundColors.get(0));
		contentPanel.setBackground(backgroundColors.get(0));
		bottom.setBackground(backgroundColors.get(0));

		doneButton.setBackground(backgroundColors.get(2));
		cancelButton.setBackground(backgroundColors.get(2));

		obstructionNameTextField.setBackground(backgroundColors.get(1));
		obstructionLengthTextField.setBackground(backgroundColors.get(1));
		obstructionWidthTextField.setBackground(backgroundColors.get(1));
		obstructionHeightTextField.setBackground(backgroundColors.get(1));
		obstructionPositionAlongRunwayTextField.setBackground(backgroundColors.get(1));
		obstructionDistanceFromRunwayTextField.setBackground(backgroundColors.get(1));

		// formats and makes the GUI
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setModal(true);
		frame.setPreferredSize(new Dimension(450, 300));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

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
}
