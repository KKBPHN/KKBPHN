package miui.util.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import miui.util.AppConstants;
import miui.util.Pools;
import miui.util.Pools.Manager;
import miui.util.Pools.Pool;
import miui.util.concurrent.ConcurrentRingQueue;
import miui.util.concurrent.Queue.Predicate;

class TaskInfoDeliverer {
    private static final Pool TASK_DELIVERY_INFO_POOL = Pools.createSimplePool(new Manager() {
        public TaskDeliveryInfo createInstance() {
            return new TaskDeliveryInfo();
        }

        public void onRelease(TaskDeliveryInfo taskDeliveryInfo) {
            taskDeliveryInfo.clear();
        }
    }, 8);
    /* access modifiers changed from: private */
    public DeliverHandler mHandler = new DeliverHandler(AppConstants.getCurrentApplication().getMainLooper());
    private ConcurrentRingQueue mTaskDeliveryInfoQueue = new ConcurrentRingQueue(20, true, true);
    private TaskManager mTaskManager;

    class DeliverHandler extends Handler {
        static final int CONTINUE = 0;

        public DeliverHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            ((TaskInfoDeliverer) message.obj).processAllDelivery();
            message.obj = null;
        }

        public void post(TaskInfoDeliverer taskInfoDeliverer) {
            obtainMessage(0, taskInfoDeliverer).sendToTarget();
        }
    }

    class TaskDeliveryInfo {
        public Delivery delivery;
        public Object info;
        public Task task;

        TaskDeliveryInfo() {
        }

        public void clear() {
            this.task = null;
            this.delivery = null;
            this.info = null;
        }
    }

    public TaskInfoDeliverer(TaskManager taskManager) {
        this.mTaskManager = taskManager;
    }

    /* access modifiers changed from: private */
    public void processAllDelivery() {
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            while (!this.mTaskDeliveryInfoQueue.isEmpty()) {
                TaskDeliveryInfo taskDeliveryInfo = (TaskDeliveryInfo) this.mTaskDeliveryInfoQueue.get();
                if (taskDeliveryInfo != null) {
                    taskDeliveryInfo.task.deliver(this.mTaskManager, taskDeliveryInfo.delivery, taskDeliveryInfo.info);
                    TASK_DELIVERY_INFO_POOL.release(taskDeliveryInfo);
                }
            }
            return;
        }
        this.mHandler.post(this);
    }

    public void postDeliver(final Task task, Delivery delivery, Object obj) {
        TaskDeliveryInfo taskDeliveryInfo = (TaskDeliveryInfo) TASK_DELIVERY_INFO_POOL.acquire();
        taskDeliveryInfo.task = task;
        taskDeliveryInfo.delivery = delivery;
        taskDeliveryInfo.info = obj;
        if (delivery == Delivery.Result && obj == null) {
            this.mTaskDeliveryInfoQueue.remove((Predicate) new Predicate() {
                public boolean apply(TaskDeliveryInfo taskDeliveryInfo) {
                    return taskDeliveryInfo != null && taskDeliveryInfo.task == task;
                }
            });
        }
        this.mTaskDeliveryInfoQueue.put(taskDeliveryInfo);
        processAllDelivery();
    }

    public void setCallbackThread(boolean z) {
        Looper mainLooper = AppConstants.getCurrentApplication().getMainLooper();
        if (z && this.mHandler.getLooper() != mainLooper) {
            this.mHandler.getLooper().quit();
            this.mHandler = new DeliverHandler(mainLooper);
        } else if (!z && this.mHandler.getLooper() == mainLooper) {
            AnonymousClass2 r3 = new Thread() {
                public void run() {
                    Looper.prepare();
                    TaskInfoDeliverer.this.mHandler = new DeliverHandler(Looper.myLooper());
                    Looper.loop();
                }
            };
            r3.setName("TaskInfoDeliverer-Callback");
            r3.start();
        }
    }
}
