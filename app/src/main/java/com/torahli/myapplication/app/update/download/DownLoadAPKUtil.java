package com.torahli.myapplication.app.update.download;

import com.torahli.myapplication.app.net.APPProtocolUtil;
import com.torahli.myapplication.app.update.bean.UpdateInfo;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.util.SystemUtil;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class DownLoadAPKUtil {
    private static final String TAG = "update";

    /**
     * 开始下载apk
     *
     * @param update
     */
    public void startDownLoad(@Nonnull final UpdateInfo.Update update) {
        APPProtocolUtil.startDownload(update.url)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(ResponseBody responseBody) throws Exception {
                        if (responseBody.contentLength() > 0) {
                            BufferedSource source = responseBody.source();
                            return saveToDisk(source, update);
                        } else {
                            return "";
                        }

                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        if (Tlog.isShowLogCat()) {
                            Tlog.d(TAG, "onNext --- o:" + o);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Tlog.printException("torahlog", t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 保存到磁盘
     *
     * @param source
     * @param updateInfo
     * @return 文件路径
     */
    private String saveToDisk(BufferedSource source, UpdateInfo.Update updateInfo) {
        String appsdCardPath = SystemUtil.getAPPSDCardPath();
        String filePath = appsdCardPath + "update/" + updateInfo.versionName + "_" + updateInfo.versionCode + ".apk";
        BufferedSink bufferSink = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.getParentFile().mkdirs();
            file.createNewFile();
            bufferSink = Okio.buffer(Okio.sink(file));
            bufferSink.writeAll(source);
            return filePath;
        } catch (IOException e) {
            Tlog.printException("torahlog", e);
        } finally {
            if (bufferSink != null) {
                try {
                    bufferSink.close();
                } catch (IOException e) {
                    Tlog.printException("torahlog", e);
                }
            }
        }
        return "";
    }

}
