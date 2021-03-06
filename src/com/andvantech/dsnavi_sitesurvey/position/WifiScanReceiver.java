package com.andvantech.dsnavi_sitesurvey.position;

import java.util.ArrayList;
import java.util.List;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.util.Log;

public class WifiScanReceiver extends BroadcastReceiver {

	private String TAG = "WifiScanReceiver";

	private ArrayList<String> apMACGroup;
	private ArrayList<Integer> apRssiGroup;
	private ArrayList<Integer> ap_rssi_filtering;
	float[] getRssiPointXY = new float[2];

	private ArrayList<String> mFloorList = new ArrayList<String>();
	private ArrayList<String> mAPNameList = new ArrayList<String>();
	private ArrayList<String> mAPMacList = new ArrayList<String>();
	private int avgScanCount = 10;
	private int maxScanCount = 1;
	position_1F main;
	Context mContext;

	private double rssi_filter_min = -70;
	private double rssi_filter_max = -20;

	private String[] rssiString;
	public boolean bIsCreated = false;

	public int deliverAPCount = 0;

	private static final Logger logger = LoggerFactory.getLogger();

	public WifiScanReceiver(position_1F main) {
		super();
		this.main = main;
		this.mContext = main.getApplicationContext();
		// this.mAPMacList = main.apMacList;
		// this.mAPNameList = main.apNameList;
		// this.mFloorList = main.floorList;
		// this.rssi_filter_max = 0 - main.rssiMin;
		// this.rssi_filter_min = 0 - main.rssiMax;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		apRssiGroup = new ArrayList<Integer>();
		apMACGroup = new ArrayList<String>();
		ap_rssi_filtering = new ArrayList<Integer>();
		// createAPInfoArray();
		List<ScanResult> results = main.wiFiManager.getScanResults();
		for (ScanResult result : results) {
			// result.timestamp= 100;

			if (result.level > rssi_filter_min
					&& result.level < rssi_filter_max) {
				apRssiGroup.add(result.level);
				apMACGroup.add(result.BSSID);

			}
		}
		getRssiPointXY = new float[2];
		getRssiPointXY = main.imageViewHelper
				.calNewPointPixel(main.imageViewHelper.matrixPoint);
		Log.i("WifiScanReceiver", "Current Position pointX: "
				+ getRssiPointXY[0] + ", pointY: " + getRssiPointXY[1]);
		for (int i = 0; i < apRssiGroup.size(); i++) {
			// Log.v("SiteSurvey","MAC: "+apMACGroup.get(i)+", RSSI: "+apRssiGroup.get(i));
		}

		main.siteSurveyMoving();
		// isNeedToPostData();
		// main.startScanning();
	}

	private void isNeedToPostData() {
		// apMACGroup.clear();
		// apRssiGroup.clear();
		// ap_rssi_filtering.clear();

	}

}
