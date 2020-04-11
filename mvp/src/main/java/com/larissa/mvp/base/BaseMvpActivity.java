package com.larissa.mvp.base;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.larissa.mvp.R;
import com.larissa.mvp.bean.ActionBarBean;
import com.larissa.mvp.databinding.ActivityBaseMvpBinding;
import com.larissa.mvp.databinding.BaseActionbarBinding;
import com.larissa.mvp.databinding.BaseContentBinding;
import com.larissa.mvp.mvp_interface.BaseActivityView;
import com.larissa.mvp.mvp_interface.BasePresenter;
import com.blankj.utilcode.util.ToastUtils;

public abstract class BaseMvpActivity<AV extends ViewDataBinding,P extends BasePresenter> extends AppCompatActivity implements BaseActivityView {
    private P mPresenter;
    protected AV mContentBinding;
    private ActivityBaseMvpBinding mBaseBinding;
    private BaseActionbarBinding mActionBarBinding;
    private ActionBarBean mActionBarBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseBinding = DataBindingUtil.setContentView(this,R.layout.activity_base_mvp);
        mContentBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.base_content,null,false);
        mContentBinding.getRoot().setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBaseBinding.includeContent.rlContent.addView(mContentBinding.getRoot());
        initToolBar();
        mPresenter = createPresenter();
        onActivityStart(savedInstanceState);
    }
    abstract P createPresenter();

    /**
     * 设置状态栏
     */
    protected void setStatusBar(){

    }

    /**
     * 初始化toolbar
     */
    private void initToolBar(){
        setSupportActionBar(mBaseBinding.includeToolbar.toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(false);
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setHomeButtonEnabled(false);
        supportActionBar.setDisplayShowCustomEnabled(true);
        supportActionBar.setCustomView(initActionBar());
    }

    /**
     * 设置toolbar显示样式
     * 初始化ActionBar
     * @return
     */
    private View initActionBar(){
        mActionBarBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.base_actionbar, null, false);
        mActionBarBinding.getRoot().setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mActionBarBean = new ActionBarBean(getResources().getString(R.string.app_name), getResources().getDrawable(R.drawable.fanhui), null, null);
        mActionBarBinding.setActionbarBean(mActionBarBean);
        mActionBarBinding.leftImbt.setOnClickListener(v -> onLeftActionClick(v));
        mActionBarBinding.rightImbt.setOnClickListener(v -> onRightActionClick(v));
        mActionBarBinding.rightTv.setOnClickListener(v -> onRightTvActionClick(v));
        return mActionBarBinding.getRoot();
    }


    /**
     * 用于修改Toolbar显示内容
     * @param actionBarBean
     */
     protected  void setActionBarBean(ActionBarBean actionBarBean ){
        if (this.mActionBarBean == null){
            this.mActionBarBean = new ActionBarBean(getResources().getString(R.string.app_name),getResources().getDrawable(R.drawable.fanhui),null);
        }
        if (!TextUtils.isEmpty(actionBarBean.getTitle())){
            this.mActionBarBean.setTitle(actionBarBean.getTitle());
        }
        this.mActionBarBean.setLeft(actionBarBean.getLeft());
        this.mActionBarBean.setRight(actionBarBean.getRight());
        this.mActionBarBean.setRightTv(actionBarBean.getRightTv());
    }

    /**
     * 用于修改Toolbar显示内容
     * @param title
     */
    protected void setActionBarBean(String title){
        if (this.mActionBarBean == null){
            this.mActionBarBean = new ActionBarBean(getResources().getString(R.string.app_name),getResources().getDrawable(R.drawable.fanhui),null);
        }
        if (!TextUtils.isEmpty(title)){
            this.mActionBarBean.setTitle(title);
        }
        this.mActionBarBean.setLeft(getResources().getDrawable(R.drawable.fanhui));
    }



    protected void onLeftActionClick(View view){
        onBackPressed();
    }
    protected void onRightActionClick(View view){

    }
    protected void onRightTvActionClick(View view){

    }

    /**
     * Toast
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        ToastUtils.showLongSafe(msg);
    }

    /**
     * 显示|关闭加载进度条
     * @param show
     */
    @Override
    public void showLoading(boolean show) {

    }

    /**
     * 当前页面是否显示Toolbar
     * @param show
     */
    @Override
    public void showToolBar(boolean show) {
        if (show){
            getActionBar().show();
        }else{
            getActionBar().hide();
        }
    }

    @Override
    public void noLogin() {

    }

    @Override
    public AppCompatActivity getmActivity() {
        return this;
    }


    public P getmPresenter() {
        return mPresenter;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getmPresenter()!=null){
            getmPresenter().unBind();
        }
    }
}
