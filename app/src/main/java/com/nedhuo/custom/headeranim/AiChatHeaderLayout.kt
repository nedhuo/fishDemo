package com.nedhuo.custom.headeranim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.addListener
import com.nedhuo.custom.databinding.AiChatLayoutHeaderBinding
import com.nedhuo.custom.ext.dp


class AiChatHeaderLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var binding: AiChatLayoutHeaderBinding = AiChatLayoutHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * 头像状态
     */
    private var bigHeadStatus = false

    /**
     * 动画时长 ms
     */
    private var animDuration: Long = 500

    init {
        init()
    }

    private fun init() {

        switchHeaderStatus(true)
    }


    private fun shrinkAnim() {
        //头像动画
        Log.d("", "shrinkAnim width=${width} measuredWidth=$measuredWidth height=$height measuredHeight=$measuredHeight")

        //rootView Anim
        val targetHeight = 48.dp()
        val rootAnim = ObjectAnimator.ofFloat(binding.clRoot.height.toFloat(), targetHeight).apply {
            addUpdateListener {
                val height = it.animatedValue as Float
                val layoutParams = binding.root.layoutParams
                layoutParams.height = height.toInt()
                binding.root.layoutParams = layoutParams
            }
        }

        //头像Anim
        val targetSize = 40.dp() // 目标大小
        val targetX = 12.dp()  // 目标 X 坐标
        val targetY = 4.dp()// + binding.root.height - targetHeight

//
        val pvhWidth = PropertyValuesHolder.ofFloat("scaleX", targetSize / binding.sivHead.width.toFloat())
        val pvhHeight = PropertyValuesHolder.ofFloat("scaleY", targetSize / binding.sivHead.height.toFloat())
        val pvhX = PropertyValuesHolder.ofFloat("translationX", targetX - binding.sivHead.left - (binding.sivHead.width - targetSize) / 2F)
        val pvhY = PropertyValuesHolder.ofFloat("translationY", targetY - binding.sivHead.top + ((targetSize - binding.sivHead.height) / 2))
        val headerAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.sivHead, pvhWidth, pvhHeight, pvhX, pvhY)


        //音波Anim
        val targetWaveX = 62.dp()
        val targetWaveY = 6.dp()
        val targetWaveHeight = 32.dp()
        val targetWaveWidth = 64.dp()
        val pvhWaveHeight = PropertyValuesHolder.ofFloat("scaleX", targetWaveWidth / binding.llAiWave.width.toFloat())
        val pvhWaveWidth = PropertyValuesHolder.ofFloat("scaleY", targetWaveHeight / binding.llAiWave.height.toFloat())
        val wavePvhX = PropertyValuesHolder.ofFloat("translationX", targetWaveX - binding.llAiWave.left - (binding.llAiWave.width - targetWaveWidth) / 2F)
        val wavePvhY = PropertyValuesHolder.ofFloat("translationY", targetWaveY - binding.llAiWave.top + (targetWaveHeight - binding.llAiWave.height) / 2F)
        val waveAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.llAiWave, wavePvhX, wavePvhY, pvhWaveHeight, pvhWaveWidth)


        val animatorSet = AnimatorSet()
        animatorSet.setDuration(animDuration)
        animatorSet.playTogether(headerAnim, rootAnim, waveAnim)
        animatorSet.addListener(onEnd = {

        })
        animatorSet.start()
    }

    private fun expandAnim() {
        //头像动画
        Log.d("", "expandAnim width=${width} measuredWidth=$measuredWidth height=$height measuredHeight=$measuredHeight")

        //根布局
        val targetHeight = 126.dp()
        val rootAnim = ObjectAnimator.ofFloat(binding.clRoot.height.toFloat(), targetHeight).apply {
            addUpdateListener {
                val height = it.animatedValue as Float
                val layoutParams = binding.root.layoutParams
                layoutParams.height = height.toInt()
                binding.root.layoutParams = layoutParams
            }
        }

        //头像 - (targetHeight - binding.root.height.toFloat()) / 2
        val targetSize = 108.dp() // 目标大小
        val targetX = (measuredWidth - binding.sivHead.width) / 2f
        val targetY = 12.dp()
        val pvhWidth = PropertyValuesHolder.ofFloat("scaleX", targetSize / binding.sivHead.width.toFloat())
        val pvhHeight = PropertyValuesHolder.ofFloat("scaleY", targetSize / binding.sivHead.height.toFloat())
        val pvhX = PropertyValuesHolder.ofFloat("translationX", targetX - binding.sivHead.left)
        val pvhY = PropertyValuesHolder.ofFloat("translationY", targetY - binding.sivHead.top)
        val headerAnimator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.sivHead, pvhWidth, pvhHeight, pvhX, pvhY)

        //音波Anim
        val targetWaveX = 214.dp()
        val targetWaveY = 18.dp()
        val targetWaveHeight = 24.dp()
        val targetWaveWidth = 48.dp()
        val pvhWaveHeight = PropertyValuesHolder.ofFloat("scaleX", targetWaveWidth / binding.llAiWave.width.toFloat())
        val pvhWaveWidth = PropertyValuesHolder.ofFloat("scaleY", targetWaveHeight / binding.llAiWave.height.toFloat())
        val wavePvhX = PropertyValuesHolder.ofFloat("translationX", targetWaveX - binding.llAiWave.left)
        val wavePvhY = PropertyValuesHolder.ofFloat("translationY", targetWaveY - binding.llAiWave.top)
        val waveAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.llAiWave, wavePvhX, wavePvhY, pvhWaveHeight, pvhWaveWidth)


        val animatorSet = AnimatorSet()
        animatorSet.setDuration(animDuration)
        animatorSet.playTogether(headerAnimator, rootAnim, waveAnim)
        animatorSet.start()
    }

    /**
     * 切换顶部View状态
     */
    fun switchHeaderStatus(bigHead: Boolean = !bigHeadStatus) {
        if (bigHead == bigHeadStatus) return
        bigHeadStatus = bigHead
        binding.root.post {
            if (bigHeadStatus) {
                expandAnim()
            } else {
                shrinkAnim()
            }
        }
    }

    /**
     * 添加透明度动画
     */
    private fun addAlphaAnim(view: View, show: Boolean = false): ObjectAnimator {
        val pvhAlpha = if (show) {
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        } else {
            PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        }
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhAlpha)
    }


    companion object {
        const val EVENT_HEADER_ME_CREATION_CLICK = "EVENT_HEADER_ME_CREATION_CLICK"
        const val EVENT_HEADER_CLONE_TRAINING_CLICK = "EVENT_HEADER_CLONE_TRAINING_CLICK"
        const val EVENT_HEADER_EXIT_ZONE_CLICK = "EVENT_HEADER_EXIT_ZONE_CLICK"
        const val EVENT_HEADER_SHOW_H5_ZONE_CLICK = "EVENT_HEADER_SHOW_H5_ZONE_CLICK"
    }
}