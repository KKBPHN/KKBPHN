package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Option.CacheKeyUpdater;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class VideoDecoder implements ResourceDecoder {
    private static final MediaMetadataRetrieverFactory DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();
    public static final long DEFAULT_FRAME = -1;
    @VisibleForTesting
    static final int DEFAULT_FRAME_OPTION = 2;
    public static final Option FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", Integer.valueOf(2), new CacheKeyUpdater() {
        private final ByteBuffer buffer = ByteBuffer.allocate(4);

        public void update(@NonNull byte[] bArr, @NonNull Integer num, @NonNull MessageDigest messageDigest) {
            if (num != null) {
                messageDigest.update(bArr);
                synchronized (this.buffer) {
                    this.buffer.position(0);
                    messageDigest.update(this.buffer.putInt(num.intValue()).array());
                }
            }
        }
    });
    private static final String TAG = "VideoDecoder";
    public static final Option TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", Long.valueOf(-1), new CacheKeyUpdater() {
        private final ByteBuffer buffer = ByteBuffer.allocate(8);

        public void update(@NonNull byte[] bArr, @NonNull Long l, @NonNull MessageDigest messageDigest) {
            messageDigest.update(bArr);
            synchronized (this.buffer) {
                this.buffer.position(0);
                messageDigest.update(this.buffer.putLong(l.longValue()).array());
            }
        }
    });
    private final BitmapPool bitmapPool;
    private final MediaMetadataRetrieverFactory factory;
    private final MediaMetadataRetrieverInitializer initializer;

    final class AssetFileDescriptorInitializer implements MediaMetadataRetrieverInitializer {
        private AssetFileDescriptorInitializer() {
        }

        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, AssetFileDescriptor assetFileDescriptor) {
            mediaMetadataRetriever.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        }
    }

    @RequiresApi(23)
    final class ByteBufferInitializer implements MediaMetadataRetrieverInitializer {
        ByteBufferInitializer() {
        }

        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, final ByteBuffer byteBuffer) {
            mediaMetadataRetriever.setDataSource(new MediaDataSource() {
                public void close() {
                }

                public long getSize() {
                    return (long) byteBuffer.limit();
                }

                public int readAt(long j, byte[] bArr, int i, int i2) {
                    if (j >= ((long) byteBuffer.limit())) {
                        return -1;
                    }
                    byteBuffer.position((int) j);
                    int min = Math.min(i2, byteBuffer.remaining());
                    byteBuffer.get(bArr, i, min);
                    return min;
                }
            });
        }
    }

    @VisibleForTesting
    class MediaMetadataRetrieverFactory {
        MediaMetadataRetrieverFactory() {
        }

        public MediaMetadataRetriever build() {
            return new MediaMetadataRetriever();
        }
    }

    @VisibleForTesting
    interface MediaMetadataRetrieverInitializer {
        void initialize(MediaMetadataRetriever mediaMetadataRetriever, Object obj);
    }

    final class ParcelFileDescriptorInitializer implements MediaMetadataRetrieverInitializer {
        ParcelFileDescriptorInitializer() {
        }

        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, ParcelFileDescriptor parcelFileDescriptor) {
            mediaMetadataRetriever.setDataSource(parcelFileDescriptor.getFileDescriptor());
        }
    }

    VideoDecoder(BitmapPool bitmapPool2, MediaMetadataRetrieverInitializer mediaMetadataRetrieverInitializer) {
        this(bitmapPool2, mediaMetadataRetrieverInitializer, DEFAULT_FACTORY);
    }

    @VisibleForTesting
    VideoDecoder(BitmapPool bitmapPool2, MediaMetadataRetrieverInitializer mediaMetadataRetrieverInitializer, MediaMetadataRetrieverFactory mediaMetadataRetrieverFactory) {
        this.bitmapPool = bitmapPool2;
        this.initializer = mediaMetadataRetrieverInitializer;
        this.factory = mediaMetadataRetrieverFactory;
    }

    public static ResourceDecoder asset(BitmapPool bitmapPool2) {
        return new VideoDecoder(bitmapPool2, new AssetFileDescriptorInitializer());
    }

    @RequiresApi(api = 23)
    public static ResourceDecoder byteBuffer(BitmapPool bitmapPool2) {
        return new VideoDecoder(bitmapPool2, new ByteBufferInitializer());
    }

    @Nullable
    private static Bitmap decodeFrame(MediaMetadataRetriever mediaMetadataRetriever, long j, int i, int i2, int i3, DownsampleStrategy downsampleStrategy) {
        Bitmap decodeScaledFrame = (VERSION.SDK_INT < 27 || i2 == Integer.MIN_VALUE || i3 == Integer.MIN_VALUE || downsampleStrategy == DownsampleStrategy.NONE) ? null : decodeScaledFrame(mediaMetadataRetriever, j, i, i2, i3, downsampleStrategy);
        return decodeScaledFrame == null ? decodeOriginalFrame(mediaMetadataRetriever, j, i) : decodeScaledFrame;
    }

    private static Bitmap decodeOriginalFrame(MediaMetadataRetriever mediaMetadataRetriever, long j, int i) {
        return mediaMetadataRetriever.getFrameAtTime(j, i);
    }

    @TargetApi(27)
    private static Bitmap decodeScaledFrame(MediaMetadataRetriever mediaMetadataRetriever, long j, int i, int i2, int i3, DownsampleStrategy downsampleStrategy) {
        try {
            int parseInt = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
            int parseInt2 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
            int parseInt3 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(24));
            if (parseInt3 == 90 || parseInt3 == 270) {
                int i4 = parseInt2;
                parseInt2 = parseInt;
                parseInt = i4;
            }
            float scaleFactor = downsampleStrategy.getScaleFactor(parseInt, parseInt2, i2, i3);
            return mediaMetadataRetriever.getScaledFrameAtTime(j, i, Math.round(((float) parseInt) * scaleFactor), Math.round(scaleFactor * ((float) parseInt2)));
        } catch (Throwable th) {
            String str = TAG;
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Exception trying to decode frame on oreo+", th);
            }
            return null;
        }
    }

    public static ResourceDecoder parcel(BitmapPool bitmapPool2) {
        return new VideoDecoder(bitmapPool2, new ParcelFileDescriptorInitializer());
    }

    public Resource decode(@NonNull Object obj, int i, int i2, @NonNull Options options) {
        long longValue = ((Long) options.get(TARGET_FRAME)).longValue();
        if (longValue >= 0 || longValue == -1) {
            Integer num = (Integer) options.get(FRAME_OPTION);
            if (num == null) {
                num = Integer.valueOf(2);
            }
            DownsampleStrategy downsampleStrategy = (DownsampleStrategy) options.get(DownsampleStrategy.OPTION);
            if (downsampleStrategy == null) {
                downsampleStrategy = DownsampleStrategy.DEFAULT;
            }
            DownsampleStrategy downsampleStrategy2 = downsampleStrategy;
            MediaMetadataRetriever build = this.factory.build();
            try {
                this.initializer.initialize(build, obj);
                Bitmap decodeFrame = decodeFrame(build, longValue, num.intValue(), i, i2, downsampleStrategy2);
                build.release();
                return BitmapResource.obtain(decodeFrame, this.bitmapPool);
            } catch (RuntimeException e) {
                throw new IOException(e);
            } catch (Throwable th) {
                build.release();
                throw th;
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Requested frame must be non-negative, or DEFAULT_FRAME, given: ");
            sb.append(longValue);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public boolean handles(@NonNull Object obj, @NonNull Options options) {
        return true;
    }
}
