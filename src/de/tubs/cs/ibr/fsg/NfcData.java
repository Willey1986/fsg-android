/** 04.12.2012 - v2
 * 	t.luedtke@tu-bs.de
 *  This class generates the raw data, which then is written on the tags or interpret inputBlock vice versa.
 */
package de.tubs.cs.ibr.fsg;

import java.io.*;
//import java.io.IOException;
import java.security.SecureRandom;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class NfcData {
	//at the beginning of each sector there is a informationblock to get
	
	//constants for the encryption/decryption
	private final static String SECUREKEY = "KEY";
	
	/* reads out & converts the binary encoded data
	 */
	public static void interpretData(byte[][] inputBlock){
		System.out.println("ContentID: "+Byte.toString(inputBlock[0][0]));
		
		//TODO: needs to decrypt data before returning
		
		switch(inputBlock[0][0]){
        	case 10: //registrierungsdaten IDs
        		short[] test = new short[8];
        		for(int i=1;i<15;i+=2){
        			test[i/2] = (short) (((inputBlock[0][i+1]&0xFF) << 8) | (inputBlock[0][i]&0xFF));
        		}
        		System.out.println("fahrzeugID: "+test[0]);
        		System.out.println("userID: "+test[1]);
        		System.out.println("teamID: "+test[2]);
        		System.out.println("eventID: "+test[3]);
        		break;
        	case 11: //registrierungsdaten name
        		break;
        	case 20: //more
        		break;
        	default:
        		System.out.println("Error: contentID not readable!");
        		break;
        }
	}

	/*
	 * generates the data blocks for the reg. data
	 */
	public static byte[][] generateDataRegistration(Driver theDriver) throws FsgException, IOException {
		//convert the IDs now
		byte contentID 		= 10;
				
		short fahrzeugID 	= 0; //TODO:theDriver.get;
		short userID 		= (short) theDriver.getUser_id();
		short teamID 		= (short) theDriver.getTeam_id();
		short eventID 		= 1;

		//generate binary code with explicit size
		byte[][] outputBlock = new byte[2][16];
		
		//System.out.println("NfcData#generateDataRegistration failed");

		outputBlock[0][0] = contentID;
		
		outputBlock[0][1] = (byte)(fahrzeugID & 0xff);
		outputBlock[0][2] = (byte)((fahrzeugID >> 8) & 0xff);
		outputBlock[0][3] = (byte)(userID & 0xff);
		outputBlock[0][4] = (byte)((userID >> 8) & 0xff);
		outputBlock[0][5] = (byte)(teamID & 0xff);
		outputBlock[0][6] = (byte)((teamID >> 8) & 0xff);
		outputBlock[0][7] = (byte)(eventID & 0xff);
		outputBlock[0][8] = (byte)((eventID >> 8) & 0xff);
		for(int i=9;i<16;i++){
			outputBlock[0][i] = (byte) 0xff;
		}
		
		//read out and convert the name now
		contentID = 11;
		String prename, lastname;
		
		try{
			prename	= theDriver.getFirst_name().substring(0, 16);
			lastname = theDriver.getLast_name().substring(0, 1)+".";
		} catch (Exception  e) {
			throw new FsgException( e, "NfcData", FsgException.GENERIC_EXCEPTION);
		}
		
		String fullname = prename+" "+lastname;		
		System.out.println("NfcData#Name: "+fullname);
		
		/* >= 1Byte pro Buchstabe bei UTF-8
		 * >= 2Byte pro Buchstabe bei UTF-16 (UTF-16LE)
		 * 	Umlaute immer 2Byte
		 */
		outputBlock[1] = fullname.getBytes("UTF-8"); 
		
		return outputBlock;
	}
	
	public static byte[] toBytes(short s) {
        return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
    }
	
}	
