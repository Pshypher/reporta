package com.example.android.reporta.user.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.reporta.R;
import com.example.android.reporta.user.signup.basic.BasicSignUpActivity;

public class OnBoardingActivity extends AppCompatActivity
        implements SliderAdapter.OnSlideShiftedListener {

    private ViewPager mSlideViewPager;
    private SliderAdapter mAdapter;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private View[] mTabs;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        mSlideViewPager = (ViewPager) findViewById(R.id.slide_body);
        mAdapter = new SliderAdapter(this);
        mSlideViewPager.addOnPageChangeListener(mAdapter);
        mSlideViewPager.setAdapter(mAdapter);
        mTabs = new View[] {findViewById(R.id.tab_slot_1), findViewById(R.id.tab_slot_2)};
        mNextButton = (ImageButton) findViewById(R.id.button_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage++;
                if (mCurrentPage == mTabs.length) {
                    Intent intent = new Intent(OnBoardingActivity.this, BasicSignUpActivity.class);
                    startActivity(intent);
                }
                mSlideViewPager.setCurrentItem(mCurrentPage);

            }
        });
        mPreviousButton =  findViewById(R.id.button_previous);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage--;
                mSlideViewPager.setCurrentItem(mCurrentPage);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSlideViewPager.removeOnPageChangeListener(mAdapter);
    }

    @Override
    public void onSlideShifted(int position, String text) {
        mCurrentPage = position;
        TextView slideDescription = (TextView) findViewById(R.id.tv_slide_description);
        slideDescription.setText(text);
        updateTabState(position, mTabs);
        syncButtonPageState();
    }

    private void updateTabState(int position, View[] views) {
        for (View view : views) {
            view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }

        if (position >= 0 && position < views.length) {
            views[position].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            Toast.makeText(this, "Cannot find page at " + position,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void syncButtonPageState() {
        switch (mCurrentPage) {
            case 0:
                mPreviousButton.setVisibility(View.GONE);
                break;
            default:
                mPreviousButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}