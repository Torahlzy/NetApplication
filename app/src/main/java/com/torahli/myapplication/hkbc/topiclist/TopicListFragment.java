package com.torahli.myapplication.hkbc.topiclist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import javax.annotation.Nonnull;

/**
 * A placeholder fragment containing a simple view.
 * 交互方式：
 * 先加载第一页。提示
 * 拉到底部加载更多，添加到原数据，提示
 * 没有更多，提示
 */
public class TopicListFragment extends BaseFragment {
    public static final String INTENT_LINK = "intent_link";
    public static final String INTENT_TITLE = "intent_title";
    private String mLink;
    private RecyclerView mRecyclerView;
    private TopicListAdapter adapter;
    private TopicListViewModel topicListViewModel;
    private EasyRefreshLayout refreshLayout;

    public static TopicListFragment newInstance(String link,String title){
        TopicListFragment fragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_LINK,link);
        bundle.putString(INTENT_TITLE,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topiclist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArgs();
        initViews(view);
        initData();
    }

    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.hk_topiclist_easyrefresh);
        refreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                topicListViewModel.loadMore();
            }

            @Override
            public void onRefreshing() {
                topicListViewModel.initData(mLink);
            }
        });

        mRecyclerView = view.findViewById(R.id.rl_topiclist);
        adapter = new TopicListAdapter(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        mRecyclerView.setAdapter(adapter);

        topicListViewModel = ViewModelProviders.of(this).get(TopicListViewModel.class);
        topicListViewModel.getTopicListLiveData().observe(this, new Observer<TopicList>() {
            @Override
            public void onChanged(@Nullable TopicList topicList) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.refreshComplete();
                }

                if (topicList == null || topicList.isError()) {
                    if (refreshLayout.isLoading()) {
                        refreshLayout.loadMoreFail();
                    }
                    Toast.makeText(getActivity(), "获取帖子列表失败", Toast.LENGTH_SHORT).show();
                } else {
                    if (topicList.isInit()) {
                        adapter.setNewData(topicList.getTopicList());
                    } else {
                        adapter.addData(topicList.getTopicList());
                        if (refreshLayout.isLoading()) {
                            refreshLayout.loadMoreComplete();
                        }
                    }
                    showTips("加载第" + topicList.getPageName() + "页成功");
                }
            }
        });
    }

    private void initData() {
        topicListViewModel.initData(mLink);
    }

    private void initArgs() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mLink = arguments.getString(INTENT_LINK);
        }
        String title = arguments.getString(INTENT_TITLE);

    }
}
