package de.tubs.cs.ibr.fsg.db.models;

public class Run {

	private int raceDisciplineID;
	private String result;
	
	public Run() {
		this.raceDisciplineID = 0;
		this.result = "";
	}

	public int getRaceDisciplineID() {
		return raceDisciplineID;
	}

	public void setRaceDisciplineID(int raceDisciplineID) {
		this.raceDisciplineID = raceDisciplineID;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
	
}
