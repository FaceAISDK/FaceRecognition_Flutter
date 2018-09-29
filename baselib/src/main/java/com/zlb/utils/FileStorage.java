package com.zlb.utils;

import android.os.Environment;

import com.zlb.config.FileCacheConfig;

import java.io.File;

/**
 * 外部临时文件的存储路径
 *
 *
 * Created by Fsh on 2016/12/28.
 */
public class FileStorage {
    private File file;

    /**
     * 所有的App 的缓存路径都在一个目录下面，保证每个类型的临时文件只有一个
     * @param fileChildPath 要放的子目录
     */
    public FileStorage(String fileChildPath) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = new File(FileCacheConfig.CACHE_HOME+fileChildPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * 创建一个临时文件
     * @param fileName
     * @return
     */
    public File createTempFile(String fileName) {
        return new File(file, fileName);
    }

}
