# 图片加载踩坑：防盗链与 Glide 一直 loading

> 时间：2026-09（Glide 4.16 → 5.0.9 升级期间）
> 范围：图文列表页封面图全部加载失败、图片内容页(纵向看图)一直 loading
> 结论速览：

| 症状 | 根因 | 修复 |
|---|---|---|
| 列表封面图全部失败(显示失败占位图)，页面 HTML 却正常 | 图片请求是"裸请求"：无浏览器 UA / 站点 Cookie / Referer，被站点防盗链/风控拒绝(实测不带 Referer 会被拒) | Glide 换自定义 OkHttp：共享页面 CookieJar + 移动浏览器 UA + 同源 Referer，同 host 并发≤3、超时放宽 |
| 图片内容页打开后**一直转圈 loading**，网络却完全正常 | Glide 拿不到 View 尺寸进入 `WAITING_FOR_SIZE` 死等：item 内 PhotoView 为整屏 `match_parent`，加载发生在 RecyclerView bind 阶段 | 每个请求显式 `override(Target.SIZE_ORIGINAL)`，让 Glide 跳过等尺寸直接加载 |

---

## 一、防盗链：图片请求要按网页 `<img>` 模拟

### 现象
- 图文列表页(TopicListFragment)标题、链接都正常，封面图全部显示失败占位图；页面 HTML(Retrofit)能正常打开。
- 差异点：页面请求带 Cookie + 浏览器 UA(Retrofit 客户端)；图片请求(Glide 默认)是裸 OkHttp。

### 排查证据
- 直接 curl 图片地址：**不带 Referer 曾返回 404**(防盗链/风控页)，**带本站 Referer 立即 200 JPEG**；
- 高并发 20+ 一起拉时行为不稳定(疑似被限速/风控)，说明站点对"不像浏览器的大并发抓图"不友好。

### 修复
集中在 `MyHttpClient` + `MyAppGlideModule`：
1. 图片请求与页面请求**共享同一个 PersistentCookieJar**(先访问页面拿到 Discuz cookie，`<img>` 再带着请求，模拟浏览器)；
2. 补浏览器请求头：移动端 UA(与页面请求同一常量)、图片 `Accept`、`Accept-Language`、**同源 Referer**(`scheme://host/`)；
3. OkHttp `Dispatcher` 限制 **同 host 并发 ≤ 3**(`setMaxRequests(3)` + `setMaxRequestsPerHost(3)`)；
4. 超时放宽：连接 15s / 读写 30s(该站单张响应本身偏慢，默认 10s 读超时会掐死慢请求)；
5. `MyAppGlideModule.registerComponents` 里 `registry.replace(GlideUrl→OkHttpUrlLoader.Factory(client))`，让 Glide 全部走该客户端。

### 启发
- 站点类 App 的图片请求不要裸发，**按网页 `<img>` 补齐 UA/Referer/Cookie** 是基本姿势；
- 排查此类问题：用"带不带 Referer/UA"做对照 curl 矩阵，能快速定性是否防盗链。

---

## 二、Glide "一直 loading"：WAITING_FOR_SIZE 死等 View 尺寸

### 现象
- 图片内容页(纵向看图，RecyclerView + 整屏 PhotoView)打开后**永远转圈**；
- 日志特征：`bind` → `glide受理`(target.onLoadStarted) 后，**再无任何进展**：没有 OkHttp 请求、没有成功、也没有失败——分钟级无新日志。

### 排查过程(关键方法)
1. 分层打点，区分"受理层 vs 传输层"：
   - Glide 层日志(bind/onLoadStarted/onResourceReady/onLoadFailed)；
   - OkHttp 层拦截器日志(`GET 开始 / => HTTP 200 / 网络异常 耗时`)。→ 发现**只有受理层日志，传输层从未开始**。
2. 用**裸 OkHttp 直接下载该图并上屏**做对照实验：3 张并发全部 `HTTP 200`(约 0.8s)+ 解码上屏正常 → **网络/服务器/请求头都没问题，问题在 Glide 引擎层**。
3. 读 Glide 5 源码 `SingleRequest.begin()` 定位根因：

   ```java
   status = WAITING_FOR_SIZE;
   if (override 尺寸有效) onSizeReady(...)   // 直接加载
   else target.getSize(this)                  // ← 等 View 回报宽高
   ...
   target.onLoadStarted(...)                  // ← "glide受理"日志在这时就会打印
   ```

   → `onLoadStarted` 在"等尺寸"阶段就会触发；item 里 PhotoView 是整屏 `match_parent`，加载又发生在 RecyclerView bind(布局)阶段，**View 尺寸一直未就绪 → Glide 永远 WAITING_FOR_SIZE**，请求根本不会发起(Glide 4/5 表现一致，并非 Glide 引擎 bug)。

### 修复
加载时给 Glide 明确尺寸，跳过等尺寸：

```java
Glide.with(photoView)
        .load(url)
        .apply(RequestOptions.overrideOf(Target.SIZE_ORIGINAL)) // 关键
        .into(...);
```

解码完成后在 `onResourceReady` 里按图片宽高比调整 item 高度，恢复"看图流"体验。

### 启发
- Glide 对 `match_parent`/尚未布局的 View 会**等尺寸**；在列表 bind 等早期时机加载整屏目标时，务必 `override(...)` 或保证尺寸已就绪；
- 现象"只有 onLoadStarted、永远没网络"应第一时间怀疑 WAITING_FOR_SIZE(读 `SingleRequest.begin()` 即可确认)；
- 对照实验(裸网络请求 bypass 库)是隔离"库问题 vs 网络问题"的最快手段。

---

## 三、排查经验：日志抓取与打点

- `Tlog` 全部输出 tag 都带 ` torahlog` 后缀：`grep "torahlog"` 即可全量过滤，无需分别按功能 tag 过滤；
- 抓"卡住"类问题要给足时间窗：`adb logcat -c` → 操作并等待(30~60s) → `adb logcat -d | grep torahlog > 文件`，别只复制前几百毫秒(很容易恰好截断在请求刚发出处)；
- 打点分层：bind(受理) / 真正网络请求(传输) / HTTP 码 / 终态(成功/失败)，日志一次打齐，避免来回好几轮；
- 完成后**删除临时开关/探针日志**(本项目每次排查结束都清理并提交)。

## 四、版本升级记录：Glide 4.16 → 5.0.9 / OkHttp 4.12 → 5.5

编译期 API 变化(踩过)：
1. **`GlideApp` 不再生成** → 统一 `Glide.with(...)`；
2. `LoadData` 从顶层类变成 **`ModelLoader` 的内部类**(`ModelLoader.LoadData<Data>`)；
3. RequestBuilder 上删除了 `error(int/Drawable)`、`placeholder(int/Drawable)`、`circleCrop()` 等 → 全部走 **`RequestOptions`**(`.errorOf(...)` / `.placeholderOf(...)` / `.circleCrop()`，通过 `.apply(...)` 使用)；
4. `Registry.replace(...)`、`AppGlideModule`、`RequestListener`、`DrawableImageViewTarget` 回调签名、`GlideException`、`DataSource`、`Transition` 等基本不变；
5. OkHttp 5 仍用 `okhttp3` 包名，`Interceptor/Dispatcher/OkHttpClient.Builder` 等公共 API 兼容，无需改动；
6. 跨大版本升级后务必 **Clean 再编译**，避免旧 annotation processor 生成的 `GlideApp` 等残留文件干扰。

## 五、回归清单(下次改图片相关功能时对照)

- [ ] 列表封面、首页 Banner 图能正常加载(带 UA/Cookie/Referer 生效)
- [ ] 图片内容页(纵向看图)能逐张加载、滚回有缓存、失败有提示
- [ ] 同 host 并发请求不超过限制(OkHttp Dispatcher ≤3)
- [ ] 新增整屏/大图目标时记得给 Glide `override` 尺寸
