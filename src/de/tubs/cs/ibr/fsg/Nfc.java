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
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class Nfc {
	
	/**
	 * Gesamter Inhalt des NFC-Tags
	 * Array über die Sektoren
	 */
	//private String memoryContent = new String();
	private byte[][] memoryContent;
	
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
	
	/**
	 * Key A für Mifare Classic
	 */
	private byte[] keyA = MifareClassic.KEY_DEFAULT;
	
	/**
	 * Key B für Mifare Classic
	 */	
	private byte[] keyB = MifareClassic.KEY_DEFAULT;
	
	/**
	 * Default Rechte eines Sektors mit RW und Key B only
	 * TransportConfiguration ist FF 07 80 69
	 */
	private byte[] defaultRights = {(byte) '0', (byte) 'F', (byte) '0', (byte) '0', (byte) 'F', (byte) 'F', (byte) '0', (byte) '0'};
	
	/**
	 * Erster Block des zuletzt gelesenen Tags, enthält die eindeutige ID des Tags
	 */
	private String tagID = new String();
	
	/**
	 * Konstruktor
	 * @param context
	 */
	public Nfc(Context context){
		//TODO: Key auslesen und in Variable  speichern!
		//keyA = 
		//keyB = 
	}
	
	/**
	 * Speichern des Tag-Inhalts
	 * @param cardData
	 */	
	private void setData(byte[][] cardData){
		memoryContent = cardData;
	}
	
	/**
	 * Auslesen des zuletzt gespeicherten Tag-Inhalts
	 * @return
	 */
	public byte[][] getData(){
		if (memoryContent != null)
			return memoryContent;
		else
			return new byte[40][16];
	}
	
	private void setTagID(byte[] givenTagID) {
		tagID = getHexString(givenTagID, givenTagID.length);
	}
	
	public String getTagID() {
		return tagID;
	}
	
	private void setKeyA(byte[] newKeyA){
		if (keyA != newKeyA){
			keyA = newKeyA;
			//TODO: außerdem in Preferences abspeichern
		}
	}
	
	private void setKeyB(byte[] newKeyB){
		if (keyB != newKeyB){
			keyB = newKeyB;
			//TODO: außerdem in Preferences abspeichern
		}
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
		memoryContent = new byte[40][16];
	}
	
	public void resolveIntent(Intent intent) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
//			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//			if (toString(tagFromIntent.getTechList()).contains("MifareClassic")){
//				MifareClassic tag = MifareClassic.get(tagFromIntent);
				try{
//					tag.connect();
					readTag(intent); //Key irgendwo extern static speichern?
//					byte[][] byteArray = new byte[1][16];
//					//H=0x48, e=0x65, l=0x6C, l=0x6C, o=0x6F, W=0x57, o=0x6F, r=0x72, l=0x6C, d=0x64
//					byteArray[0][0] = (byte) 'T';
//					byteArray[0][1] = (byte) 'E';
//					byteArray[0][2] = (byte) 'S';
//					byteArray[0][3] = (byte) 'T';
//					byteArray[0][4] = (byte) ' ';
//					byteArray[0][5] = (byte) 'N';
//					byteArray[0][6] = (byte) 'U';
//					byteArray[0][7] = (byte) 'M';
//					byteArray[0][8] = (byte) 'M';
//					byteArray[0][9] = (byte) 'E';
//					byteArray[0][10] = (byte) 'R';
//					byteArray[0][11] = (byte) ' ';
//					byteArray[0][12] = (byte) '1';
//					byteArray[0][13] = (byte) 'E';
//					byteArray[0][14] = (byte) 'N';
//					byteArray[0][15] = (byte) 'C';
//					System.out.println("writing now bytearray");
//					writeTag(intent, byteArray);
//					tag.close();
				} catch (Exception e) {
					Log.e(TAG, e.getLocalizedMessage());
					System.out.println("Tag error!");
					throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
				}
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
	public void readTag(Intent intent) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			byte[] data;
			byte[][] content = new byte[tag.getBlockCount()][16];
			byte[] keyBlock = null;
			byte[] emptyBlock = new byte[16];
			try {
				tag.connect();
				if (tag.authenticateSectorWithKeyB(tag.blockToSector(7), keyB)){
					keyBlock = tag.readBlock(7);
				}
				if (tag.authenticateSectorWithKeyB(tag.blockToSector(0), keyB)){
					//TagID separat auslesen
					setTagID(tag.readBlock(0));
				}
				for (int i = 1; i < tag.getBlockCount(); i++){
					if (tag.authenticateSectorWithKeyB(tag.blockToSector(i), keyB)){
						data = tag.readBlock(i);
						// Sobald der erste leere Block erreicht wird, wird die Schleife verlassen.
						if(Arrays.equals(data, emptyBlock)){
							System.out.println("Empty Block");
							break;
						}
						// Key-Blöcke werden im Ergebnis Array nicht gespeichert. Setzt voraus, dass alle Key-Blöcke gleich aussehen!
						if (!Arrays.equals(data, keyBlock)){ 
							System.out.println("Content "+i+": "+getHexString(content[i], content[i].length));
							content[i-1] = data;
						}					
					}
				}
				if (!Arrays.equals(content, null)){ //content != null
					System.out.println("not null (Nfc.java)");
					byte[][] decryptedContent = read(content);
					setData(decryptedContent);
					System.out.println("Content: "+getHexString(decryptedContent));
				}
//				if (!cardData.isEmpty()){
//					System.out.println("CardData: "+cardData);
//					setData(cardData);
//				}
				System.out.println("end reading (Nfc.java)");
				tag.close();
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag reading error!");
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Unknown Tag error!");
				throw new FsgException( e, this.getClass().toString(), FsgException.SECURITY_FAIL);
			}
		}
	}
	
	/**
	 * Schreibe gegebenen Inhalt auf Tag, ab nächstem freien Block
	 * @param intent
	 * @param key
	 * @param content
	 * @throws FsgException
	 */
	public void writeTag(Intent intent, byte[][] encodedContent) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			System.out.println("writing");
			byte[][] encryptedContent = write(encodedContent);
			try {
				tag.connect();
				int emptyBlock = getEmptyBlock(tag);
				if ((emptyBlock + encryptedContent.length) <= tag.getBlockCount()){ //Anzahl leerer Blöcke größer/gleich Anzahl zu schreibender Blöcke, sonst voll
					System.out.println("content.length: "+encryptedContent.length);
					int[] writtenBlocks = new int[encryptedContent.length]; //Array mit den Blocknummern der geschriebenen Blöcke für spätere Verifizierung
					writtenBlocks[0] = emptyBlock;
				
					for (int i = 0; i < encryptedContent.length; i++){
						if (tag.authenticateSectorWithKeyB(tag.blockToSector(emptyBlock), keyB)){
							if (isEmpty(tag, emptyBlock)){//nur wenn Block tatsächlich leer wird geschrieben
								System.out.println("block is empty");
								tag.writeBlock(emptyBlock, encryptedContent[i]);
							
								System.out.println("done writing");
								emptyBlock++;
							} else {//was wenn nicht leer?
								emptyBlock = getEmptyBlock(tag); //neuer leerer Block wird gesucht
								writtenBlocks[i] = emptyBlock;
								if (emptyBlock == -1){
									throw new FsgException( new Exception(), this.getClass().toString(), FsgException.TAG_MEMORY_FULL);
								}
								i -= 1; //Einen Schritt zurückgehen, damit gesamter Content geschrieben wird und nicht ein Block übersprungen
							}
						} else {
							System.out.println("Authentication Failure 1");
						}
					}
					System.out.println("verifying");
					if(verify(tag, encryptedContent, writtenBlocks)){
						System.out.println("Writing successfull!");
					} else {
						System.out.println("Tag writing error!");
					}
				} else {
					System.out.println("Tag full!");
					throw new FsgException( new Exception(), this.getClass().toString(), FsgException.TAG_MEMORY_FULL);
				}
				tag.close();
				//System.out.println("Setting read-only");
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
	private boolean verify(MifareClassic tag, byte[][] content, int[] writtenBlocks) throws FsgException{
		if (tag.isConnected()){
			try {
				int lastBlock = 0;
				for(int i = 0; i < writtenBlocks.length; i++){
					if (writtenBlocks[i] != 0){
						lastBlock = i;
						if(tag.authenticateSectorWithKeyB(tag.blockToSector(writtenBlocks[i]), keyB)){					
							if (!Arrays.equals(tag.readBlock(writtenBlocks[i]), content[i])){//Bei Unterschied -> false
								return false;
							}
						}
					} else { //wenn nichts ins Array geschrieben wurde, wurde einfach hochgezählt
						int nextBlock = (i - lastBlock) + writtenBlocks[lastBlock];
						if(tag.authenticateSectorWithKeyB(tag.blockToSector(nextBlock), keyB)){					
							if (!Arrays.equals(tag.readBlock(nextBlock), content[i])){//Bei Unterschied -> false
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
	//d.h. für Key A = 00 11 22 33 44 55 und Access Bits = FF 0F 00(default) muss geschrieben werden: 00 11 22 33 44 55 FF 0F 00 FF FF FF FF FF FF (Key B unchanged)
	
	/**
	 * Methode zum Ändern der Keys und Rechte, Keys werden bei null nicht geändert, Rechte werden nur bei
	 * null dynamisch geändert (alle beschriebenen Blöcke werden read-only)
	 * @param intent
	 * @param sectorIndex
	 * @param newKeyA
	 * @param rights
	 * @param newKeyB
	 * @throws FsgException
	 */
	public void changeKey(Intent intent, int sectorIndex, byte[] newKeyA, byte[] rights, byte[] newKeyB) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			System.out.println("Changing Key");
			
			try {
				if (newKeyA == null){
					newKeyA = keyA;
				}
				if (newKeyB == null){
					newKeyB = keyB;
				}
				tag.connect();		
				if (rights == null){//alle beschriebenen Blöcke werden read-only gesetzt
					rights = new byte[tag.getBlockCountInSector(sectorIndex)];
					if (tag.getBlockCountInSector(sectorIndex) == 4){
						if (tag.authenticateSectorWithKeyB(sectorIndex, keyB)){
							for (int i = 0; i < tag.getBlockCountInSector(sectorIndex); i++){
								if (!Arrays.equals((tag.readBlock(tag.sectorToBlock(sectorIndex)+i)), new byte[16])){
									rights = setReadOnly(rights, i);
								} else {
									rights = setReadWrite(rights, i);
								}
									
							}
						}
					}
					if (tag.getBlockCountInSector(sectorIndex) == 16){
						if (tag.authenticateSectorWithKeyB(sectorIndex, keyB)){//Abfrage, alle 5 Blöcke müssen beschrieben sein
							for (int i = 0; i < tag.getBlockCountInSector(sectorIndex); i=i+5){
								boolean broken = false;
								for (int j = i; (j < i+5) && (j < tag.getBlockCountInSector(sectorIndex)); j++){
									if (Arrays.equals((tag.readBlock(tag.sectorToBlock(sectorIndex)+j)), new byte[16])){
										broken = true;
										break;
									}
								}//!broken bedeutet, Schleife wurde nicht abgebrochen, d.h. alle Blöcke sind beschrieben
								if (!broken){//i ist entweder 0, 5, 10 oder 15
									rights = setReadOnly(rights, i%4); //i%4 ist also 0, 1, 2 oder 3
								} else {
									rights = setReadWrite(rights, i%4);
								}
							}
						}
					}
				}
				String keyString = getHexString(newKeyA, newKeyA.length) + getHexString(rights, rights.length) + getHexString(newKeyB, newKeyB.length);
				byte[] keyBlock = new byte[16];
				keyBlock[0] = newKeyA[0];
				keyBlock[1] = newKeyA[1];
				keyBlock[2] = newKeyA[2];
				keyBlock[3] = newKeyA[3];
				keyBlock[4] = newKeyA[4];
				keyBlock[5] = newKeyA[5];
				keyBlock[6] = rights[0];
				keyBlock[7] = rights[1];
				keyBlock[8] = rights[2];
				keyBlock[9] = rights[3];
				keyBlock[10] = newKeyB[0];
				keyBlock[11] = newKeyB[1];
				keyBlock[12] = newKeyB[2];
				keyBlock[13] = newKeyB[3];
				keyBlock[14] = newKeyB[4];
				keyBlock[15] = newKeyB[5];
				if (tag.authenticateSectorWithKeyB(sectorIndex, keyB)){
					tag.writeBlock(tag.sectorToBlock(sectorIndex)+tag.getBlockCountInSector(sectorIndex)-1, keyBlock);
				}
				System.out.println("Key changed!");
				tag.close();
				if ((newKeyA != keyA) && (newKeyA != MifareClassic.KEY_DEFAULT)){
					setKeyA(newKeyA);
				}
				if ((newKeyB != keyB) && (newKeyB != MifareClassic.KEY_DEFAULT)){
					setKeyB(newKeyB);
				}
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				throw new FsgException(e1, this.getClass().toString(), FsgException.CHAR_ENCODE_FAILED);
			}	catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag writing error!");
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY);
			}
		} else {
			
		}
	}
	
	/**
	 * Initialisieren des Tags für Benutzung mit KeyB
	 * @param intent
	 * @throws FsgException
	 */
	public void initializeTag(Intent intent) throws FsgException{
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic tag = MifareClassic.get(tagFromIntent);
			System.out.println("Initializing Tag...");
			
			try {
				tag.connect();
				for (int sectorIndex = 0; sectorIndex < 40; sectorIndex++) {//Alle Sektoren durchgehen
					byte[] rights = new byte[tag.getBlockCountInSector(sectorIndex)];
					if (tag.authenticateSectorWithKeyA(sectorIndex, keyA)){
						//nicht read-only und Key B darf alles
						if (sectorIndex != 0){
							rights = setReadWrite(rights, 0);
							rights = setReadWrite(rights, 1);
							rights = setReadWrite(rights, 2);
							rights = setReadOnly(rights, 3);
						} else {
							rights = setReadOnly(rights, 0);//bei SectorIndex 0, darf Block 0 nicht rw sein, nur read-only
							rights = setReadWrite(rights, 1);
							rights = setReadWrite(rights, 2);
							rights = setReadOnly(rights, 3);
						}			
					}
					String keyString = getHexString(keyA, keyA.length) + getHexString(rights, rights.length) + getHexString(keyB, keyB.length);
					byte[] keyBlock = new byte[16];
					keyBlock[0] = keyA[0];
					keyBlock[1] = keyA[1];
					keyBlock[2] = keyA[2];
					keyBlock[3] = keyA[3];
					keyBlock[4] = keyA[4];
					keyBlock[5] = keyA[5];
					keyBlock[6] = rights[0];
					keyBlock[7] = rights[1];
					keyBlock[8] = rights[2];
					keyBlock[9] = rights[3];
					keyBlock[10] = keyB[0];
					keyBlock[11] = keyB[1];
					keyBlock[12] = keyB[2];
					keyBlock[13] = keyB[3];
					keyBlock[14] = keyB[4];
					keyBlock[15] = keyB[5];
					if (tag.authenticateSectorWithKeyA(sectorIndex, keyA)){
						tag.writeBlock(tag.sectorToBlock(sectorIndex)+tag.getBlockCountInSector(sectorIndex)-1, keyBlock);
					}
					System.out.println("Access changed: "+keyString);
				}
				tag.close();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				throw new FsgException(e1, this.getClass().toString(), FsgException.CHAR_ENCODE_FAILED);
			}	catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				System.out.println("Tag writing error!");
				throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY_OR_FORMAT);
			}
		}
	}
	
	private byte[] setReadOnly(byte[] oldRights, int i){
		byte Byte6 = oldRights[0];
		byte Byte7 = oldRights[1];
		byte Byte8 = oldRights[2];
		byte Byte9 = oldRights[3];
		switch(i){
		case 0: {
			Byte6 = setBit(Byte6, 0);
			Byte6 = flipBit(Byte6, 0);
			Byte6 = flipBit(Byte6, 4);
			Byte7 = setBit(Byte7, 0);	
			Byte7 = flipBit(Byte7, 0);
			Byte7 = setBit(Byte7, 4);
			Byte8 = setBit(Byte8, 4);
			break;
		}
		case 1: {
			Byte6 = setBit(Byte6, 1);
			Byte6 = flipBit(Byte6, 1);
			Byte6 = flipBit(Byte6, 5);
			Byte7 = setBit(Byte6, 1);
			Byte7 = flipBit(Byte6, 1);
			Byte7 = setBit(Byte7, 5);
			Byte8 = setBit(Byte8, 5);
			break;
		}
		case 2: {
			Byte6 = setBit(Byte6, 2);
			Byte6 = flipBit(Byte6, 2);
			Byte6 = flipBit(Byte6, 6);
			Byte7 = setBit(Byte6, 2);
			Byte7 = flipBit(Byte6, 2);
			Byte7 = setBit(Byte7, 6);
			Byte8 = setBit(Byte8, 6);
			break;
		}
		case 3: {//SectorTrailer
			Byte6 = flipBit(Byte6, 3);
			Byte6 = setBit(Byte6, 7);
			Byte6 = flipBit(Byte6, 7);
			Byte7 = setBit(Byte7, 3);
			Byte7 = flipBit(Byte7, 3);
			Byte8 = setBit(Byte8, 3);
			Byte8 = setBit(Byte8, 7);
			break;
		}
		}
		byte[] newRights = {Byte6, Byte7, Byte8, Byte9};
		return newRights;
	}
	
	private byte[] setReadWrite(byte[] oldRights, int i){
		byte Byte6 = oldRights[0];
		byte Byte7 = oldRights[1];
		byte Byte8 = oldRights[2];
		byte Byte9 = oldRights[3];
		switch(i){//Blöcke
		case 0: {
			Byte6 = flipBit(Byte6, 0);
			Byte6 = setBit(Byte6, 4);
			Byte6 = flipBit(Byte6, 4);
			Byte7 = setBit(Byte7, 0);	
			Byte7 = flipBit(Byte7, 0);
			Byte8 = setBit(Byte8, 0);
			Byte8 = setBit(Byte8, 4);
			break;
		}
		case 1: {
			Byte6 = flipBit(Byte6, 1);
			Byte6 = setBit(Byte6, 5);
			Byte6 = flipBit(Byte6, 5);
			Byte7 = setBit(Byte7, 1);	
			Byte7 = flipBit(Byte7, 1);
			Byte8 = setBit(Byte8, 1);
			Byte8 = setBit(Byte8, 5);
			break;
		}
		case 2: {
			Byte6 = flipBit(Byte6, 2);
			Byte6 = setBit(Byte6, 6);
			Byte6 = flipBit(Byte6, 6);
			Byte7 = setBit(Byte7, 2);	
			Byte7 = flipBit(Byte7, 2);
			Byte8 = setBit(Byte8, 2);
			Byte8 = setBit(Byte8, 6);
			break;
		}
		case 3: {//SectorTrailer
			Byte6 = flipBit(Byte6, 3);
			Byte6 = setBit(Byte6, 7);
			Byte6 = flipBit(Byte6, 7);
			Byte7 = setBit(Byte7, 3);
			Byte7 = flipBit(Byte7, 3);
			Byte8 = setBit(Byte8, 3);
			Byte8 = setBit(Byte8, 7);
			break;
		}
		}
		byte[] newRights = {Byte6, Byte7, Byte8, Byte9};
		return newRights;
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
	
	private int getEmptyBlock(MifareClassic tag) throws FsgException{
		byte[] emptyBlock = new byte[16];
		int emptyBlockIndex = 0; //Weil erster Block des Tags Hersteller-Infos enthält
		if (tag.isConnected()) {
			byte[] data = null;
			try {
				if (tag.authenticateSectorWithKeyB(tag.blockToSector(emptyBlockIndex), keyB)){
					data = tag.readBlock(emptyBlockIndex); //Außer bei der Initialisierung sollte der Block 1 nicht leer sein, da dort die Fahrer-Infos hingeschrieben werden
				} else {
					System.out.println("Authentication Failure 2");
					throw new FsgException( new Exception(), this.getClass().toString(), FsgException.TAG_WRONG_KEY_OR_FORMAT);
				}
				while ((!Arrays.equals(data, emptyBlock)) && (emptyBlockIndex < tag.getBlockCount()-1)){ //Solange Block nicht frei, letzter Block wird nicht geschaut, der soll frei bleiben
					++emptyBlockIndex; //nächsten lesen
					if (tag.authenticateSectorWithKeyB(tag.blockToSector(emptyBlockIndex), keyB)){ 
						data = tag.readBlock(emptyBlockIndex); 
					} else {
						System.out.println("Authentication Failure 3");
						throw new FsgException( new Exception(), this.getClass().toString(), FsgException.TAG_WRONG_KEY_OR_FORMAT);
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
	private boolean isEmpty(MifareClassic tag, int blockIndex) throws FsgException{
		byte[] emptyBlock = new byte[16];
		if (tag.isConnected()){
			try {
				tag.authenticateSectorWithKeyB(tag.blockToSector(blockIndex), keyB);
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
	public int getLastSector(MifareClassic tag) throws FsgException{
		int lastSectorIndex = 1;
		if (tag.isConnected()){
			lastSectorIndex = tag.blockToSector(getEmptyBlock(tag));
		} else {
			System.out.println("Tag disconnected!");
		}
		return lastSectorIndex;
	}
	
	public void cleanTag(Intent intent) throws FsgException{
		//System.out.println("Cleaning Tag...");
		byte[] rights = new byte[4];
		rights[0] = setBit(rights[0], 0);
		rights[0] = setBit(rights[0], 1);
		rights[0] = setBit(rights[0], 2);
		rights[0] = setBit(rights[0], 3);
		rights[0] = setBit(rights[0], 4);
		rights[0] = setBit(rights[0], 5);
		rights[0] = setBit(rights[0], 6);
		rights[0] = setBit(rights[0], 7);
		rights[1] = setBit(rights[1], 0);
		rights[1] = setBit(rights[1], 1);
		rights[1] = setBit(rights[1], 2);
		rights[2] = setBit(rights[2], 7);
		
		try{
			for (int i = 0; i < 40; i++){
				changeKey(intent, i, MifareClassic.KEY_DEFAULT, rights, MifareClassic.KEY_DEFAULT);
			}
			if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
				Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				MifareClassic tag = MifareClassic.get(tagFromIntent);
				tag.connect();
				byte[] keyBlock = null;
				byte[] data = null;
				byte[] emptyBlock = new byte[16];
				if (tag.authenticateSectorWithKeyA(tag.blockToSector(7), keyB)){
					keyBlock = tag.readBlock(7);
				}
				for (int i = 1; i < tag.getBlockCount(); i++){
					if(tag.authenticateSectorWithKeyA(tag.blockToSector(i), MifareClassic.KEY_DEFAULT)){
						data = tag.readBlock(i);
						// Sobald der erste leere Block erreicht wird, wird die Schleife verlassen.
						if(Arrays.equals(data, emptyBlock)){ 
							break;
						}
						if (!Arrays.equals(data, keyBlock)){
							tag.writeBlock(i, emptyBlock);
						}
					}
				}
				tag.close();
			}
		} catch (IOException e) {
			Log.e(TAG, e.getLocalizedMessage());
			System.out.println("Tag cleaning error!");
			throw new FsgException( e, this.getClass().toString(), FsgException.TAG_WRONG_KEY_OR_FORMAT);
		}
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
	
	private byte setBit( byte n, int pos ){
		return (byte) (n | (1 << pos));
	}
	
	private byte flipBit( byte n, int pos ){
	  return (byte) (n ^ (1 << pos));
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
	public String getHexString(byte[][] byteArray) {
		String string = new String();
		for (int i=0; i < byteArray.length; i++){
			string += getHexString(byteArray[i], byteArray[i].length)+" ";
		}
	    return string;
	}
	
	private static byte[][] write(byte[][] raw) throws FsgException{
		SecurityManager sManager = new SecurityManager(PASSWORD);
		
		//return sManager.encryptString(raw);
		return raw;
	}
	
	private static byte[][] read(byte[][] raw)throws FsgException{
		SecurityManager sManager = new SecurityManager(PASSWORD);
		
		//return sManager.decryptString(raw);
		return raw;
	}

}