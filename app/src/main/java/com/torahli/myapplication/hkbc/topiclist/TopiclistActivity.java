package com.torahli.myapplication.hkbc.topiclist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.hkbc.databean.ILink;

public class TopiclistActivity extends BaseActivity {
    public static final String INTENT_LINK = "intent_link";
    public static final String INTENT_TITLE = "intent_title";
    private FloatingActionButton fab;
    private Toolbar toolbar;

    public static void startTopicListActivty(Context context, String title, ILink link) {
        Intent intent = new Intent(context, TopiclistActivity.class);
        intent.putExtra(INTENT_LINK, link.getLink());
        intent.putExtra(INTENT_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_topiclist);
        initViews();
        initArgs();
    }

    private void setupActionBar() {
//透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
    private void initArgs() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString(INTENT_TITLE);

        toolbar.setTitle(title);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_fl_topiclist, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showTips(String msg) {
        Snackbar.make(fab, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
