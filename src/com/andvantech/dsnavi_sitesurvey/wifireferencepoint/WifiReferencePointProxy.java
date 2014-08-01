package com.andvantech.dsnavi_sitesurvey.wifireferencepoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.andvantech.dsnavi_sitesurvey.referencepoints.ReferencePointVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WifiReferencePointProxy {

	private WifiReferencePointDBHelper dbHelper;
	private SQLiteDatabase db;
	Context ctx;

	private String TAG = "WifiReferencePointProxy";
	private String[] column;
	private int[] mFingerPrints;

	private ArrayList<HashMap> scanData = new ArrayList<HashMap>();
	private ArrayList<HashMap> regularDataList = new ArrayList<HashMap>();
	private String nearestLine = "";

	public WifiReferencePointProxy(Context context) {
		this.ctx = context;
		initDB();
	}

	public void initDB() {
		dbHelper = new WifiReferencePointDBHelper(ctx);
		db = dbHelper.getWritableDatabase();
	}

	public void setReferencePoint(WifiReferencePointVO rpVO) {
		// TODO Auto-generated method stub
		Log.i(TAG, "setReferencePoint BEGIN");

		String qTableName = WifiReferencePointVO.TABLE_NAME;
		ContentValues values = new ContentValues();
		values.put(WifiReferencePointVO.PATH_NUMBER, rpVO.mPathNum);
		values.put(WifiReferencePointVO.POSITION_X, rpVO.mPosX);
		values.put(WifiReferencePointVO.POSITION_Y, rpVO.mPosY);
		values.put(WifiReferencePointVO.AZIMUTH, rpVO.mAzimuth);
		for (int i = 0; i < WifiReferencePointVO.aryApList.size(); i++) {
			String strApName = WifiReferencePointVO.aryApList.get(i).toString()
					.replace(":", "_");
			values.put("AP_" + strApName, rpVO.rssiArray[i]);
		}

		db.insert(qTableName, null, values);

		Log.i(TAG, "setReferencePoint END");
	}

	private int calculateDistance(int[] currentFingerPrint, int[] dbFingerPrints) {
		// TODO Auto-generated method stub

		int distance = 0;
		try {
			for (int cIdx = 0; cIdx < currentFingerPrint.length; cIdx++) {
				distance = distance
						+ (int) Math
								.pow((currentFingerPrint[cIdx] - dbFingerPrints[cIdx]),
										2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (int) Math.sqrt(distance);
	}

	public ArrayList<HashMap> queryReferencePointDis(ArrayList<HashMap> scanDataList) {
		// TODO Auto-generated method stub
		Log.i(TAG, "queryReferencePointDis BEGIN");

		this.scanData = scanDataList;

		int[] dbFingerPrints = new int[scanData.size()];
		int[] currentFingerPrints = new int[scanData.size()];

		ArrayList<HashMap> list = new ArrayList<HashMap>();
		HashMap<String, Object> map = new HashMap<String, Object>();

		column = new String[WifiReferencePointVO.aryApList.size() + 4];
		column[0] = "_AZIMUTH";
		column[1] = "_PATH_NUMBER";
		column[2] = "_POSITION_X";
		column[3] = "_POSITION_Y";
		for (int idx = 4; idx < column.length; idx++) {
			String point = WifiReferencePointVO.aryApList.get(idx - 4)
					.toString().replace(":", "_");
			// column[idx] = "AP_"+WifiReferencePointVO.aryApList.get(idx-2);
			column[idx] = "AP_" + point;
		}

		String qTableName = WifiReferencePointVO.TABLE_NAME;

		try {

			Cursor c = null;
			c = db.query(qTableName, column, "_POSITION_X="
					+ WifiReferencePointVO.POSITION_X, null, null, null, null);

			if (c.getCount() >= 1) {
				for (int cIndex = 0; cIndex < c.getCount(); cIndex++) {
					c.moveToPosition(cIndex);
					map = new HashMap<String, Object>();
					for (int i = 0; i < c.getColumnCount(); i++) {
						String strData = c.getString(i);

						if (i == 1) {
							map.put(WifiReferencePointVO.PATH_NUMBER, strData);
						} else if (i == 2) {
							map.put(WifiReferencePointVO.POSITION_X, strData);
						} else if (i == 3) {
							map.put(WifiReferencePointVO.POSITION_Y, strData);
						} 

						if (i >= 4) {
							String columName = c.getColumnName(i);
							for (int dataListIndex = 0; dataListIndex < scanData.size(); dataListIndex++) {
								if (columName.equals(scanData
										.get(dataListIndex).get("AP_BSSID"))) {
									currentFingerPrints[i - 4] = (Integer) scanData
											.get(dataListIndex).get("AP_RSSI");
									dbFingerPrints[i - 4] = Integer
											.parseInt(strData);
								}
							}
						}

					}

					int distance = 0;
					distance = calculateDistance(currentFingerPrints,
							dbFingerPrints);
					map.put(WifiReferencePointVO.DISTANCE, distance);
					list.add(map);
				}

				//findPointsInLine(list);
				//queryReferencePointInLine();

				 c.close(); 
				 dbHelper.close(); 
				 db.close();


			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<HashMap> getRegularCoodinate() {
		return regularDataList;
	}

	public float getAzimuthValue(float posX, float posY) {
		Log.i(TAG, "getAzimuthValue posx = " +posX + " posy =" +posY);
		float mAzimuthValue = 0;
		
		float[] currentPos = new float[]{posX, posY};
		float[] dbPos = new float[]{0, 0};
		
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		column = new String[WifiReferencePointVO.aryApList.size() + 4];
		column[0] = "_AZIMUTH";
		column[1] = "_PATH_NUMBER";
		column[2] = "_POSITION_X";
		column[3] = "_POSITION_Y";
		for (int idx = 4; idx < column.length; idx++) {
			String point = WifiReferencePointVO.aryApList.get(idx - 4)
					.toString().replace(":", "_");
			// column[idx] = "AP_"+WifiReferencePointVO.aryApList.get(idx-2);
			column[idx] = "AP_" + point;
		}

		String qTableName = WifiReferencePointVO.TABLE_NAME;
		try {

			Cursor c = null;
			c = db.query(qTableName, column, "_POSITION_X="
					+ WifiReferencePointVO.POSITION_X, null, null, null, null);

			if (c.getCount() >= 1) {
				for (int cIndex = 0; cIndex < c.getCount(); cIndex++) {
					c.moveToPosition(cIndex);
					map = new HashMap<String, Object>();
					for (int i = 0; i < c.getColumnCount(); i++) {
						String strData = c.getString(i);
						
						if(i == 0) {
							map.put(WifiReferencePointVO.AZIMUTH, strData);
						} else if (i == 2) {
							map.put(WifiReferencePointVO.POSITION_X, strData);
							dbPos[0] = Float.parseFloat(strData);
						} else if (i == 3) {
							map.put(WifiReferencePointVO.POSITION_Y, strData);
							dbPos[1] = Float.parseFloat(strData);
						} 

					}

					int distance = 0;
					distance = findNearestAzimuthValue(currentPos, dbPos);
					map.put(WifiReferencePointVO.DISTANCE, distance);
					list.add(map);
				}

				//findPointsInLine(list);
				//queryReferencePointInLine();

				 c.close(); 
				 dbHelper.close(); 
				 db.close();


			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(list, new Comparator<HashMap>() {
			@Override
			public int compare(HashMap lhs, HashMap rhs) {
				// TODO Auto-generated method stub
				
				return (Integer) rhs.get(WifiReferencePointVO.DISTANCE) == (Integer) lhs
						.get(WifiReferencePointVO.DISTANCE) ? 0 :
							((Integer) rhs.get(WifiReferencePointVO.DISTANCE) < (Integer) lhs
									.get(WifiReferencePointVO.DISTANCE) ? 1 : -1);
			}
		});
		
		float value = Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.AZIMUTH));
		
		return value;
	}

	private int findNearestAzimuthValue(float[] currentPos, float[] dbPos) {
		// TODO Auto-generated method stub
		
		int distance = (int) (Math.pow((currentPos[0] - dbPos[0]), 2) + 
				Math.pow((currentPos[1] - dbPos[1]), 2));
		
		return distance;
	}
}