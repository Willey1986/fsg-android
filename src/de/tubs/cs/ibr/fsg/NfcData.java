/**	t.luedtke@tu-bs.de
 *  This class generates the raw data, which then is written on the tags or interpret inputBlock vice versa.
 */
package de.tubs.cs.ibr.fsg;

import java.io.*;
import java.util.Date.*;
import de.tubs.cs.ibr.fsg.NfcObject;
import de.tubs.cs.ibr.fsg.NfcObjectBriefing;
import de.tubs.cs.ibr.fsg.db.models.Driver;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class NfcData {
	private static long tstampConstant = 1357174835;//1357174835445L; 
	//at the beginning of each sector there is a informationblock to get
	
	//constants for the encryption/decryption
	//private final static String SECUREKEY = "KEY";
	
	/* reads out & converts the binary encoded data
	 */
	public static NfcObject interpretData(byte[][] inputBlock) throws FsgException{
		NfcObject outputObject = new NfcObject();		
		
		//read out the complete inputArray
		for(int i=0;i<inputBlock.length;i++){
				//System.out.println("ContentID: "+Byte.toString(inputBlock[i][0]));
			
			switch(inputBlock[i][0]){
				case 10: //Registrierungsdaten IDs
					short[] test = new short[8];
					for(int j=1;j<15;j+=2){
						test[j/2] = (short) (((inputBlock[i][j+1]&0xFF) << 8) | (inputBlock[i][j]&0xFF));
					}					
					outputObject.DriverObject.setTeam_id(test[2]);
					outputObject.DriverObject.setUser_id(test[1]);
					outputObject.DriverObject.getTeam().setCarNr(test[0]);
					outputObject.setEventID(test[3]);
						/*
						System.out.println("fahrzeugID: "+test[0]);
						System.out.println("userID: "+test[1]);
						System.out.println("teamID: "+test[2]);
						System.out.println("eventID: "+test[3]);*/
					break;
					
				case 11: //Registrierungsdaten name        		
					try{
						String str = new String(inputBlock[i], "UTF-8");
						str = str.substring(1, str.length());

						// extract first and lastname from the string
						outputObject.DriverObject.setFirst_name(str.substring(0,str.lastIndexOf(" ")));
						outputObject.DriverObject.setLast_name(str.substring(str.lastIndexOf(" ")+1));
							//System.out.println("extraced Name: "+ outputObject.DriverObject.getFirst_name()+" "+outputObject.DriverObject.getLast_name());
					} catch (Exception e) {
						throw new FsgException( e, "NfcData", FsgException.CHAR_DECODE_FAILED);
					}
					break;
					
				case 20: //Check IN
					//getTIME System.out.println(new Timestamp(date.getTime()));
					short briefingID	= (short) (((inputBlock[i][2]&0xFF) << 8) | (inputBlock[i][1]&0xFF));
					int tstamp 			= (int) (((inputBlock[i][6]&0xFF) << 32) | ((inputBlock[i][5]&0xFF) << 16) | ((inputBlock[i][4]&0xFF) << 8) | (inputBlock[i][3]&0xFF));
					long tstamp2 		= (Long.parseLong(String.valueOf(tstamp))+tstampConstant)*1000;			

					NfcObjectBriefing newBriefing = new NfcObjectBriefing();
					newBriefing.setBriefingID(briefingID);
					newBriefing.setTimestamp(tstamp2);
					
					outputObject.addBriefing(newBriefing);
					
						//System.out.println("briefingID: "+briefingID);
						//System.out.println("TimestampOut: "+tstamp2);
						//System.out.println("TimeIN: "+new java.util.Date(tstamp2));
					break;
					
				case 21: //Check OUT
					short briefingIDo 	= (short) (((inputBlock[i][2]&0xFF) << 8) | (inputBlock[i][1]&0xFF));
					int tstampo 		= (int) (((inputBlock[i][6]&0xFF) << 32) | ((inputBlock[i][5]&0xFF) << 16) | ((inputBlock[i][4]&0xFF) << 8) | (inputBlock[i][3]&0xFF));
					long tstamp2o 		= (Long.parseLong(String.valueOf(tstampo))+tstampConstant)*1000;
					
					outputObject.removeBriefingByID(briefingIDo);
					break;
					
				case 40: //RUNS DONE
					break;
					
				default:
					System.out.println("Error: contentID "+inputBlock[i][0]+" not readable!");
					break;
			}
		}
		return outputObject;
	}

	/*
	 * generates the data blocks for the reg. data
	 */
	public static byte[][] generateDataRegistration(Driver theDriver) throws FsgException, IOException {	
		//check for working driver object
		try{
			theDriver.getTeam_id();
		} catch (Exception  e) {
			throw new FsgException( e, "NfcData", FsgException.NON_VALID_ID);
		}

		//generate binary code with explicit size
		byte[][] outputBlock = new byte[2][16];
		
		//convert the IDs now
		byte contentID 		= 10;
				
		short fahrzeugID 	= theDriver.getTeam().getCarNr();
		short userID 		= theDriver.getUser_id();
		short teamID 		= theDriver.getTeam_id();
		short eventID 		= 1; //TODO: EventID ueber Einstellungen festlegbar ?

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
			prename	= theDriver.getFirst_name(); 
				if(prename.length()>15) prename = prename.substring(0, 16);
			lastname = theDriver.getLast_name().substring(0, 1)+".";
		} catch (Exception  e) {
			throw new FsgException( e, "NfcData", FsgException.GENERIC_EXCEPTION);
		}
		
		String fullname = " "+prename+" "+lastname;	// whitespace at first position is essential; space for the contentID	
			//System.out.println("NfcData#Name: "+fullname);
		
		/* >= 1Byte pro Buchstabe bei UTF-8
		 * >= 2Byte pro Buchstabe bei UTF-16 (UTF-16LE)
		 * 	Umlaute immer 2Byte
		 */						
		outputBlock[1] = fullname.getBytes("UTF-8");
		outputBlock[1][0] = contentID; //write contentID AFTER text, to overwrite whitespace
		
		return outputBlock;
	}
	
	public static byte[][] generateCheckIN(short briefingID){
		byte[][] outputBlock = new byte[1][16];
		byte contentID = 20;
		int timestamp = makeBetterTimestampNOW();
		
		outputBlock[0][0] = contentID;
		outputBlock[0][1] = (byte)(briefingID & 0xff);
		outputBlock[0][2] = (byte)((briefingID >> 8) & 0xff);
		outputBlock[0][3] = (byte)(timestamp & 0xff);
		outputBlock[0][4] = (byte)((timestamp >> 8) & 0xff);
		outputBlock[0][5] = (byte)((timestamp >> 16) & 0xff);
		outputBlock[0][6] = (byte)((timestamp >> 32) & 0xff);		
		
		return outputBlock;
	}
	
	public static byte[][] generateCheckOUT(short briefingID){
		byte[][] outputBlock = new byte[1][16];
		byte contentID = 21;
		int timestamp = makeBetterTimestampNOW();
		
		outputBlock[0][0] = contentID;
		outputBlock[0][1] = (byte)(briefingID & 0xff);
		outputBlock[0][2] = (byte)((briefingID >> 8) & 0xff);
		outputBlock[0][3] = (byte)(timestamp & 0xff);
		outputBlock[0][4] = (byte)((timestamp >> 8) & 0xff);
		outputBlock[0][5] = (byte)((timestamp >> 16) & 0xff);
		outputBlock[0][6] = (byte)((timestamp >> 32) & 0xff);	
		
		return outputBlock;
	}
	
	private static int makeBetterTimestampNOW(){
		//get the Time	| converted output: 2010-03-08 14:59:30.252
		java.util.Date date = new java.util.Date();		
			//System.out.println("TimestampIN: "+date.getTime());
			//System.out.println("TimestampINc:"+(int)((date.getTime()/1000)-tstampConstant));
			System.out.println("TimeIN: "+date);
		//to convert to UnixTimestamp use: / 1000L
		return (int)((date.getTime()/1000)-tstampConstant);
	}
	
	public static byte[] toBytes(short s) {
        return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
    }
	
}	
