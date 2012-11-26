package de.tubs.cs.ibr.fsg.db.models;

import de.tubs.cs.ibr.fsg.db.DBHelper;
import android.content.ContentValues;

public class BlacklistedDevice {

	private int device_id;
	private String timestamp;
	
	public BlacklistedDevice(int device_id, String timestamp) {
		this.device_id = device_id;
		this.timestamp = timestamp;
	}

	public int getDevice_id() {
		return device_id;
	}

	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.BLACKLISTED_DEVICES_COLUMN_TAG_ID, device_id);
		values.put(DBHelper.BLACKLISTED_DEVICES_COLUMN_TIMESTAMP, timestamp);
		
		return values;
	}
	
}
