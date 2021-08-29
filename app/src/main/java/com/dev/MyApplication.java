package com.dev;

import android.app.Application;

import com.dev.annotation.ViewCreator;

@ViewCreator
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
