package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    	
    	switch (view.getId() ){
    	case R.id.button1:

    		intent = new Intent(this, RunActivityConfirm.class);
			
    		intent.putExtra("ActivityName","Acceleration");
			
    		startActivity(intent);
    		
    		break;
    	case R.id.button2:
    		try {
    			//Zum Testen der Fehlermeldung...
    			throw new FsgException( new Exception("'Hier ist die UrsprungException"), this.getClass().toString(), FsgException.END_OF_ROAD );

    		} catch (FsgException e) {
    			Intent mIntent = new Intent(this, ErrorActivity.class);
    			mIntent.putExtra("Exception", e);
    			startActivity(mIntent);
    			finish();
    		}
    		break;
    	case R.id.button3:
    		try {
    			//Zum Testen der Fehlermeldung...
    			throw new FsgException( new Exception("'Hier ist die UrsprungException"), this.getClass().toString(), FsgException.END_OF_ROAD );

    		} catch (FsgException e) {
    			Intent mIntent = new Intent(this, ErrorActivity.class);
    			mIntent.putExtra("Exception", e);
    			startActivity(mIntent);
    			finish();
    		}
    		break;
    	case R.id.button4:
    		try {
    			//Zum Testen der Fehlermeldung...
    			throw new FsgException( new Exception("'Hier ist die UrsprungException"), this.getClass().toString(), FsgException.END_OF_ROAD );

    		} catch (FsgException e) {
    			Intent mIntent = new Intent(this, ErrorActivity.class);
    			mIntent.putExtra("Exception", e);
    			startActivity(mIntent);
    			finish();
    		}
    		break;

    	}
    }
    
}
