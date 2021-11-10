package com.blacksoft.arrowbow.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blacksoft.arrowbow.storage_manager.FileType;
import com.blacksoft.arrowbow.storage_manager.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * This class holds Images in the same hashmap to facilitate their management.
 */
public abstract class MemoryUtils {
    private static HashMap<String, Bitmap> allAppImages;

    /**
     * private constructor to prevent users from creating an instance from this class
     */
    private MemoryUtils() {
    }

    /**
     * Initialize the images map
     */
    private static final void initializeImagesList() {
        if (allAppImages == null) allAppImages = new HashMap<>();
    }

    /**
     * @return a HashMap of bitmaps with string keys
     */
    public static final HashMap<String, Bitmap> getAllAppImages() {
        initializeImagesList();
        return allAppImages;
    }

    /**
     * Loads bitmap in ram
     *
     * @param id:     image id
     * @param bitmap:
     */
    public static final void addImageToRam(@NonNull String id,
                                           @NonNull Bitmap bitmap) {
        initializeImagesList();
        allAppImages.put(id, bitmap);
    }

    /**
     * gets bitmap from ram if it exists
     *
     * @param id: image id
     */
    public static final Bitmap getImageFromRam(@NonNull String id) {
        if (allAppImages != null)
            return allAppImages.get(id);
        else return null;
    }

    /**
     * removes bitmap from ram if it exists
     *
     * @param id: image id
     */
    public static final void removeImageFromRam(@NonNull String id) {
        if (allAppImages != null)
            allAppImages.remove(id);
    }


    /**
     * tells if the input id has a bitmap stored in ram
     *
     * @param id: image id
     */
    public static final boolean imageInRam(@Nullable String id) {
        if (allAppImages == null || id == null) return false;
        return allAppImages.containsKey(id);
    }


    /**
     * Delete images from Disk
     *
     * @param imagesPaths: images you want to delete
     */
    public static final void deleteImagesFromStorage(@Nullable String[] imagesPaths) {
        if (imagesPaths != null)
            for (int i = 0; i < imagesPaths.length; i++) {
                String current = imagesPaths[i];
                if (StorageUtils.isStoredLocally(current))
                    new File(current).delete();

            }
    }

    /**
     * Delete images from Disk
     *
     * @param imagesPaths: images you want to delete
     */
    public static final void deleteImagesFromStorage(@Nullable ArrayList<String> imagesPaths) {
        if (imagesPaths != null)
            for (int i = 0; i < imagesPaths.size(); i++) {
                String current = imagesPaths.get(i);
                if (StorageUtils.isStoredLocally(current))
                    new File(current).delete();
            }
    }

    /**
     * Delete images from Disk
     *
     * @param imagesPaths: images you want to delete from ram
     */
    public static final void removeImagesFromRam(@Nullable ArrayList<String> imagesPaths) {
        if (imagesPaths != null)
            for (int i = 0; i < imagesPaths.size(); i++) {
                String current = imagesPaths.get(i);
                removeImageFromRam(current);
            }
    }

    /**
     * clears all app data
     *
     * @param context: preferably application context
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static final void clearApplicationData(@NonNull Context context) {
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).
                clearApplicationUserData();
    }

    /**
     * @return the size of cached files in bytes
     */
    public static final long getCachedDataSize(@NonNull Context context) {
        File file = context.getApplicationContext().getCacheDir();
        long size = 0;
        for (File f : file.listFiles()) {
            if (f.isFile()) size += f.length();
            else {
                for (File f2 : f.listFiles()) {
                    if (f2.isFile()) size += f2.length();
                }
            }
        }

        return size;
    }

    /**
     * @param id:image id
     * @return bitmap size in bytes
     */
    public static final long getImageSize(@NonNull String id) {
        Bitmap bitmap = getImageFromRam(id);
        if (bitmap != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return bitmap.getAllocationByteCount();
            } else return bitmap.getByteCount();
        }
        return 0;
    }

    /**
     * @return all stored bitmaps size in bytes
     */
    public static final long getAllImagesSize() {
        long size = 0;
        if (allAppImages != null)
            for (String id : allAppImages.keySet()) {
                size += getImageSize(id);
            }
        return size;
    }


}