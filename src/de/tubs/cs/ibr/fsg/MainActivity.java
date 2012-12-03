package de.tubs.cs.ibr.fsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
    	// Während der Entwicklung können wir mit Hilfe des StrictModes darauf aufmerksam gemacht werden,
    	// wenn wir den UI-Thread/Main-Thread mit Sachen blockieren, die eigentlich nebenläufig gehören.
    	// Sonst läuft die App nicht flüssig und es drohen sogar die berühmt-berüchtigte ANR Dialoge
    	// (Application not Responding Dialog). Abhilfe: z.B. AsyncTask(einfach) oder zu Fuss mit einer
    	// kompletten Multithreading-Lösung mit einem Standard-Java-Thread (schwieriger).
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
        
    }

    
	@Override
	protected void onResume() {
        // Notwendige Registrierung des DTNServices und des DTNReceivers beim IBR-DTN  
        // (durch die Klasse "Registration" der IBR-DTN-API). Dies wird benötigt, wenn die Anwendung
        // zum allerersten Mal auf dem Gerät läuft oder wenn der Cache von IBR-DTN gelöscht 
        // wird. Sonst werden weder der DTNService noch der DTNReceiver von IBR-DTN aktiviert, 
        // was zu Folge hat, dass keine Daten empfangen werden können.
		ServiceRegisterRunnable mRunnable = new ServiceRegisterRunnable(this);
		Thread mThread = new Thread(mRunnable);
		mThread.start();
		
        super.onResume();
        Log.i(TAG, "activity resumed");
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
    
    
    /**
     * Mit Hilfe dieser Runnable lagern wir die Arbeiten um den IBR-DTN-Dienst aus,
     * so halten wir den UI-Thread "sauber".
     *
     */
    private class ServiceRegisterRunnable implements Runnable{

    	private MainActivity mActivity;
    	
		public ServiceRegisterRunnable(MainActivity mActivity) {
			super();
			this.mActivity = mActivity;
		}

		public void run() {
			Intent newIntent = new Intent(mActivity, DTNService.class);
    		newIntent.setAction(DTNService.REGISTRATION_INTENT);
    		mActivity.startService(newIntent);
			
		}
    }
    
}
