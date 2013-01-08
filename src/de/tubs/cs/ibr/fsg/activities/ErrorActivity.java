package de.tubs.cs.ibr.fsg.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class ErrorActivity extends Activity {

	private final static String TAG = "ErrorActivity";
	Exception mException;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Intent intent = getIntent();
        FsgException mException = (FsgException)intent.getSerializableExtra("Exception");
        
       	TextView userText;
       	Resources res;
       	switch (mException.getType() ){
    	case FsgException.GENERIC_EXCEPTION:
    		userText = (TextView) findViewById(R.id.textViewWithErrorMessage);
    		res = getResources();
        	userText.setText(res.getString(R.string.error_generic_exception));
    		break;
    	case FsgException.TAG_MEMORY_FULL:
    		userText = (TextView) findViewById(R.id.textViewWithErrorMessage);
    		res = getResources();
        	userText.setText(res.getString(R.string.error_tag_memory_full));
    		break;
    	case FsgException.TAG_WRONG_KEY:
    		userText = (TextView) findViewById(R.id.textViewWithErrorMessage);
    		res = getResources();
        	userText.setText(res.getString(R.string.error_tag_wrong_key));
    		break;
    	case FsgException.NOT_NFC_SUPPORT:
    		userText = (TextView) findViewById(R.id.textViewWithErrorMessage);
    		res = getResources();
        	userText.setText(res.getString(R.string.error_not_nfc_support));
    		break;
    	case FsgException.DRIVER_ALREADY_CHECKED_IN:
    		userText = (TextView) findViewById(R.id.textViewWithErrorMessage);
    		userText.setText(R.string.error_driver_already_checked_in);
    		break;
    	case FsgException.END_OF_ROAD:
    		userText = (TextView) findViewById(R.id.textViewWithErrorMessage);
    		res = getResources();
        	userText.setText(res.getString(R.string.error_end_of_road));
    		break;

    	}
       	Log.i(TAG, "activity created.");
    }


    
    public void onButtonClick(View view){
		startActivity(new  Intent(this, MainActivity.class));
    }
 
    
}


