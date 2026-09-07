package com.torahli.myapplication.hkbc.topiclist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.activity.BaseActivity;
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
    private GridLayoutManager layoutManager;
    private TopicListViewModel topicListViewModel;
    private SwipeRefreshLayout refreshLayout;
    private String title;
    private boolean loadingMore = false;

    public static TopicListFragment newInstance(BaseActivity activity, String link, String title) {
        TopicListFragment fragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_LINK, link);
        bundle.putString(INTENT_TITLE, title);
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                topicListViewModel.initData(mLink);
            }
        });

        mRecyclerView = view.findViewById(R.id.rl_topiclist);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        adapter = new TopicListAdapter(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        //滚动到底部时加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (loadingMore || refreshLayout.isRefreshing()) {
                    return;
                }
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (adapter.getItemCount() > 0 && lastVisibleItem >= adapter.getItemCount() - 3) {
                    loadingMore = true;
                    topicListViewModel.loadMore();
                }
            }
        });

        topicListViewModel = new ViewModelProvider(this).get(TopicListViewModel.class);
        topicListViewModel.getTopicListLiveData().observe(this, new Observer<TopicList>() {
            @Override
            public void onChanged(@Nullable TopicList topicList) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }

                if (topicList == null || topicList.isError()) {
                    loadingMore = false;
                    Toast.makeText(getActivity(), "获取帖子列表失败", Toast.LENGTH_SHORT).show();
                } else {
                    if (topicList.isInit()) {
                        adapter.setNewData(topicList.getTopicList());
                    } else {
                        adapter.addData(topicList.getTopicList());
                    }
                    loadingMore = false;
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
        title = arguments.getString(INTENT_TITLE);
    }

    @Override
    public String getTitle() {
        return title;
    }
}
