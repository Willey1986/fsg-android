package de.tubs.cs.ibr.fsg;

import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.dtn.UpdateRequest;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;

import java.text.SimpleDateFormat;
import java.util.*;

import android.os.Parcel;
import android.os.Parcelable;

public class NfcObject implements Parcelable{
	private short eventID;	
	private Driver driverObject;
	private short accelerationRuns;
	private short skidPadRuns;
	private short autocrossRuns;
	private short enduranceRuns;
	private ArrayList<NfcObjectBriefing> briefings;
	private boolean tagInvalidated;


	public NfcObject(){
		driverObject = new Driver();
		briefings = new ArrayList<NfcObjectBriefing>();
		accelerationRuns = 0;
		skidPadRuns = 0;
		autocrossRuns = 0;
		enduranceRuns = 0;
		eventID = 0;
		tagInvalidated = false;
	}
	
	public void addBriefing(NfcObjectBriefing temp){
		briefings.add(temp);
	}
	
	public void removeBriefingByID(short id){
		Iterator<NfcObjectBriefing> it = briefings.iterator();
		while (it.hasNext()) {
			NfcObjectBriefing briefing = it.next();
		  if(briefing.getBriefingID() == id){
			  briefings.remove(briefing);
		  }
		}
	}
	
	
	public boolean  existThisBriefingByID(short id){
		boolean result = false;
		for (int i=0; i<briefings.size(); i++){
			if (briefings.get(i).getBriefingID()== id){
				result = true;
				break;
			}
		}
		return result;
	}
	
	
	public void clear() {
		driverObject = new Driver();
		briefings.removeAll(briefings);
		accelerationRuns = 0;
		skidPadRuns = 0;
		autocrossRuns = 0;
		enduranceRuns = 0;
		eventID = 0;
		tagInvalidated = true;
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
	


	public short getAccelerationRuns() {
		return accelerationRuns;
	}

	public void setAccelerationRuns(short accelerationRuns) {
		this.accelerationRuns = accelerationRuns;
	}

	public short getSkidPadRuns() {
		return skidPadRuns;
	}

	public void setSkidPadRuns(short skidPadRuns) {
		this.skidPadRuns = skidPadRuns;
	}

	public short getAutocrossRuns() {
		return autocrossRuns;
	}

	public void setAutocrossRuns(short autocrossRuns) {
		this.autocrossRuns = autocrossRuns;
	}

	public short getEnduranceRuns() {
		return enduranceRuns;
	}

	public void setEnduranceRuns(short enduranceRuns) {
		this.enduranceRuns = enduranceRuns;
	}

	public void setBriefings(ArrayList<NfcObjectBriefing> briefings) {
		this.briefings = briefings;
	}

	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeTypedList(briefings );
		parcel.writeSerializable(driverObject );
		parcel.writeString(String.valueOf(eventID) );
		parcel.writeString(String.valueOf(accelerationRuns));
		parcel.writeString(String.valueOf(skidPadRuns));
		parcel.writeString(String.valueOf(autocrossRuns));
		parcel.writeString(String.valueOf(enduranceRuns));

	}
	
	
    /**
     * 
     */
    public static final Parcelable.Creator<NfcObject> CREATOR = new Parcelable.Creator<NfcObject>() {
        public NfcObject createFromParcel(Parcel mParcel) {
            return new NfcObject(mParcel);
        }

        public NfcObject[] newArray(int size) {
            return new NfcObject[size];
        }
    };


    /**
     * @param mParcel
     */
    private NfcObject(Parcel mParcel) {
    	this();
    	mParcel.readTypedList(briefings, NfcObjectBriefing.CREATOR);
    	driverObject = (Driver)mParcel.readSerializable();
    	eventID = Short.valueOf(mParcel.readString());
    	accelerationRuns = Short.valueOf(mParcel.readString());
    	skidPadRuns = Short.valueOf(mParcel.readString());
    	autocrossRuns = Short.valueOf(mParcel.readString());
    	enduranceRuns = Short.valueOf(mParcel.readString());
    	
    }
	
    
    /**
     * Mit Hilfe dieser Methode bekommen wir die Daten eines Tags in JSON-Format.
     * 
     * @return Alle Daten in JSON-Format, die auf einem Tag geschrieben sind.
     */
    public String getJSONData(){
    	String result = "TODO";
    	
    	//TODO Das hier muss mit Leben gefuellt werden
    	
    	return result;
    }
	
    public boolean isValid() {
    	return !tagInvalidated;
    }
	
	
}