package com.blacksoft.arrowbow.components;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * @author AbdelWadoud Rasmi
 * The goal of this interface is to hold the basic tools used in a screen
 * ({@link androidx.fragment.app.Fragment} or {@link android.app.Activity})
 */
public interface ArrowbowScreen {

    /**
     * flags used
     */
    int RESULT_WARNING = -1;
    int RESULT_ERROR = -10;
    int RESULT_DONE = 255;

    View getBackNavigationView();

    /**
     * back navigation
     */
    void setBackNavigationView(@Nullable View backNavigationView);

    View getCloseKeyboardView();

    /**
     * input management
     */
    void setCloseKeyboardView(@Nullable View closeKeyboardView);

    /**
     * shows a circular progress window with a message
     *
     * @param progressMessage: the message you want to show to the user
     * @param cancelable:      if the window can be canceled by clicking anywhere on the screen.
     */
    AlertDialog showProgressWindow(@NonNull String progressMessage,
                                   boolean cancelable);

    /**
     * Makes progress window disappear
     */
    void hideProgressWindow();

    /**
     * @return progress window message {@link TextView}.
     */
    @Nullable
    TextView getProgressWindowTextView();

    /**
     * @return progress window {@link ProgressBar}.
     */
    @Nullable
    ProgressBar getProgress();

    /**
     * shows a window with some sort of results, with the obligation of clicking
     *
     * @param resultsButtonText: the text that should be written in the button.
     * @param message:           Description of the results.
     * @param resultType:        one of the following values {RESULT_WARNING, RESULT_ERROR, RESULT_DONE},
     *                           mainly used to display a drawable in the window, and passed as a parameter to onResultsWindowButtonClicked
     *                           to let you decide what to do after you've shown your results to the user.
     */
    AlertDialog showResultsWindow(@NonNull String resultsButtonText,
                                  @NonNull String message,
                                  int resultType);

    /**
     * Makes results window disappear
     */
    void hideResultsWindow();

    /**
     * Shows a take action window, it's generally used when you need the user to accept or refuse something,
     * since it contains 2 buttons (one is for negative answer, the other one for positive answer)
     * example: yes, no
     *
     * @param positiveButtonText: text that should be written in the positive action button
     * @param negativeButtonText: text that should be written in the negative action button
     * @param message:            the message or the question you want to user to answer
     * @param params:             parameters you want to pass to onUserTookAction
     */
    AlertDialog showActionWindow(@Nullable String positiveButtonText,
                                 @NonNull String negativeButtonText,
                                 @NonNull String message,
                                 int actionImgResourceId,
                                 @Nullable Object... params);


    /**
     * Called after the user clicks on the button from the resultsWindow
     *
     * @param resultsType: one of the following values {RESULT_ERROR, RESULT_WARNING, RESULT_DONE}
     */
    void onResultsWindowButtonClicked(int resultsType);

    /**
     * Called after the user clicks on the button from the resultsWindow
     *
     * @param positiveAction: if the user has clicked the positive or negative action, if true then it's positive
     *                        else it's negative.
     * @param params:         params passed by you when you showed the window, usually you need them to deal with
     *                        them depending on the answer you got from the user.
     */
    void onUserTookAction(boolean positiveAction, Object params);
}
