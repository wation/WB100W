package com.example.applist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAppAdapter extends BaseAdapter {
	private ArrayList<AppInfo> items = new ArrayList<AppInfo>();
	private Context context;
	private HashMap<String, Drawable> iconMap = new HashMap<>();

	public MyAppAdapter(Context context,ArrayList<AppInfo> items){
		this.context = context;
		if(items!=null){
			this.items = items;
		}else{
			items = new ArrayList<AppInfo>();
		}

		iconMap.put("com.android.settings", context.getResources().getDrawable(R.drawable.btn_setting));
		iconMap.put("com.android.browser", context.getResources().getDrawable(R.drawable.btn_browser));
		iconMap.put("com.android.calculator2", context.getResources().getDrawable(R.drawable.btn_calculator));
		iconMap.put("com.android.deskclock", context.getResources().getDrawable(R.drawable.btn_alam));
		iconMap.put("com.android.providers.downloads.ui", context.getResources().getDrawable(R.drawable.btn_update));
		iconMap.put("com.android.rk", context.getResources().getDrawable(R.drawable.btn_myfile));
		iconMap.put("com.rockchip.wfd", context.getResources().getDrawable(R.drawable.btn_mirroring));
		iconMap.put("com.waxrain.airplaydmr2", context.getResources().getDrawable(R.drawable.btn_sharing));
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public AppInfo getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.app_gridview_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.icon);
            holder.tv = (TextView) convertView.findViewById(R.id.appName);
            convertView.setTag(holder);
        }else{
        	holder = (viewHolder) convertView.getTag();
        }
		final AppInfo info = getItem(position);
		Drawable drawable = iconMap.get(info.getPackageName());
        holder.iv.setImageDrawable(drawable == null ? (Drawable) info.getIcon() : drawable);
        holder.tv.setText(info.getAppName());
		return convertView;
	}
	
	private class viewHolder{
		private ImageView iv;
		private TextView tv;
		
		
	}

}
