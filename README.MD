# 一个针对特定论坛美化的“浏览器”APP
- TODO
    - rxjava 内存泄漏
    - 优化登录体验
        - 记录上次登录账号密码
    - 下一页
        - 大图页图片预览，快速定位
    - 自动检查更新测试
# 开发规范
- 区分dataBean和UIBean
    - UIBean可以添加用于界面提示的字段，比如网络结果。dataBean不能添加
    - dataBean的hashCode和equals方法要能够把相同数据源的2个对象判断为相等


# 用到的开源框架
    - rxjava、retrofit、okHttp、ViewModel+LiveData
        - retrofit教程 https://www.jianshu.com/p/308f3c54abdd
        - okio教程 https://juejin.im/post/5856680c8e450a006c6474bd
    - 带进度的进度条 https://github.com/daimajia/NumberProgressBar
    - 各种dialog https://github.com/afollestad/material-dialogs/blob/master/README_OLD.md
    - retrofit下载器 https://github.com/Tamicer/FastDownloader
    - Rx Preferences https://github.com/f2prateek/rx-preferences