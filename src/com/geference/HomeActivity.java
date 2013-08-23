package com.geference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends Activity {
	private String fileContent;
	private ListView lv;
	private DrawerLayout dl;
	ActionBarDrawerToggle drawToggle;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		
		// Navigation drawer 
		 ArrayList<String> al = new ArrayList<String>();
	        al.add("All diseases");
	        al.add("Risky diseases");
	        al.add("Most viewed news");
	        
	     ArrayAdapter<String> Adapter;
	     Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
	      
	     lv= (ListView)findViewById(R.id.left_drawer);
	     lv.setAdapter(Adapter);
	     lv.setOnItemClickListener(onClickItem); 
	       
	     dl = (DrawerLayout)findViewById(R.id.drawer_layout);
	     
	     drawToggle = new ActionBarDrawerToggle( this , dl, R.drawable.ic_drawer ,  1 , 0) {
        	/** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                if( getActionBar().getTitle() == "Menu"){
                	getActionBar().setTitle("");
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        	
        };
        
        dl.setDrawerListener( drawToggle);
	
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        
    	
    	
		
		// Get content 
		GetNews news = new GetNews();
		news.execute("http://api.geference.com/phn");
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
	
	
	AdapterView.OnItemClickListener onClickItem = new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			
			lv.setItemChecked(pos, true);
			getActionBar().setTitle(""+ lv.getItemAtPosition(pos) );
			dl.closeDrawer(lv);
			
			
			Fragment fragment = new ExamFragment();
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
			
		}
		
		
	};
	
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawToggle.syncState();
    }
    
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawToggle.onConfigurationChanged(newConfig);
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
	
	
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	*/
	
	
	
	
	

	


	protected View setNewsView( String title, String date, String img_url, String content, String disease_type ) throws InterruptedException{
		View view = (View)getLayoutInflater().inflate(  R.layout.news_item, null );
		
		TextView disease_type_view = (TextView)view.findViewById(R.id.disease);
		TextView title_view = (TextView)view.findViewById(R.id.title);
		TextView date_view = (TextView)view.findViewById(R.id.date);
		ImageView img_view = (ImageView)view.findViewById(R.id.img);
		TextView content_view = (TextView)view.findViewById(R.id.content);
		title_view.setText( title);
		date_view.setText(date);
		content_view.setText(content);
		disease_type_view.setText(disease_type);
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
				String disease_type = json.getString("disease_type");
			//	String content = json.getString("article");
				
						
				View newsView=setNewsView( title, date, img_url_str, json.toString(), disease_type );
				
				newsView.setOnClickListener(new View.OnClickListener(){
					

					public void onClick(View v) {
					
						
							
						//Toast.makeText(MainActivity.this,"touch "+v.getId() ,0).show();
						
						TextView tv= (TextView)v.findViewById(R.id.content);
						
						// Start News display Activity with clicked view ID for display 
						Intent intent = new Intent(HomeActivity.this, NewsViewActivity.class);
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

