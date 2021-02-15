package androidx.core.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.collection.LruCache;
import androidx.core.content.res.FontResourcesParserCompat.FamilyResourceEntry;
import androidx.core.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import androidx.core.content.res.FontResourcesParserCompat.ProviderResourceEntry;
import androidx.core.content.res.ResourcesCompat.FontCallback;
import androidx.core.provider.FontsContractCompat;
import androidx.core.provider.FontsContractCompat.FontInfo;

@SuppressLint({"NewApi"})
public class TypefaceCompat {
    private static final LruCache sTypefaceCache = new LruCache(16);
    private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;

    static {
        int i = VERSION.SDK_INT;
        TypefaceCompatBaseImpl typefaceCompatBaseImpl = i >= 29 ? new TypefaceCompatApi29Impl() : i >= 28 ? new TypefaceCompatApi28Impl() : i >= 26 ? new TypefaceCompatApi26Impl() : (i < 24 || !TypefaceCompatApi24Impl.isUsable()) ? VERSION.SDK_INT >= 21 ? new TypefaceCompatApi21Impl() : new TypefaceCompatBaseImpl() : new TypefaceCompatApi24Impl();
        sTypefaceCompatImpl = typefaceCompatBaseImpl;
    }

    private TypefaceCompat() {
    }

    @NonNull
    public static Typeface create(@NonNull Context context, @Nullable Typeface typeface, int i) {
        if (context != null) {
            if (VERSION.SDK_INT < 21) {
                Typeface bestFontFromFamily = getBestFontFromFamily(context, typeface, i);
                if (bestFontFromFamily != null) {
                    return bestFontFromFamily;
                }
            }
            return Typeface.create(typeface, i);
        }
        throw new IllegalArgumentException("Context cannot be null");
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static Typeface createFromFontInfo(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] fontInfoArr, int i) {
        return sTypefaceCompatImpl.createFromFontInfo(context, cancellationSignal, fontInfoArr, i);
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static Typeface createFromResourcesFamilyXml(@NonNull Context context, @NonNull FamilyResourceEntry familyResourceEntry, @NonNull Resources resources, int i, int i2, @Nullable FontCallback fontCallback, @Nullable Handler handler, boolean z) {
        Typeface typeface;
        if (familyResourceEntry instanceof ProviderResourceEntry) {
            ProviderResourceEntry providerResourceEntry = (ProviderResourceEntry) familyResourceEntry;
            boolean z2 = false;
            if (!z ? fontCallback == null : providerResourceEntry.getFetchStrategy() == 0) {
                z2 = true;
            }
            typeface = FontsContractCompat.getFontSync(context, providerResourceEntry.getRequest(), fontCallback, handler, z2, z ? providerResourceEntry.getTimeout() : -1, i2, z);
        } else {
            typeface = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, (FontFamilyFilesResourceEntry) familyResourceEntry, resources, i2);
            if (fontCallback != null) {
                if (typeface != null) {
                    fontCallback.callbackSuccessAsync(typeface, handler);
                } else {
                    fontCallback.callbackFailAsync(-3, handler);
                }
            }
        }
        if (typeface != null) {
            sTypefaceCache.put(createResourceUid(resources, i, i2), typeface);
        }
        return typeface;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static Typeface createFromResourcesFontFile(@NonNull Context context, @NonNull Resources resources, int i, String str, int i2) {
        Typeface createFromResourcesFontFile = sTypefaceCompatImpl.createFromResourcesFontFile(context, resources, i, str, i2);
        if (createFromResourcesFontFile != null) {
            sTypefaceCache.put(createResourceUid(resources, i, i2), createFromResourcesFontFile);
        }
        return createFromResourcesFontFile;
    }

    private static String createResourceUid(Resources resources, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(resources.getResourcePackageName(i));
        String str = "-";
        sb.append(str);
        sb.append(i);
        sb.append(str);
        sb.append(i2);
        return sb.toString();
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static Typeface findFromCache(@NonNull Resources resources, int i, int i2) {
        return (Typeface) sTypefaceCache.get(createResourceUid(resources, i, i2));
    }

    @Nullable
    private static Typeface getBestFontFromFamily(Context context, Typeface typeface, int i) {
        FontFamilyFilesResourceEntry fontFamily = sTypefaceCompatImpl.getFontFamily(typeface);
        if (fontFamily == null) {
            return null;
        }
        return sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, fontFamily, context.getResources(), i);
    }
}
