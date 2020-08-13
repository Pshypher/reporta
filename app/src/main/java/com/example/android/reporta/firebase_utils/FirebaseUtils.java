package com.example.android.reporta.firebase_utils;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseUtils {

    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
