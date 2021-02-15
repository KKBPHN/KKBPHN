package miui.util.async.tasks;

import android.content.ContentValues;
import android.net.Uri;
import miui.util.AppConstants;
import miui.util.Pools;
import miui.util.async.Task;

public class ContentResolverBulkInsertTask extends Task {
    private String mDescription;
    private final Uri mUri;
    private final ContentValues[] mValues;

    public ContentResolverBulkInsertTask(Uri uri, ContentValues[] contentValuesArr) {
        this.mUri = uri;
        this.mValues = contentValuesArr;
    }

    public Integer doLoad() {
        return Integer.valueOf(AppConstants.getCurrentApplication().getContentResolver().bulkInsert(this.mUri, this.mValues));
    }

    public String getDescription() {
        if (this.mDescription == null) {
            StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
            sb.append('[');
            ContentValues[] contentValuesArr = this.mValues;
            if (contentValuesArr.length == 0) {
                sb.append("NULL");
            } else {
                sb.append(contentValuesArr[0]);
                int length = this.mValues.length;
                for (int i = 1; i < length; i++) {
                    sb.append("; ");
                    sb.append(this.mValues[i]);
                }
            }
            sb.append("]@");
            sb.append(this.mUri);
            this.mDescription = sb.toString();
            Pools.getStringBuilderPool().release(sb);
        }
        return this.mDescription;
    }
}
