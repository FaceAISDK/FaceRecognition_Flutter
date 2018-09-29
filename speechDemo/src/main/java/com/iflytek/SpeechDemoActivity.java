package com.iflytek;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.voicedemo.R;

/**
 * 语音识别使用的例子，用完删除
 * <p>
 * https://doc.xfyun.cn/msc_android/index.html
 */
public class SpeechDemoActivity extends BaseSpeechActivity implements OnClickListener {
    private static String TAG = SpeechDemoActivity.class.getSimpleName();

    private EditText mResultText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_demo);
        initLayout();
        mResultText = findViewById(R.id.iat_text);
    }

    public void showSpeechResult(String result){
        mResultText.setText(result);
    }

    /**
     * 初始化Layout。
     */
    private void initLayout() {
        findViewById(R.id.iat_recognize).setOnClickListener(SpeechDemoActivity.this);

        findViewById(R.id.iat_stop).setOnClickListener(SpeechDemoActivity.this);
        findViewById(R.id.iat_cancel).setOnClickListener(SpeechDemoActivity.this);
    }


    @Override
    public void onClick(View view) {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            Toast.makeText(this,"创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (view.getId()) {
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.iat_recognize:
                startAISpeech(mResultText);
                break;

            // 停止听写
            case R.id.iat_stop:
                mIat.stopListening();
                break;
            // 取消听写
            case R.id.iat_cancel:
                mIat.cancel();
                break;

        }
    }




}
