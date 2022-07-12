package com.nedhuo.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.jar.Attributes

class XfermodeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC)
    val bounds = RectF(150f, 50f, 300f, 200f)
    val circleBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565)
    val squareBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565)

    //角度
    val radius = 10f

    init {
        var canvas = Canvas(circleBitmap)
        mPaint.color = Color.parseColor("#D81B60")
        canvas.drawOval(50f, 0F, 150f, 100f, mPaint)
        mPaint.color = Color.parseColor("#2196F3")
        canvas.setBitmap(squareBitmap)
        canvas.drawOval(0f, 50f, 100f, 150f, mPaint)
    }

    override fun onDraw(canvas: Canvas) {
//        val count = canvas.saveLayer(
//            left.toFloat() - paddingLeft,
//            top.toFloat() - paddingTop,
//            right.toFloat() - paddingRight,
//            right.toFloat() - paddingBottom,
//            mPaint
//        )
        val layer = canvas.saveLayer(bounds, null)
        canvas.drawBitmap(circleBitmap, 150f, 50f, mPaint)
        //Xfermode操作
//        canvas.drawRoundRect(
//            left.toFloat() - paddingLeft,
//            top.toFloat() - paddingTop,
//            right.toFloat() - paddingRight,
//            right.toFloat() - paddingBottom, radius, radius, mPaint,
//        )
        mPaint.xfermode = XFERMODE
        canvas.drawBitmap(squareBitmap, 150f, 50f, mPaint)
        mPaint.xfermode = null


        canvas.restoreToCount(layer)
    }
}