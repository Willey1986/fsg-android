package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AdminActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }
    
    public void onButtonClick(View view){
    	switch (view.getId() ){
    	case R.id.button6:
    		startActivity(new Intent(this, TransferActivity.class));
    		break;
    	case R.id.button7:
    		startActivity(new Intent(this, DeleteActivity.class));
    		break;
    	case R.id.button8:
    		startActivity(new Intent(this, DeleteOneActivity.class));
    		break;
    		
    	}
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_admin, menu);
        return true;
    }        
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected (item);
    	switch(item.getItemId()){
    	case R.id.logout:
       		startActivity(new Intent(this, MainActivity.class));
    		break;
    	}
    	
    	return true;
    	
    	
    }
        
}
