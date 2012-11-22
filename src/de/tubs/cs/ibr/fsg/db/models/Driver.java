package de.tubs.cs.ibr.fsg.db.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Driver {
	
	private long id;
	private int user_id;
	private int team_id;
	private String first_name;
	private String last_name;
	private boolean female;
	
	public Driver(JSONObject driver) {
		try {
			this.id = 1;
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
		super();
		this.id = id;
		this.user_id = user_id;
		this.team_id = team_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.female = female;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	public String toString() {
		return this.user_id + "|"
				+ this.team_id + "|"
				+ this.first_name + "|"
				+ this.last_name + "|"
				+ this.female;
	}

}
