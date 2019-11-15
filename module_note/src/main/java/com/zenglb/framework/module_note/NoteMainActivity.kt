package com.zenglb.framework.module_note

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.zenglb.framework.module_note.cameraX.CameraXActivity
import com.zenglb.framework.module_note.http.NoteApiService
import com.zenglb.framework.module_note.objectbox.ObjectBox.boxStore
import com.zlb.base.BaseDaggerActivity
import com.zlb.httplib.DefaultObserver
import com.zlb.httplib.scheduler.SwitchSchedulers
import io.objectbox.android.AndroidScheduler

import kotlinx.android.synthetic.main.app_bar_note_main.*
import javax.inject.Inject

/**
 * 练习使用Kotlin 的使用,混合Java 开发
 *
 * kotlin+dagger+objectBox
 *
 */
class NoteMainActivity : BaseDaggerActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var apiService: NoteApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        testObjectBox()

//        getLocalModuleTest()

        //类型后面加?表示可为空
         var age: String? = "34567"
        //抛出空指针异常
        val ages = age!!.toInt()
        //不做处理返回 null
        val ages1 = age?.toInt() ?: 1111
        //age为空返回-1
        val ages2 = age?.toInt() ?: -1

        Log.e("HHH","value: "+parseInt("3234232"))

        var a=1
    }


    /**
     * 解析Int
     *
     */
    private fun parseInt(str: String): Int? {
        return str.toInt()
    }


    /**
     * 配合Spring 项目进行开发测试
     *
     */
    fun getLocalModuleTest() {
        apiService.getPropertyStr()
                .compose(SwitchSchedulers.applySchedulers())
                //BaseObserver 参数问题优化，如果不传参数context 的话，依赖context 的功能就要改
                .subscribe(object : DefaultObserver<String>(context) {
                    override fun onSuccess(userBean: String?) {
                        userBean.toString()
                    }
                })
    }


    /**
     *
     *
     */
    fun getLocalModuleTest2() {
        apiService.getUsers()
                .compose(SwitchSchedulers.applySchedulers())
                //BaseObserver 参数问题优化，如果不传参数context 的话，依赖context 的功能就要改
                .subscribe(object : DefaultObserver<List<UserObjectTestBean>>(context) {
                    override fun onSuccess(userBean: List<UserObjectTestBean>?) {
                        userBean.toString()
                    }
                })
    }


    /**
     * 测试使用ObjectBox
     *
     */
    fun testObjectBox() {
        val userbox = boxStore.boxFor(UserObjectTestBean::class.java)

        var userBean = UserObjectTestBean()

        userBean.age = 33

        userBean.userName = "jackson" + SystemClock.currentThreadTimeMillis()

        userbox.put(userBean)

        Log.e("OBJECTBOX", userbox.all?.get(0)?.userName + "fdaf" + apiService.toString())

        //练习使用Rxjava
        // https://docs.objectbox.io/data-observers-and-rx
        queryAllWithRxJava()
    }



    /**
     * 练习ObjextBox 和 rxjava 的结合使用 10.39.231.102
     *
     */
    fun queryAllWithRxJava() {
        val query = boxStore.boxFor(UserObjectTestBean::class.java).query().build()
        query.subscribe()
                .on(AndroidScheduler.mainThread())
                .observer { data ->
                    updateUi(data)

                    Thread.currentThread().name;
                }
    }


    /**
     * 更新UI
     *
     */
    private fun updateUi(data: List<UserObjectTestBean>) {
        data.toString()
    }

    override val layoutId: Int
        get() = R.layout.activity_note_main


    //    override var layoutId: Int
//        get() = R.layout.activity_note_main
//        set(value) {}

    /**
     * 视图的初始化等
     */
    override fun initViews() {
        setToolBarVisible(View.GONE)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            //            getLocalModuleTest()
            //倒计时自动到达NoteMainActivity

            val i1 = Intent()
            i1.setClass(this, CameraXActivity::class.java)
            startActivity(i1)
            this.finish()

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.note_navigation_drawer_open, R.string.note_navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    /**
     * 后退按键修改为DrawerLayout
     *
     */
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.note_main_activity, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
