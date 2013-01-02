package de.tubs.cs.ibr.fsg.db.models;

import org.json.JSONException;
import org.json.JSONObject;

import de.tubs.cs.ibr.fsg.db.DBHelper;

import android.content.ContentValues;

public class Team {
	
	private short teamId;
	private String cn;
	private String cn_short_en;
	private String city;
	private String university;
	private short carNr;
	private short pitNr;
	private short isWaiting;
	private short eventClass_id;
	private String name_pits;
	
	public Team() {
		this.teamId = 0;
		this.cn = null;
		this.cn_short_en = null;
		this.city = null;
		this.university = null;
		this.carNr = 0;
		this.pitNr = 0;
		this.isWaiting = 0;
		this.eventClass_id = 0;
		this.name_pits = null;
	}
	
	public Team(short teamId, String cn, String cn_short_en,
			String city, String university, short carNr, short pitNr,
			short isWaiting, short eventClass_id, String name_pits) {
		this.teamId = teamId;
		this.cn = cn;
		this.cn_short_en = cn_short_en;
		this.city = city;
		this.university = university;
		this.carNr = carNr;
		this.pitNr = pitNr;
		this.isWaiting = isWaiting;
		this.eventClass_id = eventClass_id;
		this.name_pits = name_pits;
	}
	
	public Team(JSONObject team) {
		try {
			this.teamId = (short) team.getInt("TeamID");
			this.cn = team.getString("CN");
			this.cn_short_en = team.getString("cn_short_en");
			this.city = team.getString("city");
			this.university = team.getString("U");
			this.carNr = (short) team.getInt("Car");
			this.pitNr = (short) team.getInt("Pit");
			this.isWaiting = (short) team.getInt("iswaiting");
			this.eventClass_id = (short) team.getInt("class");
			this.name_pits = team.getString("name_pits");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public short getTeamId() {
		return teamId;
	}

	public void setTeamId(short teamId) {
		this.teamId = teamId;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getCn_short_en() {
		return cn_short_en;
	}

	public void setCn_short_en(String cn_short_en) {
		this.cn_short_en = cn_short_en;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public short getCarNr() {
		return carNr;
	}

	public void setCarNr(short carNr) {
		this.carNr = carNr;
	}

	public short getPitNr() {
		return pitNr;
	}

	public void setPitNr(short pitNr) {
		this.pitNr = pitNr;
	}

	public short getIsWaiting() {
		return isWaiting;
	}

	public void setIsWaiting(short isWaiting) {
		this.isWaiting = isWaiting;
	}

	public short getEventClassId() {
		return eventClass_id;
	}

	public void setEventClass(short eventClass_id) {
		this.eventClass_id = eventClass_id;
	}

	public String getName_pits() {
		return name_pits;
	}

	public void setName_pits(String name_pits) {
		this.name_pits = name_pits;
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.TEAMS_COLUMN_TEAM_ID, this.teamId);
		values.put(DBHelper.TEAMS_COLUMN_CN, this.cn);
		values.put(DBHelper.TEAMS_COLUMN_CN_SHORT_EN, this.cn_short_en);
		values.put(DBHelper.TEAMS_COLUMN_CITY, this.city);
		values.put(DBHelper.TEAMS_COLUMN_U, this.university);
		values.put(DBHelper.TEAMS_COLUMN_CAR, this.carNr);
		values.put(DBHelper.TEAMS_COLUMN_PIT, this.pitNr);
		values.put(DBHelper.TEAMS_COLUMN_ISWAITING, this.isWaiting);
		values.put(DBHelper.TEAMS_COLUMN_EVENT_CLASS_ID, this.eventClass_id);
		values.put(DBHelper.TEAMS_COLUMN_NAME_PITS, this.name_pits);

		return values;
	}

}
