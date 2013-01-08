package de.tubs.cs.ibr.fsg.db.models;

import java.util.Date;

import de.tubs.cs.ibr.fsg.db.DBHelper;

import android.content.ContentValues;

public class Briefing {
	
	private short briefingId;
	private Date startTime;
	private Date endTime;
	
	private RaceDiscipline raceDiscipline;
	
	public Briefing() {
		this.briefingId = 0;
		this.startTime = null;
		this.endTime = null;
		this.raceDiscipline = null;
	}
	
	public Briefing(short briefingId, RaceDiscipline raceDiscipline, Date startTime, Date endTime) {
		this.briefingId = briefingId;
		this.raceDiscipline = raceDiscipline;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public short getBriefingId() {
		return briefingId;
	}

	public void setBriefingId(short briefingId) {
		this.briefingId = briefingId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public RaceDiscipline getRaceDiscipline() {
		return raceDiscipline;
	}

	public void setRaceDiscipline(RaceDiscipline raceDiscipline) {
		this.raceDiscipline = raceDiscipline;
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		int startTime = (int)(this.startTime.getTime() / 1000L);
		int endTime = (int)(this.endTime.getTime() / 1000L);
		values.put(DBHelper.BRIEFINGS_COLUMN_ID, this.briefingId);
		values.put(DBHelper.BRIEFINGS_COLUMN_RACE_DISCIPLINE_ID, this.raceDiscipline.getRaceDisciplineId());
		values.put(DBHelper.BRIEFINGS_COLUMN_START_TIME, startTime);
		values.put(DBHelper.BRIEFINGS_COLUMN_END_TIME, endTime);
		return values;
	}
	
}
