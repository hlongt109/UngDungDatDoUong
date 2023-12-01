package com.longthph30891.ungdungdatdouong.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.longthph30891.ungdungdatdouong.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginRegisterActivity.class));
            finish();
        }, 3000);

    }
}