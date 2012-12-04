package de.tubs.cs.ibr.fsg.db.models;

import org.json.JSONException;
import org.json.JSONObject;

import de.tubs.cs.ibr.fsg.db.DBHelper;

import android.content.ContentValues;

public class Driver {

	private int user_id; 
	private int team_id;
	private String first_name;
	private String last_name;
	private boolean female;
	private Team team;
	
	public Driver(JSONObject driver) {
		try {
			this.user_id = driver.getInt("UserID");
			this.team_id = driver.getInt("TeamID");
			this.first_name = driver.getString("first_name");
			this.last_name = driver.getString("last_name");
			switch (driver.getInt("gender")) {
				case 0:
					this.female = false;
					break;
				case 1:
					this.female = true;
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public Driver(long id, int user_id, int team_id, String first_name,
			String last_name, boolean female) {
		this.user_id = user_id;
		this.team_id = team_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.female = female;
	}
	
	public Driver() {
		
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
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

	public boolean isFemale() {
		return female;
	}

	public void setGender(boolean female) {
		this.female = female;
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
		if (female)
			values.put(DBHelper.DRIVERS_COLUMN_GENDER, 1);
		else
			values.put(DBHelper.DRIVERS_COLUMN_GENDER, 0);
		
		return values;
	}
	
	public String toString() {
		return this.user_id + "|"
				+ this.team_id + "|"
				+ this.first_name + "|"
				+ this.last_name + "|"
				+ this.female;
	}

}
