package com.google.android.play.core.tasks;

import java.util.concurrent.Executor;

class InvokeFailureListener implements InvocationListener {
    final Object lock = new Object();
    private final Executor mExecutor;
    final OnFailureListener mListener;

    InvokeFailureListener(Executor executor, OnFailureListener onFailureListener) {
        this.mExecutor = executor;
        this.mListener = onFailureListener;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0010, code lost:
        r2.mExecutor.execute(new com.google.android.play.core.tasks.TaskFailureRunnable(r2, r3));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invoke(Task task) {
        if (!task.isSuccessful()) {
            synchronized (this.lock) {
                if (this.mListener == null) {
                }
            }
        }
    }
}
