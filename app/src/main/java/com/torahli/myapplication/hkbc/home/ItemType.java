package com.torahli.myapplication.hkbc.home;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef
@Retention(RetentionPolicy.SOURCE)
public @interface ItemType {
    int Banners = 1;
    int Topic = 2;
}