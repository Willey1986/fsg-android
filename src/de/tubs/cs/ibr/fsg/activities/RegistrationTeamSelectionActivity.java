package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.DBHelper;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.db.models.Team;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RegistrationTeamSelectionActivity extends Activity {
	
	TableLayout regTable;
	
	DBAdapter dba = new DBAdapter(this);
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_team_selection);
		regTable = (TableLayout) findViewById(R.id.regTable);
		ArrayList<Team> teams = dba.getAllTeams();
		for (int i = 0; i < teams.size(); i++) {
			final Team team = teams.get(i);
			TableRow row = new TableRow(this);
			TextView teamId = new TextView(this);
			TextView teamName = new TextView(this);
			TextView teamCountry = new TextView(this);
			TextView teamCity = new TextView(this);
			TextView teamUniversity = new TextView(this);
			teamId.setText(""+team.getTeamId());
			teamId.setPadding(15, 0, 0, 0);
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
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registration, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuItemRefresh:
				refreshDB();
				return true;
			case R.id.miRegInsertDummyData:
				writeSampleDataToDb();
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
	
	public void writeSampleDataToDb() {
		String writeDriver1 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(80,20,'Harald','Juhnke',0);";
		String writeDriver2 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(81,20,'Stefan','Raab',0);";
		String writeDriver3 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(82,20,'Verona','Pooth',1);";
		String writeDriver4 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(83,21,'Claudia','Roth',1);";
		String writeDriver5 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_DRIVERS + " VALUES(84,21,'Michael','Schumacher',0);";
		
		String writeTeam1 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_TEAMS + " VALUES(20,'DE','Germany','Braunschweig','TU',11,3,0,1,'Lions Racing Team');";
		String writeTeam2 = "INSERT OR IGNORE INTO " + DBHelper.TABLE_TEAMS + " VALUES(21,'E','Spain','Barcelona','U',20,4,0,1,'Fernando Alonso Racing');";
		
		dba.rawQuery(writeDriver1);
		dba.rawQuery(writeDriver2);
		dba.rawQuery(writeDriver3);
		dba.rawQuery(writeDriver4);
		dba.rawQuery(writeDriver5);
		
		dba.rawQuery(writeTeam1);
		dba.rawQuery(writeTeam2);
	}
	
}
