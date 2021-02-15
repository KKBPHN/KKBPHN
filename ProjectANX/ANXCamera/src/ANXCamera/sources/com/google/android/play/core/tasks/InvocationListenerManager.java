package com.google.android.play.core.tasks;

import java.util.ArrayDeque;
import java.util.Queue;

final class InvocationListenerManager {
    private boolean invoked;
    private final Object lock = new Object();
    private Queue mInvocationListenerQueue;

    InvocationListenerManager() {
    }

    /* access modifiers changed from: 0000 */
    public void addInvocationListener(InvocationListener invocationListener) {
        synchronized (this.lock) {
            if (this.mInvocationListenerQueue == null) {
                this.mInvocationListenerQueue = new ArrayDeque();
            }
            this.mInvocationListenerQueue.add(invocationListener);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0010, code lost:
        r1 = r2.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0012, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r0 = (com.google.android.play.core.tasks.InvocationListener) r2.mInvocationListenerQueue.poll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001b, code lost:
        if (r0 != null) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001d, code lost:
        r2.invoked = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0020, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0021, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0022, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0023, code lost:
        r0.invoke(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invokeListener(Task task) {
        synchronized (this.lock) {
            if (this.mInvocationListenerQueue != null) {
                if (!this.invoked) {
                    this.invoked = true;
                }
            }
        }
    }
}
