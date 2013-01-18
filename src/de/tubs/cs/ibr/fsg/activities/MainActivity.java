package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.tubs.cs.ibr.fsg.R;


public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final boolean DEVELOPER_MODE = false;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	// W�hrend der Entwicklung k�nnen wir mit Hilfe des StrictModes darauf aufmerksam gemacht werden,
    	// wenn wir den UI-Thread/Main-Thread mit Sachen blockieren, die eigentlich nebenl�ufig geh�ren.
    	// Sonst l�uft die App nicht fl�ssig und es drohen sogar die ber�hmt-ber�chtigte ANR Dialoge
    	// (Application not Responding Dialog). Abhilfe: z.B. AsyncTask(einfach) oder zu Fuss mit einer
    	// kompletten Multithreading-L�sung mit einem Standard-Java-Thread (schwieriger).
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        
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
    	.setTitle("Nutzertyp auswaehlen")
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
    		startActivity(new Intent(this, RegistrationTeamSelectionActivity.class));
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
    		startActivity(new Intent(this, InfoTerminalPreActivity.class));
    		break;
    	}
    }
    
    

    
}
