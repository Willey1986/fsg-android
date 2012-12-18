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
import de.tubs.cs.ibr.fsg.service.DTNService;

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
			teamId.setTextColor(getResources().getColor(R.color.white));
			teamName.setTextColor(getResources().getColor(R.color.white));
			teamCountry.setTextColor(getResources().getColor(R.color.white));
			teamCity.setTextColor(getResources().getColor(R.color.white));
			teamUniversity.setTextColor(getResources().getColor(R.color.white));
			row.addView(teamId);
			row.addView(teamName);
			row.addView(teamCountry);
			row.addView(teamCity);
			row.addView(teamUniversity);
			row.setBackgroundResource(R.drawable.tablerow_gradient_light);
			row.setPadding(8, 8, 8, 8);
			row.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Intent intent = new Intent(getBaseContext(), RegistrationDriverSelectionActivity.class);
					intent.putExtra("teamID", team.getTeamId());
					intent.putExtra("teamName", team.getName_pits());
					startActivity(intent);
				}
			});
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
			case R.id.requestRegistrationDataUpdate:
				requestRegistrationUpdate();
				return true;
		}
		return false;
	}
	
	

	/**
	 * Fragt aktuelle Daten über das Netz an und schreibt sie in die Datenbank
	 */

	private void requestRegistrationUpdate() {
		Intent mIntent = new Intent(this, DTNService.class);
		mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.SEND_DATA);
		mIntent.putExtra("singletonendpoint", "dtn://mulita-fsg.dtn/fsg");
		mIntent.putExtra("jsondata", "reg_update");
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
		
		String writeDriver1 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(80,20,'Harald','Juhnke');";
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
	}
	
	
}
