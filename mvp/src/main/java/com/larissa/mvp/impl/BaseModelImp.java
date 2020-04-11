package com.larissa.mvp.impl;

import com.larissa.mvp.mvp_interface.BaseModel;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public  class BaseModelImp implements BaseModel {
    private CompositeSubscription mCs;

    public CompositeSubscription getmCs() {
        if (mCs == null || mCs.isUnsubscribed()){
            mCs = new CompositeSubscription();
        }
        return mCs;
    }

    @Override
    public void add(Observable o, Subscriber subscriber) {
                o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void add(Observable o, Action1 subscriber) {
        o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void addSubscription(Subscription subscription){
        if (subscription!=null){
            getmCs().add(subscription);
        }
    }





}
