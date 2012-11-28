package de.tubs.cs.ibr.fsg;

import android.os.Bundle;
import android.os.StrictMode;
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
	private static final boolean DEVELOPER_MODE = true;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "activity created.");
        
        // Notwendige Registrierung des DTNServices und des DTNReceivers beim IBR-DTN  
        // (durch die Klasse "Registration" der IBR-DTN-API). Dies wird ben�tigt, wenn die Anwendung
        // zum allerersten Mal auf dem Ger�t l�uft oder wenn der Cache von IBR-DTN gel�scht 
        // wird. Sonst werden weder der DTNService noch der DTNReceiver von IBR-DTN aktiviert, 
        // was zu Folge hat, dass keine Daten empfangen werden k�nnen.
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
