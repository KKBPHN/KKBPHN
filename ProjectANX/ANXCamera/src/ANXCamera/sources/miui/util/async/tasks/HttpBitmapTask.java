package miui.util.async.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.util.Map;
import miui.net.http.HttpResponse;
import miui.net.http.HttpSession;
import miui.util.async.Cacheable;
import miui.util.async.tasks.HttpTask.Method;

public class HttpBitmapTask extends HttpTask implements Cacheable {
    private Options mDecodeOptions;

    public HttpBitmapTask(String str) {
        this(null, Method.Get, str, null, null);
    }

    public HttpBitmapTask(HttpSession httpSession, String str) {
        this(httpSession, Method.Get, str, null, null);
    }

    public HttpBitmapTask(HttpSession httpSession, Method method, String str, Map map) {
        this(httpSession, method, str, map, null);
    }

    public HttpBitmapTask(HttpSession httpSession, Method method, String str, Map map, Options options) {
        super(httpSession, method, str, map);
        this.mDecodeOptions = options;
    }

    public Bitmap doLoad() {
        HttpResponse request = request();
        FileBitmapTask.DECODE_LIMITATION.acquireUninterruptibly();
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(request.getContent(), null, this.mDecodeOptions);
            return decodeStream;
        } finally {
            request.release();
            FileBitmapTask.DECODE_LIMITATION.release();
        }
    }

    public String getCacheKey() {
        return getDescription();
    }

    public int sizeOf(Object obj) {
        if (obj instanceof Bitmap) {
            return ((Bitmap) obj).getByteCount();
        }
        return 0;
    }
}
