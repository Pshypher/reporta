package com.example.android.reporta.user.signup.basic;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.android.reporta.R;

public class SignUpTipDialogFragment extends DialogFragment {

    private OnDialogDismissedListener mCallback;

    public SignUpTipDialogFragment(OnDialogDismissedListener listener) {
        mCallback = listener;
    }

    public interface OnDialogDismissedListener {
        void onDismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.layout_tips, container, false);
       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mCallback.onDismiss();
               dismiss();
           }
       });
       return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog;
    }
}
