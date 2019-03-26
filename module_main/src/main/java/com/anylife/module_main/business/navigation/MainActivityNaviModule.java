package com.anylife.module_main.business.navigation;

import dagger.Module;

/**
 *
 * anylife.zlb@gmail.com
 */
@Module
public abstract class MainActivityNaviModule {
    //NOTE:  IF you want to have something be only in the Fragment scope but not activity mark a
    //@provides or @Binds method as @FragmentScoped.  Use case is when there are multiple fragments
    //in an activity but you do not want them to share all the same objects.


//    @FragmentScope
//    @ContributesAndroidInjector
//    abstract Rxjava2DemoFragment provideRxjava2DemoFragment();


//    @FragmentScope
//    @ContributesAndroidInjector
//    abstract MeProfileFragment provideMeProfileFragment();


//    @Provides
//    @ActivityScope
//    @Nullable
//    static String provideTaskId(MainActivityBottomNavi activity) {
//        return activity.getIntent().getStringExtra(MainActivityBottomNavi.);
//    }


//    @Provides
//    @ActivityScope
//    static boolean provideStatusDataMissing(AddEditTaskActivity activity) {
//        return activity.isDataMissing();
//    }


    // Rather than having the activity deal with getting the intent extra and passing it to the presenter
    // we will provide the taskId directly into the AddEditTaskActivitySubcomponent
    // which is what gets generated for us by Dagger.Android.
    // We can then inject our TaskId and state into our Presenter without having pass through dependency from
    // the Activity. Each UI object gets the dependency it needs and nothing else.


//    @ActivityScope
//    @Binds
//    abstract AddEditTaskContract.Presenter taskPresenter(AddEditTaskPresenter presenter);


}
