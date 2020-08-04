package com.example.android.reporta.user.signup.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.android.reporta.R;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;

public class BasicSignupActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_signup);

        ImageView googleButton = findViewById(R.id.logo_google);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthMethodPickerLayout basicSignUpLayout = new AuthMethodPickerLayout
                        .Builder(R.layout.activity_basic_signup)
                        .setGoogleButtonId(R.id.logo_google)
                        .build();

                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setAuthMethodPickerLayout(basicSignUpLayout)
                                .build(), RC_SIGN_IN);
            }
        });
    }
}