package de.tubs.cs.ibr.fsg.db.models;

import java.util.Date;

public class DrivenRun {

	private Driver driver;
	private RaceDiscipline raceDiscipline;
	private boolean valid;
	private String timeStamp;
	private String result;
	
	public DrivenRun() {
		this.driver = new Driver();
		this.raceDiscipline = new RaceDiscipline();
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

	public RaceDiscipline getRaceDiscipline() {
		return raceDiscipline;
	}

	public void setRaceDiscipline(RaceDiscipline raceDiscipline) {
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
