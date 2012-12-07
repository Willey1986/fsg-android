package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.views.DriverView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RegistrationDriverSelectionActivity extends Activity{
	
	TableLayout tblRegDrivers;
	DBAdapter dba;
	String teamName;
	short teamId;
	LinearLayout llDriversList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_driver_selection);
		
		Bundle extras = getIntent().getExtras();
		teamName = extras.getString("teamName");
		teamId = extras.getShort("teamID");
		llDriversList = (LinearLayout) findViewById(R.id.llDriversList);
		
		dba = new DBAdapter(this);
		ArrayList<Driver> drivers = dba.getAllDriversByTeamID(teamId);
		
		if(drivers.size()>0) {
			for(int i = 0; i <drivers.size(); i++) {
				Driver driver = drivers.get(i);
				llDriversList.addView(new DriverView(this, driver));
			}
		}
		
	}
	
}
