package com.nedhuo.custom.anim.fish;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 实现思路
 * 先设置鱼的中心点坐标,以及鱼的中心角度
 * 根据中心点坐标计算鱼头摇摆时的中心点，鱼尾摇摆时的中心点等所有有摆动幅度的中心点
 * 再根据鱼的中心角度计算鱼尾摇摆时以及鱼头摇摆时的角度
 * 再根据中心点去绘制图形
 */

/**
 * 要保持鱼头与鱼尾的摇摆程度不同，摇摆频率不同
 */
public class FishDrawable extends Drawable {

    private Paint mPaint;
    private Path mPath;

    /*
    * 默认绘制的透明度
    * */
    private final int OTHER_ALPHA = 110;
    /*
    * 鱼的重心
    * 通过鱼的重心确定鱼的位置，并根据鱼每个部位离重心的长度确定其所在的位置（还需要一个角度方向参数）
    * */
    private PointF middlePoint;
    /*
    * 鱼的朝向角度
    * 通过鱼每个部位相对于朝向的角度来确定鱼部位的方向（结合部位离重心的长度）便可以确定位置
    * */
    private float fishMainAngle = 90;

    /*
    * 控制摆动速率的属性
    * 鱼在游动的时候摆尾会加快
    * 静止的时候摆尾会放缓
    * */
    private float frequence = 1.0f;
    /**
     * 鱼的长度值
     */

    //绘制鱼头的半径
    private float HEAD_RADIUS = 20;
    private float BODY_LENGTH = 3.2F * HEAD_RADIUS;

    //中心点到鱼鳍中心点的长度
    private float FIND_FINS_LENGTH = 0.9F * HEAD_RADIUS;
    //鱼鳍的长度
    private float FINS_LENGTH = 1.3F * HEAD_RADIUS;

    //尾部三个节点中大圆半径
    private float BIG_RADIUS = 0.7F * HEAD_RADIUS;
    //尾部三个节点中中圆半径
    private float MIDDLE_RADIUS = 0.6f * BIG_RADIUS;
    //尾部三个节点中小圆半径
    private float SMALL_RADIUS = 0.4F * MIDDLE_RADIUS;
    //中心点到尾部中圆圆心的线长
    private float FIND_MIDDLE_CIRCLE_LENGTH = BIG_RADIUS * (0.6f + 1);
    private float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_RADIUS * (0.4f + 2.7F);
    //寻找大三角形底边中心点的线长
    private final float FIND_TRANGLE_LENGTH = MIDDLE_RADIUS * 2.7f;

    public float getmCurrentValue() {
        return mCurrentValue;
    }

    public void setmCurrentValue(float mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
    }

    /**
     * 通过改值控制各个部位的动画
     * 通过改值既要改变摇摆频率也要控制摇摆幅度
     * (fishAngle + Math.sin(Math.toRadians(mCurrentValue * 2)) * 25);
     * mCurrentValue * 2 可以控制摇摆频率 2倍频率 要控制计算的值是360 的倍数，否则动画会出现偶尔卡顿情况
     * Math.sin(..)) * 25) 控制摇摆幅度 （-1 ` 1） * 25
     */
    private float mCurrentValue;
    private PointF mHeadPoint;


    public FishDrawable() {
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setStyle(Paint.Style.FILL);//画笔类型
        mPaint.setColor(Color.RED);
        mPaint.setARGB(OTHER_ALPHA, 367, 367, 9);

        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);

        //动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //从0到360
                mCurrentValue = (float) animation.getAnimatedValue();
                //重绘
                invalidateSelf();
            }
        });
        valueAnimator.start();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //通过动画值得变化来改变鱼身体的倾斜程度
        /*
        * 当角度* frequence 的时候，角度原本变化值 0 - 360，在此处的变化值相当于 0 - 360 * frequence
        * 又因为此处是求sin值，因此，如果 0-360执行了一次摆尾，那么0-360*frequence便相当于执行了frequence次摆尾
        * 因此frequence决定了摆尾的频率
        *
        * 在sin求值之后再 *2 决定摆动幅度，因为此处得出的直接是一个角度值，代码会根据这个角度值绘制View，
        * 相当于这个角度控制了View的倾斜程度，在动画加持下，就相当于控制了摆动幅度
        * */
        float fishAngle = (float) (fishMainAngle + Math.sin(Math.toRadians(mCurrentValue) * frequence) * 2);
        //鱼头的圆心坐标
        mHeadPoint = caculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(mHeadPoint.x, mHeadPoint.y, HEAD_RADIUS, mPaint);

        //右鱼鳍
        PointF rightFinsPoint = caculatePoint(mHeadPoint, FIND_FINS_LENGTH, fishAngle - 110);
        drawFins(canvas, rightFinsPoint, fishAngle, true);

        //左鱼鳍
        PointF leftFinsPoint = caculatePoint(mHeadPoint, FIND_FINS_LENGTH, fishAngle + 110);
        drawFins(canvas, leftFinsPoint, fishAngle, false);

        //节肢1
        PointF arthropodPoint = caculatePoint(mHeadPoint, BODY_LENGTH, fishAngle - 180);
        PointF upperCenterPoint = drawArthropodPoint(canvas, arthropodPoint, fishAngle);

        //节肢2
        drawArthropodPoint2(canvas, upperCenterPoint, fishAngle);

        //尾巴
        drawTriangel(canvas, upperCenterPoint, fishAngle);
        drawTriangel1(canvas, upperCenterPoint, fishAngle);

        //画身体
        drawBody(canvas, mHeadPoint, arthropodPoint, fishAngle);
    }

    private void drawBody(Canvas canvas, PointF headPoint, PointF upperCenterPoint, float fishAngle) {
        PointF topLeftPoint = caculatePoint(headPoint, HEAD_RADIUS, fishAngle + 90);
        PointF topRightPoint = caculatePoint(headPoint, HEAD_RADIUS, fishAngle - 90);
        PointF bottomLeftPoint = caculatePoint(upperCenterPoint, BIG_RADIUS, fishAngle + 90);
        PointF bottomRightPoint = caculatePoint(upperCenterPoint, BIG_RADIUS, fishAngle - 90);

        //二阶贝塞尔曲线的控制点
        PointF controlLeft = caculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF controlRight = caculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130);
        //绘制
        mPath.reset();
        mPath.moveTo(topLeftPoint.x, topLeftPoint.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, bottomLeftPoint.x, bottomLeftPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.quadTo(controlRight.x, controlRight.y, topRightPoint.x, topRightPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawTriangel1(Canvas canvas, PointF startPoint, float fishAngle) {
        float triangel = (float) (fishAngle + Math.sin(Math.toRadians(mCurrentValue) * frequence) * 30);
        //三角形底边中心坐标
        PointF centerPoint = caculatePoint(startPoint, FIND_TRANGLE_LENGTH - 10, triangel - 180);
        PointF leftPoint = caculatePoint(centerPoint, BIG_RADIUS - 20, triangel + 90);
        PointF rightPoint = caculatePoint(centerPoint, BIG_RADIUS - 20, triangel - 90);

        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x, leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    //绘制尾部三角形
    private void drawTriangel(Canvas canvas, PointF startPoint, float fishAngle) {
        //计算三角形当前角度
        float triangel = (float) (fishAngle + Math.sin(Math.toRadians(mCurrentValue) * frequence) * 30);
        //三角形底边中心坐标
        PointF centerPoint = caculatePoint(startPoint, FIND_TRANGLE_LENGTH, triangel - 180);
        PointF leftPoint = caculatePoint(centerPoint, BIG_RADIUS, triangel + 90);
        PointF rightPoint = caculatePoint(centerPoint, BIG_RADIUS, triangel - 90);

        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x, leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    //绘制节肢2
    private void drawArthropodPoint2(Canvas canvas, PointF bottomCenterPoint, float fishAngle) {
        float arthropodPoint = (float) (fishAngle + Math.sin(Math.toRadians(mCurrentValue) * frequence) * 30);

        //梯形上底长
        PointF upperCenterPoint = caculatePoint(bottomCenterPoint, FIND_MIDDLE_CIRCLE_LENGTH,
                arthropodPoint - 180);
        //梯形四个点
        PointF bottomLeftPoint = caculatePoint(bottomCenterPoint, MIDDLE_RADIUS, arthropodPoint + 90);
        PointF bottomRightPoint = caculatePoint(bottomCenterPoint, MIDDLE_RADIUS, arthropodPoint - 90);
        PointF upperLeftPoint = caculatePoint(upperCenterPoint, SMALL_RADIUS, arthropodPoint + 90);
        PointF upperRightPoint = caculatePoint(upperCenterPoint, SMALL_RADIUS, arthropodPoint - 90);

        //画圆
        //canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, MIDDLE_RADIUS, mPaint);
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, SMALL_RADIUS, mPaint);

        //画梯形 按顺序依次画三条线自动合上第四条
        mPath.reset();
        mPath.moveTo(upperLeftPoint.x, upperLeftPoint.y);
        mPath.lineTo(upperRightPoint.x, upperRightPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.lineTo(bottomLeftPoint.x, bottomLeftPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    //
    private PointF drawArthropodPoint(Canvas canvas, PointF bottomCenterPoint, float fishAngle) {
        //通过动态值计算出来的角度
        float arthropodPoint = (float) (fishAngle + Math.cos(Math.toRadians(mCurrentValue) * frequence) * 20);

        //梯形上底长
        PointF upperCenterPoint = caculatePoint(bottomCenterPoint,
                FIND_MIDDLE_CIRCLE_LENGTH, arthropodPoint - 180);
        //梯形四个点
        PointF bottomLeftPoint = caculatePoint(bottomCenterPoint, BIG_RADIUS, arthropodPoint + 90);
        PointF bottomRightPoint = caculatePoint(bottomCenterPoint, BIG_RADIUS, arthropodPoint - 90);
        PointF upperLeftPoint = caculatePoint(upperCenterPoint, MIDDLE_RADIUS, arthropodPoint + 90);
        PointF upperRightPoint = caculatePoint(upperCenterPoint, MIDDLE_RADIUS, arthropodPoint - 90);

        //画圆
        canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, BIG_RADIUS, mPaint);
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, MIDDLE_RADIUS, mPaint);

        //画梯形 按顺序依次画三条线自动合上第四条
        mPath.reset();
        mPath.moveTo(upperLeftPoint.x, upperLeftPoint.y);
        mPath.lineTo(upperRightPoint.x, upperRightPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.lineTo(bottomLeftPoint.x, bottomLeftPoint.y);
        canvas.drawPath(mPath, mPaint);

        return upperCenterPoint;
    }

    /**
     * 绘制鱼鳍
     * 使用二阶，三阶贝塞尔曲线
     */
    private void drawFins(Canvas canvas, PointF startPoint, float fishAngle, boolean isRight) {
        float controlAngle = 115;
        //鱼鳍的终点---二阶贝赛尔曲线的终点 鱼鳍终点方向垂直向左
        PointF endPoint = caculatePoint(startPoint, FINS_LENGTH, fishAngle - 180);
        //控制点 -----三阶贝赛尔曲线的控制点  这个长度和角度是自己找的，看效果调整
        PointF controlPoint = caculatePoint(startPoint, FINS_LENGTH * 1.8F,
                isRight ? (fishAngle - controlAngle) : (fishAngle + controlAngle));

        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        Log.i("鱼鳍", startPoint.x + "==" + startPoint.y);
        Log.i("鱼鳍", endPoint.x + "==" + endPoint.y);
        Log.i("鱼鳍", controlPoint.x + "==" + controlPoint.y);
        //绘制二阶贝塞尔曲线
        mPath.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * @param startPoint 起始点坐标
     * @param length     要求的点到起始点的长度
     * @param angle      鱼部位当前的朝向角度
     * @return 通过起始点与线段长度，线段方向计算另一落点的坐标
     */
    public PointF caculatePoint(PointF startPoint, float length, float angle) {
        float deltax = (float) (Math.cos(Math.toRadians(angle)) * length);
        //向左平移π，相当于取反 Android坐标与数学坐标中的y值是相反的，此处 -180 相当于取反
        float deltay = (float) (Math.sin(Math.toRadians(angle - 180)) * length);

        return new PointF(startPoint.x + deltax, startPoint.y + deltay);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    //设置wrap_content时固定宽高生效
    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * HEAD_RADIUS);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * HEAD_RADIUS);
    }

    //暴露鱼的中心点
    public PointF getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(PointF middlePoint) {
        this.middlePoint = middlePoint;
    }

    /**
     * todo 可以优化鱼头朝向角度
     *
     * @return 返回鱼头圆心的相对位置
     */
    public PointF getHeadPoint() {
        return mHeadPoint;
    }

    public float getHeadRedius() {
        return HEAD_RADIUS;
    }

    public float getFrequence() {
        return frequence;
    }

    public void setFrequence(float frequence) {
        this.frequence = frequence;
    }

    public float getFishMainAngle() {
        return fishMainAngle;
    }

    public void setFishMainAngle(float fishMainAngle) {
        this.fishMainAngle = fishMainAngle;
    }
}
