package de.tubs.cs.ibr.fsg.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.os.Environment;
import android.util.Log;
import de.tubs.cs.ibr.fsg.exceptions.FsgException;

public class FileHelper {

	private static final String TAG = "FileHelper";
	
	public static final int GENERIC_FSG_DIR = 1;
	public static final int DRIVER_PICS_DIR = 2;
	public static final int TEMP_FSG_DIR    = 3;
	
	
	
	/**
	 * Mit Hilfe dieser Methode kann eine Zip-Datei entpackt werden.
	 * 
	 * @param zipFile Komprimierte Datei, die entpackt werden soll.
	 * @param destDir Zielverzeichnis, wo die entpackten Daten gespeichert werden
	 * @throws FsgException Theoretisch k�nnten zwei Arten von Exceptions die Ursache f�r Probleme sein: eine ZipException oder eine IOException.
	 */
	public static void extractZipFile(File zipFile, File destDir) throws FsgException{
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		ZipFile javaZipFile;
		try {
			javaZipFile = new ZipFile(zipFile);
			Enumeration mEntries = javaZipFile.entries();
			byte[] bytesBuffer = new byte[8192];
			int length;
			
			while (mEntries.hasMoreElements()) {
				ZipEntry mEntry = (ZipEntry) mEntries.nextElement();
				String entryFileName = mEntry.getName(); //TODO Muss an dieser Stelle �berpr�fen, was passiert, wenn Unterverzeichnisse im zipfile angegeben sind!

				if (!mEntry.isDirectory()) {
					BufferedOutputStream bOutputStream = new BufferedOutputStream(
							new FileOutputStream(new File(destDir, entryFileName)));
					BufferedInputStream bInputStream = new BufferedInputStream( javaZipFile.getInputStream(mEntry) );

					while ((length = bInputStream.read(bytesBuffer)) > 0) {
						bOutputStream.write(bytesBuffer, 0, length);
					}

					bOutputStream.flush();
					bOutputStream.close();
					bInputStream.close();
				}
			}
			javaZipFile.close();
			
		} catch (ZipException e) {
			throw new FsgException( e, new FileHelper().getClass().toString(), FsgException.ZIP_FILE_FAIL );
		} catch (IOException e) {
			throw new FsgException( e, new FileHelper().getClass().toString(), FsgException.ZIP_FILE_FAIL );
		}catch (Exception e) {
			throw new FsgException( e, new FileHelper().getClass().toString(), FsgException.ZIP_FILE_FAIL );
		}

	}

	
	
	/**
	 * @param mFsgBundle
	 * @throws FsgException
	 */
	public static void saveDriverPics(FsgPackage mFsgBundle) throws FsgException {
		
		File destDir = getStoragePath(DRIVER_PICS_DIR);
		FileHelper.deleteDirectory(destDir);
		
		File tempZipFileDir   = getStoragePath(TEMP_FSG_DIR);
		String outputFileName = System.currentTimeMillis() + ".zip";
		File tempZipFile    = new File(tempZipFileDir, outputFileName);

		try {
			FileOutputStream fos = new FileOutputStream(tempZipFile);
			fos.write( mFsgBundle.getPayload() );
			fos.close();
			FileHelper.extractZipFile(tempZipFile, destDir);
			tempZipFile.delete();
			tempZipFile=null;
			
		} catch (FsgException e) {
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new FsgException( e, new FileHelper().getClass().toString(), FsgException.DRIVER_PICS_FAIL );
		}
	}
	
	
	
	/**
	 * Mit dieser Methode k�nnen wir ein ganzes Verzeichnis l�schen (rekursiv, alle
	 * Unterverzeichnisse werden ebenfalls gel�scht).
	 * 
	 * @param directory Verzeichnis, das gel�scht werden soll
	 * @return true, wenn das Verzeichnis erfolgreich gel�scht werden konnte. Sonst false.
	 */
	private static boolean deleteDirectory(File directory) {
		File[] fileArray = directory.listFiles();
		if (fileArray != null) {
			for (int i = 0; i < fileArray.length; i++) {
				if (fileArray[i].isDirectory()) {
					deleteDirectory(fileArray[i]); // Den Ordner leeren und anschlie�end loeschen 
				} else {
					fileArray[i].delete(); // Datei loeschen
				}
			}
			return directory.delete(); // Ordner loeschen
		}
		return false;
	}
	
	
	
	/**
	 * Mit Hilfe dieser Methode finden wir die Verzeichnisse heraus, die f�r die Applikation
	 * von Bedeutung sind (z.B. wo die Fahrerbilder gespeichert werden).
	 * 
	 * @return Gibt den Pfad des Vereichnisses zur�ck, das wir suchen (z.B. f�r tempor�re Dateien, Fahrerbilder, usw.).
	 */
	public static File getStoragePath(int typeOfDir) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File externalStorage = Environment.getExternalStorageDirectory();
			// Wenn es noch nicht vorhanden ist, wird an dieser Stelle ein Arbeitsverzeichnis erstellt.
			File sharefolder = null;
			if(typeOfDir == GENERIC_FSG_DIR){
				sharefolder = new File(externalStorage.getPath() + File.separatorChar + "fsg");
				
			}else if(typeOfDir == TEMP_FSG_DIR){
				sharefolder = new File(externalStorage.getPath() + File.separatorChar + "fsg"+ File.separatorChar + "temp");
				
			}else{
				//DRIVER_PICS_DIR
				sharefolder = new File(externalStorage.getPath() + File.separatorChar + "fsg" + File.separatorChar + "driver_pics");
			}

			if (!sharefolder.exists()) {
				sharefolder.mkdir();
			}
			return sharefolder;
		}
		return null;
	}

	
	
	/**
	 * Diese Methode wird nur zum Testen gebraucht, bis die interne Datenbank benutzt werden kann.
	 * @param tempFile Tempor�re Datei, die dauerhaft gespeichert werden soll.
	 */
	public static void storeTempFile(File tempFile, int typeOfDir) {
		File root = FileHelper.getStoragePath(typeOfDir);
		String outputFileName = System.currentTimeMillis() + ".txt";
	    File outputFile = new File(root, outputFileName);

	    FileReader in  = null;
	    FileWriter out = null;
		try {
			in = new FileReader(tempFile);
		    out = new FileWriter(outputFile);
		    
		    int c;
		    while ((c = in.read()) != -1){
			      out.write(c);
		    }
		    in.close();
		    out.close();
		    
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Can not create a file.", e );
		} catch (IOException e) {
			Log.e(TAG, "Can not create a file.", e );
		}
	}
	
	
	
	/**
	 * Diese Methode wird nur zum Testen gebraucht, bis die interne Datenbank benutzt werden kann.
	 * @param stringToSave Text, der in einer Datei gespeichert wird.
	 * @param fileName Name der Datei, wo der Text gespeichert werden soll.
	 */
	public static void storeStringToFile(String stringToSave, String fileName, int typeOfDir) {
		try {
			File root = FileHelper.getStoragePath(typeOfDir);
			if (root.canWrite()) {
				File gpxfile = new File(root, fileName);
				FileWriter gpxwriter = new FileWriter(gpxfile);
				BufferedWriter out = new BufferedWriter(gpxwriter);
				out.write(stringToSave);
				out.close();
			}
		} catch (IOException e) {
			Log.e(TAG, "Could not write file ", e );
		}
	}


}
