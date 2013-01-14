package de.tubs.cs.ibr.fsg.dtn;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdateRequest implements Parcelable{

	private boolean driversRequest;
	
	private boolean teamsRequest;
	
	private boolean blacklistTagsRequest;
	
	private boolean blalistDevicesRequest;
	
	private boolean driverPicsRequest;
	
	
	/**
	 * 
	 */
	public UpdateRequest() {
		this.driversRequest        = false;
		this.teamsRequest          = false;
		this.blacklistTagsRequest  = false;
		this.blalistDevicesRequest = false;
		this.driverPicsRequest     = false;
	}

	
	/**
	 * @param driversRequest
	 * @param teamsRequest
	 * @param blacklistTagsRequest
	 * @param blalistDevicesRequest
	 * @param driverPicsRequest
	 */
	public UpdateRequest(boolean driversRequest, boolean teamsRequest,
			boolean blacklistTagsRequest, boolean blalistDevicesRequest,
			boolean driverPicsRequest) {

		this.driversRequest        = driversRequest;
		this.teamsRequest          = teamsRequest;
		this.blacklistTagsRequest  = blacklistTagsRequest;
		this.blalistDevicesRequest = blalistDevicesRequest;
		this.driverPicsRequest     = driverPicsRequest;
	}

	public boolean isDriversRequest() {
		return driversRequest;
	}

	public void setDriversRequest(boolean driversRequest) {
		this.driversRequest = driversRequest;
	}

	public boolean isTeamsRequest() {
		return teamsRequest;
	}

	public void setTeamsRequest(boolean teamsRequest) {
		this.teamsRequest = teamsRequest;
	}

	public boolean isBlacklistTagsRequest() {
		return blacklistTagsRequest;
	}

	public void setBlacklistTagsRequest(boolean blacklistTagsRequest) {
		this.blacklistTagsRequest = blacklistTagsRequest;
	}

	public boolean isBlalistDevicesRequest() {
		return blalistDevicesRequest;
	}

	public void setBlalistDevicesRequest(boolean blalistDevicesRequest) {
		this.blalistDevicesRequest = blalistDevicesRequest;
	}

	public boolean isDriverPicsRequest() {
		return driverPicsRequest;
	}

	public void setDriverPicsRequest(boolean driverPicsRequest) {
		this.driverPicsRequest = driverPicsRequest;
	}


	public int describeContents() {
		return 0;
	}


	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeBooleanArray(new boolean[] {this.driversRequest} );
		parcel.writeBooleanArray(new boolean[] {this.teamsRequest} );
		parcel.writeBooleanArray(new boolean[] {this.blacklistTagsRequest} );
		parcel.writeBooleanArray(new boolean[] {this.blalistDevicesRequest} );
		parcel.writeBooleanArray(new boolean[] {this.driverPicsRequest}  );
	}
	
	
    /**
     * 
     */
    public static final Parcelable.Creator<UpdateRequest> CREATOR = new Parcelable.Creator<UpdateRequest>() {
        public UpdateRequest createFromParcel(Parcel mParcel) {
            return new UpdateRequest(mParcel);
        }

        public UpdateRequest[] newArray(int size) {
            return new UpdateRequest[size];
        }
    };


    /**
     * @param mParcel
     */
    private UpdateRequest(Parcel mParcel) {
    	boolean[] driversRequestBooleanArray = new boolean[1];
    	mParcel.readBooleanArray(driversRequestBooleanArray);
    	this.driversRequest = driversRequestBooleanArray[0];
    	
    	boolean[] teamsRequestBooleanArray = new boolean[1];
    	mParcel.readBooleanArray(teamsRequestBooleanArray);
    	this.teamsRequest = teamsRequestBooleanArray[0];
    	
    	boolean[] blacklistTagsBooleanArray = new boolean[1];
    	mParcel.readBooleanArray(blacklistTagsBooleanArray);
    	this.blacklistTagsRequest = blacklistTagsBooleanArray[0];
    	
    	boolean[] blalistDevicesBooleanArray = new boolean[1];
    	mParcel.readBooleanArray(blalistDevicesBooleanArray);
    	this.blalistDevicesRequest = blalistDevicesBooleanArray[0];
    	
    	boolean[] driverPicsBooleanArray = new boolean[1];
    	mParcel.readBooleanArray(driverPicsBooleanArray);
    	this.driverPicsRequest = driverPicsBooleanArray[0];
    }

}
