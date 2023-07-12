package com.nedhuo.custom.simple_danma

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.children
import com.nedhuo.custom.R
import kotlinx.coroutines.delay
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


/**
 * developer：huohuo
 * version:
 * date: 2023/7/12 11:20
 * description: 匹配转盘背景
 */
class MatchTurntableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val cacheMap = HashMap<PointF, ImageView>()
    private val viewMaxHeight = 30.dp()

    /**
     * 初始圆半径
     */
    private val initRadius = 100F

    /**
     * 半径间距
     */
    private val radiusDistance = 100

    private val centerPointF: PointF = PointF()

    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true //抗锯齿
            isDither = true //防抖动
            style = Paint.Style.FILL //画笔类型
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_match_turntable, this, true)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        centerPointF.y = ((bottom - top) / 2).toFloat()
        centerPointF.x = ((right - left) / 2).toFloat()
        super.onLayout(changed, left, top, right, bottom)
    }

    suspend fun startAnimation() {
        while (true) {
            delay(200)

            //生成随机所在位置
            val radius: Float = (Random.nextInt(1, 4)) * 50.dp()
            val pointF: PointF = calculatePoint(centerPointF, radius)

            addView(pointF)
        }
    }


    fun addView(pointF: PointF) {

        /**
         * 控制View不重叠的方法 使用delay() 可以避免死循环
         */
        cacheMap.keys.forEach {
            if (abs(it.x - pointF.x) < viewMaxHeight && abs(it.y - pointF.y) < viewMaxHeight) {
                return
            }
        }

        //生成View
        val imageView = ImageView(context)
        imageView.setImageResource(R.mipmap.ic_launcher)

        cacheMap[pointF] = imageView


        super.addView(imageView)
        val layoutParams = imageView.layoutParams

        //随机宽高
        layoutParams.width = 30.dp().toInt()
        layoutParams.height = 30.dp().toInt()
        imageView.layoutParams = layoutParams
        val i = layoutParams.height / 2 //宽高的一半 需要拿计算出的位置 与 该值 计算出最终位置


        //设置位置
        imageView.y = pointF.y - i
        imageView.x = pointF.x - i

        //放大
        val amplifyAnimX = ObjectAnimator.ofFloat(imageView, "scaleX", 0F, 1F)
        val amplifyAnimY = ObjectAnimator.ofFloat(imageView, "scaleY", 0F, 1F)
        //缩小
        val shrinkAnimX = ObjectAnimator.ofFloat(imageView, "scaleX", 1F, 0F)
        val shrinkAnimY = ObjectAnimator.ofFloat(imageView, "scaleY", 1F, 0F)


        val animSet = AnimatorSet().apply {
            //先放大再缩小
            play(amplifyAnimX).with(amplifyAnimY).before(shrinkAnimX).before(shrinkAnimY)
            duration = 2000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    removeView(imageView)
                    cacheMap.remove(pointF)
                }
            })
            start()
        }
    }

    /**
     * 绘制背景
     */
    private fun drawBackground(canvas: Canvas) {
        //绘制背景蒙版
        canvas.save() //逐层绘制
        paint.setARGB(189, 0, 0, 0)
        paint.style = Paint.Style.FILL
        canvas.drawColor(0x22000000)
        canvas.restore()
        //绘制圈圈
        canvas.save() //逐层绘制
        paint.setARGB(255, 204, 204, 204)
        paint.strokeWidth = 2F
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(centerPointF.x, centerPointF.y, initRadius, paint)


        canvas.drawCircle(centerPointF.x, centerPointF.y, initRadius + radiusDistance, paint)


        canvas.drawCircle(centerPointF.x, centerPointF.y, initRadius + 2 * radiusDistance, paint)
        canvas.restore()
    }

    /**
     * 计算相对于圆心的起始位置
     *
     * @param startPoint 起始点
     * @param radius     半径
     */
    private fun calculatePoint(startPoint: PointF, radius: Float): PointF {
        val angle: Double = Random.nextInt(360).toDouble()//计算角度
        val deltaX = (cos(Math.toRadians(angle)) * radius).toFloat()
        //向左平移π，相当于取反 Android坐标与数学坐标中的y值是相反的，此处 -180 相当于取反
        val deltaY = (sin(Math.toRadians(angle - 180)) * radius).toFloat()
        //备份
        val pointF = PointF(startPoint.x + deltaX, startPoint.y + deltaY)

        return pointF
    }

    /**
     * 判断View是否重叠
     */
    fun areViewsOverlapping(view1: View, view2: View): Boolean {
        val rect1 = Rect()
        view1.getGlobalVisibleRect(rect1)
        val rect2 = Rect()
        view2.getGlobalVisibleRect(rect2)
        return Rect.intersects(rect1, rect2)
    }


}

fun Int.dp(): Float {
    return this * Resources.getSystem().displayMetrics.density
}