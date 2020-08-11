package com.example.android.reporta.user.signup.detailed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.android.reporta.user.signup.basic.BasicSignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DetailedSignUpActivity extends AppCompatActivity {

    private String EMPTY_FIELD_ERROR = "Field cannot be empty";
    private String TAG = this.getClass().getSimpleName();

    private EditText mEditTextFullName;
    private EditText mEditTextUserName;
    private EditText mEditTextPhone;
    private EditText mEditTextContactAddress;
    private Button mButtonSignUp;

    private String mEmail;
    private String mPassword;
    private String mFullName;
    private String mUserName;
    private String mPhoneNumber;
    private String mContactAddress;

    private boolean isFullNameFilled;
    private boolean isUserNameFilled;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_signup);

        if (getIntent() != null) {
            initFields();
            verifyFieldsAreFilled();
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
        if (isFullNameFilled && isUserNameFilled) {
           mFirebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
               @Override
               public void onSuccess(AuthResult authResult) {
                   mEditTextFullName.setText("");
                   mEditTextUserName.setText("");
                   mEditTextPhone.setText("");
                   mEditTextContactAddress.setText("");
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

    private void verifyFieldsAreFilled() {
        mEditTextFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    isFullNameFilled = true;
                }
                else {
                    isFullNameFilled = false;
                    mEditTextFullName.setError(EMPTY_FIELD_ERROR);
                }
            }
        });
        mEditTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    isUserNameFilled = true;
                }
                else {
                    isUserNameFilled = false;
                    mEditTextUserName.setError(EMPTY_FIELD_ERROR);
                }
            }
        });
    }

    private void initFields() {
        //Get email & password from the bundle passed in from BasicSignUpActivity
        mEmail = getIntent().getExtras().getString(BasicSignUpActivity.EMAIL_EXTRA);
        mPassword = getIntent().getExtras().getString(BasicSignUpActivity.PASSWORD_EXTRA);

        mEditTextFullName = findViewById(R.id.full_name);
        mEditTextUserName = findViewById(R.id.username);
        mEditTextPhone = findViewById(R.id.editTextPhone);
        mEditTextContactAddress = findViewById(R.id.contact_address);
        mButtonSignUp = findViewById(R.id.btn_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }
}