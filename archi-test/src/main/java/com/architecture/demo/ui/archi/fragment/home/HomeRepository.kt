package com.architecture.demo.ui.archi.fragment.home

import android.util.Log
import com.architecture.baselib.livedata.StateLiveData
import com.architecture.demo.http.beans.FakerDataBean
import com.architecture.demo.http.service.FakerApiService
import com.architecture.httplib.core.HttpResult
import com.architecture.httplib.utils.MoshiUtils

/**
 *
 *
 */
class HomeRepository {

    /**
     * 精简一下各种error,对于业务层来说没必要细分成这样，一个方法中区分就可以了！
     *
     * @param data
     */
    suspend fun  getFakerData(data:StateLiveData<List<FakerDataBean>>) {

        // 需要再精简一下，有点啰嗦
        when (val result = FakerApiService.getService().getFakerData()) {
            is HttpResult.Success -> {
                //List<FakerDataBean> 数据就直接出来了，方便极了
                data.postSuccess(result.data)
                Log.e("Success", MoshiUtils.toJson(result.data))
            }

            is HttpResult.Failure ->{
                Log.e("HttpResult.Failure", "错误编码："+result.code+"  错误信息："+result.message)
            }

        }
    }

}