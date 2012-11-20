package de.tubs.cs.ibr.fsg.db.models;

public class BlacklistedDevice {

	private long id;
	private int device_id;
	private String timestamp;
	
	public BlacklistedDevice(long id, int device_id, String timestamp) {
		super();
		this.id = id;
		this.device_id = device_id;
		this.timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
}
