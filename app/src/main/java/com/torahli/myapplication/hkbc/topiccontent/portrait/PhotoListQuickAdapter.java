package com.torahli.myapplication.hkbc.topiccontent.portrait;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import java.util.List;

import javax.annotation.Nonnull;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;

public class PhotoListQuickAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    @Nonnull
    private final RequestManager activityGlide;

    public PhotoListQuickAdapter(@Nullable List<String> data, @Nonnull RequestManager activityGlide) {
        super(R.layout.pager_photoview, data);
        this.activityGlide = activityGlide;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        final PhotoView photoView = helper.getView(R.id.photoview_photo);
        final NumberProgressBar progressBar = helper.getView(R.id.pb_photo_load_progress);
        final String url = HKBCProtocolUtil.getWholeUrl(item);
        progressBar.setVisibility(View.VISIBLE);
        // Glide 下载监听
        ProgressManager.getInstance().addResponseListener(url, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                int progress = (int) (progressInfo.getCurrentbytes() * 100 / progressInfo.getContentLength());
                progressBar.setMax(100);
                progressBar.setProgress(progress);
                if (progressInfo.isFinish()) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(long id, Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
        Tlog.d("torahlog", "PhotoListQuickAdapter.convert(..)--加载的图片地址:" + url);

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
        GestureDetector.OnDoubleTapListener onDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                NavigationUtil.startPicDetialsPage(mContext, url);
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        };
        photoView.setOnDoubleTapListener(onDoubleTapListener);
    }


}
