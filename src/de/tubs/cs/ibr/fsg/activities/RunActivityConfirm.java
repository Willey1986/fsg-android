package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.Nfc;
import de.tubs.cs.ibr.fsg.NfcData;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RunActivityConfirm extends NfcEnabledActivity {

	private CheckBox check1;
	private CheckBox check2;
	
	private boolean isWaiting;


	private Nfc nfc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_confirm);
		
		String title = getIntent().getStringExtra("ActivityName");
		
		TextView titleLabel = (TextView) findViewById(R.id.runTitle);
		titleLabel.setText(title);
		
		check1 = (CheckBox) findViewById(R.id.check1);
		check2 = (CheckBox) findViewById(R.id.check2);

		Button scanB = (Button) findViewById(R.id.scanButton);
		scanB.setEnabled(false);
		
		//init waiting is false
		this.isWaiting = false;
		
		//init an nfc instance
		nfc = new Nfc(this);
	}
	
	
	private void shouldShowWait(boolean showWaiting){

		
		
		TextView text1 = (TextView) findViewById(R.id.textView1);
		TextView text2 = (TextView) findViewById(R.id.textView2);
		
		
		//should show the waiting information
		int isVisible = View.GONE;
		
		//show checkbox
		int checkBoxIsVisible = View.GONE;
		
		if(showWaiting){
			text1.setText("Initialisierung");
			
			isVisible = View.VISIBLE;
			
			checkBoxIsVisible = View.INVISIBLE;
		}else{

			text1.setText("Wie viele Rennen sollen geloggt werden?");
			isVisible = View.INVISIBLE;

			checkBoxIsVisible = View.VISIBLE;
		}
		this.isWaiting = showWaiting;
		
		text2.setVisibility(isVisible);
		ProgressBar progress = (ProgressBar) findViewById(R.id.progressbar);
		progress.setVisibility(isVisible);
		
		check1.setVisibility(checkBoxIsVisible);
		check2.setVisibility(checkBoxIsVisible);
		
		
	}
	
    /**
     * 
     * @param view
     */
    public void onButtonClick(View view){
    	switch (view.getId() ){
    		case R.id.scanButton:
    		
    			//user pressed scan button
    			
    			this.shouldShowWait(!this.isWaiting);
    			
    			//user should wait
    			if(this.isWaiting){
    				Button scanB = (Button) findViewById(R.id.scanButton);
    				scanB.setText("Abbruch");
    			}else{
    				Button scanB = (Button) findViewById(R.id.scanButton);
    				scanB.setText("Scan");
    			}
    			
    			
    			break;
    		case R.id.check1:
    			
    			//user pressed 1 checkbox
    			
    			//unselect the other checkbox
    			if(check1.isChecked()&&check2.isChecked()){
    				check2.setChecked(false);
    			}
    			

				Button scanB1 = (Button) findViewById(R.id.scanButton);
				
    			//nothing is checked
    			if(!check1.isChecked()&&!check2.isChecked()){
    				scanB1.setEnabled(false);
    			}else{

    				scanB1.setEnabled(true);
    			}
    			
    			break;
    			
    		case R.id.check2:
    			
    			//user pressed 2 checkbox
    			
    			//unselect the other checkbox
    			if(check1.isChecked()&&check2.isChecked()){
    				check1.setChecked(false);
    			}


				Button scanB2 = (Button) findViewById(R.id.scanButton);
				
    			//nothing is checked
    			if(!check1.isChecked()&&!check2.isChecked()){
    				scanB2.setEnabled(false);
    			}else{

    				scanB2.setEnabled(true);
    			}
    			
    			break;
    			
    	}
    }


	@Override
	public void executeNfcAction(Intent intent) {
		// TODO Auto-generated method stub
		if(!this.isWaiting){
			//do nothing
			return;
		}else{
			//read the card
			
			Intent mIntent = new Intent(this, RunActivityMessage.class);
			
			try {
				nfc.readTag(intent);
				NfcObject tagContent = NfcData.interpretData(nfc.getData());
				
				mIntent.putExtra("showError",false);
				
				Driver driver = tagContent.getDriverObject();
				
				if(driver.getDriverID() == 0){
					mIntent.putExtra("message","Band ist ungültig. Kein Fahrerdaten vorhanden!");
					throw new FsgException( new Exception("Invalid Driver"), 
							this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
				}else{

					TextView titleLabel = (TextView) findViewById(R.id.runTitle);
					String message = "Fahrer "+driver.getLastName()+", "+driver.getFirstName()
							+", mit ID " + driver.getDriverID() + " wurde die Genehmigung für Rennen "
							+ titleLabel.getText() + " erteilt.";
					mIntent.putExtra("posMessage",message);
					
				}
				
				
			} catch (FsgException e) {
				
				//error type
				mIntent.putExtra("showError",true);
				
			}finally{
				//run title
				TextView titleLabel = (TextView) findViewById(R.id.runTitle);
				mIntent.putExtra("runTitle",titleLabel.getText());
				startActivity(mIntent);
			}
			
			
		}
	}
}
