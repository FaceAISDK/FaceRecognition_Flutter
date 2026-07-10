package com.faceaisdk.flutter

import android.content.Context
import android.view.View
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

class FaceRecognitionView(
    context: Context,
    id: Int,
    creationParams: Map<String?, Any?>?,
    messenger: BinaryMessenger
) : PlatformView, MethodChannel.MethodCallHandler {

    private val placeholderView: View = View(context)
    private val methodChannel: MethodChannel = MethodChannel(messenger, "com.facerecognition/view_$id")

    init {
        methodChannel.setMethodCallHandler(this)
    }

    override fun getView(): View {
        return placeholderView
    }

    override fun dispose() {
        methodChannel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "startScan", "stopScan" -> {
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }
}
