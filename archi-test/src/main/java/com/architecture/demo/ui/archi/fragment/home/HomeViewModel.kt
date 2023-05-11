package com.architecture.demo.ui.archi.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architecture.baselib.livedata.StateLiveData
import com.architecture.demo.http.beans.FakerDataBean
import kotlinx.coroutines.launch


/**
 * ViewModel 没啥好说的
 *
 */
class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    private val repository by lazy { HomeRepository() }

    val fakeDataList = StateLiveData<List<FakerDataBean>>()


    fun requestNet() {
        viewModelScope.launch {
            repository.getFakerData(fakeDataList)
        }
    }


}