package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class RunActivity extends Activity{
	
	Button btnAcceleration;
	Button btnSkidPad;
	Button btnAutocross;
	Button btnEndurance;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
		btnAcceleration = (Button) findViewById(R.id.btnRunAcceleration);
		btnSkidPad = (Button) findViewById(R.id.btnRunSkidPad);
		btnAutocross = (Button) findViewById(R.id.btnRunAutocross);
		btnEndurance = (Button) findViewById(R.id.btnRunEndurance);
	}
	
    /**
     * 
     * @param view
     */
    public void onButtonClick(View view){
    	
    	Intent intent;
    	
    	
    	
    	
    	switch(view.getId()) {
    		case R.id.btnRunAcceleration:
    			intent = new Intent(this, RunActivityConfirm.class);
    			intent.putExtra("DisciplineName", "Acceleration");
    			break;
    		case R.id.btnRunSkidPad:
    			intent = new Intent(this, RunActivityConfirm.class);
    			intent.putExtra("DisciplineName", "Skid Pad");
    			break;
    		case R.id.btnRunAutocross:
    			intent = new Intent(this, RunActivityConfirm.class);
    			intent.putExtra("DisciplineName", "Autocross");
    			break;
    		case R.id.btnRunEndurance:
    			intent = new Intent(this, RunActivityConfirm.class);
    			intent.putExtra("DisciplineName", "Endurance");
    			break;
    		default:
    			intent = null;
    			
    	}
    	startActivity(intent);
    }

}
