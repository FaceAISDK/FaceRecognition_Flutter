package com.zenglb.framework.news.handylife;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zenglb.framework.news.R;
import com.zlb.base.BaseDaggerActivity;
import javax.inject.Inject;


/**
 * 仅仅是调试的时候使用，没有实际的用途
 * <p>
 * (Fragment) ARouter.getInstance().build("/news/packageFragment").navigation()
 * 被组件化到了Main
 */
public class NewsPackageActivity extends BaseDaggerActivity {

    @Inject
    String testNameStr;


    @Override
    public int getLayoutId() {
        return R.layout.activity_news_package;
    }

    @Override
    protected void initViews() {
        setActivityTitle("News 模块组件化调试");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, (Fragment) ARouter.getInstance().build("/news/packageFragment").navigation())
                .commit();
    }

    @Override
    public boolean isShowBackIcon() {
        return false;
    }

}
