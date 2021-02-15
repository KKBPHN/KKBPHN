package com.google.android.play.core.tasks;

final class TaskSuccessRunnable implements Runnable {
    private final InvokeSuccessListener mSuccessExecutor;
    private final Task mTask;

    TaskSuccessRunnable(InvokeSuccessListener invokeSuccessListener, Task task) {
        this.mSuccessExecutor = invokeSuccessListener;
        this.mTask = task;
    }

    public void run() {
        synchronized (this.mSuccessExecutor.lock) {
            if (this.mSuccessExecutor.mListener != null) {
                this.mSuccessExecutor.mListener.onSuccess(this.mTask.getResult());
            }
        }
    }
}
