package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
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
			final TextView teamId = new TextView(this);
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
			row.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Intent intent = new Intent(getBaseContext(), RegistrationDriverSelectionActivity.class);
					intent.putExtra("test", ""+team.getTeamId());
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
