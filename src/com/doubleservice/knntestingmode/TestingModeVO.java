package com.doubleservice.knntestingmode;

import android.provider.BaseColumns;

public class TestingModeVO  implements BaseColumns{

	public final static String DBName = "/sdcard/testing_mode_db.db";
	public final static String TABLE_NAME = "TestingMode";
	public final static String POSITION_X = "_POSITION_X";
	public final static String POSITION_Y = "_POSITION_Y";
	public final static String POS_JUMP_2M = "_POS_JUMP_2M";
	public final static String POS_JUMP_3M = "_POS_JUMP_3M";
	public final static String DISTANCE = "_DISTANCE";
	public final static String SITESURVEY = "_SITESURVEY";
	
	public String mPosX = "";
	public String mPosY = "";
	public String mPosJump2M = "";
	public String mPosJump3M = "";
	public String mDis = "";
	
	public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			POSITION_X+ " TEXT, " +
			POSITION_Y+ " TEXT, " +
			POS_JUMP_2M+ " TEXT, " +
			POS_JUMP_3M+ " TEXT, " +
			DISTANCE+ " TEXT, " +
			SITESURVEY+ " TEXT " +")" ;
	
}
