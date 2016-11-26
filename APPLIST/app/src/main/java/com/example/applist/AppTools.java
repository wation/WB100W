package com.example.applist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class AppTools {
	private static final String[] defaultShowApp = {
			"com.rockchip.wfd",
			"com.waxrain.airplaydmr2",
			"com.android.browser",
			"kantv.filemanager",
			"com.android.settings",
	};

	public static void initApps(Context context){
		SharedPreferences appShow = context.getSharedPreferences("AppShow",Activity.MODE_PRIVATE);
		boolean appInited = appShow.getBoolean("AppShow_appInited", false);

		if(!appInited){
			Editor editor = appShow.edit();
			for(int i = 0;i < defaultShowApp.length;i++){
				editor.putBoolean(defaultShowApp[i], false);
			}
			editor.putBoolean("AppShow_appInited", true);
			editor.commit();
		}
	}


	public static ArrayList<AppInfo> getAllApps(Context context){
		ArrayList<AppInfo> items = new ArrayList<AppInfo>();
		AppInfo info;
		// 得到PackageManager对象
		PackageManager pm = context.getPackageManager();
		// 得到系统安装的所有程序包的PackageInfo对象
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (PackageInfo pi : packs) {
			String packageName = pi.packageName;
			//Log.i("appshow", "packageName : " + pi.packageName);
			if(pm.getLaunchIntentForPackage(packageName)!=null){
				info = new AppInfo();
				info.setIcon(pi.applicationInfo.loadIcon(pm));// 图标
				info.setAppName(String.valueOf(pi.applicationInfo.loadLabel(pm)));// 应用名
				info.setPackageName(pi.packageName);// 包名
				items.add(info);// 循环读取存到HashMap,再增加到ArrayList.一个HashMap就是一项
			}
		}
		return items;
	}


	public static ArrayList<AppInfo> getSetApps(Context context){
		ArrayList<AppInfo> items = new ArrayList<AppInfo>();
		SharedPreferences preferences ;
		AppInfo info;
		boolean flag;
		// 得到PackageManager对象
		PackageManager pm = context.getPackageManager();
		// 得到系统安装的所有程序包的PackageInfo对象
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (PackageInfo pi : packs) {
			if (pm.getLaunchIntentForPackage(pi.packageName)!=null) {
//				preferences = context.getSharedPreferences("AppShow",Activity.MODE_PRIVATE);
//				flag = preferences.getBoolean(pi.packageName, true);
//				if(!flag){//判断是否显示该应用
				info = new AppInfo();
				info.setIcon(pi.applicationInfo.loadIcon(pm));// 图标
				info.setAppName(String.valueOf(pi.applicationInfo.loadLabel(pm)));// 应用名
				info.setPackageName(pi.packageName);// 包名
				items.add(info);// 循环读取存到HashMap,再增加到ArrayList.一个HashMap就是一项
//				}
			}

		}
		return items;

	}

}
