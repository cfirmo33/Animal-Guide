package com.marcelo.animalguide.activitys.splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.marcelo.animalguide.R;

public class SplashScreenActivity extends AppCompatActivity
{

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}
