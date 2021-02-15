package miui.util.async.tasks;

import android.content.ContentValues;
import android.net.Uri;
import miui.util.AppConstants;
import miui.util.async.Task;

public class ContentResolverInsertTask extends Task {
    private String mDescription;
    private final Uri mUri;
    private final ContentValues mValues;

    public ContentResolverInsertTask(Uri uri, ContentValues contentValues) {
        this.mUri = uri;
        this.mValues = contentValues;
    }

    public Uri doLoad() {
        return AppConstants.getCurrentApplication().getContentResolver().insert(this.mUri, this.mValues);
    }

    public String getDescription() {
        if (this.mDescription == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mValues.toString());
            sb.append('@');
            sb.append(this.mUri);
            this.mDescription = sb.toString();
        }
        return this.mDescription;
    }
}
