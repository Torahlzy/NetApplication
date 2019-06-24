package com.torahli.myapplication.hkbc.topiccontent;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.ILink;
import com.torahli.myapplication.hkbc.topiccontent.bean.TopicContent;

import javax.annotation.Nonnull;

/**
 * 主题详情页
 */
public class TopicContentActivity extends BaseActivity {
    public static final String INTENT_LINK = NavigationUtil.INTENT_LINK;
    private String mlink;
    @Nonnull
    private TopicContentViewModel topicContentViewModel;
    private ViewPager mViewPager;
    @Nonnull
    protected RequestManager activityGlide;
    private PhotoViewPagerAdapter pagerAdapter;
    private ProgressBar mPageProgress;
    private TextView mTvCurpage;
    private int maxSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_topic_content);
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

        mViewPager = findViewById(R.id.photo_viewpager);
        mTvCurpage = findViewById(R.id.tv_curpage);

        mPageProgress = findViewById(R.id.topic_content_page_progress);

        pagerAdapter = new PhotoViewPagerAdapter(null, activityGlide, getLayoutInflater());
        mViewPager.setAdapter(pagerAdapter);
        topicContentViewModel = ViewModelProviders.of(this).get(TopicContentViewModel.class);
        topicContentViewModel.getContentLiveData().observe(this, new Observer<TopicContent>() {
            @Override
            public void onChanged(@Nullable TopicContent topicContent) {
                if (Tlog.isShowLogCat()) {
                    Tlog.d(TAG, "onChanged --- topicContent:" + topicContent);
                }
                mPageProgress.setVisibility(View.GONE);
                if (topicContent != null && !topicContent.isError()) {
                    pagerAdapter.setNewData(topicContent.getImgList());
                    pagerAdapter.notifyDataSetChanged();
                    maxSize = topicContent.getImgList().size();
                    mTvCurpage.setText("1/" + maxSize);
                } else {
                    Toast.makeText(TopicContentActivity.this, "获取详情失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvCurpage.setText((position + 1) + "/" + maxSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        mViewPager.clearOnPageChangeListeners();
    }
}
