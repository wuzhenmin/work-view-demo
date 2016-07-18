package com.kingdee.min.logindemo.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.TextView;

public class DisplayUtil {

    public static float dp2px(float dp){
        return dp * getDensity() + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    /**
     * 根据文字长度，缩放大小
     *
     * @param tv
     */
    public static void fixTextSize(TextView tv, int maxLen, float scaleSize) {
        float textLen = tv.getText().toString().length();
        if (textLen == maxLen) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleSize);
        } else if (textLen > maxLen) {
            float newScaleSize = scaleSize - (textLen - maxLen);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, newScaleSize);
        }
    }

    public static void setTextSize(TextView tv, float scaleSize) {
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleSize);
    }

    /**
     * 得到密度0.75,1.0,1.5
     * @return
     */
    public static float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static float getAspectRatio(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        return width / (float) height;
    }

    public static int floatToInt(float f) {
        return (int) Math.round(f + 0.5);
    }
}

