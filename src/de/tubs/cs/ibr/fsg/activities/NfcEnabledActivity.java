package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.exceptions.FsgException;
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
			if (nfcAdapter != null) {
				pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
				ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
				ndef.addDataType("*/*");
				filters = new IntentFilter[] {ndef,};
				techLists = new String[][] { new String[] { MifareClassic.class.getName() } };
			}else{
				throw new FsgException( new NullPointerException("'Der NfcAdapter ist ein \"null\"-Objekt. Grund: Keine NFC-Unterstützung auf dem Gerät"), this.getClass().toString(), FsgException.NOT_NFC_SUPPORT );
			}
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail",e);
		}  catch (FsgException e1) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e1);
			startActivity(mIntent);
			finish();
		} 
	}
	
	public void onNewIntent(Intent intent) {
		setIntent(intent);
    	resolveIntent(intent);
    }
	
	public void resolveIntent(Intent intent) {
		if (nfcAdapter != null) {
			String action = intent.getAction();
			if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
				executeNfcAction(intent);
	        }	
		}
		
	}
	
	abstract public void executeNfcAction(Intent intent);

	public void onPause() {
		try{
			if (nfcAdapter != null){
				nfcAdapter.disableForegroundDispatch(this);
			}else{
				throw new FsgException( new NullPointerException("'Der NfcAdapter ist ein \"null\"-Objekt. Grund: Keine NFC-Unterstützung auf dem Gerät"), this.getClass().toString(), FsgException.NOT_NFC_SUPPORT );
			}
		}catch (FsgException e1){
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e1);
			startActivity(mIntent);
			finish();
		}
		super.onPause();

	}
	
	public void onResume() {
		try{
			if (nfcAdapter != null){
				nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techLists);
			}else{
				throw new FsgException( new NullPointerException("'Der NfcAdapter ist ein \"null\"-Objekt. Grund: Keine NFC-Unterstützung auf dem Gerät"), this.getClass().toString(), FsgException.NOT_NFC_SUPPORT );
			}
		}catch (FsgException e1){
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e1);
			startActivity(mIntent);
			finish();
		}
		super.onResume();

	}
}
