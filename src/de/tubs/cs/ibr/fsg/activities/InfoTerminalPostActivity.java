package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;

public class InfoTerminalPostActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_terminal_post);
        
        Intent mIntent = getIntent();
        
        
        Tag tagFromIntent = mIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		MifareClassic tag = MifareClassic.get(tagFromIntent);
		try {
			tag.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Ist noch dran: " + tag.isConnected());
        
        
        NfcObject mNfcObject = mIntent.getParcelableExtra("nfc_object");
        System.out.println(mNfcObject);
    }


}
