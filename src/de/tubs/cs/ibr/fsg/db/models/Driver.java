package de.tubs.cs.ibr.fsg.db.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import de.tubs.cs.ibr.fsg.db.DBHelper;

import android.content.ContentValues;

public class Driver implements Serializable{

	private static final long serialVersionUID = 1L;
	private short driverID; 
	private short teamID;
	private String firstName;
	private String lastName;
	private Team team;
	
	public Driver(JSONObject driver) {
		try {
			this.driverID = (short) driver.getInt("UserID");
			this.teamID = (short) driver.getInt("TeamID");
			this.firstName = driver.getString("first_name");
			this.lastName = driver.getString("last_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public Driver(short userID, short teamID, String firstName,
			String lastName) {
		this.driverID = userID;
		this.teamID = teamID;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Driver() {
		this.team = new Team();
		this.driverID = 0;
		this.teamID = 0;
		this.firstName = "";
		this.lastName = "";
	}

	public short getDriverID() {
		return this.driverID;
	}

	public void setDriverID(short driverID) {
		this.driverID = driverID;
	}

	public short getTeamID() {
		return this.teamID;
	}

	public void setTeamID(short teamID) {
		this.teamID = teamID;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.DRIVERS_COLUMN_USER_ID, this.driverID);
		values.put(DBHelper.DRIVERS_COLUMN_FIRST_NAME, this.firstName);
		values.put(DBHelper.DRIVERS_COLUMN_LAST_NAME, this.lastName);
		values.put(DBHelper.DRIVERS_COLUMN_TEAM_ID, this.teamID);

		return values;
	}
	
	public String toString() {
		return this.driverID + "|"
				+ this.teamID + "|"
				+ this.firstName + "|"
				+ this.lastName + "|";
	}

}
