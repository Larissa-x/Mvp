package com.larissa.mvp.impl;

import androidx.databinding.ViewDataBinding;

import com.larissa.mvp.mvp_interface.BaseModel;
import com.larissa.mvp.mvp_interface.BasePresenter;
import com.larissa.mvp.mvp_interface.BaseView;

public abstract class BasePresenterImp<AV extends ViewDataBinding,V extends BaseView> implements BasePresenter<V> {
    private V mView;
    private BaseModel mModel;
    private AV mBinding;


    public BasePresenterImp(AV mBinding, V mView) {
        this.mView = mView;
        this.mBinding = mBinding;
        mModel = new BaseModelImp();
    }

    @Override
    public V getmView() {
        return mView;
    }

    public AV getmBinding() {
        return mBinding;
    }

    public BaseModel getmModel() {
        return mModel;
    }


    @Override
    public void unBind() {
        if (getmBinding()!=null){
            mBinding.unbind();
            mBinding = null;
        }
        if (getmView()!=null){
            mView = null;
        }
        if (getmModel()!=null){
            mModel = null;
        }

    }
}
