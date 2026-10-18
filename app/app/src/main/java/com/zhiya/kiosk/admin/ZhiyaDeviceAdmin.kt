package com.zhiya.kiosk.admin

import android.app.admin.DeviceAdminReceiver

/**
 * 设备管理员组件。
 * 刷机后首次开机（不登录账号）执行一次：
 *   adb shell dpm set-device-owner com.zhiya.kiosk/.admin.ZhiyaDeviceAdmin
 * 之后本 APP 即为 Device Owner，KioskManager 可调用设备策略 API。
 */
class ZhiyaDeviceAdmin : DeviceAdminReceiver()
