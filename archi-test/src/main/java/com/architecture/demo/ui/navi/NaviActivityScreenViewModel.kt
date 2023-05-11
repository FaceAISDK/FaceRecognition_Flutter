package com.architecture.demo.ui.navi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architecture.demo.http.beans.FakerDataBean
import com.architecture.demo.http.service.EuropeanaApiService
import com.architecture.demo.http.service.FakerApiService
import com.architecture.httplib.core.HttpResult
import com.architecture.httplib.utils.MoshiUtils
import com.architecture.demo.http.service.ExceptionApiService
import com.architecture.demo.http.service.WanAndroidApiService
import kotlinx.coroutines.launch

/**
 * 使用场景还要简化再封装
 *
 *
 * ViewModel:  https://developer.android.com/topic/libraries/architecture/viewmodel?hl=zh-cn
 *
 * LiveData 也是可以用一下的，不是所有的场景都要上复杂的东西，
 *
 */
class NaviActivityScreenViewModel : ViewModel() {


    fun onGetFakeDataWithKotlinNpeClicked() {

        viewModelScope.launch {
            val result = FakerApiService.getService().getFakerData()
            when (result) {
                is HttpResult.Success -> {
                    //List<FakerDataBean> 数据就直接出来了，方便极了
                    val fakeData: List<FakerDataBean> = result.data
                    Log.e("Success", MoshiUtils.toJson(fakeData))
                }

                is HttpResult.Failure -> {
                    Log.e("Failure", result.message + "  -----  " + result.code)
                }

            }
        }
    }


    /**
     *  viewModelScope.launch {  每个请求都要这样写太繁琐了吧
     *
     */
    fun onHttpWanHttp() {

        viewModelScope.launch {
            val result = WanAndroidApiService.getService().getBanner()
            when (result) {
                is HttpResult.Success -> {
                    Log.e("Success", MoshiUtils.toJson(result.data))
                }

                is HttpResult.Failure -> {
                    Log.e("Failure", result.message + "  -----  " + result.code)
                }

            }
        }

    }


    /**
     *  Europeana ApiService 返回的不是标准code+msg+data HttpWrapper包装类
     *
     */
    fun onEuropeanaRequest() {
        viewModelScope.launch {
            when (val result = EuropeanaApiService.getService().getEuropData()) {
                is HttpResult.Success -> {
                    if(result.data.success){
                        Log.e("Success", MoshiUtils.toJson(result.data.items)) //多一层链路.item
                    }else{
                        Log.e("code is not ok","")
                    }
                }

                is HttpResult.Failure -> {
                    Log.e("Failure", result.message + "  -----  " + result.code)
                }
            }
        }
    }


    fun onHttpbinOrg404Clicked() {
        viewModelScope.launch {
            when (val result = ExceptionApiService.getService().status404()) {
                is HttpResult.Success -> {
                    Log.e("Success", MoshiUtils.toJson(result.data))
                }

                is HttpResult.Failure -> {
                    Log.e("Failure", result.message + "  -----  " + result.code)
                }
            }
        }
    }


    fun onHttpbinOrg501Clicked() {

        viewModelScope.launch {
            when (val result = ExceptionApiService.getService().status501()) {


                is HttpResult.Success -> {
                    Log.e("Success", MoshiUtils.toJson(result.data))
                }

                is HttpResult.Failure -> {
                    Log.e("Failure", result.message + "  -----  " + result.code)
                }

            }
        }
    }


}