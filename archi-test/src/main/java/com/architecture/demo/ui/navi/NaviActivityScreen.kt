package com.architecture.demo.ui.navi

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architecture.demo.ui.archi.MainActivity

/**
 * Composable
 *
 */
@Composable
fun MainActivityScreen(viewModel: NaviActivityScreenViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(20.dp)
    ) {

        val context = LocalContext.current


        Button(onClick = {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }, modifier = Modifier.padding(20.dp).fillMaxWidth()){
            Text(text = "App架构重构", fontSize = 16.sp, color = Color.Green)
        }


        Button(onClick = {
            viewModel.onGetFakeDataWithKotlinNpeClicked()
        }, modifier = Modifier.padding(20.dp).fillMaxWidth()){
            Text(text = "Moshi的Kotlin Safe NPE", fontSize = 14.sp)
        }


        Button(onClick = {
            viewModel.onHttpWanHttp()
        }, modifier = Modifier.padding(20.dp).fillMaxWidth()){
            Text(text = "Wan Banner Standard Wrapper ", fontSize = 14.sp)
        }


        Button(onClick = {
            viewModel.onEuropeanaRequest()
        }, modifier = Modifier.padding(20.dp).fillMaxWidth()){
            Text(text = "Euro data unStandard Wrapper", fontSize = 14.sp)
        }


        Button(onClick = {
            viewModel.onHttpbinOrg404Clicked()
        }, modifier = Modifier.padding(20.dp).fillMaxWidth()){
            Text(text = "httpbin.org的404返回", fontSize = 14.sp)
        }


        Button(onClick = {
            viewModel.onHttpbinOrg501Clicked()
        }, modifier = Modifier.padding(20.dp).fillMaxWidth()){
            Text(text = "httpbin.org的501返回", fontSize = 14.sp)
        }



    }
}