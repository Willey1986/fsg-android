package de.tubs.cs.ibr.fsg.db;

import java.util.*;

import org.json.*;

import de.tubs.cs.ibr.fsg.db.models.*;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

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
	 * Schreibt ein JSON-Array [{"DeviceID":int,"Timestamp":"String"},...] mit geblacklisteten Ger�ten in die interne SQLite-Datenbank
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
	 * Schreibt ein JSON-Array [{...},{...}] mit geblacklisteten Armb�ndern in die interne SQLite-Datenbank
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
	
	/**
	 * List einen einzelnen Fahrer anhand der seiner ID aus der Datenbank aus
	 * @param driverID = FahrerID
	 * @return = der gew�nschte Fahrer
	 */
	public Driver getDriver(short driverID) {
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
			close();
			return driver;
		}
		else
			close();
			return null;	
	}
	
	/**
	 * List alle Fahrer aus der Datenbank aus und gibt sie in einer ArrayList zur�ck
	 * @return = ArrayList mit s�mtlichen in der DB vorhandenen Teams
	 */
	public ArrayList<Driver> getAllDrivers() {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		
		String sql = "SELECT * FROM " + DBHelper.TABLE_DRIVERS + " ORDER BY " + DBHelper.DRIVERS_COLUMN_LAST_NAME + " ASC;";
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
				driver.setTeam(getTeam(driver.getTeam_id()));
				drivers.add(driver);
			} while(cursor.moveToNext());
		}

		close();
		return drivers;
	}
	
	/**
	 * Liest alle Fahrer eines bestimmten Teams aus der Datenbank aus und gibt eine entsprechende ArrayList zur�ck
	 * @param teamID = Gew�nschte TeamID
	 * @return = ArrayList mit allen Fahrern eines Teams
	 */
	public ArrayList<Driver> getAllDriversByTeamID(short teamID) {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		
		String sql = "SELECT * FROM " + DBHelper.TABLE_DRIVERS
				+ " WHERE team_id=" + teamID 
				+ " ORDER BY " + DBHelper.DRIVERS_COLUMN_LAST_NAME 
				+ " ASC";
		System.out.println(sql);
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
				driver.setTeam(getTeam(teamID));
				drivers.add(driver);
			} while(cursor.moveToNext());
		}

		close();
		return drivers;
	}
	
	/**
	 * Liest ein einzelnes Team anhand der TeamID aus der Datenbank aus
	 * @param teamID = ID des Teams
	 * @return = Das gew�nschte Team
	 */
	public Team getTeam(short teamID) {
		Team team = new Team();
		String sql = "SELECT * FROM " + DBHelper.TABLE_TEAMS
				+ " WHERE team_id=" + teamID
				+ ";";
		
		open();
		Cursor cursor = database.rawQuery(sql, null);
		
		if(cursor.moveToFirst()) {
			team.setTeamId(cursor.getShort(0));
			team.setCn(cursor.getString(1));
			team.setCn_short_en(cursor.getString(2));
			team.setCity(cursor.getString(3));
			team.setUniversity(cursor.getString(4));
			team.setCarNr(cursor.getShort(5));
			team.setPitNr(cursor.getShort(6));
			team.setIsWaiting(cursor.getShort(7));
			team.setEventClass(cursor.getShort(8));
			team.setName_pits(cursor.getString(9));
		}
		return team;
	}
	
	/**
	 * Liest alle Teams aus und speichert sie in einer ArrayList
	 * @return = ArrayList die s�mtliche Teams der Datenbank enth�lt
	 */
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
				team.setCn(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CN)));
				team.setEventClass(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CLASS)));
				team.setCarNr(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CAR)));
				team.setPitNr(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_PIT)));
				team.setIsWaiting(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_ISWAITING)));
				teams.add(team);
			} while(cursor.moveToNext());
		}
		
		close();
		return teams;
	}
	
	/**
	 * Schreibt ein Team-Objekt in die Datenbank
	 * @param team
	 */
	public void writeTeamToDB(Team team) {
		open();
		database.insertWithOnConflict(DBHelper.TABLE_TEAMS, null, team.getContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
		close();
	}
	
	/**
	 * Schreibt einen String der ein JSON-Array mit Teamdaten enth�lt in die Datenbank
	 * @param jsonArray = String der JSON-Array enth�lt
	 */
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
	
	/**
	 * Schreibt ein Objekt BlacklistedDevice in die Datenbank
	 * @param blDevice = Blacklisted Device
	 */
	public void writeBlacklistedDeviceToDB(BlacklistedDevice blDevice) {
		ContentValues values = blDevice.getContentValues();
		open();
		database.insert(DBHelper.TABLE_BLACKLISTED_DEVICES, null, values);
		close();
	} 
	
	/**
	 * Schreibt ein Objekt BlacklistedTag in die Datenbank
	 * @param blTag = Blacklisted Tag
	 */
	public void writeBlacklistedTagToDB(BlacklistedTag blTag) {
		ContentValues values = blTag.getContentValues();
		open();
		database.insert(DBHelper.TABLE_BLACKLISTED_TAGS, null, values);
		close();
	}
	
	/**
	 * Dient dem direkten Ausf�hren von SQL-Ausdr�cken
	 * @param sql = SQL-String
	 */
	public void execSQL(String sql) {
		open();
		database.execSQL(sql);
		close();
	}
	
	/**
	 * Dient dem direkten Ausf�hren von SQL-Abfragen 
	 * @param sql = SQL-String
	 * @return Ergebnis der Abfrage als Cursor
	 */
	public Cursor rawQuery(String sql) {
		open();
		Cursor cursor = database.rawQuery(sql, null);
		close();
		return cursor;
	}
	
	

}
