package com.example.simplescaffoldapp.model

/**
 * 彩票配置数据类
 * @param numberCount 彩票号码个数，默认10个
 * @param minNumber 最小号码值，默认1
 * @param maxNumber 最大号码值，默认35
 */
data class LotteryConfig(
    val numberCount: Int = 10,
    val minNumber: Int = 1,
    val maxNumber: Int = 35
)

/**
 * 彩票生成结果数据类
 * @param numbers 生成的彩票号码列表
 * @param generatedTime 生成时间
 * @param config 使用的配置
 */
data class LotteryTicket(
    val numbers: List<Int> = emptyList(),
    val generatedTime: String = "",
    val config: LotteryConfig = LotteryConfig()
)

/**
 * 彩票界面状态 - 单一不可变状态（MVVM模式）
 * @param currentTicket 当前生成的彩票
 * @param historyTickets 历史生成记录
 * @param config 当前配置
 * @param isGenerating 是否正在生成
 * @param showConfigDialog 是否显示配置对话框
 */
data class LotteryUiState(
    val currentTicket: LotteryTicket? = null,
    val historyTickets: List<LotteryTicket> = emptyList(),
    val config: LotteryConfig = LotteryConfig(),
    val isGenerating: Boolean = false,
    val showConfigDialog: Boolean = false
)