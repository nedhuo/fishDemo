package com.nedhuo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class GradientTextView extends AppCompatTextView {

    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Rect mTextBound = new Rect();
    /**
     * 移动距离
     */
    private int translateWidth;

    public GradientTextView(Context context) {
        super(context);

    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if(w!=0){
//            width=w;
//        }else{
//            width=getMeasuredWidth();
//        }
//        mPaint = getPaint();
//        //X0: 渐变起初点坐标x位置
//        //
//        //y0: 渐变起初点坐标y位置
//        //
//        //x1: 渐变终点坐标x位置
//        //
//        //y1: 渐变终点坐标y位置
//        //
//        //colors: 渐变颜色数组
//        //
//        //positions:这个也是一个数组用来指定颜色数组的相对位置 如果为null 就沿坡度线均匀分布
//        //
//        //tile：平铺方式
//        mLinearGradient = new LinearGradient(-width, 0,0,0,
//                new int[]{Color.RED,Color.GREEN,Color.MAGENTA},
//                new float[]{0,0.5f,1f},
//                Shader.TileMode.CLAMP);
//        //添加渲染
//        mPaint.setShader(mLinearGradient);
//        mMatrix=new Matrix();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        String str = getText().toString();
        mPaint = getPaint();
        mLinearGradient = new LinearGradient(0, 0, measuredWidth, 0,
                new int[]{Color.RED, Color.GREEN},
                new float[]{0, 1f},
                Shader.TileMode.CLAMP);
        mPaint.getTextBounds(str, 0, str.length(), mTextBound);
        mPaint.setShader(mLinearGradient);
    //    canvas.drawText(str, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
        super.onDraw(canvas);
//        if(mMatrix==null)
//            return;
//        //每次移动原来宽的15分之一
//        translateWidth+=width/15;
//        //表示刚刚移动 了width个宽度 即 正好包含了整个textview 的时候还原
//        if(translateWidth>width*2){
//            translateWidth-=width*2;
//        }
//        //移动
//        mMatrix.setTranslate(translateWidth,0);
//        mLinearGradient.setLocalMatrix(mMatrix);
//        postInvalidateDelayed(100);
    }

}
