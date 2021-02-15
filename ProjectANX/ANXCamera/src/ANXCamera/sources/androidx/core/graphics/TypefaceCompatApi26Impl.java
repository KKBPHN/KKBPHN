package androidx.core.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.Typeface.Builder;
import android.graphics.fonts.FontVariationAxis;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import androidx.core.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import androidx.core.provider.FontsContractCompat;
import androidx.core.provider.FontsContractCompat.FontInfo;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

@RequiresApi(26)
@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
    private static final String ABORT_CREATION_METHOD = "abortCreation";
    private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
    private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String FREEZE_METHOD = "freeze";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi26Impl";
    protected final Method mAbortCreation;
    protected final Method mAddFontFromAssetManager;
    protected final Method mAddFontFromBuffer;
    protected final Method mCreateFromFamiliesWithDefault;
    protected final Class mFontFamily;
    protected final Constructor mFontFamilyCtor;
    protected final Method mFreeze;

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r5v1 */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TypefaceCompatApi26Impl() {
        Method method;
        Method method2;
        Method method3;
        Method method4;
        Constructor constructor;
        Method method5;
        Class cls;
        ? r0;
        try {
            r0 = 0;
            Class obtainFontFamily = obtainFontFamily();
            Constructor obtainFontFamilyCtor = obtainFontFamilyCtor(obtainFontFamily);
            Method obtainAddFontFromAssetManagerMethod = obtainAddFontFromAssetManagerMethod(obtainFontFamily);
            Method obtainAddFontFromBufferMethod = obtainAddFontFromBufferMethod(obtainFontFamily);
            Method obtainFreezeMethod = obtainFreezeMethod(obtainFontFamily);
            Method obtainAbortCreationMethod = obtainAbortCreationMethod(obtainFontFamily);
            ? obtainCreateFromFamiliesWithDefaultMethod = obtainCreateFromFamiliesWithDefaultMethod(obtainFontFamily);
            r0 = obtainCreateFromFamiliesWithDefaultMethod;
            ? r7 = obtainFontFamily;
            method5 = obtainCreateFromFamiliesWithDefaultMethod;
            cls = r7;
            constructor = obtainFontFamilyCtor;
            method4 = obtainAddFontFromAssetManagerMethod;
            method3 = obtainAddFontFromBufferMethod;
            method2 = obtainFreezeMethod;
            method = obtainAbortCreationMethod;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to collect necessary methods for class ");
            sb.append(e.getClass().getName());
            Log.e(TAG, sb.toString(), e);
            ? r1 = r0;
            ? r2 = r1;
            ? r3 = r2;
            ? r4 = r3;
            ? r5 = r4;
            method = r5;
            cls = r0;
            method5 = r1;
            constructor = r2;
            method4 = r3;
            method3 = r4;
            method2 = r5;
        }
        this.mFontFamily = cls;
        this.mFontFamilyCtor = constructor;
        this.mAddFontFromAssetManager = method4;
        this.mAddFontFromBuffer = method3;
        this.mFreeze = method2;
        this.mAbortCreation = method;
        this.mCreateFromFamiliesWithDefault = method5;
    }

    private void abortCreation(Object obj) {
        try {
            this.mAbortCreation.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException unused) {
        }
    }

    private boolean addFontFromAssetManager(Context context, Object obj, String str, int i, int i2, int i3, @Nullable FontVariationAxis[] fontVariationAxisArr) {
        try {
            return ((Boolean) this.mAddFontFromAssetManager.invoke(obj, new Object[]{context.getAssets(), str, Integer.valueOf(0), Boolean.valueOf(false), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), fontVariationAxisArr})).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }

    private boolean addFontFromBuffer(Object obj, ByteBuffer byteBuffer, int i, int i2, int i3) {
        try {
            return ((Boolean) this.mAddFontFromBuffer.invoke(obj, new Object[]{byteBuffer, Integer.valueOf(i), null, Integer.valueOf(i2), Integer.valueOf(i3)})).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }

    private boolean freeze(Object obj) {
        try {
            return ((Boolean) this.mFreeze.invoke(obj, new Object[0])).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }

    private boolean isFontFamilyPrivateAPIAvailable() {
        if (this.mAddFontFromAssetManager == null) {
            Log.w(TAG, "Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        return this.mAddFontFromAssetManager != null;
    }

    @Nullable
    private Object newFamily() {
        try {
            return this.mFontFamilyCtor.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException unused) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Typeface createFromFamiliesWithDefault(Object obj) {
        try {
            Object newInstance = Array.newInstance(this.mFontFamily, 1);
            Array.set(newInstance, 0, obj);
            return (Typeface) this.mCreateFromFamiliesWithDefault.invoke(null, new Object[]{newInstance, Integer.valueOf(-1), Integer.valueOf(-1)});
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return null;
        }
    }

    @Nullable
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        FontFileResourceEntry[] entries;
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        Object newFamily = newFamily();
        if (newFamily == null) {
            return null;
        }
        for (FontFileResourceEntry fontFileResourceEntry : fontFamilyFilesResourceEntry.getEntries()) {
            if (!addFontFromAssetManager(context, newFamily, fontFileResourceEntry.getFileName(), fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic() ? 1 : 0, FontVariationAxis.fromFontVariationSettings(fontFileResourceEntry.getVariationSettings()))) {
                abortCreation(newFamily);
                return null;
            }
        }
        if (!freeze(newFamily)) {
            return null;
        }
        return createFromFamiliesWithDefault(newFamily);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004b, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004c, code lost:
        if (r11 != null) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r11.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0056, code lost:
        throw r12;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] fontInfoArr, int i) {
        if (fontInfoArr.length < 1) {
            return null;
        }
        if (!isFontFamilyPrivateAPIAvailable()) {
            FontInfo findBestInfo = findBestInfo(fontInfoArr, i);
            try {
                ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(findBestInfo.getUri(), "r", cancellationSignal);
                if (openFileDescriptor == null) {
                    if (openFileDescriptor != null) {
                        openFileDescriptor.close();
                    }
                    return null;
                }
                Typeface build = new Builder(openFileDescriptor.getFileDescriptor()).setWeight(findBestInfo.getWeight()).setItalic(findBestInfo.isItalic()).build();
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                return build;
            } catch (IOException unused) {
                return null;
            } catch (Throwable th) {
                r10.addSuppressed(th);
            }
        } else {
            Map prepareFontData = FontsContractCompat.prepareFontData(context, fontInfoArr, cancellationSignal);
            Object newFamily = newFamily();
            if (newFamily == null) {
                return null;
            }
            boolean z = false;
            for (FontInfo fontInfo : fontInfoArr) {
                ByteBuffer byteBuffer = (ByteBuffer) prepareFontData.get(fontInfo.getUri());
                if (byteBuffer != null) {
                    if (!addFontFromBuffer(newFamily, byteBuffer, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic() ? 1 : 0)) {
                        abortCreation(newFamily);
                        return null;
                    }
                    z = true;
                }
            }
            if (!z) {
                abortCreation(newFamily);
                return null;
            } else if (!freeze(newFamily)) {
                return null;
            } else {
                Typeface createFromFamiliesWithDefault = createFromFamiliesWithDefault(newFamily);
                if (createFromFamiliesWithDefault == null) {
                    return null;
                }
                return Typeface.create(createFromFamiliesWithDefault, i);
            }
        }
    }

    @Nullable
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, i, str, i2);
        }
        Object newFamily = newFamily();
        if (newFamily == null) {
            return null;
        }
        if (!addFontFromAssetManager(context, newFamily, str, 0, -1, -1, null)) {
            abortCreation(newFamily);
            return null;
        } else if (!freeze(newFamily)) {
            return null;
        } else {
            return createFromFamiliesWithDefault(newFamily);
        }
    }

    /* access modifiers changed from: protected */
    public Method obtainAbortCreationMethod(Class cls) {
        return cls.getMethod(ABORT_CREATION_METHOD, new Class[0]);
    }

    /* access modifiers changed from: protected */
    public Method obtainAddFontFromAssetManagerMethod(Class cls) {
        Class cls2 = Integer.TYPE;
        return cls.getMethod(ADD_FONT_FROM_ASSET_MANAGER_METHOD, new Class[]{AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, cls2, cls2, cls2, FontVariationAxis[].class});
    }

    /* access modifiers changed from: protected */
    public Method obtainAddFontFromBufferMethod(Class cls) {
        Class cls2 = Integer.TYPE;
        return cls.getMethod(ADD_FONT_FROM_BUFFER_METHOD, new Class[]{ByteBuffer.class, cls2, FontVariationAxis[].class, cls2, cls2});
    }

    /* access modifiers changed from: protected */
    public Method obtainCreateFromFamiliesWithDefaultMethod(Class cls) {
        Class cls2 = Integer.TYPE;
        Method declaredMethod = Typeface.class.getDeclaredMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{Array.newInstance(cls, 1).getClass(), cls2, cls2});
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }

    /* access modifiers changed from: protected */
    public Class obtainFontFamily() {
        return Class.forName(FONT_FAMILY_CLASS);
    }

    /* access modifiers changed from: protected */
    public Constructor obtainFontFamilyCtor(Class cls) {
        return cls.getConstructor(new Class[0]);
    }

    /* access modifiers changed from: protected */
    public Method obtainFreezeMethod(Class cls) {
        return cls.getMethod(FREEZE_METHOD, new Class[0]);
    }
}
