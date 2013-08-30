package com.geference;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FirstUseActivity extends Activity {

	SQLdb sqlDB;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstuse );
		
		
		ArrayAdapter<CharSequence> adapter;
		adapter = ArrayAdapter.createFromResource( this, R.array.disease_list ,android.R.layout.simple_list_item_multiple_choice);
		
		ListView lv = (ListView)findViewById(R.id.disease_list);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adapter);
		
		sqlDB = new SQLdb(this);
	
	}
	
	public void confirm_select(View v){
		ListView lv = (ListView)findViewById(R.id.disease_list);
		SparseBooleanArray sb = lv.getCheckedItemPositions();
		
		// Edit user profile on disease of interest 
		
		SQLiteDatabase db = sqlDB.getWritableDatabase();
		
		for(int i=0; i< lv.getCount() ; i++){		
			if( sb.get(i)){
				
				db.execSQL(" insert into user_disease values (null, '"+lv.getItemAtPosition(i).toString() +"');" );
				
				
			}
		}
		sqlDB.close(); 
		
		
		// Start news display 
		finish();
		startActivity( new Intent(FirstUseActivity.this, HomeActivity.class) ) ;
		
	}	
		
	
/*
	class SQLdb extends SQLiteOpenHelper {

		public SQLdb(Context context) {
			super(context, "disease.db",null,1);
			// TODO Auto-generated constructor stub
		}

		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("create table user_disease(_id integer primary key autoincrement, disease TEXT );" );
		}
		
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub			
		}
	}
*/
}
