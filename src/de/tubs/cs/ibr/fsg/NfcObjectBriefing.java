package de.tubs.cs.ibr.fsg;

import android.os.Parcel;
import android.os.Parcelable;


public class NfcObjectBriefing implements Parcelable{
	private short briefingID;
	private long timestamp;
	
	
	
	public NfcObjectBriefing() {
		super();
		briefingID =0;
		timestamp=0;
	}
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
	
	
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(String.valueOf(briefingID) );
		parcel.writeLong(timestamp );
	}
	
	
    /**
     * 
     */
    public static final Parcelable.Creator<NfcObjectBriefing> CREATOR = new Parcelable.Creator<NfcObjectBriefing>() {
        public NfcObjectBriefing createFromParcel(Parcel mParcel) {
            return new NfcObjectBriefing(mParcel);
        }

        public NfcObjectBriefing[] newArray(int size) {
            return new NfcObjectBriefing[size];
        }
    };


    /**
     * @param mParcel
     */
    private NfcObjectBriefing(Parcel mParcel) {
    	this.briefingID = Short.valueOf(mParcel.readString());
    	this.timestamp = mParcel.readLong();

    }

}
