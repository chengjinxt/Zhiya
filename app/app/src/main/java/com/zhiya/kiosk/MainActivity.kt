package com.zhiya.kiosk

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.zhiya.kiosk.kiosk.KioskManager
import com.zhiya.kiosk.ui.HomeScreen
import com.zhiya.kiosk.ui.ZhiyaTheme

/**
 * M1：唯一 Activity，同时是系统 HOME。
 * 启动即尝试进入锁任务模式（Device Owner 下静默，开发机调试时会弹确认框）。
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ZhiyaTheme {
                HomeScreen(
                    onEnterParentMode = {
                        KioskManager.stopKiosk(this)
                        // TODO M5: 跳转家长维护界面（白名单/文件管理/记录查看/配置）
                        Toast.makeText(this, "维护模式：M5 实现", Toast.LENGTH_SHORT).show()
                        KioskManager.reapplyRestrictions(this)
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        KioskManager.startKiosk(this)
        hideSystemBars()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    private fun hideSystemBars() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
