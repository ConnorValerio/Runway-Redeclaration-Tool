package Views;

import java.util.ArrayList;

import Controllers.*;
import Models.LogicalRunway;
import Services.CalculationService;
import Views.Visualisation.VisualisationType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.*;

public class VisPanel {

	private static final int SIDE_VIEW_GROUND_HEIGHT = 150;
	private static final int TOP_VIEW_LEFT_START = 70;
	private static final int TOP_VIEW_RIGHT_START = 830;

	private VisualisationType currentDisplay;
	private Sprite background, clearwayL, stopwayL, clearwayR, stopwayR, clearwayMarkerL, stopwayMarkerL, clearwayMarkerR, stopwayMarkerR, clearedNGraded, runwayImage, displacedThreshold, displacedThresholdR, sideOnBackground, currentPlane, currentObstacle;
	private ArrayList<Sprite> controls;
	public ArrayList<Integer> controlStartX, controlStartY, controlWidth;
	private LogicalRunway currentLogicalRunway;
	private AssetManager manager;
	private int width, height, offsetY;
	private Runway runway;
	private boolean isApproachingFromLeft = true;
	private boolean isLanding = false;
	private BitmapFont[] fonts;
	private OrthographicCamera cam;
	private OrthographicCamera camFixed;
	public double zoomLevel = 1.0;
	public float panLevel = +0.0f;
	public float tiltLevel = +00.0f;
	public float rotation = +00.0f;
	public int buttonWidth = 20;

	private String landingAvoidDistanceString;
	private String takeOffAvoidDistanceString;

	public boolean screenshotInProgress = false;

	public VisPanel(AssetManager manager, VisualisationType type, int width, int height, int offsetY, Runway runway, BitmapFont big, BitmapFont small) {
		this.manager = manager;
		this.currentDisplay = type;
		this.width = width;
		this.height = height;
		this.offsetY = offsetY;
		this.runway = runway;
		this.isApproachingFromLeft = true;
		this.fonts = new BitmapFont[2];
		this.fonts[0] = big;
		this.fonts[1] = small;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();
		camFixed = new OrthographicCamera(w, h);
		camFixed.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		camFixed.update();
		setBackdrop();
	}

	public void updateVis(Runway runway) {
		this.runway = runway;
		setBackdrop();
	}

	private boolean isTopDown() {
		return currentDisplay == VisualisationType.TopDown;
	}

	private void setBackdrop() {

		// Defines sprites for runway components:

		this.background = new Sprite(manager.get("assets/data/New_Runway_Graphics/grassbottom.png", Texture.class));
		this.clearwayL = new Sprite(manager.get("assets/data/New_Runway_Graphics/Clearway.PNG", Texture.class));
		this.stopwayL = new Sprite(manager.get("assets/data/New_Runway_Graphics/Stopway.PNG", Texture.class));
		this.clearwayMarkerL = new Sprite(manager.get("assets/data/New_Runway_Graphics/ClearwayMarker.PNG", Texture.class));
		this.stopwayMarkerL = new Sprite(manager.get("assets/data/New_Runway_Graphics/stopwayMarker.jpg", Texture.class));
		this.clearwayR = new Sprite(manager.get("assets/data/New_Runway_Graphics/ClearwayR.PNG", Texture.class));
		this.stopwayR = new Sprite(manager.get("assets/data/New_Runway_Graphics/StopwayR.PNG", Texture.class));
		this.clearwayMarkerR = new Sprite(manager.get("assets/data/New_Runway_Graphics/ClearwayMarkerR.PNG", Texture.class));
		this.stopwayMarkerR = new Sprite(manager.get("assets/data/New_Runway_Graphics/stopwayMarkerR.jpg", Texture.class));
		this.runwayImage = new Sprite(manager.get("assets/data/New_Runway_Graphics/Runway.PNG", Texture.class));
		this.sideOnBackground = new Sprite(manager.get("assets/data/New_Runway_Graphics/sidevis.png", Texture.class));
		this.clearedNGraded = new Sprite(manager.get("assets/data/New_Runway_Graphics/ClearedAndGraded.png", Texture.class));
		this.displacedThreshold = new Sprite(manager.get("assets/data/New_Runway_Graphics/DisplacedThreshold.png", Texture.class));
		this.displacedThresholdR = new Sprite(manager.get("assets/data/New_Runway_Graphics/DisplacedThresholdR.png", Texture.class));

		// Set the sizes and positions of controls and obstructions:

		switch (currentDisplay) {
		case TopDown:
			this.currentPlane = new Sprite(manager.get("assets/data/topplane.png", Texture.class));
			this.currentObstacle = new Sprite(manager.get("assets/data/topcar.png", Texture.class));
			this.controls = new ArrayList<Sprite>();
			this.controlStartX = new ArrayList<Integer>();
			this.controlStartY = new ArrayList<Integer>();
			this.controlWidth = new ArrayList<Integer>();
			int buttonPosX = (int) (Gdx.graphics.getWidth() / 2.0f) - 40;
			int nextButtonPosY = (int) ((Gdx.graphics.getHeight() / 2.0f) - 100);
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/ZoomIn.PNG", Texture.class)));
			nextButtonPosY -= buttonWidth;
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/ZoomOut.PNG", Texture.class)));
			nextButtonPosY -= buttonWidth * 1.5;
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/RightRotate.PNG", Texture.class)));
			nextButtonPosY -= buttonWidth;
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/LeftRotate.PNG", Texture.class)));
			nextButtonPosY -= buttonWidth * 1.5;
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/TiltUp.PNG", Texture.class)));
			nextButtonPosY -= buttonWidth;
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/ResetView.PNG", Texture.class)));
			this.controlStartX.add(buttonPosX - buttonWidth);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/PanLeft.PNG", Texture.class)));
			this.controlStartX.add(buttonPosX + buttonWidth);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/PanRight.PNG", Texture.class)));
			nextButtonPosY -= buttonWidth;
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth - 7);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/TiltDown.PNG", Texture.class)));
			nextButtonPosY -= buttonWidth * 2.0;
			this.controlStartX.add(buttonPosX);
			this.controlStartY.add(nextButtonPosY);
			this.controlWidth.add(buttonWidth);
			this.controls.add(new Sprite(manager.get("assets/data/Navigation_Controls/Compass.PNG", Texture.class)));
			break;
		case SideView:
			this.currentPlane = new Sprite(manager.get("assets/data/sideplane.png", Texture.class));
			this.currentObstacle = new Sprite(manager.get("assets/data/sidecar.png", Texture.class));
			break;
		default:
			break;
		}

		// Set the sizes and positions of runway components:

		int runwayDisplayWidth = 30;
		double leftStopwayLength = runway.getOriginalLogicalRunwayFromRight().stopwayLength;
		double rightStopwayLength = runway.getOriginalLogicalRunwayFromLeft().stopwayLength;
		double leftClearwayLength = runway.getOriginalLogicalRunwayFromRight().clearwayLength;
		double rightClearwayLength = runway.getOriginalLogicalRunwayFromLeft().clearwayLength;
		double runwayLength = runway.getOriginalLogicalRunwayFromLeft().lda;
		double totalLength = leftStopwayLength + runwayLength + rightStopwayLength;

		switch (currentDisplay) {
		case TopDown:
			int yPos = (height * 4 / 3) + (runwayDisplayWidth / 2) + 39;
			this.background.setSize(width, height);
			this.background.setPosition(0, offsetY);
			this.stopwayL.setSize((int) (width * (leftStopwayLength / totalLength)), (int) (runwayDisplayWidth));
			this.stopwayL.setPosition(0, yPos);
			this.stopwayR.setSize((int) (width * (rightStopwayLength / totalLength)), (int) (runwayDisplayWidth));
			this.stopwayR.setPosition((int) (width * ((leftStopwayLength + runwayLength) / totalLength)), yPos);
			this.runwayImage.setSize((int) (width * (runwayLength / totalLength)), (int) (runwayDisplayWidth));
			this.runwayImage.setPosition((int) (width * (leftStopwayLength / totalLength)), yPos);
			this.clearwayL.setSize((int) (width * (leftClearwayLength / totalLength)), (int) (runwayDisplayWidth * 3.0));
			this.clearwayL.setPosition((int) (width * ((leftStopwayLength - leftClearwayLength) / totalLength)), yPos - runwayDisplayWidth);
			this.clearwayR.setSize((int) (width * (rightClearwayLength / totalLength)), (int) (runwayDisplayWidth * 3.0));
			this.clearwayR.setPosition((int) (width * ((leftStopwayLength + runwayLength) / totalLength)), yPos - runwayDisplayWidth);
			this.clearedNGraded.setSize(width, (int) (runwayDisplayWidth * 6.0));
			this.clearedNGraded.setPosition(0, (int) (yPos - runwayDisplayWidth * 2.5));
			this.displacedThreshold.setSize((int) (width * (runwayLength / (totalLength * 10))), (int) (runwayDisplayWidth));
			this.displacedThreshold.setPosition(-50000, yPos);
			this.displacedThresholdR.setSize((int) (width * (runwayLength / (totalLength * 10))), (int) (runwayDisplayWidth));
			this.displacedThresholdR.setPosition(-50000, yPos);
			break;

		case SideView:
			int yPosS = 117 + (runwayDisplayWidth / 2);
			int markerSize = 5;
			this.sideOnBackground.setSize(width, height);
			this.sideOnBackground.setPosition(0, offsetY);
			this.clearwayMarkerL.setSize((int) (width * (leftClearwayLength / totalLength)), markerSize);
			this.clearwayMarkerL.setPosition((int) (width * ((leftStopwayLength - leftClearwayLength) / totalLength)), yPosS);
			this.clearwayMarkerR.setSize((int) (width * (rightClearwayLength / totalLength)), markerSize);
			this.clearwayMarkerR.setPosition((int) (width * ((leftStopwayLength + runwayLength) / totalLength)), yPosS);
			this.stopwayMarkerL.setSize((int) (width * (leftStopwayLength / totalLength)), markerSize);
			this.stopwayMarkerL.setPosition(0, yPosS + markerSize);
			this.stopwayMarkerR.setSize((int) (width * (rightStopwayLength / totalLength)), markerSize);
			this.stopwayMarkerR.setPosition((int) (width * ((leftStopwayLength + runwayLength) / totalLength)), yPosS + markerSize);
			break;
		}

		this.currentLogicalRunway = runway.getLeftLandLogicalRunway();
		currentPlane.setScale(0.2f);
		this.currentPlane.setPosition(0, isTopDown() ? offsetY - 210 : offsetY + 200);
	}

	private boolean isObstacleValid() {
		return runway.getFirstObstruction() != null && runway.getFirstObstruction().getWidth() > 0.01;
	}

	public void render(SpriteBatch batch, SpriteBatch controlsBatch, SpriteBatch sideOnViewBatch) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		if (isTopDown()) {
			cam = new OrthographicCamera((int) (w / zoomLevel), (int) (h / zoomLevel));
			cam.position.set((cam.viewportWidth / 2f) + panLevel, (cam.viewportHeight / 2f) + tiltLevel + 35, 0);
			cam.rotate(rotation);
			cam.update();
			batch.setProjectionMatrix(cam.combined);
		} else {
			cam = new OrthographicCamera(w, h);
			cam.position.set((cam.viewportWidth / 2f), (cam.viewportHeight / 2f), 0);
			cam.rotate(0.0f);
			cam.update();
			batch.setProjectionMatrix(cam.combined);
		}

		CharSequence leftThreshold;

		if (runway.getRunwayID() < 10) {
			leftThreshold = runway.getRunwayName() + " (0" + runway.getRunwayID() + "L)";
		} else {
			leftThreshold = runway.getRunwayName() + " (" + runway.getRunwayID() + "L)";
		}

		CharSequence rightThreshold = runway.getRunwayName() + " (" + (runway.getRunwayID() + 18) + "R)";

		// decide on x position of text depending on length
		int textLength = leftThreshold.length();
		int textPositionX = width - (14 * textLength);

		// Draws sprites (previously prepared above):
		switch (currentDisplay) {
		case TopDown:
			background.draw(batch);
			clearedNGraded.draw(batch);
			runwayImage.draw(batch);
			clearwayL.draw(batch);
			stopwayL.draw(batch);
			clearwayR.draw(batch);
			stopwayR.draw(batch);
			break;
		case SideView:
			sideOnBackground.draw(batch);
			clearwayMarkerL.draw(batch);
			stopwayMarkerL.draw(batch);
			clearwayMarkerR.draw(batch);
			stopwayMarkerR.draw(batch);
			break;
		}

		if (isObstacleValid()) {
			currentObstacle.draw(batch);
			drawArrows(batch);
		}
		currentPlane.draw(batch);
		fonts[0].draw(batch, leftThreshold, 15, offsetY + 40);
		fonts[0].draw(batch, rightThreshold, textPositionX, offsetY + 40);
		drawControls(controlsBatch);

	}

	private void drawStopperedLine(ShapeRenderer r, Vector2 start, Vector2 end, float height, int thickness) {
		start.y -= (transformToScreenCoords(new Vector2(0.0f, 0.0f))).y;
		end.y -= (transformToScreenCoords(new Vector2(0.0f, 0.0f))).y;
		r.rectLine(start.x, start.y + height, end.x, end.y + height, 3);
		r.rectLine(start.x, start.y + height - 10, start.x, end.y + height + 10, 3);
		r.rectLine(end.x, start.y + height - 10, end.x, end.y + height + 10, 3);
	}

	private void drawArrows(SpriteBatch batch) {
		batch.end();
		ShapeRenderer r = new ShapeRenderer();
		float w2 = Gdx.graphics.getWidth();
		float h2 = Gdx.graphics.getHeight();
		if (isTopDown()) {
			cam = new OrthographicCamera((int) (w2 / zoomLevel), (int) (h2 / zoomLevel));
			cam.position.set((cam.viewportWidth / 2f) + panLevel, (cam.viewportHeight / 2f) + tiltLevel + 35, 0);
			cam.rotate(rotation);
			cam.update();
			r.setProjectionMatrix(cam.combined);
		} else {
			cam = new OrthographicCamera(w2, h2);
			cam.position.set((cam.viewportWidth / 2f), (cam.viewportHeight / 2f), 0);
			cam.rotate(0.0f);
			cam.update();
			r.setProjectionMatrix(cam.combined);
		}
		r.begin(ShapeType.Filled);

		float ldaHeight = (this.isTopDown() ? 100 : 70) + offsetY;
		float toraHeight = (this.isTopDown() ? 250 : 250) + offsetY;
		float asdaHeight = (this.isTopDown() ? 280 : 280) + offsetY;
		float todaHeight = (this.isTopDown() ? 310 : 310) + offsetY;
		float avoidHeight = (this.isTopDown() ? 230 : 180) + offsetY;

		Vector2 ldaStart = Vector2.Zero, ldaEnd = Vector2.Zero;
		Vector2 landingThreshold = Vector2.Zero, takeOffThreshold = Vector2.Zero;
		Vector2 toraStart = Vector2.Zero, toraEnd = Vector2.Zero;
		Vector2 todaStart = Vector2.Zero, todaEnd = Vector2.Zero;
		Vector2 landingAsdaStart = Vector2.Zero, landingAsdaEnd = Vector2.Zero;
		Vector2 takeOffAsdaStart = Vector2.Zero, takeOffAsdaEnd = Vector2.Zero;
		Vector2 landingAvoidDistanceStart = Vector2.Zero, landingAvoidDistanceEnd = Vector2.Zero;
		Vector2 takeOffAvoidDistanceStart = Vector2.Zero, takeOffAvoidDistanceEnd = Vector2.Zero;

		// transformToScreenCoords TAKES COORDINATES FROM THE POINT WHERE THE
		// LEFT STOPWAY BEGINS

		double leftStopwayLength = runway.getOriginalLogicalRunwayFromRight().stopwayLength;
		double rightStopwayLength = runway.getOriginalLogicalRunwayFromLeft().stopwayLength;
		double leftClearwayLength = runway.getOriginalLogicalRunwayFromRight().clearwayLength;
		double rightClearwayLength = runway.getOriginalLogicalRunwayFromLeft().clearwayLength;
		double runwayLength = runway.getOriginalLogicalRunwayFromLeft().lda;

		if (CalculationService.obstacleIsOnLeft(runway.getFirstObstruction(), runway)) {/* obstacle_on_left */
			if (isApproachingFromLeft) {/* land_from_left DIAGRAM 1 */
				this.currentLogicalRunway = runway.getLeftLandLogicalRunway().clone();
				landingAsdaEnd = ((new Vector2((float) (runwayLength + leftStopwayLength + rightStopwayLength), 0)));

				// als / Tocs:
				landingAvoidDistanceStart = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() + 0.5 * runway.getFirstObstruction().getLength()), 0.5f);
				landingAvoidDistanceEnd = new Vector2((float) (landingAvoidDistanceStart.x + runway.getLeftLandLogicalRunway().resa + 60.0f), 0.0f);
				landingAvoidDistanceStart = transformToScreenCoords(landingAvoidDistanceStart);
				landingAvoidDistanceEnd = transformToScreenCoords(landingAvoidDistanceEnd);
				landingAvoidDistanceString = "ALS/TOCS";
			} else {/* land_from_right DIAGRAM 3 */
				this.currentLogicalRunway = runway.getRightLandLogicalRunway().clone();
				landingAsdaEnd = ((new Vector2((float) (runwayLength + leftStopwayLength), 0)));

				// avoidDistance:
				landingAvoidDistanceStart = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() + 0.5 * runway.getFirstObstruction().getLength()), 0.0f);
				landingAvoidDistanceEnd = new Vector2((float) (landingAvoidDistanceStart.x + runway.getLeftLandLogicalRunway().resa + 60.0f), 0.0f);
				landingAvoidDistanceStart = transformToScreenCoords(landingAvoidDistanceStart);
				landingAvoidDistanceEnd = transformToScreenCoords(landingAvoidDistanceEnd);
				landingAvoidDistanceString = "AVOID";
			}
			ldaEnd = ((new Vector2((float) (runwayLength + leftStopwayLength), 0)));

			landingThreshold = (new Vector2(
			// This is threshold + leftStopwayLength
			(float) (currentLogicalRunway.displacedThreshold + leftStopwayLength), 0));
			ldaStart = (new Vector2((float) (ldaEnd.x - currentLogicalRunway.lda), 0));
			landingAsdaStart = (new Vector2((float) (landingAsdaEnd.x - currentLogicalRunway.asda), 0));

			if (isApproachingFromLeft) {/* takeOff_from_left DIAGRAM 5 */
				this.currentLogicalRunway = runway.getLeftTakeOffLogicalRunway().clone();
				todaEnd = (new Vector2((float) (leftStopwayLength + runwayLength + rightClearwayLength), 0));
				takeOffAsdaEnd = (new Vector2((float) (leftStopwayLength + runwayLength + rightStopwayLength), 0));

				// avoidDistance:
				takeOffAvoidDistanceStart = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() + 0.5 * runway.getFirstObstruction().getLength()), 0.0f);
				takeOffAvoidDistanceEnd = new Vector2((float) (takeOffAvoidDistanceStart.x + runway.getBlastDistance()), 0.0f);
				takeOffAvoidDistanceStart = transformToScreenCoords(takeOffAvoidDistanceStart);
				takeOffAvoidDistanceEnd = transformToScreenCoords(takeOffAvoidDistanceEnd);
				takeOffAvoidDistanceString = "BLAST";
				this.displacedThreshold.setPosition((float) (takeOffAvoidDistanceEnd.x - displacedThreshold.getWidth()), displacedThreshold.getY());
				
			} else {/* takeOff_from_right DIAGRAM 7 */
				this.currentLogicalRunway = runway.getRightTakeOffLogicalRunway().clone();
				todaEnd = (new Vector2((float) (leftStopwayLength + runwayLength), 0));
				takeOffAsdaEnd = (new Vector2((float) (leftStopwayLength + runwayLength), 0));

				// als/Tocs:
				takeOffAvoidDistanceStart = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() + 0.5 * runway.getFirstObstruction().getLength()), 0.5f);
				takeOffAvoidDistanceEnd = new Vector2((float) (takeOffAvoidDistanceStart.x + currentLogicalRunway.resa + 60.0f), 0.0f);
				takeOffAvoidDistanceStart = transformToScreenCoords(takeOffAvoidDistanceStart);
				takeOffAvoidDistanceEnd = transformToScreenCoords(takeOffAvoidDistanceEnd);
				takeOffAvoidDistanceString = "ALS/TOCS";
			}
			toraEnd = (new Vector2((float) (leftStopwayLength + runwayLength), 0));

			takeOffThreshold = (new Vector2(
			// This is threshold + leftStopwayLength
			(float) (currentLogicalRunway.displacedThreshold + leftStopwayLength), 0));
			toraStart = (new Vector2((float) (toraEnd.x - currentLogicalRunway.tora), 0));
			todaStart = (new Vector2((float) (todaEnd.x - currentLogicalRunway.toda), 0));
			takeOffAsdaStart = (new Vector2((float) (takeOffAsdaEnd.x - currentLogicalRunway.asda), 0));

		} else {/* obstacle_on_right */
			if (isApproachingFromLeft) {/* land_from_left DIAGRAM 2 */
				this.currentLogicalRunway = runway.getLeftLandLogicalRunway().clone();
				landingAsdaStart = (new Vector2((float) leftStopwayLength, 0));

				// avoidDistance:
				landingAvoidDistanceEnd = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() - 0.5 * runway.getFirstObstruction().getLength()), 0.0f);
				landingAvoidDistanceStart = new Vector2((float) (landingAvoidDistanceEnd.x - (currentLogicalRunway.resa + 60.0f)), 0.0f);
				landingAvoidDistanceStart = transformToScreenCoords(landingAvoidDistanceStart);
				landingAvoidDistanceEnd = transformToScreenCoords(landingAvoidDistanceEnd);
				landingAvoidDistanceString = "AVOID";
			} else {/* land_from_right DIAGRAM 4 */
				this.currentLogicalRunway = runway.getRightLandLogicalRunway().clone();
				landingAsdaStart = (new Vector2(0.0f, 0));

				// als/tocs:
				landingAvoidDistanceEnd = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() - 0.5 * runway.getFirstObstruction().getLength()), 0.5f);
				landingAvoidDistanceStart = new Vector2((float) (landingAvoidDistanceEnd.x - (currentLogicalRunway.resa + 60.0f)), 0.0f);
				landingAvoidDistanceStart = transformToScreenCoords(landingAvoidDistanceStart);
				landingAvoidDistanceEnd = transformToScreenCoords(landingAvoidDistanceEnd);
				landingAvoidDistanceString = "ALS/TOCS";
			}
			ldaStart = (new Vector2((float) leftStopwayLength, 0));

			landingThreshold = (new Vector2(
			// This is threshold + leftStopwayLength
			(float) (currentLogicalRunway.displacedThreshold + leftStopwayLength), 0));
			ldaEnd = (new Vector2((float) (ldaStart.x + currentLogicalRunway.lda), 0));
			landingAsdaEnd = (new Vector2((float) (landingAsdaStart.x + currentLogicalRunway.asda), 0));

			if (isApproachingFromLeft) {/* takeOff_from_left DIAGRAM 6 */
				this.currentLogicalRunway = runway.getLeftTakeOffLogicalRunway().clone();
				todaStart = (new Vector2((float) leftStopwayLength, 0));
				takeOffAsdaStart = (new Vector2((float) leftStopwayLength, 0));

				// als/tocs:
				takeOffAvoidDistanceEnd = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() - 0.5 * runway.getFirstObstruction().getLength()), 0.5f);
				takeOffAvoidDistanceStart = new Vector2((float) (takeOffAvoidDistanceEnd.x - (currentLogicalRunway.resa + 60.0f)), 0.0f);
				takeOffAvoidDistanceStart = transformToScreenCoords(takeOffAvoidDistanceStart);
				takeOffAvoidDistanceEnd = transformToScreenCoords(takeOffAvoidDistanceEnd);
				takeOffAvoidDistanceString = "ALS/TOCS";
			} else {/* takeOff_from_right DIAGRAM 8 */
				this.currentLogicalRunway = runway.getRightTakeOffLogicalRunway().clone();
				todaStart = (new Vector2((float) (leftStopwayLength - leftClearwayLength), 0));
				takeOffAsdaStart = (new Vector2(0.0f, 0));

				// blastDistance:
				takeOffAvoidDistanceEnd = new Vector2((float) (runway.getFirstObstruction().getPositionAlongRunway() - 0.5 * runway.getFirstObstruction().getLength()), 0.0f);
				takeOffAvoidDistanceStart = new Vector2((float) (takeOffAvoidDistanceEnd.x - runway.getBlastDistance()), 0.0f);
				takeOffAvoidDistanceStart = transformToScreenCoords(takeOffAvoidDistanceStart);
				takeOffAvoidDistanceEnd = transformToScreenCoords(takeOffAvoidDistanceEnd);
				takeOffAvoidDistanceString = "BLAST";

				this.displacedThresholdR.setPosition((float) (takeOffAvoidDistanceStart.x), displacedThreshold.getY());
			}
			toraStart = (new Vector2((float) leftStopwayLength, 0));

			takeOffThreshold = (new Vector2(
			// This is threshold + leftStopwayLength
			(float) (currentLogicalRunway.displacedThreshold + leftStopwayLength), 0));
			toraEnd = (new Vector2((float) (toraStart.x + currentLogicalRunway.tora), 0));
			todaEnd = (new Vector2((float) (todaStart.x + currentLogicalRunway.toda), 0));
			takeOffAsdaEnd = (new Vector2((float) (takeOffAsdaStart.x + currentLogicalRunway.asda), 0));
		}

		landingAsdaEnd = transformToScreenCoords(landingAsdaEnd);
		ldaEnd = transformToScreenCoords(ldaEnd);
		landingThreshold = transformToScreenCoords(landingThreshold);
		ldaStart = transformToScreenCoords(ldaStart);
		landingAsdaStart = transformToScreenCoords(landingAsdaStart);
		todaEnd = transformToScreenCoords(todaEnd);
		takeOffAsdaEnd = transformToScreenCoords(takeOffAsdaEnd);
		toraEnd = transformToScreenCoords(toraEnd);
		takeOffThreshold = transformToScreenCoords(takeOffThreshold);
		toraStart = transformToScreenCoords(toraStart);
		todaStart = transformToScreenCoords(todaStart);
		takeOffAsdaStart = transformToScreenCoords(takeOffAsdaStart);

		if (!isTopDown()) {
			Obstruction o = runway.getFirstObstruction();
			if (isObstacleValid()) {
				Vector2 transCoords = (transformToScreenCoords(o.getPositionAlongRunway(), o.getDistanceFromRunway()));
			}
		}

		if (isTopDown()) {
			landingAvoidDistanceStart = new Vector2(landingAvoidDistanceStart.x, (transformToScreenCoords(new Vector2(0.0f, 0.0f))).y);
			landingAvoidDistanceEnd = new Vector2(landingAvoidDistanceEnd.x, (transformToScreenCoords(new Vector2(0.0f, 0.0f))).y);
			takeOffAvoidDistanceStart = new Vector2(takeOffAvoidDistanceStart.x, (transformToScreenCoords(new Vector2(0.0f, 0.0f))).y);
			takeOffAvoidDistanceEnd = new Vector2(takeOffAvoidDistanceEnd.x, (transformToScreenCoords(new Vector2(0.0f, 0.0f))).y);
			batch.begin();
			if (isApproachingFromLeft && CalculationService.obstacleIsOnLeft(runway.getFirstObstruction(), runway) && !isLanding)
				displacedThreshold.draw(batch);
			if (!isApproachingFromLeft && !CalculationService.obstacleIsOnLeft(runway.getFirstObstruction(), runway) && !isLanding)
				displacedThresholdR.draw(batch);
			batch.end();
		}

		if (isLanding) {
			drawStopperedLine(r, ldaStart, ldaEnd, ldaHeight, 3);
			drawStopperedLine(r, landingAsdaStart, landingAsdaEnd, asdaHeight, 3);
			drawStopperedLine(r, landingAvoidDistanceStart, landingAvoidDistanceEnd, avoidHeight, 3);
		} else {
			drawStopperedLine(r, toraStart, toraEnd, toraHeight, 3);
			drawStopperedLine(r, todaStart, todaEnd, todaHeight, 3);
			drawStopperedLine(r, takeOffAsdaStart, takeOffAsdaEnd, asdaHeight, 3);
			drawStopperedLine(r, takeOffAvoidDistanceStart, takeOffAvoidDistanceEnd, avoidHeight, 3);
		}

		r.end();
		batch.begin();
		if (isLanding) {
			fonts[1].draw(batch, "LDA", (ldaStart.x + ldaEnd.x) / 2f - 15, ldaHeight - 10);
			fonts[1].draw(batch, "ASDA", (landingAsdaStart.x + landingAsdaEnd.x) / 2f - 15, asdaHeight - 10);
			fonts[1].draw(batch, landingAvoidDistanceString, (landingAvoidDistanceStart.x + landingAvoidDistanceEnd.x) / 2f - 15, (float) avoidHeight);
		} else {
			fonts[1].draw(batch, "TORA", (toraStart.x + toraEnd.x) / 2f - 20, toraHeight - 10);
			fonts[1].draw(batch, "TODA", (todaStart.x + todaEnd.x) / 2f - 20, todaHeight - 10);
			fonts[1].draw(batch, "ASDA", (takeOffAsdaStart.x + takeOffAsdaEnd.x) / 2f - 20, asdaHeight - 10);
			fonts[1].draw(batch, takeOffAvoidDistanceString, (takeOffAvoidDistanceStart.x + takeOffAvoidDistanceEnd.x) / 2f - 15, (float) avoidHeight);
		}
	}

	private void drawControls(SpriteBatch controlsBatch) {
		if (!screenshotInProgress) {
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			camFixed = new OrthographicCamera(w, h);
			camFixed.position.set(camFixed.viewportWidth / 2f, camFixed.viewportHeight / 2f, 0);
			camFixed.update();
			controlsBatch.setProjectionMatrix(camFixed.combined);
			if (controls != null) {
				for (int i = 0; i < controls.size(); i++) {
					this.controls.get(i).setScale(controlWidth.get(i) * 0.002f);
					this.controls.get(i).setPosition(controlStartX.get(i), controlStartY.get(i));
					this.controls.get(i).draw(controlsBatch);
				}
			}
		}
	}

	public void toggleIsLanding() {
		this.isLanding = !this.isLanding;
	}

	public void toggleApproachFromLeft() {
		this.isApproachingFromLeft = !this.isApproachingFromLeft;
	}

	private Vector2 transformToScreenCoords(Vector2 pos) {
		float posX;
		float posY;
		double leftStopwayLength = runway.getOriginalLogicalRunwayFromRight().stopwayLength;
		double rightStopwayLength = runway.getOriginalLogicalRunwayFromLeft().stopwayLength;
		double runwayLength = runway.getOriginalLogicalRunwayFromLeft().lda;
		double runwayAreaWidth = 500.0;

		posX = (float) (Gdx.graphics.getWidth() * pos.x / (leftStopwayLength + runwayLength + rightStopwayLength));

		if (currentDisplay == VisualisationType.TopDown) {
			posY = (float) ((Gdx.graphics.getHeight() * (3.27f / 4.0f) + (Gdx.graphics.getHeight() * pos.y / runwayAreaWidth)));
		} else {
			posY = (float) ((Gdx.graphics.getHeight() / 4.0f) - (Gdx.graphics.getHeight() / (20.0f))) + 2.7f;
		}
		return new Vector2(posX, posY);
	}

	private Vector2 transformToScreenCoords(double x, double y) {
		return transformToScreenCoords(new Vector2((float) x, (float) y));
	}

	private Vector2 getRelativeObstructionPosition(Obstruction o) {
		return new Vector2(transformToScreenCoords(o.getPositionAlongRunway(), o.getDistanceFromRunway()).x, VisPanel.SIDE_VIEW_GROUND_HEIGHT + offsetY);
	}

	public void update(float deltaTime) {
		float rateOfChangePosX = 25.0f;

		// isApproachingFromLeft = true;
		currentPlane.setFlip(!isApproachingFromLeft, false);
		currentLogicalRunway = isApproachingFromLeft ? runway.getLeftLandLogicalRunway() : runway.getRightLandLogicalRunway();

		currentPlane.setPosition(currentPlane.getX() + (isApproachingFromLeft ? rateOfChangePosX : -rateOfChangePosX) * deltaTime, currentPlane.getY());
		if (currentPlane.getX() > width - 200)
			currentPlane.setPosition(0, currentPlane.getY());
		else if (!isApproachingFromLeft && currentPlane.getX() <= rateOfChangePosX)
			currentPlane.setPosition(width - 300, currentPlane.getY());

		Obstruction o = runway.getFirstObstruction();

		if (o != null) {
			if (isObstacleValid()) {
				this.currentObstacle.setSize(70, 25);
				float yOffset = (currentDisplay == VisualisationType.TopDown) ? 0.07f : -0.8f;
				Vector2 obstructionDrawPosition = transformToScreenCoords(new Vector2((float) (o.getPositionAlongRunway() - 0.5 * currentObstacle.getWidth()), (float) (o.getDistanceFromRunway() + yOffset)));
				this.currentObstacle.setPosition(obstructionDrawPosition.x, obstructionDrawPosition.y);
			} else {
				this.currentObstacle.setPosition(0, 0);
			}
		}
	}
}
