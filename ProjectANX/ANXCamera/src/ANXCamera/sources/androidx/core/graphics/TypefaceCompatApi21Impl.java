package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import androidx.core.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import androidx.core.provider.FontsContractCompat.FontInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(21)
@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi21Impl";
    private static Method sAddFontWeightStyle;
    private static Method sCreateFromFamiliesWithDefault;
    private static Class sFontFamily;
    private static Constructor sFontFamilyCtor;
    private static boolean sHasInitBeenCalled;

    TypefaceCompatApi21Impl() {
    }

    private static boolean addFontWeightStyle(Object obj, String str, int i, boolean z) {
        init();
        try {
            return ((Boolean) sAddFontWeightStyle.invoke(obj, new Object[]{str, Integer.valueOf(i), Boolean.valueOf(z)})).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Typeface createFromFamiliesWithDefault(Object obj) {
        init();
        try {
            Object newInstance = Array.newInstance(sFontFamily, 1);
            Array.set(newInstance, 0, obj);
            return (Typeface) sCreateFromFamiliesWithDefault.invoke(null, new Object[]{newInstance});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private File getFile(@NonNull ParcelFileDescriptor parcelFileDescriptor) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
        } catch (ErrnoException unused) {
        }
        return null;
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r2v3, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void init() {
        Method method;
        Constructor constructor;
        Class cls;
        Method method2;
        ? r2;
        if (!sHasInitBeenCalled) {
            sHasInitBeenCalled = true;
            try {
                r2 = 0;
                Class cls2 = Class.forName(FONT_FAMILY_CLASS);
                Constructor constructor2 = cls2.getConstructor(new Class[0]);
                Method method3 = cls2.getMethod(ADD_FONT_WEIGHT_STYLE_METHOD, new Class[]{String.class, Integer.TYPE, Boolean.TYPE});
                Object newInstance = Array.newInstance(cls2, 1);
                ? method4 = Typeface.class.getMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{newInstance.getClass()});
                r2 = method4;
                method2 = method4;
                constructor = constructor2;
                cls = cls2;
                method = method3;
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                Log.e(TAG, e.getClass().getName(), e);
                ? r0 = r2;
                ? r1 = r0;
                method = r1;
                constructor = r2;
                method2 = r0;
                cls = r1;
            }
            sFontFamilyCtor = constructor;
            sFontFamily = cls;
            sAddFontWeightStyle = method;
            sCreateFromFamiliesWithDefault = method2;
        }
    }

    private static Object newFamily() {
        init();
        try {
            return sFontFamilyCtor.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        Object newFamily = newFamily();
        FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length = entries.length;
        int i2 = 0;
        while (i2 < length) {
            FontFileResourceEntry fontFileResourceEntry = entries[i2];
            File tempFile = TypefaceCompatUtil.getTempFile(context);
            if (tempFile == null) {
                return null;
            }
            try {
                if (!TypefaceCompatUtil.copyToFile(tempFile, resources, fontFileResourceEntry.getResourceId())) {
                    tempFile.delete();
                    return null;
                } else if (!addFontWeightStyle(newFamily, tempFile.getPath(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) {
                    return null;
                } else {
                    i2++;
                }
            } catch (RuntimeException unused) {
                return null;
            } finally {
                tempFile.delete();
            }
        }
        return createFromFamiliesWithDefault(newFamily);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x004f, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0054, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        r3.addSuppressed(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0058, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x005b, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x005c, code lost:
        if (r5 != null) goto L_0x005e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0066, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, @NonNull FontInfo[] fontInfoArr, int i) {
        if (fontInfoArr.length < 1) {
            return null;
        }
        FontInfo findBestInfo = findBestInfo(fontInfoArr, i);
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(findBestInfo.getUri(), "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                return null;
            }
            File file = getFile(openFileDescriptor);
            if (file != null) {
                if (file.canRead()) {
                    Typeface createFromFile = Typeface.createFromFile(file);
                    if (openFileDescriptor != null) {
                        openFileDescriptor.close();
                    }
                    return createFromFile;
                }
            }
            FileInputStream fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
            Typeface createFromInputStream = super.createFromInputStream(context, fileInputStream);
            fileInputStream.close();
            if (openFileDescriptor != null) {
                openFileDescriptor.close();
            }
            return createFromInputStream;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            r3.addSuppressed(th);
        }
    }
}
