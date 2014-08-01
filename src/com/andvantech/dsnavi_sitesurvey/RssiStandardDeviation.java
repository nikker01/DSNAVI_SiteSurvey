package com.andvantech.dsnavi_sitesurvey;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RssiStandardDeviation {

	private int avg;
	private double mStandardDeviation = 1;
	private ArrayList<Integer> data;

	public RssiStandardDeviation(ArrayList<Integer> rssi) {

		int total = 0;
		this.data = rssi;

		if (data.size() > 0) {
			for (int index = 0; index < data.size(); index++) {
				total = total + data.get(index);
			}

			avg = total / data.size();
		}
		
		setStandardDeviation();
	}

	private void setStandardDeviation() {
		// TODO Auto-generated method stub
		int mSdTotal = 0;
		
		if(data.size() > 0) {
			for (int index = 0; index < data.size(); index++) {
				mSdTotal = mSdTotal + (int)(Math.pow( (data.get(index)-avg) , 2) );   
			}
			
			mStandardDeviation = Math.sqrt(mSdTotal/data.size());
			DecimalFormat df = new DecimalFormat("##.00");
			mStandardDeviation = Double.parseDouble(df.format(mStandardDeviation));
			if(mStandardDeviation < 1) {
				mStandardDeviation = 1;
			} 
		}
	}
	
	public double getStandardDeviation() {
		return mStandardDeviation;
	}
}
