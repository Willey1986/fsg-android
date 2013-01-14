package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

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
		try {

			setContentView(R.layout.activity_briefing_checkin);
		
			mAdapter = NfcAdapter.getDefaultAdapter(this);
			if(mAdapter==null){
				throw new FsgException( new NullPointerException("'Der NfcAdapter ist ein \"null\"-Objekt. Grund: Keine NFC-Unterst�tzung auf dem Ger�t"), this.getClass().toString(), FsgException.NOT_NFC_SUPPORT );
			}
			mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);


			ndef.addDataType("*/*");

			mFilters = new IntentFilter[] { ndef, };

			mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };

			mIntent = getIntent();
			nfc = new Nfc(this);


			nfc.resolveIntent(mIntent);
			//nfc.writeTag(mIntent, MifareClassic.KEY_DEFAULT, NfcData.generateDataRegistration());
		} catch (FsgException e1) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e1);
			startActivity(mIntent);
			finish();
		} catch (MalformedMimeTypeException e2) {
			FsgException mException =  new FsgException( e2, this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", mException);
			startActivity(mIntent);
			finish();
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		try{
			if(mAdapter==null){
				throw new FsgException( new NullPointerException("'Der NfcAdapter ist ein \"null\"-Objekt. Grund: Keine NFC-Unterst�tzung auf dem Ger�t"), this.getClass().toString(), FsgException.NOT_NFC_SUPPORT );
			}else{
				mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
			}
			
		}catch (FsgException e1){
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e1);
			startActivity(mIntent);
			finish();
		}catch (Exception e2){
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e2);
			startActivity(mIntent);
			finish();
		}
		super.onResume();
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		try {
			nfc.resolveIntent(intent);
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			Log.e(TAG, e.toString());
			startActivity(mIntent);
		}
	}

	@Override
	public void onPause() {	
		try{
			if(mAdapter==null){
				throw new FsgException( new NullPointerException("'Der NfcAdapter ist ein \"null\"-Objekt. Grund: Keine NFC-Unterst�tzung auf dem Ger�t"), this.getClass().toString(), FsgException.NOT_NFC_SUPPORT );
			}else{
				mAdapter.disableForegroundDispatch(this);
			}
			
		}catch (FsgException e1){
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e1);
			startActivity(mIntent);
			finish();
		}catch (Exception e2){
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e2);
			startActivity(mIntent);
			finish();
		}
		super.onPause();
		
	}
}
