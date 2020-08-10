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
import android.widget.Toast;

import com.example.android.reporta.R;
import com.example.android.reporta.firebase_utils.FirebaseUtils;
import com.example.android.reporta.user.signup.detailed.DetailedSignUpActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class BasicSignUpActivity extends AppCompatActivity {
    public static String EMAIL_EXTRA = "BasicSignUpActivity.Email";
    public static String PASSWORD_EXTRA = "BasicSignUpActivity.Password";

    private String TAG = this.getClass().getSimpleName();

    private static String EMPTY_FIELD_ERROR = "Field cannot be empty";
    private static String PASSWORD_TOO_WEAK = "Password must be more than six characters";

    private static final int RC_SIGN_IN = 1;
    private static int MINIMUM_PASSWORD_CHAR = 6;

    private EditText mEditTextEmailAddress;
    private EditText mEditTextPassword;
    private Button mContinueButton;
    //Facebook login in button
    private LoginButton mLoginButton;
    //Google sign in button
    private SignInButton mSignInButton;

    //Facebook callback
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
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
        handleGoogleSignUp();
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
                    Toast.makeText(BasicSignUpActivity.this, "Cannot Proceed", Toast.LENGTH_SHORT)
                            .show();
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFacebookSignUp() {
        mLoginButton.setPermissions("email", "public_profile");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(BasicSignUpActivity.this, "Cancelled", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(BasicSignUpActivity.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(BasicSignUpActivity.this, "User created", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    Toast.makeText(BasicSignUpActivity.this,
                            "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleGoogleAccessToken(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Errro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleGoogleAccessToken(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(BasicSignUpActivity.this, "User created",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initFields() {
        mEditTextEmailAddress = findViewById(R.id.email_address);
        mEditTextPassword = findViewById(R.id.password);
        mContinueButton = findViewById(R.id.btn_continue);
        mLoginButton = findViewById(R.id.login_facebook);
        mSignInButton = findViewById(R.id.login_google);
        mCallbackManager = CallbackManager.Factory.create();
        mFirebaseAuth = FirebaseUtils.getFirebaseAuth();
    }
}