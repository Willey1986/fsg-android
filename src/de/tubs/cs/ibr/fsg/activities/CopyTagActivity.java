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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CopyTagActivity extends NfcEnabledActivity {
	
	private DBAdapter dba;
	private Nfc nfc;
	private short step = 0;
	private NfcObject theData = null;
	private short driverID;
	private short moveon = 0;
	private String tagID = "";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_invalidate_tag);
		dba = new DBAdapter(this);
		nfc = new Nfc(this);
		step = 1;
		
		dba.open();
	}

	@Override
	public void executeNfcAction(Intent intent) {
	if(step==1){
			
		try {
			System.out.println("Auslesen go!");
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
				if (true){//!database.isTagBlacklisted(tagID)){//Tag geblacklistet?
					if (database.getDriver(driverID) != null){ //Wenn der Fahrer in der DB existiert
						((TextView) findViewById(R.id.textView1)).setText("neues Band an Gerät halten");
						((TextView) findViewById(R.id.textView2)).setText("neues Band beschreiben");
						step = 2;
						System.out.println("Auslesen ende!");
					} else {
						System.out.println("Fahrer existiert nicht!");
					}
				}else {
					System.out.println("Tag blacklisted!");
				}
			}	
		//DONE
		} catch (FsgException e) {
			e.printStackTrace();
		}	
	}
		
			
			//TODO: Hier "Neue Karte ranhalten" auf Bildschirm anzeigen
			/*
				new AlertDialog.Builder (this);
       	 		LayoutInflater factory = LayoutInflater.from(this);
       	 		final View textEntryView = factory.inflate(R.layout.dialog_login, null);
       	 		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
       	 		alert.setTitle("Test");  
       	 		alert.setMessage("Test :");                
       	 		alert.setView(textEntryView);
       	 	
       	 		final Activity obj = this;
       	 	
       	 		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
       	 			public void onClick(DialogInterface dialog, int whichButton) {  
       	 				EditText mUserText;
       	 				mUserText = (EditText) textEntryView.findViewById(R.id.txt_password);
       	 			}
       	 		}); 
       	 		
       	 	alert.show();
*/
				
	else if(step==2){		
			//löschen
				try{
					//nfc.cleanTag(intent);
					nfc.initializeTag(intent);
					System.out.println("card cleaned");
				} catch (FsgException e) {
					
				}
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
					
					
					/*StringBuffer encodedString = new StringBuffer();
					
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
					}*/
					
					//byte[][] contentToWrite = encodedDriver; 
					nfc.writeTag(intent, encodedDriver);
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
						/*
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
						}*/
						
						//byte[][] contentToWrite = encodedBriefing; 
						nfc.writeTag(intent, encodedBriefing);
					
					} catch (FsgException e) {
						Intent mIntent = new Intent(this, ErrorActivity.class);
						mIntent.putExtra("Exception", e);
						startActivity(mIntent);
						finish();
					}
				 }
				
				//RUNS 
				//ACCELERATION
				for(int m=0;m<theData.getAccelerationRuns();m++){
					try {
						//kompliziertes auslesen des Timestamps und konvertierung
						byte[][] encodedBriefing = NfcData.generateRun((short)1, NfcData.makeBetterTimestampNOW());
						//TODO: Timestamp aus alten RUNS verwenden und keinen neuen anlegen
						nfc.writeTag(intent, encodedBriefing);					
					} catch (FsgException e) {
						Intent mIntent = new Intent(this, ErrorActivity.class);
						mIntent.putExtra("Exception", e);
						startActivity(mIntent);
						finish();
					}
				 }
				
				//SKID_PAD
				for(int m=0;m<theData.getAccelerationRuns();m++){
					try {
						//kompliziertes auslesen des Timestamps und konvertierung
						byte[][] encodedBriefing = NfcData.generateRun((short)2, NfcData.makeBetterTimestampNOW());
						//TODO: Timestamp aus alten RUNS verwenden und keinen neuen anlegen
						nfc.writeTag(intent, encodedBriefing);					
					} catch (FsgException e) {
						Intent mIntent = new Intent(this, ErrorActivity.class);
						mIntent.putExtra("Exception", e);
						startActivity(mIntent);
						finish();
					}
				 }
				 
				//AUTOCROSS
				for(int m=0;m<theData.getAccelerationRuns();m++){
					try {
						//kompliziertes auslesen des Timestamps und konvertierung
						byte[][] encodedBriefing = NfcData.generateRun((short)3, NfcData.makeBetterTimestampNOW());
						//TODO: Timestamp aus alten RUNS verwenden und keinen neuen anlegen
						nfc.writeTag(intent, encodedBriefing);					
					} catch (FsgException e) {
						Intent mIntent = new Intent(this, ErrorActivity.class);
						mIntent.putExtra("Exception", e);
						startActivity(mIntent);
						finish();
					}
				 }
				
				//ENDURANCE
				for(int m=0;m<theData.getAccelerationRuns();m++){
					try {
						//kompliziertes auslesen des Timestamps und konvertierung
						byte[][] encodedBriefing = NfcData.generateRun((short)4, NfcData.makeBetterTimestampNOW());
						//TODO: Timestamp aus alten RUNS verwenden und keinen neuen anlegen
						nfc.writeTag(intent, encodedBriefing);					
					} catch (FsgException e) {
						Intent mIntent = new Intent(this, ErrorActivity.class);
						mIntent.putExtra("Exception", e);
						startActivity(mIntent);
						finish();
					}
				 }
				
				
				//ENDE				
				((TextView) findViewById(R.id.textView1)).setText("Auslesen erledigt");
				((TextView) findViewById(R.id.textView2)).setText("Daten wurden übertragen");
			}		
		
	}

}
