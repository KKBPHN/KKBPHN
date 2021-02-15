package miui.util.async;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miui.os.Environment;
import miui.util.SoftReferenceSingleton;
import miui.util.async.Task.Priority;
import miui.util.async.Task.Status;
import miui.util.cache.Cache;
import miui.util.cache.LruCache;

public class TaskManager {
    public static final int DEFAULT_CACHE_SIZE = -1;
    private static final int DEFAULT_QUEUE_SIZE = 10;
    private static final int DEFAULT_THREAD_SIZE = -1;
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public TaskManager createInstance() {
            return new TaskManager();
        }
    };
    static final boolean LOG_DEBUG = false;
    static final boolean LOG_ERROR = true;
    static final boolean LOG_INFO = false;
    static final boolean LOG_VERBOSE = false;
    static final boolean LOG_WARNING = true;
    static final String TAG = "async";
    private Cache mCache;
    private TaskInfoDeliverer mDeliverer;
    private Object mFinalizeGuardian;
    private final TaskQueue mQueue;
    private volatile boolean mShutdown;
    private ArrayList mTaskThreads;

    public TaskManager() {
        this(10, -1, -1);
    }

    public TaskManager(int i, int i2, int i3) {
        this.mFinalizeGuardian = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() {
                try {
                    TaskManager.this.shutdown();
                } finally {
                    super.finalize();
                }
            }
        };
        this.mShutdown = false;
        this.mQueue = new TaskQueue(this, i);
        if (i2 < 0) {
            i2 = Environment.getCpuCount();
            if (i2 <= 0) {
                i2 = 4;
            }
        }
        this.mTaskThreads = new ArrayList(i2);
        for (int i4 = 0; i4 < i2; i4++) {
            this.mTaskThreads.add(new TaskThread(this, this.mQueue, i4));
            ((TaskThread) this.mTaskThreads.get(i4)).start();
        }
        this.mDeliverer = new TaskInfoDeliverer(this);
        this.mCache = new LruCache(i3);
    }

    private void clearQueue() {
        while (!this.mQueue.isEmpty()) {
            Task task = this.mQueue.get();
            if (task != null) {
                task.setStatus(Status.Canceled, null);
            }
        }
    }

    public static TaskManager getDefault() {
        return (TaskManager) INSTANCE.get();
    }

    public void add(Task task) {
        boolean z = this.mShutdown;
        String str = TAG;
        if (z) {
            Log.e(str, "Cannot add task into a shut down task manager");
            return;
        }
        String str2 = " has already added into task manager and not finish yet";
        String str3 = "Task ";
        if (task.isRunning()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str3);
            sb.append(task);
            sb.append(str2);
            String sb2 = sb.toString();
            Log.e(str, sb2);
            throw new IllegalArgumentException(sb2);
        } else if (task.getStatus() != Status.New && !task.restart()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Status of task ");
            sb3.append(task);
            sb3.append(" is not New, and cannot restart.");
            throw new IllegalArgumentException(sb3.toString());
        } else if (task.setTaskManager(this)) {
            Cache cache = this.mCache;
            if (cache != null && (task instanceof Cacheable)) {
                Object obj = cache.get(((Cacheable) task).getCacheKey());
                if (obj != null) {
                    task.setStatus(Status.Queued, null);
                    task.setStatus(Status.Executing, null);
                    task.setStatus(Status.Done, obj);
                    return;
                }
            }
            if (task.getPriority() == Priority.RealTime) {
                task.setStatus(Status.Queued, null);
                TaskThread.runRealTimeTask(this, task);
            } else {
                this.mQueue.put(task);
            }
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append(task);
            sb4.append(str2);
            throw new IllegalArgumentException(sb4.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public Cache getCache() {
        return this.mCache;
    }

    /* access modifiers changed from: 0000 */
    public boolean isShutdown() {
        return this.mShutdown;
    }

    public void pause() {
        this.mQueue.pause();
    }

    /* access modifiers changed from: 0000 */
    public void postDelivery(Task task, Delivery delivery, Object obj) {
        this.mDeliverer.postDeliver(task, delivery, obj);
    }

    /* access modifiers changed from: 0000 */
    public boolean remove(Task task) {
        return this.mQueue.remove(task);
    }

    public void resume() {
        this.mQueue.resume();
    }

    public void setCallbackThread(boolean z) {
        this.mDeliverer.setCallbackThread(z);
    }

    public void setMaxCache(int i) {
        this.mCache.setMaxSize(i);
    }

    public void setThreadCount(int i) {
        if (this.mShutdown) {
            Log.e(TAG, "Cannot add task into a shut down task manager");
            return;
        }
        if (i < 0) {
            i = Environment.getCpuCount();
            if (i <= 0) {
                i = 4;
            }
        }
        int size = this.mTaskThreads.size();
        if (i > size) {
            while (size < i) {
                TaskThread taskThread = new TaskThread(this, this.mQueue, size);
                taskThread.start();
                this.mTaskThreads.add(taskThread);
                size++;
            }
        } else if (i < size) {
            for (int i2 = size - 1; i2 >= i; i2--) {
                ((TaskThread) this.mTaskThreads.get(i2)).shutdown();
                this.mTaskThreads.remove(i2);
            }
        }
    }

    public void shutdown() {
        if (this != getDefault() && !this.mShutdown) {
            this.mShutdown = true;
            Iterator it = this.mTaskThreads.iterator();
            while (it.hasNext()) {
                ((TaskThread) it.next()).shutdown();
            }
            this.mTaskThreads.clear();
            this.mCache.clear();
            clearQueue();
        }
    }
}
