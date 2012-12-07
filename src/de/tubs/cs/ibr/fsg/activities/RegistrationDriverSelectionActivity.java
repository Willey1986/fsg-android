package de.tubs.cs.ibr.fsg.activities;


import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.views.DriverView;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class RegistrationDriverSelectionActivity extends Activity{
	
	TableLayout tblRegDrivers;
	DBAdapter dba;
	String teamName;
	short teamId;
	LinearLayout llDriversList;
	AlertDialog.Builder dialog;
	TextView txtSelectedTeam;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_driver_selection);
		
		Bundle extras = getIntent().getExtras();
		teamName = extras.getString("teamName");
		teamId = extras.getShort("teamID");
		llDriversList = (LinearLayout) findViewById(R.id.llDriversList);
		dialog = new AlertDialog.Builder(this);
		txtSelectedTeam = (TextView) findViewById(R.id.txtSelectedTeam);
		txtSelectedTeam.setText(" "+teamName);
		
		dba = new DBAdapter(this);
		ArrayList<Driver> drivers = dba.getAllDriversByTeamID(teamId);
		
		if(drivers.size()>0) {
			for(int i = 0; i <drivers.size(); i++) {
				final Driver driver = drivers.get(i);
				DriverView driverPanel = new DriverView(this,driver);
				driverPanel.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
							dialog.setMessage("Fahrer " + driver.getFirst_name() + " " + driver.getLast_name() + " ausgewŠhlt\n\nIm nŠchsten Schritt erfolgt die Codierung");
							dialog.show();
							//TODO driver Codieren um zu verschlŸsseln und aufs Band zu schreiben
					}
				});
				llDriversList.addView(driverPanel);
			}
		}
		
	}
	
}
