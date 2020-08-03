package com.example.android.reporta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.reporta.user.signup.basic.BasicSignupActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* setContentView(R.layout.activity_main); */

        startActivity(new Intent(SplashScreenActivity.this,
                BasicSignupActivity.class));
    }
}