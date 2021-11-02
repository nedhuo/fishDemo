package com.nedhuo.custom.circle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Random;

/**
 * 背景固定
 * 获取所有 view
 * 给他安排固定位置
 */
public class RoundCircleLayout extends FrameLayout {
    private final String TAG = "RoundCircleLayout";
    private final Context mContext;

    private List<ImageView> mPendingImageList;
    private PointF mCenterPointF;
    private int mHeight;
    private int mWidth;
    private Random mRandom;

    public RoundCircleLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public RoundCircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RoundCircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //不需要重写 给其固定宽高
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        Log.i(TAG, "left=" + left + ";"
                + "top=" + top + ";"
                + "right=" + right + ";"
                + "bottom=" + bottom);

        mHeight = bottom - top;
        mWidth = right - left;

        mCenterPointF.y = (bottom - top) / 2;
        mCenterPointF.x = (right - left) / 2;

        super.onLayout(changed, left, top, right, right);
        //父容器的宽、高
//        int centerX = getWidth() / 2;
//        int centerY = getHeight() / 2;

        //
//        if (mPendingImageList != null & mPendingImageList.size() > 0) {
//            for (ImageView imageView : mPendingImageList) {
//                PointF pointF = calculatePoint(new PointF(getWidth() / 2, getHeight() / 2), 300);
//
//            }
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWidth = getWidth();
        mHeight = getHeight();

        super.onDraw(canvas);
    }

    private void init() {
        mRandom = new Random();
        mCenterPointF = new PointF();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(new RoundCircleDrawable(mContext));
        }
    }

    public void addView(ImageView view) {
        super.addView(view);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        //随机宽高


        layoutParams.width = 20;
        layoutParams.height = 20;
        view.setLayoutParams(layoutParams);
        int i = layoutParams.height / 2;//宽高的一半 需要拿计算出的位置 与 该值 计算出最终位置

        //控制随机线
        int radius = (mRandom.nextInt(3) + 2) * 100;//200->400
        PointF pointF = calculatePoint(mCenterPointF, radius);

        //设置位置
        float mOffsetY = mCenterPointF.y - pointF.y + i;
        float mOffsetX = mCenterPointF.x - pointF.x + i;
        view.setY(pointF.y - i);
        view.setX(pointF.x - i);


        int line = (mRandom.nextInt(6) + 3) * 10;//30->80
        float percent = line / 20f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", percent);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", percent);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY);
        animSet.setDuration(1000);
        animSet.start();

        Log.i(TAG, "mOffsetY=" + mOffsetY + "mOffsetX=" + mOffsetX);
        Log.i(TAG, "setX=" + (pointF.x - i) + "setY=" + (pointF.x - i));
        Log.i(TAG, "position=" + radius + "size=" + line);

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                RotateAnimation rotation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

//                view.setPivotX(0.5f);//是指以view为坐标系（mOffsetX,mOffsetY）位置
//                view.setPivotY(0.5f);
                rotation.setRepeatCount(ValueAnimator.INFINITE);
                rotation.setInterpolator(new LinearInterpolator());
                rotation.setDuration(3000);
                view.setAnimation(rotation);
                //rotation.start();
            }
        });


    }

    /**
     * 计算相对于圆心的起始位置
     *
     * @param startPoint 起始点
     * @param radius     半径
     */
    private PointF calculatePoint(PointF startPoint, float radius) {
        double angle = calculateStartAngle();//计算角度
        float deltax = (float) (Math.cos(Math.toRadians(angle)) * radius);
        //向左平移π，相当于取反 Android坐标与数学坐标中的y值是相反的，此处 -180 相当于取反
        float deltay = (float) (Math.sin(Math.toRadians(angle - 180)) * radius);

        return new PointF(startPoint.x + deltax, startPoint.y + deltay);
    }

    private double calculateStartAngle() {
        return mRandom.nextInt(360);
    }

}
