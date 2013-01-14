package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.dtn.DTNService;
import de.tubs.cs.ibr.fsg.dtn.UpdateRequest;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class UpdateRequestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_request);
		
		
	}
	
    public void onRequestButtonClick(View view){
    	CheckBox teamsCheckbox            = (CheckBox) findViewById(R.id.checkBoxTeams);
    	CheckBox driversCheckbox          = (CheckBox) findViewById(R.id.checkBoxDrivers);
    	CheckBox driverPicsCheckbox       = (CheckBox) findViewById(R.id.checkBoxDriverPics);
    	CheckBox blacklistTagsCheckbox    = (CheckBox) findViewById(R.id.checkBoxBlacklistTags);
    	CheckBox blacklistDevicesCheckbox = (CheckBox) findViewById(R.id.checkBoxBlackDevice);
    	
    	
    	if (!driversCheckbox.isChecked() && !teamsCheckbox.isChecked() &&
    			!blacklistTagsCheckbox.isChecked() && !blacklistDevicesCheckbox.isChecked() && !driverPicsCheckbox.isChecked() ){
    		
    		LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    		View layout = inflater.inflate(R.layout.toast_layout, null);
    		TextView text = (TextView) layout.findViewById(R.id.text);
           	text.setText(R.string.error_empy_choice);
    		Toast toast = new Toast(getApplicationContext());
    		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    		toast.setDuration(Toast.LENGTH_LONG);
    		toast.setView(layout);
    		toast.show();
    	}else{
        	UpdateRequest mRequest = new UpdateRequest(driversCheckbox.isChecked(), teamsCheckbox.isChecked(),
        			blacklistTagsCheckbox.isChecked(), blacklistDevicesCheckbox.isChecked(), driverPicsCheckbox.isChecked() );
        	
        	requestRegistrationUpdate(mRequest);
        	onBackPressed();
    	}
    	

    }
	
    public void onCancelButtonClick(View view){
    	onBackPressed();
    }


	/**
	 * Fragt aktuelle Daten über das Netz an und schreibt sie in die Datenbank
	 */

	private void requestRegistrationUpdate(UpdateRequest mRequest) {
		Intent mIntent = new Intent(this, DTNService.class);
		mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.SEND_DATA);
		mIntent.putExtra("destination", "dtn://fsg-backend.dtn/fsg");
		mIntent.putExtra("type", "1");
		mIntent.putExtra("request", mRequest);
		mIntent.putExtra("version", "0");
		mIntent.putExtra("payload", "nichts");
		startService(mIntent);
	}

}