package miui.net.http;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.net.ssl.SSLException;

public class BaseRetryStrategy implements RetryStrategy {
    private static ArrayList BLACK_LIST = new ArrayList();
    public static final float DEFAULT_BACKOFF_MULT = 1.0f;
    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final int DEFAULT_TIMEOUT = 10000;
    private final float mBackoffMultiplier;
    private int mCurrentRetryCount;
    private int mCurrentTimeout;
    private final int mMaxNumRetries;

    static {
        BLACK_LIST.add(InterruptedIOException.class);
        BLACK_LIST.add(SSLException.class);
    }

    public BaseRetryStrategy() {
        this(10000, 3, 1.0f);
    }

    public BaseRetryStrategy(int i, int i2, float f) {
        this.mCurrentTimeout = i;
        this.mMaxNumRetries = i2;
        this.mBackoffMultiplier = f;
    }

    public int getCurrentRetryCount() {
        return this.mCurrentRetryCount;
    }

    public int getCurrentTimeout() {
        return this.mCurrentTimeout;
    }

    /* access modifiers changed from: protected */
    public boolean hasAttemptRemaining() {
        return this.mCurrentRetryCount <= this.mMaxNumRetries;
    }

    /* access modifiers changed from: protected */
    public boolean isUnretryThrowable(Throwable th) {
        Iterator it = BLACK_LIST.iterator();
        while (it.hasNext()) {
            if (((Class) it.next()).isInstance(th)) {
                return true;
            }
        }
        return false;
    }

    public boolean retry(Throwable th) {
        this.mCurrentRetryCount++;
        int i = this.mCurrentTimeout;
        this.mCurrentTimeout = (int) (((float) i) + (((float) i) * this.mBackoffMultiplier));
        return hasAttemptRemaining() && isUnretryThrowable(th);
    }
}
