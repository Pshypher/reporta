package com.example.android.reporta.user.onboarding.models;

import android.content.Context;

public class Page {

    private int mImageId;
    private String mText;

    public Page(Context context, int imageId, int stringId) {
        mImageId = imageId;
        mText = context.getString(stringId);
    }

    public int getImageId() {
        return mImageId;
    }

    public String getText() {
        return mText;
    }
}
