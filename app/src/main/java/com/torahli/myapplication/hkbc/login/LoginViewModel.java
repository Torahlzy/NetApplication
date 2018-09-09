package com.torahli.myapplication.hkbc.login;

import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.app.sharedpreferences.SharedPrefsKey;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.framwork.vm.BaseViewModel;
import com.torahli.myapplication.hkbc.login.bean.LoginResult;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import io.reactivex.Flowable;
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

    public void startLogin(final String account, final String password) {
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
                loginResultLiveData.setValue(result);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.getApplication());
                RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
                Flowable.just(account)
                        .subscribeOn(Schedulers.io())
                        .subscribe(rxPreferences.getString(SharedPrefsKey.username).asConsumer());
                Flowable.just(password)
                        .subscribeOn(Schedulers.io())
                        .subscribe(rxPreferences.getString(SharedPrefsKey.password).asConsumer());
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
