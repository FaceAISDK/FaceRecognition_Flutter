package com.zlb.dagger;

import androidx.lifecycle.ViewModelProvider;
import com.zlb.dagger.viewmodel.MyViewModelFactory;
import com.zlb.dagger.component.BaseActivityComponent;
import dagger.Binds;
import dagger.Module;


/**
 * Jetpack 的View Model 的注入
 */
@Module(subcomponents = {
        BaseActivityComponent.class
})
public abstract class BaseAllViewModelModule {

    @Binds abstract ViewModelProvider.Factory bindViewModelFactory(MyViewModelFactory factory);

}
