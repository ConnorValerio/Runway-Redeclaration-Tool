package Controllers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import Views.WelcomeGUI;

public class Main {
	public static void main(String[] args) {

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				WelcomeGUI welcome = new WelcomeGUI();
				welcome.init();

				/***** LOG FILES *****/

				// gets date in correct format
				SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
				Date date = new Date();
				String today = sdfdate.format(date);

				// creates file
				String filename = today + ".log";
				File file = new File(filename);

				// creates timestamp
				SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
				String time = sdftime.format(date);

				if (file.exists() == true) {

					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));

						// print + close
						String content = time + "      Runway Re-Declaration Tool Opened.";
						pw.println(content);
						pw.close();

					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else {

					try {

						file.createNewFile();

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));

						// print + close
						String content1 = time + "      New log file created for today.";
						String content2 = time + "      Runway Re-Declaration Tool Opened.";

						pw.println(content1);
						pw.println(content2);
						pw.close();

					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			}

		});
	}
}