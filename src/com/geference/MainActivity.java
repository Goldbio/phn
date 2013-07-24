package com.geference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Set;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
	private String fileContent;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get content 
		GetNews news = new GetNews();
		news.execute("http://api.geference.com/phn/prostate_cancer");
		while(true){
			if( news.checkDone() == true){
				fileContent = news.getContent();

				displayNews(fileContent);
					
				
				break;
			}
			try{
				Thread.sleep(20);
			}catch(Exception e){}
		}
		
		
		
			
	}
	


	protected View setNewsView( String title, String date, String img_url, String content ) throws InterruptedException{
		View view = (View)getLayoutInflater().inflate(  R.layout.news_item, null );
		
		TextView title_view = (TextView)view.findViewById(R.id.title);
		TextView date_view = (TextView)view.findViewById(R.id.date);
		ImageView img_view = (ImageView)view.findViewById(R.id.img);
		TextView content_view = (TextView)view.findViewById(R.id.content);
		title_view.setText( title);
		date_view.setText(date);
		content_view.setText(content);
		new GetImage( img_view).execute( img_url );
		
		
		return view;
	}
	protected void displayNews(String fileContent)   {
		LinearLayout item_wrapper = (LinearLayout)findViewById(R.id.item_wrapper);			
		
		
		try{
			JSONArray newsJson = new JSONArray(fileContent);
			
			for(int i=0; i< newsJson.length() ; i++){
				JSONObject json = newsJson.getJSONObject(i); 
			
				String title =json.getString("title") ;
				String date =json.getString("date") ;
				String img_url_str = json.getString("image_url");
			//	String content = json.getString("article");
				
						
				View newsView=setNewsView( title, date, img_url_str, json.toString() );
				
				newsView.setOnClickListener(new View.OnClickListener(){
					

					public void onClick(View v) {
					
						
							
						//Toast.makeText(MainActivity.this,"touch "+v.getId() ,0).show();
						
						TextView tv= (TextView)v.findViewById(R.id.content);
						
						// Start News display Activity with clicked view ID for display 
						Intent intent = new Intent(MainActivity.this, NewsViewActivity.class);
						intent.putExtra( "data", tv.getText() );
						startActivity(intent);
							
						
					}
					
					
				});
				
				item_wrapper.addView(newsView);
				
			
				// Download picture 
				// class GetImage 
				
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
					
	}	
	
}

