package de.tubs.cs.ibr.fsg.db;


import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_CLASSES = "event_classes";
	public static final String CLASSES_COLUMN_CLASS = "name";
	public static final String CLASSES_COLUMN_EVENT_ID = "event_id";
	
	public static final String TABLE_TEAMS = "teams";
	public static final String TEAMS_COLUMN_TEAM_ID = "team_id";
	public static final String TEAMS_COLUMN_CN = "cn";
	public static final String TEAMS_COLUMN_CN_SHORT_EN = "cn_short_en";
	public static final String TEAMS_COLUMN_CITY = "city";
	public static final String TEAMS_COLUMN_U = "u";
	public static final String TEAMS_COLUMN_CAR = "car";
	public static final String TEAMS_COLUMN_PIT = "pit";
	public static final String TEAMS_COLUMN_ISWAITING = "iswaiting";
	public static final String TEAMS_COLUMN_EVENT_CLASS_ID = "event_class_id";
	public static final String TEAMS_COLUMN_NAME_PITS = "name_pits";
	
	public static final String TABLE_DRIVERS = "drivers";
	public static final String DRIVERS_COLUMN_USER_ID = "user_id";
	public static final String DRIVERS_COLUMN_TEAM_ID = "team_id";
	public static final String DRIVERS_COLUMN_FIRST_NAME = "first_name";
	public static final String DRIVERS_COLUMN_LAST_NAME = "last_name";
	
	public static final String TABLE_CHECKED_IN = "checked_in";
	public static final String CHECKED_IN_COLUMN_ID = "_id";
	public static final String CHECKED_IN_COLUMN_DRIVER_ID = "driver_id";
	public static final String CHECKED_IN_COLUMN_BRIEFING_ID = "briefing_id";
	public static final String CHECKED_IN_COLUMN_TIMESTAMP = "timestamp";
	
	public static final String TABLE_CHECKED_OUT = "checked_out";
	public static final String CHECKED_OUT_COLUMN_ID = "_id";
	public static final String CHECKED_OUT_COLUMN_DRIVER_ID = "driver_id";
	public static final String CHECKED_OUT_COLUMN_BRIEFING_ID = "briefing_id";
	public static final String CHECKED_OUT_COLUMN_TIMESTAMP = "timestamp";
	
	public static final String TABLE_DRIVEN_RUNS = "driven_runs";
	public static final String DRIVEN_RUNS_COLUMN_ID = "_id";
	public static final String DRIVEN_RUNS_COLUMN_DRIVER_ID = "driver_id";
	public static final String DRIVEN_RUNS_COLUMN_RACE_DISCIPLINE_ID = "race_discipline_id";
	public static final String DRIVEN_RUNS_COLUMN_VALID = "valid";
	public static final String DRIVEN_RUNS_COLUMN_DATE = "date";
	public static final String DRIVEN_RUNS_COLUMN_TIME = "time";
	
	public static final String TABLE_REGISTERED_TAGS = "tags";
	public static final String REGISTERED_TAGS_COLUMN_ID = "_id";
	public static final String REGISTERED_TAGS_COLUMN_TAG_ID = "tag_id";
	
	public static final String TABLE_BLACKLISTED_TAGS = "blTags";
	public static final String BLACKLISTED_TAGS_COLUMN_ID = "_id";
	public static final String BLACKLISTED_TAGS_COLUMN_TAG_ID = "tag_id";
	
	public static final String TABLE_BLACKLISTED_DEVICES = "blDevices";
	public static final String BLACKLISTED_DEVICES_COLUMN_TAG_ID = "tag_id";
	public static final String BLACKLISTED_DEVICES_COLUMN_TIMESTAMP = "timestamp";
	
	public static final String TABLE_VALUES = "keyvalues";
	public static final String VALUES_COLUMN_ID = "_id";
	public static final String VALUES_COLUMN_KEY = "key";
	public static final String VALUES_COLUMN_VALUE = "value";

	public static final String DATABASE_NAME = "fsg.db";
	public static final int DATABASE_VERSION = 50;

	public static final String TABLE_CLASSES_CREATE = "CREATE TABLE " + TABLE_CLASSES + " (" 
			+ CLASSES_COLUMN_CLASS + " TEXT NOT NULL,"
			+ CLASSES_COLUMN_EVENT_ID + " INTEGER PRIMARY KEY NOT NULL);";

	public static final String TABLE_TEAMS_CREATE = "CREATE TABLE " + TABLE_TEAMS + " (" 
			+ TEAMS_COLUMN_TEAM_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE,"
			+ TEAMS_COLUMN_CN + " TEXT NOT NULL," 
			+ TEAMS_COLUMN_CN_SHORT_EN + " TEXT NOT NULL,"
			+ TEAMS_COLUMN_CITY + " TEXT NOT NULL,"
			+ TEAMS_COLUMN_U + " TEXT NOT NULL,"
			+ TEAMS_COLUMN_CAR + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_PIT + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_ISWAITING + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_EVENT_CLASS_ID + " INTEGER NOT NULL,"
			+ TEAMS_COLUMN_NAME_PITS + " TEXT NOT NULL);";

	public static final String TABLE_DRIVERS_CREATE = "CREATE TABLE " + TABLE_DRIVERS + " ("
			+ DRIVERS_COLUMN_USER_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE,"
			+ DRIVERS_COLUMN_TEAM_ID + " INTEGER NOT NULL,"
			+ DRIVERS_COLUMN_FIRST_NAME + " TEXT NOT NULL,"
			+ DRIVERS_COLUMN_LAST_NAME + " TEXT NOT NULL)";
	
	public static final String TABLE_CHECKED_IN_CREATE = "CREATE TABLE " + TABLE_CHECKED_IN + " ("
			+ CHECKED_IN_COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ CHECKED_IN_COLUMN_DRIVER_ID + " INTEGER NOT NULL, "
			+ CHECKED_IN_COLUMN_BRIEFING_ID + " INTEGER NOT NULL, "
			+ CHECKED_IN_COLUMN_TIMESTAMP + " TEXT NOT NULL);";
	
	public static final String TABLE_CHECKED_OUT_CREATE = "CREATE TABLE " + TABLE_CHECKED_OUT + " ("
			+ CHECKED_OUT_COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ CHECKED_OUT_COLUMN_DRIVER_ID + " INTEGER NOT NULL, "
			+ CHECKED_OUT_COLUMN_BRIEFING_ID + " INTEGER NOT NULL, "
			+ CHECKED_OUT_COLUMN_TIMESTAMP + " TEXT NOT NULL);";
	
	public static final String TABLE_DRIVEN_RUNS_CREATE = "CREATE TABLE " + TABLE_DRIVEN_RUNS + " ("
			+ DRIVEN_RUNS_COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ DRIVEN_RUNS_COLUMN_DRIVER_ID + " INTEGER NOT NULL, "
			+ DRIVEN_RUNS_COLUMN_RACE_DISCIPLINE_ID + " INTEGER NOT NULL, "
			+ DRIVEN_RUNS_COLUMN_VALID + " INTEGER NOT NULL, "
			+ DRIVEN_RUNS_COLUMN_DATE + " TEXT NOT NULL, "
			+ DRIVEN_RUNS_COLUMN_TIME + " TEXT NOT NULL);";
	
	public static final String TABLE_REGISTERED_TAGS_CREATE = "CREATE TABLE " + TABLE_REGISTERED_TAGS + " ("
			+ REGISTERED_TAGS_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE, "
			+ REGISTERED_TAGS_COLUMN_TAG_ID + " TEXT NOT NULL);";
	
	public static final String TABLE_BLACKLISTED_TAGS_CREATE = "CREATE TABLE " + TABLE_BLACKLISTED_TAGS + " ("
			+ BLACKLISTED_TAGS_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE, "
			+ BLACKLISTED_TAGS_COLUMN_TAG_ID + " TEXT NOT NULL);";
	
	public static final String TABLE_BLACKLISTED_DEVICES_CREATE = "CREATE TABLE " + TABLE_BLACKLISTED_DEVICES + " ("
			+ BLACKLISTED_DEVICES_COLUMN_TAG_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE,"
			+ BLACKLISTED_DEVICES_COLUMN_TIMESTAMP + " TEXT NOT NULL);";
	
	public static final String TABLE_VALUES_CREATE = "CREATE TABLE " + TABLE_VALUES + " ("
			+ VALUES_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE,"
			+ VALUES_COLUMN_KEY + " TEXT NOT NULL,"
			+ VALUES_COLUMN_VALUE + " TEXT NOT NULL);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CLASSES_CREATE);
		database.execSQL(TABLE_DRIVERS_CREATE);
		database.execSQL(TABLE_TEAMS_CREATE);
		database.execSQL(TABLE_BLACKLISTED_TAGS_CREATE);
		database.execSQL(TABLE_BLACKLISTED_DEVICES_CREATE);
		database.execSQL(TABLE_CHECKED_IN_CREATE);
		database.execSQL(TABLE_CHECKED_OUT_CREATE);
		database.execSQL(TABLE_DRIVEN_RUNS_CREATE);
		database.execSQL(TABLE_VALUES_CREATE);
		database.execSQL(TABLE_REGISTERED_TAGS_CREATE);
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKED_IN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKED_OUT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVEN_RUNS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALUES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTERED_TAGS);
		onCreate(db);
	}

}

