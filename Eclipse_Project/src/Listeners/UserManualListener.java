package Listeners;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import Services.LocalizationService;

public class UserManualListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (Desktop.isDesktopSupported()) {
			try {
				File myFile = new File("guide/guide.pdf");
				Desktop.getDesktop().open(myFile);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, LocalizationService.localizeString("no_pdf"), LocalizationService.localizeString("no_pdf_tit"), JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
