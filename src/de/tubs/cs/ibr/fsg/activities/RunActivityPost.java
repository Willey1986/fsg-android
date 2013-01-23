package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class RunActivityPost extends Activity{

	private String disciplineName, message;
	private int runCount;
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
		res = getResources();
		
		llRunPost = (LinearLayout) findViewById(R.id.llRunPost);
		btnIgnore = new Button(this);
		btnGoBack = new Button(this);
		
		if (success) 
			llRunPost.setBackgroundDrawable(res.getDrawable(R.drawable.background_green));
		else {
			llRunPost.setBackgroundDrawable(res.getDrawable(R.drawable.background_red));
			
			TextView tvMessage = new TextView(this);
			tvMessage.setText(message);
			tvMessage.setTextSize(res.getDimension(R.dimen.grid07));
			tvMessage.setTextColor(res.getColor(R.color.button_text_color));
			tvMessage.setPadding(0, 0, 0, 16);
			
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

			
			llRunPost.addView(tvMessage);
			llRunPost.addView(btnIgnore);
			llRunPost.addView(btnGoBack);
			
			btnIgnore.getId();
		
		}
	}
	
	private void returnToRunActivity() {
		Intent mIntent = new Intent(this,RunActivityConfirm.class);
		
		startActivity(mIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_run_post, menu);
		return true;
	}

}
