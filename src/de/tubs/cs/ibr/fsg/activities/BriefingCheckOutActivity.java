package de.tubs.cs.ibr.fsg.activities;

import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import de.tubs.cs.ibr.fsg.FsgHelper;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class BriefingCheckOutActivity extends NfcEnabledActivity {
	private Nfc nfc;
	private static final String TAG = "purchtagscanact";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_briefing_checkout);
		nfc = new Nfc(this);
	}

	
	@Override
	public void executeNfcAction(Intent intent) {
		try {
			//nfc.resolveIntent(intent);
				System.out.println("Daten lesen");
			nfc.readTag(intent);
			byte[][] data = nfc.getData();
			if (!Arrays.equals(data, null)){
					System.out.println("not null (checkin)");
				NfcObject checkInObject = NfcData.interpretData(data);
					System.out.println("Data interpreted");
				short driverID = checkInObject.getDriverObject().getDriverID();
					System.out.println("DriverID: "+driverID);
				DBAdapter database = new DBAdapter(this);
				database.open();
				if (!database.isTagBlacklisted(nfc.getTagID())){//Tag geblacklistet?
					if (database.getDriver(driverID) != null){ //Wenn der Fahrer in der DB existiert
						database.writeCheckOut(checkInObject.getDriverObject().getDriverID());
					} 
				}
				//if (database.isCheckedIn(driverID)){
					System.out.println("Checked OUT, writing to Tag...");
					nfc.writeTag(intent, NfcData.generateCheckOUT(FsgHelper.generateIdForTodaysBriefing()));

				//}
				database.close();
				Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				Intent mIntent = new Intent(this, BriefingSuccessfulActivity.class);
				mIntent.putExtra("modus", BriefingSuccessfulActivity.CHECK_OUT);
				mIntent.putExtra(NfcAdapter.EXTRA_TAG, tagFromIntent);
				startActivity(mIntent);
			} else {
				throw new FsgException(new Exception(), this.getClass().toString(), FsgException.TAG_EMPTY);
			}
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			Log.e(TAG, e.toString());
			startActivity(mIntent);
		}
		
	}
}
