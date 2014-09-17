package com.andvantech.dsnavi_sitesurvey.position;

import java.io.File;

import com.andvantech.dsnavi_sitesurvey.OpenFiles;
import com.andvantech.dsnavi_sitesurvey.R;
import com.andvantech.dsnavi_sitesurvey.proxy.DownloadFileProxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

public class PushMsgFileActivity extends Activity {

	private ProgressDialog pd = null;
	private String fileType = "";
	private String filePath = "";
	private PushMsgVO vo = null;
	private String TAG = "PushMsgFileActivity";
	
	private String webUrl = "http://www.fecityonline.com/BigCity/index.do";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_msg_layout);

		vo = (PushMsgVO) getIntent().getSerializableExtra("ap_msg");
		// if(vo)

		if (vo != null) {
			fileType = vo.fileExt;
			filePath = vo.fileName;
			webUrl = vo.url;
		}
		
		WebView webView = (WebView) findViewById(R.id.webView1);     

        WebSettings websettings = webView.getSettings();  
        websettings.setSupportZoom(true);  
        websettings.setBuiltInZoomControls(true);  
        websettings.setJavaScriptEnabled(true);
        websettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient());  
        websettings.setUseWideViewPort(true);  
        websettings.setLoadWithOverviewMode(true);  
        
        
        Log.i(TAG, "URL = " +webUrl);//"http://m.citypass.tw/member/login_app.php?link="+postData2);
        webView.loadUrl(webUrl);  
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setTitle(getResources().getString(R.string.menu_push_msg));

	}

	public void fileDownloadStatus(boolean isDone) {
		Log.i(TAG, "fileDownloadStatus is done" + isDone);
		if (pd != null) {
			pd.dismiss();
			if (isDone) {
				filePath = "/sdcard/Double_Service/" + filePath;
				agreeToOpenFileDialog();
			}
		}
	}

	private void agreeToOpenFileDialog() {
		// TODO Auto-generated method stub
		Builder MyAlertDialog = new AlertDialog.Builder(this);
		MyAlertDialog.setTitle("success");
		MyAlertDialog.setMessage("open file?");

		MyAlertDialog.setPositiveButton(
				getResources().getString(R.string.dialog_btn_confirm),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// clearMyReservation();
						openFile();
					}
				});

		MyAlertDialog.setCancelable(false);
		MyAlertDialog.show();
	}

	protected void openFile() {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		if(file!=null&&file.isFile())
        {
            String fileName = file.toString();
            Intent intent;
            if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingImage))){
                intent = OpenFiles.getImageFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingWebText))){
                intent = OpenFiles.getHtmlFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingPackage))){
                intent = OpenFiles.getApkFileIntent(file);
                startActivity(intent);

            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingAudio))){
                intent = OpenFiles.getAudioFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingVideo))){
                intent = OpenFiles.getVideoFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingText))){
                intent = OpenFiles.getTextFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingPdf))){
                intent = OpenFiles.getPdfFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingWord))){
                intent = OpenFiles.getWordFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingExcel))){
                intent = OpenFiles.getExcelFileIntent(file);
                startActivity(intent);
            }else if(checkEndsWithInStringArray(fileName, getResources().
                    getStringArray(R.array.fileEndingPPT))){
                intent = OpenFiles.getPPTFileIntent(file);
                startActivity(intent);
            }else
            {
            	Log.i(TAG, "openFile open fail");
            }
        }else
        {
        	Log.i(TAG, "openFile open fail");
        }
	}

	private boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
}
