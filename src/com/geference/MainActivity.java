package com.geference;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity{


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash );
		
		Handler myhandler = new Handler();
		  
        // run a thread to start the home screen
        myhandler.postDelayed(new Runnable()
        {
            @Override
            public void run() 
            {
               finish();    
               
               // Check if first use 
               SharedPreferences pref = getSharedPreferences( "UserProfile",0);
               String deviceID = pref.getString("deviceID", null);
               
               if( deviceID == null ){
            	  // Direct to user setting page 
            	   startActivity( new Intent( MainActivity.this, FirstUseActivity.class ) );
               }
               else{
            	   startActivity( new Intent(MainActivity.this, HomeActivity.class) ) ;
               }
            }
  
        }, 1000); 
	
	
	}
		
}
