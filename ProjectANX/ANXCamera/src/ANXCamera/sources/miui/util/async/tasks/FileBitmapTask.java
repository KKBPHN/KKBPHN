package miui.util.async.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.util.concurrent.Semaphore;
import miui.util.async.Cacheable;
import miui.util.async.Task;

public class FileBitmapTask extends Task implements Cacheable {
    static final Semaphore DECODE_LIMITATION = new Semaphore(2);
    private Options mDecodeOptions;
    private String mFilePath;

    public FileBitmapTask(String str) {
        this(str, null);
    }

    public FileBitmapTask(String str, Options options) {
        this.mFilePath = str;
        this.mDecodeOptions = options;
    }

    public Bitmap doLoad() {
        DECODE_LIMITATION.acquireUninterruptibly();
        try {
            return BitmapFactory.decodeFile(this.mFilePath, this.mDecodeOptions);
        } finally {
            DECODE_LIMITATION.release();
        }
    }

    public String getCacheKey() {
        return this.mFilePath;
    }

    public String getDescription() {
        return this.mFilePath;
    }

    public int sizeOf(Object obj) {
        if (obj instanceof Bitmap) {
            return ((Bitmap) obj).getByteCount();
        }
        return 0;
    }
}
