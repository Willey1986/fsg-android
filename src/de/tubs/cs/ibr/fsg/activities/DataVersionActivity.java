package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.R;

public class DataVersionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_version);
		
		SharedPreferences prefs = this.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_MULTI_PROCESS);
		
		TextView driversVesionTextView = (TextView) findViewById(R.id.textViewDriversVersion);
		int version = prefs.getInt("version_drivers", 0);
		driversVesionTextView.setText( String.valueOf(version) );
		
		TextView teamsVesionTextView = (TextView) findViewById(R.id.textViewTeamsVersion);
		version = prefs.getInt("version_teams", 0);
		teamsVesionTextView.setText( String.valueOf(version) );
		
		TextView blacklistTagsVesionTextView = (TextView) findViewById(R.id.textViewBlacklistTagsVersion);
		version = prefs.getInt("version_black_tags", 0);
		blacklistTagsVesionTextView.setText( String.valueOf(version) );
		
		TextView blacklistDevicesTextView = (TextView) findViewById(R.id.textViewBlacklistDevicesVersion);
		version = prefs.getInt("version_black_devices", 0);
		blacklistDevicesTextView.setText( String.valueOf(version) );
		
		TextView driverPicsTextView = (TextView) findViewById(R.id.textViewDriverPicsVersion);
		version = prefs.getInt("version_driver_pics", 0);
		driverPicsTextView.setText(String.valueOf(version)  );
		
	}
	
    public void onButtonClick(View view){
    	onBackPressed();
    }
	
}
