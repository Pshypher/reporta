package com.example.android.reporta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.reporta.user.onboarding.OnBoardingActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        startActivity(new Intent(SplashScreenActivity.this,
                OnBoardingActivity.class));
    }
}