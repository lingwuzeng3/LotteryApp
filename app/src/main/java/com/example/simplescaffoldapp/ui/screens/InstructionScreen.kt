package com.example.simplescaffoldapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 使用说明页面
 * 
 * 详细介绍应用的功能和使用方法
 */
@Composable
fun InstructionScreen(innerPadding: PaddingValues) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 页面标题
            Text(
                text = "使用说明",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ===== 应用介绍 =====
            InstructionSection(
                icon = Icons.Filled.Info,
                iconColor = MaterialTheme.colorScheme.primary,
                title = "应用简介",
                content = """
                    中国体育彩票模拟生成器是一款用于模拟生成彩票号码的应用程序。
                    
                    本应用仅供学习和娱乐目的，生成的号码为随机数字，不具有任何预测价值，请理性对待。
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 功能说明 =====
            InstructionSection(
                icon = Icons.Filled.Star,
                iconColor = Color(0xFFFFD700),
                title = "主要功能",
                content = """
                    1. 彩票号码生成
                    • 点击"生成彩票号码"按钮或悬浮按钮
                    • 系统将随机生成一组不重复的号码
                    • 号码按从小到大排列显示
                    
                    2. 历史记录
                    • 自动保存最近10次生成记录
                    • 可在抽屉菜单中清空历史
                    
                    3. 参数配置
                    • 自定义号码个数（默认10个）
                    • 自定义号码范围（默认1-35）
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 配置说明 =====
            InstructionSection(
                icon = Icons.Filled.Settings,
                iconColor = MaterialTheme.colorScheme.secondary,
                title = "参数配置说明",
                content = """
                    号码个数：
                    • 每张彩票生成的号码数量
                    • 默认值：10个
                    • 可根据需要自行调整
                    
                    号码范围：
                    • 最小值：号码的起始值（默认1）
                    • 最大值：号码的最大值（默认35）
                    • 注意：范围必须足够大，以生成指定数量的不重复号码
                    
                    配置入口：
                    • 顶部栏右侧设置按钮
                    • 侧边抽屉栏"参数配置"选项
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 操作指南 =====
            InstructionSection(
                icon = Icons.Filled.CheckCircle,
                iconColor = Color(0xFF4CAF50),
                title = "操作指南",
                content = """
                    基本操作：
                    ① 打开应用，默认进入彩票生成页面
                    ② 点击"生成彩票号码"按钮
                    ③ 等待系统生成随机号码
                    ④ 查看生成结果和历史记录
                    
                    导航方式：
                    • 底部导航栏：快速切换页面
                    • 侧边抽屉：点击左上角菜单图标打开
                    • 抽屉包含所有页面入口和功能设置
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 温馨提示 =====
            InstructionSection(
                icon = Icons.Filled.Warning,
                iconColor = Color(0xFFFF9800),
                title = "温馨提示",
                content = """
                    ⚠️ 重要声明：
                    
                    1. 本应用生成的号码完全随机，仅供娱乐
                    2. 不保证中奖，不具有任何预测功能
                    3. 请理性购彩，量力而行
                    4. 未满18周岁禁止购买彩票
                    
                    🎯 中国体育彩票公益宣传：
                    "公益体彩，乐善人生"
                    体育彩票是国家公益彩票，筹集的公益金广泛用于：
                    • 全民健身计划
                    • 奥运争光计划
                    • 社会公益事业
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 说明章节组件
 */
@Composable
fun InstructionSection(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题行
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 内容
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
            )
        }
    }
}