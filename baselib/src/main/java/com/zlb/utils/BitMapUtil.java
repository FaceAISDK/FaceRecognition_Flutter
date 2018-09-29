package com.zlb.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class BitMapUtil {

    /**
     *zlb
     */
    public static Bitmap getSimpleByBelowLine(Context context,Uri file, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只是去读取图片的信息，不要实际的分配空间
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(file),null,options);
        }catch (Exception e){

        }
        int originWidth= options.outWidth;
        int originHeight = options.outHeight;
        int simpleSize=1;
        if(originWidth>maxWidth||originHeight>maxHeight){
            simpleSize = calculateInSampleSize(options,maxWidth,maxHeight);   //最大的图片的size 限制在1920*1080
        }
        options.inJustDecodeBounds = false;
        if(simpleSize>1){
            options.inSampleSize = simpleSize;
        }
        options.inPreferredConfig= Bitmap.Config.RGB_565;   //降低编码成像质量
        Bitmap bitmap=null;
        try {
            bitmap=BitmapFactory.decodeStream(context.getContentResolver().openInputStream(file),null,options);
        }catch (Exception e){

        }
        return bitmap;
    }

    /**
     *zlb
     */
    public static Bitmap getSimpleByBelowLine(String file, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只是去读取图片的信息，不要实际的分配空间
        BitmapFactory.decodeFile(file, options);
        int originWidth= options.outWidth;
        int originHeight = options.outHeight;
        int simpleSize=1;
        if(originWidth>maxWidth||originHeight>maxHeight){
            simpleSize = calculateInSampleSize(options,maxWidth,maxHeight);   //最大的图片的size 限制在1920*1080
        }

        options.inJustDecodeBounds = false;
        if(simpleSize>1){
            options.inSampleSize = simpleSize;
        }
        options.inPreferredConfig= Bitmap.Config.RGB_565;   //降低编码成像质量

        return BitmapFactory.decodeFile(file, options);
    }


    /**
     * 计算缩放比例的方法    zlb
     *
     * @param op
     * @param reqWidth
     * @param reqheight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options op, int reqWidth, int reqheight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqheight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;

            while ((halfWidth / inSampleSize > reqWidth)||(halfHeight / inSampleSize > reqheight)) {
                inSampleSize *= 2;
            }
            if(originalWidth/(2*inSampleSize)>reqWidth||originalHeight/(2*inSampleSize)>reqheight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }



    public static void saveBmpToSD(Bitmap bmp,String path) {
        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *
     *
     * @param str
     * @param size
     * @return
     */
    public static Bitmap Create2DCode(String str, int size) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, size, size);
            matrix = deleteWhite(matrix);//删除白边
            size = matrix.getWidth();
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * size + x] = Color.BLACK;
                    } else {
                        pixels[y * size + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除白色的边
     *
     * @param matrix
     * @return
     */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }


}
