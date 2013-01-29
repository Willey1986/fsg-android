package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class RunActivityPost extends Activity{

	private String disciplineName, message;
	private int runCount, errorNumber;
	private boolean success;
	private Resources res;
	
	private LinearLayout llRunPost;
	private Button btnIgnore;
	private Button btnGoBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_post);
		Bundle extras = getIntent().getExtras();
		disciplineName = extras.getString("DisciplineName");
		message = extras.getString("Message");
		runCount = extras.getInt("RunCount");
		success = extras.getBoolean("Success");
		errorNumber = extras.getInt("ErrorNumber");
		res = getResources();
		
		llRunPost = (LinearLayout) findViewById(R.id.llRunPost);
		btnIgnore = new Button(this);
		btnGoBack = new Button(this);
		
		if (success) {
			//llRunPost.setBackgroundDrawable(res.getDrawable(R.drawable.background_green));
			setContentView(R.layout.activity_run_successful);
			Button okButton = (Button) findViewById(R.id.buttonRunSuccessful);
			okButton.setOnClickListener(new OnClickListener() {

	            public void onClick(View v) {
	            	onBackPressed();
	            }
	        });
		}else {
			llRunPost.setBackgroundDrawable(res.getDrawable(R.drawable.background_red));
			
			TextView tvMessage = new TextView(this);
			tvMessage.setText(message);
			tvMessage.setTextSize(res.getDimension(R.dimen.grid07));
			tvMessage.setTextColor(res.getColor(R.color.button_text_color));
			tvMessage.setPadding(0, 0, 0, 16);
			tvMessage.setGravity(Gravity.CENTER_HORIZONTAL);
			
			btnIgnore.setText("Trotzdem genehmigen");
			btnIgnore.setHeight(100);
			btnIgnore.setTextSize(res.getDimension(R.dimen.grid06));
			btnIgnore.setTextColor(res.getColor(R.color.button_text_color));
			btnIgnore.setBackgroundDrawable(res.getDrawable(R.layout.button_selector));
			btnIgnore.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Intent mIntent = new Intent(getBaseContext(), RunActivityConfirm.class);
					mIntent.putExtra("DisciplineName", disciplineName);
					mIntent.putExtra("RunCount", runCount);
					mIntent.putExtra("Ignore",true);
					startActivity(mIntent);
				}
			});

			
			btnGoBack.setText("Auswahl ändern");
			btnGoBack.setHeight(100);
			btnGoBack.setTextSize(res.getDimension(R.dimen.grid06));
			btnGoBack.setTextColor(res.getColor(R.color.button_text_color));
			btnGoBack.setBackgroundDrawable(res.getDrawable(R.layout.button_selector));
			btnGoBack.setOnClickListener(new OnClickListener(	) {
				
				public void onClick(View v) {
					Intent mIntent = new Intent(getBaseContext(), RunActivity.class);
					startActivity(mIntent);
				}
			});

			if (errorNumber == RunActivityConfirm.ERROR_TAG_BLACKLISTED) {
				llRunPost.addView(tvMessage);
			}
			else if (errorNumber == RunActivityConfirm.ERROR_BRIEFING_ABSENT) {
				llRunPost.addView(tvMessage);
				llRunPost.addView(btnIgnore);
			}
			else {
				llRunPost.addView(tvMessage);
				llRunPost.addView(btnIgnore);
				llRunPost.addView(btnGoBack);
			}
		
		}
	}
	
	private void returnToRunActivity() {
		Intent mIntent = new Intent(this,RunActivityConfirm.class);
		
		startActivity(mIntent);
	}


}
