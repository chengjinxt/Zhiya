# 知芽学习机 APP（M1 工程骨架）

包名 `com.zhiya.kiosk`，Kotlin + Jetpack Compose，minSdk 30 / targetSdk 35。

## M1 范围

- 唯一 Activity 即系统 HOME（5 个大图标占位，点击提示所属里程碑）
- `ZhiyaDeviceAdmin`：Device Owner 绑定组件
- `KioskManager`：锁任务模式 + 用户限制（禁安全模式/恢复出厂/USB 调试/多用户/卸载）+ 封状态栏/去锁屏 + 沉浸式
- 家长入口：右上角连点 5 次 → 密码框（默认 `123456`，M5 改为可修改+加密存储）→ `stopLockTask()`

## 在 Android Studio 中打开

1. Android Studio（Koala 及以上）→ Open → 选中 `app/` 目录（含 settings.gradle.kts 的那层）。
2. 首次打开按提示安装 Android SDK（API 35）与 Gradle Wrapper（`gradle wrapper` 会自动生成）。
3. Sync → Run，装到 MIX 2 上验证。

## 真机联调（MIX 2，刷机完成后）

```bash
# 1. 恢复出厂，开机向导不登录账号、不连 WiFi
# 2. 设为 Device Owner（只需一次）
adb shell dpm set-device-owner com.zhiya.kiosk/.admin.ZhiyaDeviceAdmin
# 3. 安装
adb install app-debug.apk
# 4. 按 Home，应回到"知芽"界面；验证：
adb shell dumpsys device_policy | findstr owner   # Device Owner 生效
```

预期：状态栏拉不下来、安全模式进不去、Home/最近任务无出口。

## 里程碑 TODO

- M2：电话（ROLE_DIALER + 白名单拨号盘 + CallScreeningService）/ 短信（ROLE_SMS + 指令短信）
- M3：定位（LocationManager + LOC/RING/BAT 指令）
- M4：视频（Media3 ExoPlayer）/ 文档（PdfRenderer）
- M5：家长维护模式（白名单管理/文件管理/记录查看/作息配置）+ `KioskManager.reapplyRestrictions`
