package com.nedhuo.custom.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 1-->5 从小圆到大源
 */
public class RoundCircleView extends View {
    private float radius1 = 200;
    private float radius2 = 300;
    private float radius3 = 400;

    private Paint mPaint;
    private Context mContext;
    private PointF mPointF;
    private Random mRandom;

    private List<ImageView> mPendingImageList = new ArrayList<>();
    private List<ImageView> mRunningImageList = new ArrayList<>();

    public RoundCircleView(Context context) {
        super(context);
        init(context);
    }

    public RoundCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPointF = new PointF();
        mRandom = new Random();

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        if (mPendingImageList != null & mPendingImageList.size() > 0) {
            for (ImageView imageView : mPendingImageList) {
                PointF pointF = calculatePoint(new PointF(getWidth() / 2, getHeight() / 2), 300);

            }
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        //中心点 取布局中心点
        mPointF.x = getWidth() / 2;
        mPointF.y = getHeight() / 2;

        //画圆
        //绘制背景
        drawBackground(canvas);

        //绘制中心image


        super.onDraw(canvas);
    }

//    private void drawCenterImage() {
//
//        Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.xihu));
//
//        bitmap = scaleBitmap(bitmap, 2 * r, 2 * r);
//
//        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//        mPaint.setShader(bitmapShader);
//
//        canvas.drawCircle(r, r, r, paint);
//    }


    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {

        if (origin == null) {
            return null;
        }

        int height = origin.getHeight();
        int width = origin.getWidth();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘

        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);

        if (!origin.isRecycled()) {
            origin.recycle();
        }

        return newBM;
    }

    private void drawBackground(Canvas canvas) {
        canvas.save();//逐层绘制

        mPaint.setARGB(150, 255, 255, 255);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mPointF.x, mPointF.y, radius1, mPaint);

        mPaint.setARGB(110, 255, 255, 255);
        mPaint.setStrokeWidth(radius2 - radius1);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mPointF.x, mPointF.y, (radius2 - radius1) / 2 + radius1, mPaint);

        mPaint.setARGB(80, 255, 255, 255);
        mPaint.setStrokeWidth(radius3 - radius2);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mPointF.x, mPointF.y, (radius3 - radius2) / 2 + radius2, mPaint);

        canvas.restore();
    }


    public void addImage(Bitmap bitmap) {
        //生成一个Image
        ImageView imageView = new ImageView(mContext);
        int i = mRandom.nextInt(60) + 20;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageBitmap(bitmap);
        //获取位置
        mPendingImageList.add(imageView);

        invalidate();
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
        Random random = new Random(360);
        return random.nextDouble();
    }
}
