package com.torahli.myapplication.hkbc.userinfo;

import android.arch.lifecycle.MutableLiveData;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.hkbc.userinfo.bean.UserInfo;

import javax.annotation.Nonnull;

/**
 * 用户信息管理者
 */
public class UserInfoManager {
    public static final String TAG = "UserInfoManager";
    @Nonnull
    private UserInfo userinfo;
    @Nonnull
    private MutableLiveData<UserInfo> userInfoLiveData;

    private static volatile UserInfoManager instance;

    private UserInfoManager() {
        userInfoLiveData = new MutableLiveData<>();
        userinfo = new UserInfo();
    }

    @Nonnull
    public MutableLiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public UserInfoManager setUserinfo(String userName, String userHeadUrl) {
        if (Tlog.isShowLogCat()) {
            Tlog.d(TAG, "setUserinfo --- userName:" + userName + "\n userHeadUrl:" + userHeadUrl);
        }
        userinfo.setUserInfo(userName, userHeadUrl);
        return this;
    }

    public void notifyUserInfoChanged() {
        userInfoLiveData.setValue(userinfo);
    }

    public UserInfoManager setUserDetail(String userDefen, String userGrade) {
        if (Tlog.isShowLogCat()) {
            Tlog.d(TAG, "setUserDetail --- userDefen:" + userDefen + "\n userGrade:" + userGrade);
        }
        userinfo.setUserDetails(userDefen, userGrade);
        return this;
    }

    public static UserInfoManager getInstance() {
        if (instance == null) {
            synchronized (UserInfoManager.class) {
                if (instance == null) {
                    instance = new UserInfoManager();
                }
            }
        }
        return instance;
    }

    public synchronized static void release() {

    }

}
