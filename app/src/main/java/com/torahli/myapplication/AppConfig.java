package com.torahli.myapplication;

/**
 * 不同app配置不同，可变参数应该写到这里
 */
public class AppConfig {
    /**
     * todo 区分上架渠道
     */
    public static final String CHANNEL = "any";
    /**
     * logcat打印附加tag
     */
    public static final String TAG_APPEND = " torahlog";

    public static final boolean debug = true;

    public static final String UMENG_APP_KEY = "5b6ea00ef43e4852fc0000ef";
    public static final String UMENG_PUSH_SECRET = "";
    public static final String UMENG_CHANNEL = CHANNEL;

    public static final String CHECK_PDATE_JSON_URL = "https://github.com/Torahlzy/NetApplication/raw/master/raw/notice.json";
    /**
     * sd卡的文件夹路径
     * xml\file_paths.xml
     */
    public static final String SDCARD_FOLDER = "MyNetApp";

    public static final boolean showLogcat = true;
}
