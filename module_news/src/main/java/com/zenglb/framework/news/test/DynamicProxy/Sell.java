package com.zenglb.framework.news.test.DynamicProxy;


/**
 * 下面我们用Vendor类代表生产厂家，BusinessAgent类代表微商代理，
 * 来介绍下静态代理的简单实现，委托类和代理类都实现了Sell接口
 * Sell接口的定义如下
 *
 */
public interface Sell {
    void sell();
    void ad();
}
