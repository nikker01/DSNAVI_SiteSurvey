package com.andvantech.dsnavi_sitesurvey;

import com.andvantech.dsnavi_sitesurvey.position.position_1F;

import android.app.Activity;

public class GenericDialog {

	Activity mActivity;
	position_1F mAct;
	public GenericDialog(final position_1F activity ,int functioName) {
		this.mAct = activity;
	}
}
