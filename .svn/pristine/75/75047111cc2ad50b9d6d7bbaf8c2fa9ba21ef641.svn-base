package com.andvantech.dsnavi_sitesurvey.position;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ApMacScanReceiver extends BroadcastReceiver {

	ImageViewHelper apScan; //modify your class name here
	
	private ArrayList<String> apNameList = new ArrayList<String>();
	private ArrayList<String> apMacList = new ArrayList<String>();
	private ArrayList<String> apInfoList = new ArrayList<String>();
	
	private String TAG = "ApMacScanReceiver";
	private int bestSignalCount = 0;
	private int receiveCount;
	private String bestAPName = "";
	private String bestAPMac = "";
	
	private int apConfirmCount = apScan.scanCount;
	private int apScanFailValue = apConfirmCount + 1;

	public ApMacScanReceiver(ImageViewHelper apMacScan) {
		// TODO Auto-generated constructor stub
		super();
		this.apScan = apMacScan;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		receiveCount = receiveCount + 1;
		List<ScanResult> results = apScan.wiFiManager.getScanResults();
		ScanResult bestSignal = null;
		for (ScanResult result : results) {
			
			addToApList(result.SSID ,result.BSSID, result.level);
			
			if (bestSignal == null
					|| WifiManager.compareSignalLevel(bestSignal.level,
							result.level) < 0)
				bestSignal = result;
		}
		
		
		Log.i(TAG, "bestSignal.BSSID =" + bestSignal.BSSID + " bestAPName = "
				+ bestSignal.SSID + " bestAPLevel = " + bestSignal.level);

		
		Log.i(TAG, "bestSignalCount = " + bestSignalCount);
		
		if(receiveCount > apScanFailValue && bestSignalCount < apConfirmCount  )
		{
			Log.i(TAG, "search bestsignal ap fail");
			String msg = "Scan Fail";
			apScan.showScanRes(msg);
		}
		if (bestSignalCount < apConfirmCount) {

			if (bestSignal.SSID.equals(bestAPName)) {
				Log.i(TAG, "bestSignalCount++");
				bestSignalCount++;
			} else {
				bestAPName = bestSignal.SSID;
				bestAPMac = bestSignal.BSSID;
				bestSignalCount = 0;
			}
			
		} else if (bestSignalCount == apConfirmCount) {
			Log.i(TAG, "Get BestSignal AP SSID = " + bestAPName
					+ " BestSignal AP MAC = " + bestAPMac);
		}
		
		if(receiveCount < apScan.scanCount )
			apScan.fetchAPMac();
		else 
		{
			apScan.showScanResult(ScanResults.getApInfo());
			//receiveCount=0;
			if(bestSignalCount == apConfirmCount)
			{
				//String msg = "AP SSID = "+bestAPName +" \nAP Mac = "+bestAPMac;
				//apScan.showScanRes(msg);
				
			}
		}
		
		
	}

	private void addToApList(String ssid, String bssid, int rssiLevel) {
		// TODO Auto-generated method stub
		
		ScanResults.addToApList(ssid, bssid, rssiLevel);
	}
	

}
