package com.geference;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		GetNews news = new GetNews();
		news.execute();
		while(true){
			if( news.checkDone() == true){
				String fileContent = news.getContent();
				Toast.makeText(this, fileContent,0).show();
				break;
			}
			try{
				Thread.sleep(200);
			}catch(Exception e){}
			
		}
		
	}

	

	
}

