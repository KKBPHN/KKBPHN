package com.google.android.play.core.tasks;

final class TaskFailureRunnable implements Runnable {
    private final InvokeFailureListener mFailureExecutor;
    private final Task mTask;

    TaskFailureRunnable(InvokeFailureListener invokeFailureListener, Task task) {
        this.mFailureExecutor = invokeFailureListener;
        this.mTask = task;
    }

    public void run() {
        synchronized (this.mFailureExecutor.lock) {
            if (this.mFailureExecutor.mListener != null) {
                this.mFailureExecutor.mListener.onFailure(this.mTask.getException());
            }
        }
    }
}
