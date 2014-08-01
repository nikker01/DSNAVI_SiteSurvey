package com.doubleservice.knntestingmode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.andvantech.dsnavi_sitesurvey.wifireferencepoint.WifiReferencePointDBHelper;
import com.andvantech.dsnavi_sitesurvey.wifireferencepoint.WifiReferencePointVO;

public class TestingModeProxy {

	private TestingModeDBHelper dbHelper;
	private SQLiteDatabase db;
	Context ctx;

	private String TAG = "TestingModeProxy";
	
	private int outOfDistanceCount2M = 0;
	private int outOfDistanceCount3M = 0;
	
	private String[] column = new String[]{"_POSITION_X", "_POSITION_Y", "_POS_JUMP_2M",
			"_POS_JUMP_3M", "_DISTANCE", "_SITESURVEY"};
	
	public TestingModeProxy(Context context) {
		this.ctx = context;
		initDB();
	}

	public void initDB() {
		// TODO Auto-generated method stub
		dbHelper = new TestingModeDBHelper(ctx);
		db = dbHelper.getWritableDatabase();
	}
	
	public void closeDbAndCursor() {
		dbHelper.close();
		db.close();
	}
	
	public void setTestingData(TestingModeVO vo) {
		Log.i(TAG, "setTestingData BEGIN");
		
		String qTableName = TestingModeVO.TABLE_NAME;
		ContentValues values = new ContentValues();
		values.put(TestingModeVO.POSITION_X, vo.mPosX);
		values.put(TestingModeVO.POSITION_Y, vo.mPosY);
		values.put(TestingModeVO.DISTANCE, vo.mDis);
		values.put(TestingModeVO.POS_JUMP_2M, vo.mPosJump2M);
		values.put(TestingModeVO.POS_JUMP_3M, vo.mPosJump3M);

		db.insert(qTableName, null, values);

		Log.i(TAG, "setTestingData END");
	}
	
	public int queryOutOfDistance(String dis) {
		Log.i(TAG, "queryOutOfDistance BEGIN");
		
		String qTableName = TestingModeVO.TABLE_NAME;
		
		int outOfDistanceCount = 0;
		outOfDistanceCount2M = 0;
		outOfDistanceCount3M = 0;
		
		try {
			Cursor c = null;
			c = db.query(qTableName, column, "_POSITION_X="
					+ TestingModeVO.POSITION_X, null, null, null, null);
			
			if(c.getCount() >= 1) {
				for (int cIndex = 0; cIndex < c.getCount(); cIndex++) {
					c.moveToPosition(cIndex);
					for (int i = 0; i < c.getColumnCount(); i++) {
						String strData = c.getString(i);
						switch(i) {
						case 2:
							if(strData.equals("Y")) 
								outOfDistanceCount2M++;
							break;
						case 3:
							if(strData.equals("Y")) 
								outOfDistanceCount3M++;
							break;
						}
					}
				}
			}
			
			if(dis.equals("2M"))
				outOfDistanceCount = outOfDistanceCount2M;
			else if(dis.equals("3M"))
				outOfDistanceCount = outOfDistanceCount3M;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return outOfDistanceCount;
	}
}
