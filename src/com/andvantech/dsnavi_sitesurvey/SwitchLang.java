package com.andvantech.dsnavi_sitesurvey;

import java.util.Locale;

import com.andvantech.dsnavi_sitesurvey.position.position_1F;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SwitchLang extends Activity {

	private String appLang;
	private Button enusBtn;
	private Button zhtwBtn;
	private final static int settingPage = 0;
	private final static int siteSurveyPage = 1;
	private int intSourcePage;
	private int res = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_lang_view);

		Log.i("SwitchLang", "onCreate");
		initBundle();
		//loadComponent();
		openDialog();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(getResources().getString(R.string.lang_setting));
		//getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2aa8c8")));
	}
	private void initBundle(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		intSourcePage = bundle.getInt("sourcePage");
		
	}
	private void openDialog() {
		// TODO Auto-generated method stub
		Log.i("SwitchLang", "openDialog");
		
		String m[] = { getResources().getString(R.string.btn_english),getResources().getString(R.string.btn_chs),getResources().getString(R.string.btn_cht)};

		
		new AlertDialog.Builder(SwitchLang.this)
				.setTitle(getResources().getString(R.string.lang_setting))
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(m, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								switch (which) {
								case 0:
									//switch lang to chinese
									setLanguageConfigure(which);
									break;
								case 1:
									//switch lang to english
									setLanguageConfigure(which);
									break;
								case 2:
									//switch lang to english
									setLanguageConfigure(which);
									break;
								}

								dialog.dismiss();
							}
						}).setCancelable(false).show();
		
		if(res == 1)
			backToHomePage();
	}

	private void backToHomePage() {
		Intent i = null;
		Log.i("SwitchLang", "backToHomePage ");
		switch (intSourcePage){
		case settingPage:
			i = new Intent();
			i.setClass(SwitchLang.this, BaseRssiScan.class);
			startActivity(i);
			finish();
			break;
		case siteSurveyPage:
			i = new Intent();
			i.setClass(SwitchLang.this, position_1F.class);
			startActivity(i);
			finish();
			break;
		}
	}

	protected void setLanguageConfigure(int langIndex) {
		// TODO Auto-generated method stub
		Log.i("SwitchLang", "setLanguageConfigure: "+langIndex);
		Resources res = getResources();
		Configuration config = res.getConfiguration();
		
		if (langIndex == 0) {
			config.locale = Locale.ENGLISH;
			
		} else if (langIndex == 1){
			config.locale = Locale.SIMPLIFIED_CHINESE;
		}else if (langIndex == 2){
			config.locale = Locale.TRADITIONAL_CHINESE;
		}

		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(config, dm);
		
		backToHomePage();
	}

	


}
