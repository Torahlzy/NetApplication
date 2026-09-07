package com.torahli.myapplication.hkbc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.torahli.myapplication.R;
import com.torahli.myapplication.app.update.CheckUpdateViewModel;
import com.torahli.myapplication.app.update.bean.UpdateInfo;
import com.torahli.myapplication.app.update.download.DownLoadAPKUtil;
import com.torahli.myapplication.framwork.GlideApp;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.framwork.util.SystemUtil;
import com.torahli.myapplication.hkbc.home.HomePageFragment;
import com.torahli.myapplication.hkbc.login.LoginActivity;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;
import com.torahli.myapplication.hkbc.setting.SettingsActivity;
import com.torahli.myapplication.hkbc.userinfo.UserInfoManager;
import com.torahli.myapplication.hkbc.userinfo.bean.UserInfo;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 主Activity
 *
 * @author torah
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, DownLoadAPKUtil.IView {

    public static final int INSTALL_PACKAGES_REQUESTCODE = 110;
    public static final int ACTION_MANAGE_UNKNOWN_APP_SOURCES = 111;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private CheckUpdateViewModel checkUpdateViewModel;
    private View fab;
    private boolean hasLogin;
    private long preClickTimeMills;
    private DownLoadAPKUtil downLoadAPKUtil;

    @Override
    public Activity getActivity() {
        return this;
    }

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
        initPermission();
        initView();
        initHome();
        initNaviView();
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            checkUpdate();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    private void checkUpdate() {
        checkUpdateViewModel = new ViewModelProvider(this).get(CheckUpdateViewModel.class);
        checkUpdateViewModel.getContentLiveData().observe(this, new Observer<UpdateInfo>() {
            @Override
            public void onChanged(@Nullable UpdateInfo updateInfo) {
                if (Tlog.isShowLogCat()) {
                    Tlog.d(TAG, "onChanged --- updateInfo:" + updateInfo);
                }

                if (updateInfo != null && updateInfo.isAvailable()) {
                    UpdateInfo.Update update = updateInfo.getUpdate();
                    judgeStartDownLoad(update);
                } else {
                    Toast.makeText(MainActivity.this, "检查更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkUpdateViewModel.checkUpdate();
    }

    /**
     * 提示用户可升级
     *
     * @param update
     */
    private void judgeStartDownLoad(UpdateInfo.Update update) {
        if (SystemUtil.getAppVersionCode() < update.versionCode) {
            //开始下载
            downLoadAPKUtil = new DownLoadAPKUtil();
            downLoadAPKUtil.startDownLoad(update, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkUpdate();
                } else {
                    Toast.makeText(MainActivity.this, "无存储卡权限将不能自动升级", Toast.LENGTH_SHORT).show();
                }
                break;
            case INSTALL_PACKAGES_REQUESTCODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downLoadAPKUtil.continueInstall(this);
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTION_MANAGE_UNKNOWN_APP_SOURCES:
                downLoadAPKUtil.continueInstall(this);
                break;
            default:
                break;
        }
    }

    private void initView() {
        Toolbar toolBar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips("todo");
            }
        });
        drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView = (NavigationView) this.findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
    }

    private void initNaviView() {
        View headerView = navView.getHeaderView(0);
        final ImageView headImage = (ImageView) headerView.findViewById(R.id.head_image);
        final TextView userName = (TextView) headerView.findViewById(R.id.head_title);
        final TextView userContent = (TextView) headerView.findViewById(R.id.head_content);
        headImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View it) {
                if (hasLogin) {
                    if (System.currentTimeMillis() - preClickTimeMills < 500) {
                        LoginActivity.startLoginActivity(MainActivity.this);
                    } else {
                        preClickTimeMills = System.currentTimeMillis();
                        showToast("您已经登录，双击头像可以替换其他账号");
                    }
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    LoginActivity.startLoginActivity(MainActivity.this);
                }
            }
        });
        UserInfoManager.getInstance().getUserInfoLiveData()
                .observe(this, new Observer<UserInfo>() {
                    @Override
                    public void onChanged(@Nullable UserInfo userinfo) {
                        if (userinfo != null && userinfo.isLogin()) {
                            hasLogin = true;
                            userName.setText(userinfo.getUserName());
                            GlideApp.with(MainActivity.this)
                                    .load(HKBCProtocolUtil.getWholeUrl(userinfo.getUserHeadUrl()))
                                    .placeholder(R.drawable.ic_default_user)
                                    .error(R.drawable.ic_default_user)
                                    .circleCrop()
                                    .into(headImage);
                            userContent.setText(userinfo.getUserDetails());
                            showTips("欢迎回来，" + userinfo.getUserName());
                        } else {
                            hasLogin = false;
                            userName.setText(R.string.myapp_name);
                            headImage.setImageResource(R.drawable.ic_default_user);
                            userContent.setText(R.string.app_login_tip);
                            showTips("请在侧边栏登录，否则部分帖子可能加载失败");
                        }

                    }
                });
    }

    private void initHome() {
        if (findFragment(HomePageFragment.class) == null) {
            loadRootFragment(R.id.hk_main_content, HomePageFragment.newInstance(this));
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressedSupport();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Menu menu) {
        this.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 右上角三个点
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧边栏选项
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.nav_camera) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (item.getItemId() == R.id.nav_setting) {
            SettingsActivity.startSettingActivity(this);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void showTips(String msg) {
        Snackbar.make(fab, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
