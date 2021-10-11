package com.blacksoft.arrowbow.networking;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this class is to add to Http requests the required fields and their values
 */

public class RequestHeader {
    /**
     * Properties are a list of {@link com.blacksoft.arrowbow.items.Field} objects.
     */
    private final ArrayList<com.blacksoft.arrowbow.items.Field> properties;

    /**
     * Constructor
     */
    public RequestHeader(com.blacksoft.arrowbow.items.Field... requestProperties) {
        properties = new ArrayList<>();
        addProperties(requestProperties);
    }

    /**
     * @param requestProperties: Properties are a list of {@link com.blacksoft.arrowbow.items.Field} objects.
     *                           example: Content-length: 1024;
     */
    public void addProperties(@NonNull com.blacksoft.arrowbow.items.Field... requestProperties) {
        for (com.blacksoft.arrowbow.items.Field r : requestProperties) {
            properties.add(r);
        }
    }

    /**
     * @return: a list of Properties, are a list of {@link com.blacksoft.arrowbow.items.Field} objects.
     * example: Content-length: 1024;
     */
    public ArrayList<com.blacksoft.arrowbow.items.Field> getProperties() {
        return properties;
    }

    /**
     * @return: a Map of of Properties, are a list of {@link com.blacksoft.arrowbow.items.Field} objects.
     * example: Content-length: 1024;
     */
    public Map<String, String> getPropertiesMap() {
        Map<String, String> propertiesA = new HashMap<>();
        for (int i = 0; i < this.properties.size(); i++) {
            com.blacksoft.arrowbow.items.Field field = properties.get(i);
            propertiesA.put(field.getName(), field.getValue());
        }
        return propertiesA;
    }

    /**
     * Possible Request header fields
     */
    public interface Field {
        String ACCEPT = "Accept";
        String ACCEPT_CHARSET = "Accept-Charset";
        String ACCEPT_ENCODING = "Accept-Encoding";
        String ACCEPT_LANGUAGE = "Accept-Language";
        String AUTHORIZATION = "Authorization";
        String BOUNDARY = "boundary";
        String CACHE_CONTROL = "Cache-Control";
        String CONTENT_ENCODING = "Content-Encoding";
        String CONTENT_LANGUAGE = "Content-Language";
        String CONTENT_LENGTH = "Content-Length";
        String CONTENT_LOCATION = "Content-Location";
        String CONTENT_TYPE = "Content-Type";
        String COOKIE = "Cookie";
        String DATE = "Date";
        String ETAG = "ETag";
        String EXPIRES = "Expires";
        String HOST = "Host";
        String IF_MATCH = "If-Match";
        String IF_MODIFIED_SINCE = "If-Modified-Since";
        String IF_NONE_MATCH = "If-None-Match";
        String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
        String LAST_MODIFIED = "Last-Modified";
        String LOCATION = "Location";
        String SET_COOKIE = "Set-Cookie";
        String USER_AGENT = "User-Agent";
        String VARY = "Vary";
        String WWW_AUTHENTICATE = "WWW-Authenticate";
    }

    /**
     * Possible Data-types for {@link Field}
     */
    public interface DataType {
        String APPLICATION_ATOM_XML = "application/atom+xml";
        String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
        String APPLICATION_JSON = "application/json";
        String APPLICATION_OCTET_STREAM = "application/octet-stream";
        String APPLICATION_SVG_XML = "application/svg+xml";
        String APPLICATION_XHTML_XML = "application/xhtml+xml";
        String APPLICATION_XML = "application/xml";
        String MULTIPART_FORM_DATA = "multipart/form-data";
        String MEDIA_TYPE_WILDCARD = "*";
        String WILDCARD = "*/*";
        String ZIP = "application/zip";
        String JAR = "application/java-archive";
        String RAR = "application/x-rar-compressed";
        String TAR = "application/x-tar";
        String XLS = "application/vnd.ms-excel";
        String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        //
        String TEXT_HTML = "text/html";
        String TEXT_PLAIN = "text/plain";
        String TEXT_XML = "text/xml";
        String TEXT_CSS = "text/css";
        String TEXT_CSV = "text/csv";
        String TEXT_ICS = "text/calendar";
        //
        String IMAGE_APNG = "image/apng";
        String IMAGE_AVIF = "image/avif";
        String IMAGE_GIF = "image/gif";
        String IMAGE_JPEG = "image/jpeg";
        String IMAGE_PNG = "image/png";
        String IMAGE_SVG = "image/svg+xml";
        String IMAGE_WEBP = "image/webp";
        String IMAGE_BITMAP = "image/bmp";
        String IMAGE_IEF = "image/ief";
        String IMAGE_PIPEG = "image/pipeg";
        String IMAGE_TIFF = "image/tiff";
        String IMAGE_ICON = "image/x-icon";
        //
        String AUDIO_FLAC = "audio/flac";
        String AUDIO_M3U = "audio/mpegurl";
        String AUDIO_M4B = "audio/mp4";
        String AUDIO_MP3 = "audio/mpeg";
        String AUDIO_OGG = "audio/ogg";
        String AUDIO_PLS = "audio/x-scpls";
        String AUDIO_WAV = "audio/wav";
        String AUDIO_AAC = "audio/aac";
        String AUDIO_WEBM = "audio/webm";
        String AUDIO_WMA = "audio/x-ms-wma";
        String AUDIO_XSPF = "application/xspf+xml";
        String AUDIO_AU = "audio/basic";
        String AUDIO_MID = "audio/mid";
        String AUDIO_3G2 = "audio/3gpp";
        //
        String VIDEO_FLV = "video/x-flv";
        String VIDEO_MP4 = "video/mp4";
        String VIDEO_3GP = "video/3gpp";
        String VIDEO_MOV = "video/quicktime";
        String VIDEO_AVI = "video/x-msvideo";
        String VIDEO_WMV = "video/x-ms-wmv";
        String VIDEO_MPEG = "video/mpeg";
        String VIDEO_OGV = "video/ogg";
        String VIDEO_WEBM = "video/webm";

    }

}