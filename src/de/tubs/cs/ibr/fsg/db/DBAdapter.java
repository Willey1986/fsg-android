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
		ContentValues values = driver.getContentValues();
		database.insert(DBHelper.TABLE_DRIVERS, null, values);
	}
	
	/**
	 * Schreibt einen JSON-String mit Fahrerdaten in die Datenbank
	 * @param jsonArray
	 */
	public void writeDriversToDB(String jsonArray) {
		try {
			JSONArray jDrivers = new JSONArray(jsonArray);
			open();
			for(int i = 0; i < jDrivers.length(); i++) {
				Driver driver = new Driver(jDrivers.getJSONObject(i));
				writeDriverToDB(driver);
			}
			close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Driver getDriver(int driverID) {
		String sql = "SELECT * FROM " + DBHelper.TABLE_DRIVERS
				+ " WHERE " + DBHelper.DRIVERS_COLUMN_USER_ID
				+ " = " + driverID + ";";
		open();
		Cursor cursor = database.rawQuery(sql, null);
		
		if(cursor.moveToFirst()) {
			Driver driver = new Driver();
			driver.setUser_id(cursor.getInt(0));
			driver.setTeam_id(cursor.getInt(1));
			driver.setTeam(null);
			driver.setFirst_name(cursor.getString(2));
			driver.setLast_name(cursor.getString(3));
			if (cursor.getInt(4)==0)
				driver.setGender(false);
			else 
				driver.setGender(true);
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
				driver.setUser_id(cursor.getInt(0));
				driver.setTeam_id(cursor.getInt(1));
				driver.setFirst_name(cursor.getString(2));
				driver.setLast_name(cursor.getString(3));
				if (cursor.getInt(4)==0)
					driver.setGender(false);
				else
					driver.setGender(true);
				drivers.add(driver);
			} while(cursor.moveToNext());
		}

		close();
		return drivers;
	}
	
	public void writeTeamToDB(Team team) {
		
	}
	
	public void writeTeamsToDB(String jsonArray) {
		
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
