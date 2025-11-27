package com.example.simplescaffoldapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplescaffoldapp.model.LotteryConfig
import com.example.simplescaffoldapp.model.LotteryEffect
import com.example.simplescaffoldapp.model.LotteryIntent
import com.example.simplescaffoldapp.model.LotteryTicket
import com.example.simplescaffoldapp.viewModel.LotteryViewModel

/**
 * 彩票生成主页面 - MVI 模式
 *
 * MVI 核心变化：
 * 1. 通过 onIntent 发送意图，而非直接调用 ViewModel 方法
 * 2. 监听 Effect 处理一次性事件
 */
@Composable
fun LotteryScreen(
    innerPadding: PaddingValues,
    viewModel: LotteryViewModel
) {
    val context = LocalContext.current

    // 收集 UI 状态
    val uiState by viewModel.uiState.collectAsState()

    // ==================== MVI：定义 Intent 发送器 ====================
    val onIntent: (LotteryIntent) -> Unit = { intent ->
        viewModel.handleIntent(intent)
    }

    // ==================== MVI：处理一次性副作用 ====================
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LotteryEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is LotteryEffect.GenerateSuccess -> {
                    Toast.makeText(
                        context,
                        "生成成功！号码：${effect.numbers.joinToString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is LotteryEffect.ConfigUpdated -> {
                    Toast.makeText(context, "配置已更新", Toast.LENGTH_SHORT).show()
                }
                is LotteryEffect.HistoryCleared -> {
                    Toast.makeText(context, "历史记录已清空", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ==================== UI 渲染 ====================
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 配置信息卡片
            ConfigInfoCard(config = uiState.config)

            Spacer(modifier = Modifier.height(16.dp))

            // 彩票显示卡片 - 传递 onIntent
            LotteryDisplayCard(
                ticket = uiState.currentTicket,
                isGenerating = uiState.isGenerating,
                onIntent = onIntent  // MVI: 传递 Intent 发送器
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 历史记录
            Text(
                text = "📋 历史记录",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (uiState.historyTickets.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "暂无历史记录\n点击下方按钮生成彩票",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(uiState.historyTickets) { index, ticket ->
                        HistoryTicketItem(
                            index = index + 1,
                            ticket = ticket
                        )
                    }
                }
            }
        }

        // 配置对话框 - 传递 onIntent
        if (uiState.showConfigDialog) {
            ConfigDialog(
                currentConfig = uiState.config,
                onIntent = onIntent  // MVI: 传递 Intent 发送器
            )
        }
    }
}

/**
 * 配置信息卡片（无变化）
 */
@Composable
fun ConfigInfoCard(config: LotteryConfig) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ConfigItem(label = "号码个数", value = "${config.numberCount}")
            ConfigItem(label = "最小值", value = "${config.minNumber}")
            ConfigItem(label = "最大值", value = "${config.maxNumber}")
        }
    }
}

@Composable
fun ConfigItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * 彩票显示卡片 - MVI 版本
 *
 * 变化：使用 onIntent 代替 onGenerateClick
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LotteryDisplayCard(
    ticket: LotteryTicket?,
    isGenerating: Boolean,
    onIntent: (LotteryIntent) -> Unit  // MVI: Intent 发送器
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎯 中国体育彩票",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "模拟生成号码",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isGenerating -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "正在生成...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    ticket != null -> {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ticket.numbers.forEachIndexed { index, number ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            delayMillis = index * 50
                                        )
                                    ) + scaleIn(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            delayMillis = index * 50
                                        )
                                    )
                                ) {
                                    LotteryBall(number = number)
                                }
                            }
                        }
                    }
                    else -> {
                        Text(
                            text = "点击按钮生成彩票号码",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (ticket != null && !isGenerating) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "生成时间: ${ticket.generatedTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // MVI: 发送 Intent 而非调用方法
            Button(
                onClick = { onIntent(LotteryIntent.GenerateLottery) },
                enabled = !isGenerating,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (isGenerating) "生成中..." else "生成彩票号码")
            }
        }
    }
}

/**
 * 彩票号码球（无变化）
 */
@Composable
fun LotteryBall(
    number: Int,
    size: Int = 40,
    isSmall: Boolean = false
) {
    val actualSize = if (isSmall) 32 else size
    val fontSize = if (isSmall) 12.sp else 16.sp

    Box(
        modifier = Modifier
            .size(actualSize.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF5252),
                        Color(0xFFD32F2F)
                    )
                )
            )
            .border(
                width = 2.dp,
                color = Color(0xFFFFD700),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String.format("%02d", number),
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 历史记录项（无变化）
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryTicketItem(
    index: Int,
    ticket: LotteryTicket
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "第 $index 期",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ticket.generatedTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ticket.numbers.forEach { number ->
                    LotteryBall(number = number, isSmall = true)
                }
            }
        }
    }
}

/**
 * 配置对话框 - MVI 版本
 *
 * 变化：使用 onIntent 代替 onDismiss/onConfirm
 */
@Composable
fun ConfigDialog(
    currentConfig: LotteryConfig,
    onIntent: (LotteryIntent) -> Unit  // MVI: Intent 发送器
) {
    var numberCount by remember { mutableStateOf(currentConfig.numberCount.toString()) }
    var minNumber by remember { mutableStateOf(currentConfig.minNumber.toString()) }
    var maxNumber by remember { mutableStateOf(currentConfig.maxNumber.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = {
            // MVI: 发送关闭对话框的 Intent
            onIntent(LotteryIntent.ToggleConfigDialog(false))
        },
        title = {
            Text(
                text = "⚙️ 参数配置",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = numberCount,
                    onValueChange = {
                        numberCount = it
                        errorMessage = null
                    },
                    label = { Text("号码个数") },
                    placeholder = { Text("默认10个") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = minNumber,
                    onValueChange = {
                        minNumber = it
                        errorMessage = null
                    },
                    label = { Text("最小号码") },
                    placeholder = { Text("默认1") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = maxNumber,
                    onValueChange = {
                        maxNumber = it
                        errorMessage = null
                    },
                    label = { Text("最大号码") },
                    placeholder = { Text("默认35") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Divider()
                Text(
                    text = "说明：号码范围必须能够生成指定数量的不重复号码",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val count = numberCount.toIntOrNull() ?: 10
                    val min = minNumber.toIntOrNull() ?: 1
                    val max = maxNumber.toIntOrNull() ?: 35

                    when {
                        count <= 0 -> {
                            errorMessage = "号码个数必须大于0"
                        }
                        min < 1 -> {
                            errorMessage = "最小号码必须大于等于1"
                        }
                        max <= min -> {
                            errorMessage = "最大号码必须大于最小号码"
                        }
                        max - min + 1 < count -> {
                            errorMessage = "号码范围不足以生成${count}个不重复号码"
                        }
                        else -> {
                            // MVI: 发送更新配置的 Intent
                            onIntent(LotteryIntent.UpdateConfig(LotteryConfig(count, min, max)))
                            // MVI: 发送关闭对话框的 Intent
                            onIntent(LotteryIntent.ToggleConfigDialog(false))
                        }
                    }
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // MVI: 发送关闭对话框的 Intent
                    onIntent(LotteryIntent.ToggleConfigDialog(false))
                }
            ) {
                Text("取消")
            }
        }
    )
}