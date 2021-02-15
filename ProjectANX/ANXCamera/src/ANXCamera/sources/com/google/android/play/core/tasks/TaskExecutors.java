package com.google.android.play.core.tasks;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.Executor;

public class TaskExecutors {
    public static final Executor MAIN_THREAD = new MainThreadExecutor();
    static final Executor sExecutor = new TaskExecutor();

    final class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        MainThreadExecutor() {
        }

        public void execute(@NonNull Runnable runnable) {
            this.mainThreadHandler.post(runnable);
        }
    }
}
