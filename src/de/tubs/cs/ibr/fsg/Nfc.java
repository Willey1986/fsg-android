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
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class Nfc {
	
	/**
	 * Gesamter Inhalt des NFC-Tags
	 */
	private String memoryContent;
	
	/**
	 * String zur Identifizierung im Log-Cat
	 */
	private static final String TAG = "FSG";

	/**
	 * Hilfstabelle für Hex
	 */
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
			(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
			(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

	/**
	 * Konstruktor
	 * @param context
	 */
	public Nfc(Context context){

	}
	
	/**
	 * Speichern des Tag-Inhalts
	 * @param cardData
	 */
	private void setData(String cardData){
		memoryContent = cardData;
	}
	
	/**
	 * Auslesen des zuletzt gespeicherten Tag-Inhalts
	 * @return
	 */
	private String getData(){
		return memoryContent;
	}
	
	/**
	 * Auslesen des Inhalts eines Tags
	 * @param intent
	 * @param key
	 * @return
	 * @throws FsgException
	 */
	public String getData(Intent intent, byte[] key) throws FsgException{
		readTag(intent, key);
		if (memoryContent != null)
			return memoryContent;
		else
			return "No Data";
	}
	
	public void resolveIntent(Intent intent) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			if (toString(tagFromIntent.getTechList()).contains("MifareClassic")){
				MifareClassic tag = MifareClassic.get(tagFromIntent);
				try{
					tag.connect();
					
					tag.close();
				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage());
					System.out.println("Tag reading error!");
					throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
				}
			}
		} else {
			
		}
	}
	
	
//	public void resolveIntent(Intent intent) throws FsgException{
//		String action = intent.getAction();
//		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
//			System.out.println("Discovered tag with intent: " + intent);
//			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//			System.out.println(toString(tagFromIntent.getTechList()));
//			if (toString(tagFromIntent.getTechList()).contains("MifareClassic")){
//				MifareClassic tag = MifareClassic.get(tagFromIntent);
//				byte[] data;
//				try{
//					tag.connect();
//					String cardData = null;
//					System.out.println("Authenticating the Tag...");
//					//Sector 0 mit Default Key authentifizieren - Key sollte noch geändert werden!
//					if (tag.authenticateSectorWithKeyA(0,MifareClassic.KEY_DEFAULT)){
//						data = tag.readBlock(1);
//						cardData = getHexString(data, data.length);
//						System.out.println("Tag reading successfull!");
//						System.out.println("Data: "+data);
//						System.out.println("CardData: "+cardData);
//						System.out.println("Key used: "+getHexString(MifareClassic.KEY_DEFAULT, MifareClassic.KEY_DEFAULT.length));
//					} else {
//						System.out.println("Authentication Failure!");
//					}
//					tag.close();
//				} catch (IOException e) {
//					Log.e(TAG, e.getLocalizedMessage());
//					System.out.println("Tag reading error!");
//					throw new FsgException("Fehler beim Lesen des Tags, eventuell Key falsch", e, this.getClass().toString(), FsgException.GENERIC_EXCEPTION);
//				}
//			}
//		} else {
//			System.out.println("Online + Scan a tag");
//		}
//	}
	
	/**
	 * Lesen eines gesamten Tags und Zwischenspeichern des Inhalts
	 * Geht nur, wenn alle Sektoren den gleichen Schlüssel verwenden!
	 * @param intent
	 * @param key
	 * @throws FsgException
	 */
	public void readTag(Intent intent, byte[] key) throws FsgException{
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
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
		} else {
			System.out.println("Ready!");
		}
	}
	
	/**
	 * Liest einen gegebenen Sektor eines gegebenen Tags.
	 * @param tag
	 * @param key
	 * @param sectorIndex
	 * @return
	 * @throws FsgException
	 */
	public String readSector(MifareClassic tag, byte[] key, int sectorIndex) throws FsgException{
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
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
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
	
	//Auflösen des 2D-Arrays in schreibbare Blöcke
	//und schreiben der Blöcke
	public void writeSector(MifareClassic tag, int sectorIndex, byte[] key, byte[][] sectorContent) throws FsgException{
		if (tag.isConnected()){
			
		} else {
			System.out.println("Tag disconnected!");
		}
	}
	
	/**
	 * Falls der SektorIndex nicht bekannt ist, wird zuerst der zuletzt beschriebene Sektor gesucht,
	 * bevor geschrieben wird.
	 * @param tag
	 * @param key
	 * @param sectorContent
	 * @throws FsgException
	 */
	public void writeSector(MifareClassic tag, byte[] key, byte[][] sectorContent) throws FsgException{
		writeSector(tag, getLastSector(tag, key), key, sectorContent);
	}
	
	public void writeTag(Intent intent, byte[] key, byte[][] content) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			try {
				tag.connect();
				int emptyBlock = getEmptyBlock(tag, key);
				if (content.length <= 16){
					System.out.println("1 Block");
				} else {
					System.out.println("Mehr als 1 Block");
				}
				tag.close();
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag reading error!");
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
		} else {
			System.out.println("Done!"); //Und jetzt am besten noch zurücklesen und vergleichen, ob erfolgreich
		}
	}
	
	//Mifare Key:
    //6 byte for key A
    //4 byte for Access Bits
    //6 byte for key B which is optional and can be set to 00 or any other value
	//d.h. für Key A = 00 11 22 33 44 55 und Access Bits = FF 0F 00 (default) muss geschrieben werden: 00 11 22 33 44 55 FF 0F 00 FF FF FF FF FF FF (Key B unchanged)
	
	/**
	 * Methode zum Ändern des Keys eines Sektors.
	 * @param tag
	 * @param sectorIndex
	 * @param oldKey
	 * @param newKey
	 */
	public void changeKey(MifareClassic tag, int sectorIndex, byte[] oldKey, byte[] newKey){
		if (tag.isConnected()){
			
		} else {
			System.out.println("Tag disconnected!");
		}
	}
	
	/**
	 * Die Methode getEmptyBlock sucht nach dem ersten unbeschriebenen Block, der nicht zum potentiellen
	 * Ungültigmachen des vorhergehenden Blocks benötigt wird.
	 * @param tag
	 * @param key
	 * @return
	 * @throws FsgException
	 */
	private int getEmptyBlock(MifareClassic tag, byte[] key) throws FsgException{
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
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
		} else {
			System.out.println("Tag disconnected!");
		}
		return emptyBlockIndex;
	}
	
	/**
	 * Die Methode getLastSector arbeitet unter der Annahme, dass der Tag sequentiell beschrieben wird.
	 * Ist dies der Fall, so wird der zum letzten beschriebenen Block gehörende Sektor zurückgegeben.
	 * @param MifareClassic tag
	 * @param byte[] key
	 * @return int lastSectorIndex
	 * @throws FsgException
	 */
	public int getLastSector(MifareClassic tag, byte[] key) throws FsgException{
		int lastSectorIndex = 1;
		if (tag.isConnected()){
			lastSectorIndex = tag.blockToSector(getEmptyBlock(tag, key)-2);
		} else {
			System.out.println("Tag disconnected!");
		}
		return lastSectorIndex;
	}
	
	/**
	 * Mehtode zur Umwandlung eines Byte-Arrays in einen String.
	 * @param raw
	 * @param len
	 * @return
	 */
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
	
	/**
	 * Methode zur leserlichen Ausgabe eines String-Arrays.
	 * @param stringArray
	 * @return
	 */
	public String toString(String[] stringArray) {
		String string = new String();
		for (int i=0; i < stringArray.length; i++){
			string += stringArray[i]+" ";
		}
	    return string;
	}

}