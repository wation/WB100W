package com.example.applist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

public class ListOfAppsActivity extends Activity implements OnPageChangeListener {

	private static final String TAG = "ListOfAppsActivity";
	private MyViewPager pager;
	private ArrayList<AppInfo> list;
	private ArrayList<AppInfo> mlist;
	private ArrayList<ArrayList<AppInfo>> AllAppList;
	private int pagerSize;
	private ViewPagerAdapter viewPagerAdapter;
	private ViewGroup group;
	private ImageView imageView;
	private ImageView[] imageViews;
	private static final int SHOW =1,HIDDEN =2,LOAD = 3;
	public  Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //声明使用自定义标题
		setContentView(R.layout.app_viewpager);
		UiSetup.setBackTitle(this, "应用列表");
		//AppTools.getList(this);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.app_title);//自定义布局赋值
		mHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
					case SHOW:
						group.setVisibility(View.VISIBLE);
						break;

					case HIDDEN:
						group.setVisibility(View.INVISIBLE);
						break;
					case LOAD:
						imageViews = new ImageView[AllAppList.size()];
						for (int i = 0; i < ListOfAppsActivity.this.AllAppList.size(); i++) {
							imageView = new ImageView(ListOfAppsActivity.this);
							imageView.setLayoutParams(new LayoutParams(20, 20));
							imageView.setPadding(20, 0, 20, 20);
							imageViews[i] = imageView;
							if (i == 0) {
								// 默认选中第一个view
								imageViews[i].setBackgroundResource(R.drawable.dot_selected);
							} else {
								imageViews[i].setBackgroundResource(R.drawable.dot_none);
							}
							group.addView(imageViews[i]);
						}
						viewPagerAdapter = new ViewPagerAdapter(AllAppList,getApplicationContext());
						pager.setOnPageChangeListener(ListOfAppsActivity.this);
						pager.setAdapter(viewPagerAdapter);
						if(AllAppList.size()==1){
							pager.setpagerCount(1,ListOfAppsActivity.this);
						}else{
							pager.setpagerCount(AllAppList.size(),ListOfAppsActivity.this);
							pager.setCurrentItem(AllAppList.size()*1000);
						}
						break;
				}
			};
		};
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();

		//registerHomeKeyReceiver(this);
	}

	@Override
	protected void onPause() {

		//unregisterHomeKeyReceiver(this);
		super.onPause();
	}

	private void initView() {
		pager = (MyViewPager) findViewById(R.id.viewpager);
		//添加滑动时候的小圆点
		group = (ViewGroup) findViewById(R.id.viewGroup);
		new Thread(){
			public void run() {
				getAppDate();
				mHandler.sendEmptyMessage(LOAD);
			};
		}.start();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		//更新界面
		viewPagerAdapter.Update(getAppDate());
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	private ArrayList<ArrayList<AppInfo>> getAppDate() {
		//获得设置可显示的App的list集合
		list = AppTools.getSetApps(getApplicationContext());
		//每页显示3行4列12个，获得显示的页数
		pagerSize = (list.size()-1) / 24 + 1;
		//每页需要显示的mlist作为AllAppList集合的元素
		AllAppList = new ArrayList<ArrayList<AppInfo>>();
		for (int i = 0; i < pagerSize; i++) {
			mlist = new ArrayList<AppInfo>();
			//判断最后一页的list个数，不一定有12个
			for (int j = i * 24; j < ((i + 1) * 24 < list.size() ? (i + 1) * 24
					: list.size()); j++) {
				mlist.add(list.get(j));
			}
			AllAppList.add(mlist);
			mlist = null;
		}
		return AllAppList;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		arg0 = arg0 % AllAppList.size();

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if(AllAppList.size()!=1){//如果是一页怎不显示小圆点
			//获得实际选中的页面
			arg0 = arg0 % AllAppList.size();
			for (int i = 0; i < AllAppList.size(); i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.dot_selected);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.dot_none);
				}
			}
		}
	}

	private HomeWatcherReceiver mHomeKeyReceiver = null;

	private void registerHomeKeyReceiver(Context context) {
		Log.i(TAG, "registerHomeKeyReceiver");
		mHomeKeyReceiver = new HomeWatcherReceiver();
		final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

		context.registerReceiver(mHomeKeyReceiver, homeFilter);
	}

	private void unregisterHomeKeyReceiver(Context context) {
		Log.i(TAG, "unregisterHomeKeyReceiver");
		if (null != mHomeKeyReceiver) {
			context.unregisterReceiver(mHomeKeyReceiver);
		}
	}

	public class HomeWatcherReceiver extends BroadcastReceiver {
		private static final String LOG_TAG = "HomeReceiver";
		private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
		private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
		private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
		private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i(LOG_TAG, "onReceive: action: " + action);
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				// android.intent.action.CLOSE_SYSTEM_DIALOGS
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				Log.i(LOG_TAG, "reason: " + reason);

				if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
					// 短按Home键
					Log.i(LOG_TAG, "homekey");
					finish();

				}
				else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
					// 长按Home键 或者 activity切换键
					Log.i(LOG_TAG, "long press home key or activity switch");

				}
				else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
					// 锁屏
					Log.i(LOG_TAG, "lock");
				}
				else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
					// samsung 长按Home键
					Log.i(LOG_TAG, "assist");
				}

			}
		}

	}
}
