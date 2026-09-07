# AGENTS.MD

面向 AI 编程助手 / 协作者的仓库说明。修改代码前请先阅读本文件与 `README.MD`。

## 项目概述

一个针对特定论坛（HKBC）美化的"浏览器" Android App：不是直接展示网页，而是通过
**抓取页面 HTML → 解析成数据 → 用原生 UI 重新渲染**，提供主页（Banner + 列表）、
图文主题列表、内容页（横屏/竖屏看图）、登录、设置（可改站点域名）等能力。


- 后续新增代码必须优先使用kt语言，除非更改的是旧的java类。
- 注释、README 均为中文，提交信息为中文，请沿用。

## 技术栈（2026 已现代化）

- 平台：Android
- 架构：MVVM —— `androidx.lifecycle`（`lifecycle-viewmodel/livedata:2.11`）
- 网络：Retrofit 3.0 + OkHttp 4.12 + RxJava 2（响应体为 `String` 原始 HTML）
- 解析：jsoup 1.23 + Gson
- UI：RecyclerView、Material Components（material 1.14，原 support-design）、
  ViewPager2（首页轮播为 `HomeAdapter` 内置实现）、PhotoView 2.3（androidx 版）、
  SwipeRefreshLayout（替代原 EasyRefreshLayout）、约束布局 2.2
- 导航：`Fragmentation` 的 AndroidX 移植版 **`fragmentationx-core:1.0.2`**（包名仍是
  `me.yokeyword.fragmentation`，仅依赖坐标不同）
- 图片：Glide 4.16（AppGlideModule 生成 `GlideApp`）
- 其他：PersistentCookieJar、Stetho、jsr305 + JetBrains annotations
- 已移除：Support Library / android.arch / BRVAH / banner / EasyRefreshLayout /
  material-dialogs / NumberProgressBar / ProgressManager / RxPreferences / 友盟统计

## 代码结构

代码按三个顶层包组织（均在 `app/src/main/java/com/torahli/myapplication/` 下）：

```
app/          # 应用层：检查更新、下载 APK、SharedPreferences（SharedPrefsKey）
  net/        #   APPProtocolUtil / CheckUpdateProtocol / DownloadProtocol
  update/     #   更新逻辑（CheckUpdateViewModel、DownLoadAPKUtil…）
framwork/     # 通用框架层，与具体站点无关，新功能优先复用这里
  activity/   #   BaseActivity（AppCompatActivity 链，含静态 getScreenWidth()）
  fragment/   #   BaseFragment（普通页面基类）/ BaseNavFragment（可再开 fragment 的页面）
  vm/         #   BaseViewModel（所有 ViewModel 的父类）
  bean/       #   BaseLiveData / NetErrorType / DeProguard
  retrofit/   #   MyRetrofit / MyHttpClient / 拦截器 / StringConverterFactory
  util/       #   ProtocolUtil / SystemUtil
  Tlog.java   #   日志工具
hkbc/         # 业务层：针对目标论坛的全部功能
  net/        #   HKBCProtocolUtil + 各 Protocol 接口（Retrofit 定义）
  login/  home/  topiclist/(含 texttitle)  topiccontent/(含 portrait)
  userinfo/  setting/  databean/
  MainActivity / NavigationUtil / AppConfig / MainApplication（root 下）
```

## 架构与数据流（重要）

每个功能模块基本遵循同一种写法，改代码时先按既有模块照抄结构：

```
Retrofit 请求（Protocol 接口, 返回 Flowable<String> 原始HTML）
  → Parser（jsoup/正则解析 HTML 成 dataBean/UIBean）
    → ViewModel（extends BaseViewModel，持有 LiveData，做 RxJava 调度）
      → Fragment/Activity（订阅 LiveData 刷新 UI）
        → 原生 RecyclerView.Adapter 渲染
```

要点：

- 站点地址：域名可在设置页修改，存于默认 SharedPreferences；请求统一走
  `HKBCProtocolUtil`（父类 `ProtocolUtil`），`MyRetrofit.create(BASEURL)` 动态创建。
- 页面里的链接常是相对路径（缺域名），拼完整地址用
  `HKBCProtocolUtil.getWholeUrl(...)`，不要各自手拼。
- ViewModel 获取一律 `new ViewModelProvider(owner).get(XxxViewModel.class)`。
- 列表页不再使用 BRVAH：均为原生 `RecyclerView.Adapter`，
  `setNewData/addData/notify*` 需自己管理；下拉刷新 = `SwipeRefreshLayout`，
  加载更多 = RecyclerView 滚动到接近底部触发（见 `TopicListFragment` 模式）。
- 首页轮播在 `HomeAdapter.BannerHolder` 内用 ViewPager2 + Glide 自实现
  （代码控制高度、3 秒自动播放、标题角标）；生命周期由
  `HomePageFragment.onResume/onPause` → `HomeAdapter.onResume/onPause` 驱动。
- 网络错误/状态提示字段放在 UIBean 层（如继承 `BaseLiveData`），不要在 dataBean 里
  塞界面状态。

## 开发规范（请遵守）

来源为 `README.MD` + 代码惯例，均为既有约定：

1. **区分 dataBean 与 UIBean**：
   - UIBean 可加用于界面提示的字段（如网络结果、加载状态）；dataBean 不允许。
   - dataBean 的 `hashCode` / `equals` 必须能判断"同一数据源的 2 个对象相等"。
2. **Parser 只做解析**，不碰网络与 UI；ViewModel 不做 UI 操作；Fragment/Activity
   不直接发请求。
3. 新页面/新网络接口按"上面数据流"的既有模块复制改造，尽量复用
   `framwork` 下的 Base 类（继承而不是另起炉灶）。
4. 常量/开关类放 `AppConfig`。
5. 日志用 `Tlog`，遵循既有输出习惯；不要引入新日志体系。
6. 保持中文注释；

## 常见任务指引

- **改列表/主页 UI**：改对应 `*Adapter`（原生 RecyclerView.Adapter）与 item 布局
  （`app/src/main/res/layout`），解析逻辑不动。
- **站点页面结构变了导致解析失败**：定位到对应 `*Parser`，用 jsoup selector
  对照新 HTML 修改；改动后用真机/日志验证。
- **新增一个站点页面入口**：在 `databean` 定义模型 → `hkbc/net` 加 Protocol →
  `HKBCProtocolUtil` 加静态方法 → 新建 `*Parser` → `*ViewModel` →
  `*Fragment`/`*Activity`，参考 `topiclist` 或 `topiccontent` 模块。
- **新增应用级请求**（检查更新等）：参考 `app/net` + `app/update`，走
  `APPProtocolUtil`。

## 提醒

- 从 `MainActivity` 用 `loadRootFragment` 挂 `HomePageFragment`；列表页之间用
  `NavigationUtil` 的 `extraTransaction().start(...)`（fragmentationx，包名不变）。
- 修改后不要跑 `./gradlew :app:assembleDebug` 编译。告知用户手动编译
