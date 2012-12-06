package de.tubs.cs.ibr.fsg.views;


import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
		
	}

}
