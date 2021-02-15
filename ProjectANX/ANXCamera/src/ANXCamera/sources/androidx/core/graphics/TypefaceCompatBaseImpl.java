package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import androidx.core.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import androidx.core.provider.FontsContractCompat.FontInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
class TypefaceCompatBaseImpl {
    private static final int INVALID_KEY = 0;
    private static final String TAG = "TypefaceCompatBaseImpl";
    private ConcurrentHashMap mFontFamilies = new ConcurrentHashMap();

    interface StyleExtractor {
        int getWeight(Object obj);

        boolean isItalic(Object obj);
    }

    TypefaceCompatBaseImpl() {
    }

    private void addFontFamily(Typeface typeface, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry) {
        long uniqueKey = getUniqueKey(typeface);
        if (uniqueKey != 0) {
            this.mFontFamilies.put(Long.valueOf(uniqueKey), fontFamilyFilesResourceEntry);
        }
    }

    private FontFileResourceEntry findBestEntry(FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, int i) {
        return (FontFileResourceEntry) findBestFont(fontFamilyFilesResourceEntry.getEntries(), i, new StyleExtractor() {
            public int getWeight(FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.getWeight();
            }

            public boolean isItalic(FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.isItalic();
            }
        });
    }

    private static Object findBestFont(Object[] objArr, int i, StyleExtractor styleExtractor) {
        int i2 = (i & 1) == 0 ? 400 : 700;
        boolean z = (i & 2) != 0;
        int i3 = Integer.MAX_VALUE;
        Object obj = null;
        for (Object obj2 : objArr) {
            int abs = (Math.abs(styleExtractor.getWeight(obj2) - i2) * 2) + (styleExtractor.isItalic(obj2) == z ? 0 : 1);
            if (obj == null || i3 > abs) {
                obj = obj2;
                i3 = abs;
            }
        }
        return obj;
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long getUniqueKey(@Nullable Typeface typeface) {
        String str;
        String str2;
        ? r0 = "Could not retrieve font from family.";
        String str3 = TAG;
        if (typeface == null) {
            return 0;
        }
        try {
            str2 = r0;
            Field declaredField = Typeface.class.getDeclaredField("native_instance");
            declaredField.setAccessible(true);
            long longValue = ((Number) declaredField.get(typeface)).longValue();
            str2 = longValue;
            return longValue;
        } catch (NoSuchFieldException e) {
            Log.e(str3, str, e);
            return 0;
        } catch (IllegalAccessException e2) {
            Log.e(str3, str2, e2);
            return 0;
        }
    }

    @Nullable
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        FontFileResourceEntry findBestEntry = findBestEntry(fontFamilyFilesResourceEntry, i);
        if (findBestEntry == null) {
            return null;
        }
        Typeface createFromResourcesFontFile = TypefaceCompat.createFromResourcesFontFile(context, resources, findBestEntry.getResourceId(), findBestEntry.getFileName(), i);
        addFontFamily(createFromResourcesFontFile, fontFamilyFilesResourceEntry);
        return createFromResourcesFontFile;
    }

    @Nullable
    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] fontInfoArr, int i) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        if (fontInfoArr.length < 1) {
            return null;
        }
        try {
            inputStream = context.getContentResolver().openInputStream(findBestInfo(fontInfoArr, i).getUri());
            try {
                Typeface createFromInputStream = createFromInputStream(context, inputStream);
                TypefaceCompatUtil.closeQuietly(inputStream);
                return createFromInputStream;
            } catch (IOException unused) {
                TypefaceCompatUtil.closeQuietly(inputStream);
                return null;
            } catch (Throwable th) {
                th = th;
                inputStream2 = inputStream;
                TypefaceCompatUtil.closeQuietly(inputStream2);
                throw th;
            }
        } catch (IOException unused2) {
            inputStream = null;
            TypefaceCompatUtil.closeQuietly(inputStream);
            return null;
        } catch (Throwable th2) {
            th = th2;
            TypefaceCompatUtil.closeQuietly(inputStream2);
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public Typeface createFromInputStream(Context context, InputStream inputStream) {
        File tempFile = TypefaceCompatUtil.getTempFile(context);
        Typeface typeface = null;
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tempFile, inputStream)) {
                return null;
            }
            typeface = Typeface.createFromFile(tempFile.getPath());
            tempFile.delete();
            return typeface;
        } catch (RuntimeException unused) {
            return typeface;
        } finally {
            tempFile.delete();
        }
    }

    @Nullable
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        File tempFile = TypefaceCompatUtil.getTempFile(context);
        Typeface typeface = null;
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tempFile, resources, i)) {
                return null;
            }
            typeface = Typeface.createFromFile(tempFile.getPath());
            tempFile.delete();
            return typeface;
        } catch (RuntimeException unused) {
            return typeface;
        } finally {
            tempFile.delete();
        }
    }

    /* access modifiers changed from: protected */
    public FontInfo findBestInfo(FontInfo[] fontInfoArr, int i) {
        return (FontInfo) findBestFont(fontInfoArr, i, new StyleExtractor() {
            public int getWeight(FontInfo fontInfo) {
                return fontInfo.getWeight();
            }

            public boolean isItalic(FontInfo fontInfo) {
                return fontInfo.isItalic();
            }
        });
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public FontFamilyFilesResourceEntry getFontFamily(Typeface typeface) {
        long uniqueKey = getUniqueKey(typeface);
        if (uniqueKey == 0) {
            return null;
        }
        return (FontFamilyFilesResourceEntry) this.mFontFamilies.get(Long.valueOf(uniqueKey));
    }
}
