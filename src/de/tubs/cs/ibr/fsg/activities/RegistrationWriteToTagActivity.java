package de.tubs.cs.ibr.fsg.activities;

import java.io.IOException;

import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.SecurityManager;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class RegistrationWriteToTagActivity extends NfcEnabledActivity {
	
	private SecurityManager scm;
	private TextView txtInfo, txtStatus;
	private Nfc nfc;
	private Driver driver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_write_to_tag);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
	        		driver.getFirst_name() + " " + driver.getLast_name() +
	        		"\n\nCodiert:\n" + encodedString +
	        		"\n\nVerschlüsselt:\n" + encryptedString + 
	        		"\n\nEntschlüsselt:\n" + decryptedString + 
	        		"\n\nAus ByteArray generierter Fahrer:\n" + nfcContent.DriverObject.toString();
	        txtInfo.setText(infoText);
	        

			
			
		} catch (FsgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(this.getClass().getName(), e.getOriginException().getMessage());
			System.out.println("Bubu");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	@Override
	public void executeNfcAction() {
		// TODO Auto-generated method stub
		txtStatus.setText("Band gefunden");
	}
    
    
}
