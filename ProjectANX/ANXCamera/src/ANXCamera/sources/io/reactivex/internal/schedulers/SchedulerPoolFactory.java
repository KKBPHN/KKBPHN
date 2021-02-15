package io.reactivex.internal.schedulers;

import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class SchedulerPoolFactory {
    static final Map POOLS = new ConcurrentHashMap();
    public static final boolean PURGE_ENABLED;
    static final String PURGE_ENABLED_KEY = "rx2.purge-enabled";
    public static final int PURGE_PERIOD_SECONDS;
    static final String PURGE_PERIOD_SECONDS_KEY = "rx2.purge-period-seconds";
    static final AtomicReference PURGE_THREAD = new AtomicReference();

    final class ScheduledTask implements Runnable {
        ScheduledTask() {
        }

        public void run() {
            try {
                Iterator it = new ArrayList(SchedulerPoolFactory.POOLS.keySet()).iterator();
                while (it.hasNext()) {
                    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) it.next();
                    if (scheduledThreadPoolExecutor.isShutdown()) {
                        SchedulerPoolFactory.POOLS.remove(scheduledThreadPoolExecutor);
                    } else {
                        scheduledThreadPoolExecutor.purge();
                    }
                }
            } catch (Throwable th) {
                RxJavaPlugins.onError(th);
            }
        }
    }

    static {
        Properties properties = System.getProperties();
        String str = PURGE_ENABLED_KEY;
        int i = 1;
        boolean z = properties.containsKey(str) ? Boolean.getBoolean(str) : true;
        if (z) {
            String str2 = PURGE_PERIOD_SECONDS_KEY;
            if (properties.containsKey(str2)) {
                i = Integer.getInteger(str2, 1).intValue();
            }
        }
        PURGE_ENABLED = z;
        PURGE_PERIOD_SECONDS = i;
        start();
    }

    private SchedulerPoolFactory() {
        throw new IllegalStateException("No instances!");
    }

    public static ScheduledExecutorService create(ThreadFactory threadFactory) {
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, threadFactory);
        if (PURGE_ENABLED && (newScheduledThreadPool instanceof ScheduledThreadPoolExecutor)) {
            POOLS.put((ScheduledThreadPoolExecutor) newScheduledThreadPool, newScheduledThreadPool);
        }
        return newScheduledThreadPool;
    }

    public static void shutdown() {
        ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) PURGE_THREAD.get();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
        POOLS.clear();
    }

    public static void start() {
        if (PURGE_ENABLED) {
            while (true) {
                ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) PURGE_THREAD.get();
                if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
                    ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, new RxThreadFactory("RxSchedulerPurge"));
                    if (PURGE_THREAD.compareAndSet(scheduledExecutorService, newScheduledThreadPool)) {
                        ScheduledTask scheduledTask = new ScheduledTask();
                        int i = PURGE_PERIOD_SECONDS;
                        newScheduledThreadPool.scheduleAtFixedRate(scheduledTask, (long) i, (long) i, TimeUnit.SECONDS);
                        return;
                    }
                    newScheduledThreadPool.shutdownNow();
                } else {
                    return;
                }
            }
        }
    }
}
