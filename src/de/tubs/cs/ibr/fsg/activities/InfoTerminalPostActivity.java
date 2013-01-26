package de.tubs.cs.ibr.fsg.activities;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.dtn.FileHelper;

public class InfoTerminalPostActivity extends Activity {
	
	private final static String TAG = "InfoTerminalPostActivity";

	private NfcObject mNfcObject;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_terminal_post);
        
        Intent mIntent = getIntent();
        mNfcObject = mIntent.getParcelableExtra("nfc_object");
		NfcOperation mNfcOperation = new NfcOperation(this);
		mNfcOperation.execute(mIntent);
		this.getDataAndDisplayIt();
		Log.i(TAG, "activity created.");
    }
    
    
    
	private void getDataAndDisplayIt() {
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		Driver databaseDriver = dba.getDriver(mNfcObject.getDriverObject().getDriverID());
		String lastNameFromDatabase = null;
		String firstNameFromDatabase = null;
		if (databaseDriver!=null){
			lastNameFromDatabase = databaseDriver.getLastName();
			firstNameFromDatabase = databaseDriver.getFirstName();
		}
		
		
		if (lastNameFromDatabase==null || lastNameFromDatabase.equalsIgnoreCase("")){
			lastNameFromDatabase = mNfcObject.getDriverObject().getLastName() ;
		}
		TextView driverLastName = (TextView) findViewById(R.id.textViewLastname2);
		driverLastName.setText( lastNameFromDatabase );
		

		if (firstNameFromDatabase==null || firstNameFromDatabase.equalsIgnoreCase("")){
			firstNameFromDatabase = mNfcObject.getDriverObject().getFirstName() ;
		}
		TextView driverFirstName = (TextView) findViewById(R.id.textViewFirstname2);
		driverFirstName.setText( firstNameFromDatabase );
		
		
		TextView accelerationRuns = (TextView) findViewById(R.id.textViewAccelerationRuns2);
		accelerationRuns.setText( String.valueOf(mNfcObject.getAccelerationRuns()) );
		
		TextView enduranceRuns = (TextView) findViewById(R.id.textViewEnduranceRuns2);
		enduranceRuns.setText( String.valueOf(mNfcObject.getEnduranceRuns()) );
		
		TextView skidPadRuns = (TextView) findViewById(R.id.textViewSkidPadRuns2);
		skidPadRuns.setText( String.valueOf(mNfcObject.getSkidPadRuns()) );
		
		TextView autocrossRuns = (TextView) findViewById(R.id.textViewAutocrossRuns2);
		autocrossRuns.setText( String.valueOf(mNfcObject.getAutocrossRuns()) );
		
		
		if(mNfcObject.haveTheDriverTodaysBriefing()){
			View driverPic = (View) findViewById(R.id.infoTerminalBriefingIcon);
			//Drawable mDrawable = Drawable.createFromPath( picFile.getAbsolutePath() );
			//ImageView image = (ImageView) findViewById(R.id.);
			driverPic.setBackgroundDrawable( getResources().getDrawable(R.drawable.icon_briefing_in) );
		}
		
		
		// An dieser Stelle suchen wir nach dem Fahrerbild und stellen es ggf dar.
		int driverId = mNfcObject.getDriverObject().getDriverID();
		File picDirectory = FileHelper.getStoragePath(FileHelper.DRIVER_PICS_DIR);
		File picFile = null;
		if(picDirectory!=null){
			picFile = new File( picDirectory.getAbsoluteFile() + String.valueOf(File.separatorChar) + String.valueOf(driverId) + ".jpg" );
		}
		if (picFile!=null && picFile.exists() ) {
			View driverPic = (View) findViewById(R.id.infoTerminalDriverPic);
			Drawable mDrawable = Drawable.createFromPath( picFile.getAbsolutePath() );
			driverPic.setBackgroundDrawable(mDrawable);
		}
		
		
		
	}



	private class NfcOperation extends AsyncTask<Intent, Integer, String> {

		InfoTerminalPostActivity mInfoTerminalPostActivity;

		public NfcOperation(InfoTerminalPostActivity mInfoTerminalPostActivity) {
			this.mInfoTerminalPostActivity = mInfoTerminalPostActivity;
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
				if (mInfoTerminalPostActivity!=null){
					mInfoTerminalPostActivity.onBackPressed();
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
