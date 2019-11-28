package com.anylife.module_main.dagger;

import android.content.Context;

import com.anylife.module_main.blog.BlogHttpService.BlogApiService;
import com.anylife.module_main.blog.BlogHttpService.BlogHttpRetrofit;
import com.anylife.module_main.http.MainModuleApiService;
import com.zlb.Sp.SPDao;
import com.zlb.httplib.HttpRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A MODULE  使用的全局的东西，API Service 等
 *
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Module
public class MainGlobalModule {

    /**
     * 这是我们的主ApiService,当然也有其他的系统Api
     *
     *
     *
     *
     * @param spDao      这个参数在BaseGlobal Module 中已经被依赖注入了
     * @param mContext   同理，没有显式的传入。
     * @return           AModuleApiService 动态代理的对象，并不是一个真正的AModuleApiService接口的implements产生的对象
     *
     * 当AModuleApiService对象调用getXXXXX方法时会被动态代理拦截，然后调用Proxy.newProxyInstance方法中的InvocationHandler对象，
     * 它的invoke方法会传入3个参数：
     *
     * Object proxy:    代理对象，不关心这个
     * Method method：  调用的方法，就是getAuthor方法
     * Object... args： 方法的参数，打一个断点就知道是哪些了
     *
     * 而Retrofit关心的就是method和它的参数args，接下去Retrofit就会用Java反射获取到getXXXXX方法的注解信息，配合args参数，创建一个ServiceMethod对象
     */
    @Provides
    @Singleton
    public MainModuleApiService provideApiService(SPDao spDao, Context mContext) {
        //Retrofit 的create 真是精华所在啊！
        return HttpRetrofit.getRetrofit(spDao, mContext).create(MainModuleApiService.class);
    }


    /**
     * 我们的App会遇到有多个Host，也就是有多个系统提供数据的情况
     * 这里Blog 的数据就是从第三方系统获取的，返回的数据格式也不一样
     *
     */
    @Provides
    @Singleton
    public BlogApiService provideBlogApiService() {
        //Retrofit 的create 真是精华所在啊！
        return BlogHttpRetrofit.getRetrofit().create(BlogApiService.class);
    }


}