package com.torahli.myapplication.framwork.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

abstract class AppFragment extends Fragment {
    @NonNull
    protected String TAG;

    public AppFragment() {
        TAG = this.getClass().getSimpleName();
    }
}
