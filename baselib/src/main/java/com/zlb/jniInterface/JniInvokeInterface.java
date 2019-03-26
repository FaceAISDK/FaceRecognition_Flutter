package com.zlb.jniInterface;

import java.util.HashMap;


public class JniInvokeInterface {

    static {
        System.loadLibrary("httpconstant-lib");
    }

    public static native String getHttpHostStr();

    public static native HashMap<String,String> getJniHashMap();

}
