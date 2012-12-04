/**
 * 
 */
package de.tubs.cs.ibr.fsg;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.tubs.cs.ibr.fsg.exceptions.FsgException;

/**
 * Class to handle encoding and decoding before reading/writing on the Mifare card
 * @author Vinh Tran
 *
 */
public class SecurityManager {

	private String key = null;
	
	//encryption & decryption type
	private static final String ENCRYPTION_DECYPTION_TYPE = "AES";
	
	private static final int bits = 128; 
	
	/**
	 * 
	 * Constructor for initiating the key
	 * 
	 * @param newKey
	 */
	public SecurityManager(final String newKey) {
		this.key = newKey;
	}
	
	/**
	 *  Method to encrypt string and return the encrypted string
	 * @param rawInput is the unencrypted byte array input
	 * @return the encrypted string
	 * @throws FsgException if there is something wrong with encoding
	 */
	public byte[] encryptString(byte[] rawInput)throws FsgException{
		
		//abort if the key is null
		if(this.key==null){
			throw new FsgException( new Exception("SecurityManager#No Encryption Key available"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
		try{
			//add some helper var
			byte[] keyStart = this.key.getBytes();
			
			KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPTION_DECYPTION_TYPE);
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(keyStart);
			kgen.init(bits, sr); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();
			byte[] key = skey.getEncoded();
			
			return encrypt(key,rawInput);
		}catch(Exception e){
			//this part is never called
			throw new FsgException( new Exception("Security Exception in SecurityManager"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	}
	
	public byte[] decryptString(byte[] encryptedInput) throws FsgException{
		
		//abort if the key is null
		if(this.key==null){
			throw new FsgException( new Exception("SecurityManager#No Decryption Key available"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
		try{
			//add some helper var
			byte[] keyStart = this.key.getBytes();
			
			KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPTION_DECYPTION_TYPE);
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(keyStart);
			kgen.init(bits, sr); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();
			byte[] key = skey.getEncoded();
			
			return decrypt(key,encryptedInput);
		}catch(Exception e){
			throw new FsgException( new Exception("Security Exception in SecurityManager"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	}

	/** 
	 * Method to encrypt the data using AES-methods
	 * @param raw 
	 * @param clear 
	 * @return the encrypted data
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    	
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ENCRYPTION_DECYPTION_TYPE);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_DECYPTION_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

	/** Method to decrypt the data using AES-methods
	 * 
	 * @param raw
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    	
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ENCRYPTION_DECYPTION_TYPE);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_DECYPTION_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
	
	/**
	 * Key setter method to update the key value
	 * @param key
	 */
    public void setKey(String key){
    	this.key = key;
    }
}
