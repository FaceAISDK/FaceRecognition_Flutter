package com.anylife.module_main.business.navigation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView;

import com.anylife.module_main.R;
import com.zlb.base.BaseStatusFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends BaseStatusFragment {

    protected WebView webview;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_video_list, container, false);
//    }

    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View rootView) {
        webview = (WebView) rootView.findViewById(R.id.webview);
        webview.loadUrl("https://github.com/AnyLifeZLB/MVP-Dagger2-Rxjava2");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
