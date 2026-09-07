package com.torahli.myapplication.hkbc.login;

import com.torahli.myapplication.hkbc.login.bean.LoginResult;


public class LoginResultParser {

    public static LoginResult parser(String doc) {
        LoginResult result = new LoginResult();
        if (doc.contains("登錄失敗") || doc.contains("登陆失败")) {
            result.setSucceed(false);
            result.setLoginMsg("登陆失败");
        } else {
            result.setSucceed(true);
        }
        return result;
    }
}
