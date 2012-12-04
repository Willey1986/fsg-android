package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.db.models.Team;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DriverRegistrationActivity extends Activity {
	
	TableLayout regTable;
	
	DBAdapter dba = new DBAdapter(this);
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_registration);
		regTable = (TableLayout) findViewById(R.id.regTable);
		ArrayList<Driver> drivers = dba.getAllDrivers();
		System.out.println(drivers.toString());
		for (int i = 0; i < drivers.size(); i++) {
			Driver driver = drivers.get(i);
			TableRow row = new TableRow(this);
			TextView id = new TextView(this);
			TextView teamid = new TextView(this);
			TextView firstname = new TextView(this);
			TextView lastname = new TextView(this);
			id.setText(""+driver.getUser_id());
			teamid.setText(""+driver.getTeam_id());
			firstname.setText(driver.getFirst_name());
			lastname.setText(driver.getLast_name());
			row.addView(id);
			row.addView(teamid);
			row.addView(firstname);
			row.addView(lastname);
			regTable.addView(row);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_driver_registration, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuItemRefresh:
				refreshDB();
				return true;
		}
		return false;
	}
	
	public void refreshDB() {
		Driver driver = new Driver((short) 12312, (short) 12312, "Horst", "Fuchs", (short) 0);
		Team team = new Team((short) 1, "Test", "Test Team", "BS", "TU",(short) 12, (short) 14, (short) 0, (short) 1, "TU Racing");
		dba.writeDriverToDB(driver);	
		dba.writeTeamToDB(team);
	}
	
}
