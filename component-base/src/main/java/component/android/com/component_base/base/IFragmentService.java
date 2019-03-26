package component.android.com.component_base.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public interface IFragmentService {

    //获取目标的fragment来进行操作
    Fragment getFragment(String tag);

    //用于固定的区域来填充相应fragment
    void newFragment(Activity activity, int resId, FragmentManager fragmentManager, Bundle bundle, String tag);
}
