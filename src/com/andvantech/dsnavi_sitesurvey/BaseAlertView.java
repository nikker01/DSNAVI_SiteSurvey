package com.andvantech.dsnavi_sitesurvey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.andvantech.dsnavi_sitesurvey.position.ApplicationController;
import com.andvantech.dsnavi_sitesurvey.position.position_1F;


public class BaseAlertView {
	
	private String TAG = "BaseAlertView";
	Activity mActivity;
	position_1F mActivity_1F;

	public BaseAlertView(position_1F activity, int functionName) {
		this.mActivity_1F = activity;
		
		switch(functionName) {
		case GlobalDataVO.NOTIFICATION_SITESURVEY:
			siteSurveyTip();
			break;
		case GlobalDataVO.NOTIFICATION_NEXTSTEP:
			nextStepTip();
			break;
		case GlobalDataVO.STEP_SETTING:
			normalizeSteps();
			break;
		}
	}
	
	private void normalizeSteps() {
		Log.i(TAG, "normalizeSteps BEGIN");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity_1F);
		LayoutInflater inflater = LayoutInflater.from(mActivity_1F);
		final View view = inflater.inflate(R.layout.step_setting, null);
		
		final EditText inputField = (EditText)view.findViewById(R.id.editText1);
		
		builder.setView(view);
		builder.setTitle("Settings");
		builder.setIcon(R.drawable.icon_setting);
		builder.setNegativeButton(ApplicationController.getStringByResId(R.string.dialog_btn_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		builder.setPositiveButton(ApplicationController.getStringByResId(R.string.dialog_btn_confirm),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int m = Integer.parseInt(inputField.getText().toString());
						mActivity_1F.setEveryStep(m);
					}
				});
		
		final AlertDialog dialog;
		dialog = builder.create();
		dialog.show();
	}

	private void nextStepTip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "nextStepTip BEGIN");
		
		Builder alertDialog = new AlertDialog.Builder(mActivity_1F);
		ApplicationController.getInstance();
		alertDialog.setTitle(ApplicationController.getStringByResId(R.string.dialog_title_tip));
		alertDialog.setMessage(ApplicationController.getStringByResId(R.string.dialog_msg_tip_nextstep));
		alertDialog.setPositiveButton(ApplicationController.getStringByResId(R.string.btn_scan),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mActivity_1F.actionBarProgress(true);
						mActivity_1F.beginSiteSurvey();
						//mActivity_1F.siteSurveyMoving();
					}
				});
		alertDialog.setNegativeButton(ApplicationController.getStringByResId(R.string.btn_skip),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mActivity_1F.siteSurveyMoving();
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	private void siteSurveyTip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "siteSurveyTip BEGIN");
		
		Builder alertDialog = new AlertDialog.Builder(mActivity_1F);
		ApplicationController.getInstance();
		alertDialog.setTitle(ApplicationController.getStringByResId(R.string.dialog_title_tip));
		alertDialog.setMessage(ApplicationController.getStringByResId(R.string.dialog_msg_tip_skip));
		alertDialog.setPositiveButton(ApplicationController.getStringByResId(R.string.btn_scan),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mActivity_1F.beginSiteSurvey();
					}
				});
		alertDialog.setNegativeButton(ApplicationController.getStringByResId(R.string.btn_skip),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mActivity_1F.siteSurveyMoving();
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();
	}
}
