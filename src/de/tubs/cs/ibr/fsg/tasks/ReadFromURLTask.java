package de.tubs.cs.ibr.fsg.tasks;

import java.io.*;
import java.net.*;

import android.os.AsyncTask;

public class ReadFromURLTask extends AsyncTask<String, Integer, String>{

	protected String doInBackground(String... ulrs) {
		
		StringBuffer content = new StringBuffer();
		
		try {
			for(String url : ulrs) {
				URL remoteFile = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) remoteFile.openConnection();
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				
				String line = "";
				
				while( (line = br.readLine() ) != null) {
					content.append(line);
				}
			}
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return content.toString();
	}

}
