package com.larissa.mvp.mvp_interface;


public interface BasePresenter<V extends BaseView> {
    void init();
    V getmView();
    void unBind();

}
