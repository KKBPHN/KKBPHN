package io.reactivex.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.internal.schedulers.TrampolineScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public final class Schedulers {
    @NonNull
    static final Scheduler COMPUTATION = RxJavaPlugins.initComputationScheduler(new ComputationTask());
    @NonNull
    static final Scheduler IO = RxJavaPlugins.initIoScheduler(new IOTask());
    @NonNull
    static final Scheduler NEW_THREAD = RxJavaPlugins.initNewThreadScheduler(new NewThreadTask());
    @NonNull
    static final Scheduler SINGLE = RxJavaPlugins.initSingleScheduler(new SingleTask());
    @NonNull
    static final Scheduler TRAMPOLINE = TrampolineScheduler.instance();

    final class ComputationHolder {
        static final Scheduler DEFAULT = new ComputationScheduler();

        ComputationHolder() {
        }
    }

    final class ComputationTask implements Callable {
        ComputationTask() {
        }

        public Scheduler call() {
            return ComputationHolder.DEFAULT;
        }
    }

    final class IOTask implements Callable {
        IOTask() {
        }

        public Scheduler call() {
            return IoHolder.DEFAULT;
        }
    }

    final class IoHolder {
        static final Scheduler DEFAULT = new IoScheduler();

        IoHolder() {
        }
    }

    final class NewThreadHolder {
        static final Scheduler DEFAULT = new NewThreadScheduler();

        NewThreadHolder() {
        }
    }

    final class NewThreadTask implements Callable {
        NewThreadTask() {
        }

        public Scheduler call() {
            return NewThreadHolder.DEFAULT;
        }
    }

    final class SingleHolder {
        static final Scheduler DEFAULT = new SingleScheduler();

        SingleHolder() {
        }
    }

    final class SingleTask implements Callable {
        SingleTask() {
        }

        public Scheduler call() {
            return SingleHolder.DEFAULT;
        }
    }

    private Schedulers() {
        throw new IllegalStateException("No instances!");
    }

    @NonNull
    public static Scheduler computation() {
        return RxJavaPlugins.onComputationScheduler(COMPUTATION);
    }

    @NonNull
    public static Scheduler from(@NonNull Executor executor) {
        return new ExecutorScheduler(executor);
    }

    @NonNull
    public static Scheduler io() {
        return RxJavaPlugins.onIoScheduler(IO);
    }

    @NonNull
    public static Scheduler newThread() {
        return RxJavaPlugins.onNewThreadScheduler(NEW_THREAD);
    }

    public static void shutdown() {
        computation().shutdown();
        io().shutdown();
        newThread().shutdown();
        single().shutdown();
        trampoline().shutdown();
        SchedulerPoolFactory.shutdown();
    }

    @NonNull
    public static Scheduler single() {
        return RxJavaPlugins.onSingleScheduler(SINGLE);
    }

    public static void start() {
        computation().start();
        io().start();
        newThread().start();
        single().start();
        trampoline().start();
        SchedulerPoolFactory.start();
    }

    @NonNull
    public static Scheduler trampoline() {
        return TRAMPOLINE;
    }
}
