package com.nedhuo.custom.fish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

public class FishRelativeLayout extends RelativeLayout {

    private Paint mPaint;
    private ImageView mIv_fish;
    private FishDrawable mFishDrawable;
    //触摸点的X,Y坐标
    private float mTouch_X;
    private float mTouch_Y;
    //
    private float ripple;
    private int alpha = 100;
    private Path mPath;
    private PointF controlPoint;

    public FishRelativeLayout(Context context) {
        this(context, null);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //viewGroup默认不执行onDraw()方法
        setWillNotDraw(false); //执行onDraw方法
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);//线段类型
        mPaint.setStrokeWidth(8);

        mIv_fish = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mIv_fish.setLayoutParams(layoutParams);
        mFishDrawable = new FishDrawable();
        mIv_fish.setImageDrawable(mFishDrawable);
        addView(mIv_fish);
    }

    /**
     * 触摸需要实现涟漪不断扩大不断淡化的效果，
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouch_X = event.getX();
        mTouch_Y = event.getY();

        /*
         * 设置涟漪的动画效果
         * 此处的ripple针对的并不是ripple这个属性，而是看类中有没有setRipple这个方法
         * */
        ObjectAnimator.ofFloat(this, "ripple", 0, 1f)
                .setDuration(1000)
                .start();

        /*
         * 鱼的移动轨迹是一个三阶贝塞尔曲线，因此需要确定四个点
         * 鱼的重心点 : 起始点
         * 点击位置   ：终点
         * 鱼头圆心   ：控制点1
         * 控制点2： 第四个点是随便找的，此处取 起始点分别与控制点1，终点连线所形成的夹角的一半
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            move();
        }


        return super.onTouchEvent(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void move() {
        //鱼的重心：相对ImageView坐标
        PointF middlePoint = mFishDrawable.getMiddlePoint();
        //1. 起始点坐标   计算相对于布局的绝对坐标
        PointF fishMiddle = new PointF(mIv_fish.getX() + middlePoint.x, mIv_fish.getY() + middlePoint.y);
        //2. 第一个控制点坐标（鱼头坐标）
        PointF headPoint = new PointF(mIv_fish.getX() + mFishDrawable.getHeadPoint().x,
                mIv_fish.getY() + mFishDrawable.getHeadPoint().y);
        //3. 结束点坐标
        PointF endPoint = new PointF(mTouch_X, mTouch_Y);
        /*
         * 4. 计算控制点2的坐标
         * 需要计算夹角大小，从而判断鱼头旋转方向
         * 终点连线所形成的夹角的一半
         * */
        float angle = caculateAngle(fishMiddle, headPoint, endPoint) / 2;
        //这个参数决定了鱼的转向
        float delta = caculateAngle(fishMiddle, new PointF(fishMiddle.x + 1, fishMiddle.y), headPoint);

        controlPoint = mFishDrawable.caculatePoint(fishMiddle,
                mFishDrawable.getHeadRedius() * 1.6f, angle + delta);


        mPath.reset();
        mPath.moveTo(fishMiddle.x - middlePoint.x, fishMiddle.y - middlePoint.y);
        mPath.cubicTo(headPoint.x - middlePoint.x, headPoint.y - middlePoint.y,
                controlPoint.x - middlePoint.x, controlPoint.y - middlePoint.y,
                mTouch_X - middlePoint.x, mTouch_Y - middlePoint.y);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mIv_fish,
                "x", "y", mPath)
                .setDuration(2000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFishDrawable.setFrequence(1.0f);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFishDrawable.setFrequence(2.0f);
            }
        });

        objectAnimator.start();

        //控制鱼头旋转方向 可以确定的是鱼头方向跟随所在位置的切线
        //PathMeasure 对Path操作的一个类
        final PathMeasure pathMeasure = new PathMeasure(mPath, false);
        final float[] tan = new float[2];
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //执行了整个周期的百分之多少
                float fraction = animation.getAnimatedFraction();
                //计算path相应的位置和切线 pos不为null,返回位置 tan 不为null,返回正切
                pathMeasure.getPosTan(pathMeasure.getLength() * fraction, null, tan);
                //鱼的朝向 由于android坐标系与数学坐标系的问题，因此y轴需要取反
                float angle = (float) Math.toDegrees(Math.atan2(-tan[1], tan[0]));
                mFishDrawable.setFishMainAngle(angle);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAlpha(alpha);
        //半径是动态的
        canvas.drawCircle(mTouch_X, mTouch_Y, ripple * 150, mPaint);
        invalidate();
    }


    /**
     * 通过三个点的坐标结合向量公式计算夹角
     * 客户获得两个向量长度及夹角
     * cosAOB=(ab的内积)/|a||b|
     * 这一段需要重新巩固视频
     */
    private float caculateAngle(PointF O, PointF A, PointF B) {
        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
        float OALength = (float) Math.sqrt(((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y)));
        float OBLength = (float) Math.sqrt(((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y)));
        float cosAOB = AOB / (OALength * OBLength);
        //AOB的角度值
        float angleAOB = (float) Math.toDegrees(Math.acos(cosAOB));
        //AB连线与X的夹角的tan值-OB与X轴的夹角的tan值
        float diretion = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x);
        if (diretion == 0) {
            if (AOB >= 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (diretion > 0) {
                return -angleAOB;
            } else {
                return angleAOB;
            }
        }

//        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
//        float OALength = (float) Math.sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y));
//        // OB 的长度
//        float OBLength = (float) Math.sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y));
//        float cosAOB = AOB / (OALength * OBLength);
//
//        // 反余弦
//        float angleAOB = (float) Math.toDegrees(Math.acos(cosAOB));
//
//        // AB连线与X的夹角的tan值 - OB与x轴的夹角的tan值
//        float direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x);
//
//        if (direction == 0) {
//            if (AOB >= 0) {
//                return 0;
//            } else {
//                return 180;
//            }
//        } else {
//            if (direction > 0) {
//                return -angleAOB;
//            } else {
//                return angleAOB;
//            }
//        }

    }


    //ObjectAnimator作用在自定义View属性上时需要生成set get方法
    public float getRipple() {
        return ripple;
    }

    public void setRipple(float ripple) {
        //ripple是一个动态的0-1的值，alpha成为动态的100-0的值
        alpha = (int) (100 * (1 - ripple));
        this.ripple = ripple;
    }
}
