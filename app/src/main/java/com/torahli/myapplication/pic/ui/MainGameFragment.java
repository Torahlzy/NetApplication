package com.torahli.myapplication.pic.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.pic.selectdir.ListDirModel;
import com.torahli.myapplication.hkbc.MainActivity;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.ILink;

import java.io.File;
import java.util.List;

import io.reactivex.observers.DefaultObserver;
import me.yokeyword.fragmentation.ISupportFragment;

public class MainGameFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView mainRecyclerview;
    private SelectFileDirAdapter adapter;
    private RxPermissions rxPermissions;
    private File currentFileDir;

    private EasyRefreshLayout refreshLayout;
    private ListDirModel listDirModel;

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
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            showTips("需要读取存储卡权限");
                        } else {
                            readSdCardData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取sd卡目录
     */
    private void readSdCardData() {
        listDirModel.loadDirFile(null);
    }

    private void initView(View view) {
        initBtns(view);

        mainRecyclerview = (RecyclerView) view.findViewById(R.id.game_main_recyclerview);
        adapter = new SelectFileDirAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getNoneNullActivity());
        mainRecyclerview.setLayoutManager(layoutManager);
        mainRecyclerview.setAdapter(adapter);
        //下拉刷新
        refreshLayout = (EasyRefreshLayout) view.findViewById(R.id.hk_home_easyrefresh);

        //数据绑定
        listDirModel = ViewModelProviders.of(this).get(ListDirModel.class);
        listDirModel.getTopicListLiveData().observe(this, new Observer<List<File>>() {
            @Override
            public void onChanged(@Nullable List<File> topicList) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.refreshComplete();
                }
                if (topicList != null) {
                    topicList.add(0, new File(currentFileDir.getParent()));
                }
                adapter.setNewData(topicList);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                File item = (File) adapter.getItem(position);
                currentFileDir = item;
                listDirModel.loadDirFile(item);
            }
        });
        currentFileDir = Environment.getExternalStorageDirectory();
    }

    private void initBtns(View view) {
        View btnNext = view.findViewById(R.id.btn_select);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_select) {
            startPicView();
        }
    }

    /**
     * 打开图片墙
     */
    private void startPicView() {
        NavigationUtil.startPicContent(getActivity(), new ILink() {
            @Override
            public String getLink() {
                return currentFileDir.getAbsolutePath();
            }
        });
    }
}
