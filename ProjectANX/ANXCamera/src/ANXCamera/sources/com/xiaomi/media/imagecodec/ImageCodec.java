package com.xiaomi.media.imagecodec;

import android.media.Image;
import android.util.Log;
import android.view.Surface;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ImageCodec {
    private static final String TAG = "ImageCodec";
    private final Object mContextLock = new Object();
    private ImageSpec mInputSpec;
    private long mNativeContext;
    private ImageSpec mOutputSpec;

    public final class ImageSpec {
        public final int format;
        public final int height;
        public final int width;

        private ImageSpec(int i, int i2, int i3) {
            this.width = i;
            this.height = i2;
            this.format = i3;
        }

        public String toString() {
            return String.format(Locale.US, "ImageSpec(%d, %d, %d)", new Object[]{Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.format)});
        }
    }

    static {
        System.loadLibrary("jni_imagecodec");
        nativeClassInit();
    }

    private ImageCodec(ImageSpec imageSpec) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ctor(): E ");
        sb.append(hashCode());
        Log.d(str, sb.toString());
        this.mInputSpec = imageSpec;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ctor(): X ");
        sb2.append(hashCode());
        Log.d(str2, sb2.toString());
    }

    public static ImageCodec create(int i, int i2, int i3) {
        if (i < 1 || i2 < 1) {
            throw new IllegalArgumentException("The image dimensions must be positive");
        } else if (i3 == 35 || i3 == 17) {
            return new ImageCodec(new ImageSpec(i, i2, i3));
        } else {
            throw new IllegalArgumentException("The image format must be YUV_420_888 or NV21");
        }
    }

    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Object getImageOwner(Image image) {
        boolean z = 0;
        try {
            z = z;
            Method declaredMethod = Image.class.getDeclaredMethod("getOwner", new Class[0]);
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke(image, new Object[0]);
            z = invoke;
            return invoke;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.w(TAG, "Failed to get the owner of the given image", e);
            return Boolean.valueOf(z);
        }
    }

    private boolean isImageValid(Image image) {
        try {
            Field declaredField = Image.class.getDeclaredField("mIsImageValid");
            declaredField.setAccessible(true);
            return declaredField.getBoolean(image);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.w(TAG, "Failed to check if the given image is valid ot not", e);
            return false;
        }
    }

    private static native void nativeClassInit();

    private native long nativeCreate(Object obj, int i, int i2, int i3, Surface surface);

    private native void nativeDestroy(long j);

    private native void nativeDrainInputImage(long j, Image image);

    private native void nativeGetOutputSpec(long j, ImageSpec imageSpec);

    private native void nativeSetCodecQuality(long j, int i);

    private native void nativeSetFlipFlag(long j, int i);

    private static void postEventFromNative(Object obj) {
        Log.d(TAG, "postEventFromNative(): E");
        if (((ImageCodec) ((WeakReference) obj).get()) != null) {
            Log.d(TAG, "postEventFromNative(): X");
        }
    }

    public void drainInputImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("The input image must not be null");
        } else if (image.getWidth() != this.mInputSpec.width || image.getHeight() != this.mInputSpec.height) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid input image dimensions: ");
            sb.append(image.getWidth());
            sb.append("x");
            sb.append(image.getHeight());
            throw new IllegalArgumentException(sb.toString());
        } else if (image.getFormat() != this.mInputSpec.format) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid input image format: ");
            sb2.append(image.getFormat());
            throw new IllegalArgumentException(sb2.toString());
        } else if (!image.getClass().getName().equals("android.media.ImageReader$SurfaceImage")) {
            throw new IllegalArgumentException("Only images from ImageReader can be fed to ImageCodec, other image source is not supported yet!");
        } else if (isImageValid(image)) {
            synchronized (this.mContextLock) {
                try {
                    if (this.mNativeContext != 0) {
                        nativeDrainInputImage(this.mNativeContext, image);
                    }
                    image.close();
                } catch (Throwable th) {
                    image.close();
                    throw th;
                }
            }
        } else {
            throw new IllegalStateException("Image is already closed");
        }
    }

    public ImageSpec getInputSpec() {
        return this.mInputSpec;
    }

    public ImageSpec getOutputSpec() {
        synchronized (this.mContextLock) {
            if (this.mOutputSpec == null) {
                if (this.mNativeContext != 0) {
                    this.mOutputSpec = new ImageSpec(0, 0, 0);
                    nativeGetOutputSpec(this.mNativeContext, this.mOutputSpec);
                } else {
                    throw new IllegalArgumentException("ImageCodec is not initialized");
                }
            }
        }
        return this.mOutputSpec;
    }

    public void release() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("release(): E ");
        sb.append(hashCode());
        Log.d(str, sb.toString());
        synchronized (this.mContextLock) {
            nativeDestroy(this.mNativeContext);
            this.mNativeContext = 0;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("release(): X ");
        sb2.append(hashCode());
        Log.d(str2, sb2.toString());
    }

    public void setFlip(boolean z) {
        synchronized (this.mContextLock) {
            if (this.mNativeContext != 0) {
                nativeSetFlipFlag(this.mNativeContext, z ? 1 : 0);
            } else {
                throw new IllegalArgumentException("ImageCodec is not initialized");
            }
        }
    }

    public void setOutputSurface(Surface surface) {
        if (surface != null) {
            WeakReference weakReference = new WeakReference(this);
            ImageSpec imageSpec = this.mInputSpec;
            this.mNativeContext = nativeCreate(weakReference, imageSpec.width, imageSpec.height, imageSpec.format, surface);
            return;
        }
        throw new IllegalArgumentException("The given output surface must not be null");
    }

    public void setQuality(int i) {
        if (i < 0 || i > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        }
        synchronized (this.mContextLock) {
            if (this.mNativeContext != 0) {
                nativeSetCodecQuality(this.mNativeContext, i);
            } else {
                throw new IllegalArgumentException("ImageCodec is not initialized");
            }
        }
    }
}
