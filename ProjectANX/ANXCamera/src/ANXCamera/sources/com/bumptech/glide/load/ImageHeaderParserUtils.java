package com.bumptech.glide.load;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.ImageHeaderParser.ImageType;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class ImageHeaderParserUtils {
    private static final int MARK_READ_LIMIT = 5242880;

    interface OrientationReader {
        int getOrientation(ImageHeaderParser imageHeaderParser);
    }

    interface TypeReader {
        ImageType getType(ImageHeaderParser imageHeaderParser);
    }

    private ImageHeaderParserUtils() {
    }

    @RequiresApi(21)
    public static int getOrientation(@NonNull List list, @NonNull final ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, @NonNull final ArrayPool arrayPool) {
        return getOrientationInternal(list, new OrientationReader() {
            /* JADX WARNING: Removed duplicated region for block: B:14:0x002c A[SYNTHETIC, Splitter:B:14:0x002c] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public int getOrientation(ImageHeaderParser imageHeaderParser) {
                RecyclableBufferedInputStream recyclableBufferedInputStream = null;
                try {
                    RecyclableBufferedInputStream recyclableBufferedInputStream2 = new RecyclableBufferedInputStream(new FileInputStream(ParcelFileDescriptorRewinder.this.rewindAndGet().getFileDescriptor()), arrayPool);
                    try {
                        int orientation = imageHeaderParser.getOrientation((InputStream) recyclableBufferedInputStream2, arrayPool);
                        try {
                            recyclableBufferedInputStream2.close();
                        } catch (IOException unused) {
                        }
                        ParcelFileDescriptorRewinder.this.rewindAndGet();
                        return orientation;
                    } catch (Throwable th) {
                        th = th;
                        recyclableBufferedInputStream = recyclableBufferedInputStream2;
                        if (recyclableBufferedInputStream != null) {
                            try {
                                recyclableBufferedInputStream.close();
                            } catch (IOException unused2) {
                            }
                        }
                        ParcelFileDescriptorRewinder.this.rewindAndGet();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (recyclableBufferedInputStream != null) {
                    }
                    ParcelFileDescriptorRewinder.this.rewindAndGet();
                    throw th;
                }
            }
        });
    }

    public static int getOrientation(@NonNull List list, @Nullable final InputStream inputStream, @NonNull final ArrayPool arrayPool) {
        if (inputStream == null) {
            return -1;
        }
        if (!inputStream.markSupported()) {
            inputStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream.mark(MARK_READ_LIMIT);
        return getOrientationInternal(list, new OrientationReader() {
            public int getOrientation(ImageHeaderParser imageHeaderParser) {
                try {
                    int orientation = imageHeaderParser.getOrientation(inputStream, arrayPool);
                    return orientation;
                } finally {
                    inputStream.reset();
                }
            }
        });
    }

    private static int getOrientationInternal(@NonNull List list, OrientationReader orientationReader) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int orientation = orientationReader.getOrientation((ImageHeaderParser) list.get(i));
            if (orientation != -1) {
                return orientation;
            }
        }
        return -1;
    }

    @RequiresApi(21)
    @NonNull
    public static ImageType getType(@NonNull List list, @NonNull final ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, @NonNull final ArrayPool arrayPool) {
        return getTypeInternal(list, new TypeReader() {
            /* JADX WARNING: Removed duplicated region for block: B:14:0x002a A[SYNTHETIC, Splitter:B:14:0x002a] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public ImageType getType(ImageHeaderParser imageHeaderParser) {
                RecyclableBufferedInputStream recyclableBufferedInputStream = null;
                try {
                    RecyclableBufferedInputStream recyclableBufferedInputStream2 = new RecyclableBufferedInputStream(new FileInputStream(ParcelFileDescriptorRewinder.this.rewindAndGet().getFileDescriptor()), arrayPool);
                    try {
                        ImageType type = imageHeaderParser.getType((InputStream) recyclableBufferedInputStream2);
                        try {
                            recyclableBufferedInputStream2.close();
                        } catch (IOException unused) {
                        }
                        ParcelFileDescriptorRewinder.this.rewindAndGet();
                        return type;
                    } catch (Throwable th) {
                        th = th;
                        recyclableBufferedInputStream = recyclableBufferedInputStream2;
                        if (recyclableBufferedInputStream != null) {
                            try {
                                recyclableBufferedInputStream.close();
                            } catch (IOException unused2) {
                            }
                        }
                        ParcelFileDescriptorRewinder.this.rewindAndGet();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (recyclableBufferedInputStream != null) {
                    }
                    ParcelFileDescriptorRewinder.this.rewindAndGet();
                    throw th;
                }
            }
        });
    }

    @NonNull
    public static ImageType getType(@NonNull List list, @Nullable final InputStream inputStream, @NonNull ArrayPool arrayPool) {
        if (inputStream == null) {
            return ImageType.UNKNOWN;
        }
        if (!inputStream.markSupported()) {
            inputStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream.mark(MARK_READ_LIMIT);
        return getTypeInternal(list, new TypeReader() {
            public ImageType getType(ImageHeaderParser imageHeaderParser) {
                try {
                    ImageType type = imageHeaderParser.getType(inputStream);
                    return type;
                } finally {
                    inputStream.reset();
                }
            }
        });
    }

    @NonNull
    public static ImageType getType(@NonNull List list, @Nullable final ByteBuffer byteBuffer) {
        return byteBuffer == null ? ImageType.UNKNOWN : getTypeInternal(list, new TypeReader() {
            public ImageType getType(ImageHeaderParser imageHeaderParser) {
                return imageHeaderParser.getType(byteBuffer);
            }
        });
    }

    @NonNull
    private static ImageType getTypeInternal(@NonNull List list, TypeReader typeReader) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ImageType type = typeReader.getType((ImageHeaderParser) list.get(i));
            if (type != ImageType.UNKNOWN) {
                return type;
            }
        }
        return ImageType.UNKNOWN;
    }
}
