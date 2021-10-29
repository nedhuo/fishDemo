package com.nedhuo.custom.viewpage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 注意两点
 * 1. onDraw()方法里不要new 对象，因为调用太频繁，会触动GC，引发不必要的问题
 * 2. 注意绘制的时候不要重叠，可以使用clip进行抠图处理
 */
public class SimpleViewPage extends AppCompatTextView {
    private float mPercent = 0.0f; //设置一个百分比用来表示渐变
    private String mText = "垂直水平居中";

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float mPercent) {
        this.mPercent = mPercent;
        invalidate(); //重新绘制
    }

    public SimpleViewPage(Context context) {
        super(context);
    }

    public SimpleViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleViewPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


//        canvas.restore();
//        canvas.save();
//        //下一层
//        canvas.restore();

        /*
         * 画布的剪裁
         * */
//        Rect rect = new Rect(100, 0, 200, getHeight());
//        canvas.clipRect(rect);


        drawCenterText1(canvas);
        drawCenterText(canvas);
        drawCenterLineX(canvas);
        drawCenterLineY(canvas);
    }

    private void drawCenterText1(Canvas canvas) {
        /*Canvas分层
         * Canvas其实与PS类似，是一层一层的，可以进行分层绘制
         * */
        canvas.save();
        //canvas.restore();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setTextSize(80);
        float fontWidth = paint.measureText(mText);
        paint.setTextAlign(Paint.Align.LEFT);
        float left = getWidth() / 2 - fontWidth / 2;//水平居中起始位置
        //TODO 计算文字结束位置
        float right = left + fontWidth * mPercent;//文字水平结束位置
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = getHeight() / 2 - (fontMetrics.descent + fontMetrics.ascent) / 2;

        Rect rect = new Rect((int) left, 0, (int) right, getHeight());
        canvas.clipRect(rect);

        canvas.drawText(mText, left, baseLine, paint);

        canvas.restore();
    }


    private void drawCenterText(Canvas canvas) {
        canvas.save();
        float left = getWidth() / 2;
        float baseline = 100;
        //绘制文字
        Paint paint = new Paint();
        paint.setTextSize(80);//设置绘制文字大小
        paint.setAntiAlias(true); //抗锯齿效果
        /*水平居中绘制 第一种方式*/

        paint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式，默认在左边Paint.Align.LEFT
        /*
         * x 默认起始点
         * y  baseline  大多数文字的基线 ，相当于英文四格线的第三条线（大部分文字都在第三条线之上）
         * 文字也可以超出基准线，baseline只是相当于对照的一个基准
         * 设置为0，文字会在屏幕之外，
         * */

        canvas.drawText("水平居中第一种实现方式", left, baseline, paint);
        canvas.drawText("水平居中第二行", left, baseline + paint.getFontSpacing(), paint);//相比于上一行相当于换行绘制

        /*水平居中绘制 第二种方式 计算文字的宽度加进偏移*/
        paint.setTextAlign(Paint.Align.LEFT);  //设置回默认状态
        float fontWidth = paint.measureText("水平居中第二种实现方式");
        left = (getWidth() / 2) - (fontWidth / 2);
        canvas.drawText("水平居中第二种实现方式", left, baseline + paint.getFontSpacing() * 2, paint);//相比于上一行相当于换行绘制

        /*垂直水平居中绘制 */

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();//获取文字的高度
        //从基准线到文字最顶端的高度为ascent  正常文字不会超过accent
        // 某些特殊的符号与阿拉伯文字可能会超过ascent,但不会超过top
        //从基准线到文字最低端的高度为descent  与上面同理 bottom与top对应

        float ascent = fontMetrics.ascent;
        float bottom = fontMetrics.bottom;
        float descent = fontMetrics.descent;
        float top = fontMetrics.top;

        //float fontHeight = top + bottom; 文字高度
        //由于Android屏幕向上为负，向下为正，所以top为负，bottom为正
        float fontHeight = bottom - top;
        //因为我们需要文字垂直居中，我们需要计算的是当以getHeight()/2作为基准线时需要偏移多少才能真正的居中显示
        //float offset = (descent - ascent) / 2 - descent; //要找的是垂直居中文字的基准线
        //化简
        fontWidth = paint.measureText("垂直水平居中");
        float offset = (descent + ascent) / 2;
        baseline = getHeight() / 2 - offset;
        left = getWidth() / 2 - fontWidth / 2;

        float left_x = left + fontWidth * mPercent;
        Rect rect = new Rect((int) left_x, 0, (int) (left + fontWidth), getHeight());
        canvas.clipRect(rect);

        canvas.drawText("垂直水平居中", left, baseline, paint);
        canvas.restore();
    }


    private void drawCenterLineY(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
    }

    private void drawCenterLineX(final Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
    }
}
