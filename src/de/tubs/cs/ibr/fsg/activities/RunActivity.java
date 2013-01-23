package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class RunActivity extends Activity{
	
	ToggleButton tBtnAcceleration;
	ToggleButton tBtnSkidPad;
	ToggleButton tBtnAutocross;
	ToggleButton tBtnEndurance;
	ToggleButton tBtnRunOne;
	ToggleButton tBtnRunTwo;
	
	Button btnRunConfirm;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
		tBtnAcceleration = (ToggleButton) findViewById(R.id.tBtnAcceleration);
		tBtnSkidPad = (ToggleButton) findViewById(R.id.tBtnSkidPad);
		tBtnAutocross = (ToggleButton) findViewById(R.id.tBtnAutocross);
		tBtnEndurance = (ToggleButton) findViewById(R.id.tBtnEndurance);
		tBtnRunOne = (ToggleButton) findViewById(R.id.tBtnRunOne);
		tBtnRunTwo = (ToggleButton) findViewById(R.id.tBtnRunTwo);
		
		btnRunConfirm = (Button) findViewById(R.id.btnRunConfirm);

		btnRunConfirm.setEnabled(false);
	}
	
    /**
     * 
     * @param view
     */
    public void onButtonClick(View view){

    	switch(view.getId()) {
    		case R.id.tBtnAcceleration:
    			tBtnSkidPad.setChecked(false);
    			tBtnAutocross.setChecked(false);
    			tBtnEndurance.setChecked(false);
    			break;
    		case R.id.tBtnSkidPad:
    			tBtnAcceleration.setChecked(false);
    			tBtnAutocross.setChecked(false);
    			tBtnEndurance.setChecked(false);
    			
    			break;
    		case R.id.tBtnAutocross:
    			tBtnAcceleration.setChecked(false);
    			tBtnSkidPad.setChecked(false);
    			tBtnEndurance.setChecked(false);
    			break;
    		case R.id.tBtnEndurance:
    			tBtnSkidPad.setChecked(false);
    			tBtnAutocross.setChecked(false);
    			tBtnAcceleration.setChecked(false);
    			break;
    		case R.id.tBtnRunOne:
    			tBtnRunTwo.setChecked(false);
    			break;
    		case R.id.tBtnRunTwo:
    			tBtnRunOne.setChecked(false);
    			break;	
    	}
    	
    	checkRequirementsToContinue();
    	//startActivity(intent);
    }
    
    private void checkRequirementsToContinue() {
		if ((tBtnAcceleration.isChecked() || tBtnSkidPad.isChecked() || tBtnAutocross.isChecked() || tBtnEndurance.isChecked()) && (tBtnRunOne.isChecked() || tBtnRunTwo.isChecked()))
			btnRunConfirm.setEnabled(true);
		else
			btnRunConfirm.setEnabled(false);
		
	}

	public void onConfirm(View view) {
		Intent intent = new Intent(this, RunActivityConfirm.class);
		if (tBtnAcceleration.isChecked())
			intent.putExtra("DisciplineName", "Acceleration");
		if (tBtnSkidPad.isChecked())
			intent.putExtra("DisciplineName", "Skid Pad");
		if (tBtnAutocross.isChecked())
			intent.putExtra("DisciplineName", "Autocross");
		if (tBtnEndurance.isChecked())
			intent.putExtra("DisciplineName", "Endurance");
		if (tBtnRunOne.isChecked())
			intent.putExtra("RunCount", 1);
		if (tBtnRunTwo.isChecked())
			intent.putExtra("RunCount", 2);
		startActivity(intent);
    }

}
