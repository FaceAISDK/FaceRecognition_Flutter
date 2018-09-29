package com.zlb.base;


public interface BasePresenter<T> {

    /**
     * 绑定P和V 的关系
     * Binds presenter with a view when resumed. The Presenter will perform initialization here.
     *
     * @param view the view associated with this presenter
     */
    void takeView(T view);

    /**
     * 当 页面销毁的时候取消对view 的引用
     * Drops the reference to the view when destroyed
     */
    void dropView();

}
