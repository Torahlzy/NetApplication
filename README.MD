# 一个针对特定论坛美化的“浏览器”APP
- TODO
    - rxjava 内存泄漏
    - bug:某张图片加载失败，后面不再加载？
# 开发规范
- 区分dataBean和UIBean
    - UIBean可以添加用于界面提示的字段，比如网络结果。dataBean不能添加
    - dataBean的hashCode和equals方法要能够把相同数据源的2个对象判断为相等

# 构建环境（2026 现代化）
- Gradle 9.6.0（wrapper 走腾讯镜像 https://mirrors.cloud.tencent.com/gradle/）+ AGP 9.3.2 + JDK ≥ 17（Gradle daemon 固定运行于 JDK 21，见 AGENTS.MD）
- Kotlin 由 AGP 9 内置编译（勿显式锁 kotlin-stdlib 版本）
- compileSdk 37 / minSdk 23 / targetSdk 37，全 AndroidX
- 本地构建：`./gradlew :app:assembleDebug`（详见 AGENTS.MD）

# 用到的开源框架
- androidx：appcompat / lifecycle(ViewModel+LiveData) / recyclerview / cardview /
  swiperefreshlayout / viewpager(+viewpager2) / drawerlayout / constraintlayout /
  material
- 网络：retrofit 3.0、okhttp 4.12、rxjava2/rxandroid、PersistentCookieJar、stetho
- 解析：jsoup、gson
- 图片：Glide 4.16（AppGlideModule）、PhotoView 2.3（androidx 版）
- 导航：fragmentationx-core 1.0.2（Fragmentation 的 androidx 移植，包名不变）
- 已移除：Support Library、BRVAH、banner、EasyRefreshLayout、material-dialogs、
  NumberProgressBar、ProgressManager、RxPreferences、友盟统计
