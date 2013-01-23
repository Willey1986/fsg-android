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
	
    /**
     * 
     * @param view
     */
    public void onButtonClick(View view){
    	switch (view.getId() ){
    		case R.id.scanButton:
    		
    			//user pressed scan button
    			
    			this.shouldShowWait(!this.isWaiting);
    			
    			//user should wait
    			if(this.isWaiting){
    				Button scanB = (Button) findViewById(R.id.scanButton);
    				scanB.setText("Abbruch");
    			}else{
    				Button scanB = (Button) findViewById(R.id.scanButton);
    				scanB.setText("Scan");
    			}
    			
    			
    			break;
    		case R.id.check1:
    			
    			//user pressed 1 checkbox
    			
    			//unselect the other checkbox
    			if(check1.isChecked()&&check2.isChecked()){
    				check2.setChecked(false);
    			}
    			

				Button scanB1 = (Button) findViewById(R.id.scanButton);
				
    			//nothing is checked
    			if(!check1.isChecked()&&!check2.isChecked()){
    				scanB1.setEnabled(false);
    			}else{

    				scanB1.setEnabled(true);
    			}
    			
    			break;
    			
    		case R.id.check2:
    			
    			//user pressed 2 checkbox
    			
    			//unselect the other checkbox
    			if(check1.isChecked()&&check2.isChecked()){
    				check1.setChecked(false);
    			}


				Button scanB2 = (Button) findViewById(R.id.scanButton);
				
    			//nothing is checked
    			if(!check1.isChecked()&&!check2.isChecked()){
    				scanB2.setEnabled(false);
    			}else{

    				scanB2.setEnabled(true);
    			}
    			
    			break;
    			
    	}
    }


	@Override
	public void executeNfcAction(Intent intent) {
		
		String message = "";
		
		
		try {
			nfc.readTag(intent);
			NfcObject tagContent = NfcData.interpretData(nfc.getData());
			
			Driver driver = tagContent.getDriverObject();
			
			if (tagContent.haveTheDriverTodaysBriefing()) {
				if (tagContent.howManyDisciplinesAreDriven() >= 3) { //Bereits 3 Disziplinen gefahren
					
					
				} else { //weniger als 3 Disziplinen gefahren
					if (disciplineName.equals("Acceleration")) {
						short runs = tagContent.getAccelerationRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_ACCELERATION);
								nfc.writeTag(intent, contentToWrite);
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert. Noch " + (2-runs) + " Runs auf dieser Disziplin möglich";
							Log.e("ERROR",message);
						}
					}
					if (disciplineName.equals("Skid Pad")) {
						short runs = tagContent.getSkidPadRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_SKID_PAD);
								nfc.writeTag(intent, contentToWrite);
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert. Noch " + (2-runs) + " Runs auf dieser Disziplin möglich";
							Log.e("ERROR",message);
						}
					}
					if (disciplineName.equals("Autocross")) {
						short runs = tagContent.getAutocrossRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_AUTOCROSS);
								nfc.writeTag(intent, contentToWrite);
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert. Noch " + (2-runs) + " Runs auf dieser Disziplin möglich";
							Log.e("ERROR",message);
						}
					}
					if (disciplineName.equals("Endurance")) {
						short runs = tagContent.getEnduranceRuns();
						if (runCount <= 2-runs) {
							for (int i = 0; i < runCount; i++) {
								byte[][] contentToWrite = NfcData.generateRun(FsgHelper.RUN_DISCIPLINE_ACCELERATION);
								nfc.writeTag(intent, contentToWrite);
							}
						} else {
							message = "Bereits " + runs + " Runs absolviert. Noch " + (2-runs) + " Runs auf dieser Disziplin möglich";
							Log.e("ERROR",message);
						}
					}
				}
			}
			
		} catch (FsgException e) {
			
		}
		
		Intent mIntent = new Intent(this, RunActivityMessage.class);
		mIntent.putExtra("DisciplineName", disciplineName);
		mIntent.putExtra("RunCount", runCount);
		mIntent.putExtra("ErrorMessage", message);
		startActivity(mIntent);
	}
}
