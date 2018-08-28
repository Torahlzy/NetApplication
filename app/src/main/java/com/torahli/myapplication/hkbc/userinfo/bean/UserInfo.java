package com.torahli.myapplication.hkbc.userinfo.bean;

import android.text.TextUtils;

import com.torahli.myapplication.framwork.bean.LiveDataBase;

/**
 * 用户登录状态
 */
public class UserInfo extends LiveDataBase {
    String userName;
    String userHeadUrl;
    boolean hasLogin;
    /**
     * 用户积分
     */
    private String userDefen;
    /**
     * 用户等级
     */
    private String userGrade;

    public String getUserName() {
        return userName;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserInfo(String userName, String userHeadUrl) {
        this.userName = userName;
        this.userHeadUrl = userHeadUrl;
        if (TextUtils.isEmpty(userName)) {
            hasLogin = false;
        } else {
            hasLogin = true;
        }
    }

    public void setUserDetails(String userDefen, String userGrade) {
        this.userDefen = userDefen;
        this.userGrade = userGrade;
    }

    public String getUserDetails() {
        if (!TextUtils.isEmpty(userDefen)) {
            return userDefen + " | " + userGrade;
        }
        return userGrade;
    }

    public boolean isLogin() {
        return hasLogin;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", userHeadUrl='" + userHeadUrl + '\'' +
                ", hasLogin=" + hasLogin +
                ", userDefen='" + userDefen + '\'' +
                ", userGrade='" + userGrade + '\'' +
                ", error=" + error +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
