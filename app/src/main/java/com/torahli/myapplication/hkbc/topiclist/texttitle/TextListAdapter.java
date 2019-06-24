package com.torahli.myapplication.hkbc.topiclist.texttitle;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.Topic;

public class TextListAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {
    private final BaseFragment fragment;

    public TextListAdapter(BaseFragment fragment) {
        super(R.layout.fragment_hk_item_texttopic, null);
        this.fragment = fragment;
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Topic topic = getData().get(position);
                jumpToTopicContent(topic);
            }
        });
    }

    private void jumpToTopicContent(Topic topic) {
        if (Tlog.isShowLogCat()) {
            Tlog.i(TAG, "准备打开主题--- topic:" + topic);
        }
        NavigationUtil.startPicContent(fragment.getActivity(), topic);
    }

    @Override
    protected void convert(BaseViewHolder helper, Topic item) {

        TextView title = helper.getView(R.id.hk_tv_title);
        TextView author = helper.getView(R.id.hk_tv_author);
        TextView time = helper.getView(R.id.hk_tv_time);

        title.setText(item.getTitle());
        author.setText(item.getAuthor());
        time.setText(item.getTimeStr());
    }
}
