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

public class MyAppAdapter extends BaseAdapter {
	private ArrayList<AppInfo> items = new ArrayList<AppInfo>();
	private Context context;
	
	
	
	public MyAppAdapter(Context context,ArrayList<AppInfo> items){
		this.context = context;
		if(items!=null){
			this.items = items;
		}else{
			items = new ArrayList<AppInfo>();
		}
		
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
        holder.iv.setImageDrawable((Drawable) info.getIcon());
        holder.tv.setText(info.getAppName());
		return convertView;
	}
	
	private class viewHolder{
		private ImageView iv;
		private TextView tv;
		
		
	}

}
