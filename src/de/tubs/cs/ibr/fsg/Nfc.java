/* 		
	import de.tubs.cs.ibr.fsg.Nfc;
	Nfc test = new Nfc(this.getApplicationContext());
	mAdapter = test.getAdapter();
 * 
 * 
 * 
 */

package de.tubs.cs.ibr.fsg;

import java.io.IOException;
import java.util.List;

import de.tubs.cs.ibr.fsg.R;
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

public class Nfc {

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

	
	public Nfc(Context context){
		mAdapter = NfcAdapter.getDefaultAdapter(context);
		mPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };

		mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };

		//mIntent = getIntent();
	}
	
	public NfcAdapter getAdapter(){
		return mAdapter;
	}
	
	public PendingIntent getPendingIntent(){
		return mPendingIntent;
	}
}