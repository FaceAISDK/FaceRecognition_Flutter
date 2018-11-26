package com.zenglb.framework.modulea.demo;

/**
 * 单例模式，万丈高楼平地起
 *
 * synchronized
 */
public class Single1 {

    //1.对于volatile修饰的变量（线程共享变量？）来说，在工作内存发生了变化后，必须要马上写到主内存中，
    // 而线程读取到是volatile修饰的变量时必须去主内存中去获取最新的值，而不是读工作内存中主内存的副本
    // 这就有效的保证了线程之间变量的可见性。

    // 同时能够防止指令重排
    private static volatile Single1 instance;

    private Single1() { }

    public static Single1 getInstance() {
        //2.双重校验第一步
        if (instance == null) {
            //3.同步加锁
            synchronized (Single1.class) {
                //双重校验的第二步
                if (instance == null) {
                    instance = new Single1();
                    //非原子操作需要分为3个步骤
                    //1.给 instance分配内存
                    //2.调用Single1构造方法初始化对象，形成实例
                    //3.把 instance 对象指向分配的内存空间
                }
            }
        }
        return instance;
    }

}



// Effective Java 第一版推荐写法
//public class Singleton {
//
//    private static class SingletonHolder {
//        private static final Singleton INSTANCE = new Singleton();
//    }
//
//    private Singleton (){}
//
//    public static final Singleton getInstance() {
//
//        return SingletonHolder.INSTANCE;
//
//    }
//}
