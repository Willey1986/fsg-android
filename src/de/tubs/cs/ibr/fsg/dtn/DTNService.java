package de.tubs.cs.ibr.fsg.dtn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import de.tubs.cs.ibr.fsg.R;
import de.tubs.cs.ibr.fsg.db.DBAdapter;
import de.tubs.cs.ibr.fsg.db.DBHelper;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;
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
		// Notwendige Initialitierungen f�r IBR-DTN
		mClient = new DTNClient();
		mRegistration = new Registration("fsg");
		mRegistration.add(FSG_GROUP_EID);
		mClient.setDataHandler(fdDataHandler);
		
		try {
			mClient.initialize(this, mRegistration);
		} catch (ServiceNotAvailableException e) {
			Log.e(TAG, "IBR-DTN-Daemon not are running", e);
			toastError(new FsgException(e, new DTNService().getClass().toString(), FsgException.IBR_DTN_NOT_RUNNING) );
			
		} catch (SecurityException e) {
			Log.e(TAG, "SecurityException", e);
			toastError(new FsgException(e, new DTNService().getClass().toString(), FsgException.SECURITY_FAIL) );
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
        	
        	String destinationString      = intent.getStringExtra("destination");
    		SingletonEndpoint destination = new SingletonEndpoint(destinationString);
    		String type                   = intent.getStringExtra("type");
    		String version                = intent.getStringExtra("version");
    		String payload                = intent.getStringExtra("payload");
    		
    		byte[] payloadByteArray = null;
			try {
				payloadByteArray = FsgProtocol.getByteArrayToSend(Integer.valueOf(type), Integer.valueOf(version), payload);
				if (!mClient.getSession().send(destination, 3600, payloadByteArray )){
					throw new FsgException(new Exception("Can not send the Data to backend."), new DTNService().getClass().toString(), FsgException.DTN_SENDING_FAIL);
				}else{
					Log.i(TAG, "Data sended to backend.");
				}
		
			} catch (Exception e) {
				Log.e(TAG, "Can not send the Data to backend.", e);
				toastError(new FsgException(e, new DTNService().getClass().toString(), FsgException.DTN_SENDING_FAIL) );
			}

        }else if (de.tubs.cs.ibr.fsg.Intent.REGISTRATION.equals(action)){
        	// Hier passiert nichts Zus�tzliches. Die Registrierung in der Methode 
        	// onCreated() reicht aus (...mRegistration = new Registration("fsg");...)
        	// -->  onCreated() wird n�mlich vorher schon ausgef�hrt.
        	Log.i(TAG, "IBR-DTN-Registration done");
        }
	}
	

//	/**
//	 * Diese DataHandler-Implementierung mit dem SIMPLE-Modus ist korrekt und vollst�ndig
//	 * programmiert, aber sie wird zurzeit nicht benutzt. Aktuell arbeiten wir mit dem 
//	 * FILEDESCRIPTOR-Modus, der in der fdDataHandler-Implementierung angewendet wird.
//	 */
//	private DataHandler sDataHandler = new DataHandler() {
//
//		Bundle currentBundle;
//
//		public void startBundle(Bundle bundle) {
//			this.currentBundle = bundle;
//		}
//
//		public void endBundle() {
//			BundleID receivedBundleID = new BundleID(this.currentBundle);
//
//			Intent mIntent = new Intent(DTNService.this, DTNService.class);
//			mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.MARK_DELIVERED);
//			mIntent.putExtra("bundleid", receivedBundleID);
//			startService(mIntent);
//			this.currentBundle = null;
//		}
//
//		public TransferMode startBlock(Block block) {
//			TransferMode mTranferMode  = TransferMode.SIMPLE;
//			if (block.length > 32768){
//				// Die Bl�cke werden ignoriert, die gr�sser als 3MB sind.
//				mTranferMode  = TransferMode.NULL;
//			}
//			return mTranferMode;
//		}
//
//		public void endBlock() {
//			// Brauchen wir nicht, wegen SIMPLE-MODE
//		}
//
//		public void characters(String data) {
//			//Log.i(TAG, "Received characters: " + new String(data));
//		}
//
//		public void payload(byte[] data) {
//			final String msg = new String(data);
//			
//			String fileName = System.currentTimeMillis() + ".txt";
//			FileHelper.storeStringToFile(msg, fileName, FileHelper.GENERIC_FSG_DIR);
//		}
//
//		public ParcelFileDescriptor fd() {
//			return null;
//		}
//
//		public void progress(long current, long length) {
//			Log.i(TAG, "Payload: " + current + " of " + length + " bytes.");
//		}
//
//		public void finished(int startId) {
//			// Brauchen wir nicht, wegen IntentService
//		}
//
//	};
	
	
	/**
	 * Diese DataHandler-Implementierung nutzt zum Empfangen den FILEDESCRIPTOR-Modus,
	 * der f�r Android-Ger�ten geeigneter als der SIMPLE-Modus ist, da im SIMPLE-Modus
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
				File folder = FileHelper.getStoragePath(FileHelper.TEMP_FSG_DIR);
		
				try {
					// Wir erzeugen eine temporaere Datei
					String outputFileName = "tempfile_" + System.currentTimeMillis();
					payloadFile = File.createTempFile(outputFileName, ".fsg", folder);
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
				
				try {
					FsgPackage mFsgBundle = FsgProtocol.getFsgPackage(payloadFile);
					
					// Um zu unterscheiden, wenn wir versehetlich Datenpakete bekommen, die nicht f�r
					// die Clients gedacht sind, merken wir uns das in folgender Variable. Ausserdem
					// �berpr�fen wir auch, ob es eine neue Version ist und damit f�r uns relevant.
					// In dem Fall, wo der B�ndel entweder alt oder eines f�r uns nicht relevanten Typs ist,
					// ignorieren wir den B�ndel, er wird nirgendwo gespeichert und es wird keine
					// Notification erzeugt.
					boolean isABundleTypForClients = false;
					boolean isANewVersion = false;
					
					if (mFsgBundle.getPackageTyp() == FsgProtocol.DATA_DRIVER_PICS){
						isABundleTypForClients = true;
						if (isANewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_DRIVER_PICS )){
							FileHelper.saveDriverPics(mFsgBundle);
							isANewVersion = true;
							
							sendNewVersionConfirmation(mFsgBundle.getVersion(), FsgProtocol.DATA_DRIVER_PICS);
							saveToPreferencesTheNewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_DRIVER_PICS);
							Log.i(TAG, "New driver pics received.");
						}

					}else if (mFsgBundle.getPackageTyp() == FsgProtocol.DATA_DRIVERS){
						isABundleTypForClients = true;
						if (isANewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_DRIVERS )){
							isANewVersion = true;
							String jsonArray = new String(mFsgBundle.getPayload(), "UTF8");
							writeDataToDB(FsgProtocol.DATA_DRIVERS, jsonArray);
							
							sendNewVersionConfirmation(mFsgBundle.getVersion(), FsgProtocol.DATA_DRIVERS);
							saveToPreferencesTheNewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_DRIVERS);
							Log.i(TAG, "New drivers data received.");
						}
						
					}else if (mFsgBundle.getPackageTyp() == FsgProtocol.DATA_TEAMS){
						isABundleTypForClients = true;
						if (isANewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_TEAMS )){
							isANewVersion = true;
							String jsonArray = new String(mFsgBundle.getPayload(),"UTF8");
							writeDataToDB(FsgProtocol.DATA_TEAMS, jsonArray );
							
							sendNewVersionConfirmation(mFsgBundle.getVersion(), FsgProtocol.DATA_TEAMS);
							saveToPreferencesTheNewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_TEAMS);
							Log.i(TAG, "New teams data received.");
						}
						
					}else if (mFsgBundle.getPackageTyp() == FsgProtocol.DATA_BLACK_TAGS){
						isABundleTypForClients = true;
						if (isANewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_BLACK_TAGS )){
							isANewVersion = true;
							String jsonArray = new String(mFsgBundle.getPayload(),"UTF8");
							writeDataToDB(FsgProtocol.DATA_BLACK_TAGS, jsonArray );
							
							sendNewVersionConfirmation(mFsgBundle.getVersion(), FsgProtocol.DATA_BLACK_TAGS);
							saveToPreferencesTheNewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_BLACK_TAGS);
							Log.i(TAG, "New tags-blacklist received.");
						}
						
					}else if (mFsgBundle.getPackageTyp() == FsgProtocol.DATA_BLACK_DEVICES){
						isABundleTypForClients = true;
						if (isANewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_BLACK_DEVICES )){
							isANewVersion = true;
							String jsonArray = new String(mFsgBundle.getPayload(),"UTF8");
							writeDataToDB(FsgProtocol.DATA_BLACK_DEVICES, jsonArray );
							
							sendNewVersionConfirmation(mFsgBundle.getVersion(), FsgProtocol.DATA_BLACK_DEVICES);
							saveToPreferencesTheNewVersion(mFsgBundle.getVersion(), FsgProtocol.DATA_BLACK_DEVICES);
							Log.i(TAG, "New devices-blacklist received.");
						}
						
					}
					
					if (isABundleTypForClients && isANewVersion){
						//FileHelper.storeTempFile(payloadFile, FileHelper.GENERIC_FSG_DIR);
						createNotification( mFsgBundle.getPackageTyp(), mFsgBundle.getVersion() );
					}

				} catch (Exception e) {
					toastError(new FsgException(e, new DTNService().getClass().toString(), FsgException.DTN_RECEIVING_FAIL) );
				}

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
	 * @param dataType
	 */
	protected void writeDataToDB(int dataType, String jsonArray) {
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		
		if (dataType == FsgProtocol.DATA_DRIVERS) {
			String deleteAllDrivers = "DELETE FROM " + DBHelper.TABLE_DRIVERS + ";";
			dba.execSQL(deleteAllDrivers);
			dba.writeDriversToDB(jsonArray);

		} else if (dataType == FsgProtocol.DATA_TEAMS) {
			String deleteAllTeams = "DELETE FROM " + DBHelper.TABLE_TEAMS + ";";
			dba.execSQL(deleteAllTeams);
			dba.writeTeamsToDB(jsonArray);

		} else if (dataType == FsgProtocol.DATA_BLACK_TAGS) {
			String deleteAllBlackTags = "DELETE FROM " + DBHelper.TABLE_BLACKLISTED_TAGS + ";";
			dba.execSQL(deleteAllBlackTags);
			dba.writeBlacklistedTagsToDB(jsonArray);

		} else if (dataType == FsgProtocol.DATA_BLACK_DEVICES) {
			String deleteAllBlackDevices = "DELETE FROM " + DBHelper.TABLE_BLACKLISTED_DEVICES + ";";
			dba.execSQL(deleteAllBlackDevices);
			dba.writeBlacklistedDevicesToDB(jsonArray);

		}
		dba.close();
	}
	
	
	/**
	 * Mit Hilfe dieser Methode pr�fen wir, ob die empfangene Version eines Datensatzes
	 * neuer ist als die bereits auf dem Client vorhandenen Version.
	 * 
	 * @param receivedVersion Empfangene Version des Datensatzes.
	 * @param dataType Datentyp (z.B. Fahrerdaten, Ger�te-Blacklist, usw).
	 * @return true, wenn die empfangene Version neuer ist als die auf dem Ger�t bereits vorhandene.
	 */
	protected boolean isANewVersion(int receivedVersion, int dataType) {
		boolean isANewVersion = false;
		SharedPreferences prefs = this.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_PRIVATE);
		int savedVersion = 0;
		
		if(dataType==FsgProtocol.DATA_DRIVER_PICS){
			savedVersion=  prefs.getInt("version_driver_pics", 0);
			
		}else if(dataType==FsgProtocol.DATA_DRIVERS){
			savedVersion=  prefs.getInt("version_drivers", 0);
			
		}else if(dataType==FsgProtocol.DATA_TEAMS){
			savedVersion=  prefs.getInt("version_teams", 0);
			
		}else if(dataType==FsgProtocol.DATA_BLACK_TAGS){
			savedVersion=  prefs.getInt("version_black_tags", 0);
			
		}else if(dataType==FsgProtocol.DATA_BLACK_DEVICES){
			savedVersion=  prefs.getInt("version_black_devices", 0);
		}
		
		if(receivedVersion > savedVersion){
			isANewVersion = true;
		}
		
		return isANewVersion;
	}
	
	
	/**
	 * Mit Hilfe dieser Methode merken wir uns in den "SharedPreferences" die Version des Datensatzes,
	 * den wir empfangen und gespeichert haben.
	 * 
	 * @param receivedVersion Version des Datensatzes, den wir empfangen und gespeichert haben.
	 * @param dataType Typ des Datensatzes, den wir empfangen und gespeichert haben (z.B. Fahrerdaten, Ger�te-Blacklist, usw).
	 */
	protected void saveToPreferencesTheNewVersion(int receivedVersion, int dataType) {

		SharedPreferences prefs = this.getSharedPreferences("de.tubs.cs.ibr.fsg", Context.MODE_PRIVATE);
		
		if(dataType==FsgProtocol.DATA_DRIVER_PICS){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("version_driver_pics", receivedVersion );
			editor.commit();
			
		}else if(dataType==FsgProtocol.DATA_DRIVERS){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("version_drivers", receivedVersion );
			editor.commit();
			
		}else if(dataType==FsgProtocol.DATA_TEAMS){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("version_teams", receivedVersion );
			editor.commit();
			
		}else if(dataType==FsgProtocol.DATA_BLACK_TAGS){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("version_black_tags", receivedVersion );
			editor.commit();
			
		}else if(dataType==FsgProtocol.DATA_BLACK_DEVICES){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("version_black_devices", receivedVersion );
			editor.commit();
		}
	}
	
	
	/**
	 * Nachdem wir eine neue Datensatzversion empfangen und gespeichert haben, k�nnen wir mit Hilfe dieser Methode
	 * eine Empfangsbest�tigung zum Backend schicken. Damit kann im Backend kontrolliert werden, auf welchem
	 * tats�chlichen Stand die Clients sind.
	 * 
	 * @param receivedVersion Empfangene Version, dessen Empfang best�tigt wird.
	 * @param dataType Typ des Datensatzes, den wir empfangen und gespeichert haben (z.B. Fahrerdaten, Ger�te-Blacklist, usw).
	 */
	protected void sendNewVersionConfirmation(int receivedVersion, int dataType) {
		Intent mIntent = new Intent(this, DTNService.class);
		mIntent.setAction(de.tubs.cs.ibr.fsg.Intent.SEND_DATA);
		mIntent.putExtra("destination", "dtn://fsg-backend.dtn/fsg"  );
		mIntent.putExtra("version",     String.valueOf(receivedVersion) );
		mIntent.putExtra("payload",     "nichts");

		if(dataType==FsgProtocol.DATA_DRIVER_PICS){
			mIntent.putExtra("type", String.valueOf(FsgProtocol.CONFIRM_DRIVER_PICS) );
			
		}else if(dataType==FsgProtocol.DATA_DRIVERS){
			mIntent.putExtra("type", String.valueOf(FsgProtocol.CONFIRM_DRIVERS) );
			
		}else if(dataType==FsgProtocol.DATA_TEAMS){
			mIntent.putExtra("type", String.valueOf(FsgProtocol.CONFIRM_TEAMS) );
			
		}else if(dataType==FsgProtocol.DATA_BLACK_TAGS){
			mIntent.putExtra("type", String.valueOf(FsgProtocol.CONFIRM_BLACK_TAGS) );
			
		}else if(dataType==FsgProtocol.DATA_BLACK_DEVICES){
			mIntent.putExtra("type", String.valueOf(FsgProtocol.CONFIRM_BLACK_DEVICES) );
		}
		
		startService(mIntent);
	}
	
	
	/**
	 * Mit Hilfe dieser Methode koennen wir von diesem Service aus, eine Fehlermeldung als Toast ausgeben,
	 * um bei Fehlern den Benutzer darueber zu informieren.
	 * 
	 * @param e FsgException mit dem Ursprungsfehler.
	 */
	protected void toastError(FsgException mException) {
		LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate(R.layout.toast_layout, null);
		TextView text = (TextView) layout.findViewById(R.id.text);
		
       	switch (mException.getType() ){
    	case FsgException.DTN_SENDING_FAIL:
    		Log.e(TAG, "Fehler beim Senden eines IBR-DTN-B�ndels.", mException);
    		text.setText(R.string.error_sending_bundle);
    		break;
    	case FsgException.DTN_RECEIVING_FAIL:
    		Log.e(TAG, "Fehler beim Empfangen eines IBR-DTN-B�ndels.", mException);
    		text.setText(R.string.error_receiving_bundle);
    		break;
    	case FsgException.NOT_NFC_SUPPORT:
    		Log.e(TAG, "Fehler beim Senden eines IBR-DTN-B�ndels.", mException);
    		text.setText(R.string.error_not_ibr_dtn);
    		break;
    	case FsgException.SECURITY_FAIL:
    		Log.e(TAG, "Fehler beim Empfangen eines IBR-DTN-B�ndels.", mException);
    		text.setText(R.string.error_security_exception);
    		break;
    	}

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}


	/**
	 * Mit dieser Methode erzeugen wir eine typische Android-Notification, um den Benutzer der App darueber zu informieren,
	 * dass Daten-Updates per IBR-DTN empfangen wurden.
	 */
	protected void createNotification(int dataType, int version) {
		
		int notificationTextId = 0;
		if(dataType==FsgProtocol.DATA_DRIVER_PICS){
			notificationTextId = R.string.noti_text_driver_pics;
		}else if(dataType==FsgProtocol.DATA_DRIVERS){
			notificationTextId = R.string.noti_text_drivers;
					
		}else if(dataType==FsgProtocol.DATA_TEAMS){
			notificationTextId = R.string.noti_text_teams;
					
		}else if(dataType==FsgProtocol.DATA_BLACK_TAGS){
			notificationTextId = R.string.noti_text_blacklist_tags;
					
		}else if(dataType==FsgProtocol.DATA_BLACK_DEVICES){
			notificationTextId = R.string.noti_text_blacklist_devices;
					
		}
		
		Resources res = getResources();
		String composedNotiText = res.getString(notificationTextId) + " - Version " + version;

		Notification noti = new Notification.Builder(this)
				.setContentTitle(res.getString(R.string.noti_title))
				.setContentText(composedNotiText)
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon( BitmapFactory.decodeResource(res, R.drawable.ic_launcher)).getNotification();

		noti.defaults |= Notification.DEFAULT_SOUND;
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		noti.flags    |= Notification.FLAG_AUTO_CANCEL;

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(notificationTextId, noti);
	}
	
	

 

}
