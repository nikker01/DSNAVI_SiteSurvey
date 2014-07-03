
package com.andvantech.dsnavi_sitesurvey;

public class linePointInfo 
{
	private int rssi ;
	private String bssidList;
	private String rssiList;
	private String pointX,pointY;
	//private String color;
	private String floor;
	//private String source;
	//private String updateState;
	public linePointInfo()
	{
		this.bssidList="";
		this.rssiList="";
		this.pointX="";
		this.pointY="";
		//this.color="";
		this.floor="";
		//this.source="";
		//this.updateState="";
	}
	
	
/*	
	public String getUpdateState() {
		return updateState;
	}



	public void setUpdateState(String updateState) {
		this.updateState = updateState;
	}

*/

	public linePointInfo(String bssidList,String rssiList, String pointX, String pointY)
	{
		this.bssidList = bssidList;
		this.rssiList = rssiList;
		this.pointX = pointX;
		this.pointY = pointY;
	}

	
	
	
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}
	/*
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	*/

	public void setRssiList(String rssiList) {
		this.rssiList = rssiList;
	}


	public void setBssidList(String bssidList) {
		this.bssidList = bssidList;
	}

	public String getPointX() {
		return pointX;
	}

	public void setPointX(String pointX) {
		this.pointX = pointX;
	}
	public String getPointY() {
		return pointY;
	}

	public void setPointY(String pointY) {
		this.pointY = pointY;
	}



	public String getRssiList() {
		return rssiList;
	}


	public String getBssidList() {
		return bssidList;
	}
	
	public boolean lessThan(int o_rssi)
	{
		if(this.rssi<=o_rssi)
			return true;
		else
			return false;
		
	}
}
