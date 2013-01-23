package de.tubs.cs.ibr.fsg.activities;

import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.R.layout;
import de.tubs.cs.ibr.fsg.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RunActivityPost extends Activity {

	private String disciplineName, message;
	private int runCount;
	private boolean success;
	private Resources res;
	
	private LinearLayout llRunPost;
	
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
		if (success) 
			llRunPost.setBackgroundDrawable(res.getDrawable(R.drawable.background_green));
		else {
			llRunPost.setBackgroundDrawable(res.getDrawable(R.drawable.background_red));
			TextView tvMessage = new TextView(this);
			tvMessage.setText(message);
			tvMessage.setTextSize(res.getDimension(R.dimen.grid07));
			tvMessage.setTextColor(res.getColor(R.color.button_text_color));
			
			llRunPost.addView(tvMessage);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_run_post, menu);
		return true;
	}

}
