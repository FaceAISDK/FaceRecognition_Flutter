package com.zlb.commontips;


import com.kingja.loadsir.callback.Callback;
import com.zlb.httplib.R;


/**
 * 拉取数据错误的时候的UI提示
 *
 */

public class ErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }
}
