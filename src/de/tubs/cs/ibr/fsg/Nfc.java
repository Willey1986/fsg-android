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
import java.util.Arrays;
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
	 * Array über die Sektoren
	 */
	private String[] memoryContent = new String[40];
	
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
	 * Passwort als Konstante
	 */
	final static String PASSWORD = "test";
	
	private String key = null;
	
	/**
	 * Konstruktor
	 * @param context
	 */
	public Nfc(Context context){
		//Key auslesen und in Variable key speichern!

	}
	
	/**
	 * Speichern des Tag-Inhalts
	 * @param cardData
	 */
	private void setData(String[] cardData){
		memoryContent = cardData;
	}
	
	/**
	 * Auslesen des zuletzt gespeicherten Tag-Inhalts
	 * @return
	 */
	public String[] getData(){
		if (memoryContent != null)
			return memoryContent;
		else
			return new String[0];
	}
	
	/**
	 * Auslesen des Inhalts eines Tags
	 * @param intent
	 * @param key
	 * @return
	 * @throws FsgException
	 */
//	public String[] getData(Intent intent, byte[] key) throws FsgException{
//		readTag(intent, key);
//		if (memoryContent != null)
//			return memoryContent;
//		else
//			return new String[0];
//	}
	
	public void clearContent(){
		memoryContent = new String[40];
	}
	
	public void resolveIntent(Intent intent) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
//			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//			if (toString(tagFromIntent.getTechList()).contains("MifareClassic")){
//				MifareClassic tag = MifareClassic.get(tagFromIntent);
//				try{
//					tag.connect();
					readTag(intent, MifareClassic.KEY_DEFAULT); //Key irgendwo extern static speichern?
					//byte[][] byteArray = new byte[1][16];
					//H=0x48, e=0x65, l=0x6C, l=0x6C, o=0x6F, W=0x57, o=0x6F, r=0x72, l=0x6C, d=0x64
//					byteArray[0][0] = 0x48;
//					byteArray[0][1] = 0x65;
//					byteArray[0][2] = 0x6C;
//					byteArray[0][3] = 0x6C;
//					byteArray[0][4] = 0x6F;
//					byteArray[0][5] = 0x00;
//					byteArray[0][6] = 0x57;
//					byteArray[0][7] = 0x6F;
//					byteArray[0][8] = 0x72;
//					byteArray[0][9] = 0x6C;
//					byteArray[0][10] = 0x64;
//					byteArray[0][11] = 0x00;
//					byteArray[0][12] = 0x00;
//					byteArray[0][13] = 0x00;
//					byteArray[0][14] = 0x00;
//					byteArray[0][15] = 0x00;
//					writeTag(intent, MifareClassic.KEY_DEFAULT, byteArray);
//					tag.close();
//				} catch (IOException e) {
//					Log.e(TAG, e.getLocalizedMessage());
//					System.out.println("Tag reading error!");
//					throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
//				}
//			}
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
				tag.connect();
				String[] cardData = new String[tag.getSectorCount()];
				for (int i = 0; i < tag.getBlockCount(); i++){
					if (tag.authenticateSectorWithKeyA(tag.blockToSector(i), key)){
						
						//decrypt data in read method
						data = read(tag.readBlock(i));
						cardData[tag.blockToSector(i)] += getHexString(data, data.length);
					}
				}
				if (cardData != null){
					System.out.println(cardData[0]);
					setData(cardData);
				}
				tag.close();
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag reading error!");
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
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
						
						//decrypt blocks using read method
						data = read(tag.readBlock(i));
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
	
	/**
	 * Schreibe gegebenen Inhalt auf Tag, ab nächstem freien Block
	 * @param intent
	 * @param key
	 * @param content
	 * @throws FsgException
	 */
	public void writeTag(Intent intent, byte[] key, byte[][] content) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			System.out.println("writing");
			try {
				tag.connect();
				int emptyBlock = getEmptyBlock(tag, key);
				if ((emptyBlock != -1) && (emptyBlock < tag.getBlockCount())){
					int[] writtenBlocks = new int[content.length]; //Array mit den Blocknummern der geschriebenen Blöcke für spätere Verifizierung
					writtenBlocks[0] = emptyBlock;
				
					for (int i = 0; i < content.length; i++){
						if (tag.authenticateSectorWithKeyA(tag.blockToSector(emptyBlock), key)){
							if (isEmpty(tag, key, emptyBlock)){//nur wenn Block tatsächlich leer wird geschrieben
								System.out.println("block is empty");
							
								//write encrypted data
								tag.writeBlock(emptyBlock, content[i]);
							
								System.out.println("done writing");
								emptyBlock++;
							} else {//was wenn nicht leer?
								emptyBlock = getEmptyBlock(tag, key); //neuer leerer Block wird gesucht
								writtenBlocks[i] = emptyBlock;
								if (emptyBlock == -1){
									throw new FsgException( new Exception(), this.getClass().toString(), FsgException.TAG_MEMORY_FULL);
								}
								i -= 1; //Einen Schritt zurückgehen, damit gesamter Content geschrieben wird und nicht ein Block übersprungen
							}
						} else {
							System.out.println("Authentication Failure");
						}
					}
					System.out.println("verifying");
					if(verify(tag, key, content, writtenBlocks)){
						System.out.println("Writing successfull!");
					} else {
						System.out.println("Tag writing error!");
					}
				} else {
					System.out.println("Tag full!");
					throw new FsgException( new Exception(), this.getClass().toString(), FsgException.TAG_MEMORY_FULL);
				}
				tag.close();
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag writing error!");
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
		} else {
			
		}
	}
	
	/**
	 * Gerade geschriebenen Inhalt Überprüfen, wenn sich der Inhalt vom geschriebenen unterscheidet => false, sonst true.
	 * @param tag
	 * @param key
	 * @param content
	 * @param blockIndex
	 * @return
	 * @throws FsgException
	 */
	private boolean verify(MifareClassic tag, byte[] key, byte[][] content, int[] writtenBlocks) throws FsgException{
		if (tag.isConnected()){
			try {
				int lastBlock = 0;
				for(int i = 0; i < writtenBlocks.length; i++){
					if (writtenBlocks[i] != 0){
						lastBlock = i;
						if(tag.authenticateSectorWithKeyA(tag.blockToSector(writtenBlocks[i]), key)){					
							if (!Arrays.equals(tag.readBlock(writtenBlocks[i]), write(content[i]))){//Bei Unterschied -> false
								return false;
							}
						}
					} else { //wenn nichts ins Array geschrieben wurde, wurde einfach hochgezählt
						int nextBlock = (i - lastBlock) + writtenBlocks[lastBlock];
						if(tag.authenticateSectorWithKeyA(tag.blockToSector(nextBlock), key)){					
							if (!Arrays.equals(tag.readBlock(nextBlock), write(content[i]))){//Bei Unterschied -> false
								return false;
							}
						}
					}
				}
			} catch (IOException e) {
				throw new FsgException(e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
			return true;
		} else {
			return false;
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
			//Unterscheidung zwischen großem und kleinem Sektor (4 vs 16 Blöcke)
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
//	private int getEmptyBlock(MifareClassic tag, byte[] key) throws FsgException{
//		byte[] emptyBlock = new byte[16];
//		int emptyBlockIndex = 1; //Weil erster Block des Tags Hersteller-Infos enthält
//		if (tag.isConnected()) {
//			byte[] data = null;
//			try {
//				if (tag.authenticateSectorWithKeyA(tag.blockToSector(emptyBlockIndex), key)){
//					data = tag.readBlock(emptyBlockIndex); //Außer bei der Initialisierung sollte der Block 1 nicht leer sein, da dort die Fahrer-Infos hingeschrieben werden
//				} else {
//					System.out.println("Authentication Failure");
//				}
//				while ((!Arrays.equals(data, emptyBlock)) && (emptyBlockIndex < tag.getBlockCount())){ //Solange Block nicht frei
//					if (tag.authenticateSectorWithKeyA(tag.blockToSector(emptyBlockIndex), key)){ 
//						data = tag.readBlock(emptyBlockIndex++); //nächsten lesen
//					} else {
//						System.out.println("Authentication Failure");
//					}
//					if (Arrays.equals(data, emptyBlock)){ //Falls Block frei, nächsten Block anschauen
//						emptyBlockIndex++;
//						if (tag.authenticateSectorWithKeyA(tag.blockToSector(emptyBlockIndex), key)){
//							data = tag.readBlock(emptyBlockIndex); //ist dieser frei, sollte die while-Schleife nicht nocheinmal durchlaufen werden
//						} else {
//							System.out.println("Authentication Failure");
//						}
//						if(Arrays.equals(data, key)){//falls Key -> nächsten Sektor und Block
//							if (tag.authenticateSectorWithKeyA(tag.blockToSector(emptyBlockIndex+2), key)){
//								emptyBlockIndex += 2; //weil key übersprungen werden soll
//								data = tag.readBlock(emptyBlockIndex); //ist dieser frei, sollte die while-Schleife nicht nocheinmal durchlaufen werden
//							} else {
//								System.out.println("Authentication Failure");
//							}
//						}
//					}
//				}	
//			} catch (IOException e) {
//				Log.e(TAG, e.getLocalizedMessage());
//				System.out.println("Tag reading error!");
//				e.printStackTrace();
//				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
//			}
//		} else {
//			System.out.println("Tag disconnected!");
//		}
//		if (emptyBlockIndex < tag.getBlockCount()){
//			return emptyBlockIndex;
//		} else {
//			return -1;
//		}
//	}
	
	private int getEmptyBlock(MifareClassic tag, byte[] key) throws FsgException{
		byte[] emptyBlock = new byte[16];
		int emptyBlockIndex = 1; //Weil erster Block des Tags Hersteller-Infos enthält
		if (tag.isConnected()) {
			byte[] data = null;
			try {
				if (tag.authenticateSectorWithKeyA(tag.blockToSector(emptyBlockIndex), key)){
					data = tag.readBlock(emptyBlockIndex); //Außer bei der Initialisierung sollte der Block 1 nicht leer sein, da dort die Fahrer-Infos hingeschrieben werden
				} else {
					System.out.println("Authentication Failure");
				}
				while ((!Arrays.equals(data, emptyBlock)) && (emptyBlockIndex < tag.getBlockCount()-1)){ //Solange Block nicht frei, letzter Block wird nicht geschaut, der soll frei bleiben
					++emptyBlockIndex;
					if (tag.authenticateSectorWithKeyA(tag.blockToSector(emptyBlockIndex), key)){ 
						data = tag.readBlock(emptyBlockIndex); //nächsten lesen
					} else {
						System.out.println("Authentication Failure");
					}
				}
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag reading error!");
				e.printStackTrace();
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
		} else {
			System.out.println("Tag disconnected!");
		}
		if (emptyBlockIndex < tag.getBlockCount()-1){
			return emptyBlockIndex;
		} else {
			return -1;
		}
	}
	
	
	/**
	 * Überprüft, ob gegebener Block leer ist.
	 * @param tag
	 * @param key
	 * @param blockIndex
	 * @return
	 * @throws FsgException
	 */
	private boolean isEmpty(MifareClassic tag, byte[] key, int blockIndex) throws FsgException{
		byte[] emptyBlock = new byte[16];
		if (tag.isConnected()){
			try {
				tag.authenticateSectorWithKeyA(tag.blockToSector(blockIndex), key);
				if (Arrays.equals(tag.readBlock(blockIndex), emptyBlock)){
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				throw new FsgException(e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
		} else {
			return false;
		}
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
			lastSectorIndex = tag.blockToSector(getEmptyBlock(tag, key));
		} else {
			System.out.println("Tag disconnected!");
		}
		return lastSectorIndex;
	}
	
	/**
	 * Methode zur Umwandlung eines Byte-Arrays in einen String.
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
	
	private static byte[] write(byte[] raw) throws FsgException{
		SecurityManager sManager = new SecurityManager(PASSWORD);
		
		//return sManager.encryptString(raw);
		return raw;
	}
	
	private static byte[] read(byte[]raw)throws FsgException{
		SecurityManager sManager = new SecurityManager(PASSWORD);
		
		//return sManager.decryptString(raw);
		return raw;
	}

}