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

		column = new String[WifiReferencePointVO.aryApList.size() + 3];
		column[0] = "_PATH_NUMBER";
		column[1] = "_POSITION_X";
		column[2] = "_POSITION_Y";
		for (int idx = 3; idx < column.length; idx++) {
			String point = WifiReferencePointVO.aryApList.get(idx - 3)
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

						if (i == 0) {
							map.put(WifiReferencePointVO.PATH_NUMBER, strData);
						} else if (i == 1) {
							map.put(WifiReferencePointVO.POSITION_X, strData);
						} else if (i == 2) {
							map.put(WifiReferencePointVO.POSITION_Y, strData);
						}

						if (i >= 3) {
							String columName = c.getColumnName(i);
							for (int dataListIndex = 0; dataListIndex < scanData
									.size(); dataListIndex++) {
								if (columName.equals(scanData
										.get(dataListIndex).get("AP_BSSID"))) {
									currentFingerPrints[i - 3] = (Integer) scanData
											.get(dataListIndex).get("AP_RSSI");
									dbFingerPrints[i - 3] = Integer
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

	private void queryReferencePointInLine() {
		// TODO Auto-generated method stub
		Log.i(TAG, "queryReferencePointInLine BEGIN");

		int[] dbFingerPrints = new int[scanData.size()];
		int[] currentFingerPrints = new int[scanData.size()];
		
		HashMap<String, Object> map = new HashMap<String, Object>();

		column = new String[WifiReferencePointVO.aryApList.size() + 3];
		column[0] = "_PATH_NUMBER";
		column[1] = "_POSITION_X";
		column[2] = "_POSITION_Y";
		for (int idx = 3; idx < column.length; idx++) {
			String point = WifiReferencePointVO.aryApList.get(idx - 3)
					.toString().replace(":", "_");
			// column[idx] = "AP_"+WifiReferencePointVO.aryApList.get(idx-2);
			column[idx] = "AP_" + point;
		}


		String qTableName = WifiReferencePointVO.TABLE_NAME;
		try {
			Cursor c = null;
			c = db.query(qTableName, column, "_PATH_NUMBER="
					+nearestLine, null, null, null, null);

			if (c.getCount() >= 1) {
				for (int cIndex = 0; cIndex < c.getCount(); cIndex++) {
					c.moveToPosition(cIndex);
					map = new HashMap<String, Object>();
					for (int i = 0; i < c.getColumnCount(); i++) {
						String strData = c.getString(i);

						if (i == 0) {
							map.put(WifiReferencePointVO.PATH_NUMBER, strData);
						} else if (i == 1) {
							map.put(WifiReferencePointVO.POSITION_X, strData);
						} else if (i == 2) {
							map.put(WifiReferencePointVO.POSITION_Y, strData);
						}

						if (i >= 3) {
							String columName = c.getColumnName(i);
							for (int dataListIndex = 0; dataListIndex < scanData
									.size(); dataListIndex++) {
								if (columName.equals(scanData
										.get(dataListIndex).get("AP_BSSID"))) {
									currentFingerPrints[i - 3] = (Integer) scanData
											.get(dataListIndex).get("AP_RSSI");
									dbFingerPrints[i - 3] = Integer
											.parseInt(strData);
								}
							}
						}

					}

					int distance = 0;
					distance = calculateDistance(currentFingerPrints,
							dbFingerPrints);
					map.put(WifiReferencePointVO.DISTANCE, distance);
					regularDataList.add(map);
				}

			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void findPointsInLine(ArrayList<HashMap> list) {
		// TODO Auto-generated method stub
		Log.i(TAG, "findPointsInLine BEGIN");

		Collections.sort(list, new Comparator<HashMap>() {
			@Override
			public int compare(HashMap lhs, HashMap rhs) {
				// TODO Auto-generated method stub
				return (Integer) rhs.get(ReferencePointVO.DISTANCE) < (Integer) lhs
						.get(ReferencePointVO.DISTANCE) ? 1 : -1;
			}
		});

		ArrayList<String> pathNumber1 = new ArrayList<String>();
		ArrayList<String> pathNumber2 = new ArrayList<String>();
		ArrayList<String> pathNumber3 = new ArrayList<String>();

		String[] aryPathNumInNearestK = new String[] {
				(String) list.get(0).get(WifiReferencePointVO.PATH_NUMBER),
				(String) list.get(1).get(WifiReferencePointVO.PATH_NUMBER),
				(String) list.get(2).get(WifiReferencePointVO.PATH_NUMBER) };

		pathNumber1.add(aryPathNumInNearestK[0]);
		if (aryPathNumInNearestK[1].equals(aryPathNumInNearestK[0])) {
			pathNumber1.add(aryPathNumInNearestK[1]);
		} else {
			pathNumber2.add(aryPathNumInNearestK[1]);
		}

		if (aryPathNumInNearestK[2].equals(aryPathNumInNearestK[0])) {
			pathNumber1.add(aryPathNumInNearestK[1]);
		} else if (aryPathNumInNearestK[2].equals(aryPathNumInNearestK[1])
				&& !aryPathNumInNearestK[2].equals(aryPathNumInNearestK[0])) {
			pathNumber2.add(aryPathNumInNearestK[1]);
		} else {
			pathNumber3.add(aryPathNumInNearestK[1]);
		}

		int[] aryPathNumberArraySize = new int[] { pathNumber1.size(),
				pathNumber2.size(), pathNumber3.size() };

		if (aryPathNumberArraySize[0] >= 2) {
			nearestLine = aryPathNumInNearestK[0];
		}
		if (aryPathNumberArraySize[1] >= 2) {
			nearestLine = aryPathNumInNearestK[1];
		}
		if (aryPathNumberArraySize[0] == 1 && aryPathNumberArraySize[1] == 1
				&& aryPathNumberArraySize[2] == 1) {
			nearestLine = aryPathNumInNearestK[0];
		}

		Log.i(TAG, "findPointsInLine END, nearestLine = L" +nearestLine);
	}

}
