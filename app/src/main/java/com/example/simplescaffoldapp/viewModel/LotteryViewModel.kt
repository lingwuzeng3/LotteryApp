package com.example.simplescaffoldapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplescaffoldapp.model.LotteryConfig
import com.example.simplescaffoldapp.model.LotteryEffect
import com.example.simplescaffoldapp.model.LotteryIntent
import com.example.simplescaffoldapp.model.LotteryTicket
import com.example.simplescaffoldapp.model.LotteryUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 彩票生成 ViewModel - MVI 架构
 *
 * MVI 核心变化：
 * 1. 统一通过 handleIntent() 处理所有用户意图
 * 2. 新增 Effect 通道处理一次性事件
 */
class LotteryViewModel : ViewModel() {

    // ==================== State（状态）====================
    private val _uiState = MutableStateFlow(LotteryUiState())
    val uiState: StateFlow<LotteryUiState> = _uiState.asStateFlow()

    // ==================== Effect（副作用）====================
    private val _effect = Channel<LotteryEffect>()
    val effect: Flow<LotteryEffect> = _effect.receiveAsFlow()

    // ==================== Intent 处理（MVI 核心）====================

    /**
     * 处理用户意图 - MVI 统一入口
     *
     * 所有用户操作都通过此方法处理，实现单向数据流
     *
     * @param intent 用户意图
     */
    fun handleIntent(intent: LotteryIntent) {
        when (intent) {
            is LotteryIntent.GenerateLottery -> generateLottery()
            is LotteryIntent.UpdateConfig -> updateConfig(intent.config)
            is LotteryIntent.ToggleConfigDialog -> toggleConfigDialog(intent.show)
            is LotteryIntent.ClearHistory -> clearHistory()
        }
    }

    // ==================== 业务逻辑实现（私有方法）====================

    /**
     * 生成彩票号码
     */
    private fun generateLottery() {
        viewModelScope.launch {
            // 更新状态：开始生成
            _uiState.update { it.copy(isGenerating = true) }

            // 模拟生成延迟
            delay(500)

            val config = _uiState.value.config

            // 生成不重复的随机号码
            val numbers = generateUniqueRandomNumbers(
                count = config.numberCount,
                min = config.minNumber,
                max = config.maxNumber
            )

            // 获取当前时间
            val currentTime = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

            // 创建彩票对象
            val ticket = LotteryTicket(
                numbers = numbers.sorted(),
                generatedTime = currentTime,
                config = config
            )

            // 更新状态：生成完成
            _uiState.update { state ->
                state.copy(
                    currentTicket = ticket,
                    historyTickets = listOf(ticket) + state.historyTickets.take(9),
                    isGenerating = false
                )
            }

            // 发送副作用事件
            _effect.send(LotteryEffect.GenerateSuccess(numbers))
        }
    }

    /**
     * 更新配置
     */
    private fun updateConfig(config: LotteryConfig) {
        _uiState.update { it.copy(config = config) }

        viewModelScope.launch {
            _effect.send(LotteryEffect.ConfigUpdated)
        }
    }

    /**
     * 显示/隐藏配置对话框
     */
    private fun toggleConfigDialog(show: Boolean) {
        _uiState.update { it.copy(showConfigDialog = show) }
    }

    /**
     * 清空历史记录
     */
    private fun clearHistory() {
        _uiState.update { it.copy(historyTickets = emptyList()) }

        viewModelScope.launch {
            _effect.send(LotteryEffect.HistoryCleared)
        }
    }

    /**
     * 生成指定数量的不重复随机数
     */
    private fun generateUniqueRandomNumbers(count: Int, min: Int, max: Int): List<Int> {
        require(max - min + 1 >= count) {
            "号码范围不足以生成${count}个不重复的号码"
        }
        return (min..max).shuffled().take(count)
    }

    // ==================== 兼容 MVVM 的公开方法（可选保留）====================
    // 如果需要渐进式迁移，可以保留这些方法
    // 它们内部调用 handleIntent，保持向后兼容

    @Deprecated("MVI 模式下请使用 handleIntent(LotteryIntent.GenerateLottery)")
    fun generateLotteryCompat() = handleIntent(LotteryIntent.GenerateLottery)

    @Deprecated("MVI 模式下请使用 handleIntent(LotteryIntent.UpdateConfig(config))")
    fun updateConfigCompat(config: LotteryConfig) = handleIntent(LotteryIntent.UpdateConfig(config))

    @Deprecated("MVI 模式下请使用 handleIntent(LotteryIntent.ToggleConfigDialog(show))")
    fun toggleConfigDialogCompat(show: Boolean) = handleIntent(LotteryIntent.ToggleConfigDialog(show))

    @Deprecated("MVI 模式下请使用 handleIntent(LotteryIntent.ClearHistory)")
    fun clearHistoryCompat() = handleIntent(LotteryIntent.ClearHistory)
}