package com.torahli.myapplication.hkbc.topiccontent.portrait;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.chrisbanes.photoview.PhotoView;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.framwork.util.SystemUtil;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

public class PhotoListQuickAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

    @Nonnull
    private final RequestManager activityGlide;

    public PhotoListQuickAdapter(@Nullable List<File> data, @Nonnull RequestManager activityGlide) {
        super(R.layout.pager_photoview, data);
        this.activityGlide = activityGlide;
    }

    @Override
    protected void convert(final BaseViewHolder helper, File item) {
        final PhotoView photoView = helper.getView(R.id.photoview_photo);
        final NumberProgressBar progressBar = helper.getView(R.id.pb_photo_load_progress);

        Tlog.d("torahlog", "PhotoListQuickAdapter.convert(..)--url:" + item);

        //glide加载
        activityGlide.load(item).into(new DrawableImageViewTarget(photoView) {
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
                ViewGroup.LayoutParams itemViewLayoutParams = helper.itemView.getLayoutParams();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) photoView.getLayoutParams();
                itemViewLayoutParams.width = layoutParams.width = BaseActivity.getScreenWidth();
                itemViewLayoutParams.height = layoutParams.height = SystemUtil.getWrapHeightForMatchWidth(
                        BaseActivity.getScreenWidth(), height * 1.0f / width);
                photoView.setLayoutParams(layoutParams);
                helper.itemView.setLayoutParams(itemViewLayoutParams);

                Tlog.d("torahlog",
                        "PhotoListQuickAdapter.onResourceReady(..)--bounds:" + height + " " + width);
                super.onResourceReady(resource, transition);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
