# MIX 2（chiron）刷机操作清单

目标：MIX 2 → LineageOS 20（Android 13，不装 GApps）→ Device Owner 学习机模式。
手机现状：可正常开机进 MIUI，未解锁。

## 准备工作

- [ ] 备份手机数据（**解锁会清空所有数据**）
- [ ] 电量 > 50%，准备一根稳定的数据线
- [ ] 手机登录小米账号（设置 → 小米账号）
- [ ] 电脑端准备：
  - Mi Unlock 解锁工具（小米官方）
  - MiFlash 线刷工具 + MIUI 稳定版 V11.0.6.0 fastboot 包（chiron）
  - platform-tools（adb / fastboot）
  - TWRP for chiron（`twrp-3.x-chiron.img`，从 TWRP 官网 devices/chiron 页面下）
  - LineageOS 20 for chiron（`lineage-20.0-xxx-chiron.zip`，LineageOS 官网 Devices → chiron 页面下）

## Step 1 — 解锁 bootloader

1. 手机：设置 → 我的设备 → 全部参数 → 连续点"MIUI 版本"7 次，打开开发者选项。
2. 设置 → 更多设置 → 开发者选项 → **设备解锁状态** → 绑定账号和设备。
   - 要求：插 SIM 卡、用移动数据（不要用 WiFi），登录的必须是要解锁用的那个小米账号。
3. 关机 → 按住 **音量下 + 电源** 进 fastboot 模式（兔子修机器人画面），连电脑。
4. 电脑运行 Mi Unlock，登录**同一个**小米账号 → 按提示解锁。
5. 首次一般会显示需要等待（通常 72 小时～7 天，按工具提示为准），到期后重复第 3～4 步完成解锁。
6. ⚠️ 解锁成功后首次开机**自动清空数据**，属正常现象。

## Step 2 — 刷回 MIUI V11.0.6.0 固件（LineageOS 要求的基带版本）

1. 如手机已是 MIUI V11（设置 → 我的设备查看），可跳过本步。
2. 手机进 fastboot 模式连电脑，打开 MiFlash。
3. 选择 V11.0.6.0 的 fastboot 包目录，右下角选 **"全部删除"**。
   - ⚠️ 千万别选"全部删除并 lock"，那会重新上锁！
4. 点"刷机"，等待完成自动重启。

## Step 3 — 刷 TWRP + LineageOS 20

1. 手机进 fastboot 模式，电脑执行：
   `fastboot flash recovery twrp-3.x-chiron.img`
2. **立刻**进 recovery：按住 **音量上 + 电源**，直到进入 TWRP 界面。
   - ⚠️ 不要先正常开机，MIUI 首次启动会把 TWRP 覆盖掉。
3. TWRP → Wipe → Format Data → 输入 `yes` → 滑动确认。
4. 把 `lineage-20.0-xxx-chiron.zip` 拷进手机（TWRP 下电脑可识别 MTP，或 adb push）。
5. TWRP → Install → 选中 zip → 滑动刷入。
6. 刷完点"重启到 Recovery"再"重启系统"（让 slot 状态干净）。
7. **不刷 GApps**——学习机不需要谷歌服务。

## Step 4 — Device Owner + 学习机 APP

1. 开机向导：**不连 WiFi、不登录任何账号**，能跳过就跳过。
2. 连电脑执行：
   `adb shell dpm set-device-owner com.你的包名/.AdminReceiver`
3. 安装学习机 APP，按 `docs/04-学习机实现方案.md` 的清单加锁：
   锁任务模式、禁状态栏/安全模式/恢复出厂/USB 调试、家长出口手势+密码。

## 回滚 / 救砖

- fastboot 模式还能进：MiFlash 线刷回 MIUI 官方包即满血复活。
- 完全不开机（真砖）：拆机短接或按键进高通 9008（EDL）模式，MiFlash 线刷。
- 记住：只要还能进 fastboot，就不算砖。

## 核对清单

- [ ] 解锁完成（`fastboot oem device-info` 显示 unlocked）
- [ ] MIUI V11.0.6.0
- [ ] TWRP 可进
- [ ] LineageOS 20 正常开机、无 GApps
- [ ] Device Owner 设置成功（`adb shell dumpsys device_policy` 可查）
- [ ] 学习机 APP 锁任务模式生效：Home/最近任务/状态栏均无出口
