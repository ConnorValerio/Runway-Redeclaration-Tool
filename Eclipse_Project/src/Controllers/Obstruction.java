package Controllers;

import Models.ObstructionModel;

public class Obstruction {
	private ObstructionModel myModel;

	public Obstruction(String name, Double width, Double height, Double length, Double positionAlongRunway, Double positionY) {
		myModel = new ObstructionModel();

		myModel.setName(name);
		myModel.setWidth(width);
		myModel.setHeight(height);
		myModel.setLength(length);
		myModel.setPositionAlongRunway(positionAlongRunway);
		myModel.setPositionY(positionY);
	}

	public void setID(int newID) {
		myModel.setID(newID);
	}

	public int getID() {
		return myModel.getID();
	}

	public String getName() {
		return myModel.getName();
	}

	public double getWidth() {
		return myModel.getWidth();
	}

	public double getHeight() {
		return myModel.getHeight();
	}

	public double getLength() {
		return myModel.getLength();
	}

	public double getPositionAlongRunway() {
		return myModel.getPositionAlongRunway();
	}

	public double getDistanceFromRunway() {
		return myModel.getDistanceFromRunway();
	}

}
