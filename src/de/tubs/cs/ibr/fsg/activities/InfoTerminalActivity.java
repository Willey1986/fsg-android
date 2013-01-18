package de.tubs.cs.ibr.fsg.activities;

import android.content.Intent;
import android.os.Bundle;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.SecurityManager;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class InfoTerminalActivity extends NfcEnabledActivity {

	private Nfc nfc;
	private SecurityManager scm;
	
	@Override
	public void executeNfcAction(Intent intent) {
		try {
			scm = new SecurityManager("geheim");
			nfc = new Nfc(this);
			nfc.readTag(intent);
			byte[][] readedTagContent = nfc.getData();  // Der gelesene Inhalt ist an dieser Stelle noch verschluesselt.
			
//			////////////////////////////////////////////////////////////////
//			// Block nur fuer die Fehlersuche...
//			StringBuffer encryptedString = new StringBuffer();
//			for(int i = 0; i < readedTagContent.length; i++) {
//				for(int j = 0; j < readedTagContent[i].length; j++) {
//					encryptedString.append(readedTagContent[i][j]);
//				}
//			}
//			System.out.println("Verschlüsselt: " + encryptedString);
//			////////////////////////////////////////////////////////////////
//			
			//byte[][] decryptedContent = scm.decryptString( readedTagContent ); // Hier versuchen wir zu entschluesseln // TODO Hier steigt die APP aus!
			
			NfcObject mNfcObject = NfcData.interpretData( readedTagContent ); // Ohne Verschluesselung koennen wir nun an die Daten
			
			System.out.println(mNfcObject); // Nur fuer die Fehlersuche da, hier kann ich beim debuggen stoppen... ;-)

		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		}
		


		
		
/*
		setContentView(R.layout.activity_info_terminal);
		
		DBAdapter test = new DBAdapter(this);
		Driver driver = test.getDriver((short)81);
		
		ListView listView1 = (ListView) findViewById (R.id.listView1);
		
		String[] items = { driver.getFirst_name() , driver.getLast_name(),};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
		listView1.setAdapter(adapter);
		

		ListView listView2 = (ListView) findViewById (R.id.listView2);
		
		String[] items1 = { "Rennen1" , "Rennen2", "Rennen3"};
		
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items1);
		listView2.setAdapter(adapter1);	
		
		ListView listView3 = (ListView) findViewById (R.id.listView3);
		
		String[] items11 = { "Briefingstatus"};
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items11);
		listView3.setAdapter(adapter2);		*/


	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_terminal);
	}
	
	


		
}
