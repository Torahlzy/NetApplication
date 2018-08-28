package com.torahli.myapplication.hkbc;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.torahli.myapplication.R;
import com.torahli.myapplication.R.id;
import com.torahli.myapplication.framwork.GlideApp;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.hkbc.home.HomePageFragment;
import com.torahli.myapplication.hkbc.login.LoginActivity;
import com.torahli.myapplication.hkbc.net.HKBCProtocol;
import com.torahli.myapplication.hkbc.userinfo.UserInfoManager;
import com.torahli.myapplication.hkbc.userinfo.bean.UserInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 主Activity
 *
 * @author torah
 */
public class MainActivity extends BaseActivity
        implements OnNavigationItemSelectedListener {
    /**
     * fragment的tag
     */
    @StringDef({Navigation.HOME, Navigation.TOPICLIST})
    @Retention(RetentionPolicy.SOURCE)
    @interface Navigation {
        String HOME = "home";
        String TOPICLIST = "topiclist";
    }

    private DrawerLayout drawer;
    private NavigationView navView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initHome();
        initNaviView();
    }

    private void initView() {
        Toolbar toolBar = (Toolbar) this.findViewById(id.toolbar);
        setSupportActionBar(toolBar);
        findViewById(id.fab).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener((DrawerListener) toggle);
        toggle.syncState();

        navView = (NavigationView) this.findViewById(id.nav_view);
        navView.setNavigationItemSelectedListener(this);
    }

    private void initNaviView() {
        View headerView = navView.getHeaderView(0);
        final ImageView headImage = (ImageView) headerView.findViewById(R.id.head_image);
        final TextView userName = (TextView) headerView.findViewById(R.id.head_title);
        final TextView userContent = (TextView) headerView.findViewById(R.id.head_content);
        headImage.setOnClickListener((OnClickListener) (new OnClickListener() {
            @Override
            public final void onClick(View it) {
                LoginActivity.startLoginActivity(MainActivity.this);
            }
        }));
        UserInfoManager.getInstance().getUserInfoLiveData()
                .observe(this, new Observer<UserInfo>() {
                    @Override
                    public void onChanged(@Nullable UserInfo userinfo) {
                        if (userinfo != null && userinfo.isLogin()) {
                            userName.setText(userinfo.getUserName());
                            GlideApp.with(MainActivity.this)
                                    .load(HKBCProtocol.getWholeUrl(userinfo.getUserHeadUrl()))
                                    .placeholder(R.drawable.ic_default_user)
                                    .error(R.drawable.ic_default_user)
                                    .circleCrop()
                                    .into(headImage);
                            userContent.setText(userinfo.getUserDetails());
                        } else {
                            userName.setText(R.string.myapp_name);
                            headImage.setImageResource(R.drawable.ic_default_user);
                            userContent.setText(R.string.app_login_tip);
                        }

                    }
                });
    }

    private void initHome() {
        //动态加载
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.hk_main_content, new HomePageFragment(), "home")
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Menu menu) {
        this.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:

            default:
                drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

}
