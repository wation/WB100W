/******************************************************************
 *
 *    Copyright (c) 1997-2016 MICHOI Group
 *    http://www.michoi.com/
 *
 *    Package:     com.example.wb100w.ui.apps
 *
 *    Filename:    UiSetup.java
 *
 *    Description: TODO(用一句话描述该文件做什么)
 *
 *    Copyright:   Copyright (c) 1997-2016
 *
 *    Company:     MICHOI Group
 *
 *    @author:     Wation.Haliyoo
 *
 *    @version:    1.0.0
 *
 *    Create at:   2016-11-10 下午7:22:25
 *
 *    Revision:
 *
 *    2016-11-10 下午7:22:25
 *        - first revision
 *
 *****************************************************************/
package com.example.applist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @ClassName UiSetup
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author Wation.Haliyoo
 * @Date 2016-11-10 下午7:22:25
 * @version 1.0.0
 */
public class UiSetup {

    public static boolean setBackTitle(final Activity ct, String title)
    {
        Intent intent = ct.getIntent();
        String backTitle = ct.getString(R.string.app_name);
        if (intent.hasExtra("from")) {
            backTitle = intent.getStringExtra("from");
        }
        TextView textViewBackTitle = (TextView) ct.findViewById(R.id.textViewBackTitle);
        textViewBackTitle.setText(backTitle);
        TextView textViewTitle = (TextView) ct.findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
        LinearLayout layoutBack = (LinearLayout) ct.findViewById(R.id.layoutBack);
        layoutBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ct.finish();
            }
        });

        return true;
    }
}
