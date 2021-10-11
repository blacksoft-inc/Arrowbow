package com.blacksoft.arrowbow.networking;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * This class represents the response we get when we perform an a http request
 * the response can be a text, file or java objects ext...
 */
public class Response {
    private Map<String, List<String>> headers;
    private String contentType, contentEncoding, requestMethod;
    private URL url;
    private int responseCode, connectTimeout, readTimeout;
    private long contentLength = 0;
    private Object result;


    public Response(@Nullable HttpURLConnection httpURLConnection, @Nullable String result) {
        setHttpURLConnection(httpURLConnection);
        this.result = result;

    }


    /**
     * Returns the all response headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Returns the list of values of this header
     *
     * @param name: the header key for example (Content-type)
     */
    public List<String> getHeaderAllValues(@NonNull String name) {
        return headers.get(name);
    }


    /**
     * used to get a header with a single value
     *
     * @param name: the header key for example (Content-type)
     */
    public String getHeaderFirstValue(@NonNull String name) {
        return headers.get(name).get(0);
    }

    /**
     * Returns the Response got from the server, a String containing jsonObject, a file path
     * or Error message.
     */
    @Nullable
    public Object getResult() {
        return result;
    }

    /**
     * Getters for headers
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    public long getContentLength() {
        return contentLength;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getContentType() {
        return contentType;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public URL getUrl() {
        return url;
    }

    public Response setResult(Object result) {
        this.result = result;
        return this;
    }

    /**
     * Sets the http url connection
     */
    public void setHttpURLConnection(@Nullable HttpURLConnection httpURLConnection) {
        try {
            headers = new HashMap<>();
            if (httpURLConnection != null) {
                contentType = httpURLConnection.getContentType();
                contentEncoding = httpURLConnection.getContentEncoding();
                requestMethod = httpURLConnection.getRequestMethod();
                connectTimeout = httpURLConnection.getConnectTimeout();
                url = httpURLConnection.getURL();
                responseCode = httpURLConnection.getResponseCode();
                connectTimeout = httpURLConnection.getConnectTimeout();
                readTimeout = httpURLConnection.getReadTimeout();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentLength = httpURLConnection.getContentLengthLong();
                } else {
                    contentLength = httpURLConnection.getContentLength();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Arrowbow-Response.class", "" + e.getMessage());
        }
    }
}