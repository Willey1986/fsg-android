package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Bundle;
import de.tubs.cs.ibr.fsg.R;

public class InfoTerminalPostActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_terminal_post);
        
        Intent mIntent = getIntent();
        
        
		NfcOperation mNfcOperation = new NfcOperation(this);
		mNfcOperation.execute(mIntent);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean loop = true;
			while(tag.isConnected()){
				//wait...Do Nothing...
			}

	        return null;
		}

		@Override
		protected void onPostExecute(String mString) {
			mInfoTerminalPostActivity.onBackPressed();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

		}
	}
    
    
    
    
    
    
    
    
    
    
    


}
