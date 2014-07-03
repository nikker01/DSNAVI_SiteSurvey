package com.andvantech.dsnavi_sitesurvey;

import java.util.ArrayList;

import com.andvantech.dsnavi_sitesurvey.APInfo;
public class scanAPParser 
{
	private ArrayList<String> apList ;
	private ArrayList<APInfo> apInfoList = new ArrayList<APInfo>();
	
	public scanAPParser(ArrayList<String> apInfoList)
	{
		this.apList = apInfoList;
		for(String apInfo:this.apList)
		{
			String[] split = apInfo.split("#");
			String ssid = split[0];
			String bssid = split[1];
			String[] rssiString = split[2].split(";");
			int rssi = Integer.parseInt(rssiString[0]);
			APInfo info = new APInfo(ssid,bssid,rssi);
			this.apInfoList.add(info);
		}
	}
	
	public ArrayList<APInfo> getAPInfoListBySort()
	{
		this.bubbleSort();
		return this.apInfoList;
	}
	
	private void bubbleSort()
	{
		APInfo temp;
		 if (this.apInfoList.size()>1) // check if the number of orders is larger than 1
	        {
	            for (int index=0; index<this.apInfoList.size(); index++) // bubble sort outer loop
	            {
	                for (int i=0; i < this.apInfoList.size()-index-1; i++) 
	                {
	                    if (this.apInfoList.get(i).lessThan(this.apInfoList.get(i+1).getRssi()))
	                    {
	                        temp = this.apInfoList.get(i);
	                        this.apInfoList.set(i,this.apInfoList.get(i+1) );
	                        this.apInfoList.set(i+1, temp);
	                    }
	                }
	            }
	        }
	}
	
}
