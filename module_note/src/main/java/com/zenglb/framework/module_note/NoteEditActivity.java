package com.zenglb.framework.module_note;

import android.os.Bundle;
import android.widget.TextView;

import com.zenglb.framework.module_note.http.NoteApiService;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseActivity;
import com.zlb.base.BaseMVPActivity;

import javax.inject.Inject;

/**
 * java 代码里调用Kotlin TestBean,优雅的处理NULL 判断处理.
 */
public class NoteEditActivity extends BaseMVPActivity {




    protected TextView test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_note_edit;
    }


    /**
     * NPE 问题使用KOTLIN 来优雅的处理
     */
    @Override
    protected void initViews() {
        test2 = findViewById(R.id.test2);
    }





}
