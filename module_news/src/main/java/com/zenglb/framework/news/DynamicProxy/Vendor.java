package com.zenglb.framework.news.DynamicProxy;

import android.util.Log;

/**
 * 生成厂家，委托类
 *
 */
public class Vendor implements Sell {

    @Override
    public void sell() {
        Log.e("HHHH","in sell method");
    }

    @Override
    public void ad() {
        Log.e("HHHH","in ad method");
    }

}
