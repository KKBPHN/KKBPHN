package miui.util.async;

import miui.util.async.Task.Status;
import miui.util.cache.Cache;

class TaskThread extends Thread {
    private final TaskQueue mQueue;
    private volatile boolean mShutdown = false;
    private final TaskManager mTaskManager;

    public TaskThread(TaskManager taskManager, TaskQueue taskQueue, int i) {
        this.mTaskManager = taskManager;
        this.mQueue = taskQueue;
        StringBuilder sb = new StringBuilder();
        sb.append("TaskThread-");
        sb.append(i);
        setName(sb.toString());
    }

    public static void runRealTimeTask(final TaskManager taskManager, final Task task) {
        AnonymousClass1 r0 = new Thread() {
            public void run() {
                TaskThread.runTask(TaskManager.this, this, task);
            }
        };
        r0.setName("TaskThread-RealTime");
        r0.start();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void runTask(TaskManager taskManager, Thread thread, Task task) {
        Object obj;
        Cacheable cacheable;
        if (taskManager != null && task != null) {
            task.setStatus(Status.Executing, null);
            task.setThread(thread);
            try {
                obj = task.doLoad();
                try {
                    task.setStatus(Status.Done, obj == null ? new NullPointerException("result is null") : obj);
                } catch (Exception e) {
                    e = e;
                    task.setStatus(Status.Done, new TaskExecutingException(e));
                    Cache cache = taskManager.getCache();
                    cacheable = (Cacheable) task;
                    if (cacheable.getCacheKey() != null) {
                    }
                    task.setThread(null);
                }
            } catch (Exception e2) {
                e = e2;
                obj = null;
                task.setStatus(Status.Done, new TaskExecutingException(e));
                Cache cache2 = taskManager.getCache();
                cacheable = (Cacheable) task;
                if (cacheable.getCacheKey() != null) {
                }
                task.setThread(null);
            }
            Cache cache22 = taskManager.getCache();
            if (!(cache22 == null || obj == null || !(task instanceof Cacheable))) {
                cacheable = (Cacheable) task;
                if (cacheable.getCacheKey() != null) {
                    cache22.put(cacheable.getCacheKey(), obj, cacheable.sizeOf(obj));
                }
            }
            task.setThread(null);
        }
    }

    public void run() {
        TaskQueue taskQueue = this.mQueue;
        while (!this.mShutdown) {
            Task task = taskQueue.get();
            if (task != null) {
                runTask(this.mTaskManager, this, task);
                setPriority(5);
            }
        }
    }

    public void shutdown() {
        this.mShutdown = true;
        interrupt();
    }
}
