package com.geference;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GetImage extends AsyncTask<String, Bitmap , Bitmap> {

	private final WeakReference imageViewReference;
	private Bitmap img_bitmap;
	
	public GetImage(ImageView imageView){
		imageViewReference = new WeakReference(imageView);
		
	}
	
	protected Bitmap doInBackground(String... params) {
		
		try {
			URL img_url= new URL(params[0]);
			img_bitmap=BitmapFactory.decodeStream(img_url.openStream() ); 
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img_bitmap;
		
	}
	
	protected void onPostExecute(Bitmap bitmap){
		
		if( imageViewReference !=null ){
			ImageView imgView = (ImageView) imageViewReference.get();
			if( imgView != null){
				
				if( bitmap != null ){
					imgView.setImageBitmap(bitmap);
					
				}
				
			}
			
		}
	}
}
