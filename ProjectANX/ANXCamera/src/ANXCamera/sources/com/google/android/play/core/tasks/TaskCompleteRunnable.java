package com.google.android.play.core.tasks;

final class TaskCompleteRunnable implements Runnable {
    private final InvokeCompleteListener mCompleteExecutor;
    private final Task mTask;

    TaskCompleteRunnable(InvokeCompleteListener invokeCompleteListener, Task task) {
        this.mCompleteExecutor = invokeCompleteListener;
        this.mTask = task;
    }

    public void run() {
        synchronized (this.mCompleteExecutor.lock) {
            if (this.mCompleteExecutor.mListener != null) {
                this.mCompleteExecutor.mListener.onComplete(this.mTask);
            }
        }
    }
}
