package com.zenglb.framework.module_note.test;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理类
 *
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
        Log.e(Tag,"Before");
        Object result = method.invoke(obj, args);
        Log.e(Tag,"After");
        return result;
    }


}
