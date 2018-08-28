package com.torahli.myapplication.framwork.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

abstract class AppActivity extends AppCompatActivity {
    @NonNull
    protected static String TAG;

    public AppActivity() {
        TAG = this.getClass().getSimpleName();
    }
}
