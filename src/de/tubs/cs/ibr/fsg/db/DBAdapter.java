package de.tubs.cs.ibr.fsg.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.json.*;

import de.tubs.cs.ibr.fsg.db.models.*;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

import android.content.*;
import android.database.*;
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
	
	public void writeSampleData() {
		database.delete(DBHelper.TABLE_DRIVERS, null, null);
		database.delete(DBHelper.TABLE_TEAMS, null, null);
		database.delete(DBHelper.TABLE_BRIEFINGS, null, null);
		database.delete(DBHelper.TABLE_RACE_DISCIPLINES, null, null);
		
		Driver driver1 = new Driver((short)81, (short)20, "Harald", "Juhnke");
		Driver driver2 = new Driver((short)82, (short)20, "Stefan", "Raab");
		Driver driver3 = new Driver((short)83, (short)20, "Claudia", "Roth");
		Driver driver4 = new Driver((short)84, (short)21, "Sebastian", "Vettel");
		Driver driver5 = new Driver((short)85, (short)21, "Nikki", "Lauda");
		Driver driver6 = new Driver((short)86, (short)21, "Fernando", "Alonso");
		
		Team team1 = new Team((short)20, "DE", "Germany", "Braunschweig", "TU", (short)11,(short) 3,(short) 0,(short) 1, "Lions Racing Team");
		Team team2 = new Team((short)21, "E", "Spain", "Barcelona", "U", (short)20,(short) 4,(short) 0,(short) 1, "Fernando Alonso Racing");
		
		RaceDiscipline discipline1 = new RaceDiscipline((short)100, "Acceleration");
		RaceDiscipline discipline2 = new RaceDiscipline((short)101, "Skid Pad");
		RaceDiscipline discipline3 = new RaceDiscipline((short)102, "Autocross");
		RaceDiscipline discipline4 = new RaceDiscipline((short)103, "Endurance");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		Date startTime1 = new Date();
		Date startTime2 = new Date();
		Date startTime3 = new Date();
		Date startTime4 = new Date();
		Date startTime5 = new Date();
		Date startTime6 = new Date();
		Date startTime7 = new Date();
		Date startTime8 = new Date();
		
		Date endTime1 = new Date();
		Date endTime2 = new Date();
		Date endTime3 = new Date();
		Date endTime4 = new Date();
		Date endTime5 = new Date();
		Date endTime6 = new Date();
		Date endTime7 = new Date();
		Date endTime8 = new Date();
		
		try {
			startTime1 = dateFormat.parse("20.08.2013 10:00:00");
			startTime2 = dateFormat.parse("20.08.2013 13:00:00");
			startTime3 = dateFormat.parse("21.08.2013 10:00:00");
			startTime4 = dateFormat.parse("21.08.2013 13:00:00");
			startTime5 = dateFormat.parse("22.08.2013 10:00:00");
			startTime6 = dateFormat.parse("22.08.2013 13:00:00");
			startTime7 = dateFormat.parse("23.08.2013 10:00:00");
			startTime8 = dateFormat.parse("23.08.2013 13:00:00");
			endTime1 = dateFormat.parse("20.08.2013 12:00:00");
			endTime2 = dateFormat.parse("20.08.2013 15:00:00");
			endTime3 = dateFormat.parse("21.08.2013 12:00:00");
			endTime4 = dateFormat.parse("21.08.2013 15:00:00");
			endTime5 = dateFormat.parse("22.08.2013 12:00:00");
			endTime6 = dateFormat.parse("22.08.2013 15:00:00");
			endTime7 = dateFormat.parse("23.08.2013 12:00:00");
			endTime8 = dateFormat.parse("23.08.2013 15:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Briefing briefing1 = new Briefing((short)100, discipline1, startTime1, endTime1);
		Briefing briefing2 = new Briefing((short)101, discipline1, startTime2, endTime2);
		Briefing briefing3 = new Briefing((short)102, discipline2, startTime3, endTime3);
		Briefing briefing4 = new Briefing((short)103, discipline2, startTime4, endTime4);
		Briefing briefing5 = new Briefing((short)104, discipline3, startTime5, endTime5);
		Briefing briefing6 = new Briefing((short)105, discipline3, startTime6, endTime6);
		Briefing briefing7 = new Briefing((short)106, discipline4, startTime7, endTime7);
		Briefing briefing8 = new Briefing((short)107, discipline4, startTime8, endTime8);
		
		database.insert(DBHelper.TABLE_DRIVERS, null, driver1.getContentValues());
		database.insert(DBHelper.TABLE_DRIVERS, null, driver2.getContentValues());
		database.insert(DBHelper.TABLE_DRIVERS, null, driver3.getContentValues());
		database.insert(DBHelper.TABLE_DRIVERS, null, driver4.getContentValues());
		database.insert(DBHelper.TABLE_DRIVERS, null, driver5.getContentValues());
		database.insert(DBHelper.TABLE_DRIVERS, null, driver6.getContentValues());
		
		database.insert(DBHelper.TABLE_TEAMS, null, team1.getContentValues());
		database.insert(DBHelper.TABLE_TEAMS, null, team2.getContentValues());
		
		database.insert(DBHelper.TABLE_RACE_DISCIPLINES, null, discipline1.getContentValues());
		database.insert(DBHelper.TABLE_RACE_DISCIPLINES, null, discipline2.getContentValues());
		database.insert(DBHelper.TABLE_RACE_DISCIPLINES, null, discipline3.getContentValues());
		database.insert(DBHelper.TABLE_RACE_DISCIPLINES, null, discipline4.getContentValues());
		
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing1.getContentValues());
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing2.getContentValues());
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing3.getContentValues());
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing4.getContentValues());
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing5.getContentValues());
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing6.getContentValues());
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing7.getContentValues());
		database.insert(DBHelper.TABLE_BRIEFINGS, null, briefing8.getContentValues());
		
		writeKeyValue("secret", "geheimerKey");
	}
	
	
	/**
	 * Schreibt ein JSON-Array [{"DeviceID":int,"Timestamp":"String"},...] mit geblacklisteten Ger�ten in die interne SQLite-Datenbank
	 * @param jsonArray
	 */
	public void writeBlacklistedDevicesToDB(String jsonArray) {
		try {
			JSONArray devices = new JSONArray(jsonArray);
			for(int i = 0; i < devices.length(); i++) {
				JSONObject jDevice = devices.getJSONObject(i);
				BlacklistedDevice device = new BlacklistedDevice(jDevice.getInt("DeviceID"), jDevice.getString("Timestamp"));
				writeBlacklistedDeviceToDB(device);
			}
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
			JSONArray tags = new JSONArray(jsonArray);
			for(int i = 0; i < tags.length(); i++) {
				JSONObject jDevice = tags.getJSONObject(i);
				BlacklistedTag tag = new BlacklistedTag(jDevice.getInt("TagID"));
				writeBlacklistedTagToDB(tag);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schreibt ein einzelnes Driver Objekt in die Datenbank
	 * @param driver
	 */
	public void writeDriverToDB(Driver driver) {
		database.insertWithOnConflict(DBHelper.TABLE_DRIVERS, null, driver.getContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
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
				+ " WHERE user_id=" + driverID + ";";
		Cursor cursor = database.rawQuery(sqlDriver, null);
		
		if(cursor.moveToFirst()) {
			Driver driver = new Driver();
			driver.setUser_id(cursor.getShort(cursor.getColumnIndex(DBHelper.DRIVERS_COLUMN_USER_ID)));
			driver.setTeam_id(cursor.getShort(cursor.getColumnIndex(DBHelper.DRIVERS_COLUMN_TEAM_ID)));
			driver.setFirst_name(cursor.getString(cursor.getColumnIndex(DBHelper.DRIVERS_COLUMN_FIRST_NAME)));
			driver.setLast_name(cursor.getString(cursor.getColumnIndex(DBHelper.DRIVERS_COLUMN_LAST_NAME)));			
			driver.setTeam(getTeam(driver.getTeam_id()));
			cursor.close();
			return driver;
		}
		else
			return null;	
	}
	
	/**
	 * List alle Fahrer aus der Datenbank aus und gibt sie in einer ArrayList zur�ck
	 * @return = ArrayList mit s�mtlichen in der DB vorhandenen Teams
	 */
	public ArrayList<Driver> getAllDrivers() {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		String sql = "SELECT * FROM " + DBHelper.TABLE_DRIVERS + " ORDER BY " + DBHelper.DRIVERS_COLUMN_LAST_NAME + " ASC;";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()) {
			do {
				short driverID = cursor.getShort(cursor.getColumnIndex(DBHelper.DRIVERS_COLUMN_USER_ID));
				drivers.add(getDriver(driverID));
			} while(cursor.moveToNext());
		}
		cursor.close();
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
		Cursor cursor = rawQuery(sql);

		if(cursor.moveToFirst()) {
			do {
				short driverID = cursor.getShort(cursor.getColumnIndex(DBHelper.DRIVERS_COLUMN_USER_ID));
				drivers.add(getDriver(driverID));
			} while(cursor.moveToNext());
		}
		cursor.close();
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
		
		Cursor cursor = database.rawQuery(sql, null);
		
		if(cursor.moveToFirst()) {
			team.setTeamId(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_TEAM_ID)));
			team.setCn(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CN)));
			team.setCn_short_en(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CN_SHORT_EN)));
			team.setCity(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CITY)));
			team.setUniversity(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_U)));
			team.setCarNr(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_CAR)));
			team.setPitNr(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_PIT)));
			team.setIsWaiting(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_ISWAITING)));
			team.setEventClass(cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_EVENT_CLASS_ID)));
			team.setName_pits(cursor.getString(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_NAME_PITS)));
		}
		cursor.close();
		return team;
	}
	
	/**
	 * Liest alle Teams aus und speichert sie in einer ArrayList
	 * @return = ArrayList die s�mtliche Teams der Datenbank enth�lt
	 */
	public ArrayList<Team> getAllTeams() {
		ArrayList<Team> teams = new ArrayList<Team>();
		String sql = "SELECT * FROM " + DBHelper.TABLE_TEAMS + ";";
		Cursor cursor = rawQuery(sql);	
		if(cursor.moveToFirst()) {
			do {
				short teamID = cursor.getShort(cursor.getColumnIndex(DBHelper.TEAMS_COLUMN_TEAM_ID));
				teams.add(getTeam(teamID));
			} while(cursor.moveToNext());
		}
		cursor.close();
		return teams;
	}
	
	/**
	 * Schreibt ein Team-Objekt in die Datenbank
	 * @param team
	 */
	public void writeTeamToDB(Team team) {
		database.insertWithOnConflict(DBHelper.TABLE_TEAMS, null, team.getContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
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
	
	public void writeCheckIn(short driverId, short briefingId) throws FsgException {
		Date now = new Date();
		String timestamp = "" + (now.getTime() / 1000L);
		String sql = "INSERT OR IGNORE INTO " + DBHelper.TABLE_CHECKED_IN + " (" 
				+ DBHelper.CHECKED_IN_COLUMN_DRIVER_ID + ", "
				+ DBHelper.CHECKED_IN_COLUMN_BRIEFING_ID + ", "
				+ DBHelper.CHECKED_IN_COLUMN_VALID + ", "
				+ DBHelper.CHECKED_IN_COLUMN_TIMESTAMP + ") VALUES ("
				+ driverId + ", "
				+ briefingId + ", "
				+ "1,"
				+ timestamp +")";
		if (!isCheckedIn(driverId, briefingId))
			execSQL(sql);
		else {
			throw new FsgException(null, "DBAdapter", FsgException.DRIVER_ALREADY_CHECKED_IN);
		}
	}
	
	public boolean isCheckedIn(short driverId, short briefingId) {
		String sql = "SELECT * FROM " + DBHelper.TABLE_CHECKED_IN 
				+ " WHERE " 
				+ DBHelper.CHECKED_IN_COLUMN_DRIVER_ID + "=" + driverId + " AND "
				+ DBHelper.CHECKED_IN_COLUMN_BRIEFING_ID + "=" + briefingId + " AND "
				+ DBHelper.CHECKED_IN_COLUMN_VALID + "=1";
		Cursor cursor = rawQuery(sql);
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		else {
			cursor.close();
			return false;
		}
	}
	
	public void writeCheckOut(short driverId, short briefingId, int checkedInId) throws FsgException{
		if (isCheckedIn(driverId, briefingId)) {
			Date now = new Date();
			String timestamp = "" + (now.getTime() / 1000L);
			String sql = "INSERT OR IGNORE INTO " + DBHelper.TABLE_CHECKED_OUT + " (" 
					+ DBHelper.CHECKED_OUT_COLUMN_DRIVER_ID + ", "
					+ DBHelper.CHECKED_OUT_COLUMN_BRIEFING_ID + ", "
					+ DBHelper.CHECKED_OUT_COLUMN_CHECKED_IN_ID + ", "
					+ DBHelper.CHECKED_OUT_COLUMN_TIMESTAMP + ") VALUES ("
					+ driverId + ", "
					+ briefingId + ", "
					+ checkedInId + ", "
					+ timestamp +")";
			execSQL(sql);
			
			
		} else {
			throw new FsgException(null, "DBAdapter", FsgException.DRIVER_NOT_CHECKED_IN);
		}
		
	}
	
	/**
	 * Schreibt ein Objekt BlacklistedDevice in die Datenbank
	 * @param blDevice = Blacklisted Device
	 */
	public void writeBlacklistedDeviceToDB(BlacklistedDevice blDevice) {
		ContentValues values = blDevice.getContentValues();
		database.insert(DBHelper.TABLE_BLACKLISTED_DEVICES, null, values);
	} 
	
	/**
	 * Schreibt ein Objekt BlacklistedTag in die Datenbank
	 * @param blTag = Blacklisted Tag
	 */
	public void writeBlacklistedTagToDB(BlacklistedTag blTag) {
		ContentValues values = blTag.getContentValues();
		database.insert(DBHelper.TABLE_BLACKLISTED_TAGS, null, values);
	}
	
	public boolean existsKeyValue(String key) {
		Cursor cursor = database.query(DBHelper.TABLE_VALUES, new String[] {"key", "value"}, "key='"+key+"'", null, null, null, null);
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		} else {
			cursor.close();
			return false;
		}
	}
	
	public void writeKeyValue(String key, String value) {
		if (!existsKeyValue(key)) {
			ContentValues values = new ContentValues();
			values.put(DBHelper.VALUES_COLUMN_KEY, key);
			values.put(DBHelper.VALUES_COLUMN_VALUE, value);
			database.insert(DBHelper.TABLE_VALUES, null, values);
		}
	}
	
	public String getKeyValue(String key) {
		Cursor cursor = database.query(DBHelper.TABLE_VALUES, new String[] {"key", "value"}, "key='"+key+"'", null, null, null, null);
		String value = "";
		if (cursor.moveToFirst()) {
			value = cursor.getString(cursor.getColumnIndex(DBHelper.VALUES_COLUMN_KEY));
			cursor.close();
			return value;
		} else {
			value = "";
			cursor.close();
			return value;
		}
	}
	
	/**
	 * Dient dem direkten Ausf�hren von SQL-Ausdr�cken
	 * @param sql = SQL-String
	 */
	public void execSQL(String sql) {
		database.execSQL(sql);
	}
	
	/**
	 * Dient dem direkten Ausf�hren von SQL-Abfragen 
	 * @param sql = SQL-String
	 * @return Ergebnis der Abfrage als Cursor
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Cursor rawQuery(String sql){
		Cursor cursor = database.rawQuery(sql, null);
		return cursor;
	}
	
	
	
	

}
