package de.tubs.cs.ibr.fsg;

import java.util.Date;

public class NfcObjectBriefing {
	private short briefingID;
	private long timestamp;
	
	public short getBriefingID(){
		return briefingID;
	}
	public long getTimestamp(){
		return timestamp;
	}
	public String getTime(){
		java.util.Date date = new java.util.Date(timestamp);
		return ""+date;
	}
	public void setBriefingID(short temp){
		briefingID = temp;
	}
	public void setTimestamp(long temp){
		timestamp = temp;
	}
}
