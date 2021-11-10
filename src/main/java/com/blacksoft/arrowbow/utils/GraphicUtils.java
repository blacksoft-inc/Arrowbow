package com.blacksoft.arrowbow.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blacksoft.arrowbow.helpers.ParameterizedRunnable;
import com.blacksoft.arrowbow.networking.HttpConnection;
import com.blacksoft.arrowbow.networking.RequestHeader;
import com.blacksoft.arrowbow.networking.Response;
import com.blacksoft.arrowbow.storage_manager.StorageConfig;
import com.blacksoft.arrowbow.storage_manager.StorageUtils;

import java.io.File;
import java.io.InputStream;

/**
 * All utils needed to display images
 */
public class GraphicUtils {

    /**
     * private constructor to prevent users from creating an instance from this class
     */
    private GraphicUtils() {
    }

    /**
     * compress image and load it
     *
     * @param compressionSize: means the image will be smaller (1/compressionSize)
     * @param imagePath:       image path
     */
    public static Bitmap loadCompressedBitmap(int compressionSize,
                                              @NonNull String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = compressionSize;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * display circledImage
     *
     * @param imageView: imageview you want to display the image in
     */
    public static void displayCircledImage(@NonNull ImageView imageView,
                                           @NonNull InputStream inputStream) {

        if (imageView != null)
            new Thread(new Runnable() {
                Drawable circledBitmap;

                @Override
                public void run() {
                    /**
                     * decoding bitmap
                     */
                    circledBitmap = new RoundedImage(BitmapFactory.decodeStream(inputStream));
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * displaying the bitmap
                             */
                            if (circledBitmap != null) imageView.setImageDrawable(circledBitmap);
                        }
                    });
                }
            }).start();
    }

    /**
     * display circledImage
     *
     * @param imageView:       imageview you want to display the image in
     * @param imageResourceId: image resource id
     */
    public static void displayCircledImage(@NonNull ImageView imageView,
                                           final int imageResourceId) {

        if (imageView != null)
            new Thread(new Runnable() {
                Drawable circledBitmap;

                @Override
                public void run() {
                    /**
                     * decoding bitmap
                     */
                    Bitmap bitmap = BitmapFactory.decodeResource(imageView.getResources(), imageResourceId);
                    if (bitmap != null) circledBitmap = new RoundedImage(bitmap);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * displaying the bitmap
                             */
                            if (circledBitmap != null) imageView.setImageDrawable(circledBitmap);
                        }
                    });
                }
            }).start();
    }

    /**
     * display circledImage
     *
     * @param imageView: imageview you want to display the image in.
     * @param bitmap:    image bitmap
     */
    public static void displayCircledImage(@NonNull ImageView imageView,
                                           @NonNull Bitmap bitmap) {

        if (imageView != null) {
            new Thread(new Runnable() {
                Drawable circledBitmap;

                @Override
                public void run() {
                    /**
                     * decoding bitmap
                     */
                    circledBitmap = new RoundedImage(bitmap);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * displaying the bitmap
                             */
                            if (circledBitmap != null) imageView.setImageDrawable(circledBitmap);
                        }
                    });
                }
            }).start();
        }

    }

    /**
     * displays a circled Image
     *
     * @param imageView:                         imageview you want to display the image in
     * @param filePath:                          image path(url or local path)
     * @param compressionSize:                   means the image will be smaller (1/compressionSize)
     * @param runnable:                          piece of code you want to run after the image is downloaded,
     *                                           cached and displayed, this method gives the image cached path
     *                                           inside the runnable.
     * @param executeRunnableInBackgroundThread: whether the specified runnable should run
     *                                           in the main or background thread, usually when
     *                                           it's true it can be used with some operations
     *                                           that can block the main thread like saving a row in SQLITE DB Using ROOM
     *                                           persistence library to not crash the app.
     * @param requestHeader:                     http request headers like: Authorization, ext..., can be null
     */
    public static void displayCircledImage(@NonNull ImageView imageView,
                                           @Nullable String filePath,
                                           @NonNull String storageDirectory,
                                           int compressionSize,
                                           @Nullable ParameterizedRunnable runnable,
                                           boolean executeRunnableInBackgroundThread,
                                           @Nullable RequestHeader requestHeader) {

        /**
         * case if the image already downloaded
         */
        if (StorageUtils.isStoredLocally(filePath)) {

            /**
             * case if image already exists in ram
             */
            if (MemoryUtils.imageInRam(filePath)) {

                /**
                 * using thread to not block the app main thread
                 */
                new Thread(new Runnable() {
                    RoundedImage roundedImage;

                    @Override
                    public void run() {
                        roundedImage = new RoundedImage(MemoryUtils.getImageFromRam(filePath));
                        //
                        new Handler(Looper.getMainLooper()).post(() -> {
                            imageView.setImageDrawable(roundedImage);
                            /**
                             * returning the cached file path in background thread
                             */
                            if (!executeRunnableInBackgroundThread && runnable != null) {
                                runnable.setParams(filePath);
                                runnable.run();
                            }
                        });
                        /**
                         * returning the cached file path in background thread
                         */
                        if (executeRunnableInBackgroundThread && runnable != null) {
                            runnable.setParams(filePath);
                            runnable.run();
                        }
                    }
                }).start();


            } else {

                /**
                 * Case the image is not in ram
                 *
                 * using thread to not block the app main thread
                 */
                new Thread(new Runnable() {
                    RoundedImage roundedImage;

                    @Override
                    public void run() {

                        Bitmap bitmap = GraphicUtils.loadCompressedBitmap(compressionSize, filePath);
                        MemoryUtils.addImageToRam(filePath, bitmap);
                        roundedImage = new RoundedImage(bitmap);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageDrawable(roundedImage);
                                /**
                                 * returning the cached file path in background thread
                                 */
                                if (!executeRunnableInBackgroundThread && runnable != null) {
                                    runnable.setParams(filePath);
                                    runnable.run();
                                }
                            }
                        });
                        /**
                         * returning the cached file path in background thread
                         */
                        if (executeRunnableInBackgroundThread && runnable != null) {
                            runnable.setParams(filePath);
                            runnable.run();
                        }
                    }
                }).start();

            }

        } else
        /**
         *  case it's not downloaded
         */
            new HttpConnection() {
                RoundedImage roundedImage;
                String cachedPath = null;

                @Override
                protected void doInBackgroundThread(int evolutionFlag, Response response) {
                    if (evolutionFlag == FLAG_RESPONSE_IS_READY) {
                        /**
                         * updating the media element with the cached path
                         */
                        cachedPath = response.getResult().toString();
                        Bitmap bitmap = GraphicUtils.loadCompressedBitmap(compressionSize, cachedPath);
                        roundedImage = new RoundedImage(bitmap);
                        MemoryUtils.addImageToRam(filePath, bitmap);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageDrawable(roundedImage);
                                /**
                                 * returning the cached file path in background thread
                                 */
                                if (!executeRunnableInBackgroundThread && runnable != null) {
                                    runnable.setParams(cachedPath);
                                    runnable.run();
                                }
                            }
                        });
                        /**
                         * returning the cached file path in background thread
                         */
                        if (executeRunnableInBackgroundThread && runnable != null) {
                            runnable.setParams(cachedPath);
                            runnable.run();
                        }
                    }
                }

            }.getFile(filePath,
                    requestHeader,
                    storageDirectory + File.separator + StorageConfig.IMAGES_FOLDER
            );
    }

    /**
     * display Image
     *
     * @param imageView:                         imageview you want to display the image in
     * @param filePath:                          image path(url or local path)
     * @param compressionSize:                   means the image will be smaller (1/compressionSize)
     * @param runnable:                          piece of code you want to run after the image is downloaded,
     *                                           cached and displayed, this method gives the image cached path inside the runnable.
     * @param executeRunnableInBackgroundThread: whether the specified runnable should run
     *                                           in the main or background thread, usually when
     *                                           it's true it can be used with some operations
     *                                           that can block the main thread like saving a row in SQLITE DB Using ROOM
     *                                           persistence library to not crash the app.
     * @param requestHeader:                     http request headers like: Authorization, ext..., can be null
     */
    public static void displayImage(@NonNull ImageView imageView,
                                    @Nullable String filePath,
                                    @NonNull String storageDirectory,
                                    int compressionSize,
                                    @Nullable ParameterizedRunnable runnable,
                                    boolean executeRunnableInBackgroundThread,
                                    @Nullable RequestHeader requestHeader) {

        /**
         * case if the image already downloaded
         */
        if (StorageUtils.isStoredLocally(filePath)) {

            /**
             * case if image already exists in ram
             */
            if (MemoryUtils.imageInRam(filePath)) {
                imageView.setImageBitmap(MemoryUtils.getImageFromRam(filePath));
            } else {

                /**
                 * Case the image is not in ram
                 *
                 * using thread to not block the app main thread
                 */
                new Thread(() -> {

                    Bitmap bitmap = GraphicUtils.loadCompressedBitmap(compressionSize, filePath);
                    MemoryUtils.addImageToRam(filePath, bitmap);

                    new Handler(Looper.getMainLooper()).post(() -> {
                        imageView.setImageBitmap(bitmap);
                        /**
                         * returning the cached file path in background thread
                         */
                        if (!executeRunnableInBackgroundThread && runnable != null) {
                            runnable.setParams(filePath);
                            runnable.run();
                        }
                    });
                    /**
                     * returning the cached file path in background thread
                     */
                    if (executeRunnableInBackgroundThread && runnable != null) {
                        runnable.setParams(filePath);
                        runnable.run();
                    }
                }).start();

            }

        } else
        /**
         *  case it's not downloaded
         */
            getFile(imageView, filePath, storageDirectory, compressionSize, runnable, executeRunnableInBackgroundThread, requestHeader);
    }

    /**
     *
     */
    public static void downloadBitmap(@NonNull String path,
                                      @Nullable ParameterizedRunnable runnable,
                                      boolean executeRunnableInBackgroundThread) {
    }

    @NonNull
    private static HttpConnection getFile(@NonNull ImageView imageView,
                                          @Nullable String filePath,
                                          @NonNull String storageDirectory,
                                          int compressionSize,
                                          @Nullable ParameterizedRunnable runnable,
                                          boolean executeRunnableInBackgroundThread,
                                          @Nullable RequestHeader requestHeader) {
        return new HttpConnection() {
            String cachedPath = null;

            @Override
            protected void doInBackgroundThread(int evolutionFlag, Response response) {
                //
                if (evolutionFlag == FLAG_RESPONSE_IS_READY) {

                    /**
                     * updating the media element with the cached path
                     */
                    cachedPath = response.getResult().toString();
                    Bitmap bitmap = GraphicUtils.loadCompressedBitmap(compressionSize, cachedPath);
                    MemoryUtils.addImageToRam(filePath, bitmap);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                            /**
                             * returning the cached file path in background thread
                             */
                            if (!executeRunnableInBackgroundThread && runnable != null) {
                                runnable.setParams(cachedPath);
                                runnable.run();
                            }
                        }
                    });
                    /**
                     * returning the cached file path in background thread
                     */
                    if (executeRunnableInBackgroundThread && runnable != null) {
                        runnable.setParams(cachedPath);
                        runnable.run();
                    }
                }

            }
        }.getFile(filePath,
                requestHeader,
                storageDirectory + File.separator + StorageConfig.IMAGES_FOLDER
        );
    }

    /**
     * converts a given bitmap to a rounded bitmap
     */
    private static final class RoundedImage extends Drawable {
        private final Bitmap mBitmap;
        private final Paint mPaint;
        private final RectF mRectF;
        private final int mBitmapWidth;
        private final int mBitmapHeight;
        private int imageType;
        private float radius;

        public RoundedImage(Bitmap bitmap) {
            mBitmap = bitmap;
            mRectF = new RectF();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaint.setShader(shader);
            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
        }


        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mBitmapWidth / 2, mBitmapHeight / 2, Math.min((mBitmapWidth / 2), (mBitmapHeight / 2)), mPaint);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRectF.set(bounds);
        }

        @Override
        public void setAlpha(int alpha) {
            if (mPaint.getAlpha() != alpha) {
                mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmapWidth;
        }

        @Override
        public int getIntrinsicHeight() {
            return mBitmapHeight;
        }

        public void setAntiAlias(boolean aa) {
            mPaint.setAntiAlias(aa);
            invalidateSelf();
        }

        @Override
        public void setFilterBitmap(boolean filter) {
            mPaint.setFilterBitmap(filter);
            invalidateSelf();
        }

        @Override
        public void setDither(boolean dither) {
            mPaint.setDither(dither);
            invalidateSelf();
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }
    }
}