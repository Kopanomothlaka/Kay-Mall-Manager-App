package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                openActivity();
                finish();
            }
        },5000);
    }

    private void openActivity() {

        Intent intent = new Intent(SplashActivity.this,LogInActivity.class);
        startActivity(intent);
        finish();
    }
}