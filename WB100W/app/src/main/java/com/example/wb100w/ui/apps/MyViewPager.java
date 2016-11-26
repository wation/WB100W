package com.example.wb100w.ui.apps;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;

public class MyViewPager extends ViewPager {

	private Rect mRect = new Rect();//用来记录初始位置
	private int pagerCount = 3;
	private Context context;
	private boolean handleDefault = true;
	private float preX = 0f;
	private static final float RATIO = 0.5f;//摩擦系数
	private static final float SCROLL_WIDTH = 10f;

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//设置总共有多少页
	public void setpagerCount(int pagerCount,Context context) {
		this.pagerCount = pagerCount;
		this.context = context;
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			preX = arg0.getX();//记录起点
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		ListOfAppsActivity la = (ListOfAppsActivity)context;
		switch (arg0.getAction()) {
			case MotionEvent.ACTION_UP:
				la.mHandler.sendEmptyMessageDelayed(2, 1000);//发送消失隐藏小圆点
				//当up时弹回原来的位置，使用动画
				onTouchActionUp();
				break;
			case MotionEvent.ACTION_MOVE:
				if(pagerCount!=1){//如果只有一页就不显示圆点
					la.mHandler.sendEmptyMessage(1);//发送消失显示小圆点
				}

				//当只有一页的时候
				if (pagerCount==1) {
					float nowX = arg0.getX();
					float offset = nowX - preX;
//				preX = nowX;
					if (offset > SCROLL_WIDTH){//手指向右滑动的距离大于设定值
						whetherConditionIsRight(offset);
					} else if (!handleDefault) {//这种情况是已经出现缓冲区域了
						if (getLeft() + (int) (offset * RATIO) >= mRect.left) {
							layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
						}
					}

					if (offset < -SCROLL_WIDTH) {//手指向左滑动的距离大于设定值
						whetherConditionIsRight(offset);
					}else if (!handleDefault) {
						if (getRight() + (int) (offset * RATIO) <= mRect.right) {
							layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
						}
					}

				} else {
					handleDefault = true;
				}

				if (!handleDefault) {
					return true;
				}
				break;

			default:
				break;
		}
		return super.onTouchEvent(arg0);
	}

	private void whetherConditionIsRight(float offset) {
		if (mRect.isEmpty()) {
			mRect.set(getLeft(), getTop(), getRight(), getBottom());
		}
		handleDefault = false;
		layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
	}

	private void onTouchActionUp() {
		if (!mRect.isEmpty()) {
			recoveryPosition();
		}
	}

	private void recoveryPosition() {
		TranslateAnimation ta = null;
		ta = new TranslateAnimation(getLeft(), mRect.left, 0, 0);
		ta.setDuration(300);
		startAnimation(ta);
		layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
		mRect.setEmpty();
		handleDefault = true;
	}

}

