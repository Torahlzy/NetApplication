package com.torahli.myapplication.hkbc.login;

import androidx.lifecycle.MutableLiveData;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.IResultListener;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.framwork.vm.BaseViewModel;
import com.torahli.myapplication.hkbc.login.bean.LoginResult;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

/**
 * 登录操作
 */
public class LoginViewModel extends BaseViewModel {

    private MutableLiveData<LoginResult> loginResultLiveData;

    @Nonnull
    public MutableLiveData<LoginResult> getLoginResultLiveData() {
        if (loginResultLiveData == null) {
            loginResultLiveData = new MutableLiveData<>();
        }

        return loginResultLiveData;
    }

    public void startLogin(final String account, final String password, final IResultListener<String> listener) {
        Map<String, String> params = new HashMap<String, String>(6);
        params.put("fastloginfield", "username");
        params.put("username", account);
        params.put("cookietime", "2592000");
        params.put("password", password);
        params.put("quickforward", "yes");
        params.put("handlekey", "ls");
        HKBCProtocolUtil.getLogin(params).subscribeOn(Schedulers.io()).map(new Function<String, LoginResult>() {
            @Override
            public LoginResult apply(String s) throws Exception {
                return LoginResultParser.parser(s);

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new DefaultSubscriber<LoginResult>() {
            @Override
            public void onNext(LoginResult result) {
                if (Tlog.isShowLogCat()) {
                    Tlog.d(TAG, "onNext --- result:" + result);
                }
                if (loginResultLiveData != null) {
                    loginResultLiveData.setValue(result);//更新livedata
                }
                if (result == null || !result.isSucceed()) {
                    String errorMsg = result != null ? result.getLoginMsg() : "登陆失败";
                    if (listener != null) {
                        listener.onError(null, errorMsg);
                    }
                } else {
                    if (listener != null) {
                        listener.onSucceed("登陆成功");
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Tlog.printException("torahlog", t);
                LoginResult result = new LoginResult().setError(NetErrorType.NetError, t.getMessage());
                loginResultLiveData.setValue(result);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
