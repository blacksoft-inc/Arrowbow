package com.blacksoft.arrowbow.items;

import androidx.annotation.NonNull;

import com.blacksoft.arrowbow.storage_manager.StorageUtils;

import java.io.File;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * This class holds Media files that will be used by you
 */
public class Media {
    private final String path;
    private String cachedPath = null;
    private int mediaResourceId = -1;


    /**
     * constructor for medias accessed by path
     *
     * @param path: the full path of the file you want to access
     */
    public Media(String path) {
        this.path = path;
    }

    /**
     * constructor for medias accessed by resourceId
     *
     * @param resourceId: file you want to access
     */
    public Media(int resourceId) {
        this(null);
        this.mediaResourceId = resourceId;

    }

    /**
     * guessing the file type from his name
     */
    public short guessMediaType() {
        return StorageUtils.guessFileType(this.path);
    }

    /**
     * determines if the media is stored on this device or this is a web path
     */
    public boolean isStoredLocally() {
        if (path == null && cachedPath == null) return false;
        File file = new File(path);
        File cachedFile = new File(cachedPath);
        return (file.exists() && !file.isDirectory())
                || (cachedFile.exists() && !cachedFile.isDirectory());
    }

    /**
     * returns the media resource id
     */
    public int getMediaResourceId() {
        return mediaResourceId;
    }

    /**
     * returns the media path
     */
    public String getPath() {
        return path;
    }

    /**
     * returns the media path
     */
    public String getCachedPath() {
        return cachedPath;
    }

    /**
     * set the media cached path
     */
    public void setCachedPath(String cachedPath) {
        this.cachedPath = cachedPath;
    }

    /**
     *
     */

    @NonNull
    @Override
    public String toString() {
        return "\npath: " + path;
    }


}

