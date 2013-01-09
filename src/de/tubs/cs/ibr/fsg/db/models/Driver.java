package de.tubs.cs.ibr.fsg.db.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import de.tubs.cs.ibr.fsg.db.DBHelper;

import android.content.ContentValues;

public class Driver implements Serializable{

	private static final long serialVersionUID = 1L;
	private short user_id; 
	private short team_id;
	private String first_name;
	private String last_name;
	private Team team;
	
	public Driver(JSONObject driver) {
		try {
			this.user_id = (short) driver.getInt("UserID");
			this.team_id = (short) driver.getInt("TeamID");
			this.first_name = driver.getString("first_name");
			this.last_name = driver.getString("last_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public Driver(short user_id, short team_id, String first_name,
			String last_name) {
		this.user_id = user_id;
		this.team_id = team_id;
		this.first_name = first_name;
		this.last_name = last_name;
	}
	
	public Driver() {
		
	}

	public short getUser_id() {
		return user_id;
	}

	public void setUser_id(short user_id) {
		this.user_id = user_id;
	}

	public short getTeam_id() {
		return team_id;
	}

	public void setTeam_id(short team_id) {
		this.team_id = team_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.DRIVERS_COLUMN_USER_ID, this.user_id);
		values.put(DBHelper.DRIVERS_COLUMN_FIRST_NAME, this.first_name);
		values.put(DBHelper.DRIVERS_COLUMN_LAST_NAME, this.last_name);
		values.put(DBHelper.DRIVERS_COLUMN_TEAM_ID, this.team_id);

		return values;
	}
	
	public String toString() {
		return this.user_id + "|"
				+ this.team_id + "|"
				+ this.first_name + "|"
				+ this.last_name + "|";
	}

}
