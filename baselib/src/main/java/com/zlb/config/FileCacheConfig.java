package com.zlb.config;


import android.os.Environment;

/**
 * Created by zenglb on 2017/3/15.
 */
public class FileCacheConfig {
    public static final String CACHE_HOME = Environment.getExternalStorageDirectory() + "/yourAppName/";  //Private
    public static final String CACHE_IMAGE = CACHE_HOME + "images/";          //图片
    public static final String INSTALLATION = CACHE_HOME + "Installation/";   //App 的安装信息

    public static final String CACHE_DATA = CACHE_HOME + "data/";
    public static final String CACHE_AUDIO = CACHE_HOME + "audio/";
    public static final String CACHE_APK = CACHE_HOME + "apk/";



}