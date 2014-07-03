package com.andvantech.dsnavi_sitesurvey.position;

import java.util.ArrayList;

public class ScanResults {

	private static ArrayList<String> apNameList = new ArrayList<String>();
	private static ArrayList<String> apMacList = new ArrayList<String>();
	private static ArrayList<String> apInfoList = new ArrayList<String>();
	
	public static void addToApList(String ssid, String bssid, int rssiLevel) {
		// TODO Auto-generated method stub
		boolean blnFound = apMacList.contains(bssid);
		if(!blnFound)
		{
			apNameList.add(ssid);
			apMacList.add(bssid);
			apInfoList.add(ssid+"#"+bssid+"#"+rssiLevel+";");
		}
	}
	
	public static ArrayList<String> getApName()
	{
		return apNameList;
	}
	
	public static ArrayList<String> getApMac()
	{
		return apMacList;
	}
	
	public static ArrayList<String> getApInfo()
	{
		return apInfoList;
	}
	public static void clearArrayList(){
		apNameList.clear();
		apMacList.clear();
		apInfoList.clear();
		
	}
}
