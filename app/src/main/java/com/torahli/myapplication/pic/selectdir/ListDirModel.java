package com.torahli.myapplication.pic.selectdir;

import android.arch.lifecycle.MutableLiveData;
import android.os.Environment;

import com.torahli.myapplication.framwork.vm.BaseViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 主题列表
 */
public class ListDirModel extends BaseViewModel {
    @Nonnull
    MutableLiveData<List<File>> liveData;
    private LinkedHashMap<String, String> otherPages;
    private int nextIndex;//下一个网址在map中的索引。访问网络成功时再加

    public MutableLiveData<List<File>> getTopicListLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        return liveData;
    }

    public void loadDirFile(@Nullable File file) {
        Disposable subscribe = Observable.just(file)
                .map(new Function<File, File[]>() {
                    @Override
                    public File[] apply(File dirFile) throws Exception {
                        if (dirFile != null && dirFile.isDirectory()) {
                        } else {
                            dirFile = Environment.getExternalStorageDirectory();
                        }
                        File[] files = dirFile.listFiles();
                        return files;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File[]>() {
                    @Override
                    public void accept(File[] s) throws Exception {
                        ArrayList<File> arrayList = new ArrayList<File>(Arrays.asList(s));
                        liveData.setValue(arrayList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

}
