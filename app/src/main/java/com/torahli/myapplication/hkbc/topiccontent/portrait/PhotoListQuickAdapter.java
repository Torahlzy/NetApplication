package com.torahli.myapplication.hkbc.topiccontent.portrait;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.framwork.util.SystemUtil;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * 图片列表页（纵向）的适配器：原生 RecyclerView.Adapter
 */
public class PhotoListQuickAdapter extends RecyclerView.Adapter<PhotoListQuickAdapter.Holder> {

    @Nonnull
    private final RequestManager activityGlide;
    @Nonnull
    private final List<String> mData = new ArrayList<>();

    public PhotoListQuickAdapter(@Nullable List<String> data, @Nonnull RequestManager activityGlide) {
        if (data != null) {
            this.mData.addAll(data);
        }
        this.activityGlide = activityGlide;
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
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final PhotoView photoView = holder.photoView;
        final ProgressBar progressBar = holder.progressBar;
        String url = HKBCProtocolUtil.getWholeUrl(mData.get(position));
        progressBar.setVisibility(View.VISIBLE);
        Tlog.d("torahlog", "PhotoListQuickAdapter.onBindViewHolder(..)--url:" + url);

        //glide加载
        activityGlide.load(url).into(new DrawableImageViewTarget(photoView) {
            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                progressBar.setVisibility(View.GONE);
                photoView.setImageResource(R.drawable.ic_error_outline_black_24dp);
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                int height = resource.getIntrinsicHeight();
                int width = resource.getIntrinsicWidth();
                ViewGroup.LayoutParams itemViewLayoutParams = holder.itemView.getLayoutParams();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) photoView.getLayoutParams();
                int itemWidth = BaseActivity.getScreenWidth();
                if (layoutParams != null && itemViewLayoutParams != null && width > 0) {
                    itemViewLayoutParams.width = itemWidth;
                    itemViewLayoutParams.height = SystemUtil.getWrapHeightForMatchWidth(itemWidth, height * 1.0f / width);
                    photoView.setLayoutParams(layoutParams);
                    holder.itemView.setLayoutParams(itemViewLayoutParams);
                }
                Tlog.d("torahlog",
                        "PhotoListQuickAdapter.onResourceReady(..)--bounds:" + height + " " + width);
                super.onResourceReady(resource, transition);
                progressBar.setVisibility(View.GONE);
            }
        });
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
