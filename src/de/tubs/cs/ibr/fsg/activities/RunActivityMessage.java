package de.tubs.cs.ibr.fsg.activities;

import java.util.Timer;
import java.util.TimerTask;

import de.tubs.cs.ibr.fsg.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RunActivityMessage extends  Activity{
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_message);
		
		

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
			  someView.setBackgroundColor(Color.RED);

			  headTitle.setText("Genehming fehlgeschlagen:");
			  

			  TextView textview = (TextView) findViewById(R.id.textView);
			  textview.setText(getIntent().getStringExtra("message"));
			  
		}else{
			//green background

			  // Now get a handle to any View contained 
			  // within the main layout you are using
			  View someView = findViewById(R.id.view);

			  // Set the color
			  someView.setBackgroundColor(Color.GREEN);
			  
			  headTitle.setText("Erfolgreich geloggt:");
			  
			  
			  //hide the buttons
			  Button b1 = (Button) findViewById(R.id.allowButton);
			  b1.setVisibility(View.INVISIBLE);
			  
			  Button b2 = (Button) findViewById(R.id.retryButton);
			  b2.setVisibility(View.INVISIBLE);
			  

			  headTitle.setTextColor(Color.BLACK);
			  
			  TextView textview = (TextView) findViewById(R.id.textView);
			  textview.setTextColor(Color.BLACK);
			  
			  //close the message after 3 seconds
			  new Timer().schedule(new TimerTask() {          
				    @Override
				    public void run() {
				        // this code will be executed after 3 seconds
				    	runMain();
				    }
				}, 3000);
		}
		
		
		
		
		
	}
	
	
	public void retryClick(View view){
		finish();
	}
	
	public void allowClick(View view){
		
	}
	
	void runMain(){
		startActivity(new  Intent(this, MainActivity.class));
    	finish();
	}
}
