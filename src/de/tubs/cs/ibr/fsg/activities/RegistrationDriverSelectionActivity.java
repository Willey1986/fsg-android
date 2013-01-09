package de.tubs.cs.ibr.fsg.activities;


import java.io.IOException;
import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.SecurityManager;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import de.tubs.cs.ibr.fsg.views.DriverView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Beinhaltet den Ablauf der Fahrerauswahl und initalisiert die Speicherung der Initialisierungsdaten auf dem Armband
 * @author Willem Almstedt
 *
 */
public class RegistrationDriverSelectionActivity extends Activity{
	
	DBAdapter dba = new DBAdapter(this);
	SecurityManager scm = new SecurityManager("geheim");
	String teamName;
	short teamId;
	LinearLayout llDriversList;
	AlertDialog.Builder dialog;
	TextView txtSelectedTeam;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dba.open();
		setContentView(R.layout.activity_registration_driver_selection);
		
		Bundle extras = getIntent().getExtras();				//Von der TeamSelection �bergebene Variablen abfragen
		teamName = extras.getString("teamName");
		teamId = extras.getShort("teamID");
		llDriversList = (LinearLayout) findViewById(R.id.llDriversList);
		dialog = new AlertDialog.Builder(this);
		txtSelectedTeam = (TextView) findViewById(R.id.txtSelectedTeam);
		txtSelectedTeam.setText(" "+teamName);
		
		ArrayList<Driver> drivers = dba.getAllDriversByTeamID(teamId);
		
		if(drivers.size()>0) {
			for(int i = 0; i <drivers.size(); i++) {
				final Driver driver = drivers.get(i);
				DriverView driverPanel = new DriverView(this,driver);
				driverPanel.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						try {
							byte[][] encodedDriver = NfcData.generateDataRegistration(driver);
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
							
							
							/*
							byte[][] decryptedDriver = scm.decryptString(encryptedDriver);
							StringBuffer decryptedString = new StringBuffer();
							for(int i = 0; i < decryptedDriver.length; i++) {
								for(int j = 0; j < decryptedDriver[i].length; j++) {
									decryptedString.append(decryptedDriver[i][j]);
								}
							}
							*/
							
							Intent intent = new Intent(getBaseContext(), RegistrationWriteToTagActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("driver", driver);
							intent.putExtra("bundle", bundle);
							startActivity(intent);
							
							
						} catch (IOException e) {
						Intent mIntent = new Intent(RegistrationDriverSelectionActivity.this, ErrorActivity.class);
							mIntent.putExtra("Exception", e);
							startActivity(mIntent);
							finish();
						} catch (FsgException e) {
							Intent mIntent = new Intent(RegistrationDriverSelectionActivity.this, ErrorActivity.class);
							mIntent.putExtra("Exception", e);
							startActivity(mIntent);
							finish();
						} 
					}
				});
				llDriversList.addView(driverPanel);
			}
		}
		
	}
	
	protected void onStop() {
		super.onStop();
		dba.close();
	}
	
}
