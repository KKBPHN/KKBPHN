package androidx.core.provider;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import androidx.annotation.GuardedBy;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.VisibleForTesting;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat.FontCallback;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.provider.SelfDestructiveThread.ReplyCallback;
import androidx.core.util.Preconditions;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FontsContractCompat {
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static final String PARCEL_FONT_RESULTS = "font_results";
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final SelfDestructiveThread sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
    private static final Comparator sByteArrayComparator = new Comparator() {
        public int compare(byte[] bArr, byte[] bArr2) {
            int i;
            int i2;
            if (bArr.length != bArr2.length) {
                i2 = bArr.length;
                i = bArr2.length;
            } else {
                int i3 = 0;
                while (i3 < bArr.length) {
                    if (bArr[i3] != bArr2[i3]) {
                        i2 = bArr[i3];
                        i = bArr2[i3];
                    } else {
                        i3++;
                    }
                }
                return 0;
            }
            return i2 - i;
        }
    };
    @GuardedBy("sLock")
    private static Executor sExecutor;
    static final Object sLock = new Object();
    @GuardedBy("sLock")
    static final SimpleArrayMap sPendingReplies = new SimpleArrayMap();
    static final LruCache sTypefaceCache = new LruCache(16);

    public final class Columns implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }

    public class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public FontFamilyResult(int i, @Nullable FontInfo[] fontInfoArr) {
            this.mStatusCode = i;
            this.mFonts = fontInfoArr;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }
    }

    public class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public FontInfo(@NonNull Uri uri, @IntRange(from = 0) int i, @IntRange(from = 1, to = 1000) int i2, boolean z, int i3) {
            Preconditions.checkNotNull(uri);
            this.mUri = uri;
            this.mTtcIndex = i;
            this.mWeight = i2;
            this.mItalic = z;
            this.mResultCode = i3;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        @IntRange(from = 0)
        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        @NonNull
        public Uri getUri() {
            return this.mUri;
        }

        @IntRange(from = 1, to = 1000)
        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    public class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public static final int RESULT_OK = 0;

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        @Retention(RetentionPolicy.SOURCE)
        public @interface FontRequestFailReason {
        }

        public void onTypefaceRequestFailed(int i) {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }
    }

    interface OnCompletedCallback {
        void onCompleted(Object obj);
    }

    final class OnFetchCompletedAndFirePendingReplyCallback implements OnCompletedCallback {
        @NonNull
        private final String mCacheId;

        OnFetchCompletedAndFirePendingReplyCallback(@NonNull String str) {
            this.mCacheId = str;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
            if (r3 >= r1.size()) goto L_0x002c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
            ((androidx.core.provider.SelfDestructiveThread.ReplyCallback) r1.get(r3)).onReply(r4);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0019, code lost:
            r3 = 0;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted(TypefaceResult typefaceResult) {
            synchronized (FontsContractCompat.sLock) {
                ArrayList arrayList = (ArrayList) FontsContractCompat.sPendingReplies.get(this.mCacheId);
                if (arrayList != null) {
                    FontsContractCompat.sPendingReplies.remove(this.mCacheId);
                }
            }
        }
    }

    final class SyncFontFetchTask extends FutureTask {

        final class CallableWrapper implements Callable {
            private final Callable mOriginalCallback;
            private final OnCompletedCallback mTypefaceResultOnCompletedCallback;

            CallableWrapper(@NonNull Callable callable, @NonNull OnCompletedCallback onCompletedCallback) {
                this.mOriginalCallback = callable;
                this.mTypefaceResultOnCompletedCallback = onCompletedCallback;
            }

            public TypefaceResult call() {
                TypefaceResult typefaceResult = (TypefaceResult) this.mOriginalCallback.call();
                this.mTypefaceResultOnCompletedCallback.onCompleted(typefaceResult);
                return typefaceResult;
            }
        }

        SyncFontFetchTask(@NonNull SyncFontFetchTaskCallable syncFontFetchTaskCallable) {
            super(syncFontFetchTaskCallable);
        }

        SyncFontFetchTask(@NonNull SyncFontFetchTaskCallable syncFontFetchTaskCallable, @NonNull OnCompletedCallback onCompletedCallback) {
            super(new CallableWrapper(syncFontFetchTaskCallable, onCompletedCallback));
        }
    }

    final class SyncFontFetchTaskCallable implements Callable {
        @NonNull
        private final Context mAppContext;
        @NonNull
        private final String mCacheId;
        @NonNull
        private final FontRequest mRequest;
        private final int mStyle;

        SyncFontFetchTaskCallable(@NonNull Context context, @NonNull FontRequest fontRequest, int i, @NonNull String str) {
            this.mCacheId = str;
            this.mAppContext = context.getApplicationContext();
            this.mRequest = fontRequest;
            this.mStyle = i;
        }

        public TypefaceResult call() {
            TypefaceResult fontInternal = FontsContractCompat.getFontInternal(this.mAppContext, this.mRequest, this.mStyle);
            Typeface typeface = fontInternal.mTypeface;
            if (typeface != null) {
                FontsContractCompat.sTypefaceCache.put(this.mCacheId, typeface);
            }
            return fontInternal;
        }
    }

    final class TypefaceResult {
        final int mResult;
        final Typeface mTypeface;

        TypefaceResult(@Nullable Typeface typeface, int i) {
            this.mTypeface = typeface;
            this.mResult = i;
        }
    }

    private FontsContractCompat() {
    }

    @Nullable
    public static Typeface buildTypeface(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] fontInfoArr) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, fontInfoArr, 0);
    }

    private static List convertToByteArrayList(Signature[] signatureArr) {
        ArrayList arrayList = new ArrayList();
        for (Signature byteArray : signatureArr) {
            arrayList.add(byteArray.toByteArray());
        }
        return arrayList;
    }

    private static boolean equalsByteArrayList(List list, List list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!Arrays.equals((byte[]) list.get(i), (byte[]) list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    @NonNull
    public static FontFamilyResult fetchFonts(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontRequest fontRequest) {
        ProviderInfo provider = getProvider(context.getPackageManager(), fontRequest, context.getResources());
        return provider == null ? new FontFamilyResult(1, null) : new FontFamilyResult(0, getFontFromProvider(context, fontRequest, provider.authority, cancellationSignal));
    }

    private static List getCertificates(FontRequest fontRequest, Resources resources) {
        return fontRequest.getCertificates() != null ? fontRequest.getCertificates() : FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }

    /* JADX INFO: finally extract failed */
    @VisibleForTesting
    @NonNull
    static FontInfo[] getFontFromProvider(Context context, FontRequest fontRequest, String str, CancellationSignal cancellationSignal) {
        String str2 = str;
        ArrayList arrayList = new ArrayList();
        String str3 = "content";
        Uri build = new Builder().scheme(str3).authority(str2).build();
        Uri build2 = new Builder().scheme(str3).authority(str2).appendPath(ComposerHelper.COMPOSER_PATH).build();
        Cursor cursor = null;
        try {
            Cursor query = VERSION.SDK_INT > 16 ? context.getContentResolver().query(build, new String[]{"_id", Columns.FILE_ID, Columns.TTC_INDEX, Columns.VARIATION_SETTINGS, Columns.WEIGHT, Columns.ITALIC, Columns.RESULT_CODE}, "query = ?", new String[]{fontRequest.getQuery()}, null, cancellationSignal) : context.getContentResolver().query(build, new String[]{"_id", Columns.FILE_ID, Columns.TTC_INDEX, Columns.VARIATION_SETTINGS, Columns.WEIGHT, Columns.ITALIC, Columns.RESULT_CODE}, "query = ?", new String[]{fontRequest.getQuery()}, null);
            if (query != null && query.getCount() > 0) {
                int columnIndex = query.getColumnIndex(Columns.RESULT_CODE);
                ArrayList arrayList2 = new ArrayList();
                int columnIndex2 = query.getColumnIndex("_id");
                int columnIndex3 = query.getColumnIndex(Columns.FILE_ID);
                int columnIndex4 = query.getColumnIndex(Columns.TTC_INDEX);
                int columnIndex5 = query.getColumnIndex(Columns.WEIGHT);
                int columnIndex6 = query.getColumnIndex(Columns.ITALIC);
                while (query.moveToNext()) {
                    int i = columnIndex != -1 ? query.getInt(columnIndex) : 0;
                    int i2 = columnIndex4 != -1 ? query.getInt(columnIndex4) : 0;
                    Uri withAppendedId = columnIndex3 == -1 ? ContentUris.withAppendedId(build, query.getLong(columnIndex2)) : ContentUris.withAppendedId(build2, query.getLong(columnIndex3));
                    int i3 = columnIndex5 != -1 ? query.getInt(columnIndex5) : 400;
                    boolean z = columnIndex6 != -1 && query.getInt(columnIndex6) == 1;
                    FontInfo fontInfo = new FontInfo(withAppendedId, i2, i3, z, i);
                    arrayList2.add(fontInfo);
                }
                arrayList = arrayList2;
            }
            if (query != null) {
                query.close();
            }
            return (FontInfo[]) arrayList.toArray(new FontInfo[0]);
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    @NonNull
    static TypefaceResult getFontInternal(Context context, FontRequest fontRequest, int i) {
        try {
            FontFamilyResult fetchFonts = fetchFonts(context, null, fontRequest);
            int i2 = -3;
            if (fetchFonts.getStatusCode() == 0) {
                Typeface createFromFontInfo = TypefaceCompat.createFromFontInfo(context, null, fetchFonts.getFonts(), i);
                if (createFromFontInfo != null) {
                    i2 = 0;
                }
                return new TypefaceResult(createFromFontInfo, i2);
            }
            if (fetchFonts.getStatusCode() == 1) {
                i2 = -2;
            }
            return new TypefaceResult(null, i2);
        } catch (NameNotFoundException unused) {
            return new TypefaceResult(null, -1);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static Typeface getFontSync(Context context, FontRequest fontRequest, @Nullable FontCallback fontCallback, @Nullable Handler handler, boolean z, int i, int i2) {
        return getFontSync(context, fontRequest, fontCallback, handler, z, i, i2, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00b0, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00c1, code lost:
        if (r10 != null) goto L_0x00ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00c3, code lost:
        sBackgroundThread.postAndReply(r2, new androidx.core.provider.FontsContractCompat.AnonymousClass3());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00ce, code lost:
        r10.execute(new androidx.core.provider.FontsContractCompat.SyncFontFetchTask(r2, new androidx.core.provider.FontsContractCompat.OnFetchCompletedAndFirePendingReplyCallback(r0)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00db, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @RestrictTo({Scope.LIBRARY})
    public static Typeface getFontSync(Context context, FontRequest fontRequest, @Nullable final FontCallback fontCallback, @Nullable final Handler handler, boolean z, int i, int i2, boolean z2) {
        Executor executor;
        StringBuilder sb = new StringBuilder();
        sb.append(fontRequest.getIdentifier());
        sb.append("-");
        sb.append(i2);
        final String sb2 = sb.toString();
        Typeface typeface = (Typeface) sTypefaceCache.get(sb2);
        if (typeface != null) {
            if (fontCallback != null) {
                fontCallback.onFontRetrieved(typeface);
            }
            return typeface;
        } else if (!z || i != -1) {
            if (!z2 || handler != null) {
                executor = null;
            } else {
                if (sExecutor == null) {
                    synchronized (sLock) {
                        if (sExecutor == null) {
                            sExecutor = Executors.newFixedThreadPool(1);
                        }
                    }
                }
                executor = sExecutor;
            }
            SyncFontFetchTaskCallable syncFontFetchTaskCallable = new SyncFontFetchTaskCallable(context, fontRequest, i2, sb2);
            if (!z) {
                Object obj = fontCallback != null ? executor == null ? new ReplyCallback() {
                    public void onReply(TypefaceResult typefaceResult) {
                        int i;
                        FontCallback fontCallback;
                        if (typefaceResult == null) {
                            fontCallback = FontCallback.this;
                            i = 1;
                        } else {
                            i = typefaceResult.mResult;
                            if (i == 0) {
                                FontCallback.this.callbackSuccessAsync(typefaceResult.mTypeface, handler);
                                return;
                            }
                            fontCallback = FontCallback.this;
                        }
                        fontCallback.callbackFailAsync(i, handler);
                    }
                } : new ReplyCallback() {
                    public void onReply(TypefaceResult typefaceResult) {
                        if (typefaceResult == null) {
                            FontCallback.this.onFontRetrievalFailed(1);
                            return;
                        }
                        int i = typefaceResult.mResult;
                        FontCallback fontCallback = FontCallback.this;
                        if (i == 0) {
                            fontCallback.onFontRetrieved(typefaceResult.mTypeface);
                        } else {
                            fontCallback.onFontRetrievalFailed(i);
                        }
                    }
                } : null;
                synchronized (sLock) {
                    ArrayList arrayList = (ArrayList) sPendingReplies.get(sb2);
                    if (arrayList != null) {
                        if (obj != null) {
                            arrayList.add(obj);
                        }
                    } else if (obj != null) {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(obj);
                        sPendingReplies.put(sb2, arrayList2);
                    }
                }
            } else if (executor == null) {
                try {
                    return ((TypefaceResult) sBackgroundThread.postAndWait(syncFontFetchTaskCallable, i)).mTypeface;
                } catch (InterruptedException | ExecutionException | TimeoutException unused) {
                    return null;
                }
            } else {
                SyncFontFetchTask syncFontFetchTask = new SyncFontFetchTask(syncFontFetchTaskCallable);
                executor.execute(syncFontFetchTask);
                return ((TypefaceResult) syncFontFetchTask.get((long) i, TimeUnit.MILLISECONDS)).mTypeface;
            }
        } else {
            TypefaceResult fontInternal = getFontInternal(context, fontRequest, i2);
            if (fontCallback != null) {
                int i3 = fontInternal.mResult;
                if (i3 == 0) {
                    fontCallback.callbackSuccessAsync(fontInternal.mTypeface, handler);
                } else {
                    fontCallback.callbackFailAsync(i3, handler);
                }
            }
            return fontInternal.mTypeface;
        }
    }

    @VisibleForTesting
    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static ProviderInfo getProvider(@NonNull PackageManager packageManager, @NonNull FontRequest fontRequest, @Nullable Resources resources) {
        String providerAuthority = fontRequest.getProviderAuthority();
        ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(providerAuthority, 0);
        if (resolveContentProvider == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("No package found for authority: ");
            sb.append(providerAuthority);
            throw new NameNotFoundException(sb.toString());
        } else if (resolveContentProvider.packageName.equals(fontRequest.getProviderPackage())) {
            List convertToByteArrayList = convertToByteArrayList(packageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
            Collections.sort(convertToByteArrayList, sByteArrayComparator);
            List certificates = getCertificates(fontRequest, resources);
            for (int i = 0; i < certificates.size(); i++) {
                ArrayList arrayList = new ArrayList((Collection) certificates.get(i));
                Collections.sort(arrayList, sByteArrayComparator);
                if (equalsByteArrayList(convertToByteArrayList, arrayList)) {
                    return resolveContentProvider;
                }
            }
            return null;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Found content provider ");
            sb2.append(providerAuthority);
            sb2.append(", but package was not ");
            sb2.append(fontRequest.getProviderPackage());
            throw new NameNotFoundException(sb2.toString());
        }
    }

    @RequiresApi(19)
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static Map prepareFontData(Context context, FontInfo[] fontInfoArr, CancellationSignal cancellationSignal) {
        HashMap hashMap = new HashMap();
        for (FontInfo fontInfo : fontInfoArr) {
            if (fontInfo.getResultCode() == 0) {
                Uri uri = fontInfo.getUri();
                if (!hashMap.containsKey(uri)) {
                    hashMap.put(uri, TypefaceCompatUtil.mmap(context, cancellationSignal, uri));
                }
            }
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public static void requestFont(@NonNull Context context, @NonNull FontRequest fontRequest, @NonNull FontRequestCallback fontRequestCallback, @NonNull Handler handler) {
        requestFontInternal(context.getApplicationContext(), fontRequest, fontRequestCallback, handler);
    }

    private static void requestFontInternal(@NonNull final Context context, @NonNull final FontRequest fontRequest, @NonNull final FontRequestCallback fontRequestCallback, @NonNull Handler handler) {
        final Handler handler2 = new Handler();
        handler.post(new Runnable() {
            public void run() {
                try {
                    FontFamilyResult fetchFonts = FontsContractCompat.fetchFonts(context, null, fontRequest);
                    if (fetchFonts.getStatusCode() != 0) {
                        int statusCode = fetchFonts.getStatusCode();
                        if (statusCode == 1) {
                            handler2.post(new Runnable() {
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-2);
                                }
                            });
                        } else if (statusCode != 2) {
                            handler2.post(new Runnable() {
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-3);
                                }
                            });
                        } else {
                            handler2.post(new Runnable() {
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-3);
                                }
                            });
                        }
                    } else {
                        FontInfo[] fonts = fetchFonts.getFonts();
                        if (fonts == null || fonts.length == 0) {
                            handler2.post(new Runnable() {
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(1);
                                }
                            });
                            return;
                        }
                        for (FontInfo fontInfo : fonts) {
                            if (fontInfo.getResultCode() != 0) {
                                final int resultCode = fontInfo.getResultCode();
                                if (resultCode < 0) {
                                    handler2.post(new Runnable() {
                                        public void run() {
                                            fontRequestCallback.onTypefaceRequestFailed(-3);
                                        }
                                    });
                                } else {
                                    handler2.post(new Runnable() {
                                        public void run() {
                                            fontRequestCallback.onTypefaceRequestFailed(resultCode);
                                        }
                                    });
                                }
                                return;
                            }
                        }
                        final Typeface buildTypeface = FontsContractCompat.buildTypeface(context, null, fonts);
                        if (buildTypeface == null) {
                            handler2.post(new Runnable() {
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-3);
                                }
                            });
                        } else {
                            handler2.post(new Runnable() {
                                public void run() {
                                    fontRequestCallback.onTypefaceRetrieved(buildTypeface);
                                }
                            });
                        }
                    }
                } catch (NameNotFoundException unused) {
                    handler2.post(new Runnable() {
                        public void run() {
                            fontRequestCallback.onTypefaceRequestFailed(-1);
                        }
                    });
                }
            }
        });
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static void resetCache() {
        sTypefaceCache.evictAll();
    }
}
