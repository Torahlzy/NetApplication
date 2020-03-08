package com.torahli.myapplication.hkbc.topiclist.texttitle;

import androidx.lifecycle.Observer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import javax.annotation.Nonnull;

public class TextTitleListFragment extends BaseFragment {
    public static final String INTENT_LINK = "intent_link";
    public static final String INTENT_TITLE = "intent_title";
    private String mLink;
    private RecyclerView mRecyclerView;
    private TextListAdapter adapter;
    private TextTopicListViewModel topicListViewModel;
    private EasyRefreshLayout refreshLayout;
    private String title;

    public static TextTitleListFragment newInstance(BaseActivity activity, String link, String title) {

        TextTitleListFragment fragment = new TextTitleListFragment();
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
        adapter = new TextListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getNoneNullActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);

        topicListViewModel = new ViewModelProvider(this).get(TextTopicListViewModel.class);
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
        title = arguments.getString(INTENT_TITLE);

    }

    @Override
    public String getTitle() {
        return title;
    }
}
