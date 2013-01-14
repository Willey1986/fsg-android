package de.tubs.cs.ibr.fsg.views;


import java.io.File;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.dtn.FileHelper;

public class DriverView extends RelativeLayout{
	
	public DriverView(Context context) {
		super(context);
	}
	
	public DriverView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public DriverView(Context context, Driver driver) {
		super(context);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.view_registration_driver, this);
		TextView name = (TextView) findViewById(R.id.tvName);
		name.setText(driver.getLast_name()+ ", " + driver.getFirst_name());
		
		// An dieser Stelle suchen wir nach dem Fahrerbild und stellen es ggf dar.
		int driverId = driver.getUser_id();
		File picDirectory = FileHelper.getStoragePath(FileHelper.DRIVER_PICS_DIR);
		File picFile = null;
		if(picDirectory!=null){
			picFile = new File( picDirectory.getAbsoluteFile() + String.valueOf(File.separatorChar) + String.valueOf(driverId) + ".jpg" );
		}
		if (picFile!=null && picFile.exists() ) {
			View driverPic = (View) findViewById(R.id.driverPic);
			Drawable mDrawable = Drawable.createFromPath( picFile.getAbsolutePath() );
			driverPic.setBackgroundDrawable(mDrawable);
		}
		
	}

}
