package com.xiaomi.ocr.sdk.imgprocess;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.android.camera.CameraAppImpl;
import com.android.camera.log.Log;
import java.io.File;

public class DocumentProcess {
    private static final int POINTS_COUNT = 8;
    private static final String TAG = "DocumentProcess";
    private final Object mLock = new Object();
    private long nativeObj;

    public enum DocumentType {
        DEFAULT,
        ID_CARD,
        PPT
    }

    public enum EnhanceType {
        RAW,
        COLOR,
        BIN,
        GRAY,
        FILTER_ID_CARD
    }

    public enum QuadStatus {
        QUAD_NOSHOW,
        QUAD_KEEP,
        QUAD_UPDATE
    }

    public enum RotateFlags {
        ROTATE_0,
        ROTATE_90,
        ROTATE_180,
        ROTATE_270
    }

    class Singleton {
        static final DocumentProcess INSTANCE = new DocumentProcess(CameraAppImpl.getAndroidContext().getCacheDir().getAbsolutePath(), null);

        private Singleton() {
        }
    }

    public DocumentProcess(String str, String str2) {
        init(str, str2);
    }

    private static boolean ensureFile(String str, String str2) {
        if (TextUtils.isEmpty(str) || !new File(str).exists()) {
            return false;
        }
        File file = new File(str2);
        if (file.exists()) {
            file.delete();
        }
        return true;
    }

    public static DocumentProcess getInstance() {
        return Singleton.INSTANCE;
    }

    public static String getVersion() {
        return DocumentProcessJNI.nativeGetVersion();
    }

    private void init(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mLock) {
            this.nativeObj = DocumentProcessJNI.nativeInit(str, str2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("cachePath:");
        sb.append(str);
        sb.append(", init time:");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(TAG, sb.toString());
    }

    public static void initAndRun(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        DocumentProcessJNI.nativeInitAndRun(str, str2);
        StringBuilder sb = new StringBuilder();
        sb.append("cachePath:");
        sb.append(str);
        sb.append(", initAndRun time:");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(TAG, sb.toString());
    }

    public float[] doAlginDocument(byte[] bArr, Bitmap bitmap, int i, int i2, float[] fArr, RotateFlags rotateFlags) {
        float[] fArr2 = new float[8];
        synchronized (this.mLock) {
            DocumentProcessJNI.nativeAlignDocumentBitmap(this.nativeObj, bArr, bitmap, i, i2, fArr, fArr2, rotateFlags.ordinal());
        }
        return fArr2;
    }

    public Bitmap doCropAndEnhance(Bitmap bitmap, float[] fArr, EnhanceType enhanceType, DocumentType documentType, boolean z) {
        Bitmap nativeCropAndEnhanceBitmap;
        synchronized (this.mLock) {
            nativeCropAndEnhanceBitmap = DocumentProcessJNI.nativeCropAndEnhanceBitmap(this.nativeObj, bitmap, fArr, enhanceType.ordinal(), documentType.ordinal(), z);
        }
        return nativeCropAndEnhanceBitmap;
    }

    public Bitmap doCropAndEnhance(Bitmap bitmap, float[] fArr, EnhanceType enhanceType, boolean z) {
        return doCropAndEnhance(bitmap, fArr, enhanceType, DocumentType.DEFAULT, z);
    }

    public Bitmap doCropAndEnhance(byte[] bArr, int i, int i2, float[] fArr, EnhanceType enhanceType, boolean z, RotateFlags rotateFlags) {
        Bitmap nativeCropAndEnhanceYUV;
        synchronized (this.mLock) {
            nativeCropAndEnhanceYUV = DocumentProcessJNI.nativeCropAndEnhanceYUV(this.nativeObj, bArr, i, i2, fArr, enhanceType.ordinal(), DocumentType.DEFAULT.ordinal(), z, rotateFlags.ordinal());
        }
        return nativeCropAndEnhanceYUV;
    }

    public Bitmap doCropImage(Bitmap bitmap, float[] fArr) {
        return doCropImage(bitmap, fArr, DocumentType.DEFAULT);
    }

    public Bitmap doCropImage(Bitmap bitmap, float[] fArr, DocumentType documentType) {
        Bitmap nativeCropImageBitmap;
        synchronized (this.mLock) {
            nativeCropImageBitmap = DocumentProcessJNI.nativeCropImageBitmap(this.nativeObj, bitmap, fArr, documentType.ordinal());
        }
        return nativeCropImageBitmap;
    }

    public boolean doCropImage(String str, String str2, float[] fArr) {
        if (ensureFile(str, str2)) {
            synchronized (this.mLock) {
                if (DocumentProcessJNI.nativeCropImage(this.nativeObj, str, str2, fArr) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public Bitmap doEnhance(Bitmap bitmap, EnhanceType enhanceType, boolean z) {
        synchronized (this.mLock) {
            if (DocumentProcessJNI.nativeEnhanceBitmap(this.nativeObj, bitmap, enhanceType.ordinal(), z) == 0) {
                return bitmap;
            }
            return null;
        }
    }

    public boolean doEnhance(String str, String str2, EnhanceType enhanceType) {
        if (ensureFile(str, str2)) {
            synchronized (this.mLock) {
                if (DocumentProcessJNI.nativeEnhance(this.nativeObj, str, str2, enhanceType.ordinal()) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int doScanDocument(byte[] bArr, float[] fArr, int i, int i2, int i3, int i4, DocumentType documentType, RotateFlags rotateFlags) {
        int nativeScanDocumentYUV;
        synchronized (this.mLock) {
            nativeScanDocumentYUV = DocumentProcessJNI.nativeScanDocumentYUV(this.nativeObj, bArr, i, i2, documentType.ordinal(), fArr, rotateFlags.ordinal());
        }
        return nativeScanDocumentYUV;
    }

    public float[] doScanDocument(Bitmap bitmap, DocumentType documentType) {
        float[] fArr = new float[8];
        synchronized (this.mLock) {
            DocumentProcessJNI.nativeScanDocumentBitmap(this.nativeObj, bitmap, documentType.ordinal(), fArr, 0);
        }
        return fArr;
    }

    public float[] doScanDocument(Bitmap bitmap, DocumentType documentType, RotateFlags rotateFlags) {
        float[] fArr = new float[8];
        synchronized (this.mLock) {
            DocumentProcessJNI.nativeScanDocumentBitmap(this.nativeObj, bitmap, documentType.ordinal(), fArr, rotateFlags.ordinal());
        }
        return fArr;
    }

    public float[] doScanDocument(String str, DocumentType documentType) {
        float[] fArr = new float[8];
        synchronized (this.mLock) {
            DocumentProcessJNI.nativeScanDocument(this.nativeObj, str, documentType.ordinal(), fArr);
        }
        return fArr;
    }

    public float[] doScanDocument(byte[] bArr, int i, int i2, int i3, int i4, DocumentType documentType, RotateFlags rotateFlags) {
        float[] fArr = new float[8];
        synchronized (this.mLock) {
            DocumentProcessJNI.nativeScanDocumentYUV(this.nativeObj, bArr, i, i2, documentType.ordinal(), fArr, rotateFlags.ordinal());
        }
        return fArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0017, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean normalizationPoints(float[] fArr) {
        synchronized (this.mLock) {
            boolean z = false;
            if (fArr != null) {
                if (fArr.length == 8) {
                    if (DocumentProcessJNI.nativeNormalizationPoints(this.nativeObj, fArr) == 0) {
                        z = true;
                    }
                }
            }
        }
    }

    public void release() {
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mLock) {
            DocumentProcessJNI.nativeRelease(this.nativeObj);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("release time:");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(TAG, sb.toString());
    }

    public float[] rotatePoints(float[] fArr, int i, int i2, RotateFlags rotateFlags) {
        float[] fArr2 = new float[8];
        DocumentProcessJNI.nativeRotateDocumentYUVScanPoints(fArr, i, i2, rotateFlags.ordinal(), fArr2);
        return fArr2;
    }
}
