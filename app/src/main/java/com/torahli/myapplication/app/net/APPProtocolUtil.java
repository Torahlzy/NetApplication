package com.torahli.myapplication.app.net;

import com.torahli.myapplication.AppConfig;
import com.torahli.myapplication.framwork.retrofit.MyRetrofit;
import com.torahli.myapplication.framwork.util.ProtocolUtil;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

public class APPProtocolUtil extends ProtocolUtil {
    /**
     * 检查更新
     *
     * @return
     */
    public static Flowable<String> checkUpdate() {
        CheckUpdateProtocol checkUpdateProtocol = MyRetrofit.create(
                getHostWithProtocol(AppConfig.CHECK_PDATE_JSON_URL))
                .create(CheckUpdateProtocol.class);
        Flowable<String> updateMsg = checkUpdateProtocol.checkUpdate();
        return updateMsg;
    }

    /**
     * 开始下载
     *
     * @param url
     * @return
     */
    public static Flowable<ResponseBody> startDownload(String url) {
        DownloadProtocol downloadProtocol = MyRetrofit.create(
                getHostWithProtocol(url))
                .create(DownloadProtocol.class);
        Flowable<ResponseBody> updateMsg = downloadProtocol.startDownload(url);
        return updateMsg;
    }

}
