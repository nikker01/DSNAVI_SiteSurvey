package com.andvantech.dsnavi_sitesurvey;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PointAccessDBHelper  extends SQLiteOpenHelper{

	public static boolean dbCreateFlag = false;
	public static int dbVersion = 1;
	private static String name = PointAccessDataVO.DBName;
	
	private SQLiteDatabase m_db;
	
	public PointAccessDBHelper(Context context) {
		super(context, name, null, dbVersion);
		// TODO Auto-generated constructor stub
		m_db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("PointAccessDBHelper", "onCreate BEGIN");
		
		dbCreateFlag = true;
		
		db.execSQL(PointAccessDataVO.CREATE_POINT_SQL);
		
		Log.i("PointAccessDBHelper", "onCreate END");
	}
	
	public void deletePointByFloor(String floor)
	{
		this.m_db.delete(PointAccessDataVO.TABLE_NAME, PointAccessDataVO.FLOOR+"='"+floor+"'" , null);
	}
	
	public void deletePointBySource(String source)
	{
		this.m_db.delete(PointAccessDataVO.TABLE_NAME, PointAccessDataVO.SOURCE+"='"+source+"'" , null);
	}
	
	public void dropTable()
	{
		//m_db.execSQL(PointAccessDataVO.DROP_SQL);
	}
	
	public boolean isTableExists(String tableName) {
	   

	    Cursor cursor = this.m_db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name ='"+tableName+"'",null);
	    if(cursor!=null) {
	          cursor.close();
	            return true;
	                   
	    }
	    cursor.close();
	    return false;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
	}
	
	@Override
	public synchronized void close()
	{
		super.close();
	}
}
