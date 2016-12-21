package com.github.multimedia;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, View.OnHoverListener {
    private static final String TAG = "MainActivity";
    private Button videoButton, musicButton, albumsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }
    private void initData() {
    }


    /**
     * @Description (TODO这里用一句话描述这个方法的作用)
     */
    private void initViews() {
        videoButton = (Button) findViewById(R.id.videoButton);
        musicButton = (Button) findViewById(R.id.musicButton);
        albumsButton = (Button) findViewById(R.id.albumsButton);

        videoButton.setOnClickListener(this);
        musicButton.setOnClickListener(this);
        albumsButton.setOnClickListener(this);

        videoButton.setOnHoverListener(this);
        musicButton.setOnHoverListener(this);
        albumsButton.setOnHoverListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        resetViewsLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void resetViewsLayout() {

        Button[] buttons = new Button[] {videoButton, musicButton, albumsButton};
        for (Button button : buttons) {
            RelativeLayout.LayoutParams params = null;
            params = (RelativeLayout.LayoutParams) button.getLayoutParams();
//            params.width = (int) (148 * SCALE);
//            params.height = (int) (124 * SCALE);
            params.width = 148;
            params.height = 124;
            button.setLayoutParams(params);
        }
    }


    /* (非 Javadoc)
     * Description:
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        try {
            String packageName = "";
            PackageManager pm = getPackageManager();
            switch (v.getId()) {
                case R.id.videoButton:
                    packageName = "android.rk.RockVideoPlayer";
                    break;
                case R.id.musicButton:
                    packageName = "com.android.music";
                    break;
                case R.id.albumsButton:
                    packageName = "com.android.gallery3d";
                    break;
            }
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            startActivity(intent);
        } catch (NullPointerException e) {
            Toast.makeText(this, "找不到该应用", Toast.LENGTH_SHORT).show();
        }
    }

    /* (非 Javadoc)
     * Description:
     * @see android.view.View.OnHoverListener#onHover(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onHover(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
            RelativeLayout.LayoutParams params = null;
            params = (RelativeLayout.LayoutParams) v.getLayoutParams();
//            params.width = (int) (169 * SCALE);
//            params.height = (int) (160 * SCALE);
            params.width = 169;
            params.height = 141;
            v.setLayoutParams(params);
        } else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
            RelativeLayout.LayoutParams params = null;
            params = (RelativeLayout.LayoutParams) v.getLayoutParams();
//            params.width = (int) (148 * SCALE);
//            params.height = (int) (124 * SCALE);
            params.width = 148;
            params.height = 124;
            v.setLayoutParams(params);
        }
        return false;
    }

}
