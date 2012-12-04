package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.tasks.ReadFromURLTask;
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
		try {
			dba.open();
			String stringDrivers = new ReadFromURLTask().execute("http://www.ibr.cs.tu-bs.de/users/poettner/fsg/drivers.php").get();
			String stringTeams = new ReadFromURLTask().execute("http://www.ibr.cs.tu-bs.de/users/poettner/fsg/teams.php").get();
			String stringGeneral = new ReadFromURLTask().execute("http://www.ibr.cs.tu-bs.de/users/poettner/fsg/general.php").get();
			
			JSONArray jsonDrivers = new JSONArray(stringDrivers);
			JSONArray jsonTeams = new JSONArray(stringTeams);
			JSONObject jsonGeneral = new JSONObject(stringGeneral);
			JSONArray jsonEvents = jsonGeneral.getJSONArray("Classes");
			
			for (int i = 0; i < jsonDrivers.length(); i++) {
				Driver driver = new Driver(jsonDrivers.getJSONObject(i));
				System.out.println(driver.toString());
				dba.writeDriverToDB(driver);
			}
			
			Driver driver = new Driver(0, 123123, 123123, "Horst", "Fuchs", false);
			dba.writeDriverToDB(driver);
			dba.close();
			
			dba.getAllDrivers();
			
			Driver d = dba.getDriver(123123);
			System.out.println(d.toString());
			
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
}
