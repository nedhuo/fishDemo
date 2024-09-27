package com.nedhuo.custom;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nedhuo.custom.circle.RoundCircleActivity;
import com.nedhuo.custom.anim.headeranim.FishActivity;
import com.nedhuo.custom.anim.headeranim.AiChatHeaderLayoutActivity;
import com.nedhuo.custom.viewpage.SimpleViewPage;

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

        findViewById(R.id.tv_simpleDanma).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SimpleDanmaActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_header).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AiChatHeaderLayoutActivity.class);
            startActivity(intent);
        });
    }


}
