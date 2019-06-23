package com.torahli.myapplication.hkbc.topiccontent.portrait;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.torahli.myapplication.hkbc.databean.ILink;
import com.torahli.myapplication.hkbc.topiccontent.TopicContentViewModel;
import com.torahli.myapplication.hkbc.topiccontent.bean.TopicContent;

import javax.annotation.Nonnull;

/**
 * 图片列表页_纵向
 */
public class TopicContentPortraitActivity extends BaseActivity {
    public static final String INTENT_LINK = "INTENT_LINK";
    private String mlink;
    @Nonnull
    private TopicContentViewModel topicContentViewModel;
    private RecyclerView mImgRecyclerView;
    @Nonnull
    protected RequestManager activityGlide;
    private PhotoListQuickAdapter pagerAdapter;
    private ProgressBar mPageProgress;
    private TextView mTvCurpage;
    private int maxSize;
    private LinearLayoutManager layoutManager;

    /**
     * 启动主题详情页
     *
     * @param context
     */
    public static void startTopicContentActivity(Context context, ILink link) {
        Intent intent = new Intent(context, TopicContentPortraitActivity.class);
        intent.putExtra(INTENT_LINK, link.getLink());
        context.startActivity(intent);
    }

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
//                    pagerAdapter.notifyDataSetChanged();
                    maxSize = topicContent.getImgList().size();
                    mTvCurpage.setText("1/" + maxSize);
                } else {
                    Toast.makeText(TopicContentPortraitActivity.this, "获取详情失败", Toast.LENGTH_SHORT).show();
                }
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
        mImgRecyclerView.clearOnScrollListeners();
    }
}
