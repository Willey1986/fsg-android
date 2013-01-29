package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import de.tubs.cs.ibr.fsg.FsgHelper;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final boolean DEVELOPER_MODE = false;

	protected String password;
	private DBAdapter dba;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		dba = new DBAdapter(this);
		dba.open();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.admin:
			chooseAdmin();
			break;
		case R.id.miRegInsertDummyData:
			dba.writeSampleData();
			return true;
		case R.id.showDataVersion:
			startActivity(new Intent(this, DataVersionActivity.class));
			return true;
		case R.id.updateRequest:
			startActivity(new Intent(this, UpdateRequestActivity.class));
			return true;
		case R.id.clearDB:
			dba.clearDB();
			FsgHelper.deleteTheSharedPreferences(this);
			return true;
		}
		return true;
	}

	private void chooseAdmin() {
		new AlertDialog.Builder(this);
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.dialog_login, null);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Login");
		alert.setMessage("Enter Pin :");
		alert.setView(textEntryView);

		final Activity obj = this;

		SharedPreferences prefs = this.getSharedPreferences( "de.tubs.cs.ibr.fsg", Context.MODE_MULTI_PROCESS);
		password = prefs.getString("admin_password", "kuh");
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText mUserText;
				mUserText = (EditText) textEntryView
						.findViewById(R.id.txt_password);
				String strPinCode = mUserText.getText().toString();

				if (strPinCode.equals(password)) {

					// OK Button wurde gedrueckt
					startActivity(new Intent(obj, AdminActivity.class));
				} else {
					// hier ist das Passwort falsch
					Log.d(TAG, "Pin Value : " + strPinCode);

					Log.d(TAG, "Password: " + password);
				}

				return;
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
		alert.show();
	}

	/**
	 * 
	 * @param view
	 */
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			startActivity(new Intent(this,
					RegistrationTeamSelectionActivity.class));
			break;
		case R.id.button2:
			startActivity(new Intent(this, BriefingCheckInActivity.class));
			break;
		case R.id.button3:
			startActivity(new Intent(this, BriefingCheckOutActivity.class));
			break;
		case R.id.button4:
			startActivity(new Intent(this, RunActivity.class));
			break;
		case R.id.button5:

			startActivity(new Intent(this, InfoTerminalPreActivity.class));
			break;
		}
	}

	protected void onStop() {
		super.onStop();
		dba.close();
	}

	protected void onResume() {
		super.onResume();
		dba.open();
	}
}
