package com.badi;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;
import android.test.mock.MockApplication;

import com.badi.TestApplication;


public class MockJUnitRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        return newApplication(TestApplication.class, context);
    }
}