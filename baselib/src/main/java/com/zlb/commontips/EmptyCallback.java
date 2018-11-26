package com.zlb.commontips;

import com.kingja.loadsir.callback.Callback;
import com.zlb.httplib.R;


/**
 * 当数据是空的时候
 *
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_empty;
    }

}
