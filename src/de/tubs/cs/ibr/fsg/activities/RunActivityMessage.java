package de.tubs.cs.ibr.fsg.activities;

import java.util.Timer;
import java.util.TimerTask;

import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RunActivityMessage extends  NfcEnabledActivity{
	
	boolean isAllowedToScan;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_message);
		
		this.isAllowedToScan = false;

		boolean isShowingError = getIntent().getBooleanExtra("showError",false);
		
		//set running title
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(getIntent().getStringExtra("title"));
		

		TextView headTitle = (TextView) findViewById(R.id.headTitle);
		
		//make the background red
		if(isShowingError){

			  // Now get a handle to any View contained 
			  // within the main layout you are using
			  View someView = findViewById(R.id.view);

			  // Set the color
			  //someView.setBackgroundColor(Color.RED);
			  someView.setBackgroundDrawable( getResources().getDrawable(R.drawable.background_red) );

			  headTitle.setText("Genehmigung fehlgeschlagen:");
			  

			  TextView textview = (TextView) findViewById(R.id.textView);
			  textview.setText(getIntent().getStringExtra("message"));
			  
		}else{
			//green background

			  // Now get a handle to any View contained 
			  // within the main layout you are using
			  View someView = findViewById(R.id.view);

			  // Set the color
			  //someView.setBackgroundColor(Color.GREEN);
			  someView.setBackgroundDrawable( getResources().getDrawable(R.drawable.background_green) );
			  
			  headTitle.setText("Erfolgreich geloggt:");
			  
			  
			  //hide the buttons
			  Button b1 = (Button) findViewById(R.id.allowButton);
			  b1.setVisibility(View.INVISIBLE);
			  
			  Button b2 = (Button) findViewById(R.id.retryButton);
			  b2.setVisibility(View.INVISIBLE);
			  

			  headTitle.setTextColor(Color.BLACK);
			  
			  TextView textview = (TextView) findViewById(R.id.textView);
			  textview.setTextColor(Color.BLACK);
			  
			  textview.setText(getIntent().getStringExtra("posMessage"));
			  
			  
			  //close the message after 3 seconds
			  new Timer().schedule(new TimerTask() {          
				    @Override
				    public void run() {
				        // this code will be executed after 3 seconds
				    	goToRunActivity();
				    }
				}, 5000);
		}
		
		
		
		
		
	}
	
	
	public void retryClick(View view){
		finish();
	}
	
	
	/** Try to allow even on error
	 * 
	 * @param view
	 */
	public void allowClick(View view){
		
		this.isAllowedToScan = true;
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Fahrer trotzdem genehmigen");
		alertDialog.setMessage("Bitte Band erneut dranhalten...");
		alertDialog.show();

	}
	
	void goToRunActivity(){
		startActivity(new  Intent(this, RunActivity.class));
    	finish();
	}


	@Override
	public void executeNfcAction(Intent intent) {
		// TODO Auto-generated method stub
		if(isAllowedToScan){
			try{
				Nfc nfc = new Nfc(this);
				
				short runID = getIntent().getShortExtra("runID", (short)0);
				byte[][]run = NfcData.generateRun(runID);
				
				nfc.writeTag(getIntent(),run);
				
				//write one more time
				if(getIntent().getIntExtra("runTurns", 0)==2){
					nfc.writeTag(getIntent(),run);
				}
			}catch(FsgException e){
				Intent mIntent = new Intent(this, ErrorActivity.class);
    			mIntent.putExtra("Exception", e);
    			startActivity(mIntent);
    			finish();
			}
			
			startActivity(new  Intent(this, MainActivity.class));
	    	finish();
		}
	}
}
