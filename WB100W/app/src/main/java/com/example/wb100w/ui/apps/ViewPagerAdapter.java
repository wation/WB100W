package com.example.wb100w.ui.apps;

import java.util.ArrayList;

import com.example.wb100w.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ViewPagerAdapter extends PagerAdapter{
	private ArrayList<ArrayList<AppInfo>> list;
	private Context context;



	public ViewPagerAdapter(ArrayList<ArrayList<AppInfo>> list, Context context) {
		super();
		this.context = context;
		if(list!=null){
			this.list =  list;
		}else{
			list = new ArrayList<ArrayList<AppInfo>>();
		}
	}

	public void Update(ArrayList<ArrayList<AppInfo>> list){
		if(list!=null){
			this.list =list;
		}else{
			list = new ArrayList<ArrayList<AppInfo>>();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(list.size()==1){
			return 1;
		}else{
			return Integer.MAX_VALUE;
		}

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==(View)arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View)object);
	}

	@Override
	public int getItemPosition(Object object) {
		//POSITION_NONE 是一个PagerAdapter的内部常量，值是-2，当调用notifyDataSetChanged()才可以更新数据
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View v =  LayoutInflater.from(context).inflate( R.layout.app_gridview, null);
		GridView gv = (GridView) v.findViewById(R.id.gv);
		gv.setAdapter(new MyAppAdapter(context, list.get(position%list.size())));
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int p, long id) {
				String packageName = (String) list.get(position%list.size()).get(p).getPackageName();
				PackageManager pm = context.getPackageManager();
				// 取到点击的包名
				Intent i = pm.getLaunchIntentForPackage(packageName);
				// 如果该程序不可启动（像系统自带的包，有很多是没有入口的）会返回NULL
				if (i != null) {
					context.startActivity(i);
				}
			}
		});
		((ViewPager) container).addView(v);
		return v;
	}


}
