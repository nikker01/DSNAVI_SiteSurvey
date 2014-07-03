package com.andvantech.dsnavi_sitesurvey.referencepoints;

import java.util.ArrayList;

import android.provider.BaseColumns;

public class ReferencePointVO implements BaseColumns{
	
	public final static String DBName = "/sdcard/bigcity_sitesurvey_referencepoint_db.db";
	public final static String TABLE_NAME = "Reference_Points";
	public final static String DISTANCE = "_DISTANCE";
	public final static String POSITION_X = "_POSITION_X";
	public final static String POSITION_Y = "_POSITION_Y";
	
	public final static String BEACON_A = "_BEACON_A";
	public final static String BEACON_B = "_BEACON_B";
	public final static String BEACON_C = "_BEACON_C";
	public final static String BEACON_D = "_BEACON_D";
	
	public String mPosX = "";
	public String mPosY = "";
	public String mBeaconA_rssi = "";
	public String mBeaconB_rssi = "";
	public String mBeaconC_rssi = "";
	public String mBeaconD_rssi = "";
	
	public static int beaconListSize = 0;
	public static ArrayList<String> aryBeaconList = new ArrayList<String>();
	
	public String referencePoint = "";
	public int[] beaconRssi;
	public int[] beaconArray;

	
	public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			POSITION_X+ " TEXT, " +
			POSITION_Y+ " TEXT, " ;
			
	
	
	/*
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			POSITION_X+ " TEXT, " +
			POSITION_Y+ " TEXT, " +
			BEACON_A+ " TEXT, " +
			BEACON_B+ " TEXT, " +
			BEACON_C+ " TEXT, " +
			BEACON_D + " TEXT " + ")";
		*/	
}
