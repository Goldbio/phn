package com.geference;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLdb extends SQLiteOpenHelper {

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
