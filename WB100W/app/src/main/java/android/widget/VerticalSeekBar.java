package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by wation on 16/11/26.
 */
public class VerticalSeekBar extends ImageView {
    private static final String TAG = "VerticalSeekBar";
    int progress = 0;
    int maxHeight = 0;
    int minHeight = 0;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(h, w, oldh, oldw);
//    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

//    protected void onDraw(Canvas c) {
//        //将SeekBar转转90度
//        c.rotate(-90);
//        //将旋转后的视图移动回来
//        c.translate(-getHeight(),0);
//        Log.i("getHeight()",getHeight()+"");
//        super.onDraw(c);
//    }

    float lastY = 0;
    boolean dragStart = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                dragStart = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragStart) {
                    float distance = lastY - event.getY();
                    int height = (int) (getHeight() + distance);
                    Log.i(TAG, "y:" + event.getY() + ",distance:" + distance + ", height:" + height);
                    height = height > maxHeight ? maxHeight : height;
                    height = height < minHeight ? minHeight : height;
                    setProgress(height);
                }
                break;
            case MotionEvent.ACTION_UP:
                dragStart = false;
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        progress = progress > maxHeight ? maxHeight : progress;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.height = progress;
        setLayoutParams(params);
    }
}
