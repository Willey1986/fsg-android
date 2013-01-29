package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class AdminInvalidateTagActivity extends NfcEnabledActivity{

	private DBAdapter dba;
	private Nfc nfc;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_invalidate_tag);
		dba = new DBAdapter(this);
		nfc = new Nfc(this);
		
		dba.open();
	}

	@Override
	public void executeNfcAction(Intent intent) {
		try {
			nfc.readTag(intent);
			String tagID = nfc.getTagID(); 
			byte[][] contentToWrite = NfcData.generateDataDestroyCompleteTag();
			nfc.writeTag(intent, contentToWrite);
			dba.writeBlacklistedTagToDB(tagID);
		} catch (FsgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	protected void onStop() {
		super.onStop();
		dba.close();
	}
	
	public void onResume() {
		super.onResume();
		dba.open();
	}

}
