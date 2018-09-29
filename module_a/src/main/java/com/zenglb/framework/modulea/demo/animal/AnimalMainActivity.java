package com.zenglb.framework.modulea.demo.animal;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zlb.base.BaseActivity;
import com.zenglb.framework.modulea.R;

/**
 * 动画演示的主页面,Tool bar 不实用Base 的那个
 * <p>
 * https://github.com/lgvalle/Material-Animations
 */
public class AnimalMainActivity extends BaseActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        getToolbar().setVisibility(View.GONE);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_animal_main;
    }

    @Override
    protected void initViews() {
        String  title = getIntent().getExtras().getString("title");
        ((TextView) findViewById(R.id.title)).setText(title);

        toolbar=(Toolbar) findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        getWindow().getEnterTransition().setDuration(300);
    }


}
