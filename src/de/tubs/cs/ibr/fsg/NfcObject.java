package de.tubs.cs.ibr.fsg;

import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;
import java.util.*;

public class NfcObject {
	public Driver DriverObject;
	private ArrayList<NfcObjectBriefing> Briefings = new ArrayList<NfcObjectBriefing>();
	private short eventID;	
	
	public NfcObject(){
		DriverObject = new Driver();
	}
	
	public void addBriefing(NfcObjectBriefing temp){
		Briefings.add(temp);
	}
	public void removeBriefingByID(short id){
		Iterator<NfcObjectBriefing> it = Briefings.iterator();
		while (it.hasNext()) {
		  if(it.next().getBriefingID() == id){
			  Briefings.remove(it.next());
		  }
		}

	}
	public ArrayList<NfcObjectBriefing> getBriefings(){
		return Briefings;
	}
	
	public short getEventID(){
		return eventID;
	}
	public void setEventID(short temp){
		eventID = temp;
	}
}