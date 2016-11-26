package com.example.wb100w;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VerticalSeekBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements OnClickListener, OnHoverListener, VerticalSeekBar.OnSeekChangedListener {

    private static final String TAG = "MainActivity";
    private Button mirroringButton, sharingButton, myAppButton, browserButton, multimediaButton, settingsButton, muteImageView;
    private ImageView batteryImageView, wifiImageView, volumeImageView;
    private TextView timeTextView, dateTextView;
    private VerticalSeekBar seekBar;
    private LinearLayout volumePlaneLayout;
    private RelativeLayout volumeLayout;

    private static final int MAX_VOLUME_HEIGHT = 264;
    // wifi相关
    IntentFilter wifiIntentFilter;  // wifi监听器

    AudioManager mAudioManager;
    private int maxVolumeValue, currentVolumeValue, lastVolumeValue;
    private final static int VOLUME_TYPE = AudioManager.STREAM_MUSIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int resourceId = 0;
        int rid = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            int height = getResources().getDimensionPixelSize(resourceId);
            Log.i(TAG, "nv heigth:" + height);
        }

        initViews();
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 注册wifi消息处理器
        registerReceiver(wifiIntentReceiver, wifiIntentFilter);

        resetViewsLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiIntentReceiver);
    }

    private void resetViewsLayout() {

        Button[] buttons = new Button[] {mirroringButton, sharingButton, myAppButton, browserButton, multimediaButton, settingsButton};
        for (Button button : buttons) {
            RelativeLayout.LayoutParams params = null;
            params = (LayoutParams) button.getLayoutParams();
//            params.width = (int) (148 * SCALE);
//            params.height = (int) (124 * SCALE);
            params.width = 148;
            params.height = 124;
            button.setLayoutParams(params);
        }
    }

    private void initData() {

        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        BatteryReceiver batteryReceiver = new BatteryReceiver();

        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);

        // wifi
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        // 显示时间
        Timer timeTimer = new Timer();
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 更新时间
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String time = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
                        timeTextView.setText(time);
                        String date = new SimpleDateFormat("E.MMM d").format(new Date(System.currentTimeMillis()));
                        dateTextView.setText(date);
                    }
                });
            }
        }, 0, 1000);

        // 音量
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolumeValue = mAudioManager.getStreamMaxVolume(VOLUME_TYPE);
        currentVolumeValue = mAudioManager.getStreamVolume(VOLUME_TYPE);
        lastVolumeValue = currentVolumeValue;
        Log.d(TAG, "max : " + maxVolumeValue + " current : " + currentVolumeValue);
        setVolumeViewByVolume(currentVolumeValue);
        seekBar.setMaxHeight(MAX_VOLUME_HEIGHT);
        seekBar.setMinHeight(convertVolumeValueToHeight(0));
        seekBar.setProgress(convertVolumeValueToHeight(currentVolumeValue));
        volumePlaneLayout.setVisibility(View.INVISIBLE);
    }


    /**
     * @Description (TODO这里用一句话描述这个方法的作用)
     */
    private void initViews() {
        mirroringButton = (Button) findViewById(R.id.mirroringButton);
        sharingButton = (Button) findViewById(R.id.sharingButton);
        myAppButton = (Button) findViewById(R.id.myAppButton);
        browserButton = (Button) findViewById(R.id.browserButton);
        multimediaButton = (Button) findViewById(R.id.multimediaButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        batteryImageView = (ImageView) findViewById(R.id.batteryImageView);
        wifiImageView = (ImageView) findViewById(R.id.wifiImageView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        volumeImageView = (ImageView) findViewById(R.id.volumeImageView);
        muteImageView = (Button) findViewById(R.id.muteImageView);
        seekBar = (VerticalSeekBar) findViewById(R.id.seekBar);
        volumePlaneLayout = (LinearLayout) findViewById(R.id.volumePlaneLayout);
        volumeLayout = (RelativeLayout) findViewById(R.id.volumeLayout);

        mirroringButton.setOnClickListener(this);
        sharingButton.setOnClickListener(this);
        myAppButton.setOnClickListener(this);
        browserButton.setOnClickListener(this);
        multimediaButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        mirroringButton.setOnHoverListener(this);
        sharingButton.setOnHoverListener(this);
        myAppButton.setOnHoverListener(this);
        browserButton.setOnHoverListener(this);
        multimediaButton.setOnHoverListener(this);
        settingsButton.setOnHoverListener(this);

//        mirroringButton.setOnTouchListener(this);
//        sharingButton.setOnTouchListener(this);
//        myAppButton.setOnTouchListener(this);
//        browserButton.setOnTouchListener(this);
//        multimediaButton.setOnTouchListener(this);
//        settingsButton.setOnTouchListener(this);

        muteImageView.setOnClickListener(this);

        seekBar.setOnSeekChangedListener(this);
    }

    private int convertVolumeValueToHeight(int volumeValue) {
        return (int) ((volumeValue * 1.0f / maxVolumeValue) * MAX_VOLUME_HEIGHT);
    }

    private int convertHeightToVolumeValue(int height) {
        return Math.round((height * 1.0f / MAX_VOLUME_HEIGHT) * maxVolumeValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* (非 Javadoc)
     * Description:
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        try {
            Intent mIntent = new Intent();
            ComponentName comp = null;
            switch (v.getId()) {
                case R.id.mirroringButton:
                    comp = new ComponentName("com.rockchip.wfd", "com.rockchip.wfd.WifiDisplayActivity");
                    break;
                case R.id.sharingButton:
                    comp = new ComponentName("com.waxrain.airplaydmr2", "com.waxrain.ui.WaxPlayerSetting");
                    break;
                case R.id.myAppButton:

//                    Intent it = new Intent(this, ListOfAppsActivity.class);
//                    it.putExtra("from", "首页");
//                    startActivity(it);
//                    return;
                    comp = new ComponentName("com.example.applist", "com.example.applist.ListOfAppsActivity");
                    break;
                case R.id.browserButton:
                    comp = new ComponentName("com.android.browser", "com.android.browser.BrowserActivity");
                    break;
                case R.id.multimediaButton:
                    comp = new ComponentName("kantv.filemanager", "kantv.filemanager.activity.MainActivity");
                    break;
                case R.id.settingsButton:
                    comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
                    break;

                case R.id.muteImageView:
                    currentVolumeValue = mAudioManager.getStreamVolume(VOLUME_TYPE);
                    Log.d(TAG, "max : " + maxVolumeValue + " current : " + currentVolumeValue);
                    if (currentVolumeValue > 0) { // 非静音
                        currentVolumeValue = 0;
                    } else { // 静音，设置为上次声音
                        if (lastVolumeValue <= 0) {
                            lastVolumeValue = 1;
                            int height = convertVolumeValueToHeight(lastVolumeValue);
                            seekBar.setProgress(height);
                        }
                        currentVolumeValue = lastVolumeValue;
                    }
                    setVolumeViewByVolume(currentVolumeValue);
                    mAudioManager.setStreamVolume(VOLUME_TYPE, currentVolumeValue, 0);
                    //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) volumeImageView.getLayoutParams();
                    //params.height = convertVolumeValueToHeight(lastVolumeValue);
                    //volumeImageView.setLayoutParams(params);
                    return;
            }
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.setComponent(comp);
            mIntent.setAction("android.intent.action.VIEW");
            startActivity(mIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "找不到该应用", Toast.LENGTH_SHORT).show();
        }
    }

    private static final float SCALE = (float) ((120 * 1.0f / 160) * (1.0f + 0.32));

    /* (非 Javadoc)
     * Description:
     * @see android.view.View.OnHoverListener#onHover(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onHover(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
            RelativeLayout.LayoutParams params = null;
            params = (LayoutParams) v.getLayoutParams();
//            params.width = (int) (169 * SCALE);
//            params.height = (int) (160 * SCALE);
            params.width = 169;
            params.height = 141;
            v.setLayoutParams(params);
        } else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
            RelativeLayout.LayoutParams params = null;
            params = (LayoutParams) v.getLayoutParams();
//            params.width = (int) (148 * SCALE);
//            params.height = (int) (124 * SCALE);
            params.width = 148;
            params.height = 124;
            v.setLayoutParams(params);
        }
        return false;
    }

    @Override
    public void onProgressChanged(int progress) {
        Log.i(TAG, "progress:" + progress);
        hideVolumePlaneHandler.removeMessages(0);
        hideVolumePlaneHandler.sendEmptyMessageDelayed(0, 3000);
        volumeLayout.setVisibility(progress > 0 ? View.VISIBLE : View.INVISIBLE);
        int volumeValue = convertHeightToVolumeValue(progress);
        setVolumeViewByVolume(volumeValue);
    }

    @Override
    public void onTrackingEnd(int progress) {
        hideVolumePlaneHandler.removeMessages(0);
        hideVolumePlaneHandler.sendEmptyMessageDelayed(0, 3000);
        currentVolumeValue = convertHeightToVolumeValue(progress);
        lastVolumeValue = currentVolumeValue;
        Log.i(TAG, "volume:" + currentVolumeValue + ", height:" + progress);
        mAudioManager.setStreamVolume(VOLUME_TYPE, currentVolumeValue, 0);
        setVolumeViewByVolume(currentVolumeValue);

    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        currentVolumeValue = convertHeightToVolumeValue(progress);
//        Log.i(TAG, "onProgressChanged, progress:" + progress + ",volume:" + currentVolumeValue);
//        lastVolumeValue = currentVolumeValue;
//        mAudioManager.setStreamVolume(VOLUME_TYPE, currentVolumeValue, 0);
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        Log.i(TAG, "onStartTrackingTouch");
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        Log.i(TAG, "onStopTrackingTouch");
//    }

    /**
     * 广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {

                int status=intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                if(status==BatteryManager.BATTERY_STATUS_CHARGING) {
                    batteryImageView.setBackgroundResource(R.drawable.icon_battery_charge);
                } else {
                    //获取当前电量
                    int level = intent.getIntExtra("level", 0);
                    //电量的总刻度
                    int scale = intent.getIntExtra("scale", 100);
                    //把它转成百分比
                    //tv.setText("电池电量为"+((level*100)/scale)+"%");
                    int value = (level*100)/scale;
                    if (value < 10) {
                        batteryImageView.setBackgroundResource(R.drawable.icon_battery_no);
                    } else {
                        batteryImageView.setBackgroundResource(R.drawable.icon_battery_full);
                    }
                }
            }
        }

    }

    // 声明wifi消息处理过程
    private BroadcastReceiver wifiIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifi_state = intent.getIntExtra("wifi_state", 0);
            int level = Math.abs(((WifiManager)getSystemService(WIFI_SERVICE)).getConnectionInfo().getRssi());
            Log.i(TAG, "1111:" + level);
            switch (wifi_state) {
                case WifiManager.WIFI_STATE_DISABLING:
                    Log.i(TAG, "1111:" + WifiManager.WIFI_STATE_DISABLING);
                    wifiImageView.setImageResource(R.drawable.wifi_sel);
                    wifiImageView.setImageLevel(level);
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.i(TAG, "2222:" + WifiManager.WIFI_STATE_DISABLED);
                    wifiImageView.setImageResource(R.drawable.wifi_sel);
                    wifiImageView.setImageLevel(level);
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    wifiImageView.setImageResource(R.drawable.wifi_sel);
                    wifiImageView.setImageLevel(level);
                    Log.i(TAG, "33333:" + WifiManager.WIFI_STATE_ENABLING);
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.i(TAG, "4444:" + WifiManager.WIFI_STATE_ENABLED);
                    wifiImageView.setImageResource(R.drawable.wifi_sel);
                    wifiImageView.setImageLevel(level);
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    Log.i(TAG, "5555:" + WifiManager.WIFI_STATE_UNKNOWN);
                    wifiImageView.setImageResource(R.drawable.wifi_sel);
                    wifiImageView.setImageLevel(level);
                    break;
            }
        }
    };

    private void setVolumeViewByVolume(int volume) {
        if (volume > 0) {
            seekBar.setVisibility(View.VISIBLE);
            muteImageView.setBackgroundResource(R.drawable.icon_mute_off);
        } else {
            seekBar.setVisibility(View.GONE);
            muteImageView.setBackgroundResource(R.drawable.icon_mute_on);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volumePlaneLayout.setVisibility(View.VISIBLE);
            hideVolumePlaneHandler.removeMessages(0);
            hideVolumePlaneHandler.sendEmptyMessageDelayed(0, 3000);
            currentVolumeValue = mAudioManager
                    .getStreamVolume(VOLUME_TYPE);
            lastVolumeValue = currentVolumeValue;
            int height = convertVolumeValueToHeight(currentVolumeValue);
            Log.i(TAG, "volume:" + currentVolumeValue + ", height:" + height);
            setVolumeViewByVolume(currentVolumeValue);
            seekBar.setProgress(height);
        }
        return super.dispatchKeyEvent(event);
    }

    private Handler hideVolumePlaneHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            volumePlaneLayout.setVisibility(View.INVISIBLE);
        }
    };
}
