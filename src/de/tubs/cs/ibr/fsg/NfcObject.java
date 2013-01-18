package de.tubs.cs.ibr.fsg;

import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;

import java.text.SimpleDateFormat;
import java.util.*;

public class NfcObject {
	private short eventID;	
	private Driver driverObject;
	private int accelerationRuns;
	private int skidPadRuns;
	private int autocrossRuns;
	private int enduranceRuns;
	private ArrayList<NfcObjectBriefing> briefings;


	public NfcObject(){
		driverObject = new Driver();
		briefings = new ArrayList<NfcObjectBriefing>();
		accelerationRuns = 0;
		skidPadRuns = 0;
		autocrossRuns = 0;
		enduranceRuns = 0;
		eventID = 0;
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
	
	
	public void clear() {
		driverObject = new Driver();
		briefings.removeAll(briefings);
		accelerationRuns = 0;
		skidPadRuns = 0;
		autocrossRuns = 0;
		enduranceRuns = 0;
		eventID = 0;
	}
	
	
	/**
	 * Mit Hilfe dieser Methode koennen wir leicht die Regel ueberpruefen, die besagt:
	 * Ein Fahrer darf maximal 3 Disziplinen fahren.
	 */
	public int howManyDisciplinesAreDriven(){
		int amount = 0;
		
		if(accelerationRuns>0){
			amount = amount+1;
		}

		if(skidPadRuns>0){
			amount = amount+1;
		}
		
		if(autocrossRuns>0){
			amount = amount+1;
		}
		
		if(enduranceRuns>0){
			amount = amount+1;
		}
		
		return amount;
	}
	
	
	/**
	 * Mit Hilfe dieser Methode koennen wir direkt die Regel ueberpruefen, die besagt:
	 * Nur wenn der Fahrer am morgentlichen Briefing teilgenommen hat, darf er fahren.
	 */
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
	
	public ArrayList<NfcObjectBriefing> getBriefings(){
		return briefings;
	}
	
	public short getEventID(){
		return eventID;
	}
	
	public void setEventID(short temp){
		eventID = temp;
	}



	public Driver getDriverObject() {
		return driverObject;
	}

	public void setDriverObject(Driver driverObject) {
		this.driverObject = driverObject;
	}
	


	public int getAccelerationRuns() {
		return accelerationRuns;
	}

	public void setAccelerationRuns(int accelerationRuns) {
		this.accelerationRuns = accelerationRuns;
	}

	public int getSkidPadRuns() {
		return skidPadRuns;
	}

	public void setSkidPadRuns(int skidPadRuns) {
		this.skidPadRuns = skidPadRuns;
	}

	public int getAutocrossRuns() {
		return autocrossRuns;
	}

	public void setAutocrossRuns(int autocrossRuns) {
		this.autocrossRuns = autocrossRuns;
	}

	public int getEnduranceRuns() {
		return enduranceRuns;
	}

	public void setEnduranceRuns(int enduranceRuns) {
		this.enduranceRuns = enduranceRuns;
	}

	public void setBriefings(ArrayList<NfcObjectBriefing> briefings) {
		this.briefings = briefings;
	}
	
	
	
	
}