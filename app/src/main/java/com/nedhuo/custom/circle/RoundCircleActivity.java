package com.nedhuo.custom.circle;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        roundCircleView.addView(imageView);

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = 200;
        layoutParams.height = 200;
        imageView.setLayoutParams(layoutParams);


    }
}
