package com.larissa.mvp.mvp_interface;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public interface BaseActivityView extends BaseView{
    AppCompatActivity getmActivity();
    void showToolBar(boolean show);
    void onActivityStart(Bundle savedInstanceState);
}
