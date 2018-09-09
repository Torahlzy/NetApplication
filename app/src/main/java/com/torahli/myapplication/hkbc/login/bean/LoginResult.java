package com.torahli.myapplication.hkbc.login.bean;

import com.torahli.myapplication.framwork.bean.BaseLiveData;

public class LoginResult extends BaseLiveData {
    private boolean succeed;
    private String loginMsg;

    public LoginResult setError(int error, String msg) {
        setErrorAndMsg(error, msg);
        return this;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getLoginMsg() {
        return loginMsg;
    }

    public void setLoginMsg(String loginMsg) {
        this.loginMsg = loginMsg;
    }
}
