package com.example.wb100w.ui.apps;

import android.graphics.drawable.Drawable;


public class AppInfo {
	private Drawable icon;
	private String appName;
	private String packageName;
	private boolean isCheck;
	
	public AppInfo() {
		super();
	}

	
	public AppInfo(Drawable icon, String appName, String packageName,
			boolean isCheck) {
		super();
		this.icon = icon;
		this.appName = appName;
		this.packageName = packageName;
		this.isCheck = isCheck;
	}

	public boolean isCheck() {
		return isCheck;
	}



	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}



	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable drawable) {
		this.icon = drawable;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
	
	
}
