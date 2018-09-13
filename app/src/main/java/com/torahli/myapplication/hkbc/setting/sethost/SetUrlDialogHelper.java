package com.torahli.myapplication.hkbc.setting.sethost;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.app.sharedpreferences.SharedPrefsKey;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class SetUrlDialogHelper {
    public interface IView {
        Activity getActivity();

        /**
         * 设置完域名后回调此方法
         */
        void onHostSetted();

        void showToast(String msg);
    }

    public static void showSetUrlDialog(final IView view) {
        final RxSharedPreferences rxPreferences = MainApplication.getApplication().getRxPreferences();
        new MaterialDialog.Builder(view.getActivity())
                .title("设置域名")
                .content("初始使用必须设置域名，若不知道域名，去获得app的地方找")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("exp:http://www.baidu.com/", HKBCProtocolUtil.BASEURL, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Editable text = dialog.getInputEditText().getText();
                        String url = checkHost(String.valueOf(text));
                        if (!TextUtils.isEmpty(url)) {
                            Flowable.just(url)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(rxPreferences.getString(SharedPrefsKey.hostUrl).asConsumer());
                            HKBCProtocolUtil.BASEURL = url;
                            view.onHostSetted();
                        } else {
                            view.showToast("网址填写错误");
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showSetUrlDialog(view);
                                }
                            }, 1000);
                        }
                    }
                }).show();
    }

    /**
     * 检查填写的域名是否合法
     *
     * @param text
     * @return
     */
    public static String checkHost(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        String s = String.valueOf(text).toLowerCase();
        if (s.startsWith("http")) {
            if (s.endsWith("/")) {
                return s;
            } else {
                return s + "/";
            }
        }
        return "";
    }
}
