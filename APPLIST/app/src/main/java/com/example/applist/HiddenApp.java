package com.example.applist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HiddenApp extends Activity{

	private ListView lv;
	private MyAdapter adapter;
	ArrayList<AppInfo> items = new ArrayList<AppInfo>();
	private SharedPreferences preferences;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		com.szIdeaComm.Viper.Ui.UiSetup.setCenterTitle(this, R.layout.app_hiddenapp, R.string.app_hider);
		setContentView(R.layout.app_hiddenapp);
		UiSetup.setBackTitle(this, "隐藏应用"); // 设置标题栏
		lv = (ListView)findViewById(R.id.lv_hidden);
		items = AppTools.getAllApps(this);
		preferences = getSharedPreferences("AppShow", MODE_PRIVATE);
		editor = preferences.edit();
		adapter = new MyAdapter(this,items);
		lv.setAdapter(adapter);
		addListener();

	}



	private void addListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				CheckBox checkbox = (CheckBox) view.findViewById(R.id.hidden_check);
				checkbox.setChecked(!checkbox.isChecked());
				adapter.getItem(position).setCheck(checkbox.isChecked());
				editor.putBoolean(adapter.getItem(position).getPackageName(),checkbox.isChecked());
				editor.commit();
			}
		});
	}

	private class MyAdapter extends BaseAdapter {
		private ArrayList<AppInfo> items = new ArrayList<AppInfo>();
		private Context context;


		public MyAdapter(Context context,ArrayList<AppInfo> items){
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
				convertView = LayoutInflater.from(context).inflate
						(R.layout.app_hiddenapp_item, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.hidden_icon);
				holder.tv = (TextView) convertView.findViewById(R.id.hidden_appName);
				holder.cb = (CheckBox) convertView.findViewById(R.id.hidden_check);
				convertView.setTag(holder);
			}else{
				holder = (viewHolder) convertView.getTag();
			}
			final AppInfo info = getItem(position);
			holder.iv.setImageDrawable((Drawable) info.getIcon());
			holder.tv.setText(info.getAppName());
			holder.cb.setChecked(info.isCheck());
			boolean flag = preferences.getBoolean(info.getPackageName(), true);
			if(flag){
				holder.cb.setChecked(true);
			}
			holder.cb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getItem(position).setCheck(holder.cb.isChecked());
					editor.putBoolean(info.getPackageName(),holder.cb.isChecked());
					editor.commit();

				}
			});

			return convertView;
		}

		private class viewHolder{
			private ImageView iv;
			private TextView tv;
			private CheckBox cb;

		}

	}


}
