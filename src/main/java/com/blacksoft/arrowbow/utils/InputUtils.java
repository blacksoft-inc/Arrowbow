package com.blacksoft.arrowbow.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this calls is to hold some methods that helps us to manage Keyboard and inputs.
 */
public class InputUtils {

    /**
     * closes the system keyboard.
     *
     * @param view: any non null view.
     */
    public static final void closeKeyboard(@NonNull View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
