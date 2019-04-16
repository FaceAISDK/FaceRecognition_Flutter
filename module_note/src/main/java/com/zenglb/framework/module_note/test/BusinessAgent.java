package com.zenglb.framework.module_note.test;


/**
 * 代理类，静态代理类
 *
 */
public class BusinessAgent implements Sell{
    private Sell vendor;

    public BusinessAgent(Sell vendor){
        this.vendor = vendor;
    }

    public void sell() {
        vendor.sell();
    }

    public void ad() {
        vendor.ad();
    }

}
