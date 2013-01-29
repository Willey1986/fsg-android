package de.tubs.cs.ibr.fsg.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import de.tubs.cs.ibr.fsg.R;

public class ConfigurationActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        
        SharedPreferences prefs = this.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_MULTI_PROCESS);
        String serverAddress = prefs.getString("server_address", "dtn://fsg-backend.dtn/fsg");
        String adminPassword = prefs.getString("admin_password", "kuh");
        
        
        TextView serverAddressText = (TextView) findViewById(R.id.textServer);
        serverAddressText.setText( serverAddress );
        
        TextView adminPasswordText = (TextView) findViewById(R.id.textPassword);
        adminPasswordText.setText( adminPassword );
    }
    
    public void onButtonClick(View view){
    	
    	SharedPreferences prefs = this.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_MULTI_PROCESS);
    	SharedPreferences.Editor editor = prefs.edit();
    	
    	TextView serverAddressText = (TextView) findViewById(R.id.textServer);
    	String newServerAddress = serverAddressText.getText().toString();
    	
    	TextView adminPasswordText = (TextView) findViewById(R.id.textPassword);
    	String newAdminPassword = adminPasswordText.getText().toString();
    	
    	if(newAdminPassword.equalsIgnoreCase("")){
    		toastError();
    		
    	}else{
    		
    		editor.putString("server_address", newServerAddress);
    		editor.putString("admin_password", newAdminPassword);
    		editor.commit();
    		
        	onBackPressed();
        	
    	}
    	

    }

      
	private void toastError() {
		LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate(R.layout.toast_layout, null);
		TextView text = (TextView) layout.findViewById(R.id.text);
    	text.setText(R.string.error_admin_password_empy);
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
        
}
