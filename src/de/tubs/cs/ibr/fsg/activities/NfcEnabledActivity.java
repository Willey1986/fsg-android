package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;

abstract public class NfcEnabledActivity extends Activity{
	
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private IntentFilter ndef;
	private IntentFilter[] filters;
	private String[][] techLists;
	
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			nfcAdapter = NfcAdapter.getDefaultAdapter(this);
			pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
			ndef.addDataType("*/*");
			filters = new IntentFilter[] {ndef,};
			techLists = new String[][] { new String[] { MifareClassic.class.getName() } };
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail",e);
		} 
	}
	
	public void onNewIntent(Intent intent) {
		setIntent(intent);
    	resolveIntent(intent);
    }
	
	public void resolveIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			executeNfcAction();
        }	
	}
	
	abstract public void executeNfcAction();

	public void onPause() {
		nfcAdapter.disableForegroundDispatch(this);
		super.onPause();
	}
	
	public void onResume() {
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techLists);
		super.onResume();
	}
}
