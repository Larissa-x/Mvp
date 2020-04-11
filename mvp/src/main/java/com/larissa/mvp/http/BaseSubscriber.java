package com.larissa.mvp.http;
import com.larissa.mvp.bean.Result;
import com.larissa.mvp.mvp_interface.BaseView;
import com.larissa.mvp.utils.LogI;

import rx.Subscriber;

public abstract class BaseSubscriber<T extends Result,V extends BaseView> extends Subscriber<T> {
    protected V mView;
    private boolean isShow;

    public BaseSubscriber(V mView) {
        this.mView = mView;
    }

    public BaseSubscriber(V mView, boolean isShow) {
        this.mView = mView;
        this.isShow = isShow;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mView!=null){
            if (isShow){
                mView.showLoading(true);
            }
        }
    }

    @Override
    public void onCompleted() {
        if (mView!=null){
            mView.showLoading(false);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mView!=null){
            mView.showLoading(false);
        }
        LogI.i(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        if (t == null){
            onError("未获取到数据");
        }
        if (t.getStatus().equals("OK")){
            onSuccess(t);
        }else if (t.getStatus().equals("NOAUTH")){
            if (mView!=null)mView.noLogin();
            onError("未登录");
        }else{
            onError("未获取到数据");
        }
        onCompleted();
    }
    protected void onError(String msg){
        if (mView!=null){
            mView.showToast(msg);
        }
    }
    protected abstract void onSuccess(T result);
}
