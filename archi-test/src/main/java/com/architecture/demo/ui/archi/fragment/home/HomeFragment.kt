package com.architecture.demo.ui.archi.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.architecture.baselib.livedata.StateData
import com.architecture.demo.databinding.FragmentHomeBinding
import com.architecture.httplib.utils.MoshiUtils

/**
 * 使用FakeApi的数据进行演示
 *
 */
class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        val button: Button = binding.btnHttp

        //把请求的数据格式返回了就可以了
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        homeViewModel.fakeDataList.observe(viewLifecycleOwner) {
            //转为json
            //还有异常的情况怎么办？
            textView.text = MoshiUtils.toJson(it.data)

            //有点啰嗦，写法上还是要精简一点
            homeViewModel.fakeDataList.observe(viewLifecycleOwner) { data ->
                when (data.status) {
                    StateData.DataStatus.SUCCESS -> {

                    }

                    StateData.DataStatus.ERROR -> {

                    }
                    else -> {}
                }
            }
        }





        button.setOnClickListener {
            homeViewModel.requestNet()
        }



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}