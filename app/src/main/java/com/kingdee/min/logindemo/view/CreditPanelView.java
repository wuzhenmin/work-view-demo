package com.kingdee.min.logindemo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.kingdee.min.logindemo.R;
import com.kingdee.min.logindemo.util.DisplayUtil;

/**
 * @author Shang Enliang .
 */
public class CreditPanelView extends View {

    protected Context mContext;
    private static final int PANEL_SPLIT_NUM = 6;
    private final int[] PANEL_COLORS = new int[]{
            Color.rgb(0xd7, 0x35, 0x23), Color.rgb(0xee, 0x4b, 0x39),
            Color.rgb(0xfb, 0x5c, 0x4a), Color.rgb(0xfe, 0x8b, 0x7f), Color.rgb(0xff, 0xaa,0xa0)};
    private final String[] PANEL_LABELS = new String[]{"350", "550", "600", "650", "700","950"};
    private final int PANEL_START_SCORE = 350;
    private final int PANEL_VISUAL_START_SCORE = 365;
    private final int PANEL_VISUAL_END_SCORE = 935;
    private final int PANEL_END_SCORE = 950;
    private final double FIRST_ARC_SCALE = 1.1;
    private final double LAST_ARC_SCALE = 1.2;
    private static final float INDICATOR_HEIGHT = 5f;

    private int mWidth;
    private float mOuterRadius = 100;
    private float mInnerRadius = 90;
    private float mLabelTextSize = 12;
    private float mLabelTextMargin = 8;
    private int mIndicatorPosition = 365;
    private float mIndicatorRotateDegree;

    private Path mIndicatorPath = new Path();
    private RectF mOuterRectF = new RectF();
    private RectF mInnerRectF = new RectF();

    private TextPaint mTextPaint;
    private Paint mPanelPaint;

    private boolean mMeasured = false;
    private double mTotalArc;
    private double mTotalDegree;
    private double mSectionDegree;
    private float mCenterX;
    private float mCenterY;
    private float mDensity;
    private int mAnimDuration = 1500;
    private boolean mIsPlayingAnim = false;
    private ValueAnimator mAnimator;
    private IndicatorAnimEndListener mIndicatorAnimEndListener;

    private interface IndicatorAnimEndListener{
        void onIndicatorAnimEnd();
    }


    public CreditPanelView(Context context) {
        super(context);
        init(null, 0);
    }

    public CreditPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public CreditPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }


    public void toggleIndicatorValue(int score) {
        // 设置了15个值的视觉偏移量,即视觉开始点在350+15分,结束点在950-15分
        if(score <= PANEL_VISUAL_START_SCORE){
            mIndicatorPosition = PANEL_VISUAL_START_SCORE;
            mIndicatorRotateDegree = indicatorValue2Degree(PANEL_VISUAL_START_SCORE);
            invalidate();
            return;
        }else if(score > PANEL_VISUAL_END_SCORE) {
            score = PANEL_VISUAL_END_SCORE;
        }

        if(mIsPlayingAnim && mAnimator != null) {
            final int finalScore = score;
            mIndicatorAnimEndListener = new IndicatorAnimEndListener() {
                @Override
                public void onIndicatorAnimEnd() {
                    playAnim(finalScore);
                }
            };

        }else {
            playAnim(score);
        }
    }

    public void setIndicatorValue(int score) {

    }

    public void setAnimDuration(int duration){
        this.mAnimDuration = duration;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //第一段弧
        double startAngle =(180 + (180 - mTotalDegree) / 2);
        double sweepAngle = FIRST_ARC_SCALE * mSectionDegree;
        mPanelPaint.setColor(PANEL_COLORS[0]);
        canvas.drawArc(mOuterRectF, (float) startAngle, (float) sweepAngle, true, mPanelPaint);

        //中三段
        startAngle += FIRST_ARC_SCALE * mSectionDegree;
        sweepAngle = mSectionDegree;
        for(int i=0; i< 3; i++) {
            mPanelPaint.setColor(PANEL_COLORS[1+i]);
            canvas.drawArc(mOuterRectF, (float)startAngle, (float)sweepAngle, true, mPanelPaint);
            startAngle += mSectionDegree;
        }

        //最后一段
        sweepAngle = LAST_ARC_SCALE * mSectionDegree;
        mPanelPaint.setColor(PANEL_COLORS[4]);
        canvas.drawArc(mOuterRectF, (float)startAngle, (float)sweepAngle, true, mPanelPaint);

        //画数字刻度
        drawLabels(canvas);

        //画指针
        drawIndicator(canvas);

        //最后把表盘下方填充成白色背景
        canvas.drawCircle(mCenterX, mCenterY, mInnerRadius, mPanelPaint);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CreditPanelView, defStyle, 0);
        mOuterRadius = a.getDimension(R.styleable.CreditPanelView_outerRadius, mOuterRadius);
        mInnerRadius = a.getDimension(R.styleable.CreditPanelView_innerRadius, mInnerRadius);
        mLabelTextSize = a.getDimension(R.styleable.CreditPanelView_labelTextSize, mLabelTextSize);
        mLabelTextMargin = a.getDimension(R.styleable.CreditPanelView_labelTextMargin, mLabelTextMargin);
        a.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mLabelTextSize);
        mTextPaint.setColor(Color.rgb(0xcd, 0x3b, 0x1c));

        mPanelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPanelPaint.setColor(Color.RED);
        mPanelPaint.setStyle(Paint.Style.FILL);

        mDensity = getResources().getDisplayMetrics().density;
    }

    private void drawLabels(Canvas canvas) {
        int drawingIndex;//表盘刻度部分
        for(drawingIndex = 0; drawingIndex < PANEL_SPLIT_NUM; drawingIndex++){
            canvas.save();
            double rotateDegree = 0;
            if(drawingIndex == 0){//第一个和最后一个要偏一些,否则会有部分画到屏幕外面了
                rotateDegree = -(mTotalDegree / 2) + 5;
            }else if(drawingIndex == PANEL_SPLIT_NUM - 1) {
                rotateDegree = mTotalDegree /2 - 5;
            }else {
                rotateDegree = -1.55 * mSectionDegree + (drawingIndex-1) * mSectionDegree;
            }
            canvas.rotate((float) rotateDegree, mCenterX, mCenterY);
            canvas.drawText(PANEL_LABELS[drawingIndex], mWidth/2, mLabelTextSize, mTextPaint);
            canvas.restore();
        }
    }

    private float indicatorValue2Degree(int indicatorValue){
        //表盘指针部分
        double indicatorRoteDegree;
        if(indicatorValue >= 350 && indicatorValue < 550){
            indicatorRoteDegree = -mTotalDegree/2 + (double) (indicatorValue - PANEL_START_SCORE)/(550-PANEL_START_SCORE) * FIRST_ARC_SCALE*mSectionDegree;
        }else if(indicatorValue >= 550 && indicatorValue < 700) {
            indicatorRoteDegree = (indicatorValue - 627.5)/(700 - 550) * 3 * mSectionDegree;//627.5 是表盘正中值
        }else if(indicatorValue >=700 && indicatorValue <= 950) {
            indicatorRoteDegree = mTotalDegree/2 - (double) (PANEL_END_SCORE - indicatorValue)/(PANEL_END_SCORE - 700) * LAST_ARC_SCALE * mSectionDegree;
        }else {
            return (float) (-mTotalDegree/2);
        }
        return (float) indicatorRoteDegree;
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        canvas.rotate(mIndicatorRotateDegree, mCenterX, mCenterY);
        mPanelPaint.setColor(Color.WHITE);
        canvas.drawPath(mIndicatorPath, mPanelPaint);
        canvas.restore();
    }


    private void playAnim(int score) {
        double targetIndicatorRotateDegree = indicatorValue2Degree(score);
        mAnimator = ValueAnimator.ofFloat(mIndicatorRotateDegree, (float) targetIndicatorRotateDegree).setDuration(mAnimDuration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIndicatorRotateDegree = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsPlayingAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mIndicatorAnimEndListener != null) {
                    mIndicatorAnimEndListener.onIndicatorAnimEnd();
                    mIndicatorAnimEndListener = null;
                }else {
                    mIsPlayingAnim = false;
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsPlayingAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    public void setIndicatorPosition(int position){
        mIndicatorPosition = position;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }else {
            mWidth = (int) DisplayUtil.dp2px(200);
        }

        int mHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }else {
            mHeight = (int) DisplayUtil.dp2px(200);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(!mMeasured){
            measure();
            mIndicatorRotateDegree = indicatorValue2Degree(mIndicatorPosition);
            mMeasured = true;
        }
    }

    /**
     * 有了尺寸,把可以算的各种值都算出来
     */
    private void measure() {
        float halfWidth = mWidth / 2f;
        //有效区域弧度数
        mTotalArc = Math.asin(halfWidth / mInnerRadius) * 2;
        mTotalDegree  = Math.toDegrees(mTotalArc);

        //每一块区域的弧度数,分成五段,以中间三段为基准x,分别为1.1x,x,x,x,1.2x
        mSectionDegree = mTotalDegree / (LAST_ARC_SCALE + 3 + FIRST_ARC_SCALE);

        mIndicatorRotateDegree = indicatorValue2Degree(PANEL_VISUAL_START_SCORE);

        //两圆圆心
        mCenterX = (float) (mInnerRadius * Math.cos((Math.PI - mTotalArc) / 2));
        mCenterY = mLabelTextSize + mLabelTextMargin + mOuterRadius;

        mInnerRectF.set(mCenterX - mInnerRadius, mCenterY - mInnerRadius, mCenterX + mInnerRadius, mCenterY + mInnerRadius);
        mOuterRectF.set(mCenterX - mOuterRadius, mCenterY - mOuterRadius, mCenterX + mOuterRadius, mCenterY + mOuterRadius);

        mIndicatorPath.moveTo(mCenterX, mCenterY - mInnerRadius - INDICATOR_HEIGHT * mDensity);
        mIndicatorPath.lineTo(mCenterX - INDICATOR_HEIGHT * mDensity, mCenterY - mInnerRadius + 2);//画三角形的话会和弧有1,2像素的空隙,这里把三角形底边下移2像素盖掉空隙
        mIndicatorPath.lineTo(mCenterX + INDICATOR_HEIGHT * mDensity, mCenterY - mInnerRadius + 2);
        mIndicatorPath.close();

    }
}
