package de.tubs.cs.ibr.fsg.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.ibr.dtn.api.Block;
import de.tubs.ibr.dtn.api.Bundle;
import de.tubs.ibr.dtn.api.BundleID;
import de.tubs.ibr.dtn.api.DTNClient;
import de.tubs.ibr.dtn.api.DataHandler;
import de.tubs.ibr.dtn.api.GroupEndpoint;
import de.tubs.ibr.dtn.api.Registration;
import de.tubs.ibr.dtn.api.ServiceNotAvailableException;
import de.tubs.ibr.dtn.api.SessionDestroyedException;
import de.tubs.ibr.dtn.api.SingletonEndpoint;
import de.tubs.ibr.dtn.api.TransferMode;


public class DTNService extends IntentService {

	private static final String TAG = "DTNService";
	public  static final GroupEndpoint FSG_GROUP_EID  = new GroupEndpoint("dtn://fsg.dtn/broadcast");
	
	private DTNClient mClient = null;
	private Registration mRegistration = null;

	
	public DTNService() {
		super(TAG);
	}

	
	@Override
	public void onCreate() {
		Log.i(TAG, "service created.");
		super.onCreate();
		// Notwendige Initialitierungen für IBR-DTN
		mClient = new DTNClient();
		mRegistration = new Registration("fsg");
		mRegistration.add(FSG_GROUP_EID);
		mClient.setDataHandler(fdDataHandler);
		
		try {
			mClient.initialize(this, mRegistration);
		} catch (ServiceNotAvailableException e) {
			Log.e(TAG, "IBR-DTN-Daemon not are running", e);
			
//			Toast.makeText(getApplicationContext(), R.string.error_not_ibr_dtn, Toast.LENGTH_LONG).show();
			LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			View layout = inflater.inflate(R.layout.toast_layout, null);
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText(R.string.error_not_ibr_dtn);
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();
			
		} catch (SecurityException ex) {
			Log.e(TAG, "SecurityException", ex);
			
//			Toast.makeText(getApplicationContext(), R.string.error_security_exception, Toast.LENGTH_LONG).show();
			LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			View layout = inflater.inflate(R.layout.toast_layout, null);
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText(R.string.error_security_exception);
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();
		}
	}

	
	@Override
	public void onDestroy() {
		// Der DTNClient wird gestoppt.
		mClient.terminate();
		mClient = null;
		super.onDestroy();
		Log.i(TAG, "service destroyed.");
	}
	
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();

		if (de.tubs.ibr.dtn.Intent.RECEIVE.equals(action)){
			try {
				while (mClient.getSession().queryNext());
			} catch (SessionDestroyedException e) {
				Log.e(TAG, "Can not query for bundle", e);
			} catch (InterruptedException ex) {
				Log.e(TAG, "Can not query for bundle", ex);
			}
			
		}else if (de.tubs.cs.ibr.fsg.Intent.MARK_DELIVERED.equals(action)){
        	BundleID bundleid = intent.getParcelableExtra("bundleid");
        	if (bundleid == null) {
        		Log.e(TAG, "Intent to mark a bundle as delivered, but no bundle ID given");
        	}else{
        		try {
        			mClient.getSession().delivered(bundleid);
        		}catch (Exception e) {
        			Log.e(TAG, "Can not mark bundle as delivered.", e);
        		}	
        	}
        	
        }else if (de.tubs.cs.ibr.fsg.Intent.SEND_DATA.equals(action)){
        	
        	String destinationString = intent.getStringExtra("singletonendpoint");
    		SingletonEndpoint destination = new SingletonEndpoint(destinationString);
    		String jsonData = intent.getStringExtra("jsondata");

    		try {
				if (!mClient.getSession().send(destination, 3600, jsonData.getBytes())){
					throw new Exception("Can not send the JSON-Data");
				}else{
					Log.i(TAG, "Data sended to backend.");
				}
			} catch (Exception e) {
				Log.e(TAG, "Can not send the Data to backend.", e);
			}
        	
        }else if (de.tubs.cs.ibr.fsg.Intent.REGISTRATION.equals(action)){
        	// Hier passiert nichts Zusätzliches. Die Registrierung in der Methode 
        	// onCreated() reicht aus (...mRegistration = new Registration("fsg");...)
        	// -->  onCreated() wird nämlich vorher schon ausgeführt.
        	Log.i(TAG, "IBR-DTN-Registration done");
        }
	}
	

	/**
	 * Diese DataHandler-Implementierung mit dem SIMPLE-Modus ist korrekt und vollständig
	 * programmiert, aber sie wird zurzeit nicht benutzt. Aktuell arbeiten wir mit dem 
	 * FILEDESCRIPTOR-Modus, der in der fdDataHandler-Implementierung angewendet wird.
	 */
	private DataHandler sDataHandler = new DataHandler() {

		Bundle currentBundle;

		public void startBundle(Bundle bundle) {
			this.currentBundle = bundle;
		}

		public void endBundle() {
			BundleID receivedBundleID = new BundleID(this.currentBundle);

			Intent mIntent = new Intent(DTNService.this, DTNService.class);
			mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.MARK_DELIVERED);
			mIntent.putExtra("bundleid", receivedBundleID);
			startService(mIntent);
			this.currentBundle = null;
		}

		public TransferMode startBlock(Block block) {
			TransferMode mTranferMode  = TransferMode.SIMPLE;
			if (block.length > 32768){
				// Die Blöcke werden ignoriert, die grösser als 3MB sind.
				mTranferMode  = TransferMode.NULL;
			}
			return mTranferMode;
		}

		public void endBlock() {
			// Brauchen wir nicht, wegen SIMPLE-MODE
		}

		public void characters(String data) {
			//Log.i(TAG, "Received characters: " + new String(data));
		}

		public void payload(byte[] data) {
			final String msg = new String(data);
			
			String fileName = System.currentTimeMillis() + ".txt";
			DTNService.storeStringToFile(msg, fileName);
		}

		public ParcelFileDescriptor fd() {
			return null;
		}

		public void progress(long current, long length) {
			Log.i(TAG, "Payload: " + current + " of " + length + " bytes.");
		}

		public void finished(int startId) {
			// Brauchen wir nicht, wegen IntentService
		}

	};
	
	
	/**
	 * Diese DataHandler-Implementierung nutzt zum Empfangen den FILEDESCRIPTOR-Modus,
	 * der für Android-Geräten geeigneter als der SIMPLE-Modus ist, da im SIMPLE-Modus
	 * alles im HEAP stattfindet. 
	 * 
	 */
	private DataHandler fdDataHandler = new DataHandler() {
		private Bundle currentBundle = null;
		private File payloadFile = null;
		private ParcelFileDescriptor pfd = null;

		public void startBundle(Bundle bundle) {
			this.currentBundle = bundle;
		}

		public void endBundle() {
			if (payloadFile != null) {
				//Log.i(TAG, "New JSON-File received.");

				final Bundle receivedBundle = currentBundle;

				try {
					mClient.getSession().delivered(new BundleID(receivedBundle));
				} catch (Exception e) {
					Log.e(TAG, "Can not mark bundle as delivered.", e);
				}
				payloadFile.delete();
				payloadFile = null;
			}
			currentBundle = null;
		}

		public TransferMode startBlock(Block block) {
			TransferMode mTranferMode  = null;
			if ((block.type == 1) && (payloadFile == null)) {
				File folder = DTNService.getStoragePath();
		
				try {
					// Wir erzeugen eine temporäre Datei
					payloadFile = File.createTempFile("tempJSONFile", ".txt", folder);
					mTranferMode  = TransferMode.FILEDESCRIPTOR;
				} catch (IOException e) {
					Log.e(TAG, "Can not create temporary file.", e);
					payloadFile = null;
					mTranferMode  = TransferMode.NULL;
				}
			}else{
				mTranferMode  = TransferMode.NULL;
			}
			return mTranferMode;
		}

		public void endBlock() {
			if (pfd != null) {
				try {
					pfd.close();
					pfd = null;
				} catch (IOException e) {
					Log.e(TAG, "Can not close filedescriptor.", e);
				}
			}

			if (payloadFile != null) {
				DTNService.storeTempFile(payloadFile);
				Log.i(TAG, "New JSON-File received.");
				createNotification();
				
				payloadFile.delete();
				payloadFile = null;
			}
		}

		public void characters(String data) {
			//Log.i(TAG, "Received characters: " + new String(data));
		}

		public void payload(byte[] data) {
			// Braucht man nur beim SIMPLEMODE
		}

		public ParcelFileDescriptor fd() {
			try {
				pfd = ParcelFileDescriptor.open(payloadFile, ParcelFileDescriptor.MODE_CREATE + ParcelFileDescriptor.MODE_READ_WRITE);
				return pfd;
			} catch (FileNotFoundException e) {
				Log.e(TAG, "Can not create a filedescriptor.", e );
			}
			return null;
		}

		public void progress(long current, long length) {
			Log.i(TAG, "Payload: " + current + " of " + length + " bytes.");
		}

		public void finished(int startId) {
			// Brauchen wir nicht, wegen IntentService
		}
	};

	
	/**
	 * 
	 */
	protected void createNotification() {

		Resources res = getResources();
		Notification noti = new Notification.Builder(this)
				.setContentTitle(res.getString(R.string.noti_title))
				.setContentText(res.getString(R.string.noti_text_register_data))
				.setSmallIcon(R.drawable.icon_register)
				.setLargeIcon(
						BitmapFactory.decodeResource(res, R.drawable.ic_launcher)).getNotification();

		noti.defaults |= Notification.DEFAULT_SOUND;
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, noti);
	}
	
	
	/**
	 * Diese Methode wird nur zum Testen gebraucht, bis die interne Datenbank benutzt werden kann.
	 * @param tempFile Temporäre Datei, die dauerhaft gespeichert werden soll.
	 */
	protected static void storeTempFile(File tempFile) {
		File root = DTNService.getStoragePath();
		String outputFileName = System.currentTimeMillis() + ".txt";
	    File outputFile = new File(root, outputFileName);

	    FileReader in  = null;
	    FileWriter out = null;
		try {
			in = new FileReader(tempFile);
		    out = new FileWriter(outputFile);
		    
		    int c;
		    while ((c = in.read()) != -1){
			      out.write(c);
		    }
		    in.close();
		    out.close();
		    
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Can not create a file.", e );
		} catch (IOException e) {
			Log.e(TAG, "Can not create a file.", e );
		}
	}


	/**
	 * Diese Methode wird nur zum Testen gebraucht, bis die interne Datenbank benutzt werden kann.
	 * @return Gibt den Pfad zurück, wo empfangene Daten als Datei gespeichert werden sollen.
	 */
	public static File getStoragePath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File externalStorage = Environment.getExternalStorageDirectory();
			// Wenn es noch nicht vorhanden ist, wird an dieser Stelle ein Arbeitsverzeichnis erstellt.
			File sharefolder = new File(externalStorage.getPath() + File.separatorChar + "fsg");
			if (!sharefolder.exists()) {
				sharefolder.mkdir();
			}
			return sharefolder;
		}
		return null;
	}


	/**
	 * Diese Methode wird nur zum Testen gebraucht, bis die interne Datenbank benutzt werden kann.
	 * @param stringToSave Text, der in einer Datei gespeichert wird.
	 * @param fileName Name der Datei, wo der Text gespeichert werden soll.
	 */
	public static void storeStringToFile(String stringToSave, String fileName) {
		try {
			//File root = Environment.getExternalStorageDirectory();
			File root = DTNService.getStoragePath();
			if (root.canWrite()) {
				File gpxfile = new File(root, fileName);
				FileWriter gpxwriter = new FileWriter(gpxfile);
				BufferedWriter out = new BufferedWriter(gpxwriter);
				out.write(stringToSave);
				out.close();
			}
		} catch (IOException e) {
			Log.e(TAG, "Could not write file ", e );
		}
	}
 

}
