package com.zenglb.framework.news.test.DynamicProxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理类
 *
 *
 */
public class DynamicProxy implements InvocationHandler {
    String Tag=DynamicProxy.class.getSimpleName();

    //obj为委托类对象;
    private Object obj;

    public DynamicProxy(Object obj) {
        this.obj = obj;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e("HHHH 111","Before111");
        Object result = method.invoke(obj, args);
        Log.e("HHHH 111","After111");
        return result;
    }


}
