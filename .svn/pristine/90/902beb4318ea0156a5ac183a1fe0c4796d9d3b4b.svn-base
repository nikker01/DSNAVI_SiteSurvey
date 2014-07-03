package com.andvantech.dsnavi_sitesurvey.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.andvantech.dsnavi_sitesurvey.PointAccessDBHelper;
import com.andvantech.dsnavi_sitesurvey.PointAccessDataVO;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;

public class PointAccessProxy extends Context{

	private SQLiteDatabase db;
	private PointAccessDBHelper dbHelper;
	Context context;
	
	private int m_PointCount;
	private String[] pointsSsid;
	private String[] pointsBssid;
	private String[] pointsOtherssid;
	private String[] pointsOtherBssid;
	private String[] pointsPointx;
	private String[] pointsPointy;
	private String[] pointsColor;
	private String[] pointsFloor;
	private String[] pointsSource;
	
	private String[] column = { "_SSID", "_BSSID", "_OTHERSSID", "_OTHERBSSID", "_POINTX",
			"_POINTY", "_COLOR","_FLOOR","_SOURCE"};

	public PointAccessProxy(Context ctx) {
		this.context = ctx;
	}
	
	public void initDB() {
		if (context == null)
			Log.i("PointAccessProxy", "context = null");
		else
			Log.i("PointAccessProxy", "context != null");

		dbHelper = new PointAccessDBHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public void dropTable()
	{
		Log.i("PointAccessProxy", "dropTable BEGIN");
		dbHelper.dropTable();
	}
	
	public void deletePointByFloor(String floor)
	{
		Log.i("PointAccessProxy", "Delete Point By Floor BEGIN");
		dbHelper.deletePointByFloor(floor);
	}
	public void deletePointBySource(String source)
	{
		Log.i("PointAccessProxy", "Delete Point By Source BEGIN");
		dbHelper.deletePointBySource(source);
	}
	public boolean isTableExists(String tableName)
	{
		Log.i("PointAccessProxy", "Check Table Exist");
		return dbHelper.isTableExists(tableName);
	}
	public void pointDataCreate(String[] data)
	{
		Log.i("PointAccessProxy", "pointDataCreate BEGIN");
		
		ContentValues values = new ContentValues();
		
		values.put(PointAccessDataVO.SSID, data[0]);
		values.put(PointAccessDataVO.BSSID, data[1]);
		values.put(PointAccessDataVO.OTHERSSID, data[2]);
		values.put(PointAccessDataVO.OTHERBSSID, data[3]);
		values.put(PointAccessDataVO.POINTX, data[4]);
		values.put(PointAccessDataVO.POINTY, data[5]);
		values.put(PointAccessDataVO.COLOR, data[6]);
		values.put(PointAccessDataVO.FLOOR, data[7]);
		values.put(PointAccessDataVO.SOURCE, data[8]);
		
		db.insert(PointAccessDataVO.TABLE_NAME, null, values);
	}
	
	public void getPointData(String floor)
	{
		Log.i("PointAccessProxy", "getPointData BEGIN");
		
		Cursor c = null;
		c = db.query(PointAccessDataVO.TABLE_NAME, column, "_FLOOR='" + floor+"'", null,
				null, null, null);
		
		Log.i("getPointData", "data count =" + c.getCount());
		m_PointCount = c.getCount();
		pointsSsid = new String[m_PointCount];
		pointsBssid = new String[m_PointCount];
		pointsOtherssid = new String[m_PointCount];
		pointsOtherBssid = new String[m_PointCount];
		pointsPointx = new String[m_PointCount];
		pointsPointy = new String[m_PointCount];
		pointsColor = new String[m_PointCount];
		pointsFloor = new String[m_PointCount];
		pointsSource = new String[m_PointCount];
		if (c.getCount() >= 1) {
			for (int pointIndex = 0; pointIndex < c.getCount(); pointIndex++) {
				c.moveToPosition(pointIndex);
				
				for (int i = 0; i < c.getColumnCount(); i++) {
					String strData = c.getString(i);

					switch (i) {
					case 0:
						pointsSsid[pointIndex] = strData;
						break;
					case 1:
						pointsBssid[pointIndex] = strData;
						break;
					case 2:
						pointsOtherssid[pointIndex] = strData;
						break;
					case 3:
						pointsOtherBssid[pointIndex] = strData;
						break;
					case 4:
						pointsPointx[pointIndex] = strData;
						break;
					case 5:
						pointsPointy[pointIndex] = strData;
						break;
					case 6:
						pointsColor[pointIndex] = strData;
						break;
					case 7:
						pointsFloor[pointIndex] = strData;
						break;
					case 8:
						pointsSource[pointIndex] = strData;
						break;
					}
				}
			}
		}

	}
	
	public int getPointsCount()
	{
		return m_PointCount;
	}
	
	public String[] getPointsSSID()
	{
		return pointsSsid;
	}
	
	public String[] getPointsBSSID()
	{
		return pointsBssid;
	}
	
	public String[ ] getPointsOtherSSID()
	{
		return pointsOtherssid;
	}
	
	public String[ ] getPointsOtherBSSID()
	{
		return pointsOtherBssid;
	}
	
	public String[] getPointsPosX()
	{
		return pointsPointx;
	}
	
	public String[] getPointsPosY()
	{
		return pointsPointy;
	}
	
	public String[] getPointsColor()
	{
		return pointsColor;
	}
	
	public String[] getPointsFloor()
	{
		return pointsFloor;
	}
	public String[] getPointsSource()
	{
		return pointsSource;
	}
	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int checkCallingOrSelfPermission(String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingPermission(String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkPermission(String permission, int pid, int uid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Deprecated
	public void clearWallpaper() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context createConfigurationContext(
			Configuration overrideConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context createDisplayContext(Display display) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context createPackageContext(String packageName, int flags)
			throws NameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] databaseList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteDatabase(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFile(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void enforceCallingOrSelfPermission(String permission, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags,
			String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enforceCallingPermission(String permission, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enforceCallingUriPermission(Uri uri, int modeFlags,
			String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enforcePermission(String permission, int pid, int uid,
			String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags,
			String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enforceUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags,
			String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] fileList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetManager getAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getCacheDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentResolver getContentResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getDatabasePath(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getDir(String name, int mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getExternalCacheDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getExternalFilesDir(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFileStreamPath(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFilesDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Looper getMainLooper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getObbDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageCodePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PackageManager getPackageManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageResourcePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSystemService(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Theme getTheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public Drawable getWallpaper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public int getWallpaperDesiredMinimumHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Deprecated
	public int getWallpaperDesiredMinimumWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FileInputStream openFileInput(String name)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileOutputStream openFileOutput(String name, int mode)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public Drawable peekWallpaper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter, String broadcastPermission, Handler scheduler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeStickyBroadcast(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revokeUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBroadcast(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBroadcast(Intent intent, String receiverPermission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStickyBroadcast(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStickyOrderedBroadcast(Intent intent,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStickyOrderedBroadcastAsUser(Intent intent,
			UserHandle user, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTheme(int resid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Deprecated
	public void setWallpaper(Bitmap bitmap) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Deprecated
	public void setWallpaper(InputStream data) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startActivities(Intent[] intents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startActivities(Intent[] intents, Bundle options) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startActivity(Intent intent, Bundle options) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean startInstrumentation(ComponentName className,
			String profileFile, Bundle arguments) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags)
			throws SendIntentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags, Bundle options)
			throws SendIntentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ComponentName startService(Intent service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stopService(Intent service) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		// TODO Auto-generated method stub
		
	}

}
