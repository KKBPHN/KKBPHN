package com.google.android.play.core.tasks;

import java.util.concurrent.Executor;

final class InvokeSuccessListener implements InvocationListener {
    final Object lock = new Object();
    private final Executor mExecutor;
    final OnSuccessListener mListener;

    InvokeSuccessListener(Executor executor, OnSuccessListener onSuccessListener) {
        this.mExecutor = executor;
        this.mListener = onSuccessListener;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0010, code lost:
        r2.mExecutor.execute(new com.google.android.play.core.tasks.TaskSuccessRunnable(r2, r3));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invoke(Task task) {
        if (task.isSuccessful()) {
            synchronized (this.lock) {
                if (this.mListener == null) {
                }
            }
        }
    }
}
