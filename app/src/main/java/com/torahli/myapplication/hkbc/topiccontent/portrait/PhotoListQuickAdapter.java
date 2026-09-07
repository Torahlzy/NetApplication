package com.torahli.myapplication.hkbc.topiccontent.portrait;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * 图片列表页（纵向）的适配器：原生 RecyclerView.Adapter，用 Glide 加载。
 * <p>
 * 关键点：item 里的 PhotoView 是 match_parent（整屏），如果在 bind 时不给 Glide 明确尺寸，
 * Glide 会进入 WAITING_FOR_SIZE 死等 View 回报宽高（曾导致图片一直 loading、请求不发）。
 * 因此这里对每个请求显式指定 {@link Target#SIZE_ORIGINAL}，让 Glide 跳过等尺寸直接加载，
 * 解码完成后再按图片比例调整 item 高度。网络并发由 MyHttpClient 的 OkHttp 客户端统一限制。
 */
public class PhotoListQuickAdapter extends RecyclerView.Adapter<PhotoListQuickAdapter.Holder> {

    @Nonnull
    private final List<String> mData = new ArrayList<>();

    public PhotoListQuickAdapter(@Nullable List<String> data) {
        if (data != null) {
            this.mData.addAll(data);
        }
    }

    public void setNewData(@Nullable List<String> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.pager_photoview, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final PhotoView photoView = holder.photoView;
        final ProgressBar progressBar = holder.progressBar;
        final String url = HKBCProtocolUtil.getWholeUrl(mData.get(position));
        progressBar.setVisibility(View.VISIBLE);

        RequestManager glide = Glide.with(photoView);
        glide.load(url)
                // 不给 View 等尺寸：直接按原图尺寸加载（跳过 WAITING_FOR_SIZE 死等）
                .apply(RequestOptions.overrideOf(Target.SIZE_ORIGINAL))
                .into(new DrawableImageViewTarget(photoView) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        progressBar.setVisibility(View.GONE);
                        photoView.setImageResource(R.drawable.ic_error_outline_black_24dp);
                        Tlog.w("图片加载", "内容页图片加载失败 url=" + url);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition) {
                        int width = resource.getIntrinsicWidth();
                        int height = resource.getIntrinsicHeight();
                        ViewGroup.LayoutParams itemLp = holder.itemView.getLayoutParams();
                        int itemWidth = BaseActivity.getScreenWidth();
                        if (itemLp != null && width > 0 && height > 0) {
                            itemLp.width = itemWidth;
                            itemLp.height = Math.round(itemWidth * height * 1.0f / width);
                            holder.itemView.setLayoutParams(itemLp);
                        }
                        super.onResourceReady(resource, transition);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onViewRecycled(@NonNull Holder holder) {
        super.onViewRecycled(holder);
        //item 滚出屏幕时取消未完成的请求，避免无谓的并发下载
        Glide.with(holder.photoView).clear(holder.photoView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final PhotoView photoView;
        final ProgressBar progressBar;

        Holder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photoview_photo);
            progressBar = itemView.findViewById(R.id.pb_photo_load_progress);
        }
    }
}
