package com.zhiya.kiosk.kiosk

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.UserManager
import com.zhiya.kiosk.admin.ZhiyaDeviceAdmin

/**
 * M1：kiosk 锁死逻辑。
 * Device Owner 下静默进入锁任务模式 + 应用用户限制；
 * 非 Device Owner（如开发机调试）时 startLockTask 会弹系统确认框，属正常。
 */
object KioskManager {

    private fun admin(context: Context) =
        ComponentName(context, ZhiyaDeviceAdmin::class.java)

    private fun dpm(context: Context) =
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

    fun isDeviceOwner(context: Context): Boolean =
        dpm(context).isDeviceOwnerApp(context.packageName)

    fun startKiosk(activity: Activity) {
        val dpm = dpm(activity)
        val admin = admin(activity)
        if (isDeviceOwner(activity)) {
            // 1. 登记锁任务包：之后 startLockTask() 静默生效，Home/最近任务被系统接管
            dpm.setLockTaskPackages(admin, arrayOf(activity.packageName))
            // 2. 封状态栏下拉、去锁屏
            dpm.setStatusBarDisabled(admin, true)
            dpm.setKeyguardDisabled(admin, true)
            // 3. 防绕过用户限制
            listOf(
                UserManager.DISALLOW_SAFE_BOOT,        // 禁安全模式
                UserManager.DISALLOW_FACTORY_RESET,    // 禁恢复出厂
                UserManager.DISALLOW_DEBUGGING_FEATURES, // 禁 USB 调试
                UserManager.DISALLOW_ADD_USER,         // 禁多用户
                UserManager.DISALLOW_UNINSTALL_APPS,   // 禁卸载应用
                // UserManager.DISALLOW_CONFIG_WIFI,   // 可选：彻底断网；v1 保留 WiFi 给未来留余地
                // UserManager.DISALLOW_INSTALL_APPS, // 注意：会连 adb install 一起禁，
                                                      // v1 更新走维护模式临时放开，见 ParentGate TODO
            ).forEach { dpm.addUserRestriction(admin, it) }
        }
        activity.startLockTask()
    }

    fun stopKiosk(activity: Activity) {
        activity.stopLockTask()
    }

    /** 维护模式结束时恢复限制（M5 实现） */
    fun reapplyRestrictions(context: Context) {
        // TODO M5: stopLockTask 期间如临时清除了某条限制，在此重新加上
    }
}
