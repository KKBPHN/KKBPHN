package com.google.android.play.core.remote;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.android.play.core.tasks.TaskWrapper;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class RemoteTask implements Runnable {
    private final TaskWrapper task;

    RemoteTask() {
        this.task = null;
    }

    public RemoteTask(TaskWrapper taskWrapper) {
        this.task = taskWrapper;
    }

    public abstract void execute();

    /* access modifiers changed from: 0000 */
    public final TaskWrapper getTask() {
        return this.task;
    }

    public final void run() {
        try {
            execute();
        } catch (Exception e) {
            e.printStackTrace();
            TaskWrapper taskWrapper = this.task;
            if (taskWrapper != null) {
                taskWrapper.setException(e);
            }
        }
    }
}
