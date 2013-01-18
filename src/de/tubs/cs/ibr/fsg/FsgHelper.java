package de.tubs.cs.ibr.fsg;

import java.text.SimpleDateFormat;
import java.util.*;

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
	
}
