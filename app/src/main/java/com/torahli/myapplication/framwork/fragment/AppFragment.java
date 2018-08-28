package com.torahli.myapplication.framwork.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

abstract class AppFragment extends Fragment {
    @NonNull
    protected String TAG;

    public AppFragment() {
        TAG = this.getClass().getSimpleName();
    }
}
