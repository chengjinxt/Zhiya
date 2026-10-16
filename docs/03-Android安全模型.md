# Android 安全模型

## 沙箱三层

经典 Android 沙箱 = **UID 隔离（DAC）+ SELinux 强制访问控制（MAC）+ 进程隔离**：

1. **UID 隔离**：每个 APP 安装时分配独立 Linux UID（从 10000 起），`/data/data/包名` 目录属主即该 UID、权限 700，别的 UID 的进程在内核层面就读不了。同 UID 才能 ptrace、发信号、kill 对方进程——全由内核按 UID 裁决。
2. **SELinux（MAC）**：第二道墙。普通第三方 APP 都在 `untrusted_app` 安全域里，能做什么被策略钉死（如不能直接读裸块设备）；系统应用在 `system_app`、`platform_app` 等域，权限更大。
3. **进程隔离**：默认每个 APP 独占 UID，沙箱天然隔离。

## 两个"用户 id"别搞混

- **Linux UID**：管沙箱隔离，如 10123。
- **安卓多用户 userId**：管"机主/小孩"这种系统用户，如 user 0（机主）、user 10 起（新建用户）。

拼合公式：**Linux UID = userId × 100000 + appId**

- appId：APP 安装序号，10000 起（第一个装的 APP 是 10000）。
- user 0 下装 100 个 APP → UID 为 10000～10099，各不相同、互相隔离。
- 多用户隔离是白赚来的：同一 APP 在 user 0 和 user 10 下 UID 分别为 10000 和 1010000，内核按 UID 裁决天然互不可见；数据目录也分开（`/data/user/0/包名` vs `/data/user/10/包名`）。

实用：`Binder.getCallingUid()` 拿到拼好的 UID，`UserHandle.getUserId(uid)` 反推 userId（整除 100000），取余得 appId。10000 以下为系统保留号段（system=1000、shell=2000），APP 碰不到。

## 同 UID 的"合住"情况

1. **sharedUserId**：同签名 + manifest 声明相同 `android:sharedUserId` 的 APP 共用一个 UID：互读写对方数据目录、可跑在同一进程、权限互通 = 同一个沙箱。API 29 起已废弃。
2. **isolatedProcess**：反方向。声明 `android:isolatedProcess="true"` 的 Service 跑在"隔离进程"，每次启动分配全新一次性 UID，连自己 APP 的数据目录都访问不了。WebView 渲染进程、广告 SDK 用这招。

## Device Owner 绑定的是包名，不是 UID

`dpm set-device-owner com.xxx/.AdminReceiver` 写入的是（包名 + 管理组件名 + 安卓 userId）记录，存于 `/data/system/device_owner_2.xml`。系统鉴权查的是"调用者包名是否为登记包名"；执行层面靠 `Binder.getCallingUid()` 反查包名。APP 卸载则 Device Owner 自动解除；升级（包名不变）不受影响。

## Device Owner vs root

| 对比项 | Device Owner | root |
|---|---|---|
| 性质 | 官方管理角色（企业级 API） | 越狱提权（uid 0） |
| 权限范围 | 管设备策略（锁任务、禁安全模式、禁恢复出厂……） | 无所不为，可读写任意系统文件 |
| 安全模型 | 在沙盒规则内 | 打破沙盒 |
| 稳定性 | 跟随系统升级 | OTA 可能掉 root |
| 风险 | 权限清单固定，被利用面小 | 提权后即最高权限，被利用面大 |

Android 没有"用 root 账户登录进桌面"的概念：系统没有交互式登录会话，每个 APP 是独立 UID，root 只是某个进程通过 `su` 切换到 uid 0 干活。思路是 Linux 服务器的 `sudo`，不是 Windows 的 Administrator 登录。

**结论**：学习机选 Device Owner——需求全覆盖、无需 root 能力、攻击面小、跟随官方升级。root 只在做系统镜像阶段有用（如把 APP 预置进 system 分区）。
