package de.tubs.cs.ibr.fsg.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Mit diesem BroadcastReceiver bemerken wir ankommenden Daten und starten
 * den dazugeh�rigen Service, der die Daten entsprechend empf�ngt.
 */
public class DTNReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context mContext, Intent mIntent) {
		String action = mIntent.getAction();
		
		if (action.equals(de.tubs.ibr.dtn.Intent.RECEIVE)){
			Intent newIntent = new Intent(mContext, DTNService.class);
			newIntent.setAction(de.tubs.ibr.dtn.Intent.RECEIVE);
			mContext.startService(newIntent);
		}
	}

	
}
