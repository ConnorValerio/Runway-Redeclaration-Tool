package Services;

import Controllers.*;
import Models.*;

public class CalculationService {

	public static boolean obstacleIsOnLeft(Obstruction ob, Runway runway) {
		if (ob != null) {
			double stopwayLength = runway.getOriginalLogicalRunwayFromLeft().stopwayLength;
			double runwayLength = runway.getOriginalLogicalRunwayFromLeft().lda;
			return (ob.getPositionAlongRunway() - stopwayLength) < (runwayLength / 2.0);
		} else {
			return true;
		}
	}

	public boolean obstacleIsOnLeft(Obstruction ob, RunwayModel runway) {
		if (ob != null) {
			double stopwayLength = runway.getOriginalLogicalRunwayFromLeft().stopwayLength;
			double runwayLength = runway.getOriginalLogicalRunwayFromLeft().lda;
			return (ob.getPositionAlongRunway() - stopwayLength) < (runwayLength / 2.0);
		} else {
			return true;
		}
	}

	public void declareParametersLandFromLeft(RunwayModel runway) {
		StringBuilder breakdown = new StringBuilder();
		Obstruction obstruction = runway.getFirstObstruction();
		LogicalRunway logicalRunway = runway.getLeftLandLogicalRunway();
		if (obstacleIsOnLeft(obstruction, runway)) {

			// SEE DIAGRAM 1
			double runwayLength = logicalRunway.lda;
			double stopwayLength = logicalRunway.stopwayLength;
			// double clearwayLength = logicalRunway.clearwayLength;
			breakdown.append(LocalizationService.localizeString("resa_greater") + obstruction.getHeight() + LocalizationService.localizeString("resa_greater_2") + logicalRunway.resa + "\n\n");
			breakdown.append(LocalizationService.localizeString("obstr_is") + (obstruction.getPositionAlongRunway() - stopwayLength) + LocalizationService.localizeString("along_runway"));
			double newResa = Math.max(240.0, 50 * obstruction.getHeight());
			breakdown.append(LocalizationService.localizeString("resa_distance") + newResa + " + 60.0\n\n");
			double avoidDistance = newResa + 60.0;
			double threshold = (obstruction.getPositionAlongRunway() - stopwayLength) + avoidDistance + 0.5 * obstruction.getLength();
			breakdown.append(LocalizationService.localizeString("lda_start") + (obstruction.getPositionAlongRunway() - stopwayLength) + " + 0.5 x " + obstruction.getLength() + " + " + (logicalRunway.resa + 60.0) + "= " + threshold + "\n");
			double newDisplacedLandingThreshold = Math.max(0.0, threshold);
			if (threshold < 0.0) {
				breakdown.append(LocalizationService.localizeString("lda_below_0"));
				threshold = 0.0;
			}
			double newLda = Math.max(0.0, Math.min(logicalRunway.lda, runwayLength - threshold));
			double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, runwayLength + stopwayLength - threshold));
			breakdown.append(LocalizationService.localizeString("lda_is") + runwayLength + " - " + threshold + " = " + newLda + "\n\n");
			breakdown.append(LocalizationService.localizeString("asda_is") + logicalRunway.lda + " + " + stopwayLength + " = " + newAsda + "\n");

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				logicalRunway.resa = newResa;
				logicalRunway.lda = newLda;
				logicalRunway.asda = newAsda;
				logicalRunway.displacedThreshold = newDisplacedLandingThreshold;
				logicalRunway.tora = 0.0;
				logicalRunway.toda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		} else {

			// SEE DIAGRAM 2
			double stopwayLength = logicalRunway.stopwayLength;
			double clearwayLength = logicalRunway.clearwayLength;
			double runwayLength = logicalRunway.lda;
			double newResa = 240.0;
			breakdown.append(LocalizationService.localizeString("resa_avoid") + " 240 + 60 = 300m\n\n");
			double avoidDistance = newResa + 60.0;
			breakdown.append(LocalizationService.localizeString("threshold_beyond") + (obstruction.getPositionAlongRunway() - stopwayLength) + " - (" + avoidDistance + " + 0.5 x " + obstruction.getLength() + ") = " + (obstruction.getPositionAlongRunway() - (avoidDistance + 0.5 * obstruction.getLength() + stopwayLength)) + "\n\n");
			double threshold = obstruction.getPositionAlongRunway() - (avoidDistance + 0.5 * obstruction.getLength() + stopwayLength);
			double newDisplacedLandingThreshold = Math.min(runwayLength + Math.max(stopwayLength, clearwayLength), threshold);
			double newLda = Math.max(0.0, Math.min(runwayLength, threshold));
			double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, threshold));
			breakdown.append(LocalizationService.localizeString("lda_up_to") + newLda + "\n\n");
			breakdown.append(LocalizationService.localizeString("asda_up_to") + newAsda + "\n\n");

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				logicalRunway.resa = newResa;
				logicalRunway.lda = newLda;
				logicalRunway.asda = newAsda;
				logicalRunway.displacedThreshold = newDisplacedLandingThreshold;
				logicalRunway.tora = 0.0;
				logicalRunway.toda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		}
	}

	public void declareParametersLandFromRight(RunwayModel runway) {
		StringBuilder breakdown = new StringBuilder();
		Obstruction obstruction = runway.getFirstObstruction();
		LogicalRunway logicalRunway = runway.getRightLandLogicalRunway();
		if (obstacleIsOnLeft(obstruction, runway)) {

			// SEE DIAGRAM 3
			double runwayLength = logicalRunway.lda;
			double stopwayLength = logicalRunway.stopwayLength;
			double clearwayLength = logicalRunway.clearwayLength;
			double newResa = 240.0;
			breakdown.append(LocalizationService.localizeString("resa_avoid") + " 240 + 60 = 300m\n\n");
			double avoidDistance = newResa + 60.0;
			double threshold = (avoidDistance + 0.5 * obstruction.getLength()) + obstruction.getPositionAlongRunway() - stopwayLength;
			double newDisplacedLandingThreshold = Math.max(-Math.max(stopwayLength, clearwayLength), threshold);
			breakdown.append(LocalizationService.localizeString("threshold_beyond") + threshold + "\n\n");
			double newLda = Math.max(0.0, Math.min(runwayLength, runwayLength - threshold));
			double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, runwayLength - threshold));
			breakdown.append(LocalizationService.localizeString("lda_up_to") + newLda + "\n\n");
			breakdown.append(LocalizationService.localizeString("asda_up_to") + newAsda + "\n\n");

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				logicalRunway.resa = newResa;
				logicalRunway.lda = newLda;
				logicalRunway.asda = newAsda;
				logicalRunway.displacedThreshold = newDisplacedLandingThreshold;
				logicalRunway.tora = 0.0;
				logicalRunway.toda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		} else {

			// SEE DIAGRAM 4
			double stopwayLength = logicalRunway.stopwayLength;
			// double clearwayLength = logicalRunway.clearwayLength;
			double runwayLength = logicalRunway.lda;
			double newResa = Math.max(240.0, 50 * obstruction.getHeight());
			breakdown.append(LocalizationService.localizeString("resa_greater") + obstruction.getHeight() + LocalizationService.localizeString("resa_greater_2") + logicalRunway.resa + "\n\n");
			double avoidDistance = newResa + 60.0;
			breakdown.append(LocalizationService.localizeString("resa_distance") + logicalRunway.resa + " + 60.0 = " + (newResa + 60) + "\n\n");
			breakdown.append(LocalizationService.localizeString("threshold_beyond") + (obstruction.getPositionAlongRunway() - stopwayLength) + " - (" + avoidDistance + " + 0.5 x " + obstruction.getLength() + ") = " + (obstruction.getPositionAlongRunway() - (avoidDistance + 0.5 * obstruction.getLength() + stopwayLength)) + "\n\n");
			double threshold = obstruction.getPositionAlongRunway() - (avoidDistance + 0.5 * obstruction.getLength() + stopwayLength);
			double newDisplacedLandingThreshold = Math.min(runwayLength, threshold);
			double newLda = Math.max(0.0, Math.min(logicalRunway.lda, threshold));
			double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, threshold + stopwayLength));
			breakdown.append(LocalizationService.localizeString("lda_up_to") + newLda + "\n\n");
			breakdown.append(LocalizationService.localizeString("asda_up_to") + newAsda + "\n\n");

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				logicalRunway.resa = newResa;
				logicalRunway.lda = newLda;
				logicalRunway.asda = newAsda;
				logicalRunway.displacedThreshold = newDisplacedLandingThreshold;
				logicalRunway.tora = 0.0;
				logicalRunway.toda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		}
	}

	public void declareParametersTakeOffFromLeft(RunwayModel runway) {
		StringBuilder breakdown = new StringBuilder();
		Obstruction obstruction = runway.getFirstObstruction();
		LogicalRunway logicalRunway = runway.getLeftTakeOffLogicalRunway();
		if (obstacleIsOnLeft(obstruction, runway)) {

			// SEE DIAGRAM 5
			double runwayLength = logicalRunway.lda;
			double stopwayLength = logicalRunway.stopwayLength;
			double clearwayLength = logicalRunway.clearwayLength;
			double avoidDistance = runway.getBlastDistance();
			breakdown.append(LocalizationService.localizeString("taking_off_dist") + avoidDistance + ".\n\n");
			double threshold = (obstruction.getPositionAlongRunway() - stopwayLength) + avoidDistance + 0.5 * obstruction.getLength();
			breakdown.append(LocalizationService.localizeString("threshold_beyond") + (obstruction.getPositionAlongRunway() - stopwayLength) + " + " + avoidDistance + " + 0.5 x " + obstruction.getLength() + " = " + (obstruction.getPositionAlongRunway() + avoidDistance + 0.5 * obstruction.getLength() - stopwayLength) + "\n\n");
			double newDisplacedTakeOffThreshold = Math.max(0.0, threshold);

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				double newTora = Math.max(0.0, Math.min(logicalRunway.lda, runwayLength - threshold));
				double newToda = Math.max(0.0, Math.min(logicalRunway.toda, clearwayLength + runwayLength - threshold));
				double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, stopwayLength + runwayLength - threshold));
				breakdown.append(LocalizationService.localizeString("tora_past") + logicalRunway.tora + "\n\n");
				breakdown.append(LocalizationService.localizeString("toda_past") + newToda + "\n\n");
				breakdown.append(LocalizationService.localizeString("asda_past") + newAsda + "\n\n");

				logicalRunway.displacedThreshold = newDisplacedTakeOffThreshold;
				logicalRunway.tora = newTora;
				logicalRunway.toda = newToda;
				logicalRunway.asda = newAsda;
				logicalRunway.resa = 0.0;
				logicalRunway.lda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		} else {

			// SEE DIAGRAM 6
			double clearwayLength = logicalRunway.clearwayLength;
			double stopwayLength = logicalRunway.stopwayLength;
			double runwayLength = logicalRunway.lda;
			double newResa = Math.max(240.0, 50 * obstruction.getHeight());
			double avoidDistance = newResa + 60.0;
			breakdown.append(LocalizationService.localizeString("resa_greater") + obstruction.getHeight() + LocalizationService.localizeString("resa_greater_2") + newResa + "\n\n");
			double threshold = obstruction.getPositionAlongRunway() - (avoidDistance + 0.5 * obstruction.getLength() + stopwayLength);
			breakdown.append(LocalizationService.localizeString("threshold_up_to") + (obstruction.getPositionAlongRunway() - stopwayLength) + " - (" + avoidDistance + " + 1/2 x " + obstruction.getLength() + ") = " + threshold + "\n\n");
			double newDisplacedTakeOffThreshold = Math.min(runwayLength + Math.max(stopwayLength, clearwayLength), threshold);
			double newTora = Math.max(0.0, Math.min(logicalRunway.tora, threshold));
			double newToda = Math.max(0.0, Math.min(logicalRunway.toda, threshold));
			double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, threshold));
			breakdown.append(LocalizationService.localizeString("tora_up_to") + newToda + "\n\n");
			breakdown.append(LocalizationService.localizeString("toda_up_to") + newToda + "\n\n");
			breakdown.append(LocalizationService.localizeString("asda_up_to") + newAsda + "\n\n");

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				logicalRunway.resa = newResa;
				logicalRunway.displacedThreshold = newDisplacedTakeOffThreshold;
				logicalRunway.tora = newTora;
				logicalRunway.toda = newToda;
				logicalRunway.asda = newAsda;
				logicalRunway.lda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		}

	}

	public void declareParametersTakeOffFromRight(RunwayModel runway) {
		StringBuilder breakdown = new StringBuilder();
		Obstruction obstruction = runway.getFirstObstruction();
		LogicalRunway logicalRunway = runway.getRightTakeOffLogicalRunway();
		if (obstacleIsOnLeft(obstruction, runway)) {

			// SEE DIAGRAM 7
			double runwayLength = logicalRunway.lda;
			double clearwayLength = logicalRunway.clearwayLength;
			double stopwayLength = logicalRunway.stopwayLength;
			double newResa = Math.max(240.0, 50 * obstruction.getHeight());
			breakdown.append(LocalizationService.localizeString("resa_greater") + obstruction.getHeight() + LocalizationService.localizeString("resa_greater_2") + newResa + "\n\n");
			double avoidDistance = newResa + 60.0;
			breakdown.append(LocalizationService.localizeString("resa_distance") + logicalRunway.resa + " + 60.0 = " + (newResa + 60) + "\n\n");
			double threshold = obstruction.getPositionAlongRunway() + avoidDistance + 0.5 * obstruction.getLength() - stopwayLength;
			breakdown.append(LocalizationService.localizeString("threshold_beyond") + threshold + "\n\n");
			double newDisplacedTakeOffThreshold = Math.max(-Math.max(stopwayLength, clearwayLength), runwayLength - threshold);
			double newTora = Math.max(0.0, Math.min(logicalRunway.tora, runwayLength - threshold));
			double newToda = Math.max(0.0, Math.min(logicalRunway.toda, runwayLength - threshold));
			double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, runwayLength - threshold));
			breakdown.append(LocalizationService.localizeString("tora_up_to") + newToda + "\n\n");
			breakdown.append(LocalizationService.localizeString("toda_up_to") + newToda + "\n\n");
			breakdown.append(LocalizationService.localizeString("asda_up_to") + newAsda + "\n\n");

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				logicalRunway.resa = newResa;
				logicalRunway.displacedThreshold = newDisplacedTakeOffThreshold;
				logicalRunway.tora = newTora;
				logicalRunway.toda = newToda;
				logicalRunway.asda = newAsda;
				logicalRunway.lda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		} else {

			// SEE DIAGRAM 8
			double runwayLength = logicalRunway.lda;
			double stopwayLength = logicalRunway.stopwayLength;
			double clearwayLength = logicalRunway.clearwayLength;
			double avoidDistance = runway.getBlastDistance();
			breakdown.append(LocalizationService.localizeString("taking_off_dist") + avoidDistance + ".\n\n");
			double threshold = obstruction.getPositionAlongRunway() - (avoidDistance + 0.5 * obstruction.getLength() + stopwayLength);
			breakdown.append(LocalizationService.localizeString("threshold_up_to") + (obstruction.getPositionAlongRunway() - stopwayLength) + " - (" + avoidDistance + " + 1/2 x " + obstruction.getLength() + ") = " + threshold + "\n\n");
			double newDisplacedTakeOffThreshold = Math.min(runwayLength, threshold);
			double newTora = Math.max(0.0, Math.min(logicalRunway.tora, threshold));
			double newToda = Math.max(0.0, Math.min(logicalRunway.toda, threshold + clearwayLength));
			double newAsda = Math.max(0.0, Math.min(logicalRunway.asda, threshold + stopwayLength));
			breakdown.append(LocalizationService.localizeString("tora_past") + logicalRunway.tora + "\n\n");
			breakdown.append(LocalizationService.localizeString("toda_past") + newToda + "\n\n");
			breakdown.append(LocalizationService.localizeString("asda_past") + newAsda + "\n\n");

			if (avoidDistance >= obstruction.getDistanceFromRunway()) {
				logicalRunway.displacedThreshold = newDisplacedTakeOffThreshold;
				logicalRunway.tora = newTora;
				logicalRunway.toda = newToda;
				logicalRunway.asda = newAsda;
				logicalRunway.resa = 0.0;
				logicalRunway.lda = 0.0;
			} else {
				breakdown = new StringBuilder();
				breakdown.append("The obstacle is so far from the runway that the entire runway is safe to use.");
			}

			logicalRunway.setBreakdown(breakdown.toString());

		}

	}

}
