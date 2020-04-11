package com.larissa.mvp.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.larissa.mvp.R;
import com.larissa.mvp.databinding.DialogLoadingBinding;
import com.blankj.utilcode.util.EmptyUtils;
import com.flyco.dialog.widget.base.BaseDialog;

public class LoadingDialog extends BaseDialog<LoadingDialog> {

    private DialogLoadingBinding mBinding;
    private String msg;

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        mBinding = DataBindingUtil.bind(LayoutInflater.from(mContext).inflate(R.layout.dialog_loading,null));
        return null;
    }

    @Override
    public void setUiBeforShow() {
        if (EmptyUtils.isNotEmpty(msg)){
            mBinding.tvMessage.setText(msg);
        }
    }
    public LoadingDialog setMsg(String msg){
        if (EmptyUtils.isNotEmpty(msg)){
            this.msg = msg;
        }
        return this;
    }
}
