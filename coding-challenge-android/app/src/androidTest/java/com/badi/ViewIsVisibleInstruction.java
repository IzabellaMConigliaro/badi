package com.badi;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.view.View;

import com.azimolabs.conditionwatcher.Instruction;

public class ViewIsVisibleInstruction extends Instruction {

    private final int id;

    public ViewIsVisibleInstruction(@IdRes int id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return "View Should be visible";
    }

    @Override
    public boolean checkCondition() {
        Activity activity = ((TestApplication)
                InstrumentationRegistry.getTargetContext().getApplicationContext()).getCurrentActivity();

        try {
            if (activity != null) {
                View view = activity.findViewById(id);
                if (view != null && view.getVisibility() == View.VISIBLE) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(ViewIsVisibleInstruction.class.getSimpleName(), e.getMessage(), e);
        }

        return false;
    }
}
