package com.architecture.demo.ui.navi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

/***
 * ComponentActivity ：使用最新的尝试看看
 *
 */
class NaviActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val naviActivityScreenViewModel =
            ViewModelProvider(this).get(NaviActivityScreenViewModel::class.java)


        //尝试使用Compose 吧，不需要大规模使用
        setContent {
            MainActivityScreen(naviActivityScreenViewModel)
        }



    }
}