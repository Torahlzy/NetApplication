package com.torahli.myapplication.hkbc.home;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.util.SystemUtil;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.TextTopic;
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.home.bean.Banners;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * 首页列表：Banners（ViewPager2 轮播，代码控制高度与自动播放）+ Topic 条目
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNERS = 1;
    private static final int TYPE_TOPIC = 2;
    private static final long AUTO_PLAY_INTERVAL = 3000L;

    @Nonnull
    private final RequestManager fragmentGlide;
    @Nonnull
    private final HomePageFragment homePageFragment;
    @Nonnull
    private final List<Object> data = new ArrayList<>();
    @Nonnull
    private final List<BannerHolder> bannerHolders = new ArrayList<>();

    public HomeAdapter(@Nonnull RequestManager fragmentGlide, HomePageFragment homePageFragment) {
        this.fragmentGlide = fragmentGlide;
        this.homePageFragment = homePageFragment;
    }

    public void setNewData(@Nullable List<Object> newData) {
        data.clear();
        if (newData != null) {
            data.addAll(newData);
        }
        notifyDataSetChanged();
    }

    /**
     * 页面可见时，恢复轮播自动播放
     */
    public void onResume() {
        for (BannerHolder holder : bannerHolders) {
            holder.startAutoPlay();
        }
    }

    /**
     * 页面不可见时，停止轮播自动播放
     */
    public void onPause() {
        Iterator<BannerHolder> iterator = bannerHolders.iterator();
        while (iterator.hasNext()) {
            BannerHolder holder = iterator.next();
            holder.stopAutoPlay();
            if (holder.viewPager == null || !holder.viewPager.isAttachedToWindow()) {
                iterator.remove();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        return item instanceof Banners ? TYPE_BANNERS : TYPE_TOPIC;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNERS) {
            View view = View.inflate(parent.getContext(), R.layout.fragment_hk_item_banners, null);
            return new BannerHolder(view);
        }
        View view = View.inflate(parent.getContext(), R.layout.fragment_hk_item_topic, null);
        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = data.get(position);
        if (holder instanceof BannerHolder) {
            ((BannerHolder) holder).bind((Banners) item);
        } else if (holder instanceof TopicHolder) {
            ((TopicHolder) holder).bind((Topic) item);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof BannerHolder) {
            ((BannerHolder) holder).stopAutoPlay();
            bannerHolders.remove(holder);
        }
        super.onViewRecycled(holder);
    }

    private void jumpTopicListPage(Topic entity) {
        if (entity instanceof TextTopic) {
            //打开文字列表样式的页面
            Tlog.i("页面跳转", "【点击】首页的文字主题条目 → 打开文字列表页 TextTitleListFragment，title="
                    + entity.getTitle() + "，link=" + entity.getLink());
            NavigationUtil.startTextTopicList(homePageFragment, entity.getLink(), entity.getTitle());
        } else {
            //跳转"带预览图样式列表"页
            Tlog.i("页面跳转", "【点击】首页的带图主题条目 → 打开图文列表页 TopicListFragment，title="
                    + entity.getTitle() + "，link=" + entity.getLink());
            NavigationUtil.startPicTopicList(homePageFragment, entity.getLink(), entity.getTitle());
        }
    }

    /**
     * 跳转主题详情（图片内容页）
     */
    private void jumpTopicContentPage(Topic link) {
        Tlog.i("页面跳转", "【点击】首页轮播图 → 打开图片内容页，title="
                + link.getTitle() + "，link=" + link.getLink());
        NavigationUtil.startPicContent(homePageFragment.getActivity(), link);
    }

    class TopicHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final ImageView coverView;
        private Topic topic;

        TopicHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.hk_tv_title);
            coverView = itemView.findViewById(R.id.hk_iv_title_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (topic != null) {
                        jumpTopicListPage(topic);
                    }
                }
            });
        }

        void bind(final Topic topic) {
            this.topic = topic;
            titleView.setText(topic.getTitle());
            if (topic.getPicUrl() == null || topic.getPicUrl().isEmpty()) {
                coverView.setVisibility(View.GONE);
            } else {
                coverView.setVisibility(View.VISIBLE);
                fragmentGlide.load(HKBCProtocolUtil.getWholeUrl(topic.getPicUrl()))
                        .into(new DrawableImageViewTarget(coverView) {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                coverView.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }

    /**
     * 轮播条目：ViewPager2 + Glide，代码控制高度，附自动播放
     */
    class BannerHolder extends RecyclerView.ViewHolder {
        final ViewPager2 viewPager;
        final TextView indicatorView;
        private final Handler handler = new Handler(Looper.getMainLooper());
        private final Runnable autoPlayTask = new Runnable() {
            @Override
            public void run() {
                if (viewPager == null || !viewPager.isAttachedToWindow()) {
                    return;
                }
                int count = viewPager.getAdapter() == null ? 0 : viewPager.getAdapter().getItemCount();
                if (count > 1) {
                    viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % count, true);
                }
                handler.postDelayed(this, AUTO_PLAY_INTERVAL);
            }
        };
        private List<Topic> topics = new ArrayList<>();
        @SuppressWarnings("unused")
        private Banners banners;

        BannerHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.hk_home_item_banner);
            indicatorView = itemView.findViewById(R.id.hk_banner_indicator);
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    updateIndicator(position);
                }
            });
            viewPager.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    ImageView imageView = new ImageView(parent.getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(lp);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Object tag = v.getTag();
                            if (tag instanceof Topic) {
                                jumpTopicContentPage((Topic) tag);
                            }
                        }
                    });
                    return new RecyclerView.ViewHolder(imageView) {
                    };
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    Topic topic = topics.get(position);
                    ImageView imageView = (ImageView) holder.itemView;
                    imageView.setTag(topic);
                    fragmentGlide.load(HKBCProtocolUtil.getWholeUrl(topic.getPicUrl())).into(imageView);
                }

                @Override
                public int getItemCount() {
                    return topics.size();
                }
            });
        }

        void bind(Banners banners) {
            this.banners = banners;
            this.topics = banners.getTopicList();
            //根据网络图片计算出来的比例
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            lp.height = SystemUtil.getWrapHeightForMatchWidth(
                    SystemUtil.getScreenWidth(homePageFragment.getActivity()), 1.29139f);
            itemView.setLayoutParams(lp);
            viewPager.getAdapter().notifyDataSetChanged();
            updateIndicator(0);
            if (!bannerHolders.contains(this)) {
                bannerHolders.add(this);
            }
            startAutoPlay();
        }

        void updateIndicator(int position) {
            if (indicatorView == null || topics == null || topics.isEmpty()) {
                return;
            }
            int index = Math.max(0, Math.min(position, topics.size() - 1));
            String title = topics.get(index).getTitle();
            indicatorView.setText((index + 1) + "/" + topics.size() + " · " + title);
        }

        void startAutoPlay() {
            if (topics == null || topics.size() <= 1) {
                return;
            }
            handler.removeCallbacks(autoPlayTask);
            handler.postDelayed(autoPlayTask, AUTO_PLAY_INTERVAL);
        }

        void stopAutoPlay() {
            handler.removeCallbacks(autoPlayTask);
        }
    }
}
