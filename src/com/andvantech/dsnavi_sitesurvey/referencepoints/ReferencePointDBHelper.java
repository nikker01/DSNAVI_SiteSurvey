package com.andvantech.dsnavi_sitesurvey.referencepoints;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ReferencePointDBHelper extends SQLiteOpenHelper{

	private static String name = ReferencePointVO.DBName;
	private static int dbVersion = 1;
	
	public ReferencePointDBHelper(Context context) {
		super(context, name, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("ReferencePointDBHelper", "onCreate BEGIN");
		db.execSQL(ReferencePointVO.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
