package com.blacksoft.arrowbow.storage_manager;


import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The point of this class is to store files in a structured way and
 * return the path of the stored file (better put in an other thread to not put the main thread
 * under pressure)
 **/

public class FileSaver implements StorageConfig, FileType {
    private static Object instance;

    /**
     * Empty constructor, only if you need to show progress
     */
    public FileSaver() {
        instance = this;
    }

    /**
     * Copying inputStream bytes into a new File
     *
     * @param filePathAndName: complete file name with its path.
     * @Returns true if it's a successful operation else false
     */

    public final boolean copyPaste(String filePathAndName, InputStream inputStream) {
        try {

            FileOutputStream outputStream = new FileOutputStream(filePathAndName);
            //
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[8192]; /* reading by 8kb (perfect buffer size) */
            long numberOfReadBytes = 0;
            int size;
            while ((size = bufferedInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, size);
                numberOfReadBytes += size;
                ((FileSaver) instance).showProgress(numberOfReadBytes);
            }
            outputStream.flush();
            outputStream.close();
            bufferedInputStream.close();
            inputStream.close();
            //
            outputStream = null;
            bufferedInputStream = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) inputStream = null;
        }
        return true;
    }

    /**
     * Saves input stream bytes into a named file.
     *
     * @param path:        directory path where you want to store your file.
     * @param fileName:    file name with extension.
     * @param inputStream: the input stream u want to read from
     */

    public final String save(String path, String fileName, InputStream inputStream) {

        if (inputStream == null) return null;

        //creating parent directories if not existing before
        File dataDir = new File(path);
        dataDir.mkdirs();

        String fileCompleteName = dataDir.getPath() + File.separator + fileName;
        if (copyPaste(fileCompleteName, inputStream)) return fileCompleteName;
        else return null;

    }

    /**
     * Saves file content bytes a new file
     *
     * @param file:     your file
     * @param path:     new path where you want to store your file
     * @param fileName: file name with extension
     */

    public final String save(String path, String fileName, File file) {


        if (file == null || !file.isFile()) return null;

        /**
         * creating parent directories if not existing before
         */
        File dataDir = new File(path);
        dataDir.mkdirs();

        try {
            String fileCompleteName = dataDir.getPath() + File.separator + fileName;
            if (copyPaste(fileCompleteName, new FileInputStream(file)))
                return fileCompleteName;

        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Saves file content bytes a new file
     *
     * @param file: your file
     * @Param filePrefix: in case you want to distinguish your file from others
     * example: (yesterday_1002440_244... .xls) yesterday is the prefix
     */

    public final String saveInCache(Context context, String filePrefix, File file) {

        if (file == null || !file.isFile() || context == null) return null;

        /**
         * creating parent directories if not existing before
         */
        String parentDirPath = context.getApplicationContext().
                getCacheDir().getPath() + File.separator;

        /**
         * determining which folder we want to store our file in
         */
        short fileType = StorageUtils.guessFileType(file.getPath());

        if (fileType == MICROSOFT_POWERPOINT ||
                fileType == MICROSOFT_EXCEL ||
                fileType == MICROSOFT_WORD)
            parentDirPath += MICROSOFT_OFFICE_FOLDER;

        else if (fileType == HTML ||
                fileType == CSS
        ) parentDirPath += WEB_FILES_FOLDER;


        else switch (fileType) {
                case IMAGE:
                    parentDirPath += IMAGES_FOLDER;
                    break;

                case AUDIO:
                    parentDirPath += AUDIOS_FOLDER;
                    break;

                case VIDEO:
                    parentDirPath += VIDEOS_FOLDER;
                    break;

                case PDF:
                    parentDirPath += PDF_FOLDER;
                    break;

                case SQL_DATABASE:
                    parentDirPath += SQL_DATABASES_FOLDER;
                    break;

                case TEXT:
                    parentDirPath += TEXT_FILES_FOLDER;
                    break;


                default:
                    parentDirPath += OTHER_FILES_FOLDER;
                    break;

            }

        String fileCompleteName = parentDirPath + StorageUtils.randomName(filePrefix, fileType)
                + StorageUtils.getFileExtension(file.getPath());
        File dataDir = new File(parentDirPath);
        dataDir.mkdirs();

        try {
            if (copyPaste(fileCompleteName, new FileInputStream(file)))
                return fileCompleteName;

        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Saves file content bytes a new file
     *
     * @param file: your file
     */
    public final String saveInCache(Context context, File file) {
        return saveInCache(context, LIBRARY_NAME, file);
    }

    /**
     * Shows progress of the file saving operation.
     * This is not an abstract method, in case you don't want to know the progress.
     *
     * @param numberOfReadBytes: number of bytes read till now.
     */
    public void showProgress(long numberOfReadBytes) {
    }


}