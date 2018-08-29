package com.torahli.myapplication.app.update;

import android.arch.lifecycle.MutableLiveData;

import com.torahli.myapplication.app.update.bean.UpdateInfo;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.framwork.vm.BaseViewModel;

import javax.annotation.Nonnull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

public class CheckUpdateViewModel extends BaseViewModel {
    @Nonnull
    MutableLiveData<UpdateInfo> liveData;

    public MutableLiveData<UpdateInfo> getContentLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        return liveData;
    }

    public void checkUpdate() {
        APPProtocolUtil.checkUpdate().subscribeOn(Schedulers.io())
                .map(new Function<String, UpdateInfo>() {
                    @Override
                    public UpdateInfo apply(String s) throws Exception {
                        return UpdateInfoParser.parser(s);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<UpdateInfo>() {
                    @Override
                    public void onNext(UpdateInfo updateInfo) {
                        liveData.setValue(updateInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Tlog.printException("torahlog", e);
                        UpdateInfo value = new UpdateInfo();
                        liveData.setValue(value.setError(NetErrorType.NetError, "报错" + e.getMessage()));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
