package Models;

import java.io.Serializable;

public class Airport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String airportName;
	String runwayFilename;
	String obstructionFilename;

	// using file name Strings instead of the ArrayList<Runway> and
	// ArrayList<Obstruction> because these Array's can be edited during runtime
	// by the user by creating new runways/obstructions. Therefore by holding
	// Strings for filenames, the updated files will always be read in.
	public Airport(String airportname, String runwayfile, String obstructionfile) {

		this.airportName = airportname;
		this.runwayFilename = runwayfile;
		this.obstructionFilename = obstructionfile;

	}

	public String getAirportName() {
		return this.airportName;
	}

	public String getRunwayFilename() {
		return this.runwayFilename;
	}

	public String getObstructionFilename() {
		return this.obstructionFilename;
	}

}
