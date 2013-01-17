package de.tubs.cs.ibr.fsg;

import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;

import java.text.SimpleDateFormat;
import java.util.*;

public class NfcObject {
	private Driver driverObject;
	private ArrayList<NfcObjectBriefing> briefings = new ArrayList<NfcObjectBriefing>();
	private ArrayList<NfcObjectRun> runs = new ArrayList<NfcObjectRun>();
	
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
	
	public ArrayList<NfcObjectRun> getRuns(){
		return runs;
	}
	
	public void addRun(NfcObjectRun temp){
		runs.add(temp);
	}
	
	public void removeRunByID(short id){
		Iterator<NfcObjectRun> it = runs.iterator();
		while (it.hasNext()) {
		  if(it.next().getRaceID() == id){
			  runs.remove(it.next());
			  break;      // TODO Geaendert, ich glaube den break brauchen wir, sonst werden alle Runs der gleichen Klasse geloescht...
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
	
	public boolean haveTheDriverTodaysBriefing() {
		boolean result = false;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.GERMANY);
		Date today = new Date();
		String todayString = dateFormat.format(today);
		
		for(int i=0; i<briefings.size(); i++){
			Date briefingDay = new Date( briefings.get(i).getTimestamp() );
			String briefingDayString = dateFormat.format(briefingDay);
			
			if(todayString.equalsIgnoreCase(briefingDayString)){
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	
	
	
}