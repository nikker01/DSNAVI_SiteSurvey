package com.andvantech.dsnavi_sitesurvey.position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.andvantech.dsnavi_sitesurvey.RssiMeanFilter;
import com.andvantech.dsnavi_sitesurvey.referencepoints.ReferencePointVO;
import com.andvantech.dsnavi_sitesurvey.wifireferencepoint.WifiReferencePointProxy;
import com.andvantech.dsnavi_sitesurvey.wifireferencepoint.WifiReferencePointVO;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.util.Log;

public class WifiFingerPrintReceiver extends BroadcastReceiver {

	position_1F act;
	private int mFunctioName;
	private String TAG = "WifiFingerPrintReceiver";
	
	private final int FUNCTION_SITESURVEY = 0;
	private final int FUNCTION_LOCATION	= 1;
	private final int FUNCTION_TESTING = 2;

	private ArrayList<Integer>[] rssiGroup;
	private ArrayList<String> mApName = new ArrayList<String>();
	private int[] mApScanRssi;
	
	private int currentScanCount = 0;
	private int maxScanCount = 0;
	
	private ArrayList<HashMap> scanDataList;

	public WifiFingerPrintReceiver(position_1F activity, int functionName) {
		this.act = activity;
		this.mFunctioName = functionName;

		mApName = WifiReferencePointVO.aryApList;
		rssiGroup = (ArrayList<Integer>[]) new ArrayList[mApName.size()];
		mApScanRssi = new int[mApName.size()];

		for (int i = 0; i < WifiReferencePointVO.aryApList.size(); i++) {
			rssiGroup[i] = new ArrayList<Integer>();
		}
		
		mFunctioName = functionName;
		scanDataList = new ArrayList<HashMap>();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onReceive BEGIN");
		currentScanCount++;

		List<ScanResult> results = act.wiFiManager.getScanResults();
		for (ScanResult result : results) {
			// result.timestamp= 100;
			
			String point = result.BSSID.toString();
			//Log.i(TAG, "MAC of the AP= " +point);

			//if(point.equals(object))
			
			if (result.level < -20) {
				try {
					int idx = mApName.indexOf(point);
					rssiGroup[idx].add(result.level);
					//Log.i(TAG, "MAC of the AP =" +point);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}
		
		if(mFunctioName == FUNCTION_SITESURVEY) {
			maxScanCount = 3;
			if(currentScanCount >= maxScanCount){
				calculateMeanRssi();
				clearRssiArray();
				act.setSiteSurveyRssiData(mApScanRssi);
			} else {
				act.wiFiManager.startScan();
			}
			
		} else if(mFunctioName == FUNCTION_LOCATION) {
			maxScanCount = 1;
			if(currentScanCount >= maxScanCount){
				calculateMeanRssi();
				clearRssiArray();
				
				queryNearestDistance(mApScanRssi);
				
				//act.setCurrentLocationRssi(mApScanRssi);
			} else {
				act.wiFiManager.startScan();
			}
		} 
	}
	
	private void queryNearestDistance(int rssi[]) {
		Log.i(TAG, "queryNearestDistance BEGIN");
		
		WifiReferencePointProxy proxy = new WifiReferencePointProxy(act);
		proxy.initDB();
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		//list = proxy.queryReferencePointDis(mApScanRssi);
		list = proxy.queryReferencePointDis(scanDataList);
		//list = proxy.getRegularCoodinate();
		
		Collections.sort(list, new Comparator<HashMap>() {
			@Override
			public int compare(HashMap lhs, HashMap rhs) {
				// TODO Auto-generated method stub
				
				return (Integer) rhs.get(WifiReferencePointVO.DISTANCE) == (Integer) lhs
						.get(WifiReferencePointVO.DISTANCE) ? 0 :
							((Integer) rhs.get(WifiReferencePointVO.DISTANCE) < (Integer) lhs
									.get(WifiReferencePointVO.DISTANCE) ? 1 : -1);
				
				//return (Integer) rhs.get(WifiReferencePointVO.DISTANCE) < (Integer) lhs
					//	.get(WifiReferencePointVO.DISTANCE) ? 1 : -1;
			}
		});
		
		float newPositionX = (
				Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_X)) +
				Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_X)) + 
				Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_X)) 
				) / 3;
		float newPositionY = (
				Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_Y)) + 
				Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_Y)) + 
				Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_Y))
				) / 3;
		
		double x0_x1 =  Math.pow(
		Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_X)) - 
		Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_X)), 2);
		double y0_y1 =Math.pow(
		Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_Y)) - 
		Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_Y)), 2);
		
		double x1_x2 = Math.pow(
		Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_X)) - 
		Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_X)), 2);
		double y1_y2 = Math.pow(
		Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_Y)) - 
		Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_Y)), 2);
		
		double x2_x0 = Math.pow(
		Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_X)) - 
		Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_X)), 2);
		double y2_y0 = Math.pow(
		Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_Y)) - 
		Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_Y)), 2);
		
		Log.i(TAG, "x0_x1 = "+x0_x1+" y0_y1 = "+y0_y1+" x1_x2 = "+x1_x2+" y1_y2 = "+y1_y2
				+" x2_x0 = "+x2_x0+" y2_y0 =" +y2_y0);
		
		double e1 = Math.sqrt(x0_x1 + y0_y1);
		double e2 = Math.sqrt(x1_x2 + y1_y2);
		double e3 = Math.sqrt(x2_x0 + y2_y0);
		
		String edges = "The Edge E1 = "+e1 + " E2 = "+e2 + " E3 = " +e3; 
		
		String res = "The 1st Nearest posX = " + newPositionX + " posY = "
				+ newPositionY;
		
		String mOnLine =  (String) list.get(0).get(WifiReferencePointVO.PATH_NUMBER);
		
		Log.i(TAG, "" +res);
		Log.i(TAG, "on Line: " +mOnLine);
		
		act.setCurrentLocation(newPositionX, newPositionY, rssi, mOnLine);
	}

	private void clearRssiArray() {
		// TODO Auto-generated method stub
		for (int apCount = 0; apCount < mApName.size(); apCount++) {
			rssiGroup[apCount].clear();
		}
	}

	private void calculateMeanRssi() {
		// TODO Auto-generated method stub
		for (int apCount = 0; apCount < mApName.size(); apCount++) {

			RssiMeanFilter meanFilter = new RssiMeanFilter(rssiGroup[apCount]);
			mApScanRssi[apCount] = meanFilter.getMean();

			Log.i(TAG, "calculateBeaconMeanRssi " + mApName.get(apCount)
					+ " Avg RSSI = " + mApScanRssi[apCount]);
			
			if(mFunctioName == FUNCTION_LOCATION) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String apMac = "AP_" + mApName.get(apCount).toString().replace(":", "_");
				map.put("AP_BSSID", apMac);
				map.put("AP_RSSI", mApScanRssi[apCount]);
				//map.put("IBeacon_RSSI", value)
				scanDataList.add(map);
			}
		}
	}
}
