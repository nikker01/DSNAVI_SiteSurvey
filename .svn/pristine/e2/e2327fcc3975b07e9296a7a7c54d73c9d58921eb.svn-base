package com.andvantech.dsnavi_sitesurvey;

import android.provider.BaseColumns;

public class SiteSurveyAccessDataVO  implements BaseColumns{
	
	public static String TABLE_NAME = "SITESURVEY_TABLE";
	
	public final static String BSSID_LIST = "_BSSID_LIST";
	public final static String RSSI_LIST = "_RSSI_LIST";
	public final static String POINTX = "_POINTX";
	public final static String POINTY = "_POINTY";
	public final static String FLOOR ="_FLOOR";
	
	public final static String DBName = "/sdcard/sutesurvey_db.db";
	
	public static String CREATE_POINT_SQL = "CREATE TABLE " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            BSSID_LIST + " TEXT, " +
            RSSI_LIST + " TEXT, " +
            POINTX + " TEXT, " +
            POINTY + " TEXT, " +
            FLOOR + " TEXT" +")";
		
	public static String DROP_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
