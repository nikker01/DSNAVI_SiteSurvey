package com.andvantech.dsnavi_sitesurvey;

import java.util.Locale;

import com.andvantech.dsnavi_sitesurvey.position.position_1F;
import com.andvantech.dsnavi_sitesurvey.proxy.siteSurveyAPIProxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class BaseRssiScan extends Activity{

	private String TAG = "BaseRssiScan";
	private int screenWidth;
	private ProgressDialog pd;
	private static  ProgressDialog apiPD;
	private String androidID;
	private String apiTransferState="";
	private TextView txTop ;
	private EditText editText_IP;
	private Button btnStart,btnSkip;
	Spinner spinner;
	public WifiManager wiFiManager;
	private BroadcastReceiver receiver;
	public String bestAPName = "";
	public String bestAPMac = "";
	public double rssi0;
	public int scanCount = 0;
	private String IP;
	private boolean isScanedRSSI0;
	String apiTransferStatus;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rssi0_scan_page);
		
		Log.i(TAG, "onCreate");
		setComponent();
		
		checkScanFlow();
		
        
		detectWifi();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setTitle(getResources().getString(R.string.live_feed));
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if(receiver != null)
			unregisterReceiver(receiver);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiver != null)
			unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}
	
	private void detectWifi() {
		// TODO Auto-generated method stub
		wiFiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
		if (!wiFiManager.isWifiEnabled()) {
			Builder MyAlertDialog = new AlertDialog.Builder(this);
			MyAlertDialog.setTitle(getResources().getString(R.string.wifi_alert_title));
			MyAlertDialog.setMessage(getResources().getString(R.string.wifi_alert_msg));
		
			MyAlertDialog.setPositiveButton(this.getResources().getString(R.string.string_scan_result),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							startActivity(intent);
							appFinish();
						}
					});
			MyAlertDialog.setCancelable(false);
			MyAlertDialog.show();
		} 
	}

	protected void appFinish() {
		// TODO Auto-generated method stub
		this.finish();
	}

	private void setComponent() {
		// TODO Auto-generated method stub
		String Tips = this.getResources().getString(R.string.tips);
		String Tips_description = this.getResources().getString(R.string.tips_description);
		
		String txString = "<font color='#68D0FE'>"+Tips+"</font><br><font color='#FFFFFF'>"+Tips_description+"</font>";
		CharSequence charSequence=Html.fromHtml(txString);
		txTop =(TextView) findViewById(R.id.top_textview);
		txTop.setText(charSequence);
		editText_IP = (EditText)findViewById(R.id.editText_IP);
		btnStart = (Button)findViewById(R.id.btnScan);
		btnSkip = (Button)findViewById(R.id.btnSkip);

		
		btnStart.setOnClickListener(click);
		btnSkip.setOnClickListener(click);
		spinner = (Spinner) findViewById(R.id.spinner_scanCount);
		initSpinner();
	}
	
	private OnClickListener click = new OnClickListener() 
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.btnScan:
				checkScanCountInput();
				break;
			case R.id.btnSkip:
				skipScan();
				break;
		}
		}
		
	};
	private void initSpinner(){
		//�إߤ@��ArrayAdapter����A�é�m�U�Կ�檺���e
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[]{"10","15","20","25","30","35","40","45","50"});
        //�]�w�U�Կ�檺�˦�
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //�]�w���سQ���᪺�ʧ@
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
               scanCount = 10+position*5;
            }
            public void onNothingSelected(AdapterView arg0) {
            	scanCount = 10;
            }
        });
	}
	private void skipScan(){
		this.IP = editText_IP.getText().toString();
		setPreference(this.IP,isScanedRSSI0);
		checkScanFlow();
    	Intent intent = new Intent(BaseRssiScan.this, position_1F.class);
    	finish();
    	intent.putExtra("serverIP", IP.trim());
    	startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Locale locale = getResources().getConfiguration().locale;
		// setting views by current language
		if (locale.toString().equals("zh_TW")) {
			getMenuInflater().inflate(R.menu.menu_lang_3, menu);
		}else if (locale.toString().equals("zh_CN")) {
			getMenuInflater().inflate(R.menu.menu_lang_2, menu);
		} else {
			getMenuInflater().inflate(R.menu.menu_lang_1, menu);

		}

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		Bundle bundle = null;
		switch (item.getItemId()) {
		case R.id.btn_lang:
			intent = new Intent(BaseRssiScan.this, SwitchLang.class);
			bundle = new Bundle();
            bundle.putInt("sourcePage", 0);
            //將Bundle物件assign給intent
            intent.putExtras(bundle);
			startActivity(intent);
			finish();
			return true;
		case R.id.btn_aboutUs:
			showAboutUsDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	protected void checkScanCountInput() {
		// TODO Auto-generated method stub
		
		//String s = inputScanCount.getText().toString();
		Log.v("scanCount: ",Integer.toString(scanCount));
		try{
			//scanCount = Integer.parseInt(s);
			pd = ProgressDialog.show(this, "", this.getResources().getString(R.string.progress_scan));
			new Thread() {
				@Override
				public void run() {

					startScanning();
					handler.sendEmptyMessage(0);
				}

			}.start();
			
		} catch (NumberFormatException e) {
			Log.i(TAG, "Input Error");
	    } catch(Exception e)
		{
	    	Log.i(TAG, "Input Error");
		}
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		//	pd.dismiss();
		
//			goToNewsFeedPage();

		}
	};

	public void startScanning() {
		// TODO Auto-generated method stub
		Log.i(TAG, "startScanning BEGIN");
		
		if (receiver == null)
		{
			Log.i(TAG, "register Receiver");
			receiver = new BaseRssiScanReceiver(this);

			registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		}
		wiFiManager.startScan();
	}
	
	public void showScanRes(final String msg)
	{
		pd.dismiss();
		
		IP = editText_IP.getText().toString();
		
		if(receiver != null)
		{
			unregisterReceiver(receiver);
			receiver = null;
		}
		Builder MyAlertDialog = new AlertDialog.Builder(this);
		MyAlertDialog.setTitle(getResources().getString(R.string.scan_complete));
		MyAlertDialog.setMessage(msg);
		
		MyAlertDialog.setPositiveButton(getResources().getString(R.string.scan_complete_confirm),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						checkScanFlow();
						if(!msg.equals("Scan Fail"))
							apiProgress();
					
					}
				});
		MyAlertDialog.setNegativeButton(getResources().getString(R.string.scan_complete_retry),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						checkScanCountInput();
					}
				});
		
		MyAlertDialog.setCancelable(false);
		MyAlertDialog.show();
	}
	private void getDeviceScreenWidth() {
		// TODO Auto-generated method stub
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		screenWidth = outMetrics.widthPixels;
	}

	private void setPreference(String IP, boolean isScaned) {
		// TODO Auto-generated method stub
		Log.v("setPreference","server IP = "+IP);
		SharedPreferences settings = getSharedPreferences("serverIPPreference", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("server_IP", IP);
		editor.putBoolean("isScanRSSI", isScaned);
		editor.commit();
	}
	
    
    private void getPreference() {
		// TODO Auto-generated method stub
    	SharedPreferences IPsettings = getSharedPreferences("serverIPPreference", 0);
    	this.IP = IPsettings.getString("server_IP", "-1");
    	this.isScanedRSSI0 = IPsettings.getBoolean("isScanRSSI", false);
    	Log.v("getPreference", "IP = "+IP+", isScaned: "+Boolean.toString(isScanedRSSI0));
	}
    private void checkScanFlow(){
    	
    	getPreference();
		if(isScanedRSSI0&&!IP.equals("-1")){
			siteSurveyAPIProxy.apiURL = "http://"+IP+"/api/";
			btnSkip.setEnabled(true);
			btnSkip.setBackgroundResource(R.drawable.btn_bg_skip_2);
			editText_IP.setText(IP);
			
		}else{
			
			btnSkip.setEnabled(false);
			btnSkip.setBackgroundResource(R.drawable.btn_bg_skip);
			
		}
    }
    
    protected void apiProgress() {
		// TODO Auto-generated method stub
		apiPD = ProgressDialog.show(this, "", this.getResources().getString(R.string.progress_update));
		new Thread() {
			@Override
			public void run() {
				
				androidID = Secure.getString(getApplication().getContentResolver(),Secure.ANDROID_ID);
				Log.v("UUID = ", androidID);
				//siteSurveyAPIProxy.updApPoint();
				IP = editText_IP.getText().toString();
				siteSurveyAPIProxy.apiURL = "http://"+IP+"/api/";
				apiTransferStatus = siteSurveyAPIProxy.updApRSSI0(androidID, bestAPName, bestAPMac, Double.toString(rssi0));
				Log.v("apiTrans",apiTransferStatus);
				apiHandler.sendEmptyMessage(0);
			}

		}.start();
	}
	
	Handler apiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			apiPD.dismiss();
			if(apiTransferStatus.equals("rssi0updSuccess")){
				isScanedRSSI0 = true;
				setPreference(IP,isScanedRSSI0);
				checkScanFlow();
				if(isScanedRSSI0&&!IP.equals(""))
					skipScan();
			}else{
				isScanedRSSI0 = false;
				setPreference(IP,isScanedRSSI0);
				checkScanFlow();
				showUpdFailDialog();	
			}
				
			
		}
	};
	private void showUpdFailDialog(){
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.getResources().getString(R.string.update_fail_title));
		builder.setMessage(this.getResources().getString(R.string.update_fail_msg));
		builder.setPositiveButton(this.getResources().getString(R.string.dialog_wifi_alert_confirm),  
				new DialogInterface.OnClickListener() {  
					public void onClick(DialogInterface dialog, int whichButton) 
                         {  
                         }
                     });  
 
		
             builder.show();
	}
	private void showAboutUsDialog(){
		Builder MyAlertDialog = new AlertDialog.Builder(this);
		MyAlertDialog.setTitle(getResources().getString(R.string.about_us_dialog_title));
		MyAlertDialog.setMessage(getResources().getString(R.string.about_us_dialog_messgae));
		//MyAlertDialog.setTitle("About Us");
		//MyAlertDialog.setMessage("Power RTLS 1.0\n\nCopyright © 2013 Advantech Co., Ltd. All rights reserved.");
		
		MyAlertDialog.setPositiveButton(getResources().getString(R.string.about_us_dialog_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {

					
					}
				});
		
		MyAlertDialog.setCancelable(false);
		MyAlertDialog.show();
	}
}
