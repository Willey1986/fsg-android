package de.tubs.cs.ibr.fsg;

import de.tubs.cs.ibr.fsg.db.models.Driver;
//import org.json.JSONException;
//import org.json.JSONObject;

public class NfcObject {
	public Driver DriverObject;
	private short eventID;
	
	
	public NfcObject(){
		DriverObject = new Driver();
	}
	
	public short getEventID(){
		return eventID;
	}
	public void setEventID(short temp){
		eventID = temp;
	}
}