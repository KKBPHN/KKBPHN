package com.xiaomi.camera.parallelservice.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.db.DbContainer;
import com.android.camera.db.DbRepository;
import com.android.camera.db.element.SaveTask;
import com.android.camera.db.item.DbItemSaveTask;
import com.android.camera.log.Log;
import com.google.android.apps.photos.api.ProcessingMetadataQuery;
import com.google.android.apps.photos.api.ProcessingMetadataQuery.ProgressStatus;
import java.io.File;
import java.io.FileNotFoundException;
import miui.extension.target.ActivityTarget;

public class ParallelProcessProvider extends ContentProvider {
    private static final int DELETE_BY_ID = 6;
    private static final String MATCH_PATH_DELETE_BY_ID = "delete/#";
    private static final String MATCH_PROCESSING_BY_ID = "processing/#";
    private static final String MATCH_PROCESSING_METADATA = "processing";
    private static final int QUERY_PROCESSING_METADATA = 7;
    private static final int QUERY_PROCESSING_METADATA_BY_ID = 8;
    private static final String TAG = "ParallelProcessProvider";
    private static final int VERSION = 1;
    private DbItemSaveTask dbItemSaveTask;
    private UriMatcher mUriMatcher;

    private int deleteProcessingMetadata(Uri uri) {
        long parseId = ContentUris.parseId(uri);
        SaveTask itemByMediaId = this.dbItemSaveTask.getItemByMediaId(Long.valueOf(parseId));
        StringBuilder sb = new StringBuilder();
        sb.append("deleteProcessingMetadata: mediaStoreId=");
        sb.append(parseId);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (itemByMediaId != null) {
            this.dbItemSaveTask.removeItem(itemByMediaId);
            notifyChange(uri);
            return 1;
        }
        Log.v(str, "deleteProcessingMetadata: no match task found");
        return 0;
    }

    private Uri insertProcessingMetadata(Uri uri, @Nullable Long l, String str) {
        if (l == null || TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("error insert values");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("algo db: insert: ");
        sb.append(l);
        sb.append(" | ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        SaveTask itemByPath = this.dbItemSaveTask.getItemByPath(str);
        itemByPath.setMediaStoreId(l);
        this.dbItemSaveTask.updateItem(itemByPath);
        Uri withAppendedPath = Uri.withAppendedPath(uri, l.toString());
        notifyChange(withAppendedPath);
        return withAppendedPath;
    }

    private ParcelFileDescriptor loadProcessingImage(long j) {
        SaveTask itemByMediaId = this.dbItemSaveTask.getItemByMediaId(Long.valueOf(j));
        if (itemByMediaId != null) {
            return ParcelFileDescriptor.open(new File(itemByMediaId.getPath()), 268435456);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Media removed: ");
        sb.append(j);
        throw new FileNotFoundException(sb.toString());
    }

    private void notifyChange(Uri uri) {
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }

    private Cursor queryProcessingMetadata(@Nullable Long l) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{ProcessingMetadataQuery.MEDIA_STORE_ID, ProcessingMetadataQuery.MEDIA_PATH, ProcessingMetadataQuery.PROGRESS_STATUS, ProcessingMetadataQuery.PROGRESS_PERCENTAGE, ProcessingMetadataQuery.START_TIME, ProcessingMetadataQuery.NO_GAUSSIAN, ProcessingMetadataQuery.PROGRESS_ANIM_TYPE});
        boolean z = l == null;
        for (SaveTask saveTask : this.dbItemSaveTask.getAllItems()) {
            if (z || saveTask.getMediaStoreId() == l) {
                matrixCursor.addRow(new Object[]{saveTask.getMediaStoreId(), saveTask.getPath(), Integer.valueOf(ProgressStatus.INDETERMINATE.getIdentifier()), Integer.valueOf(saveTask.getPercentage()), saveTask.getStartTime(), Integer.valueOf(saveTask.getNoGaussian()), Integer.valueOf(0)});
            }
        }
        matrixCursor.moveToPosition(-1);
        return matrixCursor;
    }

    private Bundle querySpecialTypesVersion() {
        Bundle bundle = new Bundle();
        bundle.putInt("version", 1);
        return bundle;
    }

    private void updateProcessingMetadata(long j, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateProcessingMetadata: mediaStoreId=");
        sb.append(j);
        String sb2 = sb.toString();
        String str = TAG;
        Log.v(str, sb2);
        SaveTask itemByMediaId = this.dbItemSaveTask.getItemByMediaId(Long.valueOf(j));
        if (itemByMediaId != null) {
            itemByMediaId.setPercentage(i);
            this.dbItemSaveTask.updateItem(itemByMediaId);
            return;
        }
        Log.v(str, "updateProcessingMetadata: no match task found");
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        this.mUriMatcher = new UriMatcher(-1);
        this.mUriMatcher.addURI(providerInfo.authority, MATCH_PATH_DELETE_BY_ID, 6);
        this.mUriMatcher.addURI(providerInfo.authority, "processing", 7);
        this.mUriMatcher.addURI(providerInfo.authority, MATCH_PROCESSING_BY_ID, 8);
    }

    @Nullable
    public Bundle call(@NonNull String str, @Nullable String str2, @Nullable Bundle bundle) {
        return TextUtils.equals("version", str) ? querySpecialTypesVersion() : super.call(str, str2, bundle);
    }

    public int delete(@NonNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete uri: ");
        sb.append(uri);
        Log.v(TAG, sb.toString());
        getContext();
        if (this.mUriMatcher.match(uri) == 8) {
            return deleteProcessingMetadata(uri);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unrecognized uri: ");
        sb2.append(uri);
        throw new IllegalArgumentException(sb2.toString());
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert uri: ");
        sb.append(uri);
        Log.v(TAG, sb.toString());
        getContext();
        if (this.mUriMatcher.match(uri) != 7) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unrecognized uri: ");
            sb2.append(uri);
            throw new IllegalArgumentException(sb2.toString());
        } else if (contentValues != null) {
            return insertProcessingMetadata(uri, contentValues.getAsLong(ProcessingMetadataQuery.MEDIA_STORE_ID), contentValues.getAsString(ProcessingMetadataQuery.MEDIA_PATH));
        } else {
            throw new IllegalArgumentException("contentValues is null");
        }
    }

    public boolean onCreate() {
        Log.v(TAG, ActivityTarget.ACTION_ON_CREATE);
        DbContainer.init(getContext());
        this.dbItemSaveTask = DbRepository.dbItemSaveTask();
        return true;
    }

    @Nullable
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String str) {
        if (!"r".equals(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unsupported mode: ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        } else if (this.mUriMatcher.match(uri) == 8) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("loading processing thumb: ");
            sb2.append(uri);
            Log.v(TAG, sb2.toString());
            return loadProcessingImage(ContentUris.parseId(uri));
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unrecognized format: ");
            sb3.append(uri);
            throw new IllegalArgumentException(sb3.toString());
        }
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        Long l;
        StringBuilder sb = new StringBuilder();
        sb.append("query uri: ");
        sb.append(uri);
        Log.v(TAG, sb.toString());
        getContext();
        int match = this.mUriMatcher.match(uri);
        if (match == 7) {
            l = null;
        } else if (match == 8) {
            l = Long.valueOf(ContentUris.parseId(uri));
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unrecognized uri: ");
            sb2.append(uri);
            throw new IllegalArgumentException(sb2.toString());
        }
        return queryProcessingMetadata(l);
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("update uri: ");
        sb.append(uri);
        Log.d(TAG, sb.toString());
        if (this.mUriMatcher.match(uri) != 8) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unrecognized uri: ");
            sb2.append(uri);
            throw new IllegalArgumentException(sb2.toString());
        } else if (contentValues != null) {
            updateProcessingMetadata(ContentUris.parseId(uri), contentValues.getAsInteger(ProcessingMetadataQuery.PROGRESS_PERCENTAGE).intValue());
            notifyChange(uri);
            return 1;
        } else {
            throw new IllegalArgumentException("contentValues is null");
        }
    }
}
