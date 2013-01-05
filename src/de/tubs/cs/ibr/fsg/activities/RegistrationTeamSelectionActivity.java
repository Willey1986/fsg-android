/**
 * Beinhaltet sämtliche Logik zur Auswahl des Teams bei der Registrierung
 */

package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.DBHelper;
import de.tubs.cs.ibr.fsg.db.models.Team;
import de.tubs.cs.ibr.fsg.dtn.DTNService;
import de.tubs.cs.ibr.fsg.dtn.FsgProtocol;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class RegistrationTeamSelectionActivity extends Activity {
	
	TableLayout regTable;
	DBAdapter dba = new DBAdapter(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_team_selection);
		regTable = (TableLayout) findViewById(R.id.regTable);
		dba.open();
		ArrayList<Team> teams = dba.getAllTeams();
		for (int i = 0; i < teams.size(); i++) {					//Bef�llt die Tabelle der Activity mit Inhalten (Teams) aus der DB
			final Team team = teams.get(i);							//TODO: Erstellen eines eigenen Widgets das Team-Objekte empf�ngt und darstellt
			TableRow row = new TableRow(this);
			TextView teamId = new TextView(this);
			TextView teamName = new TextView(this);
			TextView teamCountry = new TextView(this);
			TextView teamCity = new TextView(this);
			TextView teamUniversity = new TextView(this);
			teamId.setText(""+team.getTeamId());
			teamName.setText(team.getName_pits());
			teamCountry.setText(team.getCn_short_en());
			teamCity.setText(team.getCity());
			teamUniversity.setText(team.getUniversity());
			teamId.setTextColor(getResources().getColor(R.color.dark_red));
			teamName.setTextColor(getResources().getColor(R.color.black));
			teamCountry.setTextColor(getResources().getColor(R.color.dark_red));
			teamCity.setTextColor(getResources().getColor(R.color.black));
			teamUniversity.setTextColor(getResources().getColor(R.color.dark_red));
			teamName.setPadding(10, 0, 0, 0);
			teamCountry.setPadding(20, 0, 0, 0);
			teamCity.setPadding(20, 0, 0, 0);
			teamUniversity.setPadding(20, 0, 0, 0);
			row.addView(teamId);
			row.addView(teamName);
			row.addView(teamCountry);
			row.addView(teamCity);
			row.addView(teamUniversity);
			row.setBackgroundResource(R.drawable.tablerow_gradient_light);
			row.setPadding(10, 22, 8, 8);
			row.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Intent intent = new Intent(getBaseContext(), RegistrationDriverSelectionActivity.class);
					intent.putExtra("teamID", team.getTeamId());
					intent.putExtra("teamName", team.getName_pits());
					startActivity(intent);
				}
			});
			row.setMinimumHeight(70);
			regTable.addView(row);
		}
	}
	

	protected void onStop() {
		super.onStop();
		dba.close();
	}

	
	/**
	 * Erstellt das Optionsmenü der Registrierungs-Activity
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registration, menu);
	    return true;
	}
	
	
	/**
	 * Weist den Einträgen im Optionsmenü die jeweiligen Funktionen zu
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuItemRefresh:
				refreshDB();
				return true;
			case R.id.miRegInsertDummyData:
				writeSampleDataToDb();
				return true;
			case R.id.showDataVersion:
				startActivity(new Intent(this, DataVersionActivity.class));
				return true;
			case R.id.requestDriverPics:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_DRIVER_PICS);
				return true;
			case R.id.requestDrivers:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_DRIVERS);
				return true;
			case R.id.requestTeams:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_TEAMS);
				return true;
			case R.id.requestBlackWristlets:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_BLACK_WRISTLETS);
				return true;
			case R.id.requestBlackDevices:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_BLACK_DEVICES);
				return true;
			case R.id.requestDriversTeams:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_DRIVERS_TEAMS);
				return true;
			case R.id.requestDriversTeamsDriverPics:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_DRIVERS_TEAMS_DP);
				return true;
			case R.id.requestBothBlack:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_BOTH_BLACK);
				return true;
			case R.id.requestDriverTeamsBothBlack:
				requestRegistrationUpdate(FsgProtocol.UP_REQ_DRIV_TE_BLWR_BLDE);
				return true;
		}
		return false;
	}
	
	

	/**
	 * Fragt aktuelle Daten über das Netz an und schreibt sie in die Datenbank
	 */

	private void requestRegistrationUpdate(int requestType) {
		Intent mIntent = new Intent(this, DTNService.class);
		mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.SEND_DATA);
		mIntent.putExtra("destination", "dtn://mulita-fsg.dtn/fsg"  );
		mIntent.putExtra("type",        String.valueOf(requestType) );
		mIntent.putExtra("version",     "0");
		mIntent.putExtra("payload",     "nichts");
		startService(mIntent);
	}
	


	public void refreshDB() {
		//TODO
	}
	
	
	/**
	 * Schreibt Beispieldaten (13 Fahrer, 2 Teams) in die Datenbank
	 */
	public void writeSampleDataToDb() {
		String deleteAllDrivers = "DELETE FROM " + DBHelper.TABLE_DRIVERS + ";";
		String deleteAllTeams = "DELETE FROM " + DBHelper.TABLE_TEAMS + ";";
		
		dba.execSQL(deleteAllDrivers);
		dba.execSQL(deleteAllTeams);
		
		String writeDriver1 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(1,20,'Harald','Juhnke');";
		String writeDriver2 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(81,20,'Stefan','Raab');";
		String writeDriver3 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(82,20,'Verona','Pooth');";
		String writeDriver4 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(83,21,'Claudia','Roth');";
		String writeDriver5 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(84,21,'Ralf','Schumacher');";
		String writeDriver6 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(85,21,'Sebastian','Vettel');";
		String writeDriver7 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(86,21,'Lewis','Hamilton');";
		String writeDriver8 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(87,21,'Fernando','Alonso');";
		String writeDriver9 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(88,21,'Nikki','Lauda');";
		String writeDriver10 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(89,21,'Peter','Lustig');";
		String writeDriver11 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(90,21,'Hein','Blöd');";
		String writeDriver12 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(91,21,'Frauke','Ludowig');";
		String writeDriver13 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(92,21,'Hans-Bernd-Sebastian-Ludwig-Martin-Hans','Meier von und zu Hohenzollern');";
		
		String writeTeam1 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_TEAMS + " VALUES(20,'DE','Germany','Braunschweig','TU',11,3,0,1,'Lions Racing Team');";
		String writeTeam2 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_TEAMS + " VALUES(21,'E','Spain','Barcelona','U',20,4,0,1,'Fernando Alonso Racing');";
		
		dba.execSQL(writeDriver1);
		dba.execSQL(writeDriver2);
		dba.execSQL(writeDriver3);
		dba.execSQL(writeDriver4);
		dba.execSQL(writeDriver5);
		dba.execSQL(writeDriver6);
		dba.execSQL(writeDriver7);
		dba.execSQL(writeDriver8);
		dba.execSQL(writeDriver9);
		dba.execSQL(writeDriver10);
		dba.execSQL(writeDriver11);
		dba.execSQL(writeDriver12);
		dba.execSQL(writeDriver13);
		
		dba.execSQL(writeTeam1);
		dba.execSQL(writeTeam2);
		
		try {
			dba.writeCheckIn((short)13, (short)12);
		} catch (FsgException e) {
			// TODO Auto-generated catch block
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		}
	}
	
	
}