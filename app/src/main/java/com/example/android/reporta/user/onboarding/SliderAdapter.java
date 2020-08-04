package com.example.android.reporta.user.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android.reporta.R;
import com.example.android.reporta.user.onboarding.models.Page;

public class SliderAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private Page[] mPages;
    private LayoutInflater mInflater;
    private OnSlideShiftedListener mCallback;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        return;
    }

    @Override
    public void onPageSelected(int position) {
        mCallback.onSlideShifted(position, mPages[position].getText());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        return;
    }


    public interface OnSlideShiftedListener {
        void onSlideShifted(int position, String text);
    }

    public SliderAdapter(Context context) {
        mContext = context;
        mCallback = (OnSlideShiftedListener) context;
        mPages = new Page[] {
                new Page(context, R.drawable.onboarding_img_a, R.string.onboarding_text_a),
                new Page(context, R.drawable.onboarding_img_b, R.string.onboarding_text_b)
        };
    }

    @Override
    public int getCount() {
        return mPages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.gallery);
        slideImageView.setImageResource(mPages[position].getImageId());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
