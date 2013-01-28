package de.tubs.cs.ibr.fsg.activities;


import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.R;

public class BriefingSuccessfulActivity extends Activity {

	private final static String TAG = "BriefingSuccessfulActivity";
	
	public final static int CHECK_IN = 1;
	public final static int CHECK_OUT = 2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briefing_successful);
        
        Intent mIntent = getIntent();
        int modus = mIntent.getIntExtra("modus", 1);
        
        if(modus==CHECK_IN){
        	// Nichts tun, der Text aus der XML-Datei passt schon
        }else{
        	//modus==CHECK_OUT
        	TextView successfulText = (TextView) findViewById(R.id.textViewBriefingSuccessful);
        	successfulText.setText(R.string.checkOutSuccessful );
        }

		NfcOperation mNfcOperation = new NfcOperation(this);
		mNfcOperation.execute(mIntent);
        
        
       	Log.i(TAG, "activity created.");
    }

	private class NfcOperation extends AsyncTask<Intent, Integer, String> {

		BriefingSuccessfulActivity mBriefingSuccessfulActivity;

		public NfcOperation(BriefingSuccessfulActivity mBriefingSuccessfulActivity) {
			this.mBriefingSuccessfulActivity = mBriefingSuccessfulActivity;
		}

		@Override
		protected String doInBackground(Intent... params) {
			Intent mIntent = params[0];
	        Tag tagFromIntent = mIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			try {
				tag.connect();
			} catch (IOException e) {
				// Die Karte wurde schnell entfernt, ist nicht schlimm,  wir brauchen an
				// dieser Stelle nichts zu tun, es ist vorgesehen, dass es einfach weiter geht.
			}
			while(tag.isConnected()){
				// Hier passiert nichts! Waehrend dieser Schleife werden naemlich 
				// die Daten des Fahrers dargestellt. Man bleibt in der Schleife,
				// so lange man den Tag an dem NFC-Lesegeraet haelt. Wird der Tag
				// vom Lesegeraet entfernt, dann verlassen wir die Schleife.
			}
	        return null;
		}

		@Override
		protected void onPostExecute(String mString) {
			try {
				if (mBriefingSuccessfulActivity!=null){
					mBriefingSuccessfulActivity.onBackPressed();
				}
			}catch (Exception e) {
				// Nichts tun, hier landen wir, wenn der Operator den Back-Button selber benutzt hat.
			}
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}
 
    
}


