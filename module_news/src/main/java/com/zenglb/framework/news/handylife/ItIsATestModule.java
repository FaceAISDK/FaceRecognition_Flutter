package com.zenglb.framework.news.handylife;

import dagger.Module;
import dagger.Provides;


/**
 * 在 {@link NewsPackageActivity } 中测试使用 ！
 * 测试使用
 *
 */
@Module
public class ItIsATestModule {

    @Provides
    String provideName() {
        return "Hello Kitty,It is a Test Module String";  //
    }



}
