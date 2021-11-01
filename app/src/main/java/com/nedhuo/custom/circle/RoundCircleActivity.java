package com.nedhuo.custom.circle;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nedhuo.custom.R;

public class RoundCircleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        RoundCircleLayout roundCircleView = findViewById(R.id.roundCircleView);
        Button btn_add = findViewById(R.id.btn_add);

        btn_add.setOnClickListener(v -> {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.ic_launcher);

            roundCircleView.addView(imageView);
            //先添加View 再获取 否则为null
//            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//            layoutParams.width = 50;
//            layoutParams.height = 50;
//            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(50, 50);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setLayoutParams(layoutParams);


        });


    }
}
