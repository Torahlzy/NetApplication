package com.torahli.myapplication.hkbc.topiclist.texttitle;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.R;
import com.torahli.myapplication.app.sharedpreferences.SharedPrefsKey;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import javax.annotation.Nonnull;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class TextTitleListFragment extends BaseFragment {
    public static final String INTENT_LINK = "intent_link";
    public static final String INTENT_TITLE = "intent_title";
    private String mLink;
    private RecyclerView mRecyclerView;
    private TextListAdapter adapter;
    private TextTopicListViewModel topicListViewModel;
    private EasyRefreshLayout refreshLayout;
    private String title;
    /**
     * 0：无过滤
     * 1:过滤“在线观看”
     */
    private int filterType;
    private RxSharedPreferences rxPreferences;

    public static TextTitleListFragment newInstance(String link, String title) {
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

        topicListViewModel = ViewModelProviders.of(this).get(TextTopicListViewModel.class);
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
        rxPreferences = MainApplication.getApplication().getRxPreferences();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mLink = arguments.getString(INTENT_LINK);
        }
        title = arguments.getString(INTENT_TITLE);

        rxPreferences.getInteger(SharedPrefsKey.filterOnline).asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Tlog.d("torahlog", "--filterType:" + filterType);
                        filterType = integer;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, 0, 0, filterType == 1 ? "只显示'在线观看'" : "全部显示");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                filterType = filterType == 1 ? 0 : 1;
                Flowable.just(filterType)
                        .subscribeOn(Schedulers.io())
                        .subscribe(rxPreferences.getInteger(SharedPrefsKey.filterOnline).asConsumer());
                onFilterTypeChanged();
                break;
            default:
                break;
        }
        return true;
    }

    private void onFilterTypeChanged() {

    }

    @Override
    public String getTitle() {
        return title;
    }
}
