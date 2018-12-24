package com.torahli.myapplication.game.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.game.demo.manager.SimpleManager;
import com.torahli.myapplication.app.MainActivity;

import me.yokeyword.fragmentation.ISupportFragment;

public class MainGameFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView mainRecyclerview;
    private MainGameFragAdapter adapter;
    private SimpleManager simpleManager;
    private SimpleManager.IView simpleViewImpl = new SimpleManager.IView() {
        @Override
        public void addsceanRecord(String msg) {
            adapter.addData(msg);
            mainRecyclerview.smoothScrollToPosition(adapter.getData().size() - 1);
        }
    };
    private EasyRefreshLayout refreshLayout;

    public static ISupportFragment newInstance(MainActivity mainActivity) {
        MainGameFragment frag = new MainGameFragment();
        frag.setNoneNullActivity(mainActivity);
        return frag;
    }

    @Override
    public String getTitle() {
        return "游戏";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initData() {
        simpleManager = SimpleManager.getInstance();
        simpleManager.setViewImpl(simpleViewImpl);
    }

    private void initView(View view) {
        initBtns(view);


        mainRecyclerview = (RecyclerView) view.findViewById(R.id.game_main_recyclerview);
        adapter = new MainGameFragAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getNoneNullActivity());
        mainRecyclerview.setLayoutManager(layoutManager);
        mainRecyclerview.setAdapter(adapter);
        //下拉刷新
        refreshLayout = (EasyRefreshLayout) view.findViewById(R.id.hk_home_easyrefresh);
        refreshLayout.setLoadMoreModel(LoadModel.NONE);
        refreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                refreshLayout.refreshComplete();
            }
        });

    }

    private void initBtns(View view) {
        View btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        View btnBga = view.findViewById(R.id.btn_bag);
        btnBga.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next) {
            simpleManager.next();
        } else if (view.getId() == R.id.btn_bag) {
            simpleManager.showPlayerBag();
        }
    }
}
