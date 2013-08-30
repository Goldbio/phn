package com.geference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

public class GetImage extends AsyncTask<String, Bitmap , Bitmap> {

	private final WeakReference imageViewReference;
	private Bitmap img_bitmap;
	private Context ctx;
	final long MAXFILEAGE = 2678400000L; 
	
	public GetImage(ImageView imageView, Context context){
		imageViewReference = new WeakReference(imageView);
		ctx = context;
	}
	
	protected Bitmap doInBackground(String... params) {
		
		try {
			URL img_url= new URL(params[0]);
			img_bitmap=BitmapFactory.decodeStream(img_url.openStream() ); 
			
			
			// Download image files to local device 
			int index_format_dot= params[1].lastIndexOf('.');
			String format = params[1].substring(index_format_dot+1);
			FileOutputStream fos = ctx.openFileOutput(params[1],0);
			
			if( format == "jpg"){
				
				img_bitmap.compress( Bitmap.CompressFormat.JPEG, 100, fos);
			}
			else{
				img_bitmap.compress( Bitmap.CompressFormat.PNG, 100, fos);
			}
			
			// Delete old files ( More than a month ) 
			
			String path = Environment.getDataDirectory().getAbsolutePath() + "/data/com.geference/files/" ; 
			File f = new File(path);
			File file[] = f.listFiles();
			for (int i=0; i < file.length; i++)
			{
				long lastModDate = file[i].lastModified() ;
				if(lastModDate + MAXFILEAGE< System.currentTimeMillis()) {
				      file[i].delete();
				}
				
			}
			
			
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
