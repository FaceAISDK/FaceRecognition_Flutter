package com.zenglb.framework.camerax

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.Intent
import android.widget.FrameLayout
import android.widget.Toast
import com.zenglb.framework.camerax.utils.FLAGS_FULLSCREEN

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L

/**
 * Main entry point into our app. This app follows the single-activity pattern,
 * and all functionality is implemented in the form of fragments.
 *
 * 看看能否把拍照，切换摄像头，到相册等控制分离处理
 *
 * 合并到 AndroidX 的时候才能使用
 *
 * https://codelabs.developers.google.com/codelabs/camerax-getting-started/#7
 *
 */
class CameraXActivity : AppCompatActivity(){
    private lateinit var container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.fragment_container)

        container.setOnClickListener { Toast.makeText(this,"点击预览",Toast.LENGTH_LONG).show() }
    }

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        container.postDelayed({
            container.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }


    /**
     * 监听音量向下按键，发送本地广播，那UI 控制也可以抽取出来的啊
     *
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }


    /***
     * 伴生对象
     *
     */
    companion object {
        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }


}
