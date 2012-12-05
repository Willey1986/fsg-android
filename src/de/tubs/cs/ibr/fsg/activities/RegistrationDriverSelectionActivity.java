package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RegistrationDriverSelectionActivity extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_driver_selection);
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			TextView test = (TextView) findViewById(R.id.txtSelectedTeamID);
			test.setText("Ausgewähltes Team: " + extras.getString("test"));
		}
	}
	
}
