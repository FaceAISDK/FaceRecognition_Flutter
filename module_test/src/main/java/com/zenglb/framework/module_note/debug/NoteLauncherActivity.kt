package com.zenglb.framework.module_note.debug

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zenglb.framework.module_note.NoteMainActivity
import com.zenglb.framework.module_note.R
import com.zenglb.framework.module_note.cameraX.CameraXActivity
import com.zlb.base.BaseDaggerActivity


/**
 * 单独的Module 也需要Launcher
 *
 */
class NoteLauncherActivity : BaseDaggerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyAppTheme)
        super.onCreate(savedInstanceState)

        //倒计时自动到达NoteMainActivity
        val i1 = Intent()
        i1.setClass(this@NoteLauncherActivity, NoteMainActivity::class.java)
        startActivity(i1)
        this@NoteLauncherActivity.finish()
    }


    override val layoutId: Int
        get() = R.layout.launcher_layout


    override fun initViews() {
        setToolBarVisible(View.GONE)
    }


}
