package com.blacksoft.arrowbow.storage_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blacksoft.arrowbow.networking.RequestHeader;

import java.io.File;
import java.util.Date;
import java.util.Random;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * This class holds basic utils related to files
 */

public final class StorageUtils {

    /**
     * Giving files random names based on some inputs
     *
     * @param prefix:   file name prefix
     * @param fileType: {@link FileType}
     */
    public static final String randomName(@Nullable String prefix,
                                          short fileType) {
        if (prefix == null || prefix.isEmpty()) prefix = "not_prefixed";

        String typePrefix = "other_files";

        if (fileType == FileType.HTML || fileType == FileType.CSS)
            typePrefix = "web_file";

        else if (fileType == FileType.MICROSOFT_EXCEL || fileType == FileType.MICROSOFT_WORD
                || fileType == FileType.MICROSOFT_POWERPOINT)
            typePrefix = "microsoft_office";

        else if (fileType == FileType.SQL_DATABASE) typePrefix = "database";

        else if (fileType == FileType.ANDROID_APPLICATION) typePrefix = "android_application";

        else if (fileType == FileType.JAR) typePrefix = "jar";

        else
        switch (fileType) {
            case FileType.IMAGE:
                typePrefix = "picture";
                break;

            case FileType.VIDEO:
                typePrefix = "movie";
                break;

            case FileType.AUDIO:
                typePrefix = "audio";
                break;

            case FileType.PDF:
                typePrefix = "pdf";
                break;

            case FileType.TEXT:
                typePrefix = "text";
                break;

            case FileType.XML:
                typePrefix = "xml";
                break;

        }

        //
        long time = new Date().getTime();
        Random random = new Random();


        return (prefix + "_" + typePrefix + "_" + time + "_" +
                random.nextLong() + "_" + (random.nextLong() + "_" +
                time * random.nextInt() + "_" + random.nextLong()));
    }

    /**
     * Giving files random names based on teh mimeType
     *
     * @param prefix:   file name prefix
     * @param mimeType: http header value (Content-type) for ex: image/png...
     */
    public static final String randomNameWithExtension(@Nullable String prefix,
                                                       @NonNull String mimeType) {
        short fileType = StorageUtils.guessFileType(mimeType);
        return randomName(prefix, fileType)
                + StorageUtils.guessFileExtension(mimeType);

    }

    /**
     * Giving file random name based on default configs like app name
     *
     * @param fileType: {@link FileType}
     */

    public static final String defaultRandomName(short fileType) {
        return randomName(StorageConfig.LIBRARY_NAME, fileType);
    }


    /**
     * Guessing the file type from its name or from a string containing its type
     *
     * @param path: file path
     * @return short integer file type
     */
    public static final short guessFileType(@Nullable String path) {

        if (path == null || path.isEmpty()) return FileType.NOT_A_FILE;

        String lCPath = path.trim().toLowerCase();

        if (lCPath.contains("image/") || lCPath.contains(".webp") || lCPath.contains(".png")
                || lCPath.contains(".jpg") || lCPath.contains(".jpeg")
                || lCPath.contains(".gif") || lCPath.contains(".bmp")
                || lCPath.contains(".gifv") || lCPath.contains(".apng")
                || lCPath.contains(".avif") || lCPath.contains(".jfif")
                || lCPath.contains(".pjpeg") || lCPath.contains(".pjp")
                || lCPath.contains(".svg") || lCPath.contains(".ico")
                || lCPath.contains(".cur") || lCPath.contains(".tif")
        ) return FileType.IMAGE;

        else if (lCPath.contains("video/") || lCPath.contains(".mp4") || lCPath.contains(".mpg")
                || lCPath.contains(".mpeg") || lCPath.contains(".3gp")
                || lCPath.contains(".mkv") || lCPath.contains(".webm")
                || lCPath.contains(".flv") || lCPath.contains(".vob")
                || lCPath.contains(".ogv") || lCPath.contains(".ovv")
                || lCPath.contains(".drc") || lCPath.contains(".f4b")
                || lCPath.contains(".mnv") || lCPath.contains(".avi")
                || lCPath.contains("ts") || lCPath.contains(".mov")
                || lCPath.contains(".qt") || lCPath.contains(".wmv")
                || lCPath.contains(".yuv")
                || lCPath.contains(".viv") || lCPath.contains(".asf")
                || lCPath.contains(".amv")
                || lCPath.contains(".m4v") || lCPath.contains(".svi")
                || lCPath.contains(".3g2") || lCPath.contains(".mxf")
                || lCPath.contains(".roq") || lCPath.contains(".nsv")
                || lCPath.contains(".f4v") || lCPath.contains(".f4p")
                || lCPath.contains(".f4a")
        ) return FileType.VIDEO;


        else if (lCPath.contains("audio/") || lCPath.contains(".aa") || lCPath.contains(".act")
                || lCPath.contains(".aiff") || lCPath.contains(".alac")
                || lCPath.contains(".ape") || lCPath.contains(".amr")
                || lCPath.contains(".au") || lCPath.contains(".awb")
                || lCPath.contains(".dss") || lCPath.contains(".dvf")
                || lCPath.contains(".flac") || lCPath.contains(".gsm")
                || lCPath.contains(".iklax") || lCPath.contains(".ivs")
                || lCPath.contains(".m4a") || lCPath.contains(".m4b")
                || lCPath.contains(".m4p") || lCPath.contains(".mmf")
                || lCPath.contains(".mp3") || lCPath.contains(".mpc")
                || lCPath.contains(".msv") || lCPath.contains(".nmf")
                || lCPath.contains(".ogg") || lCPath.contains(".oga")
                || lCPath.contains(".mogg") || lCPath.contains(".opus")
                || lCPath.contains(".org") || lCPath.contains(".ra")
                || lCPath.contains(".rm") || lCPath.contains(".raw")
                || lCPath.contains(".rf64") || lCPath.contains(".sln")
                || lCPath.contains(".tta") || lCPath.contains(".voc")
                || lCPath.contains(".vox") || lCPath.contains(".wav")
                || lCPath.contains(".wma") || lCPath.contains(".wv")
                || lCPath.contains(".8svx") || lCPath.contains(".cda")

        ) return FileType.AUDIO;

        else if (lCPath.contains(".txt")) return FileType.TEXT;

        else if (lCPath.contains(".xls") || lCPath.contains(".xlt")
                || lCPath.contains(".xla") || lCPath.contains(".xlsx")
                || lCPath.contains(".xlsm")
                || lCPath.contains(".xltx") || lCPath.contains(".xltm")
                || lCPath.contains(".xlam")
        ) return FileType.MICROSOFT_EXCEL;

        else if (lCPath.contains(".doc") || lCPath.contains(".dot")
                || lCPath.contains(".wbk")
                || lCPath.contains(".docx") || lCPath.contains(".docm")
                || lCPath.contains(".dotx") || lCPath.contains(".dotm")
                || lCPath.contains(".docb")
        ) return FileType.MICROSOFT_WORD;

        else if (lCPath.contains(".ppt")) return FileType.MICROSOFT_POWERPOINT;

        else if (lCPath.contains(".pdf")) return FileType.PDF;

        else if (lCPath.contains(".htm")) return FileType.HTML;

        else if (lCPath.contains(".css")) return FileType.CSS;

        else if (lCPath.contains(".xml")) return FileType.XML;

        else if (lCPath.contains(".exe")) return FileType.WINDOWS_EXECUTABLE;

        else if (lCPath.contains(".lib")) return FileType.WINDOWS_EXTERNAL_LIBRARY;

        else if (lCPath.contains(".db") || lCPath.contains(".mdf")
                || lCPath.contains(".sdf")
        ) return FileType.SQL_DATABASE;

        else if (lCPath.equals("application/vnd.android.package-archive")
                || lCPath.contains(".apk")
                || lCPath.contains(".aab"))
            return FileType.ANDROID_APPLICATION;

        else if (lCPath.equals(RequestHeader.DataType.JAR_FILE)
                || lCPath.contains(".jar")) return FileType.JAR;

        else return FileType.OTHERS;
    }

    /**
     * guessing file type from it's name but the input here is a file
     *
     * @param file: your file
     */
    public static final short guessFileType(@Nullable File file) {
        String path;
        if (file == null || !file.exists()) path = null;
        else path = file.getName();
        return guessFileType(path);
    }

    /**
     * getting file extension from its name or its path
     */
    public static final String getFileExtension(@Nullable String fileName) {
        if (fileName == null || fileName.isEmpty()
                || !fileName.contains(".")) return "";

        String[] parts = fileName.replace(".", "##").split("##");
        return "." + parts[parts.length - 1];
    }

    /**
     * @param mimeType: a string containing the mime type of the file downloaded
     *                  from server for example image/jpeg
     * @return: String containing the file extension for example image/jpeg gives you (.jpg),
     * or an empty String in case mime is null or empty, or even (.bin) if the mime type is unknown.
     */
    public static final String guessFileExtension(@Nullable String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) return ".bin";
        else {
            String lcMime = mimeType.toLowerCase().trim();
            String extension = "";

            /**
             *For Image Files
             */
            if (lcMime.contains(RequestHeader.DataType.IMAGE_APNG)) extension = ".apng";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_AVIF)) extension = ".avif";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_GIF)) extension = ".gif";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_JPEG)) extension = ".jpg";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_PNG)) extension = ".png";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_SVG)) extension = ".svg";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_WEBP)) extension = ".webp";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_BITMAP)) extension = ".bmp";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_IEF)) extension = ".ief";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_PIPEG)) extension = ".pipeg";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_TIFF)) extension = ".tiff";
            else if (lcMime.contains(RequestHeader.DataType.IMAGE_ICON)) extension = ".ico";

            /**
             *For Audio Files
             */
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_FLAC)) extension = ".flac";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_M3U)) extension = ".m3u";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_M4B)) extension = ".m4b";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_MP3)) extension = ".mp3";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_OGG)) extension = ".ogg";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_PLS)) extension = ".pls";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_WAV)) extension = ".wav";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_AAC)) extension = ".aac";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_WEBM)) extension = ".webm";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_WMA)) extension = ".wma";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_AU)) extension = ".au";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_MID)) extension = ".mid";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_3G2)) extension = ".3g2";
            else if (lcMime.contains(RequestHeader.DataType.AUDIO_XSPF)) extension = ".xspf";


            /**
             *For Video Files
             */
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_FLV)) extension = ".flv";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_MP4)) extension = ".mp4";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_3GP)) extension = ".3gp";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_MOV)) extension = ".mov";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_AVI)) extension = ".avi";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_WMV)) extension = ".wmv";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_MPEG)) extension = ".mpeg";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_OGV)) extension = ".ogv";
            else if (lcMime.contains(RequestHeader.DataType.VIDEO_WEBM)) extension = ".webm";

            /**
             * for Android app
             */
            else if (lcMime.contains(RequestHeader.DataType.APPLICATION_ANDROID))
                extension = ".apk";

            /**
             *For Others
             */
            else if (lcMime.contains(RequestHeader.DataType.APPLICATION_XML)) extension = ".xml";
            else if (lcMime.contains(RequestHeader.DataType.ZIP)) extension = ".zip";
            else if (lcMime.contains(RequestHeader.DataType.JAR_FILE)) extension = ".jar";
            else if (lcMime.contains(RequestHeader.DataType.RAR)) extension = ".rar";
            else if (lcMime.contains(RequestHeader.DataType.TAR)) extension = ".tar";
            else if (lcMime.contains(RequestHeader.DataType.XLS)) extension = ".xls";
            else if (lcMime.contains(RequestHeader.DataType.XLSX)) extension = ".xlsx";
            else if (lcMime.contains(RequestHeader.DataType.TEXT_HTML)) extension = ".html";
            else if (lcMime.contains(RequestHeader.DataType.TEXT_CSS)) extension = ".css";
            else if (lcMime.contains(RequestHeader.DataType.TEXT_ICS)) extension = ".ics";
            else if (lcMime.contains(RequestHeader.DataType.TEXT_CSV)) extension = ".csv";
            else if (lcMime.contains(RequestHeader.DataType.TEXT_XML)) extension = ".xml";
            else if (lcMime.contains(RequestHeader.DataType.TEXT_PLAIN)) extension = ".txt";
            else if (lcMime.contains(RequestHeader.DataType.APPLICATION_JSON)) extension = ".json";
            else if (lcMime.contains(RequestHeader.DataType.APPLICATION_OCTET_STREAM))
                extension = ".bin";
            else if (lcMime.contains(RequestHeader.DataType.APPLICATION_XHTML_XML))
                extension = ".xhtml";


            else extension = ".bin";


            return extension;
        }
    }

    /**
     * Tells if this file is stored locally on this device or not
     */
    public static boolean isStoredLocally(@Nullable String filePath) {
        if (filePath == null || filePath.isEmpty()) return false;
        File file = new File(filePath);
        return (file.exists() && !file.isDirectory());
    }

}