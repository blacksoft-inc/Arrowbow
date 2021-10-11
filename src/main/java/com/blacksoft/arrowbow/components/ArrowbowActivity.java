package com.blacksoft.arrowbow.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.blacksoft.arrowbow.R;
import com.blacksoft.arrowbow.utils.InputUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this class is to hold most needed app settings like app language, theme,
 * user info ext...
 */
public class ArrowbowActivity extends FragmentActivity implements ArrowbowScreen {
    /**
     * App themes
     */
    public static final String APPLICATION_SETTINGS_THEME = "appTheme";
    /**
     * App other settings
     */
    public static final String APPLICATION_SETTINGS_LANGUAGE = "appLang";
    public static final String APPLICATION_SETTINGS_USER_INFO = "user_info";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    /**
     * params values
     */
    private static String appLanguage;
    private static int appTheme;
    private static JSONObject userInfo;
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
    /**
     * this var is used to determine if an activity is visible to the user or not
     */
    private boolean isVisible;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeSharedPrefs(getApplicationContext());
        loadAppSettingsBeforeLoadingLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    /**
     * Attaching new language to the new configuration
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadAppLanguage(getApplicationContext(), appLanguage);
    }

    /**
     * Attaching new language configuration to our new context
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        initializeSharedPrefs(newBase);
        if (sharedPreferences.contains(APPLICATION_SETTINGS_LANGUAGE)) {
            appLanguage = sharedPreferences.
                    getString(APPLICATION_SETTINGS_LANGUAGE, "en");
            super.attachBaseContext(loadAppLanguage(newBase, appLanguage));
        }
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences = null;
        editor = null;
        appLanguage = null;
        userInfo = null;
    }

    /**
     * Stores the new language code in shared preferences
     *
     * @param languageCode: 2 chars code for example (United states: us)
     */
    public final void changeAppLanguage(@NonNull String languageCode) {
        editor.putString(APPLICATION_SETTINGS_LANGUAGE, languageCode);
        editor.commit();
        appLanguage = languageCode;
    }

    /**
     * Stores the new language code in shared preferences and restarts the app
     *
     * @param languageCode: 2 chars code for example (United states: us)
     */
    public final void changeAppLanguageAndRestartApp(@NonNull String languageCode) {
        changeAppLanguage(languageCode);
        recreate();
    }

    /**
     * Stores the new language code in shared preferences and restarts the app
     *
     * @param languageCode: 2 chars code for example (United states: us)
     */
    private final Context loadAppLanguage(@NonNull Context context,
                                          @NonNull String languageCode) {
        /**
         * setting the language of the activity in case of old api is missing
         */
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    /**
     * changes the app theme and restarts the app
     *
     * @param newThemeRes: one of the values APPLICATION_LIGHT_THEME, APPLICATION_DARK_THEME
     */
    public final void changeAppTheme(int newThemeRes) {
        initializeSharedPrefs(getApplicationContext());
        editor.putInt(APPLICATION_SETTINGS_THEME, newThemeRes);
        editor.commit();
        appTheme = newThemeRes;
        finish();
        startActivity(getIntent());

    }

    /**
     * Saves user needed information as a json object
     * for example
     * {
     * "firstName":"Muhammad",
     * "username":"goodman",
     * "password":"1sd74f5z54538"
     * "profile_picture_path":"path"
     * }
     *
     * @param userInfo: json object holding user information fields
     */
    private final void saveUserInfo(@Nullable JSONObject userInfo) {
        initializeSharedPrefs(getApplicationContext());
        if (userInfo != null) {
            editor.putString(APPLICATION_SETTINGS_USER_INFO, userInfo.toString());
            editor.commit();
            ArrowbowActivity.userInfo = userInfo;
        }
    }

    /**
     * @return user info Json Object if found, else null
     */
    private final JSONObject getUserInfo() {
        initializeSharedPrefs(getApplicationContext());
        JSONObject info = null;

        try {
            info = new JSONObject(sharedPreferences.
                    getString(APPLICATION_SETTINGS_USER_INFO, null));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Arrowbow_library", e.getMessage());
        }
        userInfo = info;
        return info;
    }

    /**
     * @return SharedPreferences object used to read settings.
     */
    public final SharedPreferences getSettingsPrefs() {
        initializeSharedPrefs(getApplicationContext());
        return sharedPreferences;
    }

    /**
     * @return SharedPreferences.Editor object used to save settings
     */
    public final SharedPreferences.Editor getSettingsEditor() {
        initializeSharedPrefs(getApplicationContext());
        return editor;
    }

    /**
     * Initializes shared prefs, and editor to be used
     */
    private final void initializeSharedPrefs(@NonNull Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("arrowbow_preferences", MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    /**
     * loads app settings before show activity view to the user
     * <p>
     * useful to change the theme, and load app needed settings to prepare the layout before
     * getting exposed.
     * <p>
     * it will be called in onCreate.
     * <p>
     * can be overridden
     */
    protected void loadAppSettingsBeforeLoadingLayout() {
        if (sharedPreferences.contains(APPLICATION_SETTINGS_THEME)) {
            appTheme = sharedPreferences.getInt(APPLICATION_SETTINGS_THEME, 0);
            setTheme(appTheme);
        }
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
                    onBackPressed();
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
        else progressWindow = new AlertDialog.Builder(this).create();
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
        resultsWindow = ((new AlertDialog.Builder(this))).create();
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
        else takeActionWindow = new AlertDialog.Builder(this).create();

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

    public boolean isVisible() {
        return isVisible;
    }

}