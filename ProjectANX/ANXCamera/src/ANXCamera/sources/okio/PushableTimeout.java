package okio;

import java.util.concurrent.TimeUnit;

final class PushableTimeout extends Timeout {
    private long originalDeadlineNanoTime;
    private boolean originalHasDeadline;
    private long originalTimeoutNanos;
    private Timeout pushed;

    PushableTimeout() {
    }

    /* access modifiers changed from: 0000 */
    public void pop() {
        this.pushed.timeout(this.originalTimeoutNanos, TimeUnit.NANOSECONDS);
        if (this.originalHasDeadline) {
            this.pushed.deadlineNanoTime(this.originalDeadlineNanoTime);
        } else {
            this.pushed.clearDeadline();
        }
    }

    /* access modifiers changed from: 0000 */
    public void push(Timeout timeout) {
        long j;
        this.pushed = timeout;
        this.originalHasDeadline = timeout.hasDeadline();
        this.originalDeadlineNanoTime = this.originalHasDeadline ? timeout.deadlineNanoTime() : -1;
        this.originalTimeoutNanos = timeout.timeoutNanos();
        timeout.timeout(Timeout.minTimeout(this.originalTimeoutNanos, timeoutNanos()), TimeUnit.NANOSECONDS);
        if (this.originalHasDeadline && hasDeadline()) {
            j = Math.min(deadlineNanoTime(), this.originalDeadlineNanoTime);
        } else if (hasDeadline()) {
            j = deadlineNanoTime();
        } else {
            return;
        }
        timeout.deadlineNanoTime(j);
    }
}
