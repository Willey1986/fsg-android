package de.tubs.cs.ibr.fsg.activities;

import android.content.Intent;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
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
			
			////////////////////////////////////////////////////////////////
			// Block nur fuer die Fehlersuche...
			StringBuffer encryptedString = new StringBuffer();
			for(int i = 0; i < readedTagContent.length; i++) {
				for(int j = 0; j < readedTagContent[i].length; j++) {
					encryptedString.append(readedTagContent[i][j]);
				}
			}
			System.out.println("Verschlüsselt: " + encryptedString);
			////////////////////////////////////////////////////////////////
			
			byte[][] decryptedContent = scm.decryptString( readedTagContent ); // Hier versuchen wir zu entschluesseln // TODO Hier steigt die APP aus!
			
			NfcObject tagContent = NfcData.interpretData( decryptedContent ); // Ohne Verschluesselung koennen wir nun an die Daten
			
			System.out.println(tagContent); // Nur fuer die Fehlersuche da, hier kann ich beim debuggen stoppen... ;-)

		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		}
		
	}


		
}
