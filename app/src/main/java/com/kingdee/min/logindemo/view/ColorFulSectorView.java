package com.kingdee.min.logindemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private float mAngle;
    private float mTriangle;
    private int mTriangleWidth;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private Path mPath;
    private RectF mRectF;
    private final int SUM2PARTS = 5;
    private final float BACKROTATE = SUM2PARTS / 2f;
    private final float INDENTATION = 0.4f;
    private final int[] PANEL_COLORS = new int[]{
            Color.rgb(0xd7, 0x35, 0x23), Color.rgb(0xee, 0x4b, 0x39),
            Color.rgb(0xfb, 0x5c, 0x4a), Color.rgb(0xfe, 0x8b, 0x7f), Color.rgb(0xff, 0xaa, 0xa0)};

    private final String[] PANEL_LABELS = new String[]{"350", "550", "600", "650", "700", "950"};

    private float centreX;
    private float centreY;


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
                case R.styleable.ColorFulSectorView_mTriangle:
                    mTriangle = array.getFloat(attr, 100);
                    break;
                case R.styleable.ColorFulSectorView_mTriangleWidth:
                    mTriangleWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ColorFulSectorView_mTextColor:
                    mTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ColorFulSectorView_mTextSize:
                    mTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ColorFulSectorView_mAngle:
                    mAngle = array.getFloat(attr, 30);
                    break;
                default:
                    break;

            }
        }
        array.recycle();
        mPaint = new Paint();
        mTextPaint = new TextPaint();
        mRectF = new RectF();
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        centreX = getWidth() / 2;
        centreY = getHeight();
        mPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        float mStartAngle = (float) (Math.toDegrees(Math.acos(centreX / smallRadius)) + 180);
        float mSweepAngleSum = (float) (180 - 2 * Math.toDegrees(Math.acos(centreX / smallRadius)));
        float mSweepAngleApart = mSweepAngleSum / SUM2PARTS;
        float flagAngle = mStartAngle;
        mRectF.set(centreX - bigRadius, centreY - bigRadius, centreX + bigRadius, centreY + bigRadius);
        for (int i = 0; i < SUM2PARTS; i++) {
            canvas.drawArc(mRectF, mStartAngle, mSweepAngleApart, true, mPaint);
            mPaint.setColor(PANEL_COLORS[i]);
            mStartAngle = mStartAngle + mSweepAngleApart;
        }

        mRectF.set(centreX - smallRadius, centreY - smallRadius, centreX + smallRadius, centreY + smallRadius);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(centreX, centreY, smallRadius, mPaint);

        drawLabels(canvas, mSweepAngleApart);

        drawTriangle(canvas, flagAngle);


    }

    public void drawTriangle(Canvas canvas, float startAngle) {
        float sweepAngle = startAngle - 180 + 60;
        float widAngle = 20;

        float angleForLeft, angleForTop, angleForRight, radiusForTop;

        float pLeftX = 0, pLeftY = 0, pTopX = 0, pTopY = 0, pRightX = 0, pRightY = 0;

        radiusForTop = smallRadius + 10;
        angleForTop = sweepAngle;
        angleForLeft = sweepAngle - widAngle;
        angleForRight = sweepAngle + widAngle;

        if (sweepAngle > 90) {
            angleForTop = 180 - sweepAngle;
            pTopX = (float) (centreX + radiusForTop * Math.cos(angleForTop));
        } else {
            pTopX = (float) (centreX - radiusForTop * Math.cos(angleForTop));
        }

        if (angleForLeft > 90) {
            angleForLeft = 180 - angleForLeft;
            pLeftX = (float) (centreX + smallRadius * Math.cos(angleForLeft));
        } else {
            pLeftX = (float) (centreX - smallRadius * Math.cos(angleForLeft));
        }

        if (angleForRight > 90) {
            angleForRight = 180 - angleForRight;
            pRightX = (float) (centreX + smallRadius * Math.cos(angleForRight));
        } else {
            pRightX = (float) (centreX - smallRadius * Math.cos(angleForRight));
        }

        pLeftY = centreY + (float) (smallRadius * Math.sin(angleForLeft));
        pTopY = centreY + (float) (radiusForTop * Math.sin(angleForTop));
        pRightY = centreY + (float) (smallRadius * Math.sin(angleForRight));

        mPath.moveTo(pLeftX, pLeftY);
        mPath.lineTo(pTopX, pTopY);
        mPath.lineTo(pRightX, pRightY);
        mPath.close();

        mPaint.setColor(Color.GREEN);
        canvas.drawPath(mPath, mPaint);

    }

    private void drawLabels(Canvas canvas, float mSweepAngleApart) {
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);
        canvas.drawText("hello", centreX, centreY - bigRadius - mPaint.getTextSize(), mTextPaint);
        canvas.rotate(-BACKROTATE * mSweepAngleApart, centreX, centreY);
        mTextPaint.setColor(mTextColor);
        for (int i = 0; i < PANEL_LABELS.length; i++) {
            float halfTextLength = mTextPaint.measureText(PANEL_LABELS[i]) / 2f;
            if (i == 0) {
                canvas.rotate(mSweepAngleApart * INDENTATION, centreX, centreY);
                canvas.drawText(PANEL_LABELS[i], centreX - halfTextLength, centreY - bigRadius - mPaint.getTextSize(), mTextPaint);
            } else if (i == PANEL_LABELS.length - 1) {
                canvas.rotate(mSweepAngleApart * (1 - INDENTATION), centreX, centreY);
                canvas.drawText(PANEL_LABELS[i], centreX - halfTextLength, centreY - bigRadius - mPaint.getTextSize(), mTextPaint);
            } else if (i == 1) {
                canvas.rotate(mSweepAngleApart * (1 - INDENTATION), centreX, centreY);
                canvas.drawText(PANEL_LABELS[i], centreX - halfTextLength, centreY - bigRadius - mPaint.getTextSize(), mTextPaint);
            } else {
                canvas.rotate(mSweepAngleApart, centreX, centreY);
                canvas.drawText(PANEL_LABELS[i], centreX - halfTextLength, centreY - bigRadius - mPaint.getTextSize(), mTextPaint);
            }

        }
    }


}
