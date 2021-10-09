package com.ameldev.tiktokprodownloader;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MyApplication getContext(){
        return mContext;
    }
}