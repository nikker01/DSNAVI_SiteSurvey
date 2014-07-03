package com.andvantech.dsnavi_sitesurvey;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class BaseRssiScanReceiver extends BroadcastReceiver {
	private ArrayList<Integer> ap_rssiList = new ArrayList<Integer>();

	private static final String TAG = "WiFiScanReceiver";
	BaseRssiScan rssiScan;
	private String bestAPName = "";
	private String bestAPMac = "";
	private int bestSignalCount = 0;
	private int receiveCount;
	private double rssi0;

	public BaseRssiScanReceiver(BaseRssiScan rssiScan) {
		super();
		this.rssiScan = rssiScan;
	}

	@Override
	public void onReceive(Context c, Intent intent) {
		Log.i(TAG, "onReceive");
		receiveCount = receiveCount + 1;
		List<ScanResult> results = rssiScan.wiFiManager.getScanResults();
		ScanResult bestSignal = null;
		for (ScanResult result : results) {
			if (bestSignal == null
					|| WifiManager.compareSignalLevel(bestSignal.level,
							result.level) < 0)
				bestSignal = result;
			
			if(bestSignalCount == 3 && result.BSSID.equals(bestAPMac))
			{
				Log.i(TAG, "Add Rssi To List: " +result.level);
				if(result.level < -20)
					ap_rssiList.add(result.level);
			}
		}


		Log.i(TAG, "bestSignal.BSSID =" + bestSignal.BSSID + " bestAPName = "
				+ bestSignal.SSID + " bestAPLevel = " + bestSignal.level);
		//Log.i(TAG, "onReceive() message: " + message);
		
		Log.i(TAG, "bestSignalCount = " +bestSignalCount);
		
		if(receiveCount > 4 && bestSignalCount < 3  )
		{
			Log.i(TAG, "search bestsignal ap fail");
			receiveCount = rssiScan.scanCount;
			String msg = "Scan Fail";
			rssiScan.showScanRes(msg);
			
		}	
		if (bestSignalCount < 3) {
			
			if (bestSignal.SSID.equals(bestAPName)) {
				Log.i(TAG, "bestSignalCount++");
				bestSignalCount++;
			} else {
				bestAPName = bestSignal.SSID;
				bestAPMac = bestSignal.BSSID;
				bestSignalCount = 0;
			}
		} else if(bestSignalCount == 3)
		{
			Log.i(TAG, "Get BestSignal AP SSID = " + bestAPName +" BestSignal AP MAC = " +bestAPMac);
		}
		

		if(receiveCount < rssiScan.scanCount )
			rssiScan.startScanning();
		else
			filterSignal();
	}

	private void filterSignal() {
		// TODO Auto-generated method stub
		Log.i(TAG, "filterSignal BEGIN");

		if(ap_rssiList.size()>0)
		{
			GFilter filter = new GFilter(ap_rssiList);
			rssi0 = filter.rssiFiltering();
			Log.i(TAG, "filterSignal rssi0 = " + rssi0);

			String msg = "AP SSID = "+bestAPName +" \nAP Mac = "+bestAPMac+ "\n RSSI0 = " +rssi0;
			rssiScan.bestAPName = bestAPName;
			rssiScan.bestAPMac = bestAPMac;
			rssiScan.rssi0 = rssi0;
			
			rssiScan.showScanRes(msg);
		}
	}

}
