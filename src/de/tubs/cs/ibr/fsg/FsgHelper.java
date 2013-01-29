package de.tubs.cs.ibr.fsg;

import java.text.SimpleDateFormat;
import java.util.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import de.tubs.cs.ibr.fsg.dtn.DTNService;
import de.tubs.cs.ibr.fsg.dtn.UpdateRequest;

public class FsgHelper {
	
	public static final short RUN_DISCIPLINE_ACCELERATION = 1;
	public static final short RUN_DISCIPLINE_SKID_PAD     = 2;
	public static final short RUN_DISCIPLINE_AUTOCROSS    = 3;
	public static final short RUN_DISCIPLINE_ENDURANCE    = 4;

	public static short generateIdForTodaysBriefing(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("1MMdd", Locale.GERMANY); // Die 1 ist da, damit die führende Null nicht verschwindet.
		Date today  = new Date();
		String todayString = dateFormat.format(today);
		short id =  Short.valueOf(todayString);
		return id;
	}
	
	public static long generateUNIXTimestamp() {
		Date date = new Date();
		return (date.getTime() / 1000L);
	}
	
	
	/**
	 * Mit Hilfe dieser Methode koennen wir per IBR-DTN den jeweils letzten Stand
	 * aller gelesenen Baender an das Backend verschicken. 
	 * 
	 * @param mContext Die Activity, die diese Funktion aufgerufen hat.
	 * @param JSONData Die Daten aller Tags in JSON-Format.
	 */
	public static void sendLastDatasetOfAllTags(Context mContext, String JSONData){
		SharedPreferences prefs = mContext.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_MULTI_PROCESS);
        String serverAddress = prefs.getString("server_address", "dtn://fsg-backend.dtn/fsg");
        
		UpdateRequest mRequest = new UpdateRequest();
		Intent mIntent = new Intent(mContext, DTNService.class);
		mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.SEND_DATA);
		mIntent.putExtra("destination", serverAddress);
		mIntent.putExtra("type", "16");
		mIntent.putExtra("request", mRequest);
		mIntent.putExtra("version", "0");
		mIntent.putExtra("payload", JSONData);
		mContext.startService(mIntent);
	}
	
	
	/**
	 * Mit Hilfe dieser Methode koennen wir per IBR-DTN den Stand eines Bandes
	 * an das Backend verschicken.
	 * 
	 * @param mContext Die Activity, die diese Funktion aufgerufen hat.
	 * @param JSONData Die Daten des Tags in JSON-Format.
	 */
	public static void sendDatasetOfOneTag(Context mContext, String JSONData){
		SharedPreferences prefs = mContext.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_MULTI_PROCESS);
        String serverAddress = prefs.getString("server_address", "dtn://fsg-backend.dtn/fsg");
        
		UpdateRequest mRequest = new UpdateRequest();
		Intent mIntent = new Intent(mContext, DTNService.class);
		mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.SEND_DATA);
		mIntent.putExtra("destination", serverAddress);
		mIntent.putExtra("type", "15");
		mIntent.putExtra("request", mRequest);
		mIntent.putExtra("version", "0");
		mIntent.putExtra("payload", JSONData);
		mContext.startService(mIntent);
	}
	
	
	public static void deleteTheSharedPreferences(Context mContext){
		SharedPreferences prefs = mContext.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString("admin_password", "kuh");
		editor.putString("server_address", "dtn://fsg-backend.dtn/fsg");
		editor.putInt("version_driver_pics", 0 );
		editor.putInt("version_drivers", 0 );
		editor.putInt("version_teams", 0 );
		editor.putInt("version_black_tags", 0 );
		editor.putInt("version_black_devices", 0 );
		editor.commit();
        

	}
	
	
	
	
	
	
	
	
	
}
