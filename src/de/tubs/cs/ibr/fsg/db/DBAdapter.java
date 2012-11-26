package de.tubs.cs.ibr.fsg.db;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	public DBAdapter(Context context) {
		this.dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		this.database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		this.dbHelper.close();
	}
	
	
	/**
	 * Schreibt ein JSON-Array [{...},{...}] mit geblacklisteten Geräten in die interne SQLite-Datenbank
	 * @param jsonArray
	 */
	public void writeBlacklistedDevices(String jsonArray) {
		//TODO
	}
	
	
	/**
	 * Schreibt ein JSON-Array [{...},{...}] mit geblacklisteten Armbändern in die interne SQLite-Datenbank
	 * @param jsonArray
	 */
	public void writeBlacklistedTags(String jsonArray) {
		try {
			JSONArray tags = new JSONArray(jsonArray);
			for (int i = 0; i <= tags.length(); i++) {
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

}
