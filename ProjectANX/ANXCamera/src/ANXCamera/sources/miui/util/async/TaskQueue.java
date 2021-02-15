package miui.util.async;

import android.util.Log;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.util.async.Task.Priority;
import miui.util.async.Task.Status;
import miui.util.concurrent.ConcurrentRingQueue;
import miui.util.concurrent.Queue;
import miui.util.concurrent.Queue.Predicate;

class TaskQueue implements Queue {
    private final Queue mHighQueue;
    private final Queue mLowQueue;
    private final Queue mNormalQueue;
    private final AtomicBoolean mPause = new AtomicBoolean(false);
    private final Semaphore mSemaphore = new Semaphore(0, true);
    private final TaskManager mTaskManager;

    /* renamed from: miui.util.async.TaskQueue$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$util$async$Task$Priority = new int[Priority.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$miui$util$async$Task$Priority[Priority.Low.ordinal()] = 1;
            $SwitchMap$miui$util$async$Task$Priority[Priority.Normal.ordinal()] = 2;
            $SwitchMap$miui$util$async$Task$Priority[Priority.High.ordinal()] = 3;
            try {
                $SwitchMap$miui$util$async$Task$Priority[Priority.RealTime.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public TaskQueue(TaskManager taskManager, int i) {
        this.mTaskManager = taskManager;
        this.mHighQueue = new ConcurrentRingQueue(i, true, true);
        this.mNormalQueue = new ConcurrentRingQueue(i, true, true);
        this.mLowQueue = new ConcurrentRingQueue(i, true, true);
    }

    private Task getTask() {
        Task task = (Task) this.mHighQueue.get();
        if (task == null) {
            task = (Task) this.mNormalQueue.get();
        }
        return task == null ? (Task) this.mLowQueue.get() : task;
    }

    public int clear() {
        int i = 0;
        while (this.mSemaphore.tryAcquire()) {
            if (getTask().getStatus() != Status.Canceled) {
                i++;
            }
        }
        return i;
    }

    public Task get() {
        Task task;
        if (this.mTaskManager.isShutdown()) {
            Task task2 = null;
            while (this.mSemaphore.tryAcquire()) {
                task = getTask();
                if (task.getStatus() != Status.Canceled) {
                    break;
                }
            }
        } else {
            loop0:
            while (true) {
                task = null;
                while (task == null) {
                    try {
                        this.mSemaphore.acquire();
                        if (this.mPause.get()) {
                            synchronized (this.mPause) {
                                while (this.mPause.get()) {
                                    try {
                                        this.mPause.wait();
                                    } catch (InterruptedException unused) {
                                        this.mSemaphore.release();
                                        return null;
                                    } catch (Throwable th) {
                                        throw th;
                                    }
                                }
                            }
                        }
                        task = getTask();
                        if (task.getStatus() == Status.Canceled) {
                        }
                    } catch (InterruptedException unused2) {
                        return null;
                    }
                }
                break loop0;
            }
        }
        return task;
    }

    public int getCapacity() {
        return -1;
    }

    public boolean isEmpty() {
        return this.mSemaphore.availablePermits() == 0;
    }

    public void pause() {
        synchronized (this.mPause) {
            this.mPause.set(true);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean put(Task task) {
        boolean z;
        Queue queue;
        int i = AnonymousClass1.$SwitchMap$miui$util$async$Task$Priority[task.getPriority().ordinal()];
        if (i == 1) {
            queue = this.mLowQueue;
        } else if (i == 2) {
            queue = this.mNormalQueue;
        } else if (i != 3) {
            if (i == 4) {
                Log.e("async", "Realtime task must NOT be put in Queue");
            }
            z = false;
            if (z) {
                task.setStatus(Status.Queued, null);
                this.mSemaphore.release();
            }
            return z;
        } else {
            queue = this.mHighQueue;
        }
        z = queue.put(task);
        if (z) {
        }
        return z;
    }

    public int remove(Predicate predicate) {
        throw new RuntimeException("no support for this method");
    }

    public boolean remove(Task task) {
        if (!this.mSemaphore.tryAcquire()) {
            return false;
        }
        boolean remove = this.mHighQueue.remove((Object) task);
        if (!remove) {
            remove = this.mNormalQueue.remove((Object) task);
        }
        if (!remove) {
            remove = this.mLowQueue.remove((Object) task);
        }
        if (remove) {
            return remove;
        }
        this.mSemaphore.release();
        return remove;
    }

    public void resume() {
        synchronized (this.mPause) {
            this.mPause.set(false);
            this.mPause.notifyAll();
        }
    }
}
