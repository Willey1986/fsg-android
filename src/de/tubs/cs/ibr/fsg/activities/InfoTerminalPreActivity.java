package de.tubs.cs.ibr.fsg.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.SecurityManager;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class InfoTerminalPreActivity extends NfcEnabledActivity {

	private Nfc nfc;
	private SecurityManager scm;

	@Override
	public void executeNfcAction(Intent intent) {
		TextView msgToUser = (TextView) findViewById(R.id.infoPreNearTextView);
		Resources res = getResources();
		msgToUser.setText(res.getString(R.string.infoTerminalReading));

		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		intent.putExtra(NfcAdapter.EXTRA_TAG, tagFromIntent);
			
			
		NfcOperation mNfcOperation = new NfcOperation(this, msgToUser);
		mNfcOperation.execute(intent);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_terminal_pre);
	}

	private class NfcOperation extends AsyncTask<Intent, Integer, NfcObject> {

		Intent mIntent;
		Context context;
		TextView msgToUser;

		public NfcOperation(Context context, TextView msgToUser) {
			this.context = context;
			this.msgToUser = msgToUser;
		}

		@Override
		protected NfcObject doInBackground(Intent... params) {

			NfcObject mNfcObject = null;
			try {
				this.mIntent = params[0];
				scm = new SecurityManager("geheim");
				nfc = new Nfc(context);
				nfc.readTag(mIntent);
				byte[][] readedTagContent = nfc.getData(); // Der gelesene Inhalt ist an dieser Stelle noch verschluesselt.


				// ////////////////////////////////////////////////////////////////
				// // Block nur fuer die Fehlersuche...
				// StringBuffer encryptedString = new StringBuffer();
				// for(int i = 0; i < readedTagContent.length; i++) {
				// for(int j = 0; j < readedTagContent[i].length; j++) {
				// encryptedString.append(readedTagContent[i][j]);
				// }
				// }
				// System.out.println("Verschlüsselt: " + encryptedString);
				// ////////////////////////////////////////////////////////////////
				//
				//byte[][] decryptedContent = scm.decryptString( readedTagContent ); // Hier versuchen wir zu entschluesseln // TODO Hier steigt die APP aus!

				mNfcObject = NfcData.interpretData( readedTagContent ); // Ohne Verschluesselung koennen wir nun an die Daten


				System.out.println(mNfcObject); // Nur fuer die Fehlersuche da, hier kann ich beim debuggen stoppen... ;-)

				return mNfcObject;

			} catch (FsgException e) {
				publishProgress(1);
			}
			return mNfcObject;
		}

		@Override
		protected void onPostExecute(NfcObject mNfcObject) {
			if (mNfcObject == null) {
				// Do nothing!
			} else {
		        Tag tagFromIntent = this.mIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				Intent mIntent = new Intent(context, InfoTerminalPostActivity.class);
				mIntent.putExtra("nfc_object", mNfcObject);
				mIntent.putExtra(NfcAdapter.EXTRA_TAG, tagFromIntent);
				startActivity(mIntent);
			}
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (values[0] == 1) {
				Resources res = getResources();
				this.msgToUser.setText(res.getString(R.string.wristband_near));
			}
		}
	}

}
