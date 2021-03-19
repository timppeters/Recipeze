package com.group2.recipeze.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.group2.recipeze.R;
import com.group2.recipeze.ui.login.LoginActivity;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    private final int STR_SPLASH_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startSplashTimer();
    }

    private void startSplashTimer() {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, STR_SPLASH_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}