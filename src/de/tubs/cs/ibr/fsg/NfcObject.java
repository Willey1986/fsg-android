package de.tubs.cs.ibr.fsg;

import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;
import java.util.*;

public class NfcObject {
	private Driver driverObject;
	private ArrayList<NfcObjectBriefing> briefings = new ArrayList<NfcObjectBriefing>();
	private ArrayList<NfcObjectBriefing> runs = new ArrayList<NfcObjectBriefing>();
	
	private short eventID;	
	
	public NfcObject(){
		driverObject = new Driver();
	}
	
	public void addBriefing(NfcObjectBriefing temp){
		briefings.add(temp);
	}
	
	public void removeBriefingByID(short id){
		Iterator<NfcObjectBriefing> it = briefings.iterator();
		while (it.hasNext()) {
		  if(it.next().getBriefingID() == id){
			  briefings.remove(it.next());
		  }
		}

	}
	
	public ArrayList<NfcObjectBriefing> getRuns(){
		return runs;
	}
	
	public void addRun(NfcObjectBriefing temp){
		runs.add(temp);
	}
	
	public void removeRunByID(short id){
		Iterator<NfcObjectBriefing> it = runs.iterator();
		while (it.hasNext()) {
		  if(it.next().getBriefingID() == id){
			  runs.remove(it.next());
		  }
		}

	}
	
	public ArrayList<NfcObjectBriefing> getBriefings(){
		return briefings;
	}
	
	public short getEventID(){
		return eventID;
	}
	
	public void setEventID(short temp){
		eventID = temp;
	}

	public void clear() {
		driverObject = new Driver();
		briefings.removeAll(briefings);
		runs.removeAll(runs);	
		eventID = 0;
	}

	public Driver getDriverObject() {
		return driverObject;
	}

	public void setDriverObject(Driver driverObject) {
		this.driverObject = driverObject;
	}
	
	
	
	
	
}