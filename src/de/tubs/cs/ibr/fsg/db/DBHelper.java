package de.tubs.cs.ibr.fsg.db;


import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_CLASSES = "classes";
	public static final String CLASSES_COLUMN_ID = "_id";
	public static final String CLASSES_COLUMN_CLASS = "class";
	public static final String CLASSES_COLUMN_EVENT_ID = "event_id";
	
	public static final String TABLE_TEAMS = "teams";
	public static final String TEAMS_COLUMN_ID = "_id";
	public static final String TEAMS_COLUMN_TEAM_ID = "team_id";
	public static final String TEAMS_COLUMN_CN = "cn";
	public static final String TEAMS_COLUMN_CN_SHORT_EN = "cn_short_en";
	public static final String TEAMS_COLUMN_CITY = "city";
	public static final String TEAMS_COLUMN_U = "u";
	public static final String TEAMS_COLUMN_CAR = "car";
	public static final String TEAMS_COLUMN_PIT = "pit";
	public static final String TEAMS_COLUMN_ISWAITING = "iswaiting";
	public static final String TEAMS_COLUMN_CLASS = "class";
	public static final String TEAMS_COLUMN_NAME_PITS = "name_pits";
	
	public static final String TABLE_DRIVERS = "drivers";
	public static final String DRIVERS_COLUMN_ID = "_id";
	public static final String DRIVERS_COLUMN_USER_ID = "user_id";
	public static final String DRIVERS_COLUMN_TEAM_ID = "team_id";
	public static final String DRIVERS_COLUMN_FIRST_NAME = "first_name";
	public static final String DRIVERS_COLUMN_LAST_NAME = "last_name";
	public static final String DRIVERS_COLUMN_GENDER = "gender";
	
	public static final String TABLE_BLACKLISTED_TAGS = "blTags";
	public static final String BLACKLISTED_TAGS_COLUMN_ID = "_id";
	public static final String BLACKLISTED_TAGS_COLUMN_TAG_ID = "tag_id";
	
	public static final String TABLE_BLACKLISTED_DEVICES = "blDevices";
	public static final String BLACKLISTED_DEVICES_COLUMN_ID = "_id";
	public static final String BLACKLISTED_DEVICES_COLUMN_TAG_ID = "tag_id";
	public static final String BLACKLISTED_DEVICES_COLUMN_TIMESTAMP = "timestamp";
	

	public static final String DATABASE_NAME = "fsg.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_CLASSES_CREATE = "CREATE TABLE " + TABLE_CLASSES + " (" 
			+ CLASSES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ CLASSES_COLUMN_CLASS + " TEXT NOT NULL,"
			+ CLASSES_COLUMN_EVENT_ID + " INTEGER NOT NULL);";

	public static final String TABLE_TEAMS_CREATE = "CREATE TABLE " + TABLE_TEAMS + " (" 
			+ TEAMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ TEAMS_COLUMN_TEAM_ID + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_CN + " TEXT NOT NULL," 
			+ TEAMS_COLUMN_CN_SHORT_EN + " TEXT NOT NULL,"
			+ TEAMS_COLUMN_CITY + " TEXT NOT NULL,"
			+ TEAMS_COLUMN_U + " TEXT NOT NULL,"
			+ TEAMS_COLUMN_CAR + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_PIT + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_ISWAITING + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_CLASS + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_NAME_PITS + " TEXT NOT NULL);";

	public static final String TABLE_DRIVERS_CREATE = "CREATE TABLE " + TABLE_DRIVERS + " ("
			+ DRIVERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ DRIVERS_COLUMN_USER_ID + " INTEGER NOT NULL,"
			+ DRIVERS_COLUMN_TEAM_ID + " INTEGER NOT NULL,"
			+ DRIVERS_COLUMN_FIRST_NAME + " TEXT NOT NULL,"
			+ DRIVERS_COLUMN_LAST_NAME + " TEXT NOT NULL,"
			+ DRIVERS_COLUMN_GENDER + " INTEGER NOT NULL)";
	
	public static final String TABLE_BLACKLISTED_TAGS_CREATE = "CREATE TABLE " + TABLE_BLACKLISTED_TAGS + " ("
			+ BLACKLISTED_TAGS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ BLACKLISTED_TAGS_COLUMN_TAG_ID + " INTEGER NOT NULL)";
	
	public static final String TABLE_BLACKLISTED_DEVICES_CREATE = "CREATE TABLE " + TABLE_BLACKLISTED_DEVICES + " ("
			+ BLACKLISTED_DEVICES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ BLACKLISTED_DEVICES_COLUMN_TAG_ID + " INTEGER NOT NULL,"
			+ BLACKLISTED_DEVICES_COLUMN_TIMESTAMP + " TEXT NOT NULL)";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CLASSES_CREATE);
		database.execSQL(TABLE_DRIVERS_CREATE);
		database.execSQL(TABLE_TEAMS_CREATE);
		database.execSQL(TABLE_BLACKLISTED_TAGS_CREATE);
		database.execSQL(TABLE_BLACKLISTED_DEVICES_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(), "Upgrading database from version " 
				+ oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLACKLISTED_DEVICES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLACKLISTED_TAGS);
	}

}

