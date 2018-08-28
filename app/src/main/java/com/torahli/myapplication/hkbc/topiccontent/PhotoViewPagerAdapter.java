package com.torahli.myapplication.hkbc.topiccontent;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.chrisbanes.photoview.PhotoView;
import com.torahli.myapplication.R;
import com.torahli.myapplication.hkbc.net.HKBCProtocol;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;

public class PhotoViewPagerAdapter extends PagerAdapter {
    public static final String TAG = "PhotoViewPagerAdapter";
    @Nonnull
    private final RequestManager activityGlide;
    @Nonnull
    private final LayoutInflater layoutInflater;
    List<View> views = new ArrayList<>();
    @Nonnull
    private final List<String> mData = new ArrayList<>();

    public PhotoViewPagerAdapter(List<String> data, @Nonnull RequestManager activityGlide,
                                 LayoutInflater layoutInflater) {
        if (data != null) {
            this.mData.addAll(data);
        }
        this.activityGlide = activityGlide;
        this.layoutInflater = layoutInflater;
    }

    /**
     * 设置最新数据
     *
     * @param data
     */
    public void setNewData(List<String> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {//必须实现，实例化
        View pagerPhotoView = layoutInflater.inflate(R.layout.pager_photoview, container, false);
        container.addView(pagerPhotoView);
        final PhotoView photoView = pagerPhotoView.findViewById(R.id.photoview_photo);
        final NumberProgressBar progressBar = pagerPhotoView.findViewById(R.id.pb_photo_load_progress);
        String url = HKBCProtocol.getWholeUrl(mData.get(position));
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
                super.onResourceReady(resource, transition);
                progressBar.setVisibility(View.GONE);
            }
        });

        return pagerPhotoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {//必须实现，销毁
        container.removeView((View) object);
    }

}
