package com.apps1.cocinapp.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1000; // 1 segundo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Para que no vuelva al splash al presionar atrás
        }, SPLASH_DURATION);
    }
}
