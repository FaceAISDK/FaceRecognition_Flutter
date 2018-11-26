package com.zenglb.framework.modulea.navigation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.zenglb.framework.modulea.http.AModuleApiService;
import com.zenglb.framework.modulea.http.result.CustomWeatherResult;
import com.zlb.base.BaseActivity;
import com.zenglb.framework.modulea.R;
import com.zenglb.framework.modulea.demo.MemoryLeakTest;
import com.zenglb.framework.modulea.demo.quick_input_things.QuickInputThingsActivity;
import com.zenglb.framework.modulea.mvp.handylife.AnyLifeActivity;
import com.zlb.dagger.scope.ActivityScope;
import com.zlb.persistence.dbmaster.DaoSession;
import com.zlb.takephoto.WaterCameraActivity;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 某些场景的Demo
 * <p>
 * <p>
 * 设备ID： https://www.jianshu.com/p/b6f4b0aca6b0
 * <p>
 * https://blog.csdn.net/a_long_/article/details/54829644
 *
 * @author zenglb 2016.10.24
 */
@ActivityScope
public class DemoFragment extends Fragment {
    public final static int REQUEST_TAKE_WATER_IMAGE = 1000;

    @Inject
    AModuleApiService apiService;

    @Inject
    DaoSession daoSession;

    ImageView imageView;

    @Inject
    public DemoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_demo, container, false);
        viewsInit(rootView);

//        List<TestResult> testResultList = new ArrayList<>();
//        testResultList.add(new TestResult("fullname1", "111", 1, false));
//        testResultList.add(new TestResult("fullname2", "111", 1, false));
//        testResultList.add(new TestResult("fullname3", null, 2, false));
//        testResultList.add(new TestResult("fullname4", null, 2, false));
//        testResultList.add(new TestResult("fullname5", null, 2, false));
//
//        daoSession.getTestResultDao().insertOrReplaceInTx(testResultList);

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_WATER_IMAGE:
                    String pathStr = data.getStringExtra(WaterCameraActivity.PHOTO_PATH_KEY);
                    Glide.with(this).load(pathStr).into(imageView);
                    break;
            }
        }
    }


    /**
     * init views
     *
     * @param
     */
    private void viewsInit(View rootView) {

        String daoSessionAddress = daoSession.getDatabase().getRawDatabase().toString();

        imageView = rootView.findViewById(R.id.logo_img);

        rootView.findViewById(R.id.logo_img).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WaterCameraActivity.class);
            startActivityForResult(intent, REQUEST_TAKE_WATER_IMAGE);
        });


        rootView.findViewById(R.id.quickinput).setOnClickListener(v -> {
            ((BaseActivity) getActivity()).startActivity(QuickInputThingsActivity.class);
        });

        //
        rootView.findViewById(R.id.JSBridge).setOnClickListener(v -> {
            ARouter.getInstance().build("/web/WebActivity")
                    .withString("url", "file:///android_asset/index.html")
                    .navigation();
        });


        /**
         * 退出登录，测试切换数据库
         */
        rootView.findViewById(R.id.logout).setOnClickListener(v -> {
            ARouter.getInstance().build("/login/activity").navigation();
            getActivity().finish();
        });


        /**
         * 动态替换URL,参数照样的随意的使用啊。
         *
         */
        rootView.findViewById(R.id.java8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.sojson.com/open/api/weather/json.shtml";
                Call<CustomWeatherResult> news = apiService.getWeather(url, "深圳");
                //上面的实现是非常的精巧  http://www.jianshu.com/p/c1a3a881a144

                news.enqueue(new Callback<CustomWeatherResult>() {
                    @Override
                    public void onResponse(Call<CustomWeatherResult> call, Response<CustomWeatherResult> response) {
                        CustomWeatherResult customWeatherResult = response.body();
                        Toast.makeText(getActivity(), "天气：" + customWeatherResult.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<CustomWeatherResult> call, Throwable t) {
                        Toast.makeText(getActivity(), "失败：" + call.toString() + "  异常：" + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        /**
         * mvp
         */
        rootView.findViewById(R.id.mvp).setOnClickListener(v -> {
            ((BaseActivity) getActivity()).startActivity(AnyLifeActivity.class);
        });


        /**
         * jni jni
         */
        rootView.findViewById(R.id.jni).setOnClickListener(v -> {
//            ((BaseActivity) getActivity()).startActivity(MemoryLeakTest.class);
        });


        /**
         * 内存泄漏 以及 内存无法及时释放
         * https://www.jianshu.com/p/0076cb510372
         */
        rootView.findViewById(R.id.memory_leak).setOnClickListener(v -> {
            ((BaseActivity) getActivity()).startActivity(MemoryLeakTest.class);
        });


        /**
         * dragger
         */
        rootView.findViewById(R.id.animation).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WaterCameraActivity.class);
            startActivityForResult(intent, REQUEST_TAKE_WATER_IMAGE);
        });

    }


    private void transitionToActivity(Class target, TextView textView, String title) {
//        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), true,
//                new Pair<>(textView, getActivity().getString(R.string.shared_name)));
//
//        startActivity(target, pairs, title);
    }


}
