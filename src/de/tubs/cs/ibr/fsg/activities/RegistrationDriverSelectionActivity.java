package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RegistrationDriverSelectionActivity extends Activity{
	
	TableLayout tblRegDrivers;
	DBAdapter dba;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_driver_selection);
		
		tblRegDrivers = (TableLayout) findViewById(R.id.tblRegDrivers);
		dba = new DBAdapter(this);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			TextView test = (TextView) findViewById(R.id.txtSelectedTeamID);
			test.setText("Ausgewähltes Team: " + extras.getString("teamName"));
			int teamId = extras.getInt("teamID");
			ArrayList<Driver> drivers = dba.getAllDriversByTeamID(teamId);
			System.out.println("**********************************" + drivers.size());
			if (drivers.size()>0) {
				for(int i=0; i<drivers.size(); i++) {
					Driver driver = drivers.get(i);
					TableRow row = new TableRow(this);
					TextView first_name = new TextView(this);
					TextView last_name = new TextView(this);
					first_name.setText(driver.getFirst_name());
					last_name.setText(driver.getLast_name());
					row.addView(first_name);
					row.addView(last_name);
					tblRegDrivers.addView(row);
				}
			
			}
		}
		
		
		
		
	}
	
}
