package com.blacksoft.arrowbow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * This class is used to show notifications to the user.
 */
public class NotificationUtils {

    /**
     * private constructor to prevent users from creating an instance from this class
     */
    private NotificationUtils() {
    }


    /**
     * shows a notification to the user
     *
     * @param context:        context from where you want to show the notification
     * @param notificationId: notification integer id and it has to be unique
     * @param smallIcRes:     small icon
     * @param dismissible:    if this notification can be canceled by the user or not
     * @param largeIcon:      large icon
     * @param channelId:      unique string
     * @param title:          notification title
     * @param content:        notification description (content)
     * @param actions:        action buttons with their icons and titles
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final NotificationCompat.Builder showNotification(@NonNull Context context,
                                                                    int notificationId,
                                                                    int smallIcRes,
                                                                    boolean dismissible,
                                                                    @Nullable Bitmap largeIcon,
                                                                    @NonNull String channelId,
                                                                    @NonNull String title,
                                                                    @NonNull String content,
                                                                    @Nullable NotificationCompat.Action... actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcRes)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing((!dismissible))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon);
        }

        if (actions != null) {
            for (NotificationCompat.Action action : actions) {
                if (action != null) {
                    builder.addAction(action);
                }
            }
        }

        NotificationManagerCompat
                notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
        return builder;
    }

    /**
     * shows a big image notification to the user
     *
     * @param context:        context from where you want to show the notifcation
     * @param notificationId: notification integer id and it has to be unique
     * @param smallIcRes:     small icon
     * @param bigImage:       bigImage
     * @param dismissible:    if this notification can be canceled by the user or not
     * @param channelId:      unique string
     * @param title:          notification title
     * @param content:        notification description (content)
     * @param actions:        action buttons with their icons and titles
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final NotificationCompat.Builder showBigImageNotification(@NonNull Context context,
                                                                            int notificationId,
                                                                            int smallIcRes,
                                                                            boolean dismissible,
                                                                            @Nullable Bitmap largeIcon,
                                                                            @NonNull Bitmap bigImage,
                                                                            @NonNull String channelId,
                                                                            @NonNull String title,
                                                                            @NonNull String content,
                                                                            @Nullable NotificationCompat.Action... actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcRes)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing((!dismissible))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigImage))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon);
        }

        if (actions != null) {
            for (NotificationCompat.Action action : actions) {
                if (action != null) {
                    builder.addAction(action);
                }
            }
        }

        NotificationManagerCompat
                notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

        return builder;
    }

    /**
     * shows a notification to the user
     *
     * @param context:        context from where you want to show the notifcation
     * @param notificationId: notification integer id and it has to be unique
     * @param smallIcRes:     small icon
     * @param dismissible:    if this notification can be canceled by the user or not
     * @param bigImage:       bigImage
     * @param channelId:      unique string
     * @param title:          notification title
     * @param content:        notification description (content)
     * @param actions:        action buttons with their icons and titles
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final NotificationCompat.Builder showBigExpandableImageNotification(@NonNull Context context,
                                                                                      int notificationId,
                                                                                      int smallIcRes,
                                                                                      boolean dismissible,
                                                                                      @NonNull Bitmap bigImage,
                                                                                      @NonNull String channelId,
                                                                                      @NonNull String title,
                                                                                      @NonNull String content,
                                                                                      @Nullable NotificationCompat.Action... actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcRes)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(bigImage)
                .setOngoing((!dismissible))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigImage)
                        .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        if (actions != null) {
            for (NotificationCompat.Action action : actions) {
                if (action != null) {
                    builder.addAction(action);
                }
            }
        }

        NotificationManagerCompat
                notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

        return builder;
    }

    /**
     * shows a big text notification to the user
     *
     * @param context:        context from where you want to show the notifcation
     * @param notificationId: notification integer id and it has to be unique
     * @param smallIcRes:     small icon
     * @param dismissible:    if this notification can be canceled by the user or not
     * @param largeIcon:      large icon
     * @param channelId:      unique string
     * @param title:          notification title
     * @param content:        notification description (content)
     * @param bigText:        text that will be exposed after the notification has expanded
     * @param actions:        action buttons with their icons and titles
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final NotificationCompat.Builder showBigExpandableTextNotification(@NonNull Context context,
                                                                                     int notificationId,
                                                                                     int smallIcRes,
                                                                                     boolean dismissible,
                                                                                     @Nullable Bitmap largeIcon,
                                                                                     @NonNull String channelId,
                                                                                     @NonNull String title,
                                                                                     @NonNull String content,
                                                                                     @NonNull String bigText, @Nullable NotificationCompat.Action... actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcRes)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing((!dismissible))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon);
        }

        if (actions != null) {
            for (NotificationCompat.Action action : actions) {
                if (action != null) {
                    builder.addAction(action);
                }
            }
        }

        NotificationManagerCompat
                notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

        return builder;
    }

    /**
     * shows a notification, that exposes its actions even if the screen is locked
     * like for a media player notification
     *
     * @param context:        context from where you want to show the notifcation
     * @param notificationId: notification integer id and it has to be unique
     * @param smallIcRes:     small icon
     * @param dismissible:    if this notification can be canceled by the user or not
     * @param largeIcon:      large icon
     * @param channelId:      unique string
     * @param title:          notification title
     * @param content:        notification description (content)
     * @param actions:        action buttons with their icons and titles
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final NotificationCompat.Builder showActionsExposedNotification(@NonNull Context context,
                                                                                  int notificationId,
                                                                                  int smallIcRes,
                                                                                  boolean dismissible,
                                                                                  @Nullable Bitmap largeIcon,
                                                                                  @NonNull String channelId,
                                                                                  @NonNull String title,
                                                                                  @NonNull String content,
                                                                                  @Nullable NotificationCompat.Action... actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcRes)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing((!dismissible))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon);
        }

        if (actions != null) {
            for (NotificationCompat.Action action : actions) {
                if (action != null) {
                    builder.addAction(action);
                }
            }
        }

        NotificationManagerCompat
                notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

        return builder;
    }

    /**
     * shows a progressBar notification to the user
     *
     * @param context:         context from where you want to show the notifcation
     * @param notificationId:  notification integer id and it has to be unique
     * @param smallIcRes:      small icon
     * @param dismissible:     if this notification can be canceled by the user or not
     * @param channelId:       unique string
     * @param title:           notification title
     * @param content:         notification description (content)
     * @param actions:         action buttons with their icons and titles
     * @param initialProgress: initial progress out of 100
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final NotificationCompat.Builder showProgressNotification(@NonNull Context context,
                                                                            int notificationId,
                                                                            int smallIcRes,
                                                                            boolean dismissible,
                                                                            @NonNull String channelId,
                                                                            @NonNull String title,
                                                                            @NonNull String content,
                                                                            int initialProgress,
                                                                            @Nullable NotificationCompat.Action... actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcRes)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setOngoing((!dismissible))
                .setProgress(100, initialProgress, false)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        if (actions != null) {
            for (NotificationCompat.Action action : actions) {
                if (action != null) {
                    builder.addAction(action);
                }
            }
        }

        NotificationManagerCompat
                notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

        return builder;
    }

    /**
     * updates the progress notification
     *
     * @param builder:        {@link NotificationCompat.Builder}
     * @param context:        context from where you want to show the notifcation
     * @param notificationId: notification integer id and it has to be unique
     * @param title:          notification title
     * @param content:        notification description (content)
     * @param progress:       Progress bar progress out of 100
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final void updateProgressNotification(@Nullable Context context,
                                                        @Nullable NotificationCompat.Builder builder,
                                                        @Nullable String title,
                                                        @Nullable String content,
                                                        int notificationId,
                                                        int progress) {
        if (context != null && builder != null) {
            if (title != null) {
                builder.setContentTitle(title);
            }
            if (content != null) {
                builder.setContentText(content);
            }
            builder.setProgress(100, progress, false);
            NotificationManagerCompat.from(context).notify(notificationId, builder.build());
        }
    }

    /**
     * shows a notification to the user
     *
     * @param context:        context from where you want to show the notifcation
     * @param notificationId: notification integer id and it has to be unique
     * @param dismissible:    if this notification can be canceled by the user or not
     * @param smallIcRes:     small icon
     * @param channelId:      unique string
     * @param actions:        action buttons with their icons and titles
     * @param customView:     the custom layout for your notification
     * @return NotificationCompat.Builder in case you want to customize or update your notification.
     */
    public static final NotificationCompat.Builder showCustomNotification(@NonNull Context context,
                                                                          int notificationId,
                                                                          int smallIcRes,
                                                                          boolean dismissible,
                                                                          @NonNull RemoteViews customView,
                                                                          @NonNull String channelId,
                                                                          @Nullable NotificationCompat.Action... actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcRes)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing((!dismissible))
                .setCustomContentView(customView);


        if (actions != null) {
            for (NotificationCompat.Action action : actions) {
                if (action != null) {
                    builder.addAction(action);
                }
            }
        }

        NotificationManagerCompat
                notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
        return builder;
    }


}