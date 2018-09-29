package com.iflytek;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.iflytek.voicedemo.R;

import java.util.List;

/**
 * 声音的POPUPVIEW
 */
public class AudioWavePopupView extends PopupWindow {

    private AudioWaveView audioWave;
    private TextView tipsTextView;

    public AudioWavePopupView(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.audio_wave_popview, null);
        this.setContentView(content);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

        tipsTextView = content.findViewById(R.id.tips);
        audioWave = content.findViewById(R.id.audioWave);

        //自定义paint
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        audioWave.setLinePaint(paint);

        int offset = 9;
        audioWave.setOffset(offset);

    }


    public List<Integer> getmVolumeList() {
        return audioWave.getmVolumeList();
    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, Context context) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.TOP, 0, 444);
        } else {
            this.dismiss();
        }
    }


    /**
     * 设置提示的
     *
     * @param tips
     */
    public void setTips(String tips) {
        tipsTextView.setText(tips);
    }


    public void startWaveView() {
        audioWave.startView();
    }


    /**
     * 停止WaveView
     */
    public void stopWaveView() {
        audioWave.stopView(true);
        this.dismiss();
    }

}