package com.torahli.myapplication.framwork.bean;

import androidx.annotation.Nullable;

public interface IResultListener<T> {
    void onSucceed(T t);

    void onError(@Nullable Throwable e, String msg);
}
