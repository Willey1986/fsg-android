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
    	case R.id.buttonCopyTag:
    		//startActivity(new Intent(this, CopyTagActivity.class));
    		break;
    	case R.id.buttonInvalidateTag:
    		//startActivity(new Intent(this, InvalidateTagActivity.class));
    		break;
    	case R.id.buttonInvalidateBlock:
    		//startActivity(new Intent(this, InvalidateBlockActivity.class));
    		break;
    	case R.id.buttonConfiguration:
    		//startActivity(new Intent(this, ConfigurationActivity.class));
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
