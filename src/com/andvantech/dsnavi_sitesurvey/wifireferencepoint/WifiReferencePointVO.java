package com.andvantech.dsnavi_sitesurvey.wifireferencepoint;

import java.util.ArrayList;

import android.provider.BaseColumns;

public class WifiReferencePointVO implements BaseColumns{

	public final static String DBName = "/sdcard/referencepoint_db.db";
	public final static String TABLE_NAME = "Reference_Points";
	public final static String DISTANCE = "_DISTANCE";
	public final static String PATH_NUMBER = "_PATH_NUMBER";
	public final static String POSITION_X = "_POSITION_X";
	public final static String POSITION_Y = "_POSITION_Y";
	
	public static int ApListSize = 0;
	public static ArrayList<String> aryApList = new ArrayList<String>();
	
	public String mPathNum;
	public String mPosX = "";
	public String mPosY = "";
	public int[] rssiArray;
	
	public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			PATH_NUMBER+ " TEXT, " +
			POSITION_X+ " TEXT, " +
			POSITION_Y+ " TEXT, " ;
}
