package android.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by wation on 16/11/14.
 */
public class ArialBlackTextView extends TextView {
    public ArialBlackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset (context.getAssets() , "ariblk.ttf" );
        setTypeface (face);
    }
}
