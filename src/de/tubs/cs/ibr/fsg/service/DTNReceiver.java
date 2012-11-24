package de.tubs.cs.ibr.fsg.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Mit diesem BroadcastReceiver bemerken wir ankommenden Daten und starten
 * den dazugehörigen Service, der die Daten entsprechend empfängt.
 */
public class DTNReceiver extends BroadcastReceiver{
	private static final String TAG = "DTNReceiver";

	@Override
	public void onReceive(Context mContext, Intent mIntent) {
		String action = mIntent.getAction();
		
		if (action.equals(de.tubs.ibr.dtn.Intent.RECEIVE)){
			Log.i(TAG, "receive intent");
			Intent newIntent = new Intent(mContext, DTNService.class);
			newIntent.setAction(de.tubs.ibr.dtn.Intent.RECEIVE);
			mContext.startService(newIntent);
		}
	}

	
}
