package com.andvantech.dsnavi_sitesurvey.position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.andvantech.dsnavi_sitesurvey.RssiMeanFilter;
import com.andvantech.dsnavi_sitesurvey.referencepoints.ReferencePointVO;
import com.radiusnetworks.ibeacon.IBeacon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IBeaconScanReceiver extends BroadcastReceiver{

	private String TAG = "IBeaconScanReceiver";
	private ArrayList<Integer>[] rssiGroup;  //= (ArrayList<Integer>[]) new ArrayList[4];
	private ArrayList<String> myBeaconName = new ArrayList<String>();
	//private String[] myBeaconName = new String[] { "0_1", "1_0", "2_0", "1_1" };
	private int[] iBeaconRssi; // = new int[] { 0, 0, 0, 0 };
	private int maxScanCount = 5;
	private int mScanCount = 0;
	private static int FUNCTION_SITESURVEY = 0;
	private static int FUNCTION_LOCATION = 1;
	private int mFunctioName;
	
	private ArrayList<HashMap> list;
	
	position_1F mActivity;
	
	public IBeaconScanReceiver(position_1F act, int functionName) {
		this.mActivity = act;
		
		myBeaconName = ReferencePointVO.aryBeaconList;
		rssiGroup = (ArrayList<Integer>[]) new ArrayList[myBeaconName.size()]; 
		iBeaconRssi = new int[myBeaconName.size()];
		
		for (int i = 0; i < ReferencePointVO.aryBeaconList.size(); i++) {
			rssiGroup[i] = new ArrayList<Integer>();
		}
		
		mFunctioName = functionName;
		
		list = new ArrayList<HashMap>();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mScanCount++;
		Log.i(TAG, "onReceive BEGIN");
		
		HashMap<String, IBeacon> beacons = (HashMap<String, IBeacon>) intent
				.getSerializableExtra("IBEACON_HASHMAP");
		Iterator localIterator = beacons.entrySet().iterator();
		
		try {
			while (localIterator.hasNext()) {

				if (!localIterator.hasNext()) {
					return;
				}

				IBeacon localIBeacon = (IBeacon) ((Map.Entry) localIterator
						.next()).getValue();

				String point = localIBeacon.getMajor() + "_"
						+ localIBeacon.getMinor();

				/*
				Log.i("addIBeconsDetails",
						"UUID = " + localIBeacon.getProximityUuid()
								+ "  Major = "
								+ Integer.toString(localIBeacon.getMajor())
								+ "  Minor = "
								+ Integer.toString(localIBeacon.getMinor())
								+ "  point  = " + point + "  RSSI = "
								+ Integer.toString(localIBeacon.getRssi()));
*/
	
				try {
					
					int idx = myBeaconName.indexOf(point);
					if (localIBeacon.getRssi() < -40)
						rssiGroup[idx].add(localIBeacon.getRssi());
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			
				/*
				if (point.equals(myBeaconName[0])) {
					if (localIBeacon.getRssi() < -40)
						rssiGroup[0].add(localIBeacon.getRssi());
				} else if (point.equals(myBeaconName[1])) {
					if (localIBeacon.getRssi() < -40)
						rssiGroup[1].add(localIBeacon.getRssi());
				} else if (point.equals(myBeaconName[2])) {
					if (localIBeacon.getRssi() < -40)
						rssiGroup[2].add(localIBeacon.getRssi());
				} else if (point.equals(myBeaconName[3])) {
					if (localIBeacon.getRssi() < -40)
						rssiGroup[3].add(localIBeacon.getRssi());
				} 
				*/
				
			}

			Log.i(TAG, "addIBeconsDetails END");
		} catch (NullPointerException e) {
			Log.i(TAG, "addIBeconsDetails With Null Data");
		}
		
		if(mFunctioName == FUNCTION_SITESURVEY) {
			maxScanCount = 5;
			if(mScanCount >= maxScanCount) {
				finishScanning();
			}
		} else if(mFunctioName == FUNCTION_LOCATION) {
			maxScanCount = 3;
			if(mScanCount >= maxScanCount) {
				calculateBeaconMeanRssi();
				clearBeaconRssiArray();
				mActivity.setCurrentFingerPrint(iBeaconRssi);
			}
		}
		
	}

	private void finishScanning() {
		// TODO Auto-generated method stub
		Log.i(TAG, "finishScanning BEGIN");
		
		calculateBeaconMeanRssi();
		clearBeaconRssiArray();
		mActivity.setBeaconData(iBeaconRssi);
		//mActivity.siteSurveyMoving();
		
	}

	private void calculateBeaconMeanRssi() {
		// TODO Auto-generated method stub
		for (int apCount = 0; apCount < myBeaconName.size(); apCount++) {

			RssiMeanFilter meanFilter = new RssiMeanFilter(rssiGroup[apCount]);
			iBeaconRssi[apCount] = meanFilter.getMean();

			Log.i(TAG, "calculateBeaconMeanRssi " + myBeaconName.get(apCount)
					+ " Avg RSSI = " + iBeaconRssi[apCount]);
			
			if(mFunctioName == FUNCTION_LOCATION) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("IBeacon_Major_Minor", myBeaconName.get(apCount));
				//map.put("IBeacon_RSSI", value)
			}
			
		}
	}
	
	private void clearBeaconRssiArray() {
		// TODO Auto-generated method stub
		for (int apCount = 0; apCount < myBeaconName.size(); apCount++) {
			rssiGroup[apCount].clear();
		}
	}

}
