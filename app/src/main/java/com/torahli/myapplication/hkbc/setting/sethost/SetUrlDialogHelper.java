package com.torahli.myapplication.hkbc.setting.sethost;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.app.sharedpreferences.SharedPrefsKey;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

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
        final Activity activity = view.getActivity();
        final EditText editText = new EditText(activity);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHint("exp:http://www.baidu.com/");
        if (!TextUtils.isEmpty(HKBCProtocolUtil.BASEURL)) {
            editText.setText(HKBCProtocolUtil.BASEURL);
        }

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("设置域名")
                .setMessage("初始使用必须设置域名，若不知道域名，去获得app的地方找")
                .setView(editText)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .create();
        //替换默认的确定按钮行为：校验通过才保存并关闭
        dialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
            @Override
            public void onShow(android.content.DialogInterface d) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        String url = checkHost(String.valueOf(editText.getText()));
                        view.showToast(url);
                        if (!TextUtils.isEmpty(url)) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.getApplication());
                            preferences.edit().putString(SharedPrefsKey.hostUrl, url).apply();
                            HKBCProtocolUtil.BASEURL = url;
                            view.onHostSetted();
                            dialog.dismiss();
                        } else {
                            view.showToast("网址填写错误");
                        }
                    }
                });
            }
        });
        dialog.show();
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
        if (!s.startsWith("http")) {
            s = "http://" + s;
        }

        if (s.endsWith("/")) {
            return s;
        } else {
            return s + "/";
        }
    }
}
