package com.example.simplescaffoldapp.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplescaffoldapp.ui.screens.AboutScreen
import com.example.simplescaffoldapp.ui.screens.InstructionScreen
import com.example.simplescaffoldapp.ui.screens.LotteryScreen
import com.example.simplescaffoldapp.viewModel.LotteryViewModel
import kotlinx.coroutines.launch

/**
 * 底部导航项数据类
 */
data class BottomNavItem(
    val label: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit
)

/**
 * 主脚手架组件 - 应用的主要布局结构
 * 
 * 包含：
 * 1. 顶部应用栏（TopAppBar）
 * 2. 底部导航栏（BottomNavigationBar）
 * 3. 侧边抽屉栏（NavigationDrawer）
 * 4. 悬浮操作按钮（FAB）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    viewModel: LotteryViewModel = viewModel()
) {
    // 抽屉状态管理
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    // 协程作用域，用于打开/关闭抽屉
    val scope = rememberCoroutineScope()
    
    // 当前选中的底部导航项索引
    var selectedItem by remember { mutableIntStateOf(0) }
    
    // 从 ViewModel 收集 UI 状态
    val uiState by viewModel.uiState.collectAsState()

    // 底部导航项配置
    val bottomNavItems = listOf(
        BottomNavItem(
            label = "彩票生成",
            selectedIcon = { Icon(Icons.Filled.Home, contentDescription = null) },
            unselectedIcon = { Icon(Icons.Outlined.Home, contentDescription = null) }
        ),
        BottomNavItem(
            label = "使用说明",
            selectedIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
            unselectedIcon = { Icon(Icons.Outlined.Info, contentDescription = null) }
        ),
        BottomNavItem(
            label = "关于",
            selectedIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            unselectedIcon = { Icon(Icons.Outlined.Person, contentDescription = null) }
        )
    )

    // 模态导航抽屉 - 最外层容器
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // 抽屉内容
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                
                // 抽屉标题
                Text(
                    text = "体育彩票模拟器",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                Text(
                    text = "为公益事业贡献力量",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                // 抽屉导航项 - 彩票生成
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("彩票生成") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                // 抽屉导航项 - 使用说明
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
                    label = { Text("使用说明") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                // 抽屉导航项 - 关于
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    label = { Text("关于/版权") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 抽屉功能项 - 配置设置
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                    label = { Text("参数配置") },
                    selected = false,
                    onClick = {
                        viewModel.toggleConfigDialog(true)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                // 抽屉功能项 - 清空历史
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
                    label = { Text("清空历史") },
                    selected = false,
                    onClick = {
                        viewModel.clearHistory()
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        // Scaffold 布局骨架
        Scaffold(
            // ===== 顶部应用栏 =====
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            text = bottomNavItems[selectedItem].label,
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    navigationIcon = {
                        // 菜单按钮 - 打开抽屉
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "打开菜单"
                            )
                        }
                    },
                    actions = {
                        // 顶部栏右侧操作按钮
                        if (selectedItem == 0) {
                            // 配置按钮（仅在彩票生成页显示）
                            IconButton(onClick = {
                                viewModel.toggleConfigDialog(true)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "配置参数"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },

            // ===== 底部导航栏 =====
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                if (selectedItem == index) {
                                    item.selectedIcon()
                                } else {
                                    item.unselectedIcon()
                                }
                            },
                            label = { Text(item.label) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            },

            //悬浮操作按钮
            floatingActionButton = {
                // 仅在彩票生成页显示 FAB
                if (selectedItem == 0) {
                    FloatingActionButton(
                        onClick = { viewModel.generateLottery() },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "生成彩票",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        ) { innerPadding ->
            // ===== 内容区域 =====
            // 根据选中的导航项显示不同页面
            when (selectedItem) {
                0 -> LotteryScreen(
                    innerPadding = innerPadding,
                    viewModel = viewModel
                )
                1 -> InstructionScreen(innerPadding = innerPadding)
                2 -> AboutScreen(innerPadding = innerPadding)
            }
        }
    }
}