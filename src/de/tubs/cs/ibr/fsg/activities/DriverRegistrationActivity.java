package de.tubs.cs.ibr.fsg.activities;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.tasks.ReadFromURLTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;

public class DriverRegistrationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_registration);
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
			String stringDrivers = new ReadFromURLTask().execute("http://www.ibr.cs.tu-bs.de/users/poettner/fsg/drivers.php").get();
			String stringTeams = new ReadFromURLTask().execute("http://www.ibr.cs.tu-bs.de/users/poettner/fsg/teams.php").get();
			String stringGeneral = new ReadFromURLTask().execute("http://www.ibr.cs.tu-bs.de/users/poettner/fsg/general.php").get();
			
			JSONArray jsonDrivers = new JSONArray(stringDrivers);
			JSONArray jsonTeams = new JSONArray(stringTeams);
			JSONObject jsonGeneral = new JSONObject(stringGeneral);
			JSONArray jsonEvents = jsonGeneral.getJSONArray("Classes");
			
			for (int i = 0; i <= jsonDrivers.length(); i++) {
				Driver driver = new Driver(jsonDrivers.getJSONObject(i));
				System.out.println(driver.toString());
			}
			
			
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
}
