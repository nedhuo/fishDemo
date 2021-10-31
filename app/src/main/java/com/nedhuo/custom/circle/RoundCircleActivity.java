package com.nedhuo.custom.circle;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nedhuo.custom.R;

public class RoundCircleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        View roundCircleView = findViewById(R.id.roundCircleView);
    }
}
