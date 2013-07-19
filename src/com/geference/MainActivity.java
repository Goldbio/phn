package com.geference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
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
		news.execute("http://api.geference.com/phn/breast_cancer");
		while(true){
			if( news.checkDone() == true){
				fileContent = news.getContent();

				// Display 
				displayNews(fileContent);
				
				break;
			}
			try{
				Thread.sleep(20);
			}catch(Exception e){}
		}
			
	}

	protected void displayNews(String json)  {
		LinearLayout item_wrapper = (LinearLayout)findViewById(R.id.item_wrapper);
			
		try{
			JSONArray newsJson = new JSONArray(json);
			
			
			for(int i=0; i< newsJson.length() ; i++){
					
				JSONObject eachNews = newsJson.getJSONObject(i);
								
				String title =eachNews.getString("title") ;
				String date =eachNews.getString("date") ;
				String img_url_str = eachNews.getString("image_url");
				
				View view = (View)getLayoutInflater().inflate(  R.layout.news_item, null );
				
				TextView title_view = (TextView)view.findViewById(R.id.title);
				title_view.setText( title);
				
				TextView date_view = (TextView)view.findViewById(R.id.date);
				date_view.setText(date);
				
				if( !img_url_str.equals("NA") ){
					// Download picture 
					// class GetImage 
					GetImage img = new GetImage();
					img.execute(img_url_str);
					
					while(true){
						
						Thread.sleep(10);
						if( img.isDone() ){
							//img_view.setImageBitmap( img.getImgBitmap() ); 
							
							
							
							ImageView img_view = (ImageView)view.findViewById(R.id.img);
							img_view.setImageBitmap( img.getImgBitmap() );
							
							item_wrapper.addView(view);
							break;
							
						}
					}
					
				}
				else{
					//item_wrapper.addView(view);
					
				}
			
			}
		
		}
		catch( JSONException e){;} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}

