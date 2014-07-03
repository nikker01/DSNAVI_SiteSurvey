package com.andvantech.dsnavi_sitesurvey;

public class APInfo 
{
	private int rssi ;
	private String ssid;
	private String bssid;
	private String otherBSSID;
	private String otherSSID;
	private String pointX,pointY;
	private String color;
	private String floor;
	private String source;
	//private String updateState;
	public APInfo()
	{
		this.ssid="";
		this.bssid="";
		this.rssi=0;
		this.otherBSSID="";
		this.otherSSID="";
		this.pointX="";
		this.pointY="";
		this.color="";
		this.floor="";
		this.source="";
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

	public APInfo(String ssid,String bssid,int rssi)
	{
		this.ssid = ssid;
		this.bssid = bssid;
		this.rssi = rssi;
	}

	
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
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

	public String getOtherBSSID() {
		return otherBSSID;
	}

	public void setOtherBSSID(String otherBSSID) {
		this.otherBSSID = otherBSSID;
	}

	
	
	public String getOtherSSID() {
		return otherSSID;
	}



	public void setOtherSSID(String otherSSID) {
		this.otherSSID = otherSSID;
	}



	public int getRssi() {
		return rssi;
	}

	public String getSsid() {
		return ssid;
	}

	public String getBssid() {
		return bssid;
	}
	
	public boolean lessThan(int o_rssi)
	{
		if(this.rssi<=o_rssi)
			return true;
		else
			return false;
		
	}
}
