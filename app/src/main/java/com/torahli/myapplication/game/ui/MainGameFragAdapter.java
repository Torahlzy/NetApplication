package com.torahli.myapplication.game.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.util.SystemUtil;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.home.HomePageFragment;
import com.torahli.myapplication.hkbc.home.ItemType;
import com.torahli.myapplication.hkbc.home.bean.Banners;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;
import com.torahli.myapplication.hkbc.support.BannerGlideImageLoader;
import com.torahli.myapplication.hkbc.topiccontent.TopicContentActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

public class MainGameFragAdapter extends BaseQuickAdapter<CharSequence, BaseViewHolder> {

    public MainGameFragAdapter() {
        super(R.layout.item_game_text);

    }

    @Override
    protected void convert(BaseViewHolder helper, CharSequence item) {
        TextView tv = helper.getView(R.id.tv_game);
        tv.setText(item);
    }
}
