package com.dev.api;

import android.app.Activity;

import java.lang.reflect.Constructor;

public class MyButterKnife {
    @SuppressWarnings({"all"})
    public static void bind(Activity activity) {
        try {
            Class bindingClass = Class.forName(activity.getClass().getCanonicalName() + "Binding");
            Constructor constructor = bindingClass.getDeclaredConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
