package com.nedhuo.custom.anim.headeranim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nedhuo.custom.R

class AiChatHeaderLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_chat_header_layout)

        val headerLayout = findViewById<AiChatHeaderLayout>(R.id.header)
        headerLayout.setOnClickListener {
            headerLayout.switchHeaderStatus()
        }
    }
}