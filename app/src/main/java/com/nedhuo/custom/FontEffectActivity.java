package com.nedhuo.custom;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nedhuo.custom.viewpage.SimpleViewPage;

import java.util.Timer;
import java.util.TimerTask;

public class FontEffectActivity extends AppCompatActivity {
    private SimpleViewPage mFont;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_effect);


        mFont = findViewById(R.id.simpleViewPage);


        mFont.setOnClickListener(v -> {
            //使用属性动画的方式实现渐变
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            change();
                        }
                    });
                }
            };
            timer.schedule(timerTask, 2000);
        });
    }

    private void change() {
        ObjectAnimator.ofFloat(mFont, "percent", 0, 1).setDuration(2000).start();
    }
}
