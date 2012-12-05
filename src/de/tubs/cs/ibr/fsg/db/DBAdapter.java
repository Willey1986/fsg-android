package de.tubs.cs.ibr.fsg.db;

import java.util.*;

import org.json.*;

import de.tubs.cs.ibr.fsg.db.models.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	public DBAdapter(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	
	/**
	 * Schreibt ein JSON-Array [{"DeviceID":int,"Timestamp":"String"},...] mit geblacklisteten Geräten in die interne SQLite-Datenbank
	 * @param jsonArray
	 */
	public void writeBlacklistedDevicesToDB(String jsonArray) {
		try {
			open();
			JSONArray devices = new JSONArray(jsonArray);
			for(int i = 0; i < devices.length(); i++) {
				JSONObject jDevice = devices.getJSONObject(i);
				BlacklistedDevice device = new BlacklistedDevice(jDevice.getInt("DeviceID"), jDevice.getString("Timestamp"));
				writeBlacklistedDeviceToDB(device);
			}
			close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Schreibt ein JSON-Array [{...},{...}] mit geblacklisteten Armbändern in die interne SQLite-Datenbank
	 * @param jsonArray
	 */
	public void writeBlacklistedTagsToDB(String jsonArray) {
		try {
			open();
			JSONArray tags = new JSONArray(jsonArray);
			for(int i = 0; i < tags.length(); i++) {
				JSONObject jDevice = tags.getJSONObject(i);
				BlacklistedTag tag = new BlacklistedTag(jDevice.getInt("TagID"));
				writeBlacklistedTagToDB(tag);
			}
			close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schreibt ein einzelnes Driver Objekt in die Datenbank
	 * @param driver
	 */
	public void writeDriverToDB(Driver driver) {
		open();
		database.insertWithOnConflict(DBHelper.TABLE_DRIVERS, null, driver.getContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
		close();
	}
	
	/**
	 * Schreibt einen JSON-String mit Fahrerdaten in die Datenbank
	 * @param jsonArray
	 */
	public void writeDriversToDB(String jsonArray) {
		try {
			JSONArray jDrivers = new JSONArray(jsonArray);
			for(int i = 0; i < jDrivers.length(); i++) {
				Driver driver = new Driver(jDrivers.getJSONObject(i));
				writeDriverToDB(driver);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Driver getDriver(int driverID) {
		String sqlDriver = "SELECT * FROM " + DBHelper.TABLE_DRIVERS
				+ " CROSS JOIN " + DBHelper.TABLE_TEAMS 
				+ " WHERE teams.team_id=drivers.team_id"
				+ " AND drivers.user_id=" + driverID
				+ ";";
		open();
		Cursor cursor = database.rawQuery(sqlDriver, null);
		
		if(cursor.moveToFirst()) {
			Driver driver = new Driver();
			Team team = new Team();
			driver.setUser_id((short) cursor.getShort(0));
			driver.setTeam_id((short) cursor.getShort(1));
			driver.setFirst_name(cursor.getString(2));
			driver.setLast_name(cursor.getString(3));
			driver.setFemale((short) cursor.getShort(4));			
			team.setTeamId((short) cursor.getShort(5));
			team.setCn(cursor.getString(6));
			team.setCn_short_en(cursor.getString(7));
			team.setCity(cursor.getString(8));
			team.setUniversity(cursor.getString(9));
			team.setCarNr(cursor.getShort(10));
			team.setPitNr(cursor.getShort(11));
			team.setIsWaiting(cursor.getShort(12));
			team.setEventClass(cursor.getShort(13));
			team.setName_pits(cursor.getString(14));
			driver.setTeam(team);
			return driver;
		}
		else
			return null;
		
	}
	
	public ArrayList<Driver> getAllDrivers() {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		
		String sql = "SELECT * FROM " + DBHelper.TABLE_DRIVERS;
		open();
		Cursor cursor = database.rawQuery(sql, null);
		
		if(cursor.moveToFirst()) {
			do {
				Driver driver = new Driver();
				driver.setUser_id((short) cursor.getInt(0));
				driver.setTeam_id((short) cursor.getInt(1));
				driver.setFirst_name(cursor.getString(2));
				driver.setLast_name(cursor.getString(3));
				driver.setFemale((short) cursor.getShort(4));	
				
				drivers.add(driver);
			} while(cursor.moveToNext());
		}

		close();
		return drivers;
	}
	
	public ArrayList<Driver> getAllDriversByTeamID(int teamID) {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		
		String sql = "SELECT * FROM " + DBHelper.TABLE_DRIVERS
				+ " WHERE team_id=" + teamID + ";";
		open();
		Cursor cursor = database.rawQuery(sql, null);
		
		if(cursor.moveToFirst()) {
			do {
				Driver driver = new Driver();
				driver.setUser_id((short) cursor.getInt(0));
				driver.setTeam_id((short) cursor.getInt(1));
				driver.setFirst_name(cursor.getString(2));
				driver.setLast_name(cursor.getString(3));
				driver.setFemale((short) cursor.getShort(4));	
				
				drivers.add(driver);
			} while(cursor.moveToNext());
		}

		close();
		return drivers;
	}
	
	public ArrayList<Team> getAllTeams() {
		ArrayList<Team> teams = new ArrayList<Team>();
		
		String sql = "SELECT * FROM " + DBHelper.TABLE_TEAMS + ";";
		
		open();
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()) {
			do {
				Team team = new Team();
				team.setTeamId((short)cursor.getInt(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_TEAM_ID)));
				team.setName_pits(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_NAME_PITS)));
				team.setCn_short_en(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CN_SHORT_EN)));
				team.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CITY)));
				team.setUniversity(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_U)));
				
				teams.add(team);
			} while(cursor.moveToNext());
		}
		
		close();
		return teams;
	}
	
	public void writeTeamToDB(Team team) {
		open();
		database.insertWithOnConflict(DBHelper.TABLE_TEAMS, null, team.getContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
		close();
	}
	
	public void writeTeamsToDB(String jsonArray) {
		try {
			JSONArray jTeams = new JSONArray(jsonArray);
			for(int i = 0; i < jTeams.length(); i++) {
				Team team = new Team(jTeams.getJSONObject(i));
				writeTeamToDB(team);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void writeBlacklistedDeviceToDB(BlacklistedDevice blDevice) {
		ContentValues values = blDevice.getContentValues();
		database.insert(DBHelper.TABLE_BLACKLISTED_DEVICES, null, values);
	} 
	
	public void writeBlacklistedTagToDB(BlacklistedTag blTag) {
		ContentValues values = blTag.getContentValues();
		database.insert(DBHelper.TABLE_BLACKLISTED_TAGS, null, values);
	}
	
	

}
