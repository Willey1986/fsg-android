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
			
			byte[][] decryptedContent = scm.decryptString( readedTagContent ); // Hier versuchen wir zu entschluesseln // TODO Hier steigt die APP aus!
			
			NfcObject tagContent = NfcData.interpretData( decryptedContent ); // Ohne Verschluesselung koennen wir nun an die Daten
			
//			if (tagContent.getDriverObject().getTeam_id() == 0) {
//				nfc.writeTag(intent, contentToWrite);
//				txtStatus.setText("Registrierungsdaten geschrieben");
//			} else {
//				FsgException e = new FsgException(null, "RegistrationWriteToTagActivity", FsgException.REGISTRATION_ALREADY_PRESENT);
//				throw e;
//			}
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		}
		
	}


		
}
