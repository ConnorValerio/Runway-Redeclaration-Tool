package Controllers;

import Models.*;

public class Runway {
	private RunwayModel runwayModel;

	// Runway Parameters:

	public Runway(String runwayName, int runwayID, double todaFromLeft, double todaFromRight, double tora, double asdaFromLeft, double asdaFromRight, double ldaFromLeft, double ldaFromRight, double takeOffPlaneBlastDistance) {

		// Initialise Variables:
		runwayModel = new RunwayModel(runwayName, runwayID, todaFromLeft, todaFromRight, tora, asdaFromLeft, asdaFromRight, ldaFromLeft, ldaFromRight, takeOffPlaneBlastDistance);
	}

	public void redeclareParameters() {
		runwayModel.redeclareAll();
	}

	public String getRunwayName() {
		return this.runwayModel.getRunwayName();
	}

	public int getRunwayID() {
		return this.runwayModel.getRunwayID();
	}

	public Obstruction getFirstObstruction() {
		return runwayModel.getFirstObstruction();
	}

	public void addObstruction(Obstruction obstruction) {
		runwayModel.addObstruction(obstruction);
	}

	public void removeObstruction(int obstructionID) {
		runwayModel.removeObstruction(obstructionID);
	}

	public LogicalRunway getOriginalLogicalRunwayFromLeft() {
		return runwayModel.getOriginalLogicalRunwayFromLeft();
	}

	public LogicalRunway getOriginalLogicalRunwayFromRight() {
		return runwayModel.getOriginalLogicalRunwayFromRight();
	}

	public LogicalRunway getLeftLandLogicalRunway() {
		return runwayModel.getLeftLandLogicalRunway();
	}

	public LogicalRunway getRightLandLogicalRunway() {
		return runwayModel.getRightLandLogicalRunway();
	}

	public LogicalRunway getLeftTakeOffLogicalRunway() {
		return runwayModel.getLeftTakeOffLogicalRunway();
	}

	public LogicalRunway getRightTakeOffLogicalRunway() {
		return runwayModel.getRightTakeOffLogicalRunway();
	}

	public double getBlastDistance() {
		return runwayModel.getBlastDistance();
	}

	public void setBlastDistance(double blastDistance) {
		// TODO Auto-generated method stub
		runwayModel.setPlaneBlastDistance(blastDistance);
	}
}
