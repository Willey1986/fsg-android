package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.service.DTNService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

/**
 * Diese Activity ohne View brauchen wir, um die Registrierung des IBR-DTN-Service "nur" einmal
 * veranlassen zu koennen, nämlich beim Start der Anwendung. Einmal reicht es vollkommen aus.
 */
public class StartActivity extends Activity {

	private static final String TAG = "StartActivity";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        // Notwendige Registrierung des DTNServices und des DTNReceivers beim IBR-DTN  
        // (durch die Klasse "Registration" der IBR-DTN-API). Dies wird benötigt, wenn die Anwendung
        // zum allerersten Mal auf dem Gerät läuft oder wenn der Cache von IBR-DTN gelöscht 
        // wird. Sonst werden weder der DTNService noch der DTNReceiver von IBR-DTN aktiviert, 
        // was zu Folge hat, dass keine Daten empfangen werden können.
		Intent newIntent = new Intent(this, DTNService.class);
		newIntent.setAction(DTNService.REGISTRATION_INTENT);
		startService(newIntent);
        
		Log.i(TAG, "activity created.");
        startActivity(new Intent(this, MainActivity.class));
    }


    
}
