package com.example.android.reporta.user.signup.detailed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.reporta.R;
import com.example.android.reporta.user.signup.basic.BasicSignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DetailedSignUpActivity extends AppCompatActivity {

    private String EMPTY_FIELD_ERROR = "Field cannot be empty";
    private String TAG = this.getClass().getSimpleName();

    private EditText mEditTextFullName;
    private EditText mEditTextEmail;
    private EditText mEditTextPhone;
    private EditText mEditTextContactAddress;
    private Button mButtonSignUp;

    private String mEmail;
    private String mPassword;
    private String mFullName;
    private String mUserName;
    private String mPhoneNumber;
    private String mContactAddress;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_signup);

        if (getIntent() != null) {
            initFields();
            proceedToSignUp();
        }
    }

    private void proceedToSignUp() {
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });
    }
    
    private void signUpUser() {
        if (verifyFieldsAreFilled()) {
           mFirebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
               @Override
               public void onSuccess(AuthResult authResult) {
                   Toast.makeText(DetailedSignUpActivity.this, "User created", Toast.LENGTH_SHORT).show();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(DetailedSignUpActivity.this, "Failed" + e.getMessage() , Toast.LENGTH_SHORT).show();
               }
           });
        } else {
            Toast.makeText(this, "Cannot Proceed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyFieldsAreFilled() {
        mFullName = mEditTextFullName.getText().toString();
        mUserName = mEditTextEmail.getText().toString();
        mPhoneNumber = mEditTextPhone.getText().toString();
        mContactAddress = mEditTextContactAddress.toString();
        if (!TextUtils.isEmpty(mFullName)) {
            Log.d(TAG, "Full name: " + mFullName);
            if (!TextUtils.isEmpty(mUserName)) {
                Log.d(TAG, "Email: " + mUserName);
                if (!TextUtils.isEmpty(mPhoneNumber)) {
                    Log.d(TAG, "Phone Number:" + mPhoneNumber);
                    if (!TextUtils.isEmpty(mContactAddress)) {
                        Log.d(TAG, "Contact Address:" + mContactAddress);
                        return true;
                    } else {
                        mEditTextContactAddress.setError(EMPTY_FIELD_ERROR);
                        return false;
                    }
                } else {
                    mEditTextPhone.setError(EMPTY_FIELD_ERROR);
                    return false;
                }
            } else {
                mEditTextEmail.setError(EMPTY_FIELD_ERROR);
                return false;
            }
        } else {
            mEditTextFullName.setError(EMPTY_FIELD_ERROR);
            return false;
        }
    }

    private void initFields() {
        //Get email & password from the bundle passed in from BasicSignUpActivity
        mEmail = getIntent().getExtras().getString(BasicSignUpActivity.EMAIL_EXTRA);
        mPassword = getIntent().getExtras().getString(BasicSignUpActivity.PASSWORD_EXTRA);

        mEditTextFullName = findViewById(R.id.full_name);
        mEditTextEmail = findViewById(R.id.username);
        mEditTextPhone = findViewById(R.id.editTextPhone);
        mEditTextContactAddress = findViewById(R.id.contact_address);
        mButtonSignUp = findViewById(R.id.btn_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }
}