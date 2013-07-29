package com.geference;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsViewActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_main);
	
		
		JSONObject json;
		try {
			json = new JSONObject(getIntent().getStringExtra("data"));
			String news_content = json.getString("article");
			String date = json.getString("date");
			String title = json.getString("title");
			String source = json.getString("publisher");
			
			TextView source_view = (TextView)findViewById(R.id.source);
			source_view.setText(source );
			
			TextView title_view = (TextView)findViewById(R.id.title);
			title_view.setText(title );
			
			TextView date_view = (TextView)findViewById(R.id.date);
			date_view.setText( date );
			
			WebView news_view = (WebView)findViewById(R.id.content);
			//news_view.setText( Html.fromHtml( news_content ) );
			String css="<style>"+
					  "p {text-align:justify; padding : 0 13px ; line-height:1.4em }"
					 + "p:first-letter { margin-left:10px}"
					 +"</style>";
			news_view.loadData( css+news_content, "text/html", "utf-8");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.newsbar_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.social_menu:
	            Toast.makeText(this, "Clicked",0).show();
	            return true;
	       
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	

}
