package Views;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL;

import Controllers.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.ScreenUtils;

public class Visualisation implements ApplicationListener {

	enum VisualisationType {
		TopDown, SideView
	};

	public VisPanel[] panels;
	private int width, height;

	private AssetManager manager;
	private SpriteBatch batch;
	private SpriteBatch controlsBatch;
	private SpriteBatch sideOnViewBatch;
	private Runway runway;
	private String[] assets = { "assets/data/runwayworking.png", "assets/data/sidevis.png", "assets/data/blank.jpg", "assets/data/sideplane.png", "assets/data/topplane.png", "assets/data/sidecar.png", "assets/data/topcar.png", "assets/data/Navigation_Controls/ZoomIn.PNG", "assets/data/Navigation_Controls/ZoomOut.PNG", "assets/data/Navigation_Controls/RightRotate.PNG", "assets/data/Navigation_Controls/LeftRotate.PNG", "assets/data/Navigation_Controls/ResetView.PNG", "assets/data/Navigation_Controls/PanLeft.PNG", "assets/data/Navigation_Controls/PanRight.PNG", "assets/data/Navigation_Controls/TiltUp.PNG", "assets/data/Navigation_Controls/TiltDown.PNG", "assets/data/Navigation_Controls/Compass.PNG", "assets/data/New_Runway_Graphics/DisplacedThreshold.png", "assets/data/New_Runway_Graphics/DisplacedThresholdR.png", "assets/data/New_Runway_Graphics/grassbottom.png", "assets/data/New_Runway_Graphics/ClearedAndGraded.png", "assets/data/New_Runway_Graphics/Runway.PNG", "assets/data/New_Runway_Graphics/sidevis.png", "assets/data/New_Runway_Graphics/ClearwayMarker.PNG", "assets/data/New_Runway_Graphics/ClearwayMarkerR.PNG", "assets/data/New_Runway_Graphics/Clearway.PNG", "assets/data/New_Runway_Graphics/ClearwayR.PNG", "assets/data/New_Runway_Graphics/Stopway.PNG", "assets/data/New_Runway_Graphics/StopwayR.PNG", "assets/data/New_Runway_Graphics/stopwayMarker.jpg", "assets/data/New_Runway_Graphics/stopwayMarkerR.jpg" };

	private FreeTypeFontGenerator generator;
	private static int counter = 1;

	public Visualisation(Runway runway) {
		this.runway = runway;
	}

	public void setSize(int width, int height) {
		this.resize(width, height);
	}

	@Override
	public void resize(int arg0, int arg1) {
		width = arg0;
		height = arg1;
	}

	public void update(Runway newRunway) {
		this.runway = newRunway;
		for (VisPanel panel : panels)
			panel.updateVis(runway);
	}

	// It's LibGDX from here down

	@Override
	public void create() {
		manager = new AssetManager();
		for (String asset : assets)
			manager.load(asset, Texture.class);
		manager.finishLoading();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/data/OSB.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 22;
		BitmapFont largeFont = generator.generateFont(parameter);
		parameter.size = 16;
		BitmapFont smallFont = generator.generateFont(parameter);

		panels = new VisPanel[2];
		panels[1] = new VisPanel(manager, VisualisationType.SideView, width, height / 2, 0, runway, largeFont, smallFont);
		panels[0] = new VisPanel(manager, VisualisationType.TopDown, width, height / 2, height / 2, runway, largeFont, smallFont);
		batch = new SpriteBatch();
		controlsBatch = new SpriteBatch();
		sideOnViewBatch = new SpriteBatch();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		generator.dispose();
		AL.destroy();
		Gdx.app.exit();
	}

	public void checkInput() {
		float change = 0.8f;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
			change = 2.2f;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			for (VisPanel panel : panels) {
				panel.tiltLevel -= change;
			}
		} else if (Gdx.input.isKeyPressed(Keys.UP)) {
			for (VisPanel panel : panels) {
				panel.tiltLevel += change;
			}
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			for (VisPanel panel : panels) {
				panel.panLevel -= change;
			}
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			for (VisPanel panel : panels) {
				panel.panLevel += change;
			}
		} else if (Gdx.input.isKeyPressed(Keys.M)) {
			for (VisPanel panel : panels) {
				panel.zoomLevel += change * 0.005f;
				panel.tiltLevel += change * 1.8;
				panel.panLevel += change * 1.8;
			}
		} else if (Gdx.input.isKeyPressed(Keys.N)) {
			for (VisPanel panel : panels) {
				panel.zoomLevel -= change * 0.005f;
				panel.tiltLevel -= change * 1.8;
				panel.panLevel -= change * 1.8;
			}
		} else if (Gdx.input.isKeyPressed(Keys.P)) {
			for (VisPanel panel : panels) {
				panel.rotation += change * .5f;
				panel.tiltLevel += change * 1.5 * (Math.sin(Math.toRadians(panel.rotation)));
			}
		} else if (Gdx.input.isKeyPressed(Keys.O)) {
			for (VisPanel panel : panels) {
				panel.rotation -= change * .5f;
				panel.tiltLevel -= change * 1.5 * (Math.sin(Math.toRadians(panel.rotation)));
			}
		} else if (Gdx.input.isKeyPressed(Keys.R)) {
			for (VisPanel panel : panels) {
				panel.tiltLevel = 0.0f;
				panel.panLevel = 0.0f;
				panel.zoomLevel = 1.0f;
				panel.rotation = 0.0f;
			}
		} else if (Gdx.input.isKeyPressed(Keys.K)) {
			for (VisPanel panel : panels) {
				float originalRoataion = panel.rotation;
				panel.rotation = (180.0f + (runway.getRunwayID() * 10.0f)) % 360.0f;
				panel.tiltLevel -= (originalRoataion - panel.rotation) * Gdx.graphics.getHeight() / 360.0;
			}
		}

		if (Gdx.input.isTouched()) {
			for (VisPanel panel : panels) {
				int mouseX = (int) (Gdx.input.getX() + (450 - 392) - (Gdx.graphics.getWidth() / 2.0f));
				int mouseY = (int) (-Gdx.input.getY() + (232 - 306) + (Gdx.graphics.getHeight() / 2.0f));
				ArrayList<Integer> buttonXPositions = panel.controlStartX;
				ArrayList<Integer> buttonYPositions = panel.controlStartY;
				int buttonWidth = panel.buttonWidth;

				if (buttonXPositions != null && buttonYPositions != null) {

					int i = 0;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.zoomLevel += change * 0.005f;
						panel.tiltLevel += change * 1.8;
						panel.panLevel += change * 1.8;
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.zoomLevel -= change * 0.005f;
						panel.tiltLevel -= change * 1.8;
						panel.panLevel -= change * 1.8;
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.rotation += change * .5f;
						panel.tiltLevel += change * 1.5 * (Math.sin(Math.toRadians(panel.rotation)));
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.rotation -= change * .5f;
						panel.tiltLevel -= change * 1.5 * (Math.sin(Math.toRadians(panel.rotation)));
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.tiltLevel += change;
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.tiltLevel = 0.0f;
						panel.panLevel = 0.0f;
						panel.zoomLevel = 1.0f;
						panel.rotation = 0.0f;
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.panLevel -= change;
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.panLevel += change;
					}
					i += 1;
					if (isOnButton(mouseX, mouseY, buttonXPositions.get(i), buttonYPositions.get(i), buttonWidth)) {
						panel.tiltLevel -= change;
					}
					i += 1;
					buttonWidth += 7;
					if (mouseX > buttonXPositions.get(i) + buttonWidth && mouseX < buttonXPositions.get(i) + 2.0 * buttonWidth && mouseY > buttonYPositions.get(i) && mouseY < buttonYPositions.get(i) + buttonWidth) {
						float originalRoataion = panel.rotation;
						panel.rotation = (180.0f + (runway.getRunwayID() * 10.0f)) % 360.0f;
						panel.tiltLevel -= (originalRoataion - panel.rotation) * Gdx.graphics.getHeight() / 360.0;
					}
				}
			}
		}
	}

	private boolean isOnButton(int mouseX, int mouseY, int buttonPositionX, int buttonPositionY, int buttonWidth) {
		return mouseX > buttonPositionX + 2.0 * buttonWidth && mouseX < buttonPositionX + 3.0 * buttonWidth && mouseY > buttonPositionY && mouseY < buttonPositionY + buttonWidth;
	}

	public void saveScreenshot() {
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					panels[0].screenshotInProgress = true;
					render();
					FileHandle fh;
					do {
						fh = new FileHandle("screenshot" + counter++ + ".png");
					} while (fh.exists());
					Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
					PixmapIO.writePNG(fh, pixmap);
					pixmap.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
				panels[0].screenshotInProgress = false;
			}
		});
		panels[0].screenshotInProgress = false;
	}

	private Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown) {
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

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batch.begin();
		controlsBatch.begin();
		// sideOnViewBatch.begin();
		for (VisPanel panel : panels) {
			panel.update(Gdx.graphics.getDeltaTime());
			panel.render(batch, controlsBatch, sideOnViewBatch);
		}
		batch.end();
		controlsBatch.end();
		// sideOnViewBatch.end();

		checkInput();
	}

	public void toggleIsLanding() {
		for (VisPanel panel : panels) {
			panel.toggleIsLanding();
		}
	}

	public void toggleIsFromLeft() {
		for (VisPanel panel : panels) {
			panel.toggleApproachFromLeft();
		}
	}
}
