package com.zlb.commontips;

import com.kingja.loadsir.callback.Callback;
import com.zlb.httplib.R;


/**
 * 当数据是空的时候 UI 提示
 *
 */

public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_empty;
    }

}
