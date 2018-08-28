package com.torahli.myapplication.framwork.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef
@Retention(RetentionPolicy.SOURCE)
public @interface NetErrorType {
    int NoError = 0;
    int NetError = 1;
    int NoDataError = 2;
}