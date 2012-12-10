package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class InfoTerminalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_terminal);

		
		try {
			//NfcData.interpretData()
			DBAdapter test = new DBAdapter(this); // <--- Hier kracht es aktuell...TODO TODO TODO :-)
			NfcData.generateDataRegistration(test.getDriver((short)81));
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		} catch (Exception e) {
			FsgException mException = new FsgException( e, this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", mException);
			startActivity(mIntent);
			finish();
		} 

	}
	
}
