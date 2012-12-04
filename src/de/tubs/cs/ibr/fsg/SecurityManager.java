/**
 * 
 */
package de.tubs.cs.ibr.fsg;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.tubs.cs.ibr.fsg.exceptions.FsgException;

/**
 * Class to handle encoding and decoding before reading/writing on the Mifare card
 * @author Vinh Tran
 * @version 0.2
 */
public class SecurityManager {

	private String key = null;
	
	//encryption & decryption type
	private final String ENCRYPTION_DECYPTION_TYPE = "AES";
	
	// 192 and 256 bits may not be available
	private final int bits = 128; 
	
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
			kgen.init(bits, sr);
			SecretKey skey = kgen.generateKey();
			byte[] key = skey.getEncoded();
			
			return this.encrypt(key,rawInput);
		}catch(FsgException fsge){
			//forward the exception
			throw fsge;
		}catch(NoSuchAlgorithmException nsae){
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
			kgen.init(bits, sr);
			SecretKey skey = kgen.generateKey();
			byte[] key = skey.getEncoded();
			
			return this.decrypt(key,encryptedInput);
		}catch(FsgException fsge){
			//forward the exception
			throw fsge;
		}catch(Exception e){
			throw new FsgException( new Exception("Security Exception in SecurityManager"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
	}

	/** 
	 * Method to encrypt the data using AES-methods
	 * @param raw 
	 * @param clear 
	 * @return the encrypted data
	 * @throws FsgException forward the error message to the parent method
	 */
	private byte[] encrypt(byte[] raw, byte[] clear)throws FsgException {
    	
		try{
	        SecretKeySpec skeySpec = new SecretKeySpec(raw, this.ENCRYPTION_DECYPTION_TYPE);
	        Cipher cipher = Cipher.getInstance(this.ENCRYPTION_DECYPTION_TYPE);
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	        byte[] encrypted = cipher.doFinal(clear);
	        return encrypted;
		}catch(InvalidKeyException ike){
			//wrong key
			throw new FsgException( new Exception("SecurityManager# Invalid Key"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION ); 
		}catch(NoSuchPaddingException bspe){
			throw new FsgException( new Exception("SecurityManager# NoSuchPaddingException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION ); 
		}catch(NoSuchAlgorithmException nae){
			throw new FsgException( new Exception("SecurityManager# NoSuchAlgorithmException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION ); 
		}catch(BadPaddingException bpe){
			throw new FsgException( new Exception("SecurityManager# BadPaddingException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}catch(IllegalBlockSizeException ibse){
			throw new FsgException( new Exception("SecurityManager# IllegalBlockSizeException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
    }

	/** Method to decrypt the data using AES-methods
	 * 
	 * @param raw
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
    private byte[] decrypt(byte[] raw, byte[] encrypted) throws FsgException {

		try{
	        SecretKeySpec skeySpec = new SecretKeySpec(raw, this.ENCRYPTION_DECYPTION_TYPE);
	        Cipher cipher = Cipher.getInstance(this.ENCRYPTION_DECYPTION_TYPE);
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	        byte[] decrypted = cipher.doFinal(encrypted);
	        return decrypted;
		}catch(InvalidKeyException ike){
			//wrong key
			throw new FsgException( new Exception("SecurityManager# Invalid Key"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION ); 
		}catch(NoSuchPaddingException bspe){
			throw new FsgException( new Exception("SecurityManager# NoSuchPaddingException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION ); 
		}catch(NoSuchAlgorithmException nae){
			throw new FsgException( new Exception("SecurityManager# NoSuchAlgorithmException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION ); 
		}catch(BadPaddingException bpe){
			throw new FsgException( new Exception("SecurityManager# BadPaddingException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}catch(IllegalBlockSizeException ibse){
			throw new FsgException( new Exception("SecurityManager# IllegalBlockSizeException"), this.getClass().toString(), FsgException.GENERIC_EXCEPTION );
		}
		
    }
	
	/**
	 * Key setter method to update the key value
	 * @param key
	 */
    public void setKey(String key){
    	this.key = key;
    }
}
