package com.doubleservice.knntestingmode;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestingModeDBHelper extends SQLiteOpenHelper{

	private static String name = TestingModeVO.DBName;
	private static int dbVersion = 1;
	
	public TestingModeDBHelper(Context context) {
		super(context, name, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TestingModeVO.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
