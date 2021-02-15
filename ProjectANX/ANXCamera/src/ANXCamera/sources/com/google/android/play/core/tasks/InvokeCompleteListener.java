package com.google.android.play.core.tasks;

import java.util.concurrent.Executor;

class InvokeCompleteListener implements InvocationListener {
    final Object lock = new Object();
    private final Executor mExecutor;
    final OnCompleteListener mListener;

    InvokeCompleteListener(Executor executor, OnCompleteListener onCompleteListener) {
        this.mExecutor = executor;
        this.mListener = onCompleteListener;
    }

    public void invoke(Task task) {
        synchronized (this.lock) {
            if (this.mListener != null) {
                this.mExecutor.execute(new TaskCompleteRunnable(this, task));
            }
        }
    }
}
