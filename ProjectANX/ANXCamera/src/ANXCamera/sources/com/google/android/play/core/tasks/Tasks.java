package com.google.android.play.core.tasks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Tasks {

    class AwaitTaskListener implements OnFailureListener, OnSuccessListener {
        private final CountDownLatch countDownLatch;

        private AwaitTaskListener() {
            this.countDownLatch = new CountDownLatch(1);
        }

        /* access modifiers changed from: 0000 */
        public void await() {
            this.countDownLatch.await();
        }

        /* access modifiers changed from: 0000 */
        public boolean awaitTimeout(long j, TimeUnit timeUnit) {
            return this.countDownLatch.await(j, timeUnit);
        }

        public void onFailure(Exception exc) {
            this.countDownLatch.countDown();
        }

        public void onSuccess(Object obj) {
            this.countDownLatch.countDown();
        }
    }

    private Tasks() {
    }

    private static void addTaskListener(Task task, AwaitTaskListener awaitTaskListener) {
        task.addOnSuccessListener(TaskExecutors.sExecutor, awaitTaskListener);
        task.addOnFailureListener(TaskExecutors.sExecutor, awaitTaskListener);
    }

    public static Object await(Task task) {
        if (task == null) {
            throw new NullPointerException("Task must not be null");
        } else if (task.isComplete()) {
            return getResult(task);
        } else {
            AwaitTaskListener awaitTaskListener = new AwaitTaskListener();
            addTaskListener(task, awaitTaskListener);
            awaitTaskListener.await();
            return getResult(task);
        }
    }

    public static Object await(Task task, long j, TimeUnit timeUnit) {
        if (task == null) {
            throw new NullPointerException("Task must not be null");
        } else if (timeUnit == null) {
            throw new NullPointerException("TimeUnit must not be null");
        } else if (task.isComplete()) {
            return getResult(task);
        } else {
            AwaitTaskListener awaitTaskListener = new AwaitTaskListener();
            addTaskListener(task, awaitTaskListener);
            if (awaitTaskListener.awaitTimeout(j, timeUnit)) {
                return getResult(task);
            }
            throw new TimeoutException("Timed out waiting for Task");
        }
    }

    public static Task createTaskAndSetResult(Object obj) {
        TaskImpl taskImpl = new TaskImpl();
        taskImpl.setResult(obj);
        return taskImpl;
    }

    private static Object getResult(Task task) {
        if (task.isSuccessful()) {
            return task.getResult();
        }
        throw new ExecutionException(task.getException());
    }
}
