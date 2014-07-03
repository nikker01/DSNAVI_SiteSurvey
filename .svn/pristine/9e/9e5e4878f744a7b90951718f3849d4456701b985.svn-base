package com.andvantech.dsnavi_sitesurvey;

import java.util.ArrayList;

import android.util.Log;

public class GFilter {

	private ArrayList<Integer> rssi_list = new ArrayList<Integer>();
	private ArrayList<Integer> rssi_filtering_list = new ArrayList<Integer>();
	private double total = 0;
	private double avg = 0;
	private double standarDeviation = 0;
	
	public GFilter(ArrayList<Integer> data)
	{
		this.rssi_list = data;
	}
	
	public double rssiFiltering()
	{
		calculateAvg();
		calculateSD();
		
		double floor = 0;
		double ceiling = 0;
		double gFilter_cal_total = 0;
		double average = 0;
		
		floor = 0.15 * standarDeviation + avg;
		ceiling = 3.09 * standarDeviation + avg;
		
		for (int filterIndex = 0; filterIndex < rssi_list.size(); filterIndex++) {
			if (rssi_list.get(filterIndex) >= floor
					&& rssi_list.get(filterIndex) <= ceiling)
				rssi_filtering_list.add(rssi_list.get(filterIndex));
		}
		
		for (int filterAvgIndex = 0; filterAvgIndex < rssi_filtering_list
				.size(); filterAvgIndex++) {
			gFilter_cal_total = rssi_filtering_list.get(filterAvgIndex) + gFilter_cal_total;
		}
		
		if (rssi_filtering_list.size() == 0)
			Log.i("GFilter", "data error");
		else
			average = gFilter_cal_total / rssi_filtering_list.size();
		
		return average;
	}

	private void calculateAvg() {
		// TODO Auto-generated method stub
		for (int calAvgIndex = 0; calAvgIndex < rssi_list.size(); calAvgIndex++) {
			total = rssi_list.get(calAvgIndex) + total;
		}
		
		avg = total / rssi_list.size();
	}
	
	private void calculateSD() {
		// TODO Auto-generated method stub
		double sdTotal = 0;
		
		for (int calSDIndex = 0; calSDIndex < rssi_list.size(); calSDIndex++) {
			sdTotal = sdTotal
					+ (Math.pow((rssi_list.get(calSDIndex) - avg), 2));
		}
		
		standarDeviation = Math.sqrt((sdTotal / (rssi_list.size() - 1)));
	}
}
