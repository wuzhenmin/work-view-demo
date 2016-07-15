package com.kingdee.min.logindemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.kingdee.min.logindemo.R;

/**
 * Created by min on 16-7-14.
 */
public class ColorFulSectorView extends View {

    private int bigRadius;
    private int smallRadius;
    private int mTextSize;
    private int mTextColor;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private RectF mRectF;
    private final int SUM2PARTS = 5;
    private final float BACKROTATE = SUM2PARTS / 2f-1f;
    private final int[] PANEL_COLORS = new int[]{
            Color.rgb(0xd7, 0x35, 0x23), Color.rgb(0xee, 0x4b, 0x39),
            Color.rgb(0xfb, 0x5c, 0x4a), Color.rgb(0xfe, 0x8b, 0x7f), Color.rgb(0xff, 0xaa, 0xa0)};

    final String[] PANEL_LABELS = new String[]{"350", "550", "600", "650", "700", "950"};


    public ColorFulSectorView(Context context) {
        this(context, null);
    }

    public ColorFulSectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorFulSectorView);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.ColorFulSectorView_smallRadius:
                    smallRadius = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ColorFulSectorView_bigRadius:
                    bigRadius = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ColorFulSectorView_mTextColor:
                    mTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ColorFulSectorView_mTextSize:
                    mTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;

            }
        }
        array.recycle();
        mPaint = new Paint();
        mTextPaint = new TextPaint();
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float centreX = getWidth() / 2;
        float centreY = getHeight();
        float mStartAngle = (float) (Math.toDegrees(Math.acos(centreX / smallRadius)) + 180);
        float mSweepAngleSum = (float) (180 - 2 * Math.toDegrees(Math.acos(centreX / smallRadius)));
        float mSweepAngleApart = mSweepAngleSum / SUM2PARTS;

        mRectF.set(centreX - bigRadius, centreY - bigRadius, centreX + bigRadius, centreY + bigRadius);
        for (int i = 0; i < SUM2PARTS; i++) {
            canvas.drawArc(mRectF, mStartAngle, mSweepAngleApart, true, mPaint);
            mPaint.setColor(PANEL_COLORS[i]);
            mStartAngle = mStartAngle + mSweepAngleApart;
        }

        mRectF.set(centreX - smallRadius, centreY - smallRadius, centreX + smallRadius, centreY + smallRadius);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(centreX, centreY, smallRadius, mPaint);

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);
        canvas.drawText("hello", centreX, centreY - bigRadius - mPaint.getTextSize(), mTextPaint);
        canvas.rotate(-BACKROTATE * mSweepAngleApart, centreX, centreY);
        mTextPaint.setColor(mTextColor);
        for (int i = 0; i < PANEL_LABELS.length; i++) {
            canvas.drawText(PANEL_LABELS[i], centreX, centreY - bigRadius - mPaint.getTextSize(), mTextPaint);
            canvas.rotate(mSweepAngleApart, centreX, centreY);
        }


    }
}
