package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class InfoTerminalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_terminal);		
		
		ListView listView1 = (ListView) findViewById (R.id.listView1);
		
		String[] items = { "Benutzername" , "ID",};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
		listView1.setAdapter(adapter);
		

		ListView listView2 = (ListView) findViewById (R.id.listView2);
		
		String[] items1 = { "Rennen1" , "Rennen2", "Rennen3"};
		
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items1);
		listView2.setAdapter(adapter1);	
		
		ListView listView3 = (ListView) findViewById (R.id.listView3);
		
		String[] items11 = { "Briefingstatus"};
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items11);
		listView3.setAdapter(adapter2);			

	}
		
}

