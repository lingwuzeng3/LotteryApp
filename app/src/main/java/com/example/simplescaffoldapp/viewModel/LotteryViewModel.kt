package com.example.simplescaffoldapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplescaffoldapp.model.LotteryConfig
import com.example.simplescaffoldapp.model.LotteryTicket
import com.example.simplescaffoldapp.model.LotteryUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 彩票生成 ViewModel - MVVM架构核心
 * 
 * 职责：
 * 1. 管理彩票生成的业务逻辑
 * 2. 维护UI状态
 * 3. 处理配置更新
 */
class LotteryViewModel : ViewModel() {

    // 私有可变状态
    private val _uiState = MutableStateFlow(LotteryUiState())
    
    // 公开的只读状态，供UI层观察
    val uiState: StateFlow<LotteryUiState> = _uiState.asStateFlow()

    /**
     * 生成彩票号码
     * 根据当前配置生成随机不重复的彩票号码
     */
    fun generateLottery() {
        viewModelScope.launch {
            // 设置生成中状态
            _uiState.update { it.copy(isGenerating = true) }
            
            // 模拟生成延迟，增加用户体验
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
                numbers = numbers.sorted(), // 排序后显示
                generatedTime = currentTime,
                config = config
            )
            
            // 更新状态
            _uiState.update { state ->
                state.copy(
                    currentTicket = ticket,
                    historyTickets = listOf(ticket) + state.historyTickets.take(9), // 保留最近10条
                    isGenerating = false
                )
            }
        }
    }

    /**
     * 生成指定数量的不重复随机数
     * @param count 需要生成的数量
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机数列表
     */
    private fun generateUniqueRandomNumbers(count: Int, min: Int, max: Int): List<Int> {
        require(max - min + 1 >= count) { 
            "号码范围不足以生成${count}个不重复的号码" 
        }
        
        return (min..max).shuffled().take(count)
    }

    /**
     * 更新彩票配置
     * @param config 新的配置
     */
    fun updateConfig(config: LotteryConfig) {
        _uiState.update { it.copy(config = config) }
    }

    /**
     * 显示/隐藏配置对话框
     */
    fun toggleConfigDialog(show: Boolean) {
        _uiState.update { it.copy(showConfigDialog = show) }
    }

    /**
     * 清空历史记录
     */
    fun clearHistory() {
        _uiState.update { it.copy(historyTickets = emptyList()) }
    }
}