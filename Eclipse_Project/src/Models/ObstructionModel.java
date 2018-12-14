package Models;

public class ObstructionModel {

	private int obstructionID;
	private String name;

	private double width;
	private double height;
	private double length;

	// These give the position of the centre of the obstacle
	private double positionAlongRunway;
	private double distanceFromRunway;

	// Set Methods
	public void setID(int newID) {
		this.obstructionID = newID;
	}

	public void setName(String nme) {
		this.name = nme;
	}

	public void setWidth(double receivedWidth) {
		this.width = receivedWidth;
	}

	public void setHeight(double receivedHeight) {
		this.height = receivedHeight;
	}

	public void setLength(double receivedLength) {
		this.length = receivedLength;
	}

	public void setPositionAlongRunway(double receivedPositionAlongRunway) {
		this.positionAlongRunway = receivedPositionAlongRunway;
	}

	public void setPositionY(double receivedDistanceFromRunway) {
		this.distanceFromRunway = receivedDistanceFromRunway;
	}

	// Get Methods
	public int getID() {
		return this.obstructionID;
	}

	public String getName() {
		return this.name;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public double getLength() {
		return this.length;
	}

	public double getPositionAlongRunway() {
		return this.positionAlongRunway;
	}

	public double getDistanceFromRunway() {
		return this.distanceFromRunway;
	}

}