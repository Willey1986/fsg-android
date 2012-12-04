package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.R.id;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import de.tubs.cs.ibr.fsg.service.DTNService;


public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "activity created.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
        @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    	case R.id.user:
    		chooseUser();
    		break;
    	case R.id.menu_settings:
    		settingsMenuItem();
    		break;
    	}
    	
    	return true;
    }
    
    private void chooseUser(){
    	new AlertDialog.Builder (this)
    	.setTitle("Nutzertyp auswählen")
    	.setMessage("Wahl zwischen Administrator und normalen Nutzer")
    	.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

    		}
    	}).show();
    }
    
    private void settingsMenuItem(){
    	new AlertDialog.Builder (this)
    	.setTitle("Settings")
    	.setMessage("Auswahl der Einstellungen")
    	.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			
    		}
    	}).show();
    }
    
    
    /**
     * 
     * @param view
     */
    public void onButtonClick(View view){
    	switch (view.getId() ){
    	case R.id.button1:
    		startActivity(new Intent(this, DriverRegistrationActivity.class));
    		break;
    	case R.id.button2:
    		startActivity(new Intent(this, BriefingCheckInActivity.class));
    		break;
    	case R.id.button3:
    		startActivity(new Intent(this, BriefingCheckOutActivity.class));
    		break;
    	case R.id.button4:
    		startActivity(new Intent(this, RunActivity.class));
    		break;
    	case R.id.button5:
    		startActivity(new Intent(this, InfoTerminalActivity.class));
    		break;
    	}
    }
    
    

    
}
