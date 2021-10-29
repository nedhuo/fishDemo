package com.nedhuo.custom;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nedhuo.custom.R;
import com.nedhuo.custom.fish.FishActivity;
import com.nedhuo.custom.viewpage.SimpleViewPage;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SimpleViewPage mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = findViewById(R.id.simpleViewPage);

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

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FishActivity.class);
                startActivity(intent);
            }
        });
    }

    private void change() {
        ObjectAnimator.ofFloat(mView, "percent", 0, 1).setDuration(2000).start();
    }
}
