/** 20.11.2012 - v2
 * 	t.luedtke@tu-bs.de
 *  This class should generate the raw data, which then is written on the tags (or interpoutputBlock vice versa).
 */
package de.tubs.cs.ibr.fsg;

import java.security.SecureRandom;
import de.tubs.cs.ibr.fsg.db.models.Driver;

public class NfcData {
	//testvariables
	private static short fahrzeugID = 59;
	private static short userID 	= 18664;
	private static short teamID 	= 33;
	private static short eventID 	= 1;
	
	//at the beginning of each sector there is a informationblock to get
	
	//max 1 Block
	private static String name 		= "Alexander M.";
	
	//constants for the encryption/decryption
	private final static String SECUREKEY = "KEY";
	
	/*
	 * reads out & converts the binary encoded data
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
	public static byte[][] generateDataRegistration(Driver theDriver){
		byte contentID = 10;
		//String input1 = "{"UserID":18664,"TeamID":33,"first_name":"Alexander","last_name":"Mustermann","gender":0}";
		//String input2 = "{"TeamID":33,"CN":"AT","cn_short_en":"Austria","city":"Graz","U":"TU","Car":59,"Pit":49,"iswaiting":0,"class":1,"name_pits":"TU Graz"}";

		//generate binary code
		byte[][] outputBlock = new byte[1][16];
		
		System.out.println("NfcData#generateDataRegistration failed");

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
		
		
		return outputBlock;
	}
	
	public static byte[] toBytes(short s) {
        return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
    }
	
}	
