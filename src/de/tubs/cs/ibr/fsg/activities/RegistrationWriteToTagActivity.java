package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.SecurityManager;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class RegistrationWriteToTagActivity extends NfcEnabledActivity {
	
	private SecurityManager scm;
	private TextView txtInfo, txtStatus;
	private Nfc nfc;
	private Driver driver;
	private byte[][] contentToWrite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_write_to_tag);
        Bundle extras = getIntent().getBundleExtra("bundle");
        driver = (Driver) extras.get("driver");
        txtInfo = (TextView) findViewById(R.id.txtRegWriteInfo);
        txtStatus = (TextView) findViewById(R.id.txtRegWriteStatus);
        nfc = new Nfc(this);
        scm = new SecurityManager("geheim");

        
        try {
        	byte[][] encodedDriver = NfcData.generateDataRegistration(driver);
			StringBuffer encodedString = new StringBuffer();
			for(int i = 0; i < encodedDriver.length; i++) {
				for(int j=0; j<encodedDriver[i].length; j++) {
					encodedString.append(encodedDriver[i][j]);
				}
			}
			
			byte[][] encryptedDriver = scm.encryptString(encodedDriver);
			StringBuffer encryptedString = new StringBuffer();
			for(int i = 0; i < encryptedDriver.length; i++) {
				for(int j = 0; j < encryptedDriver[i].length; j++) {
					encryptedString.append(encryptedDriver[i][j]);
				}
			}
			
			byte[][] decryptedDriver = scm.decryptString(encryptedDriver);
			StringBuffer decryptedString = new StringBuffer();
			for(int i = 0; i < decryptedDriver.length; i++) {
				for(int j = 0; j < decryptedDriver[i].length; j++) {
					decryptedString.append(decryptedDriver[i][j]);
				}
			}
			
			NfcObject nfcContent = NfcData.interpretData(decryptedDriver);
			
			String infoText = "Folgender Fahrer wird aufs Band geschrieben:\n" +
	        		driver.toString() +
	        		"\n\nCodiert:\n" + encodedString +
	        		"\n\nVerschlüsselt:\n" + encryptedString +
	        		"\n\nEntschlüsselt:\n" + decryptedString + 
	        		"\n\nDecodiert:\n" + nfcContent.getDriverObject().toString();
	        txtInfo.setText(infoText);
	        
	        

			contentToWrite = encodedDriver; //Wenn die Verschluesselung funktioniert, muss hier heissen: contentToWrite = encryptedDriver;
			
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		} catch (IOException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registration_write_to_tag, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void executeNfcAction(Intent intent) {
		txtStatus.setText("Band gefunden");
		try {
			//nfc.readTag(intent);
			NfcObject tagContent = NfcData.interpretData(nfc.getData());
			if (tagContent.getDriverObject().getTeamID() == 0) {
				nfc.writeTag(intent, contentToWrite);
				txtStatus.setText("Registrierungsdaten geschrieben");
			} else {
				FsgException e = new FsgException(null, "RegistrationWriteToTagActivity", FsgException.REGISTRATION_ALREADY_PRESENT);
				throw e;
			}
			onBackPressed();
		} catch (FsgException e) {
			Intent mIntent = new Intent(this, ErrorActivity.class);
			mIntent.putExtra("Exception", e);
			startActivity(mIntent);
			finish();
		}
	}
    
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);  
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);  
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }
    
}
