package com.torahli.myapplication.app.update.download;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.app.net.APPProtocolUtil;
import com.torahli.myapplication.app.update.bean.UpdateInfo;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.util.SystemUtil;
import com.torahli.myapplication.hkbc.MainActivity;

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

    public interface IView {
        Activity getActivity();

        void showToast(String msg);
    }

    private File installFile = null;
    private boolean hasRequest = false;

    /**
     * 开始下载apk
     *
     * @param update
     */
    public void startDownLoad(@Nonnull final UpdateInfo.Update update, final IView view) {
        APPProtocolUtil.startDownload(update.url)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        if (responseBody.contentLength() > 0) {
                            BufferedSource source = responseBody.source();
                            return saveToDisk(source, update);
                        } else {
                            return "";
                        }

                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void onNext(String str) {
                        if (!TextUtils.isEmpty(str)) {
                            if (Tlog.isShowLogCat()) {
                                Tlog.d(TAG, "onNext --- str:" + str);
                            }
                            onDownloadFinish(str, update, view);
                        } else if (Tlog.isShowLogCat()) {
                            if (Tlog.isShowLogCat()) {
                                Tlog.d(TAG, "onNext --- 保存文件失败");
                            }
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

    private void onDownloadFinish(final String path, UpdateInfo.Update update, final IView view) {
        new MaterialDialog.Builder(view.getActivity())
                .positiveText("开始安装")
                .negativeText("取消")
                .title("已下载最新版本")
                .content(update.desc +
                        "\n是否更新？（安装时可能需要授予安装apk权限）")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        install(new File(path), view);
                    }
                }).autoDismiss(true)
                .show();
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

    private void installUpdate(File file) {
        Context context = MainApplication.getApplication();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(context, "com.torahli.myapplication.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 检查权限安装apk
     *
     * @param file
     * @param view
     * @return
     */
    private void install(File file, IView view) {
        Context context = MainApplication.getApplication();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if (b) {
                installUpdate(file);
            } else {
                //请求安装未知应用来源的权限
                if (!hasRequest) {
                    installFile = file;
                    ActivityCompat.requestPermissions(view.getActivity(),
                            new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},
                            MainActivity.INSTALL_PACKAGES_REQUESTCODE);
                    hasRequest = true;
                } else {
                    view.showToast("获取权限失败，无法升级");
                }
            }
        } else {
            installUpdate(file);
        }
    }

    public void continueInstall(IView view) {
        install(installFile, view);
        installFile = null;
    }
}
