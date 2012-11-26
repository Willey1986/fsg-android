package de.tubs.cs.ibr.fsg;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import de.tubs.cs.ibr.fsg.activities.BriefingCheckInActivity;
import de.tubs.cs.ibr.fsg.activities.BriefingCheckOutActivity;
import de.tubs.cs.ibr.fsg.activities.DriverRegistrationActivity;
import de.tubs.cs.ibr.fsg.activities.InfoTerminalActivity;
import de.tubs.cs.ibr.fsg.activities.RunActivity;
import de.tubs.cs.ibr.fsg.service.DTNService;


public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "activity created.");
        
        // Notwendige Registrierung des DTNServices und des DTNReceivers beim IBR-DTN  
        // (durch die Klasse "Registration" der IBR-DTN-API). Dies wird benötigt, wenn die Anwendung
        // zum allerersten Mal auf dem Gerät läuft oder wenn der Cache von IBR-DTN gelöscht 
        // wird. Sonst werden weder der DTNService noch der DTNReceiver von IBR-DTN aktiviert, 
        // was zu Folge hat, dass keine Daten empfangen werden können.
		Intent newIntent = new Intent(this, DTNService.class);
		newIntent.setAction(DTNService.REGISTRATION_INTENT);
		this.startService(newIntent);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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
