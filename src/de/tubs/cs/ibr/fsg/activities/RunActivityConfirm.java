package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.FsgHelper;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RunActivityConfirm extends NfcEnabledActivity {

	String disciplineName, message;
	int runCount;
	TextView txtDisciplineName, txtRunCount, txtRunConfirmationProgress;
	Resources res;
	Bundle extras;
	boolean success;

	private Nfc nfc;
	private DBAdapter dba;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_confirm);
		
		nfc = new Nfc(this);
		dba = new DBAdapter(this);
		dba.open();
		
		extras = getIntent().getExtras();
		res = getResources();
		
		disciplineName = extras.getString("DisciplineName");
		runCount = extras.getInt("RunCount");
		
		
		txtDisciplineName = (TextView) findViewById(R.id.txtRunDisciplineName);
		txtRunCount = (TextView) findViewById(R.id.txtRunCount);
		txtRunConfirmationProgress = (TextView) findViewById(R.id.txtRunConfirmationProgress);
		
		txtDisciplineName.setText(res.getString(R.string.txtConfirmDisciplineName) + " " + disciplineName);
		txtRunCount.setText(res.getString(R.string.txtConfirmRunCount) + " " + runCount);
		txtRunConfirmationProgress.setText("Bitte Band anhalten um Runs zu schreiben");
	}


	@Override
	public void executeNfcAction(Intent intent) {
		
		message = "";
		success = true;
		
		
		try {
			nfc.readTag(intent);
			NfcObject tagContent = NfcData.interpretData(nfc.getData());
			
			String tagID = nfc.getTagID();
			Driver driver = tagContent.getDriverObject();
			if (!dba.isTagBlacklisted(tagID)) {
				if(extras.containsKey("Ignore")) {
					if (disciplineName.equals("Acceleration")) {
						writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_ACCELERATION);
					}
					if (disciplineName.equals("Skid Pad")) {
						writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_SKID_PAD);
					}
					if (disciplineName.equals("Autocross")) {
						writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_AUTOCROSS);
					}
					if (disciplineName.equals("Endurance")) {
						writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_ENDURANCE);
					}
				}
				else {
					if (tagContent.haveTheDriverTodaysBriefing()) {
						if (tagContent.howManyDisciplinesAreDriven() >= 3) { //Bereits 3 Disziplinen gefahren
							if (disciplineName.equals("Acceleration")) {
								writeRunsWhileThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_ACCELERATION);
							}
							if (disciplineName.equals("Skid Pad")) {
								writeRunsWhileThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_SKID_PAD);
							}
							if (disciplineName.equals("Autocross")) {
								writeRunsWhileThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_AUTOCROSS);
							}
							if (disciplineName.equals("Endurance")) {
								writeRunsWhileThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_ENDURANCE);
							}
						} else { //weniger als 3 Disziplinen gefahren
							if (disciplineName.equals("Acceleration")) {
								short runs = tagContent.getAccelerationRuns();
								if (runCount <= 2-runs) {
									writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_ACCELERATION);
								} else {
									message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
									success = false;
									Log.e("ERROR",message);
								}
							}
							if (disciplineName.equals("Skid Pad")) {
								short runs = tagContent.getSkidPadRuns();
								if (runCount <= 2-runs) {
									writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_SKID_PAD);
								} else {
									message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
									success = false;
									Log.e("ERROR",message);
								}
							}
							if (disciplineName.equals("Autocross")) {
								short runs = tagContent.getAutocrossRuns();
								if (runCount <= 2-runs) {
									writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_AUTOCROSS);
								} else {
									message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
									success = false;
									Log.e("ERROR",message);
								}
							}
							if (disciplineName.equals("Endurance")) {
								short runs = tagContent.getEnduranceRuns();
								if (runCount <= 2-runs) {
									writeRunsToTag(intent, runCount, FsgHelper.RUN_DISCIPLINE_ENDURANCE);
								} else {
									message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
									success = false;
									Log.e("ERROR",message);
								}
							}
						}
					}
					else {
						message = "Abgelehnt:\nBriefing nicht besucht";
						success = false;
					}
				}
			}
			else {
				message = "Armband gesperrt";
				success = false;
			}
			
			
			
		} catch (FsgException e) {
			
		}
		
		Intent mIntent = new Intent(this, RunActivityPost.class);
		mIntent.putExtra("DisciplineName", disciplineName);
		mIntent.putExtra("RunCount", runCount);
		mIntent.putExtra("Message", message);
		mIntent.putExtra("Success",success);
		startActivity(mIntent);
	}
	
	private void writeRunsWhileThreeDisciplinesArePresent(Intent intent, NfcObject tagContent, int disciplineNumber) {
		short runs = 0; 				//Zur initialisierung
		switch (disciplineNumber) {
			case FsgHelper.RUN_DISCIPLINE_ACCELERATION:
				runs = tagContent.getAccelerationRuns();
				break;
			case FsgHelper.RUN_DISCIPLINE_SKID_PAD:
				runs = tagContent.getSkidPadRuns();
				break;
			case FsgHelper.RUN_DISCIPLINE_AUTOCROSS:
				runs = tagContent.getAutocrossRuns();
				break;
			case FsgHelper.RUN_DISCIPLINE_ENDURANCE:
				runs = tagContent.getEnduranceRuns();
				break;
		}
		if (runs == 0) {
			message = "Maximale Anzahl gefahrene Disziplinen erreicht";
			success = false;
		} 
		else {
			if (runCount <= 2-runs) {
				writeRunsToTag(intent, runCount, disciplineNumber);
			}
			else {
				message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
				success = false;
				Log.e("ERROR",message);
			}
		}
	}
	
	private void writeRunsToTag(Intent intent, int runCount, int disciplineNumber) {
		try {
			for (int i = 0; i < runCount; i++) {
				byte[][] contentToWrite = NfcData.generateRun((short)disciplineNumber);
				nfc.writeTag(intent, contentToWrite);
				success = true;
			}
		} catch (FsgException e) {
			
		}
	}
}
