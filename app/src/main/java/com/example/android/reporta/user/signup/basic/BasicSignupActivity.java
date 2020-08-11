package com.example.android.reporta.user.signup.basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;

import com.example.android.reporta.R;

public class BasicSignupActivity extends AppCompatActivity
        implements SignUpTipDialogFragment.OnDialogDismissedListener {

    private boolean mDialogDismissed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_signup);

        showDialog();
    }

    private void showDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorCustomDialog));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorCustomDialog));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        SignUpTipDialogFragment fragment = new SignUpTipDialogFragment(this);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDismiss() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
            getWindow().setNavigationBarColor(getResources().getColor(android.R.color.white));
        };
    }
}