package com.nedhuo.custom

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nedhuo.custom.simple_danma.MatchTurntableLayout
import com.nedhuo.custom.simple_danma.SimpleDanmaLayout
import kotlinx.coroutines.launch

class SimpleDanmaActivity : AppCompatActivity() {
    private val danmaLists = arrayListOf(
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风",
        "本是无意穿堂风"
    )
    private val textViewCachePool = arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_danma)
        val simpleDanmaLayout = findViewById<SimpleDanmaLayout>(R.id.simpleDanmaLayout)
        val turntableLayout = findViewById<MatchTurntableLayout>(R.id.turntableLayout)
        val frameLayout = findViewById<FrameLayout>(R.id.fl_container)



        frameLayout.post {
            lifecycleScope.launch {
                simpleDanmaLayout.startAnim()

            }
        }

        turntableLayout.post {
            lifecycleScope.launch {
                turntableLayout.startAnimation()
            }
        }
    }


}