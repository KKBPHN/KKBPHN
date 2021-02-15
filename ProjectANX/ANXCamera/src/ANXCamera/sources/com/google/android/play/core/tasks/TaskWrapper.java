package com.google.android.play.core.tasks;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class TaskWrapper {
    private final TaskImpl mTask = new TaskImpl();

    public final Task getTask() {
        return this.mTask;
    }

    public final boolean setException(Exception exc) {
        return this.mTask.setException(exc);
    }

    public final boolean setResult(Object obj) {
        return this.mTask.setResult(obj);
    }
}
