package com.zhiya.kiosk.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// TODO M5: 口令改为家长模式可修改，存 EncryptedSharedPreferences；默认口令仅用于 M1 联调
private const val DEFAULT_PARENT_PASSWORD = "123456"

/** 家长入口：密码验证框 */
@Composable
fun ParentGate(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
) {
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("家长验证") },
        text = {
            Column {
                Text("请输入家长密码")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        error = false
                    },
                    label = { Text("密码") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = error,
                    singleLine = true,
                )
                if (error) {
                    Text("密码错误", modifier = Modifier.padding(top = 4.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (password == DEFAULT_PARENT_PASSWORD) onSuccess()
                else error = true
            }) { Text("进入维护模式") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}
