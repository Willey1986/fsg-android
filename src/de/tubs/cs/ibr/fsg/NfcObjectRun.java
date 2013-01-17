package de.tubs.cs.ibr.fsg;


public class NfcObjectRun {
	public static final short ACCELERATION = 1;
	public static final short SKID_PAD     = 2;
	public static final short AUTOCROSS    = 3;
	public static final short ENDURANCE    = 4;
	
	private short raceID;
	private long timestamp;
	

	public String getTime(){
		java.util.Date date = new java.util.Date(timestamp);
		return ""+date;
	}


	public short getRaceID() {
		return raceID;
	}


	public void setRaceID(short raceID) {
		this.raceID = raceID;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	

}
