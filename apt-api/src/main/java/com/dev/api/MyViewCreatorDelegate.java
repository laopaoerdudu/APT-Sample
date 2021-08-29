package com.dev.api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class MyViewCreatorDelegate implements IMyViewCreator {
    private IMyViewCreator mIMyViewCreator;

    @SuppressWarnings("all")
    private MyViewCreatorDelegate() {
        try {
            Class aClass = Class.forName("com.dev.apt_generation.MyViewCreatorImpl");
            mIMyViewCreator = (IMyViewCreator) aClass.newInstance();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static MyViewCreatorDelegate getInstance() {
        return Holder.MY_VIEW_CREATOR_DELEGATE;
    }

    private static final class Holder {
        private static final MyViewCreatorDelegate MY_VIEW_CREATOR_DELEGATE = new MyViewCreatorDelegate();
    }

    @Override
    public View createView(String name, Context context, AttributeSet attributeSet) {
        if (mIMyViewCreator != null) {
            return mIMyViewCreator.createView(name, context, attributeSet);
        }
        return null;
    }
}
