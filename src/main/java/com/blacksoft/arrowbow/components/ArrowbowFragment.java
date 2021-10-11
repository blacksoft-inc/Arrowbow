package com.blacksoft.arrowbow.components;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.blacksoft.arrowbow.R;
import com.blacksoft.arrowbow.utils.FragmentUtils;
import com.blacksoft.arrowbow.utils.InputUtils;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this Fragment class is to hold utils that can be used by user, to show some windows
 * or to do some other work without having to write too much code
 */
public class ArrowbowFragment extends Fragment implements ArrowbowScreen {

    /**
     * different kind of windows that can be used
     */
    private static AlertDialog progressWindow, resultsWindow, takeActionWindow;
    /**
     * this view is used as a back navigation button to the previous inflated fragment.
     */
    private View backNavigationView;
    /**
     * this view is used to close the system keyboard when clicked.
     */
    private View closeKeyboardView;

    @Override
    public void onPause() {
        InputUtils.closeKeyboard(getView());
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        InputUtils.closeKeyboard(getView());
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        /**
         * releasing resources
         */
        progressWindow = null;
        resultsWindow = null;
        takeActionWindow = null;
        this.closeKeyboardView = null;
        this.backNavigationView = null;
        super.onDestroy();
    }

    public final View getBackNavigationView() {
        return backNavigationView;
    }

    /**
     * {@link ArrowbowScreen} methods
     */


    @Override
    public final void setBackNavigationView(@Nullable View backNavigationView) {
        this.backNavigationView = backNavigationView;
        if (this.backNavigationView != null) {
            this.backNavigationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentUtils.backNavigation(getActivity().getSupportFragmentManager());
                }
            });
        }
    }

    @Override
    public final View getCloseKeyboardView() {
        return closeKeyboardView;
    }

    @Override
    public final void setCloseKeyboardView(@Nullable View closeKeyboardView) {
        this.closeKeyboardView = closeKeyboardView;
        if (this.closeKeyboardView != null) {
            this.closeKeyboardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputUtils.closeKeyboard(v);
                }
            });
        }
    }

    @Override
    public final AlertDialog showProgressWindow(@NonNull String progressMessage,
                                                boolean cancelable) {
        if (progressWindow != null) progressWindow.dismiss();
        else progressWindow = new AlertDialog.Builder(getContext()).create();
        progressWindow.setCancelable(cancelable);

        View layout = getLayoutInflater().inflate(R.layout.window_circular_progress, null);
        TextView message = layout.findViewById(R.id.message);
        progressWindow.setView(layout);

        if (progressWindow != null && isVisible()) progressWindow.show();

        message.setText(progressMessage);
        progressWindow.getWindow().getDecorView().setBackground(null);
        return progressWindow;
    }


    @Override
    public final void hideProgressWindow() {
        if (progressWindow != null) progressWindow.cancel();
    }

    @Override
    @Nullable
    public final TextView getProgressWindowTextView() {
        if (progressWindow != null) return progressWindow.findViewById(R.id.message);
        return null;
    }

    @Override
    @Nullable
    public final ProgressBar getProgress() {
        if (progressWindow != null) return progressWindow.findViewById(R.id.progress);
        return null;
    }

    @Override
    public final AlertDialog showResultsWindow(@NonNull String resultsButtonText,
                                               @NonNull String message,
                                               int resultType) {
        /**
         * if we don't create a new window each time it'll keep the same image
         */
        resultsWindow = ((new AlertDialog.Builder(getContext()))).create();
        resultsWindow.setCancelable(false);

        if (progressWindow != null) progressWindow.dismiss();
        if (resultsWindow != null) resultsWindow.dismiss();

        View layout = getLayoutInflater().inflate(R.layout.window_results_dialog, null);
        resultsWindow.setView(layout);
        if (resultsWindow != null && isVisible()) resultsWindow.show();

        resultsWindow.getWindow().getDecorView().setBackground(null);
        //
        TextView messageView = resultsWindow.findViewById(R.id.message);
        messageView.setText(message);
        //
        TextView button = resultsWindow.findViewById(R.id.next);
        button.setText(resultsButtonText);
        //
        ImageView icon = layout.findViewById(R.id.image);
        switch (resultType) {
            case RESULT_WARNING:
                icon.setImageResource(R.drawable.warning);
                break;

            case RESULT_ERROR:
                icon.setImageResource(R.drawable.error);
                break;


            case RESULT_DONE:
                icon.setImageResource(R.drawable.done);
                break;

        }
        //
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResultsWindowButtonClicked(resultType);
                if (resultsWindow != null) resultsWindow.dismiss();
            }
        });

        return resultsWindow;
    }

    @Override
    public final void hideResultsWindow() {
        if (resultsWindow != null) resultsWindow.cancel();
    }


    @Override
    public final AlertDialog showActionWindow(@Nullable String positiveButtonText,
                                              @NonNull String negativeButtonText,
                                              @NonNull String message,
                                              int actionImgResourceId,
                                              @Nullable Object... params) {
        if (takeActionWindow != null) takeActionWindow.dismiss();
        else takeActionWindow = new AlertDialog.Builder(getContext()).create();

        if (progressWindow != null) progressWindow.dismiss();
        if (resultsWindow != null) resultsWindow.dismiss();

        View layout = getLayoutInflater().inflate(R.layout.window_action, null);
        takeActionWindow.setView(layout);
        takeActionWindow.getWindow().getDecorView().setBackground(null);
        takeActionWindow.show();
        //
        ImageView image = takeActionWindow.findViewById(R.id.image);
        try {
            Drawable drawable = getResources().getDrawable(actionImgResourceId);
            image.setImageDrawable(drawable);

        } catch (Resources.NotFoundException e) {
            image.setImageResource(R.drawable.warning);
        }
        //
        TextView messageView = takeActionWindow.findViewById(R.id.message);
        messageView.setText(message);
        //
        TextView positive = takeActionWindow.findViewById(R.id.positive_action);
        positive.setText(positiveButtonText);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserTookAction(true, params);
                takeActionWindow.dismiss();
            }
        });
        //
        TextView negative = takeActionWindow.findViewById(R.id.negative_action);
        negative.setText(negativeButtonText);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserTookAction(false, params);
                takeActionWindow.dismiss();
            }
        });

        return takeActionWindow;
    }

    @Override
    public void onResultsWindowButtonClicked(int resultsType) {
    }


    @Override
    public void onUserTookAction(boolean positiveAction, Object params) {
    }

}