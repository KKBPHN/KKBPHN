package miui.util.async.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import miui.util.AppConstants;
import miui.util.async.Cacheable;
import miui.util.async.Task;

public class ResourceBitmapTask extends Task implements Cacheable {
    private Options mDecodeOptions;
    private String mDescription;
    private int mResId;

    public ResourceBitmapTask(int i) {
        this(i, null);
    }

    public ResourceBitmapTask(int i, Options options) {
        this.mResId = i;
        this.mDecodeOptions = options;
    }

    public Bitmap doLoad() {
        FileBitmapTask.DECODE_LIMITATION.acquireUninterruptibly();
        try {
            return BitmapFactory.decodeResource(AppConstants.getCurrentApplication().getResources(), this.mResId, this.mDecodeOptions);
        } finally {
            FileBitmapTask.DECODE_LIMITATION.release();
        }
    }

    public String getCacheKey() {
        return getDescription();
    }

    public String getDescription() {
        if (this.mDescription == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("resource_");
            sb.append(this.mResId);
            this.mDescription = sb.toString();
        }
        return this.mDescription;
    }

    public int sizeOf(Object obj) {
        if (obj instanceof Bitmap) {
            return ((Bitmap) obj).getByteCount();
        }
        return 0;
    }
}
