package Models;

public class LogicalRunway {
	public double resa;
	public double lda;
	public double tora;
	public double toda;
	public double asda;
	public double stopwayLength;
	public double clearwayLength;
	public double displacedThreshold;
	private String breakdown = "";

	public LogicalRunway clone() {
		LogicalRunway clone = new LogicalRunway();
		clone.resa = this.resa;
		clone.lda = this.lda;
		clone.tora = this.tora;
		clone.toda = this.toda;
		clone.asda = this.asda;
		clone.stopwayLength = this.stopwayLength;
		clone.clearwayLength = this.clearwayLength;
		clone.displacedThreshold = this.displacedThreshold;
		clone.breakdown = this.breakdown;
		return clone;
	}

	public void setBreakdown(String breakdown) {
		this.breakdown = breakdown;
	}

	public String getBreakdown() {
		return this.breakdown;
	}

}
