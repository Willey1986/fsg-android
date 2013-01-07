package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class InfoTerminalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			NfcData.interpretData(NfcData.generateCheckIN((short)2));
			NfcData.interpretData(NfcData.generateCheckOUT((short)2));
		} catch (Exception  e) {
			
		}
		
		super.onCreate(savedInstanceState);
		
		
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
		
}
