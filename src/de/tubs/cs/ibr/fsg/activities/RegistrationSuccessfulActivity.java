package de.tubs.cs.ibr.fsg.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class RegistrationSuccessfulActivity extends Activity {

	private final static String TAG = "RegistrationSuccessfulActivity";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_successful);
 
       	Log.i(TAG, "activity created.");
    }


    
    public void onButtonClick(View view){
    	onBackPressed();
		
    }
 
    
}


