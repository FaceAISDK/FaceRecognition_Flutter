package com.zlb.utils;

import com.zlb.config.FileCacheConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;


/**
 * 应用第一次安装后有了储存权限后，调用这个就会在SD 的目录产生一个UUID 文件
 *
 * 还是建议再封装一层，使用SP 来中间过度
 *
 */
public class Installation {
    private static String sID = null;
    private static final String INSTALLATION_FILE_NAME = "installationUUID";

    /**
     * 创建目录
     *
     * @param dirPath
     * @return
     */
    private static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 读取UUID
     *
     * @return
     */
    public synchronized static String readUUID() {
        if (sID == null) {

            //这里需要的是App 卸载后再安装仍然认同是同一部的设备，否则new File(context.getFilesDir(), INSTALLATION)
            //context.getFilesDir() 是App 的安装目录，卸载后数据就没有了
            createDirs(FileCacheConfig.INSTALLATION);
            File installation = new File(FileCacheConfig.INSTALLATION, INSTALLATION_FILE_NAME);
            try {
                if (!installation.exists()){
                    writeInstallationFile(installation);
                }
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }



    /**
     * 读取安装信息
     *
     * @param installation
     * @return
     * @throws IOException
     */
    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();

        // 返回的是16 位的大写的MD5 后的值
        return MD5Util.getUpperMD5Str16(new String(bytes));
    }


    /**
     * 写安装信息，把UUID 的信息写在一个地方
     *
     * @param installation
     * @throws IOException
     */
    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString()+System.currentTimeMillis();
        out.write(id.getBytes());
        out.close();
    }


}
