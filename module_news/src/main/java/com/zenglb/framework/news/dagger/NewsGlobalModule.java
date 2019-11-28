package com.zenglb.framework.news.dagger;

import android.content.Context;

import com.zenglb.framework.news.http.NewsHttpService.NewsHttpRetrofit;
import com.zenglb.framework.news.http.NewsHttpService.NewsApiService;
import com.zlb.Sp.SPDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * News MODULE  使用的全局的东西，API Service 等
 *
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Module
public class NewsGlobalModule {

    /**
     *
     * @param spDao      这个参数在BaseGlobal Module 中已经被依赖注入了
     * @param mContext   同理，没有显式的传入。
     * @return           AModuleApiService 动态代理的对象，并不是一个真正的AModuleApiService接口的implements产生的对象
     *
     * 当AModuleApiService对象调用getXXXXX方法时会被动态代理拦截，然后调用Proxy.newProxyInstance方法中的InvocationHandler对象，
     * 它的invoke方法会传入3个参数：
     *
     *      Object proxy:    代理对象，不关心这个
     *      Method method：  调用的方法，就是getAuthor方法
     *      Object... args： 方法的参数，打一个断点就知道是哪些了
     *
     * 而Retrofit关心的就是method和它的参数args，接下去Retrofit就会用Java反射获取到getXXXXX方法的注解信息，配合args参数，创建一个ServiceMethod对象
     */
    @Provides
    @Singleton
    public NewsApiService provideApiService(SPDao spDao, Context mContext) {
        //Retrofit 的create 真是精华所在啊！
        //retrofit.create(Api.class) 动态代理

        //Return 的api对象其实是一个动态代理对象，并不是一个真正的NewsApiService接口的implements产生的对象

        return NewsHttpRetrofit.getRetrofit().create(NewsApiService.class);
    }

}