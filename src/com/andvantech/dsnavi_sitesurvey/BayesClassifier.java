package com.andvantech.dsnavi_sitesurvey;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class BayesClassifier {

	private String TAG = "BayesClassifier";
	private double mLocationPosibility = 1;
	
	public BayesClassifier(HashMap dataMap, ArrayList<HashMap> scanDataList) {
		Log.i(TAG, "BayesClassifier BEGIN");
		
		for(int i = 0; i < scanDataList.size(); i++) {
			String apName = (String) scanDataList.get(i).get("AP_BSSID");
			int apScanLevel = (Integer) scanDataList.get(i).get("AP_RSSI");
			int mSignalMeanData = (Integer) dataMap.get(apName+"_RSSI");
			double mSignalStandardDeviationData = (Double) dataMap.get(apName+"_SD");
			
			float exPower =(float) ( (Math.pow((apScanLevel - mSignalMeanData), 2)) / 
					(2*(Math.pow(mSignalStandardDeviationData, 2))) );
		
			float exp = (float) Math.pow(2.72, -exPower);
			float pi2 = (float) Math.sqrt(2*3.14);
			float p; 
			BigDecimal b1 = new BigDecimal(Float.toString(exp));
			BigDecimal b2 = new BigDecimal(Float.toString(pi2));
			p = b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP).floatValue();
			
			//p = (Double)(exp/pi2);
			//DecimalFormat df = new DecimalFormat("##.00");
			mLocationPosibility = mLocationPosibility*p; //(Double.parseDouble(df.format(p)));
			
		}
		
		Log.i(TAG, "BayesClassifier posibility = " +mLocationPosibility);
	}
	
	public double getLocationPosibility() {
		return mLocationPosibility;
	}
}
