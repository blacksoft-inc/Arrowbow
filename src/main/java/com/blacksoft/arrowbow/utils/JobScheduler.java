package com.blacksoft.arrowbow.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.NonNull;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * This class is used to launch Services and BroadCast Receivers.
 */
public class JobScheduler {

    /**
     * private constructor to prevent users from creating an instance from this class
     */
    private JobScheduler() {
    }

    /**
     * starts a repeating job using alarm manager & broadcast receiver
     * the alarm is set to run every x minutes
     *
     * @param context:             context used to launch the repeating job
     * @param broadCastReceiver:   {@link android.content.BroadcastReceiver} inheriting class
     * @param requestCode:         used as an id to the pendingIntent.
     * @param startTimeInMinutes:  Time when the job starts, for example after 20 min from now.
     * @param repeatTimeInMinutes: Time when the job will be repeated again for example 60
     *                             means it will get repeated every 60 min after startTimeInMinutes.
     * @return boolean: true if the job is launched successfully false if not
     */
    public static final boolean startRepeatingBroadCast(@NonNull Context context,
                                                        @NonNull Class broadCastReceiver,
                                                        int requestCode,
                                                        int startTimeInMinutes,
                                                        int repeatTimeInMinutes) {
        AlarmManager alarmManager = (AlarmManager) context.
                getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null && broadCastReceiver != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                    new Intent(context, broadCastReceiver), PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                    1000 * 60 * repeatTimeInMinutes, pendingIntent);

            return true;
        }
        return false;

    }

    /**
     * starts a repeating job using alarm manager & Service
     * the alarm is set to run every x minutes
     *
     * @param context:             context used to launch the repeating job
     * @param service:             {@link Service} inheriting class
     * @param requestCode:         used as an id to the pendingIntent.
     * @param startTimeInMinutes:  Time when the job starts, for example after 20 min from now.
     * @param repeatTimeInMinutes: Time when the job will be repeated again for example 60
     *                             means it will get repeated every 60 min after startTimeInMinutes.
     * @param inForeground:        whether the service should be running in foreground or background,
     *                             foreground services won't be started except on aps running api >= 26
     * @return boolean: true if the job is launched successfully false if not
     */
    public static final boolean startRepeatingService(@NonNull Context context,
                                                      @NonNull Class service,
                                                      int requestCode,
                                                      int startTimeInMinutes,
                                                      int repeatTimeInMinutes,
                                                      boolean inForeground
    ) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null && service != null) {
            PendingIntent pendingIntent;
            if (inForeground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pendingIntent = PendingIntent.getForegroundService(context, requestCode,
                        new Intent(context, service), PendingIntent.FLAG_CANCEL_CURRENT);

            } else {
                pendingIntent = PendingIntent.getService(context, requestCode,
                        new Intent(context, service), PendingIntent.FLAG_CANCEL_CURRENT);
            }


            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                    1000 * 60 * repeatTimeInMinutes, pendingIntent);

            return true;
        }
        return false;
    }

    /**
     * starts a one time job using alarm manager & broadcast receiver
     *
     * @param context:            context used to launch the repeating job
     * @param broadCastReceiver:  {@link BroadcastReceiver} inheriting class
     * @param requestCode:        used as an id to the pendingIntent.
     * @param startTimeInMinutes: Time when the job starts, for example after 20 min from now.
     * @return boolean: true if the job is launched successfully false if not
     */
    public static final boolean startOneTimeReceiver(@NonNull Context context,
                                                     @NonNull Class broadCastReceiver,
                                                     int requestCode,
                                                     int startTimeInMinutes) {
        AlarmManager alarmManager = (AlarmManager) context.
                getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null && broadCastReceiver != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                    new Intent(context, broadCastReceiver), PendingIntent.FLAG_CANCEL_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME,
                        (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                        pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME,
                        (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                        pendingIntent);
            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                        (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                        pendingIntent);
            }

            return true;
        }
        return false;
    }

    /**
     * starts a one time job using alarm manager & broadcast receiver
     *
     * @param context:            context used to launch the repeating job
     * @param service:            {@link Service} inheriting class
     * @param requestCode:        used as an id to the pendingIntent.
     * @param startTimeInMinutes: Time when the job starts, for example after 20 min from now.
     * @return boolean: true if the job is launched successfully false if not
     */
    public static final boolean startOneTimeService(@NonNull Context context,
                                                    @NonNull Class service,
                                                    int requestCode,
                                                    int startTimeInMinutes,
                                                    boolean inForeground) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null && service != null) {
            PendingIntent pendingIntent;
            if (inForeground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pendingIntent = PendingIntent.getForegroundService(context, requestCode,
                        new Intent(context, service), PendingIntent.FLAG_CANCEL_CURRENT);

            } else {
                pendingIntent = PendingIntent.getService(context, requestCode,
                        new Intent(context, service), PendingIntent.FLAG_CANCEL_CURRENT);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME,
                        (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                        pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME,
                        (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                        pendingIntent);

            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                        (SystemClock.elapsedRealtime() + 1000 * 60 * startTimeInMinutes),
                        pendingIntent);
            }

            return true;
        }
        return false;
    }

    /**
     * Stops running service
     * <p>
     *
     * @param context: context used to launch the repeating job
     * @param service: {@link Service} inheriting class
     */
    public static final void stopService(@NonNull Context context,
                                         @NonNull Class service) {

        context.stopService(new Intent(context, service));
    }


}