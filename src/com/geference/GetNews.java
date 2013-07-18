package com.geference;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class GetNews extends AsyncTask{
	
	private boolean doneGet=false;
	private String fileContent;
	
	@Override
	protected String doInBackground(Object... params) {
		// Get file 
		StringBuilder file = new StringBuilder();
		try{
			URL url=new URL("http://api.geference.com/phn/breast_cancer");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if( conn != null){
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if( conn.getResponseCode() == HttpURLConnection.HTTP_OK){
					BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream()));
					
					for(;;){
						String line = br.readLine();
						if(line==null)break;
						file.append( line+"\n");
					}
					br.close();
				}
				conn.disconnect();
				fileContent=file.toString();
				doneGet=true;
			}
			
			
		}catch(Exception e){}
		
		return file.toString();
	}
	
	protected void onPostExecute(String file){
		//fileContent=file;
		
	}
	protected boolean checkDone(){
		return doneGet;
		
	}
	protected String getContent(){
		return fileContent;
		
	}

}
