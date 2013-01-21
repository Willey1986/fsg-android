package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class RunActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
	}
	
    /**
     * 
     * @param view
     */
    public void onButtonClick(View view){
    	
    	Intent intent;
    	
    	Button b = (Button)view;
    	
    	
		intent = new Intent(this, RunActivityConfirm.class);
		
		intent.putExtra("ActivityName",b.getText());
		
		startActivity(intent);
    	
    	
    }
    
}
