package com.example.android.reporta.user.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.reporta.R;
import com.example.android.reporta.user.signup.basic.BasicSignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    TextView mTextViewSignUp;
    EditText mEditTextEmailAddress;
    EditText mEditTextPassword;
    Button mButtonContinue;

    String mEmailAddress;
    String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initFields();
        signInUser();
        navigateToSignUp();
    }

    private void navigateToSignUp() {
        mTextViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, BasicSignUpActivity.class));
                finish();
            }
        });
    }

    private void signInUser() {
        mButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailAddress = mEditTextEmailAddress.getText().toString();
                mPassword = mEditTextPassword.getText().toString();
                if (!TextUtils.isEmpty(mEmailAddress) && !TextUtils.isEmpty(mPassword)) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmailAddress, mPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mEditTextEmailAddress.setText("");
                                        mEditTextPassword.setText("");
                                        Toast.makeText(SignInActivity.this,
                                                "Sign In Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignInActivity.this,
                                                "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignInActivity.this,
                            "Fields not filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initFields() {
        mTextViewSignUp = findViewById(R.id.sign_up);
        mEditTextEmailAddress = findViewById(R.id.email_address);
        mEditTextPassword = findViewById(R.id.password);
        mButtonContinue = findViewById(R.id.btn_continue);
    }
}