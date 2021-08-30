package com.dev.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyAptAPI {
    @SuppressWarnings("all")
    public static void init() {
        try {
            Class kClass = Class.forName("com.dev.HelloWorld");
            Constructor declaredConstructor = kClass.getDeclaredConstructor();
            Object o = declaredConstructor.newInstance();
            Method test = kClass.getDeclaredMethod("test", String.class);
            test.invoke(o, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
