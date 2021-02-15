package com.iqiyi.android.qigsaw.core.extension;

import android.content.ContentProvider;
import android.content.ContentProvider.PipeDataWriter;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.util.ArrayList;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class ContentProviderProxy extends ContentProvider {
    private static final String NAME_INFIX = "_Decorated_";
    private static final String TAG = "Split:ContentProviderProxy";
    private ProviderInfo providerInfo;
    private ContentProvider realContentProvider;
    private String realContentProviderClassName;
    private String splitName;

    private ContentProvider createRealContentProvider(ClassLoader classLoader) {
        Context context = getContext();
        String str = TAG;
        if (context != null) {
            String str2 = this.realContentProviderClassName;
            if (str2 != null) {
                ContentProvider contentProvider = (ContentProvider) classLoader.loadClass(str2).newInstance();
                contentProvider.attachInfo(getContext(), this.providerInfo);
                StringBuilder sb = new StringBuilder();
                sb.append("Success to create provider ");
                sb.append(this.realContentProviderClassName);
                SplitLog.d(str, sb.toString(), new Object[0]);
                return contentProvider;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Cause of null context, we can't create real provider ");
        sb2.append(this.realContentProviderClassName);
        SplitLog.w(str, sb2.toString(), new Object[0]);
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void activateRealContentProvider(ClassLoader classLoader) {
        try {
            this.realContentProvider = createRealContentProvider(classLoader);
            e = null;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e = e;
        }
        if (e != null) {
            throw new AABExtensionException(e);
        }
    }

    @NonNull
    public ContentProviderResult[] applyBatch(@NonNull ArrayList arrayList) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.applyBatch(arrayList) : super.applyBatch(arrayList);
    }

    public void attachInfo(Context context, ProviderInfo providerInfo2) {
        String[] split = getClass().getName().split(NAME_INFIX);
        this.realContentProviderClassName = split[0];
        this.splitName = split[1];
        super.attachInfo(context, providerInfo2);
        this.providerInfo = new ProviderInfo(providerInfo2);
        AABExtension.getInstance().put(this.splitName, this);
    }

    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] contentValuesArr) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.bulkInsert(uri, contentValuesArr) : super.bulkInsert(uri, contentValuesArr);
    }

    @Nullable
    public Bundle call(@NonNull String str, @Nullable String str2, @Nullable Bundle bundle) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.call(str, str2, bundle) : super.call(str, str2, bundle);
    }

    @RequiresApi(api = 19)
    @Nullable
    public Uri canonicalize(@NonNull Uri uri) {
        return getRealContentProvider() != null ? this.realContentProvider.canonicalize(uri) : super.canonicalize(uri);
    }

    public abstract boolean checkRealContentProviderInstallStatus(String str);

    public int delete(@NonNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        if (checkRealContentProviderInstallStatus(this.splitName)) {
            return this.realContentProvider.delete(uri, str, strArr);
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public ContentProvider getRealContentProvider() {
        return this.realContentProvider;
    }

    @Nullable
    public String[] getStreamTypes(@NonNull Uri uri, @NonNull String str) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.getStreamTypes(uri, str) : super.getStreamTypes(uri, str);
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        if (checkRealContentProviderInstallStatus(this.splitName)) {
            return this.realContentProvider.getType(uri);
        }
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (checkRealContentProviderInstallStatus(this.splitName)) {
            return this.realContentProvider.insert(uri, contentValues);
        }
        return null;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (checkRealContentProviderInstallStatus(this.splitName)) {
            this.realContentProvider.onConfigurationChanged(configuration);
        }
    }

    public boolean onCreate() {
        return true;
    }

    public void onLowMemory() {
        super.onLowMemory();
        ContentProvider contentProvider = this.realContentProvider;
        if (contentProvider != null) {
            contentProvider.onLowMemory();
        }
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        ContentProvider contentProvider = this.realContentProvider;
        if (contentProvider != null) {
            contentProvider.onTrimMemory(i);
        }
    }

    @Nullable
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String str) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.openAssetFile(uri, str) : super.openAssetFile(uri, str);
    }

    @RequiresApi(api = 19)
    @Nullable
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String str, @Nullable CancellationSignal cancellationSignal) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.openAssetFile(uri, str, cancellationSignal) : super.openAssetFile(uri, str, cancellationSignal);
    }

    @Nullable
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String str) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.openFile(uri, str) : super.openFile(uri, str);
    }

    @RequiresApi(api = 19)
    @Nullable
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String str, @Nullable CancellationSignal cancellationSignal) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.openFile(uri, str, cancellationSignal) : super.openFile(uri, str, cancellationSignal);
    }

    @NonNull
    public ParcelFileDescriptor openPipeHelper(@NonNull Uri uri, @NonNull String str, @Nullable Bundle bundle, @Nullable Object obj, @NonNull PipeDataWriter pipeDataWriter) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.openPipeHelper(uri, str, bundle, obj, pipeDataWriter) : super.openPipeHelper(uri, str, bundle, obj, pipeDataWriter);
    }

    @RequiresApi(api = 19)
    @Nullable
    public AssetFileDescriptor openTypedAssetFile(@NonNull Uri uri, @NonNull String str, @Nullable Bundle bundle) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.openTypedAssetFile(uri, str, bundle) : super.openTypedAssetFile(uri, str, bundle);
    }

    @RequiresApi(api = 19)
    @Nullable
    public AssetFileDescriptor openTypedAssetFile(@NonNull Uri uri, @NonNull String str, @Nullable Bundle bundle, @Nullable CancellationSignal cancellationSignal) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.openTypedAssetFile(uri, str, bundle, cancellationSignal) : super.openTypedAssetFile(uri, str, bundle, cancellationSignal);
    }

    @RequiresApi(api = 26)
    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable Bundle bundle, @Nullable CancellationSignal cancellationSignal) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.query(uri, strArr, bundle, cancellationSignal) : super.query(uri, strArr, bundle, cancellationSignal);
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        if (checkRealContentProviderInstallStatus(this.splitName)) {
            return this.realContentProvider.query(uri, strArr, str, strArr2, str2);
        }
        return null;
    }

    @RequiresApi(api = 16)
    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2, @Nullable CancellationSignal cancellationSignal) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.query(uri, strArr, str, strArr2, str2, cancellationSignal) : super.query(uri, strArr, str, strArr2, str2, cancellationSignal);
    }

    @RequiresApi(api = 26)
    public boolean refresh(Uri uri, @Nullable Bundle bundle, @Nullable CancellationSignal cancellationSignal) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.refresh(uri, bundle, cancellationSignal) : super.refresh(uri, bundle, cancellationSignal);
    }

    @RequiresApi(api = 19)
    @Nullable
    public Uri uncanonicalize(@NonNull Uri uri) {
        return checkRealContentProviderInstallStatus(this.splitName) ? this.realContentProvider.uncanonicalize(uri) : super.uncanonicalize(uri);
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        if (checkRealContentProviderInstallStatus(this.splitName)) {
            return this.realContentProvider.update(uri, contentValues, str, strArr);
        }
        return 0;
    }
}
