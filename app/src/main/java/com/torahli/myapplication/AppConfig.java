package com.torahli.myapplication;

/**
 * 不同app配置不同，可变参数应该写到这里
 */
public class AppConfig {
    /**
     * logcat打印附加tag
     */
    public static final String TAG_APPEND = " torahlog";

    public static final boolean debug = BuildConfig._debug;

    public static final String CHECK_PDATE_JSON_URL = "https://github.com/Torahlzy/NetApplication/raw/master/raw/notice.json";
    /**
     * sd卡的文件夹路径
     * xml\file_paths.xml
     */
    public static final String SDCARD_FOLDER = "MyNetApp";

    public static final boolean showLogcat = true;
}
