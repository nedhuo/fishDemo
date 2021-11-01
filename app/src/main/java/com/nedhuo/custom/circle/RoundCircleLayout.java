package com.nedhuo.custom.circle;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
        super.onLayout(changed, left, top, right, right);
        Log.i(TAG, "left=" + left + ";"
                + "top=" + top + ";"
                + "right=" + right + ";"
                + "bottom=" + bottom);
        mCenterPointF.y = (bottom - top) / 2;
        mCenterPointF.x = (right - left) / 2;
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


    private void init() {
        mCenterPointF = new PointF();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(new RoundCircleDrawable(mContext));
        }
    }

//    public void addView(ImageView view) {
//       // mPendingImageList.add(view);
//
//        addView(view);
//
//
//    }

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
        Random random = new Random(360);
        return random.nextDouble();
    }

}
