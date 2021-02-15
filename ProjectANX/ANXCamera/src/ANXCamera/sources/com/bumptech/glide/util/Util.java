package com.bumptech.glide.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

public final class Util {
    private static final int HASH_ACCUMULATOR = 17;
    private static final int HASH_MULTIPLIER = 31;
    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] SHA_256_CHARS = new char[64];

    /* renamed from: com.bumptech.glide.util.Util$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 1;
            $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 2;
            $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 3;
            $SwitchMap$android$graphics$Bitmap$Config[Config.RGBA_F16.ordinal()] = 4;
            $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 5;
        }
    }

    private Util() {
    }

    public static void assertBackgroundThread() {
        if (!isOnBackgroundThread()) {
            throw new IllegalArgumentException("You must call this method on a background thread");
        }
    }

    public static void assertMainThread() {
        if (!isOnMainThread()) {
            throw new IllegalArgumentException("You must call this method on the main thread");
        }
    }

    public static boolean bothModelsNullEquivalentOrEquals(@Nullable Object obj, @Nullable Object obj2) {
        if (obj != null) {
            return obj instanceof Model ? ((Model) obj).isEquivalentTo(obj2) : obj.equals(obj2);
        }
        return obj2 == null;
    }

    public static boolean bothNullOrEqual(@Nullable Object obj, @Nullable Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    @NonNull
    private static String bytesToHex(@NonNull byte[] bArr, @NonNull char[] cArr) {
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i] & -1;
            int i2 = i * 2;
            char[] cArr2 = HEX_CHAR_ARRAY;
            cArr[i2] = cArr2[b >>> 4];
            cArr[i2 + 1] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    @NonNull
    public static Queue createQueue(int i) {
        return new ArrayDeque(i);
    }

    public static int getBitmapByteSize(int i, int i2, @Nullable Config config) {
        return i * i2 * getBytesPerPixel(config);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [android.graphics.Bitmap, java.lang.Object] */
    /* JADX WARNING: type inference failed for: r3v3, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6, types: [int] */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=android.graphics.Bitmap, code=null, for r3v0, types: [android.graphics.Bitmap, java.lang.Object] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v5
  assigns: []
  uses: []
  mth insns count: 33
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @TargetApi(19)
    public static int getBitmapByteSize(@NonNull Bitmap r3) {
        if (!r3.isRecycled()) {
            if (VERSION.SDK_INT >= 19) {
                try {
                    r3 = r3;
                    r3 = r3.getAllocationByteCount();
                    r3 = r3;
                    return r3;
                } catch (NullPointerException unused) {
                    r3 = r3;
                }
            }
            return r3.getHeight() * r3.getRowBytes();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot obtain size for recycled Bitmap: ");
        sb.append(r3);
        sb.append("[");
        sb.append(r3.getWidth());
        sb.append("x");
        sb.append(r3.getHeight());
        sb.append("] ");
        sb.append(r3.getConfig());
        throw new IllegalStateException(sb.toString());
    }

    private static int getBytesPerPixel(@Nullable Config config) {
        if (config == null) {
            config = Config.ARGB_8888;
        }
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
        if (i == 1) {
            return 1;
        }
        if (i == 2 || i == 3) {
            return 2;
        }
        return i != 4 ? 4 : 8;
    }

    @Deprecated
    public static int getSize(@NonNull Bitmap bitmap) {
        return getBitmapByteSize(bitmap);
    }

    @NonNull
    public static List getSnapshot(@NonNull Collection collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (Object next : collection) {
            if (next != null) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static int hashCode(float f) {
        return hashCode(f, 17);
    }

    public static int hashCode(float f, int i) {
        return hashCode(Float.floatToIntBits(f), i);
    }

    public static int hashCode(int i) {
        return hashCode(i, 17);
    }

    public static int hashCode(int i, int i2) {
        return (i2 * 31) + i;
    }

    public static int hashCode(@Nullable Object obj, int i) {
        return hashCode(obj == null ? 0 : obj.hashCode(), i);
    }

    public static int hashCode(boolean z) {
        return hashCode(z, 17);
    }

    public static int hashCode(boolean z, int i) {
        return hashCode(z ? 1 : 0, i);
    }

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static boolean isValidDimension(int i) {
        return i > 0 || i == Integer.MIN_VALUE;
    }

    public static boolean isValidDimensions(int i, int i2) {
        return isValidDimension(i) && isValidDimension(i2);
    }

    @NonNull
    public static String sha256BytesToHex(@NonNull byte[] bArr) {
        String bytesToHex;
        synchronized (SHA_256_CHARS) {
            bytesToHex = bytesToHex(bArr, SHA_256_CHARS);
        }
        return bytesToHex;
    }
}
