package com.example.android.reporta.user.signup.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.android.reporta.firebase_utils.FirebaseUtils;
import com.example.android.reporta.user.signup.detailed.DetailedSignUpActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

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
    private LoginButton mLoginButton;

    //Facebook callback
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;

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
        //TODO: properly handleGoogleSignUp();
        handleFacebookSignUp();
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
                AuthMethodPickerLayout googleSignUp = new AuthMethodPickerLayout
                        .Builder(R.layout.activity_basic_signup)
                        .setGoogleButtonId(R.id.logo_google)
                        .build();

                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setAuthMethodPickerLayout(googleSignUp)
                                .build(), RC_SIGN_IN);
            }
        });

        ImageView facebookButton = findViewById(R.id.login_facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthMethodPickerLayout facebookSignUp = new AuthMethodPickerLayout
                        .Builder(R.layout.activity_basic_signup)
                        .setFacebookButtonId(R.id.login_facebook)
                        .build();

                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setAuthMethodPickerLayout(facebookSignUp)
                                .build(), RC_SIGN_IN);
            }
        });
    }

    private void handleFacebookSignUp() {
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential =
                        FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                mFirebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(BasicSignUpActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BasicSignUpActivity.this, DetailedSignUpActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasicSignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancel() {
                Toast.makeText(BasicSignUpActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(BasicSignUpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initFields() {
        mEditTextEmailAddress = findViewById(R.id.email_address);
        mEditTextPassword = findViewById(R.id.password);
        mContinueButton = findViewById(R.id.btn_continue);
        mLoginButton = findViewById(R.id.login_facebook);
        mCallbackManager = CallbackManager.Factory.create();
        mFirebaseAuth = FirebaseUtils.getFirebaseAuth();
    }
}