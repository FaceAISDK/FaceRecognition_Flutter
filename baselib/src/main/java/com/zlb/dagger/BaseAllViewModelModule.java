package com.zlb.dagger;

import androidx.lifecycle.ViewModelProvider;
import com.zlb.dagger.viewmodel.MyViewModelFactory;
import com.zlb.dagger.component.BaseActivityComponent;
import dagger.Binds;
import dagger.Module;


/**
 * Jetpack 的ViewModel 的注入
 *
 *
 *  May you do good and not evil
 *  May you find forgiveness for yourself and forgive others
 *  May you share freely,never taking more than you give
 *
 */
@Module(subcomponents = {
        BaseActivityComponent.class
})
public abstract class BaseAllViewModelModule {

    @Binds abstract ViewModelProvider.Factory bindViewModelFactory(MyViewModelFactory factory);

}
