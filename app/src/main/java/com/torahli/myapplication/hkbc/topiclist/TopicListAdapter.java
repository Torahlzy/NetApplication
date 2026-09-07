package com.torahli.myapplication.hkbc.topiclist;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.framwork.util.SystemUtil;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 原生 RecyclerView.Adapter（替代原 BaseRecyclerViewAdapterHelper）
 */
public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.Holder> {
    private final BaseFragment fragment;
    private final List<Topic> data = new ArrayList<>();

    public TopicListAdapter(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public void setNewData(List<Topic> topics) {
        data.clear();
        if (topics != null) {
            data.addAll(topics);
        }
        notifyDataSetChanged();
    }

    public void addData(List<Topic> topics) {
        int start = data.size();
        if (topics != null) {
            data.addAll(topics);
        }
        notifyItemRangeInserted(start, data.size() - start);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.fragment_topic_list_item, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        Topic item = data.get(position);
        ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
        layoutParams.width = SystemUtil.getScreenWidth(fragment.getActivity()) / 2
                - SystemUtil.dp2px(8);
        holder.img.setLayoutParams(layoutParams);

        String wholeUrl = HKBCProtocolUtil.getWholeUrl(item.getPicUrl());
        if (TextUtils.isEmpty(wholeUrl)) {
            //解析不到图时直接显示失败占位图，避免用空地址去请求
            holder.img.setImageResource(R.drawable.ic_common_fail_svg);
        } else {
            Glide.with(fragment)
                    .load(wholeUrl)
                    //Glide 5：error/placeholder 移到 RequestOptions
                    .apply(RequestOptions.errorOf(R.drawable.ic_common_fail_svg))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            //仅失败时打印：具体地址与解码层异常原因
                            Tlog.w("图片加载", "列表图片加载失败 url=" + model
                                    + "\n原因=" + (e == null ? "unknown" : e.getMessage()));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.img);
        }
        holder.tv.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getBindingAdapterPosition();
                if (pos >= 0 && pos < data.size()) {
                    jumpToTopicContent(data.get(pos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void jumpToTopicContent(Topic topic) {
        NavigationUtil.startPicContent(fragment.getActivity(), topic);
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView img;
        final TextView tv;

        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.topic_list_item_img);
            tv = itemView.findViewById(R.id.topic_list_item_tv);
        }
    }
}
