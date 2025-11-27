package com.example.simplescaffoldapp.model

/**
 * 彩票配置数据类
 */
data class LotteryConfig(
    val numberCount: Int = 10,
    val minNumber: Int = 1,
    val maxNumber: Int = 35
)

/**
 * 彩票票据数据类
 */
data class LotteryTicket(
    val numbers: List<Int> = emptyList(),
    val generatedTime: String = "",
    val config: LotteryConfig = LotteryConfig()
)

/**
 * UI 状态 - 单一不可变状态（与 MVVM 相同）
 */
data class LotteryUiState(
    val currentTicket: LotteryTicket? = null,
    val historyTickets: List<LotteryTicket> = emptyList(),
    val config: LotteryConfig = LotteryConfig(),
    val isGenerating: Boolean = false,
    val showConfigDialog: Boolean = false
)

//  MVI 新增部分

/**
 * 用户意图 (Intent) - MVI 核心
 *
 * 定义所有用户可能的操作，View 层通过发送 Intent 与 ViewModel 通信
 */
sealed class LotteryIntent {
    /** 生成彩票 */
    object GenerateLottery : LotteryIntent()

    /** 更新配置 */
    data class UpdateConfig(val config: LotteryConfig) : LotteryIntent()

    /** 显示/隐藏配置对话框 */
    data class ToggleConfigDialog(val show: Boolean) : LotteryIntent()

    /** 清空历史记录 */
    object ClearHistory : LotteryIntent()
}

/**
 * 一次性副作用 (Effect) - MVI 可选部分
 *
 * 用于处理不应该保存在状态中的一次性事件
 * 如：显示 Toast、导航、震动等
 */
sealed class LotteryEffect {
    /** 显示提示消息 */
    data class ShowToast(val message: String) : LotteryEffect()

    /** 生成成功 */
    data class GenerateSuccess(val numbers: List<Int>) : LotteryEffect()

    /** 配置更新成功 */
    object ConfigUpdated : LotteryEffect()

    /** 历史已清空 */
    object HistoryCleared : LotteryEffect()
}