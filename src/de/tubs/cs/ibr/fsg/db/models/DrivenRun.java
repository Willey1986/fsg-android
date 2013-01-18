package de.tubs.cs.ibr.fsg.db.models;

public class DrivenRun {

	private Driver driver;
	private int raceDiscipline;
	private boolean valid;
	private String timeStamp;
	private String result;
	
	public DrivenRun() {
		this.driver = new Driver();
		this.raceDiscipline = 0;
		this.valid = true;
		this.timeStamp = new String();
		this.result = "empty";
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public int getRaceDiscipline() {
		return raceDiscipline;
	}

	public void setRaceDiscipline(int raceDiscipline) {
		this.raceDiscipline = raceDiscipline;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
	
}
