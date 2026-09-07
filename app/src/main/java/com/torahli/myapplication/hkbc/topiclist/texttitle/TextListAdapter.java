package com.torahli.myapplication.hkbc.topiclist.texttitle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * 原生 RecyclerView.Adapter（替代原 BaseRecyclerViewAdapterHelper）
 */
public class TextListAdapter extends RecyclerView.Adapter<TextListAdapter.Holder> {
    private final BaseFragment fragment;
    private final List<Topic> data = new ArrayList<>();

    public TextListAdapter(BaseFragment fragment) {
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
        View view = View.inflate(parent.getContext(), R.layout.fragment_hk_item_texttopic, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        Topic item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        holder.time.setText(item.getTimeStr());
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
        if (Tlog.isShowLogCat()) {
            Tlog.i("TextListAdapter", "准备打开主题--- topic:" + topic);
        }
        NavigationUtil.startPicContent(fragment.getActivity(), topic);
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView author;
        final TextView time;

        Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.hk_tv_title);
            author = itemView.findViewById(R.id.hk_tv_author);
            time = itemView.findViewById(R.id.hk_tv_time);
        }
    }
}
