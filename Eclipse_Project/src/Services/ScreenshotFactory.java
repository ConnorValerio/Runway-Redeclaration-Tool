package Services;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenshotFactory {

	private static int counter = 1;
	private static int JPEGCounter = 1;
	private static int PNGCounter = 1;
	private static int GIFCounter = 1;
	private static Robot robot;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void saveScreenshot() {
		try {
			FileHandle fh;
			do {
				fh = new FileHandle("screenshot" + counter++ + ".png");
			} while (fh.exists());
			Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
			PixmapIO.writePNG(fh, pixmap);
			pixmap.dispose();
		} catch (Exception e) {
		}
	}

	private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown) {
		final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

		if (yDown) {
			// Flip the pixmap upside down
			ByteBuffer pixels = pixmap.getPixels();
			int numBytes = w * h * 4;
			byte[] lines = new byte[numBytes];
			int numBytesPerLine = w * 4;
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
			pixels.clear();
			pixels.put(lines);
		}

		return pixmap;
	}

	public static void saveFullScreenshot(String passedFileType) {

		String fileType = passedFileType;
		BufferedImage img = ScreenshotFactory.captureFullScreenshot();

		if (fileType.equals("JPEG")) {
			ScreenshotFactory.saveAsJPEG(img);

		} else if (fileType.equals("PNG")) {
			ScreenshotFactory.saveAsPNG(img);

		} else if (fileType.equals("GIF")) {
			ScreenshotFactory.saveAsGIF(img);
		}
	}

	// does the print screen work
	private static BufferedImage captureFullScreenshot() {
		return robot.createScreenCapture(new Rectangle(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()));
	}

	// allow full screen shot to be saved as JPEG
	public static void saveAsJPEG(BufferedImage passedImage) {

		File file = new File("full_screenshot" + JPEGCounter++ + ".JPEG");

		try {

			while (file.exists() == true) {
				file = new File("full_screenshot" + JPEGCounter++ + ".JPEG");
			}

			ImageIO.write(passedImage, "JPEG", file);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// allow full screen shot to be saved as PNG
	public static void saveAsPNG(BufferedImage passedImage) {

		File file = new File("full_screenshot" + PNGCounter++ + ".PNG");

		try {

			while (file.exists() == true) {
				file = new File("full_screenshot" + PNGCounter++ + ".PNG");
			}

			ImageIO.write(passedImage, "PNG", file);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// allow full screen shot to be saved as GIF
	public static void saveAsGIF(BufferedImage passedImage) {

		File file = new File("full_screenshot" + GIFCounter++ + ".GIF");

		try {

			while (file.exists() == true) {
				file = new File("full_screenshot" + GIFCounter++ + ".GIF");
			}

			ImageIO.write(passedImage, "GIF", file);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}