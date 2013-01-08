/**
 * Beinhaltet sämtliche Logik zur Auswahl des Teams bei der Registrierung
 */

package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Team;
import de.tubs.cs.ibr.fsg.dtn.DTNService;
import de.tubs.cs.ibr.fsg.dtn.FsgProtocol;

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
	
	protected void onResume() {
		super.onResume();
		dba.open();
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
			case R.id.miRegInsertDummyData:
				dba.writeSampleData();
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
		mIntent.putExtra("destination", "dtn://fsg-backend.dtn/fsg"  );
		mIntent.putExtra("type",        String.valueOf(requestType) );
		mIntent.putExtra("version",     "0");
		mIntent.putExtra("payload",     "nichts");
		startService(mIntent);
	}	
}