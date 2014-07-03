package com.andvantech.dsnavi_sitesurvey.referencepoints;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReferencePointProxy {

	private ReferencePointDBHelper dbHelper;
	private SQLiteDatabase db;
	Context ctx;
	private String TAG = "ReferencePointProxy";
	private String[] column; /* = {"_POSITION_X", "_POSITION_Y", 
			"_BEACON_A", "_BEACON_B", "_BEACON_C", "_BEACON_D"};*/
	private int[] mFingerPrints; // = new int[]{0, 0, 0, 0};

	public ReferencePointProxy(Context context) {
		this.ctx = context;
		initDB();
	}
	
	public void initDB() {
		dbHelper = new ReferencePointDBHelper(ctx);
		db = dbHelper.getWritableDatabase();
	}

	public void setReferencePoint(ReferencePointVO vo) {
		Log.i(TAG, "setReferencePoint BEGIN ");
		
		String qTableName = ReferencePointVO.TABLE_NAME;
		
		ContentValues values = new ContentValues();
		values.put(ReferencePointVO.POSITION_X, vo.mPosX);
		values.put(ReferencePointVO.POSITION_Y, vo.mPosY);
		
		for(int i = 0; i < ReferencePointVO.aryBeaconList.size(); i++) {
			values.put("Beacon_"+ReferencePointVO.aryBeaconList.get(i), vo.beaconArray[i]);
		}
		
		/*
		values.put(ReferencePointVO.BEACON_A, vo.mBeaconA_rssi);
		values.put(ReferencePointVO.BEACON_B, vo.mBeaconB_rssi);
		values.put(ReferencePointVO.BEACON_C, vo.mBeaconC_rssi);
		values.put(ReferencePointVO.BEACON_D, vo.mBeaconD_rssi);
		*/
		db.insert(qTableName, null, values);
		
	}
	
	public ArrayList<HashMap> queryReferencePointDis(int[] beaconRssi) {
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		mFingerPrints = new int[beaconRssi.length];
		
		column = new String[ReferencePointVO.aryBeaconList.size() + 2];
		column[0] = "_POSITION_X";
		column[1] = "_POSITION_Y";
		for(int idx = 2; idx < column.length; idx++){
			column[idx] = "Beacon_"+ReferencePointVO.aryBeaconList.get(idx-2);
		}
		
		String qTableName = ReferencePointVO.TABLE_NAME;
		try {
			Cursor c = null;
			c = db.query(qTableName, column, "_POSITION_X="
					+ ReferencePointVO.POSITION_X, null, null, null,
					null);
			
			if (c.getCount() >= 1) {
				for (int cIndex = 0; cIndex < c.getCount(); cIndex++) {
					map = new HashMap<String, Object>();
					c.moveToPosition(cIndex);
					
					for (int i = 0; i < c.getColumnCount(); i++) {
						String strData = c.getString(i);
						
						if(i == 0) {
							map.put(ReferencePointVO.POSITION_X, strData);
						} else if(i == 1) {
							map.put(ReferencePointVO.POSITION_Y, strData);
						} else if(i >= 2) {
							mFingerPrints[i-2] = Integer.parseInt(strData);
						}
						
						
						/*
						switch(i) {
						case 0:
							map.put(ReferencePointVO.POSITION_X, strData);
							break;
						case 1:
							map.put(ReferencePointVO.POSITION_Y, strData);
							break;
							
						
						case 2:
							mFingerPrints[0] = Integer.parseInt(strData);
							break;
						case 3:
							mFingerPrints[1] = Integer.parseInt(strData);
							break;
						case 4:
							mFingerPrints[2] = Integer.parseInt(strData);
						case 5:
							mFingerPrints[3] = Integer.parseInt(strData);
							break;
							
						}
					*/
					}
					int distance = 0;
					distance = calculateDistance(beaconRssi, mFingerPrints);
					map.put(ReferencePointVO.DISTANCE, distance);
					list.add(map);
				}
				
				c.close();
				dbHelper.close();
				db.close();
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	private int calculateDistance(int[] currentFingerPrint,  int[] dbFingerPrints) {
		// TODO Auto-generated method stub
		
		int distance = 0;
		try {
			for(int cIdx = 0; cIdx < currentFingerPrint.length; cIdx++) {
				distance = distance + (int) Math.pow((currentFingerPrint[cIdx]-dbFingerPrints[cIdx]), 2);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Log.i(TAG, "Distance = " +distance);
		
		return distance;
	}
}
