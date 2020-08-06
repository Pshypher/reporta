package com.example.android.reporta.user.signup.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.reporta.R;
import com.example.android.reporta.user.signup.detailed.DetailedSignUpActivity;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;

public class BasicSignUpActivity extends AppCompatActivity {
    public static String EMAIL_EXTRA = "BasicSignUpActivity.Email";
    public static String PASSWORD_EXTRA = "BasicSignUpActivity.Password";

    private String TAG = this.getClass().getSimpleName();

    private static String EMPTY_FIELD_ERROR = "Field cannot be empty";
    private static String PASSWORD_TOO_WEAK = "Password must be more than six characters";

    private static final int RC_SIGN_IN = 123;
    private static int MINIMUM_PASSWORD_CHAR = 6;

    private EditText mEditTextEmailAddress;
    private EditText mEditTextPassword;
    private Button mContinueButton;

    private String mEmailAddress;
    private String mPassword;
    private boolean mIsEmailVerified = false;
    private boolean mIsPasswordVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_signup);

        initFields();
        verifyUserInput();
        continueSignUp();
        handleGoogleSignUp();
        //TODO: handleFacebookSignUp
    }

    private void continueSignUp() {
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsEmailVerified && mIsPasswordVerified) {
                    mEmailAddress = mEditTextEmailAddress.getText().toString();
                    mPassword = mEditTextPassword.getText().toString();
                    Intent continueSignUpIntent =
                            new Intent(BasicSignUpActivity.this, DetailedSignUpActivity.class);
                    continueSignUpIntent.putExtra(EMAIL_EXTRA, mEmailAddress);
                    continueSignUpIntent.putExtra(PASSWORD_EXTRA, mPassword);
                    startActivity(continueSignUpIntent);
                } else {
                    Toast.makeText(BasicSignUpActivity.this, "Cannot Proceed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyUserInput() {
        mEditTextEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    Log.d(TAG, "Email: " + editable.toString());
                    mIsEmailVerified = true;
                    mEditTextEmailAddress.setError(null);
                } else {
                    mIsEmailVerified = false;
                    mEditTextEmailAddress.setError(EMPTY_FIELD_ERROR);
                }
            }
        });
        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    if (editable.length() > MINIMUM_PASSWORD_CHAR) {
                        mIsPasswordVerified = true;
                        mEditTextPassword.setError(null);
                    } else {
                        mIsPasswordVerified = false;
                        mEditTextPassword.setError(PASSWORD_TOO_WEAK);
                    }

                } else {
                    mIsPasswordVerified = false;
                    mEditTextPassword.setError(EMPTY_FIELD_ERROR);
                }
            }
        });
    }

    private void handleGoogleSignUp() {
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

    private void initFields() {
        mEditTextEmailAddress = findViewById(R.id.email_address);
        mEditTextPassword = findViewById(R.id.password);
        mContinueButton = findViewById(R.id.btn_continue);
    }
}