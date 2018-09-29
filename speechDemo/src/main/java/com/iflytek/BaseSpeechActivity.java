package com.iflytek;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * 语音识别,不使用默认的识别UI，设置细节封装在这里
 * <p>
 * <p>
 * https://doc.xfyun.cn/msc_android/index.html
 * anylife.zlb@gmail.com
 */
public abstract class BaseSpeechActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public SpeechRecognizer mIat;  // 语音听写对象

    public HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();    // 用HashMap存储听写结果

    public List<Integer> mVolumeList;  //音量List

    public AudioWavePopupView audioWavePopupView;  //声音指示器

    /**
     * 继承使用本类的时候必须实现接收识别的数据
     *
     * @param result
     */
    public abstract void showSpeechResult(String result);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(BaseSpeechActivity.this, mInitListener);
    }


    /**
     * 学习某些App，把权限在这里先申请一下,必须的紧急的！
     */
    private static final int RC_AUDIO_STATUS = 1002;       //请求定位权限的Code

    private void checkPermission() {
        String[] perms = {Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 已经申请过权限，可以去干想干的事情
            startAISpeechAfterPermission();

        } else {
            EasyPermissions.requestPermissions(this, "我们需要手机的录音权限来进行语音识别",
                    RC_AUDIO_STATUS, perms);
        }
    }


    /**
     * Google EasyPermission, Android 连一个检查权限的完善库都没有，国内生态太乱了
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handle to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 权限授权了！
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        startAISpeechAfterPermission();
    }

    /**
     * 当用户点击了不再提醒的时候的处理方式
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this,"语音识别失败",Toast.LENGTH_SHORT).show();
    }


    /**
     * 开始语音识别
     * <p>
     * 如何判断一次听写结束：OnResult isLast=true 或者 onError
     */
    public void stopAISpeech() {

    }


    /**
     * 开始语音识别
     * <p>
     * 如何判断一次听写结束：OnResult isLast=true 或者 onError
     */
    public void startAISpeech(View parent) {
        myViewParent = parent;
        checkPermission();
    }


    View myViewParent;

    public void startAISpeechAfterPermission() {

        //移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(BaseSpeechActivity.this, "iat_recognize");

        mIatResults.clear();

        // 设置参数
        setParam();

        // 不显示听写对话框
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
            audioWavePopupView = new AudioWavePopupView(this);
            mVolumeList = audioWavePopupView.getmVolumeList();

            audioWavePopupView.showPopupWindow(myViewParent, BaseSpeechActivity.this);
            audioWavePopupView.startWaveView();
        }

    }


    int ret = 0; // 函数调用返回值

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = code -> {
        if (code != ErrorCode.SUCCESS) {
            showTip("初始化失败，错误码：" + code);
        }
    };


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            audioWavePopupView.setTips("请开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            String errorTips = error.getPlainDescription(true);

            showTip(errorTips);
            audioWavePopupView.setTips(errorTips);
            audioWavePopupView.stopWaveView();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            audioWavePopupView.setTips("正在语音转文字");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            audioWavePopupView.setTips("请继续说话");
            if (isLast) {
                audioWavePopupView.stopWaveView();
                audioWavePopupView = null;
            }
        }

        /**
         * 只有声音的大小改变了才会有变动，那么是否应该倒计时的加入上一没有更改的声音
         *
         * @param volume
         * @param data
         */
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            if (mVolumeList != null && mVolumeList.size() > 180) {
                mVolumeList.remove(0);
            }
            mVolumeList.add(volume);  //需要限制长度。
        }


        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        showSpeechResult(resultBuffer.toString());
    }


    public void showTip(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }


    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "zh_cn");


        mIat.setParameter(SpeechConstant.VOLUME, "100");// 设置音量，范围 0~100

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }

        //audioWavePopupView 也要停止释放资源，
        if (null != audioWavePopupView) {
            audioWavePopupView.stopWaveView();
        }
    }


}

