package com.torahli.myapplication.hkbc.topiccontent.portrait;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.pic.selectdir.ListDirModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片列表页_纵向
 */
public class TopicContentPortraitActivity extends BaseActivity {
    public static final String INTENT_LINK = NavigationUtil.INTENT_LINK;
    private String mlink;
    @Nonnull
    private ListDirModel topicContentViewModel;
    private RecyclerView mImgRecyclerView;
    @Nonnull
    protected RequestManager activityGlide;
    private PhotoListQuickAdapter pagerAdapter;
    private ProgressBar mPageProgress;
    private TextView mTvCurpage;
    private int maxSize;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_portrait_photolist);
        activityGlide = Glide.with(this);

        initArgs();
        initViews();
        initData();
    }

    private void initArgs() {
        mlink = getIntent().getStringExtra(INTENT_LINK);
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mImgRecyclerView = findViewById(R.id.photo_recyclerView_list);
        mTvCurpage = findViewById(R.id.tv_curpage);

        mPageProgress = findViewById(R.id.topic_content_page_progress);

        pagerAdapter = new PhotoListQuickAdapter(null, activityGlide);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mImgRecyclerView.setLayoutManager(layoutManager);
        mImgRecyclerView.setAdapter(pagerAdapter);
        topicContentViewModel = ViewModelProviders.of(this).get(ListDirModel.class);
        topicContentViewModel.getTopicListLiveData().observe(this, new Observer<List<File>>() {
            @Override
            public void onChanged(@Nullable List<File> files) {
                filterPicsAndSetData(files);
            }
        });

        mImgRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = layoutManager.findLastVisibleItemPosition();
                    mTvCurpage.setText((position + 1) + "/" + maxSize);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 筛选图片并且设置数据
     *
     * @param files
     */
    private void filterPicsAndSetData(List<File> files) {
        Disposable subscribe = Observable.just(files)
                .map(new Function<List<File>, List<File>>() {
                    @Override
                    public List<File> apply(List<File> files) throws Exception {
                        List<File> ret = new ArrayList<>();
                        for (File file : files) {
                            if (!file.isDirectory()) {
                                if (file.getName().endsWith(".jpg") ||
                                        file.getName().endsWith(".bmp") ||
                                        file.getName().endsWith(".png")) {
                                    ret.add(file);
                                }
                            }
                        }
                        return ret;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> s) throws Exception {
                        ArrayList<File> arrayList = new ArrayList<File>(s);
                        pagerAdapter.setNewData(arrayList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void initData() {
        topicContentViewModel.loadDirFile(new File(mlink));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImgRecyclerView.clearOnScrollListeners();
    }
}
