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
import com.ajguan.library.LoadModel;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import javax.annotation.Nonnull;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopicListFragment extends BaseFragment {
    private String mLink;
    private RecyclerView mRecyclerView;
    private TopicListAdapter adapter;
    private TopicListViewModel topicListViewModel;

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
        EasyRefreshLayout refreshLayout = view.findViewById(R.id.hk_topiclist_easyrefresh);
        refreshLayout.setLoadMoreModel(LoadModel.NONE);
        refreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
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
                if (topicList == null || topicList.isError()) {
                    Toast.makeText(getActivity(), "获取帖子列表失败", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.setNewData(topicList.getTopicList());
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
            mLink = arguments.getString(TopiclistActivity.INTENT_LINK);
        }
    }
}
