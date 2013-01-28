package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;
import java.util.Arrays;

import de.tubs.cs.ibr.fsg.FsgHelper;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.SecurityManager;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class CopyTagActivity extends NfcEnabledActivity {
	
	private DBAdapter dba;
	private Nfc nfc;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_invalidate_tag);
		dba = new DBAdapter(this);
		nfc = new Nfc(this);
		
		dba.open();
	}

	@Override
	public void executeNfcAction(Intent intent) {
		NfcObject theData = null;
		short driverID;
		short moveon = 0;
		String tagID = "";
		
		try {
			//ALTES TAG AUSLESEN
			nfc.readTag(intent);
			byte[][] data = nfc.getData();
			//hats geklappt?
			if (!Arrays.equals(data, null)){
				//Daten interpretieren
				theData = NfcData.interpretData(data);
				//DriverID feststellen
				driverID = theData.getDriverObject().getDriverID();
				tagID = nfc.getTagID();
				//In Datenbank auf Blacklisted prüfen
				DBAdapter database = new DBAdapter(this);
				database.open();
				if (!database.isTagBlacklisted(tagID)){//Tag geblacklistet?
					if (database.getDriver(driverID) != null){ //Wenn der Fahrer in der DB existiert
						moveon = 1;
					} else {
						//TODO: Exception "Fahrer existiert nicht"
					}
				}
			}
				
			if(moveon==1){
			//Blacklisten des alten Bandes anhand der Band-ID DBAdapter.writeBlacklistedTag(int tagId)
				dba.writeBlacklistedTagToDB(tagID);
			//Band als ungültig kennzeichnen: Nfc.invalidateTag()
				//TODO: gibts da eine Funktion für?
				//SONST: NfcData.generateDataDestroyCompleteTag();
			
			// JETZT DATEN AUF NEUES BAND SCHREIBEN
				//Registrierungsdaten
				SecurityManager scm = new SecurityManager("geheim");
				
				try {
					byte[][] encodedDriver = NfcData.generateDataRegistration(theData.getDriverObject());
					StringBuffer encodedString = new StringBuffer();
					for(int i = 0; i < encodedDriver.length; i++) {
						for(int j=0; j<encodedDriver[i].length; j++) {
							encodedString.append(encodedDriver[i][j]);
						}
					}
					
					byte[][] encryptedDriver = scm.encryptString(encodedDriver);
					StringBuffer encryptedString = new StringBuffer();
					for(int i = 0; i < encryptedDriver.length; i++) {
						for(int j = 0; j < encryptedDriver[i].length; j++) {
							encryptedString.append(encryptedDriver[i][j]);
						}
					}
					
					byte[][] contentToWrite = encryptedDriver; 
					nfc.writeTag(intent, contentToWrite);
				} catch (FsgException e) {
					Intent mIntent = new Intent(this, ErrorActivity.class);
					mIntent.putExtra("Exception", e);
					startActivity(mIntent);
					finish();
				} catch (IOException e) {
					Intent mIntent = new Intent(this, ErrorActivity.class);
					mIntent.putExtra("Exception", e);
					startActivity(mIntent);
					finish();
				}
				//Briefings
				//für #briefings die es gibt, lese aus und schreibe auf band
				for(int m=0;m<theData.getBriefings().size();m++){
					try {
						//kompliziertes auslesen des Timestamps und konvertierung
						byte[][] encodedBriefing = NfcData.generateCheckIN(
								theData.getBriefings().get(m).getBriefingID(), 
								NfcData.makeBetterTimestampFrom(theData.getBriefings().get(m).getTimestamp()));
						StringBuffer encodedString = new StringBuffer();
						for(int i = 0; i < encodedBriefing.length; i++) {
							for(int j=0; j<encodedBriefing[i].length; j++) {
								encodedString.append(encodedBriefing[i][j]);
							}
						}
						
						byte[][] encryptedBriefing = scm.encryptString(encodedBriefing);
						StringBuffer encryptedString = new StringBuffer();
						for(int i = 0; i < encryptedBriefing.length; i++) {
							for(int j = 0; j < encryptedBriefing[i].length; j++) {
								encryptedString.append(encryptedBriefing[i][j]);
							}
						}
						
						byte[][] contentToWrite = encryptedBriefing; 
						nfc.writeTag(intent, contentToWrite);
					
					} catch (FsgException e) {
						Intent mIntent = new Intent(this, ErrorActivity.class);
						mIntent.putExtra("Exception", e);
						startActivity(mIntent);
						finish();
					}
				}
				//Runs
					//TODO
			}
			//DONE
		} catch (FsgException e) {
			e.printStackTrace();
		}				
		
	}

}
