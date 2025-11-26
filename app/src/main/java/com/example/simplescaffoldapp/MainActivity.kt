package com.example.simplescaffoldapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.simplescaffoldapp.ui.theme.SimpleScaffoldAppTheme
import com.example.simplescaffoldapp.ui.components.MainScaffold

/**
 * 主 Activity - 体育彩票模拟生成器应用入口
 *
 * 功能：
 * 1. 模拟生成彩票号码
 * 2. 使用说明界面
 * 3. 版权信息界面
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用边到边显示
        enableEdgeToEdge()

        setContent {
            // 应用自定义主题
            SimpleScaffoldAppTheme {
                // 调用主脚手架组件
                MainScaffold()
            }
        }
    }
}