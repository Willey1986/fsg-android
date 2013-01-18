package de.tubs.cs.ibr.fsg.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import de.tubs.cs.ibr.fsg.R;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final boolean DEVELOPER_MODE = true;

	private static final String PASSWORD = "kuh";

	String[] actions = new String[] {
		"User",
		"Admin",
	};	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
 
            
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                Toast.makeText(getBaseContext(), "You selected : " + actions[itemPosition]  , Toast.LENGTH_SHORT).show();
                return false;
            }
        };
        
        getActionBar().setListNavigationCallbacks(adapter, navigationListener);
        }
       
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.activity_main, menu);
            return true;
        }        
        
        public boolean onOptionsItemSelected(MenuItem item) {
        	super.onOptionsItemSelected (item);
        	switch(item.getItemId()){
        	case R.id.admin:
        		chooseAdmin();
        
        		break;
        	}
        	
        	return true;
        }
        
        private void chooseAdmin(){
        	new AlertDialog.Builder (this);
        	 LayoutInflater factory = LayoutInflater.from(this);
        	 final View textEntryView = factory.inflate(R.layout.dialog_login, null);
        	    AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
        	    alert.setTitle("Login");  
        	 alert.setMessage("Enter Pin :");                
        	 alert.setView(textEntryView);
        	 	
        	 	final Activity obj = this;
        	 	
        	    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
        	    public void onClick(DialogInterface dialog, int whichButton) {  
        	    	EditText mUserText;
        	        mUserText = (EditText) textEntryView.findViewById(R.id.txt_password);
        	        String strPinCode = mUserText.getText().toString();
        	    	

        	        if(strPinCode.equals (PASSWORD)) {

            	    	//OK Button wurde gedrueckt
            	    	startActivity(new Intent(obj, AdminActivity.class));
        	        }else{
        	        	//hier ist das Passwort falsch
            	        Log.d( TAG, "Pin Value : " + strPinCode);

            	        Log.d( TAG, "Password: " + PASSWORD);
        	        }
        	    	
        	    	
        	    	
        	        
        	        return;                 
        	      } 
        	     });  

        	    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

        	        public void onClick(DialogInterface dialog, int which) {
        	            // TODO Auto-generated method stub
        	            return;   
        	        }
        	    });
        	            alert.show();        
        }        
    
    /**
     * 
     * @param view
     */
    public void onButtonClick(View view){
    	switch (view.getId() ){
    	case R.id.button1:
    		startActivity(new Intent(this, RegistrationTeamSelectionActivity.class));
    		break;
    	case R.id.button2:
    		startActivity(new Intent(this, BriefingCheckInActivity.class));
    		break;
    	case R.id.button3:
    		startActivity(new Intent(this, BriefingCheckOutActivity.class));
    		break;
    	case R.id.button4:
    		startActivity(new Intent(this, RunActivity.class));
    		break;
    	case R.id.button5:
    		startActivity(new Intent(this, InfoTerminalInitialisierung.class));
    		break;
    	}
    }  
}
