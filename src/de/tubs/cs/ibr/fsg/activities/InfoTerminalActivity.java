package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.db.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InfoTerminalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_terminal);

		DBAdapter test = new DBAdapter(this);
		
		try {
			//NfcData.interpretData()
			NfcData.generateDataRegistration(test.getDriver((short)81));
		} catch (FsgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
