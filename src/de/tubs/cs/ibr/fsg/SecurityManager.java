/**
 * 
 */
package de.tubs.cs.ibr.fsg;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import de.tubs.cs.ibr.fsg.exceptions.FsgException;

/**
 * Class to handle encoding and decoding before reading/writing on the Mifare card
 * @author Vinh Tran
 * @version 0.2
 */
public class SecurityManager {

	//password for encoding and decoding
	private String password = null;
	
	//encryption & decryption type
	private static final String ENCRYPTION_DECRYPTION_TYPE = "AES";
	private static final String ENCRYPTION_ALGORITHM = "SHA1PRNG";

	//only 16 Byte
	private final static int BYTEARRAYSIZE = 16;
	
	//accepted characters (Hex)
	private final String HEX = "0123456789ABCDEF";
	
	//Android Version 4.2
	private static final int JELLY_BEAN_4_2 = 17;
	
	private final byte[] key = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	
	
	/**
	 * 
	 * Constructor for initiating the key
	 * 
	 * @param password is the key for encoding and decoding
	 */
	public SecurityManager(final String password) {
		this.password = password;
	}
	
	/** Method to encrypt String using given password
	 * 
	 * @param cleartext is the raw input string
	 * @return the encrypted Base64 String
	 * @throws Exception throws an exception
	 */
	public String encrypt(String cleartext) throws FsgException {
	    byte[] rawKey = this.getRawKey(this.password.getBytes());
	    byte[] result = this.encrypt(rawKey, cleartext.getBytes());
	    String fromHex = this.toHex(result);
	    String base64 = new String(Base64.encodeToString(fromHex.getBytes(), 0));
	    return base64;
	}

	/** Method to decrypt an encrypted string
	 * 
	 * @param encrypted encrypted string 
	 * @return decrypted string
	 * @throws FsgException an exception  if something went wrong during decryption
	 */
	public String decrypt(String encrypted) throws FsgException {
	    byte[] seedByte = this.password.getBytes();
	    System.arraycopy(seedByte, 0, key, 0, ((seedByte.length < BYTEARRAYSIZE) ? seedByte.length : BYTEARRAYSIZE));
	    String base64 = new String(Base64.decode(encrypted, 0));
	    byte[] rawKey = getRawKey(seedByte);
	    byte[] enc = this.toByte(base64);
	    byte[] result = this.decrypt(rawKey, enc);
	    return new String(result);
	}

	/** Method to encrypt an byte array
	 * 
	 * @param cleartext
	 * @return encrypted byte array
	 * @throws FsgException an exception if something went wrong during encryption
	 */
	public byte[] encryptBytes(byte[] cleartext) throws FsgException {
	    byte[] rawKey = this.getRawKey(this.password.getBytes());
	    byte[] result = this.encrypt(rawKey, cleartext);
	    return result;
	}


	/** Method to decrypt an byte array
	 * 
	 * @param encrypted is the encrypted input as byte array
	 * @return decrypted byte array
	 * @throws FsgException an exception  if something went wrong during decryption
	 */
	public byte[] decryptBytes(byte[] encrypted) throws FsgException {
	    byte[] rawKey = this.getRawKey(this.password.getBytes());
	    byte[] result = this.decrypt(rawKey, encrypted);
	    return result;
	}

	public byte[][] encryptString(byte[][] raw) throws FsgException{

		//abort if the key is null
		if(this.key==null){
			throw new FsgException( new Exception("SecurityManager#No Encryption Key available"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		

		//raw input is invalid
		if(raw.length==0){
			throw new FsgException( new Exception("SecurityManager#Input for Encryption is too short"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
		
		byte [] singleByteArray = this.convertToOneByteArray(raw);
		
		byte [] encryptedSingleByteArray = this.encryptBytes(singleByteArray);
		
		
		
		return this.convertToTwoByteArray(encryptedSingleByteArray);
	}
	
	
	/**
	 * Method to decrypt 2 dimensional array
	 * @param encryptedInput is the encrypted string input
	 * @return return the decrypted byte array for reading
	 * @throws FsgException if there are no password set or invalid password is used then it will throw this exception
	 */
	public byte[][] decryptString(byte[][] encryptedInput) throws FsgException{
		//abort if the key is null
		if(this.key==null){
			throw new FsgException( new Exception("SecurityManager#No Decryption Key available"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
		//encryption input is invalid
		if(encryptedInput.length==0){
			throw new FsgException( new Exception("SecurityManager#Input for decrypting is too short"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
		byte [] [] decryptedString= new byte [encryptedInput.length] [];
		
		byte [] singleByteArray = this.convertToOneByteArray(encryptedInput);
		
		byte [] encryptedSingleByteArray = this.decryptBytes(singleByteArray);
		
		
		
		return this.convertToTwoByteArray(encryptedSingleByteArray);
	}
	/**
	 * Method to get the key for encryption
	 * @param seed is the password
	 * @return encoded key for encoding
	 * @throws FsgException error handling for parent methods
	 */
	private byte[] getRawKey(byte[] seed)throws FsgException {
		
		//key generator
	    KeyGenerator kgen;
		
	    try {
			kgen = KeyGenerator.getInstance(ENCRYPTION_DECRYPTION_TYPE);
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			//developer used an unknown encryption type
			throw new FsgException( new Exception("SecurityManager#getRawKey(byte[]):Encryptiontype unknown"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
	    SecureRandom sr = null;
	    
	    try{

		    if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_4_2) {
		    	//algorithm|provider
		        sr = SecureRandom.getInstance(ENCRYPTION_ALGORITHM, "Crypto");
		    } else {
		    	//algorithm
		        sr = SecureRandom.getInstance(ENCRYPTION_ALGORITHM);
		    }
	    }catch(NoSuchAlgorithmException nae){
	    	throw new FsgException( new Exception("SecurityManager#getRawKey(byte[]):Encryption algorithm not available"), 
	    			this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
	    }catch(NoSuchProviderException npe){
	    	throw new FsgException( new Exception("SecurityManager#getRawKey(byte[]):Encryption provider not available"), 
	    			this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
	    }catch(Exception e){
	    	throw new FsgException( new Exception("SecurityManager#getRawKey(byte[]):Encryption input is invalid"),
	    			this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
	    }
	    
	    sr.setSeed(seed);
	    
	    //try to use the max available encryption type
	    try {
	        kgen.init(256, sr);
	        // kgen.init(128, sr);
	    } catch (Exception e) {
	        // Log.w(LOG, "This device doesn't suppor 256bits, trying 192bits.");
	        try {
	            kgen.init(192, sr);
	        } catch (Exception e1) {
	            // Log.w(LOG, "This device doesn't suppor 192bits, trying 128bits.");
	            try{
	            	kgen.init(128, sr);
	            }catch(Exception e2){
	            	//no encryption works
	            	throw new FsgException( new Exception("SecurityManager#getRawKey(byte[]):Encryption failed. Device doesnt support encryption."), 
	            			this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
	            }
	        }
	    }
	    
	    //generate secret key for encryption
	    SecretKey skey = kgen.generateKey();
	    
	    byte[] raw = skey.getEncoded();
	    return raw;
	}


	private byte[] encrypt(byte[] raw, byte[] clear) throws FsgException {
	    SecretKeySpec skeySpec = new SecretKeySpec(raw, ENCRYPTION_DECRYPTION_TYPE);
	    Cipher cipher;
		try {
			cipher = Cipher.getInstance(ENCRYPTION_DECRYPTION_TYPE);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#encrypt(byte[],byte[]):Encryption provider cannot provide the transformation or it is null/invalid format"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#encrypt(byte[],byte[]):Requested padding scheme not available"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		} catch(Exception e){
			throw new FsgException( new Exception("SecurityManager#encrypt(byte[],byte[]):Input is invalid or incomplete"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	    try {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#encrypt(byte[],byte[]):Key 'skeySpec'cannot be used"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	    byte[] encrypted;
		try {
			encrypted = cipher.doFinal(clear);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#encrypt(byte[],byte[]):Resulting bytes is not a multiple of the cipher block size."), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
			
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#encrypt(byte[],byte[]):Padding data does not match the padding scheme."), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	    return encrypted;
	}


	private byte[] decrypt(byte[] raw, byte[] encrypted) throws FsgException {
	    SecretKeySpec skeySpec = new SecretKeySpec(raw, ENCRYPTION_DECRYPTION_TYPE);
	    Cipher cipher;
		
		try {
			cipher = Cipher.getInstance(ENCRYPTION_DECRYPTION_TYPE);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#decrypt(byte[],byte[]):Decryption provider cannot provide the transformation or it is null/invalid format"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#decrypt(byte[],byte[]):Requested padding scheme not available"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		} catch(Exception e){
			throw new FsgException( new Exception("SecurityManager#decrypt(byte[],byte[]):Input is invalid or incomplete"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
		
		
	    try {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#decrypt(byte[],byte[]):Key 'skeySpec'cannot be used"), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	    byte[] decrypted;
		try {
			decrypted = cipher.doFinal(encrypted);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#decrypt(byte[],byte[]):Resulting bytes is not a multiple of the cipher block size."), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
			
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FsgException( new Exception("SecurityManager#decrypt(byte[],byte[]):Padding data does not match the padding scheme."), 
					this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	    return decrypted;
	}


	public String toHex(String txt) {
	    return toHex(txt.getBytes());
	}

	/** Method to convert hex to string (showing the bytes)
	 * 
	 * @param hex hex string
	 * @return bytes of the array
	 */
	public String fromHex(String hex) {
	    return new String(toByte(hex));
	}

	/** Method to convert hexstring into bytes
	 * 
	 * @param hexString is the hexstring
	 * @return converted byte array using hexstring as input
	 */
	public byte[] toByte(String hexString) {
	    int len = hexString.length() / 2;
	    byte[] result = new byte[len];
	    for (int i = 0; i < len; i++)
	        result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), BYTEARRAYSIZE).byteValue();
	    return result;
	}

	/** Method to convert byte array into hex string
	 * 
	 * @param buf input byte array
	 * @return return the converted hex string
	 */
	public String toHex(byte[] buf) {
	    if (buf == null) return "";
	    StringBuffer result = new StringBuffer(2 * buf.length);
	    for (int i = 0; i < buf.length; i++) {
	        appendHex(result, buf[i]);
	    }
	    return result.toString();
	}


	/** Method to append bytes into hex
	 * 
	 * @param sb
	 * @param b
	 */
	private void appendHex(StringBuffer sb, byte b) {
	    sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
    

	/**
	 * Key setter method to update the key value
	 * @param key
	 */
    public void setKey(String key){
    	this.password = key;
    }
    
    private byte[] convertToOneByteArray(byte[][] input){
    	
    	int numberOfItems = input.length * BYTEARRAYSIZE;    	
    	
    	//init the return array
    	byte[]singleByteArray = new byte[numberOfItems];
    	
    	for(int i=0,count = 0; i<input.length;i++){
    		for(int j=0;j<input[i].length;j++,count++){
    			singleByteArray[count] = input[i][j];
    		}
    	}
    	
    	return singleByteArray;
    }
    
    private byte[][] convertToTwoByteArray(byte[]input){
    	
    	int numberOfSections = input.length / BYTEARRAYSIZE;
    	
    	byte[][]twoByteArray = new byte[numberOfSections][BYTEARRAYSIZE];
    	
    	for(int i=0;i<input.length;i++){
    		twoByteArray[i/BYTEARRAYSIZE][i%BYTEARRAYSIZE] = input[i];
    	}
    	
    	return twoByteArray;
    	
    }
    
}
