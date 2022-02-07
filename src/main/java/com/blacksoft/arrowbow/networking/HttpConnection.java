package com.blacksoft.arrowbow.networking;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blacksoft.arrowbow.items.Field;
import com.blacksoft.arrowbow.storage_manager.FileSaver;
import com.blacksoft.arrowbow.storage_manager.StorageUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this class is to perform some time limited network connections on all android versions,
 * without needing to specify much details,
 * <p>
 * It doesn't throw any Exception but returns Integer Flags that you can compare with FLAGS.
 *
 * <p>
 * It uses the File saver class to store files.
 * <p>
 * Any class wants to use this class fuctions has to override its methods, which are a bunch of clabacks performed
 * every time an event occurs to help you to determine which stage of the operation you will be working on.
 */

public class HttpConnection {

    /**
     * the possible states of a network operation
     */

    public final static int FLAG_WRONG_URL = -3;
    public final static int FLAG_ERROR_MESSAGE = -2;
    public final static int FLAG_REQUEST_UNACCEPTED = -1;
    //
    public final static int FLAG_REQUEST_ACCEPTED = 0;
    //
    public final static int FLAG_RESPONSE_IS_READY = 1;
    //
    public final static int FLAG_DATA_SENT = 5;
    /**
     * possible data types to be sent or downloaded from the server.
     */


    public static final int DATATYPE_TEXT = 0;
    public static final int DATATYPE_FILE = 3;
    public static final int DATATYPE_MULTI_PART_DATA_FORM = 7;
    public static final int DATATYPE_INPUT_STREAM = 10;
    public static final int DATATYPE_MODEL = 12;

    /**
     * Thread priority, when it has to compete with other threads to perform some work on the system resources.
     */

    public static final int PRIORITY_LOWEST = Process.THREAD_PRIORITY_BACKGROUND;
    public static final int PRIORITY_NORMAL = Process.THREAD_PRIORITY_DEFAULT;
    public static final int PRIORITY_HIGHEST = Process.THREAD_PRIORITY_FOREGROUND;
    /**
     * Rest Connection Methods
     */
    private static final String CONNECTION_METHOD_GET = "GET";
    private static final String CONNECTION_METHOD_POST = "POST";
    private static final String CONNECTION_METHOD_DELETE = "DELETE";
    private static final String CONNECTION_METHOD_PUT = "PUT";
    private static final String CONNECTION_METHOD_PATCH = "PATCH";
    /**
     * used to post multipart data form, it separates fields
     */
    private static String boundary;
    /**
     * Url we want to be connected to
     */
    private String urlStr;
    /**
     * Whether the thread is in foreground or in background by default it's in foreground
     */
    private int threadPriority = PRIORITY_HIGHEST;
    /**
     * Data type you want to send or download.
     */
    private int dataType;

    /**
     * Whether the http.OK (200) will be returned by the server or an other one
     */
    private int successfulResponseCode = HttpURLConnection.HTTP_OK;

    /**
     * Connection timeout in millis
     */
    private int connectionTimeout = 3000;

    /**
     * Read timeout
     */
    private int readTimeout = 3000;

    /**
     * Only one constructor
     */
    public HttpConnection() {
        boundary = (boundary == null) ? UUID.randomUUID().toString() : boundary;
    }


    /**
     * Shows progress of the download.
     * This is not an abstract method, in case the user wants to Send data not to download it.
     * this method will be executed in app main thread
     *
     * @param percentage:        Progress of the download operation out of 100.
     * @param downloadSize:      Size of the file you want to download in bytes.
     * @param numberOfReadBytes: number of bytes read till now.
     */
    public void showDownloadProgress(short percentage, long downloadSize, long numberOfReadBytes) {

    }

    /**
     * Notify Main-thread of the new progress of the download.
     *
     * @param percentage:        Progress of the download operation out of 100.
     * @param downloadSize:      Size of the file you want to download in bytes.
     * @param numberOfReadBytes: number of bytes read till now.
     */
    private final void informUserWithProgress(short percentage, long downloadSize, long numberOfReadBytes) {
        /**
         * posting the code to be executed in the ui thread.
         */
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        showDownloadProgress(percentage, downloadSize, numberOfReadBytes);
                    }
                });

    }


    /**
     * Informs user with the new occurring event and giving him its result,
     * so he can perform some work on the Ui thread like showing a toast or displaying an image etc...
     *
     * @param flag:     the current state of the operation.
     * @param response: the result of the current stage of the operation.
     */
    protected void doInUiThread(int flag, @Nullable Response response) {
    }


    /**
     * Informs user with the new occurring event and giving him its result,
     * so he can perform some work on the Background thread to put less work on app main thread.
     *
     * @param flag:     the current state of the operation.
     * @param response: the result of the current stage of the operation.
     */
    protected void doInBackgroundThread(int flag, @Nullable Response response) {
    }


    /**
     * Informs user with the new occurring event and giving him its result.
     * <p>
     * it calls doInBackgroundThread and doInUiThread.
     *
     * @param flag:     the current state of the operation.
     * @param response: the result of the current stage of the operation.
     */
    private final void informUserWithNewFlag(final int flag, @Nullable final Response response) {
        /**
         * do exhaustive work in the background.
         */
        doInBackgroundThread(flag, response);
        /**
         * posting the code to be executed in the ui thread.
         */
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        doInUiThread(flag, response);
                    }
                });

    }

    /**
     * creates a url Object from a string path.
     */
    private final URL createUrl() {
        URL urlObj;
        try {
            urlObj = new URL(urlStr);
        } catch (MalformedURLException e) {
            if (e != null) {
                e.printStackTrace();
                Log.e("Arrowbow_library", e.getMessage() + "\n\nCouldn't create a url:\n"
                        + "-Check if the url is correct (misspelling)");
            }

            return null;
        }

        return urlObj;
    }

    /**
     * Prepares an {@link HttpURLConnection} to be used to communicate with the server.
     *
     * @param url:              Url object
     * @param connectionMethod: one of the possible connections methods you find in this class.
     * @param header:           the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                          its own request property
     *                          {@link Field}.
     */
    private final HttpURLConnection connectToServer(@NonNull URL url,
                                                    @NonNull String connectionMethod,
                                                    @Nullable RequestHeader header) {
        HttpURLConnection connection;

        try {

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(connectionMethod);
            /**
             * the connection will always be able to read from the server.
             */
            connection.setDoInput(true);

            /**
             * Different text responses your app can read from server.
             */
//            connection.addRequestProperty(RequestHeader.Field.ACCEPT,
//                    RequestHeader.DataType.APPLICATION_JSON);
//
//            connection.addRequestProperty(RequestHeader.Field.ACCEPT,
//                    RequestHeader.DataType.APPLICATION_XML);
//
//            connection.addRequestProperty(RequestHeader.Field.ACCEPT,
//                    RequestHeader.DataType.TEXT_PLAIN);
//
//            connection.addRequestProperty(RequestHeader.Field.ACCEPT,
//                    RequestHeader.DataType.TEXT_HTML);
//
//            connection.addRequestProperty(RequestHeader.Field.ACCEPT,
//                    RequestHeader.DataType.TEXT_XML);


            /**
             *in case you want to send something to the server.
             */
            if (!connectionMethod.equals(CONNECTION_METHOD_GET)) connection.setDoOutput(true);

            /**
             * Filling connection object with header fields.
             */
            if (header != null && header.getProperties() != null)
                for (Field field : header.getProperties()) {
                    connection.addRequestProperty(field.getName(), field.getValue());
                }

            if (dataType == DATATYPE_TEXT) {
                /**
                 * Different text formats that can be sent across the network.
                 */
//                connection.addRequestProperty(RequestHeader.Field.CONTENT_TYPE,
//                        RequestHeader.DataType.APPLICATION_JSON + "; charset=UTF-8");
//
//                connection.addRequestProperty(RequestHeader.Field.CONTENT_TYPE,
//                        RequestHeader.DataType.APPLICATION_XML + "; charset=UTF-8");
//
//                connection.addRequestProperty(RequestHeader.Field.CONTENT_TYPE,
//                        RequestHeader.DataType.TEXT_PLAIN + "; charset=UTF-8");
//
//                connection.addRequestProperty(RequestHeader.Field.CONTENT_TYPE,
//                        RequestHeader.DataType.TEXT_HTML + "; charset=UTF-8");
//
//                connection.addRequestProperty(RequestHeader.Field.CONTENT_TYPE,
//                        RequestHeader.DataType.TEXT_XML + "; charset=UTF-8");

                /**
                 * You cant's surpass 15 seconds to send or read text.
                 */

                connection.setConnectTimeout(connectionTimeout);

            } else if (dataType == DATATYPE_MULTI_PART_DATA_FORM) {
                connection.setRequestProperty(RequestHeader.Field.CONTENT_TYPE,
                        RequestHeader.DataType.MULTIPART_FORM_DATA + "; " +
                                RequestHeader.Field.BOUNDARY + "= --" + boundary);
            }

            /**
             * in case of a delete request
             */
            if (connectionMethod.equals(CONNECTION_METHOD_DELETE)) {

                connection.connect();
            }


        } catch (IOException e) {
            if (e != null) {
                e.printStackTrace();
                Log.e("Arrowbow_library", e.getMessage());
            }
            connection = null;
        }

        return connection;
    }

    /**
     * Download methods
     */


    /**
     * Downloads Text files such as JSON XML HTML and PLAIN_TEXT.
     * <p>
     * Result in callback methods will be a String equal to the returned value from server.
     *
     * @param url:           url you want to get your data from.
     * @param requestHeader: the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                       its own request property
     *                       {@link Field}.
     */
    public final HttpConnection getText(@Nullable String url,
                                        @Nullable RequestHeader requestHeader) {
        dataType = DATATYPE_TEXT;
        this.urlStr = url;
        readFromServer(requestHeader, null);
        return this;
    }


    /**
     * Downloads Files and put them.
     * <p>
     * Result in callback methods will be a String equal to the returned value from server.
     *
     * @param url:              url you want to get your data from.
     * @param requestHeader:    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                          its own request property
     *                          {@link Field}.
     * @param storageDirectory: where you want to store your file.
     */
    public final HttpConnection getFile(@Nullable String url,
                                        @Nullable RequestHeader requestHeader,
                                        @NonNull String storageDirectory) {
        dataType = DATATYPE_FILE;
        this.urlStr = url;
        readFromServer(requestHeader, storageDirectory);
        return this;
    }

    /**
     * Exposes an input stream to be read from.
     * <p>
     * Result in callback methods will be a String equal to the returned value from server.
     *
     * @param url:           url you want to get your data from.
     * @param requestHeader: the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                       its own request property
     *                       {@link Field}.
     */
    public final HttpConnection getInputStream(@Nullable String url,
                                               @Nullable RequestHeader requestHeader) {
        this.dataType = DATATYPE_INPUT_STREAM;
        this.urlStr = url;
        readFromServer(requestHeader, null);
        return this;
    }

    /**
     * Reads data from server
     *
     * @param requestHeader:    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                          its own request property
     *                          {@link Field}.
     * @param storageDirectory: Where to store your file.
     */
    private final void readFromServer(@Nullable final RequestHeader requestHeader,
                                      @Nullable String storageDirectory) {

        Thread readingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = new Response(null, "");
                Process.setThreadPriority(threadPriority);
                /**
                 * creates an {@link HttpURLConnection} and fill it with parameters
                 */
                URL url = createUrl();
                if (url == null) {
                    /**
                     * Notify User with new occurring event
                     */

                    informUserWithNewFlag(FLAG_WRONG_URL, response.setResult("There something wrong about this Url !"));
                    return;
                }
                HttpURLConnection connection = connectToServer(url, CONNECTION_METHOD_GET, requestHeader);
                InputStream inputStream = null;
                if (connection == null) {
                    /**
                     * Notify User with new occurring event
                     */
                    informUserWithNewFlag(FLAG_ERROR_MESSAGE, response.setResult("We couldn't even connect to this Url !"));
                    return;
                } else {

                    try {
                        // preparing response
                        response.setHttpURLConnection(connection);

                        /**
                         * Case request rejected
                         */
                        if (connection.getResponseCode() != successfulResponseCode) {
                            /**
                             * Reading Error message and returning it to the user
                             */
                            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), Charset.forName("UTF-8")));
                            long downloadSize = connection.getContentLength();
                            String errorMessage = buildTextFromBuffer(reader, downloadSize) + " Response Code: " + connection.getResponseCode();
                            /**
                             * Notify User with new occurring event
                             */
                            informUserWithNewFlag(FLAG_REQUEST_UNACCEPTED, response.setResult(errorMessage));

                            if (reader != null) reader.close();
                            if (inputStream != null) inputStream.close();
                            if (connection != null) connection.disconnect();

                            return;
                        } else {

                            /**
                             * Notify User with new occurring event
                             * and returning the content length of the file
                             */

                            /**
                             * reading stream from the server & converting it to the specified data type
                             */
                            inputStream = connection.getInputStream();
                            long downloadSize = connection.getContentLength();
                            informUserWithNewFlag(FLAG_REQUEST_ACCEPTED, response.setResult("Started download of " + downloadSize + " bytes"));

                            switch (dataType) {
                                case DATATYPE_TEXT:
                                    String result = null;
                                    try {
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                                        result = buildTextFromBuffer(reader, downloadSize);
                                        reader.close();

                                    } catch (IOException e) {
                                        if (e != null) {
                                            e.printStackTrace();
                                            Log.e("Arrowbow_library", e.getMessage());
                                        }
                                    } finally {
                                        /**
                                         * Notify User with new occurring event
                                         */
                                        if (result != null) {
                                            informUserWithNewFlag(FLAG_RESPONSE_IS_READY, response.setResult(result));
                                        } else {
                                            informUserWithNewFlag(FLAG_ERROR_MESSAGE, response.setResult("No Response from Server except it accepted your request !"));
                                        }
                                    }
                                    break;

                                case DATATYPE_FILE:
                                    String contentType = connection.getContentType();
                                    String fileName = StorageUtils
                                            .randomNameWithExtension("arrowbow_download", contentType);


                                    String path = new FileSaver() {
                                        /**
                                         *This var is declared to minimize the number of main thread calls when,
                                         * the download size is so big we might have the same progress many times.
                                         */
                                        short lastProgress = 0;

                                        @Override
                                        public void showProgress(long numberOfReadBytes) {
                                            short percentage = 0;

                                            if (downloadSize >= numberOfReadBytes)
                                                percentage = Short.valueOf(
                                                        String.valueOf(((numberOfReadBytes * 100) / downloadSize)));

                                            else percentage = 100;

                                            if (percentage > lastProgress) {
                                                lastProgress = percentage;
                                                informUserWithProgress(percentage, downloadSize, numberOfReadBytes);
                                            }


                                        }
                                    }.save(storageDirectory, fileName, inputStream);


                                    informUserWithNewFlag(FLAG_RESPONSE_IS_READY, response.setResult(path));
                                    break;

                                case DATATYPE_INPUT_STREAM:
                                    informUserWithNewFlag(FLAG_RESPONSE_IS_READY, response.setResult(inputStream));
                                    break;
                            }

                        }

                    } catch (IOException e) {
                        if (e != null) {
                            e.printStackTrace();
                            Log.e("Arrowbow_library", e.getMessage());
                        }
                        informUserWithNewFlag(FLAG_ERROR_MESSAGE, null);
                        return;

                    } finally {
                        if (connection != null) connection.disconnect();

                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                if (e != null) {
                                    e.printStackTrace();
//                                    Log.e("Arrowbow_library", e.getMessage());
                                }
                            }
                        }
                    }
                }

            }
        });
        readingThread.start();
    }

    /**
     * Sending methods
     */

    /**
     * Posts a String to the specified url.
     * <p>
     * Result in callback methods will be EvolutionFlag.DATA_SENT plus response message received from server.
     *
     * @param url:                              url you want to get your data from.
     * @param requestHeader:                    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                                          its own request property
     *                                          {@link Field}.
     * @param text:                             text you want to send.
     * @param responseDataType:                 integer to specify whether it's a text or a File
     * @param storageDirectoryIfResponseIsFile: where to put the file in case the response is a file
     */
    public final HttpConnection postText(@Nullable String url,
                                         @Nullable RequestHeader requestHeader,
                                         @Nullable String text,
                                         int responseDataType,
                                         @Nullable String storageDirectoryIfResponseIsFile) {
        this.urlStr = url;
        writeToServer(text, CONNECTION_METHOD_POST, requestHeader, responseDataType, storageDirectoryIfResponseIsFile);
        return this;
    }

    /**
     * Puts a String to the specified url.
     * <p>
     * Result in callback methods will be EvolutionFlag.DATA_SENT plus response message received from server.
     *
     * @param url:                              url you want to get your data from.
     * @param requestHeader:                    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                                          its own request property
     *                                          {@link Field}.
     * @param text:                             text you want to send.
     * @param responseDataType:                 integer to specify whether it's a text or a File
     * @param storageDirectoryIfResponseIsFile: where to put the file in case the response is a file
     */
    public final HttpConnection putText(@Nullable String url,
                                        @Nullable RequestHeader requestHeader,
                                        @Nullable String text,
                                        int responseDataType,
                                        @Nullable String storageDirectoryIfResponseIsFile) {
        this.urlStr = url;
        writeToServer(text, CONNECTION_METHOD_PUT, requestHeader, responseDataType, storageDirectoryIfResponseIsFile);
        return this;
    }

    /**
     * Patches a String to the specified url.
     * <p>
     * Result in callback methods will be EvolutionFlag.DATA_SENT plus response message received from server.
     *
     * @param url:                              url you want to get your data from.
     * @param requestHeader:                    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                                          its own request property
     *                                          {@link Field}.
     * @param text:                             text you want to send.
     * @param responseDataType:                 integer to specify whether it's a text or a File
     * @param storageDirectoryIfResponseIsFile: where to put the file in case the response is a file
     */
    public final HttpConnection patchText(@Nullable String url,
                                          @Nullable RequestHeader requestHeader,
                                          @Nullable String text,
                                          int responseDataType,
                                          @Nullable String storageDirectoryIfResponseIsFile) {
        this.urlStr = url;
        writeToServer(text, CONNECTION_METHOD_PATCH, requestHeader, responseDataType, storageDirectoryIfResponseIsFile);
        return this;
    }

    /**
     * Sends a delete request with a String to the specified url.
     * <p>
     * Result in callback methods will be EvolutionFlag.DATA_SENT plus response message received from server.
     *
     * @param url:                              url you want to get your data from.
     * @param requestHeader:                    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                                          its own request property
     *                                          {@link Field}.
     * @param text:                             text you want to send.
     * @param responseDataType:                 integer to specify whether it's a text or a File
     * @param storageDirectoryIfResponseIsFile: where to put the file in case the response is a file
     */
    public final HttpConnection deleteText(@Nullable String url,
                                           @Nullable RequestHeader requestHeader,
                                           @Nullable String text,
                                           int responseDataType,
                                           @Nullable String storageDirectoryIfResponseIsFile) {
        this.urlStr = url;
        writeToServer(text, CONNECTION_METHOD_DELETE, requestHeader, responseDataType, storageDirectoryIfResponseIsFile);
        return this;
    }

    /**
     * Posts a multi-part form to the specified url.
     * <p>
     * Result in callback methods will be EvolutionFlag.DATA_SENT plus response message received from server.
     *
     * @param url:                              url you want to get your data from.
     * @param requestHeader:                    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                                          its own request property
     *                                          {@link Field}.
     * @param fields:                           form fields (String(s) / File(s)) you want to send.
     * @param responseDataType:                 integer to specify whether it's a text or a File
     * @param storageDirectoryIfResponseIsFile: where to put the file in case the response is a file
     */
    public final void postMultiPartDataForm(@Nullable String url,
                                            @Nullable RequestHeader requestHeader,
                                            int responseDataType,
                                            @Nullable String storageDirectoryIfResponseIsFile,
                                            @Nullable Field... fields) {
        this.urlStr = url;
        this.dataType = DATATYPE_MULTI_PART_DATA_FORM;
        writeToServer(fields, CONNECTION_METHOD_POST, requestHeader, responseDataType, storageDirectoryIfResponseIsFile);
    }

    /**
     * Writes data to the specified URL.
     *
     * @param requestHeader:                    the header fields you want to pu into your http request, every {@link RequestHeader} has
     *                                          its own request property
     *                                          {@link Field}.
     * @param responseDataType:                 integer to specify whether it's a text or a File
     * @param storageDirectoryIfResponseIsFile: where to put the file in case the response is a file
     */
    private final void writeToServer(@Nullable final Object content,
                                     @NonNull final String connectionMethod,
                                     @Nullable final RequestHeader requestHeader,
                                     int responseDataType,
                                     @Nullable String storageDirectoryIfResponseIsFile) {
        Thread writingTask = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = new Response(null, "");
                Process.setThreadPriority(threadPriority);
                /** http url connection object to connect to the server and read stream from it*/
                URL url = createUrl();

                if (url == null) {
                    /**
                     * Notify User with new occurring event
                     */
                    informUserWithNewFlag(FLAG_WRONG_URL, response.setResult("There something wrong about this Url !"));
                    return;
                }


                HttpURLConnection connection = null;
                if (dataType != DATATYPE_MULTI_PART_DATA_FORM) {
                    connection = connectToServer(url, connectionMethod, requestHeader);

                    if (connection == null) {
                        /**
                         * Notify User with new occurring event
                         */
                        informUserWithNewFlag(FLAG_ERROR_MESSAGE, response.setResult("We couldn't even connect to this Url !"));
                        return;
                    }
                    response.setHttpURLConnection(connection);
                }

                OutputStream outputStream = null;

                try {

                    BufferedReader reader = null;

                    switch (dataType) {
                        case DATATYPE_TEXT:
                            String contentStr = (String) content;
                            outputStream = connection.getOutputStream();
                            BufferedOutputStream writer = new BufferedOutputStream(outputStream);
                            try {

                                /**
                                 * sending json to the server
                                 */
                                if (contentStr == null) contentStr = "";
                                writer.write(contentStr.trim().getBytes());
                                writer.flush();


                                /**
                                 * reading response from server
                                 */
                                if (connection.getResponseCode() != 200) {
                                    /**
                                     * case of error
                                     */
                                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), Charset.forName("UTF-8")));
                                    long downloadSize = connection.getContentLength();
                                    String errorMessage = buildTextFromBuffer(reader, downloadSize) + " Response Code: " + connection.getResponseCode();

                                    /**
                                     * Notify User with new occurring event
                                     */
                                    informUserWithNewFlag(FLAG_ERROR_MESSAGE, response.setResult(errorMessage));
                                    throw new IOException();
                                }


                                /**
                                 * case request accepted
                                 */
                                informUserWithNewFlag(FLAG_REQUEST_ACCEPTED, response.setResult("Request accepted !"));

                                /**
                                 * Notify User with new occurring event
                                 */
                                informUserWithNewFlag(FLAG_DATA_SENT, response.setResult("Data sent !"));

                                /**
                                 * getting response from server
                                 */

                                if (responseDataType == DATATYPE_TEXT) {
                                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
                                    long downloadSize = connection.getContentLength();
                                    informUserWithNewFlag(FLAG_RESPONSE_IS_READY, response.setResult(buildTextFromBuffer(reader, downloadSize)));

                                } else {
                                    String contentType = connection.getContentType();
                                    String fileName = StorageUtils.randomNameWithExtension("arrowbow_download", contentType);

                                    InputStream inputStream = connection.getInputStream();
                                    long downloadSize = connection.getContentLength();

                                    String path = new FileSaver() {
                                        /**
                                         *This var is declared to minimize the number of main thread calls when,
                                         * the download size is so big we might have the same progress many times.
                                         */
                                        short lastProgress = 0;

                                        @Override
                                        public void showProgress(long numberOfReadBytes) {
                                            short percentage = 0;

                                            if (downloadSize >= numberOfReadBytes)
                                                percentage = Short.valueOf(
                                                        String.valueOf(((numberOfReadBytes * 100) / downloadSize)));
                                            else percentage = 100;

                                            if (percentage > lastProgress) {
                                                lastProgress = percentage;
                                                informUserWithProgress(percentage, downloadSize, numberOfReadBytes);
                                            }

                                        }
                                    }.save(storageDirectoryIfResponseIsFile, fileName, inputStream);

                                    informUserWithNewFlag(FLAG_RESPONSE_IS_READY, response.setResult(path));
                                }


                            } catch (IOException e) {
                                if (e != null) {
                                    e.printStackTrace();
                                    Log.e("Arrowbow_library", e.getMessage());
                                }
                                /**
                                 * Notify User with new occurring event
                                 */
                                informUserWithNewFlag(FLAG_REQUEST_UNACCEPTED, null);

                            } finally {

                                if (reader != null) reader.close();
                                if (outputStream != null) outputStream.close();
                                if (writer != null) writer.close();
                            }
                            break;

                        case DATATYPE_MULTI_PART_DATA_FORM:

                            PrintWriter printWriter = null;
                            try {
                                Field[] values = ((Field[]) content);
                                HttpPostMultiPart post = new HttpPostMultiPart(urlStr, "UTF-8", requestHeader.getPropertiesMap(), responseDataType, storageDirectoryIfResponseIsFile);

                                /**
                                 * distinguish btw files and strings
                                 * */
                                for (int i = 0; i < values.length; i++) {

                                    if (values[i] != null) {

                                        File file = new File(values[i].getValue());


                                        if (file.exists()) {
                                            post.addFilePart(values[i].getName(), file);
                                        } else {
                                            post.addFormField(values[i].getName(), values[i].getValue());
                                        }
                                    }

                                }
                                post.finish();


                            } catch (IOException e) {
                                if (e != null) {
                                    e.printStackTrace();
                                    Log.e("Arrowbow_library", e.getMessage());
                                }
                                informUserWithNewFlag(FLAG_REQUEST_UNACCEPTED, null);

                            }
                            break;

                    }


                } catch (IOException e) {
                    if (e != null) {
                        e.printStackTrace();
                        Log.e("Arrowbow_library", e.getMessage());
                    }
                    informUserWithNewFlag(FLAG_REQUEST_UNACCEPTED, null);


                } finally {

                    if (connection != null) connection.disconnect();

                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (Exception e) {
                            if (e != null) {
                                e.printStackTrace();
                                Log.e("Arrowbow_library", e.getMessage());
                            }
                        }
                    }
                }

            }

        });
        writingTask.start();
    }


    /**
     * Sets url you want to connect to.
     *
     * @param url: url.
     */
    public HttpConnection setUrl(String url) {
        this.urlStr = url;
        return this;
    }

    public String getUrlStr() {
        return urlStr;
    }

    /**
     * Sets data type you want to send or receive
     *
     * @param dataType: data types can be found here
     */
    public final HttpConnection setDataType(int dataType) {
        this.dataType = dataType;
        return this;
    }

    public int getDataType() {
        return dataType;
    }

    /**
     * Setting thread priority over other threads running in the system.
     *
     * @param threadPriority: Whether the thread is in foreground or in background by default it's in foreground.
     */
    public final HttpConnection setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
        return this;
    }

    public int getThreadPriority() {
        return threadPriority;
    }

    /**
     * Whether the http.OK (200) will be returned by the server or an other one
     */
    public HttpConnection setSuccessfulResponseCode(int successfulResponseCode) {
        this.successfulResponseCode = successfulResponseCode;
        return this;
    }

    /**
     * The time before you quit trying to connect to the server, the default time is 3 seconds
     *
     * @param connectionTimeout: connectTimeOut in millis
     */
    public HttpConnection setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * the time before connecting to the server and waiting the first bytes of data to reach u,
     */
    public HttpConnection setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getSuccessfulResponseCode() {
        return successfulResponseCode;
    }

    /**
     * Creates a string from a buffered reader and returns progress.
     *
     * @param reader: {@link BufferedReader}
     * @return String read from reader.
     * @throws IOException .
     */
    private final String buildTextFromBuffer(BufferedReader reader, long downloadSize) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        long numberOfReadBytes = 0;
        short percentage = 100;
        /**
         *This var is declared to minimize the number of main thread calls when,
         * the download size is so big we might have the same progress many times.
         */
        short lastProgress = 0;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            numberOfReadBytes += line.getBytes().length;

            /**
             * checking if we have the right download size, to prevent the Short
             * out of range exception
             */
            if (downloadSize >= numberOfReadBytes)
                percentage = Short.valueOf(
                        String.valueOf(((numberOfReadBytes * 100) / downloadSize)));

            else percentage = 100;

            if (percentage > lastProgress) {
                lastProgress = percentage;
                informUserWithProgress(percentage, downloadSize, numberOfReadBytes);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * this class is used to post multipart data forms
     */
    private final class HttpPostMultiPart {
        private static final String LINE = "\r\n";
        private final String boundary;
        private final HttpURLConnection httpConn;
        private final String charset;
        private final OutputStream outputStream;
        private final PrintWriter writer;
        private final int responseDataType;
        private final Response response;
        private final String storageDirectoryIfResponseIsFile;

        /**
         * This constructor initializes a new HTTP POST request with content type
         * is set to multipart/form-data
         *
         * @param requestURL
         * @param charset
         * @param headers
         * @throws IOException
         */
        public HttpPostMultiPart(String requestURL, String charset, Map<String, String> headers, int responseDataType, String storageDirectoryIfResponseIsFile) throws IOException {
            this.charset = charset;
            boundary = UUID.randomUUID().toString();
            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            response = new Response(httpConn, "");
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true);    // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            if (headers != null && headers.size() > 0) {
                Iterator<String> it = headers.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = headers.get(key);
                    httpConn.setRequestProperty(key, value);
                }
            }
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
            this.responseDataType = responseDataType;
            this.storageDirectoryIfResponseIsFile = storageDirectoryIfResponseIsFile;
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            writer.append("--" + boundary).append(LINE);
            writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE);
            writer.append("Content-Type: text/plain; charset=" + charset).append(LINE);
            writer.append(LINE);
            writer.append(value).append(LINE);
            writer.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName
         * @param uploadFile
         * @throws IOException
         */
        public final void addFilePart(String fieldName, File uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE);
            writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE);
            writer.append("Content-Transfer-Encoding: binary").append(LINE);
            writer.append(LINE);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            writer.append(LINE);
            writer.flush();
        }

        /**
         * Completes the request and receives response from the server.
         *
         * @return String as response in case the server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public final void finish() throws IOException {

            writer.flush();
            writer.append("--" + boundary + "--").append(LINE);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == successfulResponseCode) {
                /**
                 * Notify User with new occurring event
                 */
                informUserWithNewFlag(FLAG_REQUEST_ACCEPTED, response.setResult("Request accepted !"));

                /**
                 * Notify User with new occurring event
                 */
                informUserWithNewFlag(FLAG_DATA_SENT, response.setResult("Data sent !"));

                if (responseDataType == DATATYPE_TEXT) {
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), Charset.forName("UTF-8")));

                    response.setResult(buildTextFromBuffer(reader, response.getContentLength()));

                } else if (responseDataType == DATATYPE_FILE) {
                    long downloadSize = response.getContentLength();

                    String contentType = response.getContentType();
                    String fileName = StorageUtils
                            .randomNameWithExtension("arrowbow_download", contentType);


                    String path = new FileSaver() {
                        /**
                         *This var is declared to minimize the number of main thread calls when,
                         * the download size is so big we might have the same progress many times.
                         */
                        short lastProgress = 0;

                        @Override
                        public void showProgress(long numberOfReadBytes) {
                            short percentage = 0;

                            if (downloadSize >= numberOfReadBytes)
                                percentage = Short.valueOf(
                                        String.valueOf(((numberOfReadBytes * 100) / downloadSize)));
                            else percentage = 100;

                            if (percentage > lastProgress) {
                                lastProgress = percentage;
                                informUserWithProgress(percentage, downloadSize, numberOfReadBytes);
                            }

                        }
                    }.save(storageDirectoryIfResponseIsFile, fileName, httpConn.getInputStream());

                    response.setResult(path);
                }
                httpConn.disconnect();
                /**
                 * Notify User with new occurring event
                 */

                informUserWithNewFlag(FLAG_RESPONSE_IS_READY, response);
            } else {
                //case of error
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), Charset.forName("UTF-8")));
                /**
                 * Notify User with new occurring event
                 */
                informUserWithNewFlag(FLAG_ERROR_MESSAGE,
                        response.setResult(buildTextFromBuffer(reader, response.getContentLength()) + " Response Code: " + response.getResponseCode()));
            }
        }
    }

}