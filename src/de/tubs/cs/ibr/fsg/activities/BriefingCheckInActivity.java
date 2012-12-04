package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;
import java.util.List;

import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class BriefingCheckInActivity extends Activity {
	
	private static NfcAdapter mAdapter;
	private static Intent mIntent;
	
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;
	
	private static final String TAG = "purchtagscanact";
	private static final int AUTH = 1;
	private static final int EMPTY_BLOCK_0 = 2;
	private static final int EMPTY_BLOCK_1 = 3;
	private static final int NETWORK = 4;
	// Hex help
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
			(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
			(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

	private Nfc nfc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_briefing_checkin);
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };

		mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };

		mIntent = getIntent();
		nfc = new Nfc(this);

		try {
			//Zum Testen der Fehlermeldung...
			//throw new FsgException( new Exception("'Hier ist die UrsprungException"), this.getClass().toString(), FsgException.TAG_WRONG_KEY );
			nfc.resolveIntent(mIntent);
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		try {
			nfc.resolveIntent(intent);
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}
}
