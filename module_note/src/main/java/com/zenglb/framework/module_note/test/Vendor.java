package com.zenglb.framework.module_note.test;

import android.util.Log;

/**
 * 生成厂家，委托类
 *
 */
public class Vendor implements Sell {
    String Tag=Vendor.class.getSimpleName();


    @Override
    public void sell() {
        Log.e(Tag,"in sell method");
    }

    @Override
    public void ad() {
        Log.e(Tag,"in ad method");
    }
}
