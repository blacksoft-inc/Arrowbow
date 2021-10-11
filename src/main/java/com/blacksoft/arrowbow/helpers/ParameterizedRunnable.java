package com.blacksoft.arrowbow.helpers;

import androidx.annotation.Nullable;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this class is to pass some parameters to a runnable instance, a good example is
 * after caching a file you need to pass the new path to user to do some work on it.
 */
public class ParameterizedRunnable implements Runnable {
    private Object[] params;

    /**
     * @param params: parameters you want to pass the the runnable.
     */
    public ParameterizedRunnable(@Nullable Object... params) {
        this.params = params;
    }

    /**
     * Code you want to run after getting your params
     *
     * @param params:parameters you want to pass the the runnable.
     */
    protected void run(@Nullable Object... params) {
    }


    @Override
    public final void run() {
        run(params);
    }

    /**
     * getting params
     */
    @Nullable
    public Object[] getParams() {
        return params;
    }

    /**
     * setting params
     */
    public void setParams(@Nullable Object... params) {
        this.params = params;
    }
}