package io.reactivex.android.plugins;

import io.reactivex.Scheduler;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import java.util.concurrent.Callable;

public final class RxAndroidPlugins {
    private static volatile Function onInitMainThreadHandler;
    private static volatile Function onMainThreadHandler;

    private RxAndroidPlugins() {
        throw new AssertionError("No instances.");
    }

    static Object apply(Function function, Object obj) {
        try {
            return function.apply(obj);
        } catch (Throwable th) {
            Exceptions.propagate(th);
            throw null;
        }
    }

    static Scheduler applyRequireNonNull(Function function, Callable callable) {
        Scheduler scheduler = (Scheduler) apply(function, callable);
        if (scheduler != null) {
            return scheduler;
        }
        throw new NullPointerException("Scheduler Callable returned null");
    }

    static Scheduler callRequireNonNull(Callable callable) {
        try {
            Scheduler scheduler = (Scheduler) callable.call();
            if (scheduler != null) {
                return scheduler;
            }
            throw new NullPointerException("Scheduler Callable returned null");
        } catch (Throwable th) {
            Exceptions.propagate(th);
            throw null;
        }
    }

    public static Scheduler initMainThreadScheduler(Callable callable) {
        if (callable != null) {
            Function function = onInitMainThreadHandler;
            return function == null ? callRequireNonNull(callable) : applyRequireNonNull(function, callable);
        }
        throw new NullPointerException("scheduler == null");
    }

    public static Scheduler onMainThreadScheduler(Scheduler scheduler) {
        if (scheduler != null) {
            Function function = onMainThreadHandler;
            return function == null ? scheduler : (Scheduler) apply(function, scheduler);
        }
        throw new NullPointerException("scheduler == null");
    }

    public static void reset() {
        setInitMainThreadSchedulerHandler(null);
        setMainThreadSchedulerHandler(null);
    }

    public static void setInitMainThreadSchedulerHandler(Function function) {
        onInitMainThreadHandler = function;
    }

    public static void setMainThreadSchedulerHandler(Function function) {
        onMainThreadHandler = function;
    }
}
