package com.torahli.myapplication.hkbc.topiccontent.portrait;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.datamanager.PicDataManager;
import com.torahli.myapplication.hkbc.topiccontent.TopicContentViewModel;
import com.torahli.myapplication.hkbc.topiccontent.bean.TopicContent;

import javax.annotation.Nonnull;

/**
 * 图片列表页_纵向
 */
public class TopicContentPortraitActivity extends BaseActivity {
    public static final String INTENT_LINK = NavigationUtil.INTENT_LINK;
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
                showContent();
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
        View foot = LayoutInflater.from(this).inflate(R.layout.item_root, mImgRecyclerView, false);
        pagerAdapter.addFooterView(foot);
        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Topic nextTopic = PicDataManager.getInstance().getNextTopic();
                if (nextTopic != null) {
                    mlink = nextTopic.getLink();
                    initData();
                } else {
                    showTips("已经到最后一项");
                }
            }
        });
    }

    private void initData() {
        topicContentViewModel.initData(mlink);
        showLoading();
    }

    private void showLoading() {
        mPageProgress.setVisibility(View.VISIBLE);
        mImgRecyclerView.setVisibility(View.GONE);
    }

    private void showContent() {
        mPageProgress.setVisibility(View.GONE);
        mImgRecyclerView.setVisibility(View.VISIBLE);
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
