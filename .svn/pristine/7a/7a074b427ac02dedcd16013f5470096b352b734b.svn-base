package com.andvantech.dsnavi_sitesurvey.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andvantech.dsnavi_sitesurvey.APInfo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class siteSurveyAPIProxy {

	//public static String apiURL = "http://192.168.20.9/api/";
	public static String apiURL;
	public static ArrayList<String> mMapImageList = new ArrayList<String>();
	public static HashMap <String,APInfo> mAPInfoList = new HashMap<String,APInfo>();//ArrayList<APInfo> mAPInfoList = new ArrayList<APInfo>();
	public static ArrayList<APInfo> mSuggestAPList = new ArrayList<APInfo>();
	public static ArrayList<APInfo> mergedExistAPAndSuggestAPList = new ArrayList<APInfo>(); 
	
	public static boolean isSuccessUpdRssi;
	public static String updApPoint(String floor, String androidID, String apSSID, String allSSID, 
			String apBSSID, String apPositionX, String apPositionY, String action)
	{
		Log.i("siteSurveyAPIProxy", "updApPoint");
		
		HttpPost httpReqeust = new HttpPost(apiURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", "4765272503474547"));
		params.add(new BasicNameValuePair("client_secret",
				"niUQ2nYjRu8dBVvNENwELqtouWM3eqKB"));
		params.add(new BasicNameValuePair("version", "1.0"));
		params.add(new BasicNameValuePair("cmd", "android"));
		params.add(new BasicNameValuePair("type", "json"));
		params.add(new BasicNameValuePair("method", "updApPoint"));
		params.add(new BasicNameValuePair("UUID", androidID));
		params.add(new BasicNameValuePair("Floor", floor));
		params.add(new BasicNameValuePair("SSID", apSSID));
		params.add(new BasicNameValuePair("allSSID", allSSID));
		params.add(new BasicNameValuePair("MAC", apBSSID));
		params.add(new BasicNameValuePair("pointX", apPositionX));
		params.add(new BasicNameValuePair("pointY", apPositionY));
		params.add(new BasicNameValuePair("action", action));
		params.add(new BasicNameValuePair("flag", "w"));
		Log.i("siteSurveyAPIProxy", "updApPoint sendData = " + params.toString());
		HttpResponse httpResponse = null;
		String code ="";
		try {

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			
			httpReqeust.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			httpResponse = new DefaultHttpClient(httpParameters)
					.execute(httpReqeust);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				Log.i("updApPoint", "HttpPostResult = " + strResult);
				try {
					JSONObject result = new JSONObject(strResult);
					code = result.getString("code");
					Log.i("code", result.getString("code"));
					Log.i("msg", result.getString("msg"));
					Log.i("data", result.getString("data"));
					

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "UnsupportedEncodingException");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Log.i("siteSurveyAPIProxy", "ClientProtocolException");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "IOException");
		}

		httpReqeust.abort();
		if (httpResponse!=null&&httpResponse.getStatusLine().getStatusCode() == 200) 
		{
			
			if(code.equals("213"))
				{code ="";
				return "AlreadyExist";
				}
			else
				{code ="";
				return "Success";
				}
		}
		else
			{code ="";
			return "Fail";
			}
	}
	
	public static void getApList(String androidID)
	{
		Log.i("siteSurveyAPIProxy", "getApList");
		
		HttpPost httpReqeust = new HttpPost(apiURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", "4765272503474547"));
		params.add(new BasicNameValuePair("client_secret",
				"niUQ2nYjRu8dBVvNENwELqtouWM3eqKB"));
		params.add(new BasicNameValuePair("version", "1.0"));
		params.add(new BasicNameValuePair("cmd", "android"));
		params.add(new BasicNameValuePair("type", "json"));
		params.add(new BasicNameValuePair("method", "getApList"));
		params.add(new BasicNameValuePair("UUID", androidID));
		
		Log.i("siteSurveyAPIProxy", "getApList sendData = " + params.toString());
		HttpResponse httpResponse = null;
		String code ="";
		try {

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			
			httpReqeust.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			httpResponse = new DefaultHttpClient(httpParameters)
					.execute(httpReqeust);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				Log.i("getApList", "HttpPostResult = " + strResult);
				
				try {
					JSONObject result = new JSONObject(strResult);
					code = result.getString("code");
					Log.i("code", result.getString("code"));
					Log.i("msg", result.getString("msg"));
					Log.i("data", result.getString("data"));

					JSONArray resultData = new JSONArray(result.getString("data"));
					mAPInfoList.clear();
					for (int i = 0; i < resultData.length(); i++) {
						APInfo info = new APInfo();
						JSONObject dataContent = resultData.getJSONObject(i);			
						String SSID = dataContent.getString("SSID");
						String BSSID = dataContent.getString("macAddress");
						String pointX = dataContent.getString("pointX");
						String pointY = dataContent.getString("pointY");
						//String action = "";
						String floor = dataContent.getString("floor");//"floor";
						
						info.setSsid(SSID);
						info.setBssid(BSSID);
						info.setPointX(pointX);
						info.setPointY(pointY);
						info.setFloor(floor);
						info.setSource("server");
						String key =  genKey(floor, pointX, pointY);//floor+"pointX:"+pointX+"pointY:"+pointY;
						//String value = mAPInfoList.get(key);
						if(mAPInfoList.get(key)==null)
							mAPInfoList.put(key, info);
						//mAPInfoList.add(info);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "UnsupportedEncodingException");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Log.i("siteSurveyAPIProxy", "ClientProtocolException");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "IOException");
		}

		httpReqeust.abort();
		
		
	}
	
	public static void getApSuggest(String androidID)
	{
		Log.i("siteSurveyAPIProxy", "getApSuggest");
		
		HttpPost httpReqeust = new HttpPost(apiURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", "4765272503474547"));
		params.add(new BasicNameValuePair("client_secret",
				"niUQ2nYjRu8dBVvNENwELqtouWM3eqKB"));
		params.add(new BasicNameValuePair("version", "1.0"));
		params.add(new BasicNameValuePair("cmd", "android"));
		params.add(new BasicNameValuePair("type", "json"));
		params.add(new BasicNameValuePair("method", "getApSuggest"));
		params.add(new BasicNameValuePair("UUID", androidID));
		
		Log.i("siteSurveyAPIProxy", "getApSuggest sendData = " + params.toString());
		HttpResponse httpResponse = null;
		String code ="";
		try {

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			
			httpReqeust.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			httpResponse = new DefaultHttpClient(httpParameters)
					.execute(httpReqeust);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				Log.i("getApSuggest", "HttpPostResult = " + strResult);
				
				try {
					JSONObject result = new JSONObject(strResult);
					code = result.getString("code");
					Log.i("code", result.getString("code"));
					Log.i("msg", result.getString("msg"));
					Log.i("data", result.getString("data"));

					JSONArray resultData = new JSONArray(result.getString("data"));
					mSuggestAPList.clear();
					for (int i = 0; i < resultData.length(); i++) {
						APInfo info = new APInfo();
						JSONObject dataContent = resultData.getJSONObject(i);			
						String pointX = dataContent.getString("pointX");
						String pointY = dataContent.getString("pointY");
						//String action = "";
						String floor = dataContent.getString("floor");//"floor";
						
						
						info.setPointX(pointX);
						info.setPointY(pointY);
						info.setFloor(floor);
						info.setSource("server");
						//String key = genKey(floor, pointX, pointY);//floor+"pointX:"+pointX+"pointY:"+pointY;
						
						mSuggestAPList.add(info);
						//mAPInfoList.add(info);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "UnsupportedEncodingException");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Log.i("siteSurveyAPIProxy", "ClientProtocolException");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "IOException");
		}

		httpReqeust.abort();
		
		
	}
	
	private static String genKey(String floor,String pointX,String pointY)
	{
		int X = (int)(Float.parseFloat(pointX));
		int Y = (int)(Float.parseFloat(pointY));
		
		return floor+"pointX:"+X+"pointY:"+Y;
		//return floor+"pointX:"+pointX+"pointY:"+pointY;
	}
	
	public static String updApRSSI0(String androidID, String apSSID, 
			String apBSSID, String RSSI0)
	{
		Log.i("siteSurveyAPIProxy", "addRssi0");
		
		HttpPost httpReqeust = new HttpPost(apiURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", "4765272503474547"));
		params.add(new BasicNameValuePair("client_secret",
				"niUQ2nYjRu8dBVvNENwELqtouWM3eqKB"));
		params.add(new BasicNameValuePair("version", "1.0"));
		params.add(new BasicNameValuePair("cmd", "android"));
		params.add(new BasicNameValuePair("type", "json"));
		params.add(new BasicNameValuePair("method", "addRssi0"));
		params.add(new BasicNameValuePair("UUID", androidID));
		params.add(new BasicNameValuePair("SSID", apSSID));
		params.add(new BasicNameValuePair("MAC", apBSSID));
		params.add(new BasicNameValuePair("rssi0", RSSI0));
		params.add(new BasicNameValuePair("flag", "w"));

		
		Log.i("siteSurveyAPIProxy", "addRssi0 sendData = " + params.toString());
		HttpResponse httpResponse = null;
		String msg = "";
		try {

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			
			httpReqeust.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			httpResponse = new DefaultHttpClient(httpParameters)
					.execute(httpReqeust);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				Log.i("updApPoint", "HttpPostResult = " + strResult);
				try {
					JSONObject result = new JSONObject(strResult);
					Log.i("code", result.getString("code"));
					Log.i("msg", result.getString("msg"));
					Log.i("data", result.getString("data"));
					msg = result.getString("msg");
					

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "UnsupportedEncodingException");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Log.i("siteSurveyAPIProxy", "ClientProtocolException");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "IOException");
		}

		httpReqeust.abort();
		if(msg.contains("SUCCESS")){
			return "rssi0updSuccess";
		}else{
			return "rssi0updFail";
		}
		
	}

	public static void getMapUrl(String androidID) {
		Log.i("siteSurveyAPIProxy", "getMapUrl");
		// String destinationFile = "image.jpg";
		
		mMapImageList.clear();
		
		HttpPost httpReqeust = new HttpPost(apiURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", "4765272503474547"));
		params.add(new BasicNameValuePair("client_secret",
				"niUQ2nYjRu8dBVvNENwELqtouWM3eqKB"));
		params.add(new BasicNameValuePair("version", "1.0"));
		params.add(new BasicNameValuePair("cmd", "android"));
		params.add(new BasicNameValuePair("type", "json"));
		params.add(new BasicNameValuePair("method", "getFloorList"));
		params.add(new BasicNameValuePair("UUID", androidID));

		Log.i("siteSurveyAPIProxy", "getMapUrl sendData = " + params.toString());

		try {

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			
			httpReqeust.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse httpResponse = new DefaultHttpClient(httpParameters)
					.execute(httpReqeust);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				Log.i("getMapUrl", "HttpPostResult = " + strResult);
				try {
					JSONObject result = new JSONObject(strResult);
					Log.i("code", result.getString("code"));
					Log.i("msg", result.getString("msg"));
					Log.i("data", result.getString("data"));
					
					JSONArray resultData = new JSONArray(result.getString("data"));
					for (int i = 0; i < resultData.length(); i++) {
						JSONObject dataContent = resultData.getJSONObject(i);
						Log.i("siteSurveyAPIProxy", "getMapUrl floor = "+ dataContent.getString("floor"));
						Log.i("siteSurveyAPIProxy", "getMapUrl floor = "+ dataContent.getString("map_image"));
						
						String s = dataContent.getString("floor")
								+ "###"
								+ dataContent.getString("map_image");
						
						mMapImageList.add(s);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "UnsupportedEncodingException");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Log.i("siteSurveyAPIProxy", "ClientProtocolException");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("siteSurveyAPIProxy", "IOException");
			// reconnectToWifi();
			// Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
		}

		httpReqeust.abort();

	}
	
	private static void mergeExistAPAndSuggestAP()
	{
		mergedExistAPAndSuggestAPList.clear();
		mergedExistAPAndSuggestAPList.addAll(mAPInfoList.values());
		/*for(APInfo info :mSuggestAPList)
		{
			String key = genKey(info.getFloor(), info.getPointX(), info.getPointY());
			if(mAPInfoList.get(key)==null)
			{
				mergedExistAPAndSuggestAPList.add(info);
			}
		}*/
	}
	
	public static ArrayList<APInfo> getAPList()
	{
		
		mergeExistAPAndSuggestAP();
		return mergedExistAPAndSuggestAPList;
	}
	
	public static ArrayList<String> getMapList()
	{
		return mMapImageList;
	}

}
