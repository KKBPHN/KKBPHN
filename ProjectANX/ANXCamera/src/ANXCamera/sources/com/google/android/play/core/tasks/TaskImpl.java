package com.google.android.play.core.tasks;

import androidx.annotation.NonNull;
import java.util.concurrent.Executor;

class TaskImpl extends Task {
    private boolean isComplete;
    private final Object lock = new Object();
    private Exception mException;
    private InvocationListenerManager mListenerManager = new InvocationListenerManager();
    private Object mResult;

    TaskImpl() {
    }

    private void assertComplete() {
        if (!this.isComplete) {
            throw new RuntimeException("Task is not yet complete");
        }
    }

    private void invokeListeners() {
        synchronized (this.lock) {
            if (this.isComplete) {
                this.mListenerManager.invokeListener(this);
            }
        }
    }

    public Task addOnCompleteListener(OnCompleteListener onCompleteListener) {
        return addOnCompleteListener(TaskExecutors.MAIN_THREAD, onCompleteListener);
    }

    public Task addOnCompleteListener(Executor executor, OnCompleteListener onCompleteListener) {
        this.mListenerManager.addInvocationListener(new InvokeCompleteListener(executor, onCompleteListener));
        invokeListeners();
        return this;
    }

    public Task addOnFailureListener(OnFailureListener onFailureListener) {
        return addOnFailureListener(TaskExecutors.MAIN_THREAD, onFailureListener);
    }

    public Task addOnFailureListener(Executor executor, OnFailureListener onFailureListener) {
        this.mListenerManager.addInvocationListener(new InvokeFailureListener(executor, onFailureListener));
        invokeListeners();
        return this;
    }

    public Task addOnSuccessListener(OnSuccessListener onSuccessListener) {
        return addOnSuccessListener(TaskExecutors.MAIN_THREAD, onSuccessListener);
    }

    public Task addOnSuccessListener(Executor executor, OnSuccessListener onSuccessListener) {
        this.mListenerManager.addInvocationListener(new InvokeSuccessListener(executor, onSuccessListener));
        invokeListeners();
        return this;
    }

    public Exception getException() {
        Exception exc;
        synchronized (this.lock) {
            exc = this.mException;
        }
        return exc;
    }

    public Object getResult() {
        Object obj;
        synchronized (this.lock) {
            assertComplete();
            if (this.mException == null) {
                obj = this.mResult;
            } else {
                throw new RuntimeExecutionException(this.mException);
            }
        }
        return obj;
    }

    public Object getResult(Class cls) {
        return null;
    }

    public boolean isComplete() {
        boolean z;
        synchronized (this.lock) {
            z = this.isComplete;
        }
        return z;
    }

    public boolean isSuccessful() {
        boolean z;
        synchronized (this.lock) {
            z = this.isComplete && this.mException == null;
        }
        return z;
    }

    public boolean setException(@NonNull Exception exc) {
        synchronized (this.lock) {
            if (this.isComplete) {
                return false;
            }
            this.isComplete = true;
            this.mException = exc;
            this.mListenerManager.invokeListener(this);
            return true;
        }
    }

    public boolean setResult(Object obj) {
        synchronized (this.lock) {
            if (this.isComplete) {
                return false;
            }
            this.isComplete = true;
            this.mResult = obj;
            this.mListenerManager.invokeListener(this);
            return true;
        }
    }

    public final void setResultCheck(Object obj) {
        synchronized (this.lock) {
            if (!this.isComplete) {
                this.isComplete = true;
                this.mResult = obj;
            } else {
                throw new RuntimeException("Task is already complete");
            }
        }
        this.mListenerManager.invokeListener(this);
    }
}
