package com.google.android.play.core.tasks;

import androidx.annotation.NonNull;
import java.util.concurrent.Executor;

public class TaskExecutor implements Executor {
    public final void execute(@NonNull Runnable runnable) {
        runnable.run();
    }
}
