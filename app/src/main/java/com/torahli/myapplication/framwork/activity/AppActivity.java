package com.torahli.myapplication.framwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.torahli.myapplication.framwork.util.SystemUtil;

import java.lang.ref.WeakReference;

abstract class AppActivity extends AppCompatActivity {
    @NonNull
    protected static String TAG;

    private static int ScreenWidth;
    private static WeakReference<AppActivity> lastActivity;

    public AppActivity() {
        TAG = this.getClass().getSimpleName();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        lastActivity = new WeakReference<>(this);
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        if (ScreenWidth == 0) {
            AppActivity lastAct = lastActivity.get();
            if (lastAct != null) {
                ScreenWidth = SystemUtil.getScreenWidth(lastAct);
            }
        }
        return ScreenWidth;
    }
}
