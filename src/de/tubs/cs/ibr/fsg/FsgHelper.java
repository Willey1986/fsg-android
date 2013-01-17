package de.tubs.cs.ibr.fsg;

import java.text.SimpleDateFormat;
import java.util.*;

public class FsgHelper {

	public static short generateIdForTodaysBriefing(){
	      SimpleDateFormat dateFormat = new SimpleDateFormat("1MMdd", Locale.GERMANY); // Die 1 ist da, damit die führende Null nicht verschwindet.
	      Date today  = new Date();
	      String todayString = dateFormat.format(today);
	      short id =  Short.valueOf(todayString);
	      return id;
	   }
	
}
