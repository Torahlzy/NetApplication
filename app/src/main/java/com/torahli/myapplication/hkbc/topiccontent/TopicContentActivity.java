package com.torahli.myapplication.hkbc.topiccontent;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.pic.selectdir.ListDirModel;
import com.torahli.myapplication.hkbc.databean.ILink;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * 主题详情页
 */
public class TopicContentActivity extends BaseActivity {
    public static final String INTENT_LINK = "INTENT_LINK";
    private String mlink;
    @Nonnull
    private ListDirModel topicContentViewModel;
    private ViewPager mViewPager;
    @Nonnull
    protected RequestManager activityGlide;
    private PhotoViewPagerAdapter pagerAdapter;
    private ProgressBar mPageProgress;
    private TextView mTvCurpage;
    private int maxSize;

    /**
     * 启动主题详情页
     *
     * @param context
     */
    public static void startTopicContentActivity(Context context, ILink link) {
        Intent intent = new Intent(context, TopicContentActivity.class);
        intent.putExtra(INTENT_LINK, link.getLink());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        topicContentViewModel = ViewModelProviders.of(this).get(ListDirModel.class);
        topicContentViewModel.getTopicListLiveData().observe(this, new Observer<List<File>>() {
            @Override
            public void onChanged(@Nullable List<File> files) {

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
        topicContentViewModel.loadDirFile(new File(mlink));
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
