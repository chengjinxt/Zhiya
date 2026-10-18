package com.zhiya.kiosk.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private data class KioskEntry(
    val label: String,
    val icon: ImageVector,
    val milestone: String, // 所属里程碑
)

private val ENTRIES = listOf(
    KioskEntry("电话", Icons.Filled.Call, "M2"),
    KioskEntry("短信", Icons.Filled.Message, "M2"),
    KioskEntry("定位", Icons.Filled.LocationOn, "M3"),
    KioskEntry("视频", Icons.Filled.PlayCircle, "M4"),
    KioskEntry("文档", Icons.Filled.Description, "M4"),
)

/**
 * M1：kiosk 主界面。5 个大图标，小孩唯一可见的界面。
 * 右上角隐藏手势：连点 5 次 → 家长密码框 → 维护模式（M5）。
 */
@Composable
fun HomeScreen(onEnterParentMode: () -> Unit) {
    val context = LocalContext.current
    var tapCount by remember { mutableIntStateOf(0) }
    var showParentGate by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "知芽",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 48.dp, bottom = 32.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(ENTRIES) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "${entry.label}：${entry.milestone} 待实现",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = entry.icon,
                                contentDescription = entry.label,
                                modifier = Modifier.size(56.dp)
                            )
                            Text(
                                text = entry.label,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // 隐藏手势区：右上角 64dp 见方，连点 5 次
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(64.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    tapCount++
                    if (tapCount >= 5) {
                        tapCount = 0
                        showParentGate = true
                    }
                }
        )
    }

    if (showParentGate) {
        ParentGate(
            onDismiss = { showParentGate = false },
            onSuccess = {
                showParentGate = false
                onEnterParentMode()
            }
        )
    }
}
