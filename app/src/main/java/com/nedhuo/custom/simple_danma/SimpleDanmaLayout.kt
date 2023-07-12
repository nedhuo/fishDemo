package com.nedhuo.custom.simple_danma

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.coroutines.delay
import kotlin.random.Random

class SimpleDanmaLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    //TODO 是否要考虑锁
    private val viewList = arrayListOf<TextView>()
    private val screenHeight = resources.displayMetrics.heightPixels.toFloat()

    init {
        repeat(50) {
            viewList.add(TextView(context).apply {
                text = "测试"
            })
        }
    }

    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true //抗锯齿
            isDither = true //防抖动
            style = Paint.Style.FILL //画笔类型
        }
    }

    /**
     * 简单的一个文字飘萍动画
     */
    suspend fun startAnim() {
        while (true) {
            delay(500)
            val textView = viewList.removeAt(0)
            //设置随机字体大小
            paint.textSize = Random.nextInt(20, 30).toFloat()
            textView.textSize = Random.nextInt(20, 30).toFloat()
            val viewWidth = paint.measureText(textView.text.toString())
            //获取屏幕宽度
            val screenWidth = resources.displayMetrics.widthPixels.toFloat()
            addView(textView)
            //设置View随机位置
            textView.x = screenWidth
            textView.y = Random.nextInt(200, screenHeight.toInt() - 200).toFloat()

            val animator = ObjectAnimator.ofFloat(
                textView,
                "translationX",
                screenWidth,
                -200f
            )
            animator.duration = 2000
            animator.interpolator = LinearInterpolator()
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    removeView(textView)
                    viewList.add(textView)
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            animator.start()
        }
    }


}