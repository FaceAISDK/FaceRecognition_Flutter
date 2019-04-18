package com.zlb.jniInterface;

import java.util.HashMap;

/**
 * 要严格保存的数据
 *
 */
public class JniInvokeInterface {

    static {
        System.loadLibrary("httpconstant-lib");
    }

    public static native String getHttpHostStr();


    /**
     * 不要使用HashMap 吧，改为使用ArrayMap 吧
     *
     * @return
     */
    public static native HashMap<String,String> getJniHashMap();

}
