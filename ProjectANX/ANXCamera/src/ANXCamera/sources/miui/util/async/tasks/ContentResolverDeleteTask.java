package miui.util.async.tasks;

import android.net.Uri;
import miui.util.AppConstants;
import miui.util.Pools;
import miui.util.async.Task;

public class ContentResolverDeleteTask extends Task {
    private String mDescription;
    private final String[] mSelectionArgs;
    private final Uri mUri;
    private final String mWhere;

    public ContentResolverDeleteTask(Uri uri, String str, String[] strArr) {
        this.mUri = uri;
        this.mWhere = str;
        this.mSelectionArgs = strArr;
    }

    public Integer doLoad() {
        return Integer.valueOf(AppConstants.getCurrentApplication().getContentResolver().delete(this.mUri, this.mWhere, this.mSelectionArgs));
    }

    public String getDescription() {
        if (this.mDescription == null) {
            if (this.mWhere == null) {
                this.mDescription = this.mUri.toString();
            } else {
                StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
                sb.append(this.mUri);
                sb.append(" WHERE ");
                if (this.mSelectionArgs != null) {
                    sb.append('[');
                    sb.append(this.mSelectionArgs[0]);
                    for (int i = 1; i < this.mSelectionArgs.length; i++) {
                        sb.append(';');
                        sb.append(this.mSelectionArgs[i]);
                    }
                    sb.append("]@");
                }
                sb.append(this.mWhere);
                this.mDescription = sb.toString();
                Pools.getStringBuilderPool().release(sb);
            }
        }
        return this.mDescription;
    }
}
