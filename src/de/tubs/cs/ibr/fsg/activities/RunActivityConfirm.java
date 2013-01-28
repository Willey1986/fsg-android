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

	public static final int ERROR_MAX_DISCIPLINES_REACHED = 0;
	public static final int ERROR_MAX_RUNS_IN_DISCIPLINE_REACHED = 1;
	public static final int ERROR_TAG_BLACKLISTED = 2;
	public static final int ERROR_BRIEFING_ABSENT = 3;
	
	String disciplineName, message;
	int runCount, errorNumber;
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
								writeRunsWhileLessThanThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_ACCELERATION);
							}
							if (disciplineName.equals("Skid Pad")) {
								writeRunsWhileLessThanThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_SKID_PAD);
							}
							if (disciplineName.equals("Autocross")) {
								writeRunsWhileLessThanThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_AUTOCROSS);
							}
							if (disciplineName.equals("Endurance")) {
								writeRunsWhileLessThanThreeDisciplinesArePresent(intent, tagContent, FsgHelper.RUN_DISCIPLINE_ENDURANCE);
							}
						}
					}
					else {
						message = "Abgelehnt:\nBriefing nicht besucht";
						errorNumber = ERROR_BRIEFING_ABSENT;
						success = false;
					}
				}
			}
			else {
				message = "Armband gesperrt";
				errorNumber = ERROR_TAG_BLACKLISTED;
				success = false;
			}
			
			
			
		} catch (FsgException e) {
			
		}
		
		Intent mIntent = new Intent(this, RunActivityPost.class);
		mIntent.putExtra("DisciplineName", disciplineName);
		mIntent.putExtra("RunCount", runCount);
		mIntent.putExtra("Message", message);
		mIntent.putExtra("Success",success);
		mIntent.putExtra("ErrorNumber",errorNumber);
		startActivity(mIntent);
	}
	
	private void writeRunsWhileLessThanThreeDisciplinesArePresent(Intent intent, NfcObject tagContent, int disciplineNumber) {						//Was für ein toller Name ;)
		short runs = 0;
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
		if (runCount <= 2-runs) {
			writeRunsToTag(intent, runCount, disciplineNumber);
		} else {
			message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
			errorNumber = ERROR_MAX_RUNS_IN_DISCIPLINE_REACHED;
			success = false;
			Log.e("ERROR",message);
		}
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
			errorNumber = ERROR_MAX_DISCIPLINES_REACHED;
			success = false;
		} 
		else {
			if (runCount <= 2-runs) {
				writeRunsToTag(intent, runCount, disciplineNumber);
			}
			else {
				message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
				errorNumber = ERROR_MAX_RUNS_IN_DISCIPLINE_REACHED;
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
