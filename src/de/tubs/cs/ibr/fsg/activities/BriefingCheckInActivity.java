package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;
import java.util.List;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.nfc.TagReading;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_briefing_checkin);
		//TagReading.readTag(this, this.getIntent());
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };

		mTechLists = new String[][] { new String[] { IsoDep.class.getName() } };

		mIntent = getIntent();
		resolveIntent(mIntent);
	}
	
	void resolveIntent(Intent intent) {
		// Parse the intent
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			System.out.println("Discovered tag with intent: " + intent);
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			System.out.println(toString(tagFromIntent.getTechList()));
			if (toString(tagFromIntent.getTechList()).contains("IsoDep")){
				//Unterscheiden zwischen den verschiedenen Technologien
				//Immer die "höchste"/"beste" verwenden und auslesen
			
				IsoDep tag = IsoDep.get(tagFromIntent);
				System.out.println(tag.getTag().toString().contains("android"));
				byte[] data;
				try {
					tag.connect();
					String cardData = null;
					//data = tag.getHistoricalBytes();
					byte [] request = new byte [] {(byte)0x00,(byte)0xB2,(byte)0x01,(byte)0x0C,(byte)0x00};
					//byte [] request = new byte [] {(byte)0x00,(byte)0x0A};
					byte[] command = new byte[]{
							  (byte)0x00, /* CLA = 00 (first interindustry command set) */
							  (byte)0xA4, /* INS = A4 (SELECT) */
							  (byte)0x04, /* P1  = 04 (select file by DF name) */
							  (byte)0x0C, /* P2  = 0C (first or only file; no FCI) */
							  (byte)0x07, /* Lc  = 7  (data/AID has 7 bytes) */
							  /* AID = A0000002471001: */
							  (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x02,
							  (byte)0x47, (byte)0x10, (byte)0x01
							};
					data = tag.transceive(command);
					cardData = getHexString(data, data.length);

					if (cardData != null) {						
						showAlert(cardData);
					} else {
						showAlert(EMPTY_BLOCK_0);
					}
				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage());
					showAlert(NETWORK);
				}
			}
		} else {
			System.out.println("Online + Scan a tag");
		}
	}

	private void showAlert(int alertCase) {
		// prepare the alert box
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		switch (alertCase) {

		case AUTH:// Card Authentication Error
			alertbox.setMessage("Authentication Failed on Block 0");
			break;
		case EMPTY_BLOCK_0: // Block 0 Empty
			alertbox.setMessage("Failed reading Block 0");
			break;
		case EMPTY_BLOCK_1:// Block 1 Empty
			alertbox.setMessage("Failed reading Block 1");
			break;
		case NETWORK: // Communication Error
			alertbox.setMessage("Tag reading error");
			break;
		}
		// set a positive/yes button and create a listener
		alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			// Save the data from the UI to the database - already done
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		// display box
		alertbox.show();

	}
	
	private void showAlert(String sAlert) {
		// prepare the alert box
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage(sAlert);
		// set a positive/yes button and create a listener
		alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			// Save the data from the UI to the database - already done
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		// display box
		alertbox.show();

	}

	public static String getHexString(byte[] raw, int len) {
		byte[] hex = new byte[2 * len];
		int index = 0;
		int pos = 0;

		for (byte b : raw) {
			if (pos >= len)
				break;

			pos++;
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}

		return new String(hex);
	}

	@Override
	public void onResume() {
		super.onResume();
		//mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);
		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		resolveIntent(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}
	
	public String toString(String[] stringArray) {
		String string = new String();
		for (int i=0; i < stringArray.length; i++){
			string += stringArray[i]+" ";
		}
	    return string;
	}
}
