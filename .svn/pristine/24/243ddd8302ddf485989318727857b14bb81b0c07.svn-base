package com.andvantech.dsnavi_sitesurvey;

import android.provider.BaseColumns;

public class PointAccessDataVO  implements BaseColumns{
	
	public static String TABLE_NAME = "POINTS_TABLE";
	
	public final static String SSID = "_SSID";
	public final static String BSSID = "_BSSID";
	public final static String OTHERSSID = "_OTHERSSID";
	public final static String OTHERBSSID = "_OTHERBSSID";
	public final static String POINTX = "_POINTX";
	public final static String POINTY = "_POINTY";
	public final static String COLOR = "_COLOR";
	public final static String FLOOR ="_FLOOR";
	public final static String SOURCE ="_SOURCE";
	
	public final static String DBName = "/sdcard/point_db.db";
	
	public static String CREATE_POINT_SQL = "CREATE TABLE " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SSID + " TEXT, " +
            BSSID + " TEXT, " +
            OTHERSSID + " TEXT, " +
            OTHERBSSID + " TEXT, " +
            POINTX + " TEXT, " +
            POINTY + " TEXT, " +
            COLOR + " TEXT,"+
            FLOOR + " TEXT," +
            SOURCE + " TEXT" +")";
		
	public static String DROP_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
