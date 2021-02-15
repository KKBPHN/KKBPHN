package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import miui.text.ExtraTextUtils;

public class AsyncTimeout extends Timeout {
    private static final long IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60);
    private static final long IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
    private static final int TIMEOUT_WRITE_SIZE = 65536;
    @Nullable
    static AsyncTimeout head;
    private boolean inQueue;
    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;

    final class Watchdog extends Thread {
        Watchdog() {
            super("Okio Watchdog");
            setDaemon(true);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
            r0.timedOut();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            while (true) {
                try {
                    synchronized (AsyncTimeout.class) {
                        AsyncTimeout awaitTimeout = AsyncTimeout.awaitTimeout();
                        if (awaitTimeout != null) {
                            if (awaitTimeout == AsyncTimeout.head) {
                                AsyncTimeout.head = null;
                                return;
                            }
                        }
                    }
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    @Nullable
    static AsyncTimeout awaitTimeout() {
        AsyncTimeout asyncTimeout = head.next;
        AsyncTimeout asyncTimeout2 = null;
        long nanoTime = System.nanoTime();
        if (asyncTimeout == null) {
            AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
            if (head.next == null && System.nanoTime() - nanoTime >= IDLE_TIMEOUT_NANOS) {
                asyncTimeout2 = head;
            }
            return asyncTimeout2;
        }
        long remainingNanos = asyncTimeout.remainingNanos(nanoTime);
        if (remainingNanos > 0) {
            long j = remainingNanos / ExtraTextUtils.MB;
            AsyncTimeout.class.wait(j, (int) (remainingNanos - (ExtraTextUtils.MB * j)));
            return null;
        }
        head.next = asyncTimeout.next;
        asyncTimeout.next = null;
        return asyncTimeout;
    }

    private static synchronized boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
        boolean z;
        synchronized (AsyncTimeout.class) {
            AsyncTimeout asyncTimeout2 = head;
            while (true) {
                if (asyncTimeout2 == null) {
                    z = true;
                    break;
                } else if (asyncTimeout2.next == asyncTimeout) {
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    z = false;
                    break;
                } else {
                    asyncTimeout2 = asyncTimeout2.next;
                }
            }
        }
        return z;
    }

    private long remainingNanos(long j) {
        return this.timeoutAt - j;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0052 A[EDGE_INSN: B:33:0x0052->B:22:0x0052 ?: BREAK  
EDGE_INSN: B:33:0x0052->B:22:0x0052 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void scheduleTimeout(AsyncTimeout asyncTimeout, long j, boolean z) {
        AsyncTimeout asyncTimeout2;
        synchronized (AsyncTimeout.class) {
            if (head == null) {
                head = new AsyncTimeout();
                new Watchdog().start();
            }
            long nanoTime = System.nanoTime();
            int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
            if (i != 0 && z) {
                j = Math.min(j, asyncTimeout.deadlineNanoTime() - nanoTime);
            } else if (i == 0) {
                if (z) {
                    asyncTimeout.timeoutAt = asyncTimeout.deadlineNanoTime();
                    long remainingNanos = asyncTimeout.remainingNanos(nanoTime);
                    asyncTimeout2 = head;
                    while (true) {
                        if (asyncTimeout2.next == null) {
                            break;
                        } else if (remainingNanos < asyncTimeout2.next.remainingNanos(nanoTime)) {
                            break;
                        } else {
                            asyncTimeout2 = asyncTimeout2.next;
                        }
                    }
                    asyncTimeout.next = asyncTimeout2.next;
                    asyncTimeout2.next = asyncTimeout;
                    if (asyncTimeout2 == head) {
                        AsyncTimeout.class.notify();
                    }
                } else {
                    throw new AssertionError();
                }
            }
            asyncTimeout.timeoutAt = j + nanoTime;
            long remainingNanos2 = asyncTimeout.remainingNanos(nanoTime);
            asyncTimeout2 = head;
            while (true) {
                if (asyncTimeout2.next == null) {
                }
                asyncTimeout2 = asyncTimeout2.next;
            }
            asyncTimeout.next = asyncTimeout2.next;
            asyncTimeout2.next = asyncTimeout;
            if (asyncTimeout2 == head) {
            }
        }
    }

    public final void enter() {
        if (!this.inQueue) {
            long timeoutNanos = timeoutNanos();
            boolean hasDeadline = hasDeadline();
            if (timeoutNanos != 0 || hasDeadline) {
                this.inQueue = true;
                scheduleTimeout(this, timeoutNanos, hasDeadline);
                return;
            }
            return;
        }
        throw new IllegalStateException("Unbalanced enter/exit");
    }

    /* access modifiers changed from: 0000 */
    public final IOException exit(IOException iOException) {
        return !exit() ? iOException : newTimeoutException(iOException);
    }

    /* access modifiers changed from: 0000 */
    public final void exit(boolean z) {
        if (exit() && z) {
            throw newTimeoutException(null);
        }
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return cancelScheduledTimeout(this);
    }

    /* access modifiers changed from: protected */
    public IOException newTimeoutException(@Nullable IOException iOException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    public final Sink sink(final Sink sink) {
        return new Sink() {
            public void close() {
                AsyncTimeout.this.enter();
                try {
                    sink.close();
                    AsyncTimeout.this.exit(true);
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                } catch (Throwable th) {
                    AsyncTimeout.this.exit(false);
                    throw th;
                }
            }

            public void flush() {
                AsyncTimeout.this.enter();
                try {
                    sink.flush();
                    AsyncTimeout.this.exit(true);
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                } catch (Throwable th) {
                    AsyncTimeout.this.exit(false);
                    throw th;
                }
            }

            public Timeout timeout() {
                return AsyncTimeout.this;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("AsyncTimeout.sink(");
                sb.append(sink);
                sb.append(")");
                return sb.toString();
            }

            public void write(Buffer buffer, long j) {
                Util.checkOffsetAndCount(buffer.size, 0, j);
                while (true) {
                    long j2 = 0;
                    if (j > 0) {
                        Segment segment = buffer.head;
                        while (true) {
                            if (j2 >= 65536) {
                                break;
                            }
                            j2 += (long) (segment.limit - segment.pos);
                            if (j2 >= j) {
                                j2 = j;
                                break;
                            }
                            segment = segment.next;
                        }
                        AsyncTimeout.this.enter();
                        try {
                            sink.write(buffer, j2);
                            j -= j2;
                            AsyncTimeout.this.exit(true);
                        } catch (IOException e) {
                            throw AsyncTimeout.this.exit(e);
                        } catch (Throwable th) {
                            AsyncTimeout.this.exit(false);
                            throw th;
                        }
                    } else {
                        return;
                    }
                }
            }
        };
    }

    public final Source source(final Source source) {
        return new Source() {
            public void close() {
                AsyncTimeout.this.enter();
                try {
                    source.close();
                    AsyncTimeout.this.exit(true);
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                } catch (Throwable th) {
                    AsyncTimeout.this.exit(false);
                    throw th;
                }
            }

            public long read(Buffer buffer, long j) {
                AsyncTimeout.this.enter();
                try {
                    long read = source.read(buffer, j);
                    AsyncTimeout.this.exit(true);
                    return read;
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                } catch (Throwable th) {
                    AsyncTimeout.this.exit(false);
                    throw th;
                }
            }

            public Timeout timeout() {
                return AsyncTimeout.this;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("AsyncTimeout.source(");
                sb.append(source);
                sb.append(")");
                return sb.toString();
            }
        };
    }

    /* access modifiers changed from: protected */
    public void timedOut() {
    }
}
