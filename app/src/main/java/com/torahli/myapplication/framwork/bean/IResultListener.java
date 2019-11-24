package com.torahli.myapplication.framwork.bean;

import android.support.annotation.Nullable;

public interface IResultListener<T> {
    void onSucceed(T t);

    void onError(@Nullable Throwable e, String msg);
}
