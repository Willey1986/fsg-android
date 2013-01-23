package de.tubs.cs.ibr.fsg.activities;

import java.util.ArrayList;

import de.tubs.cs.ibr.fsg.FsgHelper;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;
import de.tubs.cs.ibr.fsg.R;
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

	String disciplineName;
	int runCount;
	TextView txtDisciplineName, txtRunCount, txtRunConfirmationProgress;
	Resources res;
	
	private CheckBox check1;
	private CheckBox check2;
	
	private boolean isWaiting;

	private Nfc nfc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_confirm);
		
		nfc = new Nfc(this);
		
		Bundle extras = getIntent().getExtras();
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
	
	
	private void shouldShowWait(boolean showWaiting){

		
		
		TextView text1 = (TextView) findViewById(R.id.textView1);
		TextView text2 = (TextView) findViewById(R.id.textView2);
		
		
		//should show the waiting information
		int isVisible = View.GONE;
		
		//show checkbox
		int checkBoxIsVisible = View.GONE;
		
		if(showWaiting){
			text1.setText("Initialisierung");
			
			isVisible = View.VISIBLE;
			
			checkBoxIsVisible = View.INVISIBLE;
		}else{

			text1.setText("Wie viele Rennen sollen geloggt werden?");
			isVisible = View.INVISIBLE;

			checkBoxIsVisible = View.VISIBLE;
		}
		this.isWaiting = showWaiting;
		
		text2.setVisibility(isVisible);
		ProgressBar progress = (ProgressBar) findViewById(R.id.progressbar);
		progress.setVisibility(isVisible);
		
		check1.setVisibility(checkBoxIsVisible);
		check2.setVisibility(checkBoxIsVisible);
		
		
	}


	@Override
	public void executeNfcAction(Intent intent) {
		
		String message = "";
		boolean success = true;
		
		
		try {
			nfc.readTag(intent);
			NfcObject tagContent = NfcData.interpretData(nfc.getData());
			
			Driver driver = tagContent.getDriverObject();
			
			if (tagContent.haveTheDriverTodaysBriefing()) {
				if (tagContent.howManyDisciplinesAreDriven() >= 3) { //Bereits 3 Disziplinen gefahren
					if (disciplineName.equals("Acceleration")) {
						short runs = tagContent.getAccelerationRuns();
						if (runs == 0) {
							message = "Maximale Anzahl gefahrene Disziplinen erreicht";
							success = false;
						} 
						else {
							if (runCount <= 2-runs) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_ACCELERATION);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
							else {
								message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
								success = false;
								Log.e("ERROR",message);
							}
						}
					}
					if (disciplineName.equals("Skid Pad")) {
						short runs = tagContent.getSkidPadRuns();
						if (runs == 0) {
							message = "Maximale Anzahl gefahrene Disziplinen erreicht";
							success = false;
						}
						else {
							if (runCount <= 2-runs) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_SKID_PAD);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
							else {
								message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
								success = false;
								Log.e("ERROR",message);
							}
						}
					}
					if (disciplineName.equals("Autocross")) {
						short runs = tagContent.getAutocrossRuns();
						if (runs == 0) {
							message = "Maximale Anzahl gefahrene Disziplinen erreicht";
							success = false;
						}
						else {
							if (runCount <= 2-runs) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_AUTOCROSS);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
							else {
								message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
								success = false;
								Log.e("ERROR",message);
							}
						}
					}
					if (disciplineName.equals("Endurance")) {
						short runs = tagContent.getEnduranceRuns();
						if (runs == 0) {
							message = "Maximale Anzahl gefahrene Disziplinen erreicht";
							success = false;
						}
						else {
							if (runCount <= 2-runs) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_ENDURANCE);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
							else {
								message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
								success = false;
								Log.e("ERROR",message);
							}
						}
					}
					
				} else { //weniger als 3 Disziplinen gefahren
					if (disciplineName.equals("Acceleration")) {
						short runs = tagContent.getAccelerationRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_ACCELERATION);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
							success = false;
							Log.e("ERROR",message);
						}
					}
					if (disciplineName.equals("Skid Pad")) {
						short runs = tagContent.getSkidPadRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_SKID_PAD);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
							success = false;
							Log.e("ERROR",message);
						}
					}
					if (disciplineName.equals("Autocross")) {
						short runs = tagContent.getAutocrossRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_AUTOCROSS);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
							success = false;
							Log.e("ERROR",message);
						}
					}
					if (disciplineName.equals("Endurance")) {
						short runs = tagContent.getEnduranceRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_ENDURANCE);
								nfc.writeTag(intent, contentToWrite);
								success = true;
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert.\nNoch " + (2-runs) + " Runs auf dieser Disziplin erlaubt";
							success = false;
							Log.e("ERROR",message);
						}
					}
				}
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
}
