package com.nedhuo.custom;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nedhuo.custom.R;
import com.nedhuo.custom.circle.RoundCircleActivity;
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

        findViewById(R.id.tv_fish).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FishActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.tv_font).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FontEffectActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_circle).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoundCircleActivity.class);
            startActivity(intent);
        });
    }


}
