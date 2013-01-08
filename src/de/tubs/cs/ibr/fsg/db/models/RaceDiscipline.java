package de.tubs.cs.ibr.fsg.db.models;

import de.tubs.cs.ibr.fsg.db.DBHelper;
import android.content.ContentValues;

public class RaceDiscipline {

	private short raceDisciplineId;
	private String name;
	
	public RaceDiscipline() {
		this.name = null;
		this.raceDisciplineId = 0;
	}
	
	public RaceDiscipline(short raceDisciplineId, String name) {
		this.raceDisciplineId = raceDisciplineId;
		this.name = name;
	}

	public short getRaceDisciplineId() {
		return raceDisciplineId;
	}

	public void setRaceDisciplineId(short raceDisciplineId) {
		this.raceDisciplineId = raceDisciplineId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.RACE_DISCIPLINES_COLUMN_ID, this.raceDisciplineId);
		values.put(DBHelper.RACE_DISCIPLINES_COLUMN_NAME, this.name);
		return values;
	}

}
