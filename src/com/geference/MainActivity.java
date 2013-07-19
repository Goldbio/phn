package com.geference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String fileContent;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		// Get content 
		GetNews news = new GetNews();
		news.execute();
		while(true){
			if( news.checkDone() == true){
				fileContent = news.getContent();
				
				break;
			}
			try{
				Thread.sleep(200);
			}catch(Exception e){}
			
		}
	
		// Display
		
		displayNews(fileContent);
		
		
	}

	protected void displayNews(String json){
		try {
			JSONArray newsJson = new JSONArray(json);
			for(int i=0;i<newsJson.length(); i++){
				JSONObject eachNews = newsJson.getJSONObject(i);
				String publisher =eachNews.getString("publisher") ;
				Toast.makeText(this, publisher,0).show();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

