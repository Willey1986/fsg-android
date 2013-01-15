package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.SecurityManager;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.views.DriverView;
import android.app.Activity;
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
	TextView txtSelectedTeam;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dba.open();
		setContentView(R.layout.activity_registration_driver_selection);
		
		Bundle extras = getIntent().getExtras();				//Von der TeamSelection �bergebene Variablen abfragen
		teamName = extras.getString("teamName");
		teamId = extras.getShort("teamID");
		llDriversList = (LinearLayout) findViewById(R.id.llDriversList);
		txtSelectedTeam = (TextView) findViewById(R.id.txtSelectedTeam);
		txtSelectedTeam.setText(" "+teamName);
		
		ArrayList<Driver> drivers = dba.getAllDriversByTeamID(teamId);
		
		if(drivers.size()>0) {
			for(int i = 0; i <drivers.size(); i++) {
				final Driver driver = drivers.get(i);
				DriverView driverPanel = new DriverView(this,driver);
				driverPanel.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						Intent intent = new Intent(getBaseContext(), RegistrationWriteToTagActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("driver", driver);
						intent.putExtra("bundle", bundle);
						startActivity(intent);
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
