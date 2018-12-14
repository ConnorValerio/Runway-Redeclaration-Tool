package Listeners;

import java.awt.KeyEventDispatcher;
import java.awt.Window;
import java.awt.event.KeyEvent;

public class EscapeListener implements KeyEventDispatcher {
	private Window frame;

	public EscapeListener(Window frame) {
		this.frame = frame;
	}

	@Override
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
