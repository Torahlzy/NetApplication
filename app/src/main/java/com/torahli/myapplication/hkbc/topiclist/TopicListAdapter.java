package com.torahli.myapplication.hkbc.topiclist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.GlideApp;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.framwork.util.SystemUtil;
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;
import com.torahli.myapplication.hkbc.topiccontent.TopicContentActivity;

public class TopicListAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {
    private final BaseFragment fragment;

    public TopicListAdapter(BaseFragment fragment) {
        super(R.layout.fragment_topic_list_item, null);
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
            Tlog.i(TAG, "准备打开主题列表--- topic:" + topic);
        }
        TopicContentActivity.startTopicContentActivity(fragment.getActivity(), topic);
    }

    @Override
    protected void convert(BaseViewHolder helper, Topic item) {
        ImageView img = helper.getView(R.id.topic_list_item_img);
        ViewGroup.LayoutParams layoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
        layoutParams.width = SystemUtil.getScreenWidth(fragment.getActivity()) / 2
                - SystemUtil.dp2px(8);
        img.setLayoutParams(layoutParams);
        TextView tv = helper.getView(R.id.topic_list_item_tv);
        GlideApp.with(fragment)
                .load(HKBCProtocolUtil.getWholeUrl(item.getPicUrl()))
                .error(R.drawable.ic_common_fail_svg)
                .into(img);
        tv.setText(item.getTitle());
    }
}
