package com.torahli.myapplication.hkbc.home;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.torahli.myapplication.hkbc.home.bean.Banners;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;
import com.torahli.myapplication.hkbc.support.BannerGlideImageLoader;
import com.torahli.myapplication.hkbc.topiccontent.portrait.TopicContentPortraitActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

public class HomeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>
        implements LifecycleObserver {
    @Nonnull
    private final RequestManager fragmentGlide;
    @Nonnull
    private final HomePageFragment homePageFragment;
    @Nonnull
    private final List<WeakReference<Banner>> bannerViews = new ArrayList<>();

    public HomeAdapter(@Nonnull RequestManager fragmentGlide, HomePageFragment homePageFragment) {
        super(null);
        this.fragmentGlide = fragmentGlide;
        this.homePageFragment = homePageFragment;
        initType();
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity multiItemEntity = getData().get(position);
                if (multiItemEntity.getItemType() == ItemType.PicTopicList) {
                    jumpTopicListPage((Topic) multiItemEntity);
                } else if (multiItemEntity.getItemType() == ItemType.TextTopic) {
                    jumpTextTopicListPage((Topic) multiItemEntity);
                } else {
                    if (Tlog.isShowLogCat()) {
                        Tlog.w(TAG, "onItemClick --- multiItemEntity:" + multiItemEntity);
                    }
                }
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        for (WeakReference<Banner> bannerView : bannerViews) {
            Banner banner = bannerView.get();
            if (banner != null && banner.isAttachedToWindow()) {
                banner.startAutoPlay();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        for (WeakReference<Banner> bannerView : bannerViews) {
            Banner banner = bannerView.get();
            if (banner != null) {
                banner.stopAutoPlay();
            }
        }
    }

    /**
     * 缓存banner，方便生命周期时停止
     *
     * @param banner
     */
    private void addCacheBanner(Banner banner) {
        boolean hasCached = false;
        Iterator<WeakReference<Banner>> iterator = bannerViews.iterator();
        while (iterator.hasNext()) {
            WeakReference<Banner> wrBanner = iterator.next();
            Banner bannerTemp = wrBanner.get();
            if (bannerTemp != null) {
                if (bannerTemp == banner) {
                    hasCached = true;
                }
            } else {
                iterator.remove();
            }
        }
        if (!hasCached) {
            bannerViews.add(new WeakReference<Banner>(banner));
        }
    }

    /**
     * 跳转主题列表
     *
     * @param entity
     */
    private void jumpTopicListPage(Topic entity) {
        NavigationUtil.startPicTopicList(
                homePageFragment, entity.getLink(), entity.getTitle());
    }

    /**
     * 打开文字列表样式的页面
     *
     * @param entity
     */
    private void jumpTextTopicListPage(Topic entity) {
        NavigationUtil.startTextTopicList(
                homePageFragment, entity.getLink(), entity.getTitle());
    }

    private void initType() {
        addItemType(ItemType.Banners, R.layout.fragment_hk_item_banners);
        addItemType(ItemType.PicTopicList, R.layout.fragment_hk_item_topic);
        addItemType(ItemType.TextTopic, R.layout.fragment_hk_item_topic);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case ItemType.Banners:
                Banners banners = (Banners) item;
                convertForBanners(helper, banners);
                break;
            case ItemType.PicTopicList:
                Topic topic = (Topic) item;
                convertForTopic(helper, topic);
                break;
            case ItemType.TextTopic:
                Topic customTopic = (Topic) item;
                convertForTopic(helper, customTopic);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转主题详情
     *
     * @param link
     */
    private void jumpTopicContentPage(Topic link) {
        if (Tlog.isShowLogCat()) {
            Tlog.i(TAG, "准备打开 --- link:" + link);
        }
        TopicContentPortraitActivity.startTopicContentActivity(homePageFragment.getActivity(), link);
    }

    private void convertForBanners(BaseViewHolder helper, final Banners bannerData) {
        final Banner banner = helper.getView(R.id.hk_home_item_banner);
        addCacheBanner(banner);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) banner.getLayoutParams();
        layoutParams.height = SystemUtil.getWrapHeightForMatchWidth(
                //根据网络图片计算出来的比例
                SystemUtil.getScreenWidth(homePageFragment.getActivity()), 1.29139f);
        //https://github.com/youth5201314/banner
        banner.setImages(bannerData.getTopicList())
                .setBannerTitles(bannerData.getTopicListTitle())
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new BannerGlideImageLoader())
                .setDelayTime(3000)
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                List<Topic> topicList = bannerData.getTopicList();
                Topic topic = topicList.get(position);
                jumpTopicContentPage(topic);
            }
        });
    }

    private void convertForTopic(BaseViewHolder helper, Topic topic) {
        helper.setText(R.id.hk_tv_title, topic.getTitle());
        final ImageView cover = helper.getView(R.id.hk_iv_title_img);
        if (TextUtils.isEmpty(topic.getPicUrl())) {
            cover.setVisibility(View.GONE);
        } else {
            cover.setVisibility(View.VISIBLE);
            fragmentGlide.load(HKBCProtocolUtil.getWholeUrl(topic.getPicUrl())).into(new DrawableImageViewTarget(cover) {
                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    cover.setVisibility(View.GONE);
                }
            });
        }
    }
}
