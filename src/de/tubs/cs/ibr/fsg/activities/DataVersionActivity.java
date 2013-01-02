package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class DataVersionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_version);
		
		SharedPreferences prefs = this.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_PRIVATE);
		
		TextView driversVesionTextView = (TextView) findViewById(R.id.textViewDrivers);
		int version = prefs.getInt("version_drivers", 0);
		driversVesionTextView.setText( String.valueOf(version) );
		
		TextView teamsVesionTextView = (TextView) findViewById(R.id.textViewTeams);
		version = prefs.getInt("version_teams", 0);
		teamsVesionTextView.setText( String.valueOf(version) );
		
		TextView blacklistWristletsVesionTextView = (TextView) findViewById(R.id.textViewBlacklistWristlets);
		version = prefs.getInt("version_black_wristlets", 0);
		blacklistWristletsVesionTextView.setText( String.valueOf(version) );
		
		TextView blacklistDevicesTextView = (TextView) findViewById(R.id.textViewBlacklistDevices);
		version = prefs.getInt("version_black_devices", 0);
		blacklistDevicesTextView.setText( String.valueOf(version) );
		
		TextView driverPicsTextView = (TextView) findViewById(R.id.textViewDriverPics);
		version = prefs.getInt("version_driver_pics", 0);
		driverPicsTextView.setText(String.valueOf(version)  );
		
	}
	
}
