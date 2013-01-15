package de.tubs.cs.ibr.fsg.db.models;

import java.util.Date;

import de.tubs.cs.ibr.fsg.db.DBHelper;

import android.content.ContentValues;

public class Briefing {
	
	private short briefingId;
	private long startTime;
	private long endTime;
	
	private RaceDiscipline raceDiscipline;
	
	public Briefing() {
		this.briefingId = 0;
		this.startTime = 0;
		this.endTime = 0;
		this.raceDiscipline = null;
	}
	
	public Briefing(short briefingId, RaceDiscipline raceDiscipline, long startTime, long endTime) {
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

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
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
		values.put(DBHelper.BRIEFINGS_COLUMN_ID, this.briefingId);
		values.put(DBHelper.BRIEFINGS_COLUMN_RACE_DISCIPLINE_ID, this.raceDiscipline.getRaceDisciplineId());
		values.put(DBHelper.BRIEFINGS_COLUMN_START_TIME, startTime);
		values.put(DBHelper.BRIEFINGS_COLUMN_END_TIME, endTime);
		return values;
	}
	
}
