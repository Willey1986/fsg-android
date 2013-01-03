package de.tubs.cs.ibr.fsg.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class FsgProtocol {
	
	public final static int UP_REQ_DRIVERS           = 1;
	public final static int UP_REQ_TEAMS             = 2; 
	public final static int UP_REQ_BLACK_WRISTLETS   = 3;
	public final static int UP_REQ_BLACK_DEVICES     = 4;
	public final static int UP_REQ_WRISTLET_SINGLE   = 5; 
	public final static int UP_REQ_WRISTLET_MULTI    = 6;
	public final static int UP_REQ_DRIVER_PICS       = 7;

	public final static int DATA_DRIVERS             = 11; 
	public final static int DATA_TEAMS               = 12;
	public final static int DATA_BLACK_WRISTLETS     = 13;
	public final static int DATA_BLACK_DEVICES       = 14;
	public final static int DATA_WRISTLET_SINGLE     = 15;
	public final static int DATA_WRISTLET_MULTI      = 16;
	public final static int DATA_DRIVER_PICS         = 17;
	
	public final static int CONFIRM_DRIVERS          = 21; 
	public final static int CONFIRM_TEAMS            = 22;
	public final static int CONFIRM_BLACK_WRISTLETS  = 23;
	public final static int CONFIRM_BLACK_DEVICES    = 24;
	public final static int CONFIRM_WRISTLET_SINGLE  = 25;
	public final static int CONFIRM_WRISTLET_MULTI   = 26;
	public final static int CONFIRM_DRIVER_PICS      = 27;
	
	public final static int UP_REQ_DRIVERS_TEAMS     = 101;
	public final static int UP_REQ_DRIVERS_TEAMS_DP  = 102;
	public final static int UP_REQ_BOTH_BLACK        = 103;
	public final static int UP_REQ_DRIV_TE_BLWR_BLDE = 104;
	
	
	
	/**
	 * Mit Hilfe dieser Methode kann man einen Byte-Array generieren, der den Fsg-Protokoll entspricht
	 * und so an IBR-DTN abgegeben werden kann, um gesendet zu werden.
	 * 
	 * @param type "Nachrichtentyp" nach dem FsgProtokoll (Header)
	 * @param version "Version" nach dem FsgProtokoll (Header)
	 * @param payload Eigentlicher Nachrichteninhalt
	 * @return Byte-Array mit dem Fsg-Protokoll-Header und Nachrichteninhalt
	 */
	public static byte[] getByteArrayToSend(int type, int version, String payload){
		byte[] result = null;
		
		byte[] typeByteArray    = convertIntToByteArray(type);
		byte[] versionByteArray = convertIntToByteArray(version);
		byte[] payloadByteArray = payload.getBytes();
		
		int completeLength = (payloadByteArray.length + 5);
		result = new byte[completeLength];
		
		// Header Teil 1 (von 2)
		// Wir speichern den Typ der Nachricht (1 Byte)
		result[0] = typeByteArray[3];
		
		// Header Teil 2 (von 2)
		// Wir speichern die Version der Nachricht (4 Bytes)
		for (int i=0; i<4 ; i++){
			result[i+1] = versionByteArray[i];
		}
		
		// Payload
		// Wir speichern den Inhalt Nachricht (nicht immer vorhanden, z.B. bei Updates-Anforderungen)
		for (int i=0; i<payloadByteArray.length ; i++){
			result[i+5] = payloadByteArray[i];
		}
		
		return result;
	}
	
	
	/**
	 * Mit dieser Methode kann man aus einem empfangenen Buendel(als Datei) die 
	 * Header-Angaben (Nachrichtentyp, Version) von dem Payload (JSON-String, Fahrerbilder, usw)
	 * trennen und in einem FsgPackage-Objekt packen.
	 * 
	 * @param receivedFile Mit IBR-DTN empfangene Datei, wo die Header-Daten und die eigentlichen Daten enthalten sind.
	 * @return FsgPackage-Objekt mit dem FsgProtokoll-Header (Nachrichtentyp, Version) und der eigentlichen Nachrichteninhalt
	 * @throws FsgException 
	 */
	public static FsgPackage getFsgPackage(File receivedFile) throws FsgException {
		int packageType = 0;
		int version = 0;
		byte[] payload = null;

		InputStream input = null;

		try {
			if (receivedFile.length() < 5) {
				throw new FsgException(new Exception(), new FsgProtocol().getClass().toString(), FsgException.GENERIC_EXCEPTION);
			}
			input = new BufferedInputStream(new FileInputStream(receivedFile));

			// Zuerst finden wir den Typ der Nachricht heraus.
			byte[] typeByte = new byte[1];
			input.read(typeByte, 0 ,1) ;
			packageType= Byte.valueOf( typeByte[0] ).intValue();
			
			// Jetzt schauen wir nach der Version
			byte[] versionBytes = new byte[4];
			input.read(versionBytes, 0 ,4) ;
			version = convertByteArrayToInt(versionBytes);
			
			
			// Schliesslich die eigentlichen Daten, den Payload
			int payloadLength = (int) receivedFile.length()-5;
			if(payloadLength > 0){
				
				payload = new byte[payloadLength];
				int totalBytesRead = 0;
				while (totalBytesRead < payload.length) {
					int bytesRemaining = payload.length - totalBytesRead;
					int bytesRead = input.read(payload, totalBytesRead, bytesRemaining);
					if (bytesRead > 0) {
						totalBytesRead = totalBytesRead + bytesRead;
					}
				}
				
			}else{
				// Es gibt da keine Nachricht an sich. Das kann z.B. der Fall sein, wenn eine Update-Anforderung verschickt wird.
				// In diesem Fall lassen wir die Variable null bleiben.
			}


		} catch (FsgException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new FsgException(new Exception(), new FsgProtocol().getClass().toString(), FsgException.GENERIC_EXCEPTION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new FsgException(new Exception(), new FsgProtocol().getClass().toString(), FsgException.GENERIC_EXCEPTION);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new FsgException(new Exception(), new FsgProtocol().getClass().toString(), FsgException.GENERIC_EXCEPTION);
			}
		}

		FsgPackage mFsgPackage = new FsgPackage(packageType, version, payload);
		//String payloadString = new String(payload); //Just for test
		//System.out.println(payloadString);
		return mFsgPackage;
	}
	

    /**
     * Diese Methode wandelt einen Byte-Array in einen Integer-Wert.
     * 
     * @param byteArray Ein Byte-Array der Laenge 4.
     * @return Integer-Wert, der aus dem Byte-Array gewonnen wurde.
     * @throws FsgException Falls der mitgegebenem Array nicht die Laenge 4 hat, dann wird diese Exception ausgeloest.
     */
    private static int convertByteArrayToInt(byte[] byteArray) throws FsgException {
        if (byteArray.length != 4) {
        	throw new FsgException( new IllegalArgumentException("Error: The length of the Byte-Array must be 4 bytes!"), 
        			new FsgProtocol().getClass().toString(), FsgException.GENERIC_EXCEPTION);
        }
        
        int 
        value  = (0xFF & byteArray[0]) << 24 ;
        value |= (0xFF & byteArray[1]) << 16;
        value |= (0xFF & byteArray[2]) << 8;
        value |= (0xFF & byteArray[3]);
        
        return value;
    }
   
    
    /**
     * Mit dieser Methode koennen wir einen Integer-Wert in einem Byte-Array umwndeln.
     * Die Laenge des Arrays betraegt genau 4 Bytes (fuer einen 32-bit Integer!).
     * 
     * @param intValue Der Integer-Wert, der umzuwandeln ist.
     * @return Der Byte-Array mit Laenge 4, der den umgewandelten Integer beinhaltet.
     */
    private static byte[] convertIntToByteArray(int intValue) {
        byte[] byteArray = new byte[4];
   
        byteArray[0] = (byte) (intValue >>> 24);
        byteArray[1] = (byte) (intValue >>> 16);
        byteArray[2] = (byte) (intValue >>> 8);
        byteArray[3] = (byte)  intValue;
        
        return byteArray;
    }
	
}
