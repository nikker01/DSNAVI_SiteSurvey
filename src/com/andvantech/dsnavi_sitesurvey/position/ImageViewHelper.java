package com.andvantech.dsnavi_sitesurvey.position;

import java.util.ArrayList;
import java.util.List;

import com.andvantech.dsnavi_sitesurvey.APInfo;
import com.andvantech.dsnavi_sitesurvey.GlobalDataVO;
import com.andvantech.dsnavi_sitesurvey.R;
import com.andvantech.dsnavi_sitesurvey.scanAPParser;
import com.andvantech.dsnavi_sitesurvey.position.graph.Node;
import com.andvantech.dsnavi_sitesurvey.proxy.siteSurveyAPIProxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//385,0  ???
//310,20  1
//360,70  2
//330,140 3
//300.180 4
//190,180 5
//120,140 6
//400,30  7
//380,120 8
//300,70  9
//220,140 10

//scale:scalePoint    1:0.4

public class ImageViewHelper {
	private static String TAG = "ApMacScan";
	private static BroadcastReceiver receiver;
	public static WifiManager wiFiManager;
	private static ProgressDialog apiPD;

	public static int scanCount = 5;
	private String androidID;
	private String pointSelectedAction = "";

	private String apiTransferState = "";
	PopupWindow popupWindow;
	private static ProgressDialog pd;
	private ImageView selectedPoint;
	private int scanAPResultSelectIndex;
	private ArrayList<APInfo> sortResult;
	private int selectedPointIndex;
	public static String currentMapName = "";

	public static ArrayList<APInfo> arrayPointAPInfo = new ArrayList<APInfo>();

	public static final int drawLineMode = 0;
	public static final int editMode = 1;// ??????
	public static final int addPointMode = 2;// ??????
	public static int operationMode = addPointMode;

	static Context mContext;
	public static FrameLayout frameLayout;
	public static ImageView imageView;
	public static ImageView imageViewPoint;
	public static ImageView imageViewLine;
	public static Matrix matrix = new Matrix();
	public static Matrix matrixPoint = new Matrix();
	public static ArrayList<ImageView> arrayImageviewPoint;
	public static ArrayList<Matrix> arrayMatrixPoint;
	public static ArrayList<ImageView> arrayImageviewLine;
	public static ArrayList<float[]> arrayPxLine; // {lineP1x,lineP1y,lineP2x,lineP2y}
	public static boolean isDrawing = false;
	public static Matrix savedMatrix = new Matrix();
	public static Matrix savedMatrixPoint = new Matrix();
	public static Matrix savedMatrixFirstLinePoint = new Matrix();
	public static Matrix savedLinePoint = new Matrix();
	public static ArrayList<Matrix> arraySavedMatrixPoint = new ArrayList<Matrix>();
	public static int intPosition;
	public float totalTranslateX;
	public float totalTranslateY;
	private static Bitmap bitmap;
	private static Bitmap bitmapPoint;
	private int page;
	public static float currentPointScale;
	public nodeHelper nodeHelper;
	public drawLineHelper mDrawLine;
	public static float scaleRatial;
	public static float PointScaleRatial;
	public float zoomRatial;
	public static float currentX;
	public static float currentY;
	public static float savedPointX, savedPointY;
	public static float relocatedPointX, relocatedPointY;
	public static float minScaleR;// ???�格捕����蕭?

	public static final int NONE = 0;// ??????
	public static final int DRAG = 1;// ??????
	public static final int ZOOM = 2;// �格捕��???
	public static int mode = NONE;
	public static boolean isdrawLineMode = false;
	public static Matrix firstLinePointMatrix = new Matrix();

	private static boolean isTouch;
	// public static final int imgPointSizeX = 128;
	// public static final int imgPointSizeY = 128;
	public static final int imgMapSizeX = 897;
	public static final int imgMapSizeY = 571;

	public static PointF prev = new PointF();
	public static PointF mid = new PointF();
	public static float dist = 1f;
	public static DisplayMetrics dm;
	// public ImageButton zoomInButton;
	// public ImageButton zoomOutButton;
	public static float deviceScale;
	public static float initScale;
	public static float initPointScale;
	public static float initFriendPointScale;
	public boolean isAuthorize;
	private boolean isResetMoving;

	public int targetPosition;
	// ��蕭?��蕭 Y�����??4��蕭
	// public final float [] pointX =
	// {10,290,240,184,155,305,305,240,165,380,360,345,285,198,310,346,305,267,220,165,278,218,180,142};
	// public final float [] pointY =
	// {16,62,62,62,62,94,124,144,144,144,181,202,202,202,121,139,162,139,131,139,74,74,64,64};
	// public final float [] pointX = {0,255,350,350,411,411};
	// public final float [] pointY = {0,215,215,167,154,78};
	// public final float [] pointX = {0,255,350,411};
	// public final float [] pointY = {0,215,167,78};
	public final float[] pointX = { 0, 324, 280, 203, 203, 203, 203, 203, 255,
			255, 296, 296, 323, 145, 125, 79, 388, 395, 425, 410, 390, 403,
			212, 169, 168, 256, 264, 201, 339 };// APCS
	public final float[] pointY = { 0, 294, 255, 248, 198, 150, 82, 113, 95,
			137, 111, 77, 49, 260, 227, 227, 175, 123, 175, 270, 325, 87, 275,
			225, 171, 188, 117, 137, 237 };// APCS
	// public final float [] pointX =
	// {385,579,673,7617,561,355,224,748,710,561,411,617};
	// public final float [] pointY =
	// {0,37,131,261,336,336,261,56,224,131,261,224};
	public final float[] lineX = { 0, 324, 264, 161, 161, 161, 161, 161, 233,
			233, 286, 286, 323, 82, 56, -10, 412, 423, 462, 441, 412, 433, 212,
			169, 168, 256, 264, 201, 339 };
	public final float[] lineY = { 0, 294, 239, 232, 165, 100, 5, 48, 23, 82,
			43, 0, -42, 247, 202, 202, 130, 58, 130, 260, 335, 10, 275, 225,
			171, 188, 117, 137, 237 };

	public final float desireDPIx = 254;
	public final float desireDPIy = 254;
	public final static float desireDeviceScale = 0.53511703f;

	/*
	 * public static final float point0X = 385; public static final float
	 * point0Y = 0; public static final float point1X = 579; public static final
	 * float point1Y = 37; public static final float point2X = 673; public
	 * static final float point2Y = 131; public static final float point3X =
	 * 617; public static final float point3Y = 261; public static final float
	 * point4X = 561; public static final float point4Y = 336; public static
	 * final float point5X = 355; public static final float point5Y = 336;
	 * public static final float point6X = 224; public static final float
	 * point6Y = 261; public static final float point7X = 748; public static
	 * final float point7Y = 56; public static final float point8X = 710; public
	 * static final float point8Y = 224; public static final float point9X =
	 * 561; public static final float point9Y = 131; public static final float
	 * point10X = 411; public static final float point10Y = 261; public static
	 * final float point11X = 617; public static final float point11Y = 224;
	 */
	// public final String AP1 = "DS-AP1";
	// public final String AP2 = "DS-AP3";
	// public final String AP3 = "DS_RDPRINT";
	// public final String AP1 = "DS_TEST1";//"DS-AP1";
	// public final String AP2 = "DS_TEST2";//"DS-AP3";
	// public final String AP3 = "DS_TEST3";//"DS_RDPRINT";

	// AP SSID
	// public final String AP1 = "DS_TEST3";//"DS-AP1";
	// public final String AP2 = "DS_TEST1";//"DS-AP3";
	// public final String AP3 = "A005";//"DS_RDPRINT";
	// public final String AP4 = "A002";//"DS_RDPRINT";
	// APCS SSID
	public final String AP1 = "DS_APCS_001";// "DS-AP1";
	public final String AP2 = "DS_APCS_002";// "DS-AP3";
	public final String AP3 = "DS-AP1";// "DS_RDPRINT";
	public final String AP4 = "DS_APCS_004";// "DS_RDPRINT";
	public final String AP5 = "DS_APCS_005";// "DS-AP1";
	public final String AP6 = "DS_APCS_006";// "DS-AP3";
	public final String AP7 = "DS_APCS_007";// "DS_RDPRINT";
	public final String AP8 = "DS_APCS_008";// "DS_RDPRINT";
	public final String AP9 = "DS_APCS_009";// "DS-AP1";
	public final String AP10 = "DS_APCS_010";// "DS-AP3";
	public final String AP11 = "DS_APCS_011";// "DS_RDPRINT";
	public final String AP12 = "DS_APCS_012";// "DS_RDPRINT";
	public final String AP13 = "DS_APCS_013";// "DS-AP1";
	public final String AP14 = "DS_APCS_014";// "DS-AP3";
	public final String AP15 = "DS_APCS_015";// "DS_RDPRINT";
	public final String AP16 = "DS_APCS_016";// "DS_RDPRINT";
	public final String AP17 = "DS_APCS_017";// "DS-AP1";
	public final String AP18 = "DS_APCS_018";// "DS-AP3";
	public final String AP19 = "DS_APCS_019";// "DS_RDPRINT";
	public final String AP20 = "DS_APCS_020";// "DS_RDPRINT";
	public final String AP21 = "DS_APCS_021";// "DS-AP1";
	public final String AP22 = "DS_APCS_022";// "DS-AP3";
	public final String AP23 = "DS_APCS_023";// "DS_RDPRINT";
	public final String AP24 = "DS_APCS_024";// "DS_RDPRINT";
	public final String AP25 = "DS_APCS_025";// "DS-AP1";
	public final String AP26 = "DS_APCS_026";// "DS-AP3";
	public final String AP27 = "DS_APCS_027";// "DS_RDPRINT";
	public final String AP28 = "DS_APCS_028";// "DS_RDPRINT";
	public final String AP29 = "DS_APCS_029";// "DS-AP1";
	public final String AP30 = "DS_APCS_030";// "DS-AP3";
	public final String AP31 = "DS_APCS_031";// "DS_RDPRINT";
	public final String AP32 = "DS_APCS_032";// "DS_RDPRINT";

	public float screenDPIx;
	public float screenDPIy;

	public int currentPosition;
	public int lastPosition;
	/** Called when the activity is first created. */
	// magnetic sensor
	private SensorManager sensorMgr;
	private Sensor sensorOrientation;
	List<Sensor> sensorList;
	private float[] mGravity;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;
	public float azimuth;
	public static float standard_azimuth = 185.0f;// 嚙賣虜嚙賡���蕭嚙賢祐嚙賣�嚙踝蕭
	private static final float standard_azimuth_north = 0.0f;
	private static final float standard_azimuth_south = 180.0f;
	private static final float standard_azimuth_east = 90.0f;
	private static final float standard_azimuth_west = 270.0f;
	public TextView textView;

	public boolean isMoving;
	private int movingCount;
	private static final float stepDistance = 4f;
	private int m_nTime = 0;

	// /////////added by Henry
	public static float currentScale = 0;
	public boolean bIsAllowMatrix = true;

	public ImageViewHelper(Context mContext, DisplayMetrics dm,
			ImageView imageView, Bitmap bitmap, ImageView imageViewPoint,
			Bitmap bitmapPoint, ImageView imageViewLine, FrameLayout framelayout) {// ,
																					// ImageButton
																					// zoomInButton,
																					// ImageButton
																					// zoomOutButton){
		this.mContext = mContext;
		this.dm = dm;
		this.imageView = imageView;
		this.imageViewPoint = imageViewPoint;
		this.imageViewLine = imageViewLine;
		this.frameLayout = framelayout;
		// this.zoomInButton = zoomInButton;
		// this.zoomOutButton = zoomOutButton;
		this.bitmap = bitmap;
		this.bitmapPoint = bitmapPoint;
		matrix.reset();

		matrixPoint.reset();
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			framelayout.removeView(arrayImageviewPoint.get(i));
		}
		// initArrayImageView();
		arrayImageviewPoint.clear();
		arrayMatrixPoint.clear();
		this.arrayPointAPInfo.clear();
		// addMultiplePoint(framelayout);

		setImageSize();

		minZoom();
		center();

		imageView.setImageMatrix(matrix);
		imageViewLine.setImageMatrix(matrix);

		imageViewPoint.setImageMatrix(matrixPoint);

		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			arrayImageviewPoint.get(i).setImageMatrix(arrayMatrixPoint.get(i));
		}

		// Display device dpi value of X Y in pixels
		screenDPIx = dm.xdpi;
		screenDPIy = dm.ydpi;
		Log.v("dpiX", Float.toString(screenDPIx));
		Log.v("dpiY", Float.toString(screenDPIy));

		nodeHelper = new nodeHelper();
		nodeHelper.initGuideNode();
		nodeHelper.initVariable();
		isTouch = false;
		isdrawLineMode = false;
		// nodeHelper.findPath2(initNode(17), initNode(14));
		// initWifi();//???wifi???��蕭??????????��蕭???

		// Log.v("image width",
		// Float.toString(convertDpToPixel(385,this.mContext)));
		// Log.v("image Height", Integer.toString(this.bitmap.getHeight()));

		mDrawLine = new drawLineHelper(this.mContext, this.bitmap.getWidth(),
				this.bitmap.getHeight());
		// mDrawLine.clear();
		// drawPath(23,17);

		// drawPath(14,17);//��蕭? drawPath(14,17)��蕭????
		// Log.v("matrix",matrix.toString());
		// Log.v("matrixPoint",matrixPoint.toString());
		// mHandlerTime.postDelayed(timerRun, 1000);
		// pointCenter(true,true);
		// angle test
		Log.v("angle test", Float.toString(getMovingDirection(2, 3)));
		float[] arrayM = new float[9];
		Matrix m = new Matrix();
		m.set(matrix);
		m.getValues(arrayM);

		float[] arrayPointM = new float[9];
		Matrix mPoint = new Matrix();
		mPoint.set(matrixPoint);
		mPoint.getValues(arrayPointM);
		zoomRatial = (arrayPointM[2] - arrayM[2]) / pointX[3];
		Log.v("InnerPosition",
				"X: " + Float.toString(arrayPointM[2] - arrayM[2]) + ", Y: "
						+ Float.toString(arrayPointM[5] - arrayM[5]));
		isResetMoving = false;

		azimuth = 0;

	}

	public boolean allPointScanCompleted() {
		if (this.arrayPointAPInfo.size() == 0)
			return true;

		for (APInfo info : this.arrayPointAPInfo) {
			if (!pointAPInfoComplete(info))// info.getSsid().equals(""))
				return false;
		}

		return true;

	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setZoomIn() {
		minScaleR = Math.min(
				(float) dm.widthPixels / (float) bitmap.getWidth(),
				(float) dm.heightPixels / (float) bitmap.getHeight());
		if (minScaleR < 1.0) {
			matrix.postScale(minScaleR + 1f, minScaleR + 1f);
			matrixPoint.postScale((minScaleR + 1f), (minScaleR + 1f));
			for (int i = 0; i < arrayImageviewPoint.size(); i++) {
				arrayMatrixPoint.get(i).postScale((minScaleR + 1f),
						(minScaleR + 1f));
			}
			currentPointScale = minScaleR + 1f;
		} else {
			matrix.postScale(minScaleR, minScaleR);
			matrixPoint.postScale(minScaleR, minScaleR);
			for (int i = 0; i < arrayImageviewPoint.size(); i++) {
				arrayMatrixPoint.get(i).postScale(minScaleR, minScaleR);
			}
			currentPointScale = minScaleR;
		}
	}

	public void setZoomOut() {
		minScaleR = Math.max(
				(float) dm.widthPixels / (float) bitmap.getWidth(),
				(float) dm.heightPixels / (float) bitmap.getHeight());
		if (minScaleR > 1.0) {
			matrix.postScale((minScaleR - (int) minScaleR),
					(minScaleR - (int) minScaleR));
			matrixPoint.postScale((minScaleR - (int) minScaleR),
					(minScaleR - (int) minScaleR));
			for (int i = 0; i < arrayImageviewPoint.size(); i++) {
				arrayMatrixPoint.get(i).postScale(
						(minScaleR - (int) minScaleR),
						(minScaleR - (int) minScaleR));
			}
			currentPointScale = minScaleR - (int) minScaleR;
		} else {
			matrix.postScale(minScaleR, minScaleR);
			matrixPoint.postScale(minScaleR, minScaleR);
			for (int i = 0; i < arrayImageviewPoint.size(); i++) {
				arrayMatrixPoint.get(i).postScale(minScaleR, minScaleR);
			}
			currentPointScale = minScaleR;
		}
	}

	public static void minZoom() {
		minScaleR = (float) dm.heightPixels / (float) bitmap.getHeight();
		matrix.postScale(minScaleR, minScaleR);
		matrixPoint.postScale(minScaleR / 16f, minScaleR / 16f);
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			arrayMatrixPoint.get(i).postScale(minScaleR / 8f, minScaleR / 8f);
		}
		currentPointScale = minScaleR / 2;
	}

	public static void center() {
		center(true, true);
	}

	// �����??�����?����剖�嚙�
	public static void center(boolean horizontal, boolean vertical) {

		Matrix m = new Matrix();
		Matrix mpoint = new Matrix();

		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		mpoint.set(matrixPoint);
		RectF rectPoint = new RectF(0, 0, bitmapPoint.getWidth(),
				bitmapPoint.getHeight());
		mpoint.mapRect(rectPoint);

		// Log.v("matrix",matrix.toString());
		// Log.v("matrixPoint",matrixPoint.toString());
		float height = rect.height();
		float width = rect.width();

		float heightPoint = rectPoint.height();
		float widthPoint = rectPoint.width();

		float deltaX = 0, deltaY = 0;
		float[] arrayM = new float[9];
		float[] arrayPointM = new float[9];
		m.getValues(arrayM);
		mpoint.getValues(arrayPointM);

		// Log.v("scale",Float.toString(arrayM[0]));
		// Log.v("scalePoint",Float.toString(arrayPointM[0]));
		// Log.v("height", Float.toString(height));
		// Log.v("width", Float.toString(width));
		// Log.v("heightPoint", Float.toString(heightPoint));
		// Log.v("widthPoint", Float.toString(widthPoint));
		deviceScale = width / imgMapSizeX;
		if (vertical) {

			// ???��蕭??������剜�����蕭?�菜�������??
			// �剜���?�������蕭??�����??������������蕭?��陷����蕭???������������蕭?��蕭
			int screenHeight = dm.heightPixels;

			// Log.v("rect.top", Float.toString(rect.top));
			// Log.v("rect.bottom", Float.toString(rect.bottom));
			// Log.v("screenHeight",Float.toString(screenHeight));
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top - 70;// -70;
			} else if (rect.top > 0) {
				deltaY = -(rect.top);// +70);
			} else if (rect.bottom < screenHeight) {
				deltaY = screenHeight - rect.bottom - 166;
			}

		}

		if (horizontal) {
			int screenWidth = dm.widthPixels;
			// Log.v("screenWidth",Float.toString(screenWidth));
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
		matrixPoint.postTranslate(deltaX, deltaY);
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			arrayMatrixPoint.get(i).postTranslate(deltaX, deltaY);
		}
	}

	// ?�����?????
	public static float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	public static void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public void setImageSize() {

		if (bIsAllowMatrix) {
			imageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						savedMatrix.set(matrix);
						savedMatrixPoint.set(matrixPoint);
						savedMatrixFirstLinePoint.set(firstLinePointMatrix);
						arraySavedMatrixPoint.clear();
						for (int i = 0; i < arrayImageviewPoint.size(); i++) {
							Matrix m = new Matrix();
							m.set(arrayMatrixPoint.get(i));
							arraySavedMatrixPoint.add(m);
						}
						prev.set(event.getX(), event.getY());
						mode = DRAG;
						isTouch = true;
						if (isdrawLineMode && !isDrawing) {
							Log.v("touch event", "startdrawing");
							addPoint(event.getX(), event.getY());
							savedLinePoint.set(matrixPoint);
							firstLinePointMatrix.set(matrixPoint);
							// firstLinePointXY =
							// calNewPointPixel(firstLinePointMatrix);
							isDrawing = true;
						} else if (isdrawLineMode && isDrawing) {
							float[] arrayLineM = new float[9];
							float[] arrayPointM = new float[9];
							// float[] arrayFirstPointM = new float[9];
							savedMatrixFirstLinePoint.set(firstLinePointMatrix);
							savedLinePoint.getValues(arrayLineM);
							matrixPoint.getValues(arrayPointM);
							// firstLinePointMatrix.getValues(arrayFirstPointM);
							savedLinePoint.postTranslate(arrayPointM[2]
									- arrayLineM[2], arrayPointM[5]
									- arrayLineM[5]);
							// firstLinePointMatrix.postTranslate(arrayPointM[2]-arrayFirstPointM[2],
							// arrayPointM[5]-arrayFirstPointM[5]);
						}
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						dist = spacing(event);
						// ��蕭??�������蕭?��蕭?10, ��╡��?�������蕭?�怨��������
						// ?������格捕�������
						if (spacing(event) > 10f) {
							savedMatrix.set(matrix);
							savedMatrixPoint.set(matrixPoint);
							savedMatrixFirstLinePoint.set(firstLinePointMatrix);
							arraySavedMatrixPoint.clear();
							for (int i = 0; i < arrayImageviewPoint.size(); i++) {
								Matrix m = new Matrix();
								m.set(arrayMatrixPoint.get(i));
								arraySavedMatrixPoint.add(m);
							}
							midPoint(mid, event);
							mode = ZOOM;
						}
						break;
					case MotionEvent.ACTION_UP:
						switch (operationMode) {

						case addPointMode:
							Log.i(TAG, "addPointMode");
							if (isTouch && !isdrawLineMode) {
								Log.v("touch event", "touch");
								Log.v("eventX",
										Float.toString(convertPixelsToDp(
												event.getX(), mContext)));
								Log.v("eventY",
										Float.toString(convertPixelsToDp(
												event.getY(), mContext)));
								// if(!isDrawing)
								addPoint(event.getX(), event.getY());
								// pointCenter(true,true);
								isTouch = false;
							}
							
							imageView.setImageMatrix(matrix);
							imageViewPoint.setImageMatrix(matrixPoint);
							imageViewLine.setImageMatrix(matrix);
							for (int i = 0; i < arrayImageviewPoint.size(); i++) {
								arrayImageviewPoint.get(i).setImageMatrix(
									arrayMatrixPoint.get(i));
							}
							// imageViewFriendPoint.setImageMatrix(matrixFriendPoint);
							// center();
							break;
						case editMode:
							if (isTouch && !isdrawLineMode)
								clickPoint(event.getX(), event.getY());
							break;
						case drawLineMode:
							if (isdrawLineMode) {
								Log.v("pointer_up", "pointer up");
								addNewLineImageView();
							} else if (isTouch && !isdrawLineMode) {
								// addPoint(event.getX(),event.getY());
								// pointCenter(true,true);
								isTouch = false;
							}

							break;
						}
						break;
					case MotionEvent.ACTION_POINTER_UP:
						if (mode == ZOOM && !isdrawLineMode) {
							float[] arrayM = new float[9];
							float[] arrayPointM = new float[9];

							matrix.getValues(arrayM);
							matrixPoint.getValues(arrayPointM);
							currentScale = arrayM[0];
							GlobalDataVO.CURRENT_SCALE = Float
									.toString(arrayM[0]);
							Log.v("current scale ", Float.toString(arrayM[0]));
							Log.v("current scale point",
									Float.toString(arrayPointM[0]));
							float maxScale = 4f / arrayM[0];
							float minScale = 0.8f / arrayM[0];
							if (maxScale < 2) {
								matrix.postScale(maxScale, maxScale);
								matrixPoint.postScale(maxScale, maxScale);
								savedLinePoint.postScale(maxScale, maxScale);
								firstLinePointMatrix.postScale(maxScale,
										maxScale);
								for (int i = 0; i < arrayImageviewPoint.size(); i++) {
									arrayMatrixPoint.get(i).postScale(maxScale,
											maxScale);
								}
							}
							if (minScale > 2) {
								matrix.postScale(minScale, minScale);
								matrixPoint.postScale(minScale, minScale);
								savedLinePoint.postScale(minScale, minScale);
								firstLinePointMatrix.postScale(minScale,
										minScale);
								for (int i = 0; i < arrayImageviewPoint.size(); i++) {
									arrayMatrixPoint.get(i).postScale(minScale,
											minScale);
								}
							}
							savedLinePoint.set(matrixPoint);
							savedMatrixFirstLinePoint.set(firstLinePointMatrix);
							imageView.setImageMatrix(matrix);
							imageViewLine.setImageMatrix(matrix);
							imageViewPoint.setImageMatrix(matrixPoint);
							for (int i = 0; i < arrayImageviewPoint.size(); i++) {
								arrayImageviewPoint.get(i).setImageMatrix(
										arrayMatrixPoint.get(i));
							}
							for (int i = 0; i < arrayImageviewLine.size(); i++) {
								arrayImageviewLine.get(i)
										.setImageMatrix(matrix);
							}
							// center();
						}
						mode = NONE;
						break;
					case MotionEvent.ACTION_MOVE:
						if (mode == DRAG) {
							if (!isdrawLineMode
									&& getMovingDist(event.getX() - prev.x,
											event.getY() - prev.y) > 30f) {
								isTouch = false;
								matrix.set(savedMatrix);
								matrixPoint.set(savedMatrixPoint);
								firstLinePointMatrix
										.set(savedMatrixFirstLinePoint);
								for (int i = 0; i < arrayImageviewPoint.size(); i++) {
									arrayMatrixPoint.get(i).set(
											arraySavedMatrixPoint.get(i));
								}

								matrix.postTranslate(event.getX() - prev.x,
										event.getY() - prev.y);
								matrixPoint.postTranslate(
										event.getX() - prev.x, event.getY()
												- prev.y);
								firstLinePointMatrix.postTranslate(event.getX()
										- prev.x, event.getY() - prev.y);

								for (int i = 0; i < arrayImageviewPoint.size(); i++) {
									arrayMatrixPoint.get(i).postTranslate(
											event.getX() - prev.x,
											event.getY() - prev.y);
								}
							} else if (isdrawLineMode
									&& getMovingDist(event.getX() - prev.x,
											event.getY() - prev.y) > 30f) {
								//
								isTouch = false;
								addPoint(event.getX(), event.getY());
								// savedMatrixPoint.set(matrixPoint);
								drawLine();

							}
						} else if (mode == ZOOM && !isdrawLineMode) {
							isTouch = false;
							float newDist = spacing(event);// ?嚙踝蕭�踝蕭嚙踝蕭嚙踝蕭嚙踝蕭???嚙踝蕭�瘀蕭嚙�????
							if (newDist > 10f) {
								matrix.set(savedMatrix);
								matrixPoint.set(savedMatrixPoint);
								firstLinePointMatrix
										.set(savedMatrixFirstLinePoint);
								for (int i = 0; i < arrayImageviewPoint.size(); i++) {
									arrayMatrixPoint.get(i).set(
											arraySavedMatrixPoint.get(i));
								}

								float tScale = newDist / dist;
								matrix.postScale(tScale, tScale, mid.x, mid.y);
								matrixPoint.postScale(tScale, tScale, mid.x,
										mid.y);
								firstLinePointMatrix.postScale(tScale, tScale,
										mid.x, mid.y);

								for (int i = 0; i < arrayImageviewPoint.size(); i++) {
									arrayMatrixPoint.get(i).postScale(tScale,
											tScale, mid.x, mid.y);
								}
								/*
								 * float[] arraySavedMatrixPointM = new
								 * float[9]; float[] arrayLineM = new float[9];
								 * float[] reLocateArrayPointM = new float[9];
								 * savedMatrixPoint
								 * .getValues(arraySavedMatrixPointM);
								 * savedLinePoint.getValues(arrayLineM);
								 * matrixPoint.getValues(reLocateArrayPointM);
								 * savedLinePoint
								 * .postTranslate(reLocateArrayPointM[2
								 * ]-arrayLineM[2
								 * ],reLocateArrayPointM[5]-arrayLineM[5]);
								 */
							}
						}
						break;
					}
					imageView.setImageMatrix(matrix);
					imageViewLine.setImageMatrix(matrix);
					imageViewPoint.setImageMatrix(matrixPoint);
					for (int i = 0; i < arrayImageviewPoint.size(); i++) {
						arrayImageviewPoint.get(i).setImageMatrix(
								arrayMatrixPoint.get(i));
					}
					for (int i = 0; i < arrayImageviewLine.size(); i++) {
						arrayImageviewLine.get(i).setImageMatrix(matrix);
					}
					// center();
					return true;
				}

			});
		}
	}
	
	public void setPinch(float x, float y) {
		Log.i(TAG, "setPinch");
		addPoint(x, y);
		imageView.setImageMatrix(matrix);
		imageViewPoint.setImageMatrix(matrixPoint);
		imageViewLine.setImageMatrix(matrix);
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public Node initNode(int intNode) {
		Node node = new Node();
		switch (intNode) {
		case 0:
			node = this.nodeHelper.node0;
			break;
		case 1:
			node = this.nodeHelper.node1;
			break;
		case 2:
			node = this.nodeHelper.node2;
			break;
		case 3:
			node = this.nodeHelper.node3;
			break;
		case 4:
			node = this.nodeHelper.node4;
			break;
		case 5:
			node = this.nodeHelper.node5;
			break;
		case 6:
			node = this.nodeHelper.node6;
			break;
		case 7:
			node = this.nodeHelper.node7;
			break;
		case 8:
			node = this.nodeHelper.node8;
			break;
		case 9:
			node = this.nodeHelper.node9;
			break;
		case 10:
			node = this.nodeHelper.node10;
		case 11:
			node = this.nodeHelper.node11;
			break;
		case 12:
			node = this.nodeHelper.node12;
			break;
		case 13:
			node = this.nodeHelper.node13;
			break;
		case 14:
			node = this.nodeHelper.node14;
			break;
		case 15:
			node = this.nodeHelper.node15;
			break;
		case 16:
			node = this.nodeHelper.node16;
			break;
		case 17:
			node = this.nodeHelper.node17;
			break;
		case 18:
			node = this.nodeHelper.node18;
			break;
		case 19:
			node = this.nodeHelper.node19;
			break;
		case 20:
			node = this.nodeHelper.node20;
			break;
		case 21:
			node = this.nodeHelper.node21;
			break;
		case 22:
			node = this.nodeHelper.node22;
			break;
		case 23:
			node = this.nodeHelper.node23;
			break;
		case 24:
			node = this.nodeHelper.node24;
			break;
		case 25:
			node = this.nodeHelper.node25;
			break;
		case 26:
			node = this.nodeHelper.node26;
			break;
		}
		return node;
	}

	// ?�����???��蕭�������釭蝯脣�嚙��箏�����蕭??�����???��蕭??��釭璆��嚙�
	public void drawPath(int startNode, int endNode) {
		// mDrawLine.clear();
		// imageViewLine.invalidate();
		// mDrawLine = new
		// drawLineHelper(this.mContext,this.bitmap.getWidth(),this.bitmap.getHeight());
		nodeHelper.predictionPath.clear();
		nodeHelper.findPath2(initNode(endNode), initNode(startNode));
		ArrayList<Integer> pathX = new ArrayList<Integer>();
		ArrayList<Integer> pathY = new ArrayList<Integer>();
		// float scaleRatial = deviceScale/desireDeviceScale;
		pathX.add((int) convertDpToPixel(lineX[startNode] + 131, mContext));
		pathY.add((int) convertDpToPixel(lineY[startNode] + 122, mContext));

		for (int i = 0; i < nodeHelper.predictionPath.size(); i++) {
			int tmpNode = Integer.valueOf(nodeHelper.predictionPath.get(i)
					.getID());
			pathX.add((int) convertDpToPixel(lineX[tmpNode] + 131, mContext));
			pathY.add((int) convertDpToPixel(lineY[tmpNode] + 122, mContext));
		}
		// mDrawLine.clear();

	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static float convertPixelsToDp(float px, Context context) {

		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		// Log.v("dp1",Float.toString(dp));
		// Log.v("dp2",Float.toString(px /
		// context.getResources().getDisplayMetrics().density));
		return dp;

	}

	// ��蕭??�������蕭?��蕭
	public void addPoint(float touchX, float touchY) {
		// Bitmap bitmapPoint =
		// BitmapFactory.decodeResource(mContext.getResources(),
		// R.drawable.point);
		imageViewPoint.setImageBitmap(bitmapPoint);
		float[] arrayM = new float[9];
		float[] arrayPointM = new float[9];
		Log.v("decive scale", Float.toString(deviceScale));
		// �輸���matrix�輯��批�嚙���蕭??��陪��atrix?�����X
		// Y������格捕����蕭?��蕭?��陷����蕭?��蕭?��蕭����剖�嚙�???�啣��血�嚙�
		// http://blog.csdn.net/pathuang68/article/details/6991867
		matrix.getValues(arrayM);
		matrixPoint.getValues(arrayPointM);
		Log.v("scale", Float.toString(arrayM[0]));
		Log.v("scalePoint", Float.toString(arrayPointM[0]));
		Log.v("transX", Float.toString(arrayM[2]));
		Log.v("transY", Float.toString(arrayM[5]));
		ArrayList<float[]> arrayMatrixValue = new ArrayList<float[]>();
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			float[] arrayTmp = new float[9];
			arrayMatrixPoint.get(i).getValues(arrayTmp);
			arrayMatrixValue.add(arrayTmp);
		}
		// matrixFriendPoint.reset();
		matrixPoint.reset();
		matrix.reset();
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			arrayMatrixPoint.get(i).reset();
		}
		setImageSize();
		minZoom();
		// center();

		float[] initArrayM = new float[9];
		float[] initArrayPointM = new float[9];

		matrix.getValues(initArrayM);
		matrixPoint.getValues(initArrayPointM);
		ArrayList<float[]> initArrayMatrixValue = new ArrayList<float[]>();
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			float[] arrayTmp = new float[9];
			arrayMatrixPoint.get(i).getValues(arrayTmp);
			initArrayMatrixValue.add(arrayTmp);
		}
		Log.v("initScale", Float.toString(initArrayM[0]));
		Log.v("initPointScale", Float.toString(initArrayPointM[0]));
		initScale = initArrayM[0];
		initPointScale = initArrayPointM[0];
		Log.v("inittransX", Float.toString(initArrayM[2]));
		Log.v("inittransY", Float.toString(initArrayM[5]));
		scaleRatial = deviceScale / desireDeviceScale;

		Matrix m = new Matrix();

		Matrix mPoint = new Matrix();

		m.set(this.matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		// mPoint.set(imageViewHelper.matrixPoint);
		mPoint.set(this.matrixPoint);

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

		matrixPoint
				.postTranslate((touchX - arrayM[2] - pointCenterX)
						* initArrayM[0] / arrayM[0],
						(touchY - arrayM[5] - pointCenterY) * initArrayM[0]
								/ arrayM[0]);
		currentX = (touchX - arrayM[2] - 30);
		currentY = (touchY - arrayM[5] - 30);
		savedPointX = (touchX - arrayM[2] - 30) * initArrayM[0] / arrayM[0];
		savedPointY = (touchY - arrayM[5] - 30) * initArrayM[0] / arrayM[0];
		// Log.v("point location X", Float.toString(currentX));
		// Log.v("point location Y", Float.toString(currentY));

		Log.v("scale ratial",
				Float.toString(arrayPointM[0] / initArrayPointM[0]));
		PointScaleRatial = arrayPointM[0] / initArrayPointM[0];

		Log.v("point location X", Float.toString((touchX * initArrayPointM[0])
				/ PointScaleRatial));
		Log.v("point location Y", Float.toString((touchY * initArrayPointM[4])
				/ PointScaleRatial));

		matrix.postScale(arrayM[0] / initArrayM[0], arrayM[0] / initArrayM[0]);
		matrixPoint.postScale(arrayPointM[0] / initArrayPointM[0],
				arrayPointM[0] / initArrayPointM[0]);

		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			arrayMatrixPoint.get(i)
					.postScale(
							arrayMatrixValue.get(i)[0]
									/ initArrayMatrixValue.get(i)[0],
							arrayMatrixValue.get(i)[0]
									/ initArrayMatrixValue.get(i)[0]);
		}

		float[] reLocateArrayM = new float[9];
		float[] reLocateArrayPointM = new float[9];

		matrix.getValues(reLocateArrayM);
		matrixPoint.getValues(reLocateArrayPointM);

		ArrayList<float[]> reLocateArrayMatrixValue = new ArrayList<float[]>();
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			float[] arrayTmp = new float[9];
			arrayMatrixPoint.get(i).getValues(arrayTmp);
			reLocateArrayMatrixValue.add(arrayTmp);
		}
		/*
		 * Log.v("reLocateScale", Float.toString(reLocateArrayM[0]));
		 * Log.v("reLocatetransX", Float.toString(reLocateArrayM[2]));
		 * Log.v("reLocatetransY", Float.toString(reLocateArrayM[5]));
		 */
		// wifiDemo.center();
		// wifiDemo.imageView.setImageMatrix(wifiDemo.matrix);
		// imageViewPoint.setImageMatrix(matrixPoint);
		// wifiDemo.imageViewLine.setImageMatrix(wifiDemo.matrix);
		relocatedPointX = (arrayM[2] - reLocateArrayM[2]);
		relocatedPointY = (arrayM[5] - reLocateArrayM[5]);
		matrix.postTranslate((arrayM[2] - reLocateArrayM[2]),
				(arrayM[5] - reLocateArrayM[5]));
		matrixPoint.postTranslate((arrayM[2] - reLocateArrayM[2]),
				(arrayM[5] - reLocateArrayM[5]));
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			arrayMatrixPoint
					.get(i)
					.postTranslate(
							(arrayMatrixValue.get(i)[2] - reLocateArrayMatrixValue
									.get(i)[2]),
							(arrayMatrixValue.get(i)[5] - reLocateArrayMatrixValue
									.get(i)[5]));
		}
	}

	public static void pointCenter(boolean vertical, boolean horizontal) {
		Matrix m = new Matrix();
		Matrix mpoint = new Matrix();
		Matrix mfriendpoint = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		mpoint.set(matrixPoint);
		RectF rectPoint = new RectF(0, 0, bitmapPoint.getWidth(),
				bitmapPoint.getHeight());
		m.mapRect(rectPoint);
		// Log.v("matrix",matrix.toString());
		// Log.v("matrixPoint",matrixPoint.toString());
		float height = rect.height();
		float width = rect.width();

		float heightPoint = rectPoint.height();
		float widthPoint = rectPoint.width();

		float deltaX = 0, deltaY = 0;
		float[] arrayM = new float[9];
		float[] arrayPointM = new float[9];
		float[] arrayFriendPointM = new float[9];

		m.getValues(arrayM);
		mpoint.getValues(arrayPointM);
		mfriendpoint.getValues(arrayFriendPointM);
		// Log.v("scale",Float.toString(arrayM[0]));
		// Log.v("scalePoint",Float.toString(arrayPointM[0]));
		Log.v("maptransX", Float.toString(arrayM[2]));
		Log.v("maptransY", Float.toString(arrayM[5]));
		Log.v("maptransPointX", Float.toString(arrayPointM[2]));
		Log.v("maptranspointY", Float.toString(arrayPointM[5]));
		deviceScale = width / imgMapSizeX;
		int screenHeight = dm.heightPixels;
		int screenWidth = dm.widthPixels;
		// Log.v("screenHeight",
		// Float.toString(convertPixelsToDp(screenHeight,mContext)));
		// Log.v("screenWidth",
		// Float.toString(convertPixelsToDp(screenWidth,mContext)));
		Log.v("screenHeight", Integer.toString(screenHeight));
		Log.v("screenWidth", Integer.toString(screenWidth));
		deltaX = 480 - arrayPointM[2];
		deltaY = 800 - arrayPointM[5];

		matrix.postTranslate(deltaX, deltaY);
		matrixPoint.postTranslate(deltaX, deltaY);
		// center();
		imageView.setImageMatrix(matrix);
		imageViewLine.setImageMatrix(matrix);
		imageViewPoint.setImageMatrix(matrixPoint);
		// center();
	}

	private void pointMovingWithoutPath() {
		scaleRatial = deviceScale / desireDeviceScale;
		float moveX = (float) Math.cos(Math
				.toRadians(90 - (azimuth - standard_azimuth))) * stepDistance;
		float moveY = (float) Math.sin(Math
				.toRadians(90 - (azimuth - standard_azimuth))) * stepDistance;
		;
		// Log.v("cos test", Float.toString((float)
		// Math.cos(Math.toRadians(azimuth-standard_azimuth))));
		matrixPoint.postTranslate(moveX * scaleRatial, -(moveY * scaleRatial));

	}

	private void pointMovingthroughPath(int currentPosition, int targetPosition) {
		isResetMoving = false;
		scaleRatial = deviceScale / desireDeviceScale;
		if (targetPosition != -1) {
			float moveX = (float) Math.cos(Math.toRadians(getMovingDirection(
					currentPosition, targetPosition))) * stepDistance;
			float moveY = (float) Math.sin(Math.toRadians(getMovingDirection(
					currentPosition, targetPosition))) * stepDistance;
			;
			// Log.v("cos test", Float.toString((float)
			// Math.cos(Math.toRadians(azimuth-standard_azimuth))));
			float[] arrayM = new float[9];
			Matrix m = new Matrix();
			m.set(matrix);
			m.getValues(arrayM);

			float[] arrayPointM = new float[9];
			Matrix mPoint = new Matrix();
			mPoint.set(matrixPoint);
			mPoint.getValues(arrayPointM);
			if (Math.abs(getMoveToPointAngle(currentPosition, targetPosition)
					- azimuth) < 30) {// ��摩璆��嚙踝蕭嚙踝蕭
				float targetX = pointX[targetPosition] * zoomRatial;
				float targetY = pointY[targetPosition] * zoomRatial;
				// Log.v("InnerPosition", "moveX: "+ Float.toString(moveX)+
				// ", moveY: "+Float.toString(moveY));
				// Log.v("InnerPosition", "targetX: "+ Float.toString(targetX)+
				// ", targetY: "+Float.toString(targetY));
				if (Math.abs(targetX - (arrayPointM[2] - arrayM[2])) > 10
						|| Math.abs(targetY - (arrayPointM[5] - arrayM[5])) > 10)
					matrixPoint.postTranslate(moveX * scaleRatial, moveY
							* scaleRatial);
			} else if (Math.abs(getMoveToPointAngle(targetPosition,
					currentPosition) - azimuth) < 30) {// ��摩璆��嚙踝蕭嚙踝蕭
				float targetX = pointX[currentPosition] * zoomRatial;
				float targetY = pointY[currentPosition] * zoomRatial;
				// Log.v("InnerPosition", "moveX: "+ Float.toString(moveX)+
				// ", moveY: "+Float.toString(moveY));
				// Log.v("InnerPosition", "targetX: "+ Float.toString(targetX)+
				// ", targetY: "+Float.toString(targetY));
				if (Math.abs(targetX - (arrayPointM[2] - arrayM[2])) > 10
						|| Math.abs(targetY - (arrayPointM[5] - arrayM[5])) > 10) {
					// movePoint(currentPosition);
					// pointMovingthroughPath(currentPosition,setTargetPoint(currentPosition));
					matrixPoint.postTranslate(-1 * moveX * scaleRatial, -1
							* moveY * scaleRatial);
				} else if (Math.abs(targetX - (arrayPointM[2] - arrayM[2])) < 10
						&& Math.abs(targetY - (arrayPointM[5] - arrayM[5])) < 10) {
					// movePoint(currentPosition);
					// pointMovingthroughPath(currentPosition,setTargetPoint(currentPosition));
					isResetMoving = true;
				}
			}
		}
	}

	private void pointMovingWithPath() {
		scaleRatial = deviceScale / desireDeviceScale;
		if (azimuth > 330)
			azimuth -= 360;
		// private static final float standard_azimuth_north = 190.0f;
		// private static final float standard_azimuth_south = 10.0f;
		// private static final float standard_azimuth_east = 280.0f;
		// private static final float standard_azimuth_west = 100.0f;
		// if(totalTranslateY<Math.abs(pointY[currentPosition]-pointY[lastPosition])){

		if (azimuth < (standard_azimuth_north + 30) % 360
				&& azimuth > (standard_azimuth_north - 30) % 360) {// 160<azimuth<220
			totalTranslateY -= stepDistance * scaleRatial;
			matrixPoint.postTranslate(0, -(stepDistance * scaleRatial));
		}
		if (azimuth < (standard_azimuth_south + 30)
				&& azimuth > (standard_azimuth_south - 30)) {// -30<azimuth<50
			totalTranslateY += stepDistance * scaleRatial;
			matrixPoint.postTranslate(0, stepDistance * scaleRatial);
		}
		// }
		// if(totalTranslateX<Math.abs(pointX[currentPosition]-pointX[lastPosition])){
		if (azimuth < (standard_azimuth_east + 30) % 360
				&& azimuth > (standard_azimuth_east - 30) % 360) {// 250<azimuth<310
			totalTranslateY += stepDistance * scaleRatial;
			matrixPoint.postTranslate(stepDistance * scaleRatial, 0);
		}
		if (azimuth < (standard_azimuth_west + 30) % 360
				&& azimuth > (standard_azimuth_west - 30) % 360) {// 70<azimuth<130
			totalTranslateY -= stepDistance * scaleRatial;
			matrixPoint.postTranslate(-(stepDistance * scaleRatial), 0);
		}
		// }
		imageViewPoint.setImageMatrix(matrixPoint);
	}

	private float getProcessedAzimuth(float azimuth) {
		float processedAzimuth = 0;
		if (azimuth < 0) {
			processedAzimuth = azimuth + 360;
		} else {
			processedAzimuth = azimuth % 360;
		}

		return processedAzimuth;
	}

	private int setTargetPoint(int currentPosition) {
		ArrayList<Node> adjNodes = new ArrayList<Node>();
		adjNodes = nodeHelper.g.adjacentNodes(initNode(currentPosition));
		int point = -1;
		if (targetPosition == -1) {
			if (azimuth > 330)
				azimuth -= 360;
			for (int i = 0; i < adjNodes.size(); i++) {
				Node tmpNode = adjNodes.get(i);
				float tmpAngle = getMoveToPointAngle(currentPosition,
						Integer.valueOf(tmpNode.getID()));
				if (Math.abs(tmpAngle - azimuth) < 30) {
					point = Integer.valueOf(Integer.valueOf(tmpNode.getID()));
				}
			}
			Log.v("innerPosition", "Target point = " + Integer.toString(point));
			targetPosition = point;
		} else
			point = targetPosition;
		return point;
	}

	private float getMovingDirection(int currentPosition, int targetPosition) {
		float angle = -1;
		float x = pointX[targetPosition] - pointX[currentPosition];
		float y = pointY[targetPosition] - pointY[currentPosition];
		double z = Math.sqrt(x * x + y * y);
		if (x < 0 && y > 0)
			angle = 180 - Math
					.round((float) (Math.asin(y / z) / Math.PI * 180));
		else if (x < 0 && y < 0)
			angle = -180
					- Math.round((float) (Math.asin(y / z) / Math.PI * 180));
		else
			angle = Math.round((float) (Math.asin(y / z) / Math.PI * 180));
		// Log.v("innerPosition", Float.toString(angle));
		return angle;
	}

	private float getMoveToPointAngle(int currentPosition, int targetPosition) {
		float angle = -1;
		float x = pointX[targetPosition] - pointX[currentPosition];
		float y = -1 * (pointY[targetPosition] - pointY[currentPosition]);
		float z = (float) Math.sqrt(x * x + y * y);
		if (x < 0 && y > 0)
			angle = Math.round((float) (Math.asin(y / z) / Math.PI * 180)) + 180;
		else if (x < 0 && y < 0)
			angle = 180 - Math
					.round((float) (Math.asin(y / z) / Math.PI * 180));
		else
			angle = Math.round((float) (Math.asin(y / z) / Math.PI * 180));

		angle = -1 * angle + 90 + standard_azimuth;
		if (angle < 0)
			angle = (angle + 360) % 360;
		else
			angle = angle % 360;
		// Log.v("InnerPosition", "angle: "+angle);
		return angle;
	}

	private void movePoint(int point) {
		float[] arrayM = new float[9];
		float[] arrayPointM = new float[9];
		// float[] arrayFriendPointM = new float[9];
		Log.v("decive scale", Float.toString(deviceScale));
		// ���隡��matrix����剝�w����剖��Ｗ����鞈凋葉matrix�����楊X
		// Y��悌����剖�蝤��頦����玨嚗��頦������剖�蝺砍����頦������耍���敶Ｚ��剖�頦���適���蝝啗�蝛輯�
		// http://blog.csdn.net/pathuang68/article/details/6991867
		ImageViewHelper.intPosition = point;
		matrix.getValues(arrayM);
		matrixPoint.getValues(arrayPointM);
		// wifiDemo.matrixFriendPoint.getValues(arrayPointM);
		Log.v("scale", Float.toString(arrayM[0]));
		Log.v("transX", Float.toString(arrayM[2]));
		Log.v("transY", Float.toString(arrayM[5]));
		matrixPoint.reset();
		matrix.reset();
		// wifiDemo.matrixFriendPoint.reset();
		setImageSize();
		minZoom();
		center();

		float dpiRatialX = screenDPIx / desireDPIx;
		float dpiRatialY = screenDPIy / desireDPIy;

		scaleRatial = deviceScale / desireDeviceScale;

		float[] initArrayM = new float[9];
		float[] initArrayPointM = new float[9];
		// float[] initArrayFriendPointM = new float[9];
		matrix.getValues(initArrayM);
		matrixPoint.getValues(initArrayPointM);
		// wifiDemo.matrixFriendPoint.getValues(initArrayFriendPointM);
		Log.v("initScale", Float.toString(initArrayM[0]));
		Log.v("inittransX", Float.toString(initArrayM[2]));
		Log.v("inittransY", Float.toString(initArrayM[5]));
		matrixPoint.postTranslate(pointX[point] * scaleRatial, pointY[point]
				* scaleRatial);

		matrix.postScale(arrayM[0] / initArrayM[0], arrayM[0] / initArrayM[0]);
		matrixPoint.postScale(arrayPointM[0] / initArrayPointM[0],
				arrayPointM[0] / initArrayPointM[0]);
		// wifiDemo.matrixFriendPoint.postScale(arrayFriendPointM[0]/initArrayFriendPointM[0],
		// arrayFriendPointM[0]/initArrayFriendPointM[0]);

		float[] reLocateArrayM = new float[9];
		float[] reLocateArrayPointM = new float[9];
		// float[] reLocateArrayFriendPointM = new float[9];
		matrix.getValues(reLocateArrayM);
		matrixPoint.getValues(reLocateArrayPointM);
		// wifiDemo.matrixPoint.getValues(reLocateArrayPointM);
		// wifiDemo.matrixFriendPoint.getValues(reLocateArrayFriendPointM);
		/*
		 * Log.v("reLocateScale", Float.toString(reLocateArrayM[0]));
		 * Log.v("reLocatetransX", Float.toString(reLocateArrayM[2]));
		 * Log.v("reLocatetransY", Float.toString(reLocateArrayM[5]));
		 */
		// wifiDemo.center();
		// wifiDemo.imageView.setImageMatrix(wifiDemo.matrix);
		// imageViewPoint.setImageMatrix(matrixPoint);
		// wifiDemo.imageViewLine.setImageMatrix(wifiDemo.matrix);

		ImageViewHelper.matrix.postTranslate((arrayM[2] - reLocateArrayM[2]),
				(arrayM[5] - reLocateArrayM[5]));
		ImageViewHelper.matrixPoint.postTranslate(
				(arrayM[2] - reLocateArrayM[2]),
				(arrayM[5] - reLocateArrayM[5]));
		// updMemberLocation();

	}

	private void addMultiplePoint(FrameLayout framelayout) {
		// Let's create the missing ImageView
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT);
		Matrix m = new Matrix();
		arrayImageviewPoint = new ArrayList<ImageView>();
		arrayMatrixPoint = new ArrayList<Matrix>();
		for (int i = 0; i < pointX.length; i++) {
			ImageView imageView = new ImageView(mContext);

			Log.v("initial scaleRatial", Float.toString(scaleRatial));
			Matrix matrix = new Matrix();
			matrix.reset();
			// Now the layout parameters, these are a little tricky at first

			imageView.setScaleType(ImageView.ScaleType.MATRIX);
			imageView.setImageResource(R.drawable.point);
			// imageView.setImageMatrix(matrix);
			// Let's get the root layout and add our ImageView
			arrayImageviewPoint.add(imageView);
			arrayMatrixPoint.add(matrix);
			framelayout.addView(arrayImageviewPoint.get(i), 1, params);

		}
	}

	static void initArrayImageView() {
		arrayImageviewLine = new ArrayList<ImageView>();
		arrayImageviewPoint = new ArrayList<ImageView>();
		arrayMatrixPoint = new ArrayList<Matrix>();
		arrayPxLine = new ArrayList<float[]>();

	}

	static void initAllImageView() {
		/*
		 * matrix.reset(); matrixPoint.reset(); setImageSize();
		 * 
		 * minZoom(); center();
		 * 
		 * imageView.setImageMatrix(matrix); //initPointPosition();
		 * imageViewPoint.setImageMatrix(matrixPoint);
		 */
		for (int i = 0; i < arrayImageviewPoint.size(); i++) {
			arrayImageviewPoint.get(i).setImageMatrix(arrayMatrixPoint.get(i));
		}
	}

	private void clickPoint(float touchX, float touchY) {
		float[] arrayM = new float[9];

		matrix.getValues(arrayM);
		float[] arrayPointM = new float[9];
		matrixPoint.getValues(arrayPointM);

		currentX = (touchX * arrayPointM[0]) / PointScaleRatial;
		currentY = (touchY * arrayPointM[0]) / PointScaleRatial;// *(arrayM[4])+bitmapPoint.getHeight()*(arrayM[4]));
		Log.i("bitmapPoint.getWidth()", "" + arrayM[0]);// bitmapPoint.getWidth());
		Log.i("bitmapPoint.getHeight()", "" + arrayM[4]);// bitmapPoint.getHeight());;

		// Log.i("click location X", Float.toString(touchX));
		// Log.i("click location Y", Float.toString(touchY));
		// Log.i("point location X", Float.toString(currentX));
		// Log.i("point location Y", Float.toString(currentY));;

		for (int index = 0; index < arrayMatrixPoint.size(); index++) {
			RectF rectPoint = new RectF(0, 0, bitmapPoint.getWidth(),
					bitmapPoint.getHeight());
			Matrix mpoint = new Matrix();
			mpoint.set(arrayMatrixPoint.get(index));
			mpoint.mapRect(rectPoint);
			// Log.i("point left - right",
			// Float.toString(rectPoint.left)+"-"+Float.toString(rectPoint.right));
			// Log.i("point top - bottom",
			// Float.toString(rectPoint.top)+"-"+Float.toString(rectPoint.bottom));

			if (rectPoint.left < touchX && touchX < rectPoint.right
					&& rectPoint.bottom > touchY && touchY > rectPoint.top) {
				selectedPointIndex = index;
				createPopUpWindows(rectPoint, arrayImageviewPoint.get(index));
				// ===================================================================
				/*
				 * float[] xy = calSelectedPointPixel(); float[] transxy =
				 * transPointPixelToTrans
				 * (xy[0],xy[1]);//calSelectedPointPixel();
				 * Log.i("trans X pixel", Float.toString(transxy[0]));
				 * Log.i("trans Y pixel", Float.toString(transxy[1]));
				 * 
				 * float[] xy2 = getSelectedPointTrans();
				 * Log.i("calculate New Pojnt X pixel", Float.toString(xy2[0]));
				 * Log.i("calculate New Pojnt Y pixel", Float.toString(xy2[1]));
				 */
			}
		}
	}

	/*
	 * private float[] transPointPixelToTrans(float pixelX,float pixelY) {
	 * float[] transXY = new float[2]; float[] arrayM = new float[9];
	 * this.matrix.getValues(arrayM); transXY[0] =
	 * (arrayM[2]+(pixelX*arrayM[0])); transXY[1] =
	 * (arrayM[5]+(pixelY*arrayM[4]));
	 * 
	 * return transXY; }
	 * 
	 * private float[] getSelectedPointTrans() { float[] xy = new float[2];
	 * float[] arrayPointM = new float[9]; Matrix selectedPointMatrix =
	 * this.arrayMatrixPoint.get(selectedPointIndex);
	 * selectedPointMatrix.getValues(arrayPointM); xy[0] =
	 * (arrayPointM[2]);//-arrayM2
	 * [2])/arrayM2[0]+mapXY[0];//this.getRatio();//;///PointScaleRatial; xy[1]
	 * =
	 * (arrayPointM[5]);//-arrayM2[5])/arrayM2[4]+mapXY[1];//this.getRatio();//
	 * ;///PointScaleRatial; return xy; }
	 */

	static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			// Toast.makeText(SyncData.this, , Toast.LENGTH_LONG);
		}
	};

	/*
	 * private static void showProgressDialog() { pd =
	 * ProgressDialog.show(mContext, "", ""); new Thread() {
	 * 
	 * @Override public void run() { try { sleep(5000); } catch
	 * (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * handler.sendEmptyMessage(0); }
	 * 
	 * }.start(); }
	 */

	private void startScanAP() {
		if (checkWifiConnect()) {
			ScanResults.clearArrayList();
			scanningProgress();
		} else
			this.showDialog(
					this.mContext.getResources().getString(
							R.string.string_error),
					this.mContext.getResources().getString(
							R.string.string_scan_error));
		// Toast.makeText(mContext, "Scan Error,Please Check Your Wifi State",
		// Toast.LENGTH_LONG).show();

		// Toast.makeText(mContext, "Cancel!", Toast.LENGTH_SHORT).show();
		// startScanAP();
		// ScanResults.getApInfo();
		// ArrayList<String> apinfo = ScanResults.getApInfo();//new
		// ArrayList<String>();
		/*
		 * apinfo.add("AFFSDFS#A1:B2:C3:D4:E5:F6#-50;");
		 * apinfo.add("G3#A1:B2:C3:D4:E5:F6#-66;");
		 * apinfo.add("G5#A1:B2:C3:D4:E5:F6#-59;");
		 * apinfo.add("G6#A1:B2:C3:D4:E5:F6#-32;");
		 * apinfo.add("G9#A1:B2:C3:D4:E5:F6#-79;");
		 * apinfo.add("dffgtgrtr#A1:B2:C3:D4:E5:F6#-67;");
		 * apinfo.add("sfgfiotpjgoiehp#A1:B2:C3:D4:E5:F6#-90;");
		 * apinfo.add("sfgfiotpjgoiehp#A1:B2:C3:D4:E5:F6#-90;");
		 * apinfo.add("sfgfiotpjgoiehp#A1:B2:C3:D4:E5:F6#-90;");
		 * apinfo.add("sfgfiotpjgoiehp#A1:B2:C3:D4:E5:F6#-90;");
		 * apinfo.add("sfgfiotpjgoiehp#A1:B2:C3:D4:E5:F6#-90;");
		 */
		// scanAPParser parser = new scanAPParser(apinfo);
		// sortResult = parser.getAPInfoListBySort();
		// showScanAPResult();
		// }
		// else
		// Toast.makeText(mContext,
		// "Update Error \nplease check your Wifi state",
		// Toast.LENGTH_LONG).show();

	}

	public void showScanResult(ArrayList<String> apInfoList) {
		// ScanResults.getApInfo();
		handler.sendEmptyMessage(0);
		if (receiver != null) {
			mContext.unregisterReceiver(receiver);
			receiver = null;
		}
		ArrayList<String> apinfo = apInfoList;
		scanAPParser parser = new scanAPParser(apinfo);
		sortResult = parser.getAPInfoListBySort();
		showScanAPResult();
	}

	private void updAP() {
		/*
		 * APInfo selectScanAPInfo =
		 * this.sortResult.get(this.scanAPResultSelectIndex); APInfo
		 * selectPointAPInfo = this.arrayPointAPInfo.get(selectedPointIndex);
		 * String SSID = selectScanAPInfo.getSsid(); String MAC =
		 * selectScanAPInfo.getBssid(); String otherSSID=""; String otherBSSID
		 * =""; for(int index =0;index<sortResult.size();index++) {
		 * if(scanAPResultSelectIndex!=index) { APInfo info =
		 * sortResult.get(index); otherSSID = otherSSID+info.getSsid()+",";
		 * otherBSSID = otherBSSID+info.getBssid()+","; } }
		 * 
		 * selectPointAPInfo.setSsid(SSID); selectPointAPInfo.setBssid(MAC);
		 * selectPointAPInfo.setRssi(selectScanAPInfo.getRssi());
		 * selectPointAPInfo.setOtherSSID(otherSSID);
		 * selectPointAPInfo.setOtherBSSID(otherBSSID);
		 * selectPointAPInfo.setAction("add");
		 */
		APInfo selectPointAPInfo = this.arrayPointAPInfo
				.get(selectedPointIndex);
		if (this.pointAPInfoComplete(selectPointAPInfo))// !selectPointAPInfo.getSsid().equals(""))
			preDeleteAPApiProgress();
		else
			apiProgress();

	}

	private void showScanAPResult() {

		final CharSequence[] items = new CharSequence[sortResult.size()];
		for (int index = 0; index < sortResult.size(); index++) {
			APInfo info = sortResult.get(index);
			items[index] = "SSID: " + info.getSsid() + "\nMAC: "
					+ info.getBssid() + "\nRSSI: " + info.getRssi();
		}
		// int selectedIndex;
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(this.mContext.getResources().getString(
				R.string.string_scan_result));
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// Toast.makeText(mContext, items[item],
						// Toast.LENGTH_SHORT).show();
						scanAPResultSelectIndex = item;
					}
				});
		builder.setPositiveButton(
				mContext.getResources().getString(
						R.string.dialog_wifi_alert_updates),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (checkWifiConnect()) {
							updAP();
						} else
							showDialog(
									mContext.getResources().getString(
											R.string.string_error),
									mContext.getResources().getString(
											R.string.string_update_error));

						// Toast.makeText(mContext,
						// "Update Error,Please Check Your Wifi State",
						// Toast.LENGTH_LONG).show();

						// apiProgress();
						// choiceResult();
					}
				});
		builder.setNegativeButton(
				mContext.getResources().getString(
						R.string.dialog_wifi_alert_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		builder.show();
	}

	private float getRatio() {
		float[] pointXY = new float[2];
		Matrix m = new Matrix();

		// Matrix mPoint = new Matrix();

		m.set(this.matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		// mPoint.set(imageViewHelper.matrixPoint);
		// mPoint.set(this.arrayMatrixPoint.get(selectedPointIndex));

		// RectF rectPoint = new RectF(0, 0, bitmapPoint.getWidth(),
		// bitmapPoint.getHeight());
		// mPoint.mapRect(rectPoint);

		float height = rect.height();
		float width = rect.width();
		float ratio = width / bitmap.getWidth();

		return ratio;

	}

	private void createPopUpWindows(RectF rectPoint, ImageView selectedPoint) {
		String[] title = {
				mContext.getResources().getString(R.string.pop_window_scan),
				mContext.getResources().getString(R.string.pop_window_delete),
				mContext.getResources().getString(R.string.pop_window_info) };// ,"Delete","Info","Scan","Delete","Info","Scan","Delete","Info","Scan","Delete","Info","Scan","Delete","Info","Scan","Delete","Info"};

		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.dialog, null);
		ListView listView = (ListView) layout.findViewById(R.id.lv_dialog);
		listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.text,
				R.id.tv_text, title));

		popupWindow = new PopupWindow(mContext);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// int width = getWindowManager();
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		int popwindowWidth = (int) this.convertDpToPixel(105, this.mContext);// windowManager.getDefaultDisplay().getWidth()
																				// /
																				// 3;
		// double list_size ;
		int popwindowHight = (int) this.convertDpToPixel(140, this.mContext);// (windowManager.getDefaultDisplay().getHeight()/6);

		popupWindow.setWidth(popwindowWidth);
		popupWindow.setHeight(popwindowHight);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// showAsDropDown隡�������iew雿�蛹����抬���誑閬��皛∪�撟�arent
		// popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
		Activity activity = (Activity) mContext;
		int popX = (int) (rectPoint.left - (popwindowWidth * 0.5) + (rectPoint.right - rectPoint.left) / 2);// .right-bitmapPoint.getWidth()/3;
		int popY = (int) (rectPoint.bottom + (popwindowHight * 0.5));// bottom+bitmapPoint.getHeight()/3;

		// Log.i("pop location X", Float.toString(popX));
		// Log.i("pop location Y", Float.toString(popY));
		// Log.i("Ratio", Float.toString(getRatio()));
		popupWindow.showAtLocation(activity.findViewById(R.id.imageView_root),
				Gravity.LEFT | Gravity.TOP, popX, popY);// ������Gravity嚗��霈斗��菜�center.

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// button.setText(title[arg2]);

				// Toast.makeText(mContext, "select-"+arg2,
				// Toast.LENGTH_SHORT).show();
				switch (arg2) {
				case 0:
					APInfo info = arrayPointAPInfo.get(selectedPointIndex);
					/*
					 * if(info.getSsid().equals("")) pointSelectedAction =
					 * "add"; else pointSelectedAction = "upd";
					 */
					pointSelectedAction = "add";
					startScanAP();

					break;
				case 1:
					pointSelectedAction = "del";
					deleteAP();
					break;
				case 2:
					showSelectedAPInfo();
					break;
				}
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
	}

	private void showSelectedAPInfo() {
		APInfo info = this.arrayPointAPInfo.get(selectedPointIndex);
		String title = "AP Setting Info";
		String msg = "";
		// if(this.pointAPInfoComplete(info))//!info.getSsid().equals(""))
		// {
		msg += "AP SSID:\n" + info.getSsid();
		msg += "\n\nAP MAC Address:\n" + info.getBssid();
		msg += "\n\nPIXEL:\nX = " + info.getPointX();
		msg += "\nY = " + info.getPointY();
		msg += "\n\nFloor:\n" + info.getFloor();

		/*
		 * msg +="\n\nOther Scanned AP List:\n======\n";
		 * 
		 * String[] otherSSID = info.getOtherSSID().split(","); String[]
		 * otherBSSID = info.getOtherBSSID().split(","); for(int
		 * index=0;index<otherSSID.length;index++) { String scannedAPInfo =
		 * "SSID: "+otherSSID[index]+"\nMAC: "+otherBSSID[index]; msg
		 * +=scannedAPInfo+"\n======\n"; }
		 */

		// msg +="\n\nOther AP MAC:\n"+info.getOtherBSSID();
		// }
		// else
		// msg =
		// mContext.getResources().getString(R.string.pop_window_info_no_content);
		this.showDialog(title, msg);
	}

	private void deleteAP() {
		APInfo selectPointAPInfo = arrayPointAPInfo.get(selectedPointIndex);
		// String SSID = selectPointAPInfo.getSsid();
		if (!this.pointAPInfoComplete(selectPointAPInfo))// SSID.equals(""))//||selectPointAPInfo.getUpdateState().equals("AlreadyExist"))
		{
			deletePointInView();
		} else {
			if (checkWifiConnect()) {
				// selectPointAPInfo.setAction("del");

				apiProgress();

			} else
				showDialog(
						mContext.getResources()
								.getString(R.string.string_error),
						mContext.getResources().getString(
								R.string.string_delete_error));

			// this.showDialog("Error",
			// "Delete Error,Please Check Your Wifi State");
			// Toast.makeText(mContext,
			// "Delete Error,Please Check Your Wifi State",
			// Toast.LENGTH_LONG).show();
		}
		// if(checkWifiConnect())
		// deleteAPProgressDialog();
		// else
		// Toast.makeText(mContext,
		// "Update Error \nplease check your Wifi state",
		// Toast.LENGTH_LONG).show();

	}

	public void deletePointInView() {
		frameLayout.removeView(arrayImageviewPoint.get(selectedPointIndex));
		arrayImageviewPoint.remove(selectedPointIndex);
		arrayMatrixPoint.remove(selectedPointIndex);
		arraySavedMatrixPoint.remove(selectedPointIndex);
		this.arrayPointAPInfo.remove(selectedPointIndex);
	}

	public void fetchAPMac() {
		wiFiManager = (WifiManager) mContext
				.getSystemService(mContext.WIFI_SERVICE);
		if (receiver == null) {
			Log.i(TAG, "register Receiver");

			receiver = new ApMacScanReceiver(this);

			mContext.registerReceiver(receiver, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		}
		wiFiManager.startScan();
	}

	public void scanningProgress() {
		// TODO Auto-generated method stub
		pd = ProgressDialog.show(mContext, "", mContext.getResources()
				.getString(R.string.progress_scan));
		new Thread() {
			@Override
			public void run() {

				fetchAPMac();

				// handler.sendEmptyMessage(0);
			}

		}.start();
	}

	public void showScanRes(String msg) {
		// Log.i(TAG, "showScanRes AP SSID = " +apName + " AP MacAddress = "
		// +apMac);

		pd.dismiss();

		if (receiver != null) {
			mContext.unregisterReceiver(receiver);
			receiver = null;
		}

		Builder MyAlertDialog = new AlertDialog.Builder(mContext);
		MyAlertDialog.setTitle(mContext.getResources().getString(
				R.string.scan_complete));
		MyAlertDialog.setMessage(msg);

		MyAlertDialog.setPositiveButton(
				mContext.getResources().getString(
						R.string.dialog_wifi_alert_confirm),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// ScanResults.getApName();
						// apiProgress();

					}
				});

		MyAlertDialog.setCancelable(false);
		MyAlertDialog.show();
	}

	private float[] calSelectedPointPixel() {
		float[] pointXY = new float[2];
		Matrix m = new Matrix();

		Matrix mPoint = new Matrix();

		m.set(this.matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		// mPoint.set(imageViewHelper.matrixPoint);
		mPoint.set(this.arrayMatrixPoint.get(selectedPointIndex));

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

		float pointX = (rectPoint.left - rect.left) / ratio;// +pointCenterX)/ratio;
		float pointY = (rectPoint.top - rect.top) / ratio;// +pointCenterY)/ratio;
		Log.i("Ratio ", Float.toString(ratio));
		// Log.i("Select point pixelX ",Float.toString(pointX));
		// Log.i("Select point pixelY ",Float.toString(pointY));
		pointXY[0] = pointX;
		pointXY[1] = pointY;
		return pointXY;
	}

	protected void apiProgress() {
		// TODO Auto-generated method stub
		apiPD = ProgressDialog.show(mContext, "", mContext.getResources()
				.getString(R.string.progress_update));
		new Thread() {
			@Override
			public void run() {
				APInfo selectPointAPInfo = arrayPointAPInfo
						.get(selectedPointIndex);

				androidID = Secure.getString(mContext.getContentResolver(),
						Secure.ANDROID_ID);
				Log.v("UUID = ", androidID);

				/*
				 * float[] xy = calSelectedPointPixel(); float pixelX = xy[0];
				 * float pixelY = xy[1];
				 */

				if (pointSelectedAction.equals("add")) {
					APInfo selectScanAPInfo = sortResult
							.get(scanAPResultSelectIndex);
					String otherSSID = "";
					String otherBSSID = "";
					for (int index = 0; index < sortResult.size(); index++) {
						if (scanAPResultSelectIndex != index) {
							APInfo info = sortResult.get(index);
							otherSSID = otherSSID + info.getSsid() + ",";
							otherBSSID = otherBSSID + info.getBssid() + ",";
						}
					}

					// selectPointAPInfo.setSsid(selectScanAPInfo.getSsid());
					// selectPointAPInfo.setBssid(selectScanAPInfo.getBssid());
					// selectPointAPInfo.setRssi(selectScanAPInfo.getRssi());
					// selectPointAPInfo.setOtherSSID(otherSSID);
					// selectPointAPInfo.setOtherBSSID(otherBSSID);
					// selectPointAPInfo.setAction("add");

					apiTransferState = siteSurveyAPIProxy.updApPoint(
							currentMapName, androidID,
							selectScanAPInfo.getSsid(), otherBSSID,
							selectScanAPInfo.getBssid(),
							selectPointAPInfo.getPointX(),
							selectPointAPInfo.getPointY(), pointSelectedAction);// selectPointAPInfo.getAction());

				} else {
					apiTransferState = siteSurveyAPIProxy.updApPoint(
							currentMapName, androidID,
							selectPointAPInfo.getSsid(),
							selectPointAPInfo.getOtherBSSID(),
							selectPointAPInfo.getBssid(),
							selectPointAPInfo.getPointX(),
							selectPointAPInfo.getPointY(), pointSelectedAction);// selectPointAPInfo.getAction());
				}
				/*
				 * apiTransferState =
				 * siteSurveyAPIProxy.updApPoint(currentMapName, androidID,
				 * selectPointAPInfo.getSsid(),
				 * selectPointAPInfo.getOtherBSSID(),
				 * selectPointAPInfo.getBssid(), Float.toString(pixelX),
				 * Float.toString(pixelY), selectPointAPInfo.getAction());
				 */
				apiHandler.sendEmptyMessage(0);
			}

		}.start();
	}

	Handler apiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			apiPD.dismiss();
			APInfo selectPointAPInfo = arrayPointAPInfo.get(selectedPointIndex);

			if (apiTransferState.equals("Success")) {
				ImageView image = arrayImageviewPoint.get(selectedPointIndex);

				if (pointSelectedAction.equals("del"))// selectPointAPInfo.getAction().equals("del"))
					deletePointInView();
				else if (pointSelectedAction.equals("add"))// selectPointAPInfo.getAction().equals("add"))
				{
					image.setImageResource(R.drawable.pin_green);
					// selectPointAPInfo.setUpdateState("Success");

					APInfo selectScanAPInfo = sortResult
							.get(scanAPResultSelectIndex);
					String otherSSID = "";
					String otherBSSID = "";
					for (int index = 0; index < sortResult.size(); index++) {
						if (scanAPResultSelectIndex != index) {
							APInfo info = sortResult.get(index);
							otherSSID = otherSSID + info.getSsid() + ",";
							otherBSSID = otherBSSID + info.getBssid() + ",";
						}
					}
					selectPointAPInfo.setSsid(selectScanAPInfo.getSsid());
					selectPointAPInfo.setBssid(selectScanAPInfo.getBssid());
					selectPointAPInfo.setRssi(selectScanAPInfo.getRssi());
					selectPointAPInfo.setOtherSSID(otherSSID);
					selectPointAPInfo.setOtherBSSID(otherBSSID);
					selectPointAPInfo.setSource("server");
					// selectPointAPInfo.setAction("add");
				}
			} else if (apiTransferState.equals("AlreadyExist")) {

				// Toast.makeText(mContext,
				// "Point Already Exist, Please Check Your Point",
				// Toast.LENGTH_LONG).show();
				ImageView image = arrayImageviewPoint.get(selectedPointIndex);
				// selectPointAPInfo.setUpdateState("AlreadyExist");

				/*
				 * if(selectPointAPInfo.getAction().equals("add"))
				 * image.setImageResource(R.drawable.pin_blue);
				 */

				/*
				 * selectPointAPInfo.setSsid("");
				 * selectPointAPInfo.setBssid(""); selectPointAPInfo.setRssi(0);
				 * selectPointAPInfo.setOtherSSID("");
				 * selectPointAPInfo.setOtherBSSID("");
				 * //selectPointAPInfo.setPointX(pixelX);
				 * //selectPointAPInfo.setPointY(pixelY);
				 * selectPointAPInfo.setAction("");
				 */

				showDialog(
						mContext.getResources()
								.getString(R.string.string_error),
						mContext.getResources().getString(
								R.string.string_point_already_exist));
				// showDialog("Error",
				// "Point Already Exist, Please Check Your Point");
			} else if (pointSelectedAction.equals("del"))// selectPointAPInfo.getAction().equals("del"))
			{
				// Toast.makeText(mContext,
				// "Delete Error, Please Check Your Connection With Server",
				// Toast.LENGTH_LONG).show();
				showDialog(
						mContext.getResources()
								.getString(R.string.string_error),
						mContext.getResources()
								.getString(
										R.string.string_delete_error_check_connect_with_server));

				// showDialog("Error",
				// "Delete Error, Please Check Your Connection With Server");
			} else if (pointSelectedAction.equals("add"))// selectPointAPInfo.getAction().equals("add"))
			{
				/*
				 * selectPointAPInfo.setSsid("");
				 * selectPointAPInfo.setBssid(""); selectPointAPInfo.setRssi(0);
				 * selectPointAPInfo.setOtherBSSID("");
				 * selectPointAPInfo.setOtherSSID("");
				 * selectPointAPInfo.setAction("");
				 */
				showDialog(
						mContext.getResources()
								.getString(R.string.string_error),
						mContext.getResources()
								.getString(
										R.string.string_update_error_check_connect_with_server));

				// showDialog("Error",
				// "Update Error, Please Check Your Connection With Server");
				// Toast.makeText(mContext,
				// "Update Error, Please Check Your Connection With Server",
				// Toast.LENGTH_LONG).show();
			}
			apiTransferState = "";
		}
	};

	private boolean checkWifiConnect() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(mContext.CONNECTIVITY_SERVICE);
		// get network infop
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		WifiManager wiFiManager = (WifiManager) mContext
				.getSystemService(mContext.WIFI_SERVICE);
		// String UUID = API_Base.getUUID();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	private void showDialog(String title, String msg) {
		Builder MyAlertDialog = new AlertDialog.Builder(mContext);
		MyAlertDialog.setTitle(title);
		MyAlertDialog.setMessage(msg);

		MyAlertDialog.setPositiveButton(
				mContext.getResources().getString(
						R.string.dialog_wifi_alert_confirm),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// ScanResults.getApName();
						// apiProgress();

					}
				});

		MyAlertDialog.setCancelable(false);
		MyAlertDialog.show();
	}

	boolean pointAPInfoComplete(APInfo info) {
		if (info.getSsid().equals("") || info.getBssid().equals("")) {
			return false;
		} else
			return true;
	}

	private void preDeleteAPApiProgress() {
		// TODO Auto-generated method stub
		apiPD = ProgressDialog.show(mContext, "", mContext.getResources()
				.getString(R.string.progress_update));
		new Thread() {
			@Override
			public void run() {
				APInfo selectPointAPInfo = arrayPointAPInfo
						.get(selectedPointIndex);

				androidID = Secure.getString(mContext.getContentResolver(),
						Secure.ANDROID_ID);
				Log.v("UUID = ", androidID);

				apiTransferState = siteSurveyAPIProxy.updApPoint(
						currentMapName, androidID, selectPointAPInfo.getSsid(),
						selectPointAPInfo.getOtherBSSID(),
						selectPointAPInfo.getBssid(),
						selectPointAPInfo.getPointX(),
						selectPointAPInfo.getPointY(), "del");// selectPointAPInfo.getAction());
				preDeleteAPApiHandler.sendEmptyMessage(0);
			}

		}.start();
	}

	private Handler preDeleteAPApiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			apiPD.dismiss();
			if (apiTransferState.equals("Success")) {
				apiTransferState = "";
				APInfo selectPointAPInfo = arrayPointAPInfo
						.get(selectedPointIndex);
				ImageView image = arrayImageviewPoint.get(selectedPointIndex);
				image.setImageResource(R.drawable.pin_blue);
				selectPointAPInfo.setSsid("");
				selectPointAPInfo.setBssid("");
				selectPointAPInfo.setRssi(0);
				selectPointAPInfo.setOtherSSID("");
				selectPointAPInfo.setOtherBSSID("");
				apiProgress();

			} else {
				showDialog(
						mContext.getResources()
								.getString(R.string.string_error),
						mContext.getResources()
								.getString(
										R.string.string_update_error_check_connect_with_server));
			}
			apiTransferState = "";
		}
	};

	public float[] calNewPointPixel(Matrix pointM) {
		float[] pointXY = new float[2];
		Matrix m = new Matrix();

		Matrix mPoint = new Matrix();

		m.set(this.matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		// mPoint.set(imageViewHelper.matrixPoint);
		mPoint.set(pointM);

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

	private void drawLine() {
		// imageViewLine.invalidate();
		mDrawLine = new drawLineHelper(this.mContext, this.bitmap.getWidth(),
				this.bitmap.getHeight());
		float[] sPixelXY = new float[2];
		float[] tPixelXY = new float[2];
		// if(isDrawing)
		// Now the layout parameters, these are a little tricky at first

		sPixelXY = calNewPointPixel(savedLinePoint);
		tPixelXY = calNewPointPixel(matrixPoint);

		mDrawLine.clearLine();
		imageViewLine.setImageBitmap(mDrawLine.drawLine((int) sPixelXY[0],
				(int) sPixelXY[1], (int) tPixelXY[0], (int) tPixelXY[1]));

	}

	private void addNewLineImageView() {
		// Let's create the missing ImageView
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT);
		ImageView imageView = new ImageView(mContext);
		imageView.setScaleType(ImageView.ScaleType.MATRIX);
		Matrix matrix = new Matrix();
		matrix.reset();
		// Now the layout parameters, these are a little tricky at first
		int distance = pointDistance(firstLinePointMatrix, matrixPoint);
		Log.v("point Distance", "distance = " + distance);
		float[] sPixelXY = new float[2];
		float[] tPixelXY = new float[2];
		sPixelXY = calNewPointPixel(savedLinePoint);
		tPixelXY = calNewPointPixel(matrixPoint);
		savedLinePoint.set(matrixPoint);
		float[] linePoint = new float[4];
		linePoint[0] = sPixelXY[0];
		linePoint[1] = sPixelXY[1];
		linePoint[2] = tPixelXY[0];
		linePoint[3] = tPixelXY[1];
		arrayPxLine.add(linePoint);
		/*
		 * if(distance>50){ sPixelXY = calNewPointPixel(savedLinePoint);
		 * tPixelXY = calNewPointPixel(matrixPoint);
		 * savedLinePoint.set(matrixPoint); }else{
		 * Log.v("draw line","correctness"); sPixelXY =
		 * calNewPointPixel(savedLinePoint); tPixelXY =
		 * calNewPointPixel(firstLinePointMatrix); mDrawLine.clearLine();
		 * savedLinePoint.set(firstLinePointMatrix);
		 * matrixPoint.set(firstLinePointMatrix); }
		 */
		drawLineHelper savedDrawLine = new drawLineHelper(this.mContext,
				this.bitmap.getWidth(), this.bitmap.getHeight());
		Bitmap savedBitmap = savedDrawLine.drawLine((int) sPixelXY[0],
				(int) sPixelXY[1], (int) tPixelXY[0], (int) tPixelXY[1]);
		// savedDrawLine.clear();
		imageView.setImageBitmap(savedBitmap);
		arrayImageviewLine.add(imageView);
		// imageView.setScaleType(ImageView.ScaleType.MATRIX);
		// imageView.setImageResource(R.drawable.pin_blue);//.point);
		Log.v("add line", "subview: " + frameLayout.getChildCount());
		frameLayout.addView(ImageViewHelper.arrayImageviewLine
				.get(ImageViewHelper.arrayImageviewLine.size() - 1),
				frameLayout.getChildCount(), params);

		float[] arrayM = new float[9];
		ImageViewHelper.matrix.getValues(arrayM);
		matrix.setValues(arrayM);

		imageView.setImageMatrix(matrix);
		// savedLinePoint.set(matrixPoint);

	}

	private int pointDistance(Matrix sMatrix, Matrix tMatrix) {
		int distance = 0;
		float[] sPointXY = new float[2];
		float[] tPointXY = new float[2];
		sPointXY = calNewPointPixel(sMatrix);
		tPointXY = calNewPointPixel(tMatrix);
		distance = (int) Math.sqrt(Math.pow(sPointXY[0] - tPointXY[0], 2)
				+ Math.pow(sPointXY[1] - tPointXY[1], 2));

		return distance;
	}

	private float getMovingDist(float diffX, float diffY) {
		return (float) Math.sqrt(diffX * diffX + diffY * diffY);
	}

}
