package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
		System.out.println("Geklick0rt");
	}

	
}
