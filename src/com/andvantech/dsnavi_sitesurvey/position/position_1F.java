package com.andvantech.dsnavi_sitesurvey.position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.andvantech.dsnavi_sitesurvey.APInfo;
import com.andvantech.dsnavi_sitesurvey.BaseAlertView;
import com.andvantech.dsnavi_sitesurvey.BaseRssiScan;
import com.andvantech.dsnavi_sitesurvey.BayesClassifier;
import com.andvantech.dsnavi_sitesurvey.GlobalDataVO;
import com.andvantech.dsnavi_sitesurvey.PointAccessDBHelper;
import com.andvantech.dsnavi_sitesurvey.PointAccessDataVO;
import com.andvantech.dsnavi_sitesurvey.R;
import com.andvantech.dsnavi_sitesurvey.SwitchLang;
import com.andvantech.dsnavi_sitesurvey.proxy.PointAccessProxy;
import com.andvantech.dsnavi_sitesurvey.proxy.SiteSurveyAccessProxy;
import com.andvantech.dsnavi_sitesurvey.proxy.siteSurveyAPIProxy;
import com.andvantech.dsnavi_sitesurvey.referencepoints.ReferencePointProxy;
import com.andvantech.dsnavi_sitesurvey.referencepoints.ReferencePointVO;
import com.andvantech.dsnavi_sitesurvey.wifireferencepoint.WifiReferencePointProxy;
import com.andvantech.dsnavi_sitesurvey.wifireferencepoint.WifiReferencePointVO;
import com.doubleservice.knntestingmode.TestingModeProxy;
import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class position_1F extends Activity implements SensorEventListener {

	private String TAG = "position_1F";
	private final static int MESSAGE_GET_POSITION = 2000;

	public String saveImgIndex;

	public ArrayList<String> mMapImageList = new ArrayList<String>();
	private ArrayList<APInfo> mAPList = new ArrayList<APInfo>();

	private ImageView imageView;
	private ImageView imageViewPoint;
	private ImageView imageViewLine;
	private ImageView imageViewArrow;
	private FrameLayout framelayout;
	private DisplayMetrics dm;
	private Bitmap bitmap, bitmapPoint;
	private Button btnSaveImg, btnAddPoint, btnEditModeSwitch;
	private Button btnStartSiteSurvey, btnLine;
	private TextView txTop;
	private String currentFloor;

	private ArrayList<ImageView> arrayImageviewPoint;
	private ArrayList<Matrix> arrayMatrixPoint;

	// private ImageButton zoomInButton;
	// private ImageButton zoomOutButton;
	public ImageViewHelper imageViewHelper;
	private Uri imageUri;
	private float scale;
	private Bitmap savedBitmap;
	public static final int editMode = 0;// ??????
	public static final int addPointMode = 1;// ??????
	public static final int drawLineMode = 2;
	public static int nextmode = addPointMode;
	private EditText editText_ip_setting;
	// private DBHelper dbHelper = new DBHelper
	// (this,Environment.getExternalStorageDirectory().getPath()+
	// "/add_AP/dbAPINFO.db", null, 1);
	static final String TABLE = "apinfo";
	private boolean isLoadActionBar = true;
	PointAccessProxy proxy;
	SiteSurveyAccessProxy SiteSurveyProxy;

	// magnetic sensor
	private SensorManager sensorMgr;
	private Sensor sensorOrientation;
	List<Sensor> sensorList;
	private Handler mHandlerTime = new Handler();
	public WifiManager wiFiManager;
	private BroadcastReceiver receiver;
	private BroadcastReceiver wifiReceiver;
	private BroadcastReceiver iReceiver;
	public boolean scanning = false;
	public double rssiMin = 0;
	public double rssiMax = 0;

	public boolean isMoving;
	public float[] mGravity;
	public float mAccel;
	public float mAccelCurrent = 0.0f;
	public float mAccelLast;
	private float currentAzimuth = 0.0f;
	private boolean isStart = false;

	private int index_line = 0;

	// added By Henry
	private ProgressDialog pd;

	private int mPathNum = 0;

	private int[] scannedRssi;
	private String testingModeStartTime;
	private String testingModeEndTime;
	
	private int mEveryStep = 0;
	NavigationSensor navSensor;// = new NavigationSensor(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.position1f);
		

		// ApplicationController.getInstance().onIBeaconServiceStart();

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		imageView = (ImageView) findViewById(R.id.imageView);
		imageViewPoint = (ImageView) findViewById(R.id.imageViewPoint);
		imageViewLine = (ImageView) findViewById(R.id.imageViewLine);
		imageViewArrow = (ImageView) findViewById(R.id.imageview_arrow);
		imageViewArrow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showGetAzimuthDialog();

			}
		});
		framelayout = (FrameLayout) findViewById(R.id.imageView_root);
		// btnSaveImg = (Button)findViewById(R.id.btnSaveImage);
		// btnSaveImg.setOnClickListener(btnSaveImgListener);
		btnAddPoint = (Button) findViewById(R.id.btnAddPoint);
		btnAddPoint.setOnClickListener(btnSaveImgListener);
		btnEditModeSwitch = (Button) findViewById(R.id.btnEditModeSwitch);
		btnEditModeSwitch.setOnClickListener(btnSaveImgListener);
		btnStartSiteSurvey = (Button) findViewById(R.id.btnStartSiteSurvey);
		btnStartSiteSurvey.setOnClickListener(btnSaveImgListener);
		btnLine = (Button) findViewById(R.id.btnLine);
		btnLine.setOnClickListener(btnSaveImgListener);
		arrayImageviewPoint = new ArrayList<ImageView>();
		arrayMatrixPoint = new ArrayList<Matrix>();

		ImageViewHelper.initArrayImageView();

		// apiProgress();
		wiFiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);

		// ///////////////////////////////
		// added by Henry
		// //////////////////////////////
		// initActionBar();
		initMap();
		initBeaconData();
		BaseAlertView alert = new BaseAlertView(this, GlobalDataVO.STEP_SETTING);
	}

	private void initBeaconData() {
		// TODO Auto-generated method stub
		pd = ProgressDialog.show(position_1F.this, "", "handling...");
		new Thread() {
			@Override
			public void run() {

				PListXMLParser parser = new PListXMLParser();
				PListXMLHandler plistHandler = new PListXMLHandler();
				parser.setHandler(plistHandler);

				try {
					parser.parse(getAssets().open("WifiAPList.plist"));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				PList actualPList = ((PListXMLHandler) parser.getHandler())
						.getPlist();
				Dict root = (Dict) actualPList.getRootElement();
				Map<String, PListObject> list = root.getConfigMap();

				for (int i = 0; i < list.keySet().size(); i++) {
					Dict provinceRoot = (Dict) list.get(String.valueOf(i));
					Map<String, PListObject> area = provinceRoot.getConfigMap();

					String areaName = area.keySet().iterator().next();
					Log.i("Plist Parser", "Area Name = " + areaName);
					

					Dict pointsRoot = (Dict) area.get(areaName);
					Map<String, PListObject> points = pointsRoot.getConfigMap();

					for (int j = 0; j < points.keySet().size(); j++) {
						Dict city = (Dict) points.get(String.valueOf(j));
						String pointName = city.getConfigMap().keySet()
								.iterator().next();
						Log.i("Plist Parser", "pointName");
						Array districts = city.getConfigurationArray(pointName);
						WifiReferencePointVO.ApListSize = districts.size();

						for (int k = 0; k < districts.size(); k++) {
							com.longevitysoft.android.xml.plist.domain.String district = (com.longevitysoft.android.xml.plist.domain.String) districts
									.get(k);
							Log.i("Plist Parser",
									"points = " + district.getValue());
							WifiReferencePointVO.aryApList.add(district
									.getValue());
							String strReplace = district.getValue().replace(
									":", "_");
							
							if (k == districts.size() - 1) {
								WifiReferencePointVO.CREATE_TABLE = WifiReferencePointVO.CREATE_TABLE
										+ "AP_" + strReplace + " TEXT "  + ")";
							} else {
								WifiReferencePointVO.CREATE_TABLE = WifiReferencePointVO.CREATE_TABLE
										+ "AP_" + strReplace + " TEXT, " ;
							}
							
						}
				
					}

				}
				handler.sendEmptyMessage(0);
			}

		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			navSensor = new NavigationSensor(position_1F.this);
			WifiReferencePointProxy proxy = new WifiReferencePointProxy(
					position_1F.this);
			// ReferencePointProxy proxy = new
			// ReferencePointProxy(position_1F.this);
		}
	};

	private void checkBluePinScanCompleted() {
		if (!imageViewHelper.allPointScanCompleted())
			this.txTop.setVisibility(View.VISIBLE);
		else
			this.txTop.setVisibility(View.GONE);
	}

	private void changeToEditMode() {
		// this.setTitle("Edit");

		btnAddPoint.setEnabled(false);
		btnAddPoint.setVisibility(View.INVISIBLE);
		btnLine.setEnabled(false);
		btnLine.setVisibility(View.INVISIBLE);
		btnStartSiteSurvey.setEnabled(false);
		btnStartSiteSurvey.setVisibility(View.INVISIBLE);
		ImageViewHelper.imageViewPoint.setVisibility(View.INVISIBLE);
		this.imageViewArrow.setVisibility(View.VISIBLE);
		nextmode = addPointMode;
		ImageViewHelper.operationMode = nextmode;
		btnEditModeSwitch.setText(R.string.operation_mode_addpoint);
		this.checkBluePinScanCompleted();
		this.txTop.setVisibility(View.VISIBLE);
	}

	private void changeToAddPointMode() {
		// this.setTitle("Add");
		btnAddPoint.setEnabled(true);
		btnAddPoint.setVisibility(View.VISIBLE);
		btnLine.setEnabled(false);
		btnLine.setVisibility(View.INVISIBLE);
		btnStartSiteSurvey.setEnabled(false);
		btnStartSiteSurvey.setVisibility(View.INVISIBLE);
		ImageViewHelper.imageViewPoint.setVisibility(View.VISIBLE);
		//this.imageViewArrow.setVisibility(View.GONE);
		nextmode = drawLineMode;
		ImageViewHelper.operationMode = nextmode;
		btnEditModeSwitch.setText(R.string.operation_mode_drawline);
		this.txTop.setVisibility(View.GONE);
	}

	private void changeToDrawLineMode() {
		// this.setTitle("Add");
		btnAddPoint.setEnabled(false);
		btnAddPoint.setVisibility(View.INVISIBLE);
		btnLine.setEnabled(true);
		btnLine.setVisibility(View.VISIBLE);
		btnStartSiteSurvey.setEnabled(true);
		btnStartSiteSurvey.setVisibility(View.VISIBLE);
		ImageViewHelper.imageViewPoint.setVisibility(View.VISIBLE);
		//this.imageViewArrow.setVisibility(View.GONE);
		nextmode = editMode;
		ImageViewHelper.operationMode = nextmode;
		btnEditModeSwitch.setText(R.string.operation_mode_edit);
		this.txTop.setVisibility(View.GONE);
	}

	Button.OnClickListener btnSaveImgListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnAddPoint:
				Log.i(TAG, "btnAddPoint BEGIN");
				ImageViewHelper.isdrawLineMode = false;
				addNewPointImageView();
				// Toast.makeText(position_1F.this,
				// imageViewHelper.currentMapName, Toast.LENGTH_SHORT).show();
				// addPointToBitmap();
				break;
			case R.id.btnLine:
				Log.v("button mode", "line button click");
				if (!ImageViewHelper.isdrawLineMode) {
					ImageViewHelper.isdrawLineMode = true;
					btnLine.setText("drag mode");
					// btnLine.setEnabled(false);
				} else {
					// imageViewHelper.matrixPoint.set(imageViewHelper.s);
					// imageViewHelper.imageViewPoint.setImageMatrix(imageViewHelper.matrixPoint);
					ImageViewHelper.isdrawLineMode = false;
					btnLine.setText("draw line mode");
					// btnLine.setEnabled(true);
				}
				break;
			case R.id.btnStartSiteSurvey:
				Log.i(TAG, "StartSiteSurvey BEGIN");
				mPathNum++;
				index_line = 0;
				ImageViewHelper.isdrawLineMode = false;
				btnLine.setText("draw line mode");
				// ImageViewHelper.pointCenter(true,true);
				imageViewHelper.matrixPoint
						.set(imageViewHelper.firstLinePointMatrix);
				imageViewHelper.imageViewPoint
						.setImageMatrix(imageViewHelper.matrixPoint);
				// mHandlerTime.postDelayed(timerRun,3000);
				showSiteSurveyAlert();
				//scanning = true;
				//startScanning();
				break;

			case R.id.btnEditModeSwitch:
				ImageViewHelper.isdrawLineMode = false;
				btnLine.setText("draw line mode");
				switch (nextmode) {
				case editMode:
					ImageViewHelper.isDrawing = false;
					ImageViewHelper.imageViewLine.invalidate();
					ImageViewHelper.imageViewLine.setImageDrawable(null);
					for (int i = 0; i < ImageViewHelper.arrayImageviewLine
							.size(); i++) {
						ImageViewHelper.arrayImageviewLine.get(i).invalidate();
						ImageViewHelper.arrayImageviewLine.get(i)
								.setImageDrawable(null);
					}
					ImageViewHelper.arrayImageviewLine.clear();
					ImageViewHelper.arrayPxLine.clear();

					Toast.makeText(
							position_1F.this,
							getResources().getString(
									R.string.string_change_to_editmode),
							Toast.LENGTH_SHORT).show();
					changeToEditMode();
					break;
				case addPointMode:
					ImageViewHelper.isDrawing = false;
					ImageViewHelper.imageViewLine.invalidate();
					ImageViewHelper.imageViewLine.setImageDrawable(null);
					for (int i = 0; i < ImageViewHelper.arrayImageviewLine
							.size(); i++) {
						ImageViewHelper.arrayImageviewLine.get(i).invalidate();
						ImageViewHelper.arrayImageviewLine.get(i)
								.setImageDrawable(null);
					}
					ImageViewHelper.arrayImageviewLine.clear();
					ImageViewHelper.arrayPxLine.clear();

					Toast.makeText(
							position_1F.this,
							getResources().getString(
									R.string.string_change_to_addpointmode),
							Toast.LENGTH_SHORT).show();
					changeToAddPointMode();
					break;
				case drawLineMode:
					Toast.makeText(
							position_1F.this,
							getResources().getString(
									R.string.string_change_to_addpointmode),
							Toast.LENGTH_SHORT).show();
					changeToDrawLineMode();
					break;
				}
				// addPointToBitmap();
				break;
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!isLoadActionBar) {
			deletePointByFloor(currentFloor);
		}
		mHandlerTime.removeCallbacks(timerRun);
		releaseSensor();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// setting views by current language
		/*
		 * Locale locale = getResources().getConfiguration().locale; // setting
		 * views by current language if (locale.toString().equals("zh_TW")) {
		 * getMenuInflater().inflate(R.menu.menu_setting_lan3, menu); } else if
		 * (locale.toString().equals("zh_CN")) {
		 * getMenuInflater().inflate(R.menu.menu_setting_lan2, menu); } else {
		 * getMenuInflater().inflate(R.menu.menu_setting_lan1, menu);
		 * 
		 * }
		 */
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_getposition:
			scanningCurrentFingerPrint();
			// int[] rssi = new int[]{-55, -60, -70};
			// setCurrentFingerPrint(rssi);
			break;
		
		case R.id.action_setting_step:
			BaseAlertView alert = new BaseAlertView(this, GlobalDataVO.STEP_SETTING);
			break;
			/*
		case R.id.action_testing_mode:
			startTestingMode();
			break;
			*/
		}

		return true;
	}

	public Bitmap combineImages(Bitmap bitmap, Bitmap s) { // can add a 3rd
															// parameter 'String
															// loc' if you want
															// to save the new
															// image - left some
															// code to do that
															// at the bottom
		float[] arrayPointM = new float[9];
		float[] arrayM = new float[9];
		ImageViewHelper.matrixPoint.getValues(arrayPointM);
		ImageViewHelper.matrix.getValues(arrayM);

		Log.v("scale point ratial", Float.toString(arrayM[0] / arrayPointM[0]));

		Bitmap cs = null;

		cs = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		// cs = Bitmap.createBitmap((int)(bitmap.getWidth()),
		// (int)(bitmap.getHeight()/arrayM[0]), Bitmap.Config.ARGB_8888);

		Log.v("image width", Integer.toString(bitmap.getWidth()));
		Log.v("image width", Integer.toString(bitmap.getHeight()));
		Canvas comboImage = new Canvas(cs);

		comboImage.drawBitmap(bitmap, 0f, 0f, null);
		Log.v("scale", Float.toString(arrayM[0]));
		Log.v("point location X", Float.toString(arrayPointM[2]));
		Log.v("point location Y", Float.toString(arrayPointM[5]));
		comboImage.drawBitmap(s, imageViewHelper.currentX / scale,
				imageViewHelper.currentY / scale, null);

		// this is an extra bit I added, just incase you want to save the new
		// image somewhere and then return the location
		/*
		 * String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";
		 * 
		 * OutputStream os = null; try { os = new FileOutputStream(loc +
		 * tmpImg); cs.compress(CompressFormat.PNG, 100, os); }
		 * catch(IOException e) { Log.e("combineImages",
		 * "problem combining images", e); }
		 */

		return cs;
	}

	private void addNewPointImageView() {
		// Let's create the missing ImageView
		Log.i(TAG, "addNewPointImageView BEGIN");

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT);
		ImageView imageView = new ImageView(this);
		Matrix matrix = new Matrix();
		matrix.reset();
		// Now the layout parameters, these are a little tricky at first

		imageView.setScaleType(ImageView.ScaleType.MATRIX);
		imageView.setImageResource(R.drawable.pin_blue);// .point);

		float[] arrayM2 = new float[9];
		float[] arrayPointM = new float[9];

		imageViewHelper.matrix.getValues(arrayM2);
		imageViewHelper.matrixPoint.getValues(arrayPointM);

		// String pixelX =
		// Float.toString((arrayPointM[2]-arrayM2[2])/ImageViewHelper.PointScaleRatial);
		// String pixelY =
		// Float.toString((arrayPointM[5]-arrayM2[5])/ImageViewHelper.PointScaleRatial);
		float[] pixelXY = this.calNewPointPixel();
		String pixelX = Float.toString(pixelXY[0]);
		String pixelY = Float.toString(pixelXY[1]);

		Log.i("addNewPoint X", pixelX);
		Log.i("addNewPoint Y", pixelY);
		ImageViewHelper.arrayImageviewPoint.add(imageView);
		ImageViewHelper.arrayMatrixPoint.add(matrix);

		framelayout
				.addView(ImageViewHelper.arrayImageviewPoint
						.get(ImageViewHelper.arrayImageviewPoint.size() - 1),
						1, params);

		float[] arrayM = new float[9];
		ImageViewHelper.matrixPoint.getValues(arrayM);

		matrix.setValues(arrayM);

		imageView.setImageMatrix(matrix);

		Log.i(TAG, "addNewPointImageView END");
	}

	private float[] calNewPointPixel() {
		float[] pointXY = new float[2];
		Matrix m = new Matrix();

		Matrix mPoint = new Matrix();

		m.set(this.imageViewHelper.matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		// mPoint.set(imageViewHelper.matrixPoint);
		mPoint.set(this.imageViewHelper.matrixPoint);

		RectF rectPoint = new RectF(0, 0, bitmapPoint.getWidth(),
				bitmapPoint.getHeight());
		mPoint.mapRect(rectPoint);

		float height = rect.height();
		float width = rect.width();
		float ratio = width / bitmap.getWidth();

		// Log.v("matrix",matrix.toString());
		// Log.v("matrixPoint",matrixPoint.toString());
		float pointCenterX = (rectPoint.right - rectPoint.left) / 2;
		float pointCenterY = (rectPoint.bottom - rectPoint.top) / 2;

		float pointX = (rectPoint.left - rect.left + pointCenterX) / ratio;
		float pointY = (rectPoint.top - rect.top + pointCenterY) / ratio;
		// Log.i("Ratio ",Float.toString(ratio));
		// Log.i("Select point pixelX ",Float.toString(pointX));
		// Log.i("Select point pixelY ",Float.toString(pointY));
		pointXY[0] = pointX;
		pointXY[1] = pointY;
		return pointXY;
	}

	private float[] transPointPixelToTrans(float pixelX, float pixelY) {
		float[] transXY = new float[2];
		float[] arrayM = new float[9];
		imageViewHelper.matrix.getValues(arrayM);
		transXY[0] = (arrayM[2] + (pixelX * arrayM[0]));
		transXY[1] = (arrayM[5] + (pixelY * arrayM[4]));

		return transXY;
	};

	private void initImageview(Uri uri) {

		try {
			bitmap = MediaStore.Images.Media.getBitmap(
					this.getContentResolver(), uri);
			Log.v("imageURI", uri.getPath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.dreammall_1f);
		bitmapPoint = BitmapFactory.decodeResource(getResources(),
				R.drawable.pin_red);
		imageView.setImageBitmap(bitmap);
		imageViewPoint.setImageBitmap(bitmapPoint);
		String txString = "<font color='#68D0FE'>";
		txString += getResources().getString(
				R.string.string_tips_complete_bluepin);
		txString += "</font><br><font color='#FFFFFF'>";
		txString += getResources().getString(
				R.string.string_tips_complete_bluepin_content);
		txString += "</font>";
		// String txString =
		// "<font color='#68D0FE'>Completed the scan</font><br><font color='#FFFFFF'>Please tap the blue pin to start scan!</font>";
		CharSequence charSequence = Html.fromHtml(txString);
		txTop = (TextView) findViewById(R.id.top_textview);
		txTop.setText(charSequence);
		// initBundle();

		imageViewHelper = new ImageViewHelper(this, dm, imageView, bitmap,
				imageViewPoint, bitmapPoint, imageViewLine, framelayout);

		// getPointInfo(currentFloor);
		changeToEditMode();

	}

	private void initMap() {
		//File f = new File("/sdcard/Download/office_v3_ReferencePoints.jpg");
		File f = new File("/sdcard/Download/map_advantech.png");
		Uri uri = Uri.fromFile(f);
		if (uri != null) {
			initImageview(uri);
			// getPointInfo(currentFloor);
			initSensor();
		} else {
			Toast.makeText(position_1F.this,
					getResources().getString(R.string.string_no_map),
					Toast.LENGTH_LONG).show();
			// setTitle("無效的檔案路徑 !!");

		}
	}

	private void deletePointByFloor(String floor) {
		this.proxy.deletePointByFloor(floor);
	}

	public void initSensor() {
		sensorMgr = (SensorManager) this.getSystemService(this.SENSOR_SERVICE);
		sensorMgr.registerListener(this,
				sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorMgr.registerListener(this,
				sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		mGravity = new float[3];
		mGravity[0] = 0;
		mGravity[1] = 0;
		mGravity[2] = 0;
	}

	public void releaseSensor() {
		if (sensorMgr != null)
			sensorMgr.unregisterListener(this);
		isMoving = false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ORIENTATION:
			imageViewHelper.azimuth = 0;
			imageViewHelper.azimuth = event.values[0];
			OnSensorChangeArrow(imageViewHelper.azimuth);
			// Log.v("sensor","目前地磁方向：X - " + event.values[0] + ", Y - "+
			// event.values[1] + ", Z - " + event.values[2]);
			break;
		}

		// Log.v("magnetic sensor", "?��??��??��?�? - " + event.values[0] +
		// ", Y - " + event.values[1] + ", Z - " + event.values[2]);
	}

	private void OnSensorChangeArrow(float azimuth) {
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.postRotate(-1 * azimuth, 50, 50);
		imageViewArrow.setImageMatrix(matrix);

	}

	private void showGetAzimuthDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);
		MyAlertDialog.setTitle("DSNAVI-SiteSurvey");
		MyAlertDialog.setMessage("請將手機正對地圖前方，並按下ok");
		// MyAlertDialog.setTitle("About Us");
		// MyAlertDialog.setMessage("Power RTLS 1.0\n\nCopyright © 2013 Advantech Co., Ltd. All rights reserved.");

		MyAlertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.v("DSNAVI-SiteSurvey", "standard azimuth = "
								+ imageViewHelper.azimuth);
						imageViewHelper.standard_azimuth = imageViewHelper.azimuth;
						// add send standard to server
					}
				});
		MyAlertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		MyAlertDialog.setCancelable(false);
		MyAlertDialog.show();
	}

	private final Runnable timerRun = new Runnable() {
		public void run() {
			mHandlerTime.postDelayed(this, 500);

			float[] linePoint = new float[4];
			// index_line = 0;
			linePoint = imageViewHelper.arrayPxLine.get(index_line);
			float lineLength = (float) (Math.sqrt((linePoint[2] - linePoint[0])
					* (linePoint[2] - linePoint[0])
					+ (linePoint[3] - linePoint[1])
					* (linePoint[3] - linePoint[1])));
			float moveX = 5 * (linePoint[2] - linePoint[0]) / lineLength;
			float moveY = 5 * (linePoint[3] - linePoint[1]) / lineLength;
			imageViewHelper.matrixPoint.postTranslate(moveX, moveY);
			imageViewHelper.imageViewPoint
					.setImageMatrix(imageViewHelper.matrixPoint);

			float[] currentPointXY = imageViewHelper
					.calNewPointPixel(imageViewHelper.matrixPoint);

			float movingDist = (float) (Math
					.sqrt((currentPointXY[0] - linePoint[0])
							* (currentPointXY[0] - linePoint[0])
							+ (currentPointXY[1] - linePoint[1])
							* (currentPointXY[1] - linePoint[1])));
			if (movingDist > lineLength) {
				index_line++;
				float[] arrayPointM = new float[9];
				imageViewHelper.matrixPoint.getValues(arrayPointM);

				Matrix mPoint = new Matrix();

				// mPoint.set(imageViewHelper.matrixPoint);
				mPoint.set(imageViewHelper.matrixPoint);
				RectF rectPoint = new RectF(0, 0, bitmapPoint.getWidth(),
						bitmapPoint.getHeight());
				mPoint.mapRect(rectPoint);
				// Log.v("matrix",matrix.toString());
				// Log.v("matrixPoint",matrixPoint.toString());
				float pointCenterX = (rectPoint.right - rectPoint.left) / 2;
				float pointCenterY = (rectPoint.bottom - rectPoint.top) / 2;

				arrayPointM[2] = transPointPixelToTrans(linePoint[2],
						linePoint[3])[0] - pointCenterX;
				arrayPointM[5] = transPointPixelToTrans(linePoint[2],
						linePoint[3])[1] - pointCenterY;
				imageViewHelper.matrixPoint.setValues(arrayPointM);
			}
			if (index_line > imageViewHelper.arrayPxLine.size() - 1) {
				mHandlerTime.removeCallbacks(timerRun);
				unregisterReceiver(receiver);
				receiver = null;
				scanning = false;
			}
		}
	};

	public void startScanning() {

		wifiReceiver = new WifiFingerPrintReceiver(this, 0);
		registerReceiver(wifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wiFiManager.startScan();

	}

	private void scanningCurrentFingerPrint() {
		wifiReceiver = new WifiFingerPrintReceiver(this, 1);
		registerReceiver(wifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wiFiManager.startScan();
	}

	public void setCurrentFingerPrint(int[] rssi) {
		Log.i(TAG, "currentFingerPrint BEGIN");

		String rssiRes = "Current RSSI:";
		for (int i = 0; i < rssi.length; i++) {
			rssiRes = rssiRes + "\nBeacon" + i + " rssi = " + rssi[i];
		}

		Toast.makeText(position_1F.this, rssiRes, Toast.LENGTH_LONG).show();

		if (iReceiver != null)
			unregisterReceiver(iReceiver);

		ArrayList<HashMap> list = new ArrayList<HashMap>();
		final ReferencePointProxy proxy = new ReferencePointProxy(this);
		list = proxy.queryReferencePointDis(rssi);
		// list = proxy.getRegularCoodinate();

		Collections.sort(list, new Comparator<HashMap>() {
			@Override
			public int compare(HashMap lhs, HashMap rhs) {
				// TODO Auto-generated method stub
				return (Integer) rhs.get(ReferencePointVO.DISTANCE) < (Integer) lhs
						.get(ReferencePointVO.DISTANCE) ? 1 : -1;
			}
		});
		
		

		float newPositionX = (Float.parseFloat((String) list.get(0).get(
				ReferencePointVO.POSITION_X))
				+ Float.parseFloat((String) list.get(1).get(
						ReferencePointVO.POSITION_X)) + Float
				.parseFloat((String) list.get(2).get(
						ReferencePointVO.POSITION_X))) / 3;
		float newPositionY = (Float.parseFloat((String) list.get(0).get(
				ReferencePointVO.POSITION_Y))
				+ Float.parseFloat((String) list.get(1).get(
						ReferencePointVO.POSITION_Y)) + Float
				.parseFloat((String) list.get(2).get(
						ReferencePointVO.POSITION_Y))) / 3;

		String res = "The 1st Nearest posX = " + newPositionX + " posY = "
				+ newPositionY;

		// Toast.makeText(position_1F.this, res, Toast.LENGTH_LONG).show();

		float[] arrayPointM = new float[9];
		imageViewHelper.matrixPoint.getValues(arrayPointM);
		float[] pointXYTrans = this.transPointPixelToTrans(newPositionX,
				newPositionY);

		arrayPointM[2] = Float.valueOf(pointXYTrans[0] - bitmapPoint.getWidth()
				* arrayPointM[0] / 2);
		arrayPointM[5] = Float.valueOf(pointXYTrans[1]
				- bitmapPoint.getHeight() * arrayPointM[4] / 2);
		imageViewHelper.matrixPoint.reset();
		imageViewHelper.matrixPoint.setValues(arrayPointM);
		imageViewPoint.setImageMatrix(imageViewHelper.matrixPoint);

		// imageViewHelper.setPinch(newPositionX, newPositionY);

		final ReferencePointVO rpVO = new ReferencePointVO();
		rpVO.mPosX = Float.toString(newPositionX);
		rpVO.mPosY = Float.toString(newPositionY);
		rpVO.beaconArray = rssi;
		/*
		 * rpVO.mBeaconA_rssi = Integer.toString(rssi[0]); rpVO.mBeaconB_rssi =
		 * Integer.toString(rssi[1]); rpVO.mBeaconC_rssi =
		 * Integer.toString(rssi[2]); rpVO.mBeaconD_rssi =
		 * Integer.toString(rssi[3]);
		 */
		proxy.initDB();

		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Tips");
		alertDialog.setMessage("Want to added to fingerprint DB?");
		alertDialog.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						proxy.setReferencePoint(rpVO);
					}
				});
		alertDialog.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});

		alertDialog.setCancelable(false);
		//alertDialog.show();

		// proxy.setReferencePoint(rpVO);

	}

	public void setBeaconData(final int[] rssi) {
		Log.i(TAG, "setBeaconData BEGIN");

		if (iReceiver != null)
			unregisterReceiver(iReceiver);

		float[] getRssiPointXY = new float[2];
		getRssiPointXY = imageViewHelper
				.calNewPointPixel(imageViewHelper.matrixPoint);

		final float posX = getRssiPointXY[0];
		final float posY = getRssiPointXY[1];

		Log.i("setBeaconData", "Current Position pointX: " + getRssiPointXY[0]
				+ ", pointY: " + getRssiPointXY[1]);

		String res = "";
		for (int i = 0; i < rssi.length; i++) {
			res = res + "\nBeacon" + i + " rssi = " + rssi[i];
		}

		/*
		 * String res = "BeaconA rssi = " + rssi[0] + " BeaconB rssi = " +
		 * rssi[1] + " BeaconC rssi =" + rssi[2] + " BeaconD rssi = " + rssi[3];
		 */

		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Tips");
		alertDialog.setMessage(res);
		alertDialog.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						saveReferencePoint(posX, posY, rssi);
					}
				});
		alertDialog.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startScanning();
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();

	}

	protected void saveReferencePoint(float posX, float posY, int[] rssi) {
		// TODO Auto-generated method stub
		ReferencePointVO rpVO = new ReferencePointVO();
		rpVO.mPosX = Float.toString(posX);
		rpVO.mPosY = Float.toString(posY);
		rpVO.beaconArray = rssi;
		/*
		 * rpVO.mBeaconA_rssi = Integer.toString(rssi[0]); rpVO.mBeaconB_rssi =
		 * Integer.toString(rssi[1]); rpVO.mBeaconC_rssi =
		 * Integer.toString(rssi[2]); rpVO.mBeaconD_rssi =
		 * Integer.toString(rssi[3]);
		 */
		ReferencePointProxy proxy = new ReferencePointProxy(this);
		proxy.setReferencePoint(rpVO);

		if (scanning)
			nextStepTip();
	}

	private void nextStepTip() {
		// TODO Auto-generated method stub
		
		BaseAlertView alert = new BaseAlertView(position_1F.this, GlobalDataVO.NOTIFICATION_NEXTSTEP);
		/*
		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Tips");
		alertDialog.setMessage("1 step forward, and click ok");
		alertDialog.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						actionBarProgress(true);
						siteSurveyMoving();
					}
				});
		alertDialog.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();
		*/
	}

	public void siteSurveyMoving() {
		Log.i(TAG, "siteSurveyMoving BEGIN");

		float[] linePoint = new float[4];
		// index_line = 0;
		if (index_line < ImageViewHelper.arrayPxLine.size()) {
			linePoint = ImageViewHelper.arrayPxLine.get(index_line);
			float lineLength = (float) (Math.sqrt((linePoint[2] - linePoint[0])
					* (linePoint[2] - linePoint[0])
					+ (linePoint[3] - linePoint[1])
					* (linePoint[3] - linePoint[1])));

			float moveX = getEveryStep() * (linePoint[2] - linePoint[0]) / lineLength;
			float moveY = getEveryStep() * (linePoint[3] - linePoint[1]) / lineLength;
			ImageViewHelper.matrixPoint.postTranslate(moveX, moveY);
			ImageViewHelper.imageViewPoint
					.setImageMatrix(ImageViewHelper.matrixPoint);

			float[] currentPointXY = imageViewHelper
					.calNewPointPixel(ImageViewHelper.matrixPoint);
			Log.i("siteSurveyMoving", "currentPointXY X = " + currentPointXY[0]
					+ " currentPointXY Y = " + currentPointXY[1]);

			float movingDist = (float) (Math
					.sqrt((currentPointXY[0] - linePoint[0])
							* (currentPointXY[0] - linePoint[0])
							+ (currentPointXY[1] - linePoint[1])
							* (currentPointXY[1] - linePoint[1])));

			if (movingDist > lineLength) {
				Log.i(TAG, "siteSurveyMoving, movingDist > lineLength, dif = " +(movingDist-lineLength));
				index_line++;
				float[] arrayPointM = new float[9];
				ImageViewHelper.matrixPoint.getValues(arrayPointM);
				Matrix mPoint = new Matrix();
				// mPoint.set(imageViewHelper.matrixPoint);
				mPoint.set(ImageViewHelper.matrixPoint);
				RectF rectPoint = new RectF(0, 0, bitmapPoint.getWidth(),
						bitmapPoint.getHeight());
				mPoint.mapRect(rectPoint);
				// Log.v("matrix",matrix.toString());
				// Log.v("matrixPoint",matrixPoint.toString());
				float pointCenterX = (rectPoint.right - rectPoint.left) / 2;
				float pointCenterY = (rectPoint.bottom - rectPoint.top) / 2;

				arrayPointM[2] = transPointPixelToTrans(linePoint[2],
						linePoint[3])[0] - pointCenterX;
				arrayPointM[5] = transPointPixelToTrans(linePoint[2],
						linePoint[3])[1] - pointCenterY;
				ImageViewHelper.matrixPoint.setValues(arrayPointM);
				
				if (index_line >= ImageViewHelper.arrayPxLine.size()) {
					scanning = false;
				}
				
			} else {
				nextStepTip();
				//showSiteSurveyAlert();
			}
			//startScanning();
			
		}

		

	}

	// /////////////////////////////////////////////////////////////
	//
	// Added By Henry
	//
	// //////////////////////////////////////////////////////////////

	public void setSiteSurveyRssiData(final int rssi[], boolean isDone) {
		Log.i(TAG, "setSiteSurveyRssiData BEGIN");

		if(isDone) {
			if (wifiReceiver != null)
				unregisterReceiver(wifiReceiver);
		}
		
		
		float azimuth = navSensor.getOrientationAzimuth();

		float[] getRssiPointXY = new float[2];
		getRssiPointXY = imageViewHelper
				.calNewPointPixel(imageViewHelper.matrixPoint);

		final float posX = getRssiPointXY[0];
		final float posY = getRssiPointXY[1];

		Log.i("setSiteSurveyRssiData", "Current Position pointX: "
				+ getRssiPointXY[0] + ", pointY: " + getRssiPointXY[1] + " Orientation Value =" +azimuth);

		String res = "";
		for (int i = 0; i < rssi.length; i++) {
			res = res + "AP_" + i + " rssi = " + rssi[i] + "\n";
		}
		
		if(isDone)
			Toast.makeText(this, res, Toast.LENGTH_LONG).show();
		
		saveSiteSurveyRssiData(posX, posY, rssi, isDone, azimuth);
	}

	protected void saveSiteSurveyRssiData(float posX, float posY, int[] rssi, boolean isDone, float azimuth) {
		// TODO Auto-generated method stub
		WifiReferencePointVO rpVO = new WifiReferencePointVO();
		rpVO.mPosX = Float.toString(posX);
		rpVO.mPosY = Float.toString(posY);
		rpVO.rssiArray = rssi;
		rpVO.mPathNum = Integer.toString(this.mPathNum);
		rpVO.mAzimuth = Float.toString(azimuth);

		WifiReferencePointProxy proxy = new WifiReferencePointProxy(this);
		proxy.setReferencePoint(rpVO);

		if (scanning) {
			if(isDone) {
				actionBarProgress(false);
				siteSurveyMoving();
				//nextStepTip();
			}
			
		} else if(!scanning) {
			if(isDone) {
				Toast.makeText(this, "finish site-survey", Toast.LENGTH_LONG).show();
				actionBarProgress(false);
			}
		}
				
	}

	public void setCurrentLocation(float posX, float posY, final int rssi[]) {
		// TODO Auto-generated method stub
		Log.i(TAG, "setCurrentLocation BEGIN");

		if (wifiReceiver != null)
			unregisterReceiver(wifiReceiver);

		String strToastRssiMsg = "";
		for (int i = 0; i < rssi.length; i++) {
			strToastRssiMsg = strToastRssiMsg + "RSSI" + i + " = " + rssi[i]
					+ "\n";
		}

		Toast.makeText(position_1F.this, strToastRssiMsg, Toast.LENGTH_LONG)
				.show();

		float[] arrayPointM = new float[9];
		imageViewHelper.matrixPoint.getValues(arrayPointM);
		float[] pointXYTrans = this.transPointPixelToTrans(posX, posY);

		arrayPointM[2] = Float.valueOf(pointXYTrans[0] - bitmapPoint.getWidth()
				* arrayPointM[0] / 2);
		arrayPointM[5] = Float.valueOf(pointXYTrans[1]
				- bitmapPoint.getHeight() * arrayPointM[4] / 2);
		imageViewHelper.matrixPoint.reset();
		imageViewHelper.matrixPoint.setValues(arrayPointM);
		imageViewPoint.setImageMatrix(imageViewHelper.matrixPoint);

		final WifiReferencePointVO rpVO = new WifiReferencePointVO();
		rpVO.mPosX = Float.toString(posX);
		rpVO.mPosY = Float.toString(posY);
		rpVO.rssiArray = rssi;

		final WifiReferencePointProxy wifiProxy = new WifiReferencePointProxy(
				this);
		wifiProxy.initDB();

		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Tips");
		alertDialog.setMessage("Want to added to fingerprint DB?");
		alertDialog.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						wifiProxy.setReferencePoint(rpVO);
					}
				});
		alertDialog.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();

	}
	
	public void setCurrentLocationRssi(int rssi[], final ArrayList<HashMap> scanDataList) {
		Log.i(TAG, "setCurrentLocationRssi BEGIN");
		
		if (wifiReceiver != null)
			unregisterReceiver(wifiReceiver);
		
		new Thread() {
			@Override
			public void run() {
				
				WifiReferencePointProxy proxy = new WifiReferencePointProxy(position_1F.this);
				proxy.initDB();
				ArrayList<HashMap> list = new ArrayList<HashMap>();
				list = proxy.queryReferencePointDis(scanDataList);
				
				Collections.sort(list, new Comparator<HashMap>() {
					@Override
					public int compare(HashMap lhs, HashMap rhs) {
						// TODO Auto-generated method stub
						
						return (Integer) rhs.get(WifiReferencePointVO.DISTANCE) == (Integer) lhs
								.get(WifiReferencePointVO.DISTANCE) ? 0 :
									((Integer) rhs.get(WifiReferencePointVO.DISTANCE) < (Integer) lhs
											.get(WifiReferencePointVO.DISTANCE) ? 1 : -1);
					}
				});
				
				float newPositionX = (
						Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_X)) +
						Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_X)) + 
						Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_X)) 
						) / 3;
				float newPositionY = (
						Float.parseFloat((String) list.get(0).get(WifiReferencePointVO.POSITION_Y)) + 
						Float.parseFloat((String) list.get(1).get(WifiReferencePointVO.POSITION_Y)) + 
						Float.parseFloat((String) list.get(2).get(WifiReferencePointVO.POSITION_Y))
						) / 3;
				
				proxy.initDB();
				float azimuthValue = proxy.getAzimuthValue(newPositionX, newPositionY);
				
				float[] newPositionXY = new float[]{newPositionX, newPositionY};
				
				Message msg = new Message();
				msg.what = MESSAGE_GET_POSITION;
				msg.obj = newPositionXY;

				posHandler.sendMessage(msg);
			}

		}.start();
	}
	
	Handler posHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_GET_POSITION:
				float[] positionXY = (float[])msg.obj;
				String strToastRssiMsg = "";
				strToastRssiMsg = "Location X = " +positionXY[0] + " Y =" +positionXY[1];
				/*
				for (int i = 0; i < scannedRssi.length; i++) {
					strToastRssiMsg = strToastRssiMsg + "RSSI" + i + " = " + scannedRssi[i]
							+ "\n";
				}
				*/
				
				Toast.makeText(position_1F.this, strToastRssiMsg, Toast.LENGTH_LONG)
				.show();
				
				float[] arrayPointM = new float[9];
				imageViewHelper.matrixPoint.getValues(arrayPointM);
				float[] pointXYTrans = transPointPixelToTrans(positionXY[0], positionXY[1]);

				arrayPointM[2] = Float.valueOf(pointXYTrans[0] - bitmapPoint.getWidth()
						* arrayPointM[0] / 2);
				arrayPointM[5] = Float.valueOf(pointXYTrans[1]
						- bitmapPoint.getHeight() * arrayPointM[4] / 2);
				imageViewHelper.matrixPoint.reset();
				imageViewHelper.matrixPoint.setValues(arrayPointM);
				imageViewPoint.setImageMatrix(imageViewHelper.matrixPoint);
				break;
			}
		}
	};
	

	private ProgressDialog pdTestingMode;

	private void startTestingMode() {
		// TODO Auto-generated method stub
		Log.i(TAG, "startTestingMode BEGIN");

		wifiReceiver = new WifiFingerPrintReceiver(this, 2);
		registerReceiver(wifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wiFiManager.startScan();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date();
		testingModeStartTime = sdf.format(dt);
		pdTestingMode = ProgressDialog.show(this, "",
				"Testing mode progressing...");
		// getActionBar().setTitle("Tesing mode...");
	}

	public void finishTestingMode(int posibilityOutOfDis2M,
			int posibilityOutOfDis3M) {
		Log.i(TAG, "finishTestingMode BEGIN");
		pdTestingMode.dismiss();

		if (wifiReceiver != null)
			unregisterReceiver(wifiReceiver);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date();
		testingModeEndTime = sdf.format(dt);

		String res = "BEGIN: " + testingModeStartTime + "\n" + "END: "
				+ testingModeEndTime + "\nout of distance 2M = "
				+ posibilityOutOfDis2M + "\n" + "out of distance 3M = "
				+ posibilityOutOfDis3M;

		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("finish testing mode");
		alertDialog.setMessage(res);
		alertDialog.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();
	}
	
	private void showSiteSurveyAlert() {
		// TODO Auto-generated method stub
		BaseAlertView alert = new BaseAlertView(this, GlobalDataVO.NOTIFICATION_SITESURVEY);
	}
	
	public void beginSiteSurvey() {
		Log.i(TAG, "beginSiteSurvey BEGIN");
		Toast.makeText(this, "begin site-survey on this position", Toast.LENGTH_LONG).show();
		scanning = true;
		startScanning();
		actionBarProgress(true);
	}
	
	public void actionBarProgress(boolean isLoading) {
		setProgressBarIndeterminateVisibility(isLoading);
	}
	
	private int getEveryStep() {
		return mEveryStep;
	}
	
	public void setEveryStep(int settingNumber) {
		mEveryStep = settingNumber;
	}

}
