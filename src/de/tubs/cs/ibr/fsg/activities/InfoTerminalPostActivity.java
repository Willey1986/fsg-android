package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class InfoTerminalPostActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_terminal_post);
        
        Intent mIntent = getIntent();
        NfcObject mNfcObject = mIntent.getParcelableExtra("nfc_object");
        System.out.println(mNfcObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_info_terminal_post, menu);
        return true;
    }
}
