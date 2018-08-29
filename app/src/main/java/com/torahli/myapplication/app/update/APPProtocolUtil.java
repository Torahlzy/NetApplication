package com.torahli.myapplication.app.update;

import com.torahli.myapplication.AppConfig;
import com.torahli.myapplication.framwork.retrofit.MyRetrofit;
import com.torahli.myapplication.framwork.util.ProtocolUtil;

import io.reactivex.Flowable;

public class APPProtocolUtil extends ProtocolUtil {
    /**
     * 检查更新
     * @return
     */
    public static Flowable<String> checkUpdate() {
        CheckUpdateProtocol checkUpdateProtocol = MyRetrofit.create(
                getHostWithProtocol(AppConfig.CHECK_PDATE_JSON_URL))
                .create(CheckUpdateProtocol.class);
        Flowable<String> updateMsg = checkUpdateProtocol.checkUpdate();
        return updateMsg;
    }


}
