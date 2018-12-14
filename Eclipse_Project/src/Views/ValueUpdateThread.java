package Views;

import java.awt.MouseInfo;

import javax.swing.JTextField;

public class ValueUpdateThread extends Thread {

	Double changeFactor = 0.5;

	JTextField toUpdate;
	double originalMouseX;
	double originalTextFieldValue;

	// used to decide whether to run the code inside the thread
	boolean isRunning;

	public ValueUpdateThread(JTextField toUpdate) {
		this.toUpdate = toUpdate;
		this.isRunning = true;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				originalMouseX = MouseInfo.getPointerInfo().getLocation().getX();
				originalTextFieldValue = Double.parseDouble(toUpdate.getText());

				while (isRunning) {
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Double newMouseX = MouseInfo.getPointerInfo().getLocation().getX();
					Double newValue = originalTextFieldValue + ((newMouseX - originalMouseX) / 5) * changeFactor;
					newValue = Math.round(newValue - 0.5) + 0.5;
					toUpdate.setText(newValue.toString());
				}

			} catch (NumberFormatException e) {

			}
		}

	}

	// stops the thread from executing its run() code
	public void kill() {
		this.isRunning = false;
	}

}
