package com.andvantech.dsnavi_sitesurvey;

import com.andvantech.dsnavi_sitesurvey.position.position_1F;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainSettingView extends Activity{

	private String TAG = "MainSettingView";
	
	private Button loadMap;
	private Button loadAPList;
	private Spinner modeChoice;
	private Button start;
	
	private boolean isMapLoaded = false;
	private String mapUri;
	private int whichMenuItem = 0; //0:ibeacon 1:WiFi AP
	private boolean isListLoaded = false;
	
	private static final int REQ_CODE_OPEN_ALBUN 	= 1000;
	private static final String REQ_CODE_MAP 				= "_REQ_CODE_MAP";
	private static final String REQ_CODE_LIST				= "_REQ_CODE_LIST";
	
	private int spinnerItem = 0;
	private static final String[] mode = new String[]{"MODE_SITESURVEY", "MODE_SET_POINTS"};
	private String[] plistMenu = new String[]{"WifiAPList"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_setting_layout);

		setComponent();
	}

	private void setComponent() {
		// TODO Auto-generated method stub
		String[] arySpinnerItem = {"Site Survey", "Set Points"};
		ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, arySpinnerItem);
		modeChoice = (Spinner)findViewById(R.id.spinner1);
		modeChoice.setAdapter(spinnerAdapter);
		modeChoice.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				spinnerItem = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				spinnerItem = 0;
			}});
		loadMap= (Button)findViewById(R.id.btnLoadMap);
		loadAPList = (Button)findViewById(R.id.btnLoadList);
		start = (Button)findViewById(R.id.btnStart);
		
		loadMap.setOnClickListener(btnClicked);
		loadAPList.setOnClickListener(btnClicked);
		start.setOnClickListener(btnClicked);
	}
	
	private OnClickListener btnClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()) {
			case R.id.btnLoadMap:
				doOpenAlbum();
				break;
			case R.id.btnLoadList:
				doOpenMenu();
				break;
			case R.id.btnStart:
				settingFinish();
				break;
			}
		}
		
	};

	protected void doOpenAlbum() {
		// TODO Auto-generated method stub
		Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);  
		openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
		startActivityForResult(openAlbumIntent, REQ_CODE_OPEN_ALBUN);  
	}
	
	protected void settingFinish() {
		// TODO Auto-generated method stub
		String mToastMsg = "Mode ="+mode[spinnerItem]+ " is Map loaded = " +isMapLoaded + " ,is list loaded = " +isListLoaded;
		
		Toast.makeText(this, mToastMsg, Toast.LENGTH_LONG).show();
		/*
		if(spinnerItem == GlobalDataVO.MODE_SITE_SURVEY) {
			
		} else if(spinnerItem == GlobalDataVO.MODE_SET_POINT) {
			
		}
		*/
	
		if(isMapLoaded && isListLoaded) {
			Intent intent = new Intent();
			intent.putExtra(REQ_CODE_MAP, mapUri);
			intent.putExtra(REQ_CODE_LIST, whichMenuItem);
			intent.setClass(this, position_1F.class);
			this.startActivity(intent);
		}
		
	}

	protected void doOpenMenu() {
		// TODO Auto-generated method stub

		new AlertDialog.Builder(this)
			.setTitle("choose list")
			.setSingleChoiceItems(plistMenu, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						whichMenuItem = which;
					}
				})
		.setCancelable(false)
		.setNegativeButton(
				getResources().getString(R.string.dialog_btn_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						isListLoaded = false;
					}
				})
		.setPositiveButton(
				getResources().getString(R.string.dialog_btn_confirm),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						isListLoaded = true;
					}
				}).show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			switch(requestCode) {
			case REQ_CODE_OPEN_ALBUN:
				Uri uri = data.getData();  
				Log.i(TAG, "onActivityResult REQ_CODE_OPEN_ALBUN Uri = " +uri);
				mapUri = uri.toString();
				isMapLoaded = true;
				break;
			}
		}
	}
}
