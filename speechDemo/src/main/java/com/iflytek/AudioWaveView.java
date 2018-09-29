package com.iflytek;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;


import com.iflytek.voicedemo.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 科大讯飞语音识别时候的声波动画和提示
 *
 * 不是很完善，时间不充足，后面再优化吧
 * anylife.zlb@gmail.com
 */
public class AudioWaveView extends View {

    final protected Object mLock = new Object();

    private Context mContext;

    private Bitmap mBitmap, mBackgroundBitmap;

    private Paint mPaint;

    private Paint mViewPaint;

    private Canvas mCanvas = new Canvas();

    private Canvas mBackCanVans = new Canvas();

    private drawThread mInnerThread;

    private int mWidthSpecSize;
    private int mHeightSpecSize;
    private int mBaseLine;

    private int mOffset = -11;//波形之间线与线的间隔


    private boolean mIsDraw = true;


    private boolean mPause = false;//是否站暂停

    private int mWaveCount = 2;

    private int mWaveColor = Color.WHITE;


    private final List<Integer> mVolumeList = new ArrayList<>();

    /**
     * invalidate
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AudioWaveView.this.invalidate();
        }
    };

    public AudioWaveView(Context context) {
        super(context);
        init(context, null);
    }

    public AudioWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AudioWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsDraw = false;
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        if (mBackgroundBitmap != null && !mBackgroundBitmap.isRecycled()) {
            mBackgroundBitmap.recycle();
        }
    }

    public void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (isInEditMode())
            return;

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.waveView);
            mOffset = ta.getInt(R.styleable.waveView_waveOffset, dip2px(context, -11));
            mWaveColor = ta.getColor(R.styleable.waveView_waveColor, Color.WHITE);
            mWaveCount = ta.getInt(R.styleable.waveView_waveCount, 2);
            ta.recycle();
        }

        if (mOffset == dip2px(context, -11)) {
            mOffset = dip2px(context, 1);
        }

        if (mWaveCount < 1) {
            mWaveCount = 1;
        } else if (mWaveCount > 2) {
            mWaveCount = 2;
        }

        mPaint = new Paint();
        mViewPaint = new Paint();
        mPaint.setColor(mWaveColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        createBackGroundBitmap();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && mBackgroundBitmap == null) {
            createBackGroundBitmap();
        }
    }


    private void createBackGroundBitmap() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getWidth() > 0 && getHeight() > 0) {
                    mWidthSpecSize = getWidth();
                    mHeightSpecSize = getHeight();
                    mBaseLine = mHeightSpecSize / 2;
                    mBackgroundBitmap = Bitmap.createBitmap(mWidthSpecSize, mHeightSpecSize, Bitmap.Config.ARGB_8888);
                    mBitmap = Bitmap.createBitmap(mWidthSpecSize, mHeightSpecSize, Bitmap.Config.ARGB_8888);
                    mBackCanVans.setBitmap(mBackgroundBitmap);
                    mCanvas.setBitmap(mBitmap);
                    ViewTreeObserver vto = getViewTreeObserver();
                    vto.removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    int offset = 22;

    /**
     * 修改为根据音量的大小来绘制波纹
     * 但是要考虑音量是非线性的，最好改为非线性的
     */
    private class drawThread extends Thread {
        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            while (mIsDraw) {

                List<Integer> myVolumeList = new ArrayList<>();
                synchronized (mVolumeList) {
                    if (mVolumeList.size() != 0) {
                        try {
                            myVolumeList = (List<Integer>) deepCopy(mVolumeList);// 保存  接收数据
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }

                if (mBackgroundBitmap == null) {
                    continue;
                }

                if (!mPause) {

                    if (mBackCanVans != null) {
                        mBackCanVans.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

//                        int startPosition = (mDrawReverse) ? mWidthSpecSize - mDrawStartOffset : mDrawStartOffset;

                        mBackCanVans.drawLine(0, mBaseLine, mWidthSpecSize, mBaseLine, mPaint);

                        offset = 2;  //绘制的偏移量
                        //======== 从下面开始使用真实的音量来画东西=======

                        int size = myVolumeList.size();

                        int maxVolume = 1;  //已经出现的最大的声音
                        for (int i = 0; i < size; i++) {
                            int x = myVolumeList.get(i); //取出声音大小的值

                            if (x > maxVolume) {
                                maxVolume = x;
                            }
                        }

                        int maxAbsY = (mHeightSpecSize - mBaseLine);

                        for (int i = 0; i < size; i++) {

                            offset = offset + 5;
                            int j = offset;

                            int x = myVolumeList.get(i); //取出声音值

                            x = (maxAbsY * 4 * x) / (5 * maxVolume); //按照比例从新的赋值

                            if(x==0) x=1;

                            int max = mBaseLine + x;
                            int min = mBaseLine - x;

                            mBackCanVans.drawLine(j, mBaseLine, j, max, mPaint);
                            mBackCanVans.drawLine(j, min, j, mBaseLine, mPaint);
                        }

                        //======== 从下面开始使用真实的音量来画东西=======

                        synchronized (mLock) {
                            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            mCanvas.drawBitmap(mBackgroundBitmap, 0, 0, mPaint);
                        }

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }
                //休眠暂停资源
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * deepClone to avoid ConcurrentModificationException
     *
     * @param src list
     * @return dest
     */
    public List deepCopy(List src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List dest = (List) in.readObject();
        return dest;
    }


    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        if (mIsDraw && mBitmap != null) {
            synchronized (mLock) {
                c.drawBitmap(mBitmap, 0, 0, mViewPaint);
            }
        }
    }



    /**
     * 开始绘制
     */
    public void startView() {
        if (mInnerThread != null && mInnerThread.isAlive()) {
            mIsDraw = false;
            while (mInnerThread.isAlive()) ;
            mBackCanVans.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        mIsDraw = true;
        mInnerThread = new drawThread();
        mInnerThread.start();
    }

    /**
     * 停止绘制
     */
    public void stopView(boolean cleanView) {
        mIsDraw = false;
        if (mInnerThread != null) {
            while (mInnerThread.isAlive()) ;
        }
        if (cleanView) {
            mVolumeList.clear();
            mBackCanVans.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
    }

    /**
     * 停止绘制
     */
    public void stopView() {
        stopView(true);
    }



    public boolean isPause() {
        return mPause;
    }

    public void setPause(boolean pause) {
        synchronized (mVolumeList) {
            this.mPause = pause;
        }
    }


    /**
     * 将这个list传到CallBack 填充
     *
     */
    public List<Integer> getmVolumeList() {
        return mVolumeList;
    }

    /**
     * 设置线与线之间的偏移
     *
     * @param offset 偏移值 pix
     */
    public void setOffset(int offset) {
        this.mOffset = offset;
    }


    public int getWaveColor() {
        return mWaveColor;
    }

    /**
     * 设置波形颜色
     *
     * @param waveColor 音频颜色
     */
    public void setWaveColor(int waveColor) {
        this.mWaveColor = waveColor;
        if (mPaint != null) {
            mPaint.setColor(mWaveColor);
        }
    }

    /**
     * 设置自定义的paint
     */
    public void setLinePaint(Paint paint) {
        if (paint != null) {
            mPaint = paint;
        }
    }


    /**
     * 设置波形颜色
     *
     * @param waveCount 波形数量 1或者2
     */
    public void setWaveCount(int waveCount) {
        mWaveCount = waveCount;
        if (mWaveCount < 1) {
            mWaveCount = 1;
        } else if (mWaveCount > 2) {
            mWaveCount = 2;
        }
    }

    /**
     * dip转为PX
     */
    private int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }





}
