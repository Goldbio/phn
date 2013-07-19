package com.geference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetImage extends AsyncTask<String, Void, Void> {

	private boolean isDone=false;
	private Bitmap img_bitmap;
	
	protected Void doInBackground(String... params) {
		
		try {
			URL img_url= new URL(params[0]);
			img_bitmap=BitmapFactory.decodeStream(img_url.openStream() ); 
		
			isDone=true;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

	
	protected boolean isDone(){
		return isDone;
		
	}
	
	protected Bitmap getImgBitmap(){
		return img_bitmap;
		
	}
}
