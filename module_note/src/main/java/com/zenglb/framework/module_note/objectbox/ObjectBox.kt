package com.zenglb.framework.module_note.objectbox

import android.content.Context
import android.util.Log
import com.zenglb.framework.module_note.BuildConfig
import com.zenglb.framework.module_note.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

/**
 * Singleton to keep BoxStore reference.
 */
object ObjectBox {
    //全缀的意思是
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        //default.json 是默认的名字默认的位置。也可以分账户分库使用
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()

        if (BuildConfig.DEBUG) {
            val isStartObjectBox = AndroidObjectBrowser(boxStore).start(context.applicationContext)
            Log.d("ObjectBox", "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})  " + isStartObjectBox)
        }
    }

}