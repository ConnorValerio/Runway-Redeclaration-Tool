package Models;

import java.util.ArrayList;

import Services.CalculationService;
import Controllers.Obstruction;

public class RunwayModel {

	// Runway Variables: ///// (These are fixed for any given runway)
	// ///////////////////////////////////////
	private LogicalRunway leftLand;
	private LogicalRunway rightLand;
	private LogicalRunway leftTakeOff;
	private LogicalRunway rightTakeOff;
	private LogicalRunway originalFromLeft;
	private LogicalRunway originalFromRight;

	private double planeBlastDistance = 0.0;

	private ArrayList<Obstruction> obstructions;

	/** Runway ID - may not be needed, used so XML file can be read in **/
	private int runwayID;
	private String runwayName;

	// Constructor
	public RunwayModel(String runwayName, int runwayID, double todaFromLeft, double todaFromRight, double tora, double asdaFromLeft, double asdaFromRight, double ldaFromLeft, double ldaFromRight, double takeOffPlaneBlastDistance) {
		obstructions = new ArrayList<Obstruction>();
		originalFromLeft = new LogicalRunway();
		originalFromRight = new LogicalRunway();
		originalFromLeft.tora = tora;
		originalFromRight.tora = tora;

		originalFromLeft.toda = todaFromLeft;
		originalFromLeft.asda = asdaFromLeft;
		originalFromLeft.lda = ldaFromLeft;
		originalFromLeft.stopwayLength = asdaFromLeft - ldaFromLeft;
		originalFromLeft.clearwayLength = todaFromLeft - ldaFromLeft;
		originalFromRight.toda = todaFromRight;
		originalFromRight.asda = asdaFromRight;
		originalFromRight.lda = ldaFromRight;
		originalFromRight.stopwayLength = asdaFromRight - ldaFromRight;
		originalFromRight.clearwayLength = todaFromRight - ldaFromRight;

		originalFromLeft.displacedThreshold = tora - ldaFromLeft;
		originalFromRight.displacedThreshold = ldaFromRight;

		resetAllParameters();

		this.planeBlastDistance = takeOffPlaneBlastDistance;
		this.runwayID = runwayID;
		this.runwayName = runwayName;
	}

	public void setRunwayName(String name) {
		this.runwayName = name;
	}

	public void setRunwayID(int runwayID) {
		/* may not be needed, used so XML file can be read in */
		this.runwayID = runwayID;

		int normalisedID = runwayID % 36;
		if (normalisedID >= 18)
			normalisedID = 36 - normalisedID;
	}

	// Getter Methods
	public String getRunwayName() {
		return this.runwayName;
	}

	public int getRunwayID() {
		return this.runwayID;
	}

	public void setPlaneBlastDistance(double blastDistance) {
		this.planeBlastDistance = blastDistance;
	}

	public LogicalRunway getOriginalLogicalRunwayFromLeft() {
		return originalFromLeft;
	}

	public LogicalRunway getOriginalLogicalRunwayFromRight() {
		return originalFromRight;
	}

	public LogicalRunway getLeftLandLogicalRunway() {
		return leftLand;
	}

	public LogicalRunway getRightLandLogicalRunway() {
		return rightLand;
	}

	public LogicalRunway getLeftTakeOffLogicalRunway() {
		return leftTakeOff;
	}

	public LogicalRunway getRightTakeOffLogicalRunway() {
		return rightTakeOff;
	}

	public void resetAllParameters() {
		this.leftLand = originalFromLeft.clone();
		this.rightLand = originalFromRight.clone();
		this.leftTakeOff = originalFromLeft.clone();
		this.rightTakeOff = originalFromRight.clone();
	}

	public void redeclareAll() {
		resetAllParameters();
		CalculationService calc = new CalculationService();
		calc.declareParametersLandFromLeft(this);
		calc.declareParametersLandFromRight(this);
		calc.declareParametersTakeOffFromLeft(this);
		calc.declareParametersTakeOffFromRight(this);
	}

	public void addObstruction(Obstruction obstruction) {
		// Removes current obstruction as the program currently supports just
		// one obstruction
		this.clearObstructions();
		this.obstructions.add(obstruction);
		this.redeclareAll();
	}

	public void clearObstructions() {
		this.obstructions = new ArrayList<Obstruction>();
	}

	public void removeObstruction(int obstructionID) {
		for (int i = 0; i < this.obstructions.size(); ++i) {
			if (this.obstructions.get(i).getID() == obstructionID) {
				this.obstructions.remove(i);
				break;
			}
		}
		this.redeclareAll();
	}

	// Accessors:
	// ///////////////////////////////////////////////////////////////////////////////////

	public Obstruction getFirstObstruction() {
		if (this.obstructions.size() > 0) {
			return this.obstructions.get(0);
		} else {
			return null;
		}
	}

	public double getBlastDistance() {
		return this.planeBlastDistance;
	}

}
