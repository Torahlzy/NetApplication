package com.torahli.myapplication.hkbc.topiccontent.picdetails;

import androidx.lifecycle.Observer;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.github.chrisbanes.photoview.PhotoView;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.topiccontent.TopicContentViewModel;
import com.torahli.myapplication.hkbc.topiccontent.bean.TopicContent;

import javax.annotation.Nonnull;

public class PicDetailActivity extends BaseActivity {

    public static final String INTENT_LINK = NavigationUtil.INTENT_LINK;
    private String mlink;
    @Nonnull
    private TopicContentViewModel topicContentViewModel;
    @Nonnull
    protected RequestManager activityGlide;
    private ProgressBar mPageProgress;
    private PhotoView mPhotoView;
    private int maxSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_topic_details);
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


        mPageProgress = findViewById(R.id.topic_details_page_progress);
        mPageProgress = findViewById(R.id.topic_details_photoview_photo);

        topicContentViewModel = new ViewModelProvider(this).get(TopicContentViewModel.class);
        topicContentViewModel.getContentLiveData().observe(this, new Observer<TopicContent>() {
            @Override
            public void onChanged(@Nullable TopicContent topicContent) {
                if (Tlog.isShowLogCat()) {
                    Tlog.d(TAG, "onChanged --- topicContent:" + topicContent);
                }
                mPageProgress.setVisibility(View.GONE);

            }
        });

    }

    private void initData() {
        topicContentViewModel.initData(mlink);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}