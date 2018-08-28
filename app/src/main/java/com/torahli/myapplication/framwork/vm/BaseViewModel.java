package com.torahli.myapplication.framwork.vm;

import android.arch.lifecycle.ViewModel;

import javax.annotation.Nonnull;

public class BaseViewModel extends ViewModel {
    @Nonnull
    protected String TAG;

    public BaseViewModel() {
        this.TAG = this.getClass().getSimpleName();
    }
}
