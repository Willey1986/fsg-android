/* 		
	import de.tubs.cs.ibr.fsg.Nfc;
	Nfc test = new Nfc(this.getApplicationContext());
	mAdapter = test.getAdapter();
 * 
 * 
 * 
 */

package de.tubs.cs.ibr.fsg;

import java.io.IOException;
import java.util.List;

import de.tubs.cs.ibr.fsg.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import de.tubs.cs.ibr.fsg.NfcData;

public class Nfc {

//	private static NfcAdapter mAdapter;
//	private static Intent mIntent;
//	
//	private static PendingIntent mPendingIntent;
//	private static IntentFilter[] mFilters;
//	private static String[][] mTechLists;
	
	private String memoryContent;
	
	private static final String TAG = "purchtagscanact";

	// Hex help
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
			(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
			(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

	
	public Nfc(Context context){
//		mAdapter = NfcAdapter.getDefaultAdapter(context);
//		mPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
//		try {
//			ndef.addDataType("*/*");
//		} catch (MalformedMimeTypeException e) {
//			throw new RuntimeException("fail", e);
//		}
//		mFilters = new IntentFilter[] { ndef, };
//
//		mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };

//		mIntent = intent;
//		resolveIntent(mIntent);
	}
	
//	public NfcAdapter getAdapter(){
//		return mAdapter;
//	}
//	
//	public PendingIntent getPendingIntent(){
//		return mPendingIntent;
//	}
//	
//	public String[][] getTechLists() {
//		return mTechLists;
//	}
//	
//	public IntentFilter[] getIntentFilter() {
//		return mFilters;
//	}
	
	private void setData(String cardData){
		memoryContent = cardData;
	}
	
	private String getData(){
		return memoryContent;
	}
	
	public String getData(Intent intent, byte[] key){
		readTag(intent, key);
		if (memoryContent != null)
			return memoryContent;
		else
			return "No Data";
	}
	
	public void resolveIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			System.out.println("Discovered tag with intent: " + intent);
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			System.out.println(toString(tagFromIntent.getTechList()));
			if (toString(tagFromIntent.getTechList()).contains("MifareClassic")){
				MifareClassic tag = MifareClassic.get(tagFromIntent);
				byte[] data;
				try{
					tag.connect();
					String cardData = null;
					System.out.println("Authenticating the Tag...");
					//Sector 0 mit Default Key authentifizieren - Key sollte noch geändert werden!
					if (tag.authenticateSectorWithKeyA(0,MifareClassic.KEY_DEFAULT)){
						data = tag.readBlock(1);
						cardData = getHexString(data, data.length);
						System.out.println("Tag reading successfull!");
						System.out.println("Data: "+data);
						System.out.println("CardData: "+cardData);
						System.out.println("Key used: "+getHexString(MifareClassic.KEY_DEFAULT, MifareClassic.KEY_DEFAULT.length));
					} else {
						System.out.println("Authentication Failure!");
					}
					tag.close();
				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage());
					//showAlert(NETWORK);
					System.out.println("Tag reading error!");
				}
			}
		} else {
			System.out.println("Online + Scan a tag");
		}
	}
	
	public void readTag(Intent intent, byte[] key){
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			byte[] data;
			try {
				String cardData = null;
				tag.connect();
				for (int i = 0; i < tag.getBlockCount(); i++){
					if (tag.authenticateSectorWithKeyA(tag.blockToSector(i), key)){
						data = tag.readBlock(i);
						cardData = cardData.concat(getHexString(data, data.length));
					}
				}
				if (cardData != null){
					System.out.println(cardData);
					setData(cardData);
				}
				tag.close();
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag reading error!");
			}
		} else {
			System.out.println("Ready!");
		}
	}
	
	//Liest einen ganzen Sector und gibt den String zurück
	private String readSector(MifareClassic tag, byte[] key, int sectorIndex){
		byte[] data;
		String cardData = null;
		if (tag.isConnected()){
			try{
				for (int i = tag.sectorToBlock(sectorIndex); i < (tag.sectorToBlock(sectorIndex)+tag.getBlockCountInSector(sectorIndex)); i++){
					if (tag.authenticateSectorWithKeyA(sectorIndex, key)){
						data = tag.readBlock(i);
						cardData = cardData.concat(getHexString(data, data.length));
					}
				}
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag reading error!");
			}
			if (cardData != null){
				return cardData;
			} else {
				return "";
			}
		} else {
			System.out.println("Tag disconnected!");
			return "";
		}
	}
	
	//Eigentliches Schreiben der einzelnen Blöcke
	//blockIndex sollte der Index des nächsten leeren Blocks sein, der nicht zum Ungültigmachen benötigt wird
	public void writeBlock(Intent intent, byte[] key, byte[] blockContent, int blockIndex){
		
	}
	
	//Auflösen des 2D-Arrays in schreibbare Blöcke
	public void writeSector(Intent intent, byte[] key, byte[][] sectorContent){
		
	}
	
	public void changeKey(Intent intent, byte[] oldKey, byte[] newKey){
		
	}
	
	//Gibt ersten gefundenen leeren Block zurück
	//der kein potentieller ungültig-Block ist
	private int getEmptyBlock(MifareClassic tag, byte[] key){
		byte[] emptyBlock = new byte[16];
		int emptyBlockIndex = 1;
		if (tag.isConnected()) {
			byte[] data;
			try {
				data = tag.readBlock(emptyBlockIndex); //Außer bei der Initialisierung sollte der Block nicht leer sein, da dort die Fahrer-Infos hingeschrieben werden
				int i = 1;
				while (!data.equals(emptyBlock)){ //Solange Block nicht frei
					i++; //nächsten lesen
					if (tag.authenticateSectorWithKeyA(tag.blockToSector(i), key)){
						data = tag.readBlock(i);
					} else {
						System.out.println("Authentication Failure");
					}
					if (data.equals(emptyBlock)){ //Falls Block frei, nächsten Block anschauen
						if (tag.authenticateSectorWithKeyA(tag.blockToSector(i+1), key)){
							data = tag.readBlock(i+1); //ist dieser frei, sollte die while-Schleife nicht nocheinmal durchlaufen werden
						} else {
							System.out.println("Authentication Failure");
						}
					}
				}
				emptyBlockIndex = i+1; //der Index des nächsten beschreibbaren Blocks, da der vorherige zum Ungültigmachen reserviert ist
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag reading error!");
			}
		} else {
			System.out.println("Tag disconnected!");
		}
		return emptyBlockIndex;
	}
	
	public static String getHexString(byte[] raw, int len) {
		byte[] hex = new byte[2 * len];
		int index = 0;
		int pos = 0;

		for (byte b : raw) {
			if (pos >= len)
				break;

			pos++;
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}

		return new String(hex);
	}
	
	public String toString(String[] stringArray) {
		String string = new String();
		for (int i=0; i < stringArray.length; i++){
			string += stringArray[i]+" ";
		}
	    return string;
	}

}