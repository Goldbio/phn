package com.geference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Set;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.GestureDetector;
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
	ArrayList<String> al ;
	GetNews[] raw_news_array= new GetNews[3];
	
	private GestureDetectorCompat gd; 
	
	
	private final class SimpleGestureListener 
	  extends GestureDetector.SimpleOnGestureListener {
		
		// Detect swipe up at the bottom of the page, and add 10 more news 
		
		public boolean onDown(MotionEvent e){
			Toast.makeText(HomeActivity.this,"down",0).show();
			return false;
			
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	      return gd.onTouchEvent(event);
	}
	 
	public boolean dispatchTouchEvent(MotionEvent ev){
		    super.dispatchTouchEvent(ev);    
		    return gd.onTouchEvent(ev); 
	}
		
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		
		
		// Navigation drawer menu 
	 	al = new ArrayList<String>();
        al.add("My diseases");
        al.add("All diseases");
       
        
	     ArrayAdapter<String> Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
	      
	     lv= (ListView)findViewById(R.id.left_drawer);
	     lv.setAdapter(Adapter);
	     lv.setOnItemClickListener(onClickItem); 
	       

		 gd=new GestureDetectorCompat(this, new SimpleGestureListener() );

		 ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
		 //scroll.setOnScrollChanged();
		 
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
        
        
    	
    	
		
		//////// Get content  according to user_disease in SQLdb 
      
        // Read user_disease in sqlDB 
		
 		SQLdb sqlDB= new SQLdb(this);
 		SQLiteDatabase db = sqlDB.getReadableDatabase();
 		Cursor cursor;
     		

		// Check if a user select a given disease
     	ArrayList<String> list =new ArrayList<String>();
     	
     	
		cursor = db.rawQuery("select disease from user_disease", null );
		while( cursor.moveToNext() ){
			String disease = cursor.getString(0).toLowerCase().replace(" ","_");
			list.add( disease );

		}
		String user_disease_param=TextUtils.join(",", list);
        
		
		String[] all_disease_array= getResources().getStringArray(R.array.disease_list);
		ArrayList<String> list_all = new ArrayList<String>();
		
		for(int i=0; i< all_disease_array.length ; i++){
			String processed_disease= all_disease_array[i].toLowerCase().replace(" ","_");
			list_all.add( processed_disease);
		}
		String all_disease_param= TextUtils.join( ",",list_all ) ;
		
		
		// Prepare Navigation drawer menu 
		raw_news_array[0] = new GetNews();	
		raw_news_array[0].execute("http://api.geference.com/phn/"+user_disease_param);
		
		raw_news_array[1] = new GetNews();	
		raw_news_array[1].execute("http://api.geference.com/phn/"+all_disease_param);
		
		
		
		while(true){
			if( raw_news_array[0].checkDone() == true ){
				fileContent = raw_news_array[0].getContent();
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
			
			// Clear news list 
			LinearLayout item_wrapper = (LinearLayout)findViewById(R.id.item_wrapper);			
			item_wrapper.removeAllViews();
			
			
			if( pos == 1 ){
				while(true){
					if( raw_news_array[1].checkDone() == true ){
						fileContent = raw_news_array[1].getContent();
						displayNews(fileContent);
						
						break;
					}
					try{
						Thread.sleep(20);
					}catch(Exception e){}
				}
				
				
				
			}
			else{
				
				fileContent = raw_news_array[0].getContent();
				displayNews(fileContent);
				
			}
			
			
			/*
			Fragment fragment = new ExamFragment();
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
			*/
			
			lv.setItemChecked(pos, true);
			getActionBar().setTitle(""+ lv.getItemAtPosition(pos) );
			dl.closeDrawer(lv);
			
			
			
			
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
		
		// Check if a given image is in local device
		// if not, download image
		
		String path = Environment.getDataDirectory().getAbsolutePath();
		int idx= img_url.lastIndexOf('/');
		String img_file_name = img_url.substring(idx+1);
		path+= "/data/com.geference/files/" +  img_file_name;
		
		if( new File(path).exists() == false){
			new GetImage( img_view, getBaseContext() ).execute( img_url, img_file_name  );
			
			
			
		}
		else{
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			img_view.setImageBitmap( bitmap );
			
		}
		
		
		
		
		
		
		return view;
	}
	
	
	
	
	
	  // Filter in only user_diseases 
	protected void displayNews(String fileContent)   {
		
		
		// Display news 
		LinearLayout item_wrapper = (LinearLayout)findViewById(R.id.item_wrapper);			
		
		
		try{
			JSONArray newsJson = new JSONArray(fileContent);
			
			for(int i=0; i< newsJson.length() ; i++){
				JSONObject json = newsJson.getJSONObject(i); 
			
				String title =json.getString("title") ;
				String date =json.getString("date") ;
				String img_url_str = json.getString("lead_image_url");
				String disease_type = json.getString("disease_type");
				
				
						
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

