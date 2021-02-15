package io.reactivex.plugins;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.Beta;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.parallel.ParallelFlowable;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import org.reactivestreams.Subscriber;

public final class RxJavaPlugins {
    @Nullable
    static volatile Consumer errorHandler;
    static volatile boolean failNonBlockingScheduler;
    static volatile boolean lockdown;
    @Nullable
    static volatile BooleanSupplier onBeforeBlocking;
    static volatile Function onCompletableAssembly;
    @Nullable
    static volatile BiFunction onCompletableSubscribe;
    @Nullable
    static volatile Function onComputationHandler;
    @Nullable
    static volatile Function onConnectableFlowableAssembly;
    @Nullable
    static volatile Function onConnectableObservableAssembly;
    @Nullable
    static volatile Function onFlowableAssembly;
    @Nullable
    static volatile BiFunction onFlowableSubscribe;
    @Nullable
    static volatile Function onInitComputationHandler;
    @Nullable
    static volatile Function onInitIoHandler;
    @Nullable
    static volatile Function onInitNewThreadHandler;
    @Nullable
    static volatile Function onInitSingleHandler;
    @Nullable
    static volatile Function onIoHandler;
    @Nullable
    static volatile Function onMaybeAssembly;
    @Nullable
    static volatile BiFunction onMaybeSubscribe;
    @Nullable
    static volatile Function onNewThreadHandler;
    @Nullable
    static volatile Function onObservableAssembly;
    @Nullable
    static volatile BiFunction onObservableSubscribe;
    @Nullable
    static volatile Function onParallelAssembly;
    @Nullable
    static volatile Function onScheduleHandler;
    @Nullable
    static volatile Function onSingleAssembly;
    @Nullable
    static volatile Function onSingleHandler;
    @Nullable
    static volatile BiFunction onSingleSubscribe;

    private RxJavaPlugins() {
        throw new IllegalStateException("No instances!");
    }

    @NonNull
    static Object apply(@NonNull BiFunction biFunction, @NonNull Object obj, @NonNull Object obj2) {
        try {
            return biFunction.apply(obj, obj2);
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @NonNull
    static Object apply(@NonNull Function function, @NonNull Object obj) {
        try {
            return function.apply(obj);
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @NonNull
    static Scheduler applyRequireNonNull(@NonNull Function function, Callable callable) {
        Object apply = apply(function, callable);
        ObjectHelper.requireNonNull(apply, "Scheduler Callable result can't be null");
        return (Scheduler) apply;
    }

    @NonNull
    static Scheduler callRequireNonNull(@NonNull Callable callable) {
        try {
            Object call = callable.call();
            ObjectHelper.requireNonNull(call, "Scheduler Callable result can't be null");
            return (Scheduler) call;
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @NonNull
    public static Scheduler createComputationScheduler(@NonNull ThreadFactory threadFactory) {
        ObjectHelper.requireNonNull(threadFactory, "threadFactory is null");
        return new ComputationScheduler(threadFactory);
    }

    @NonNull
    public static Scheduler createIoScheduler(@NonNull ThreadFactory threadFactory) {
        ObjectHelper.requireNonNull(threadFactory, "threadFactory is null");
        return new IoScheduler(threadFactory);
    }

    @NonNull
    public static Scheduler createNewThreadScheduler(@NonNull ThreadFactory threadFactory) {
        ObjectHelper.requireNonNull(threadFactory, "threadFactory is null");
        return new NewThreadScheduler(threadFactory);
    }

    @NonNull
    public static Scheduler createSingleScheduler(@NonNull ThreadFactory threadFactory) {
        ObjectHelper.requireNonNull(threadFactory, "threadFactory is null");
        return new SingleScheduler(threadFactory);
    }

    @Nullable
    public static Function getComputationSchedulerHandler() {
        return onComputationHandler;
    }

    @Nullable
    public static Consumer getErrorHandler() {
        return errorHandler;
    }

    @Nullable
    public static Function getInitComputationSchedulerHandler() {
        return onInitComputationHandler;
    }

    @Nullable
    public static Function getInitIoSchedulerHandler() {
        return onInitIoHandler;
    }

    @Nullable
    public static Function getInitNewThreadSchedulerHandler() {
        return onInitNewThreadHandler;
    }

    @Nullable
    public static Function getInitSingleSchedulerHandler() {
        return onInitSingleHandler;
    }

    @Nullable
    public static Function getIoSchedulerHandler() {
        return onIoHandler;
    }

    @Nullable
    public static Function getNewThreadSchedulerHandler() {
        return onNewThreadHandler;
    }

    @Nullable
    public static BooleanSupplier getOnBeforeBlocking() {
        return onBeforeBlocking;
    }

    @Nullable
    public static Function getOnCompletableAssembly() {
        return onCompletableAssembly;
    }

    @Nullable
    public static BiFunction getOnCompletableSubscribe() {
        return onCompletableSubscribe;
    }

    @Nullable
    public static Function getOnConnectableFlowableAssembly() {
        return onConnectableFlowableAssembly;
    }

    @Nullable
    public static Function getOnConnectableObservableAssembly() {
        return onConnectableObservableAssembly;
    }

    @Nullable
    public static Function getOnFlowableAssembly() {
        return onFlowableAssembly;
    }

    @Nullable
    public static BiFunction getOnFlowableSubscribe() {
        return onFlowableSubscribe;
    }

    @Nullable
    public static Function getOnMaybeAssembly() {
        return onMaybeAssembly;
    }

    @Nullable
    public static BiFunction getOnMaybeSubscribe() {
        return onMaybeSubscribe;
    }

    @Nullable
    public static Function getOnObservableAssembly() {
        return onObservableAssembly;
    }

    @Nullable
    public static BiFunction getOnObservableSubscribe() {
        return onObservableSubscribe;
    }

    @Beta
    @Nullable
    public static Function getOnParallelAssembly() {
        return onParallelAssembly;
    }

    @Nullable
    public static Function getOnSingleAssembly() {
        return onSingleAssembly;
    }

    @Nullable
    public static BiFunction getOnSingleSubscribe() {
        return onSingleSubscribe;
    }

    @Nullable
    public static Function getScheduleHandler() {
        return onScheduleHandler;
    }

    @Nullable
    public static Function getSingleSchedulerHandler() {
        return onSingleHandler;
    }

    @NonNull
    public static Scheduler initComputationScheduler(@NonNull Callable callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function function = onInitComputationHandler;
        return function == null ? callRequireNonNull(callable) : applyRequireNonNull(function, callable);
    }

    @NonNull
    public static Scheduler initIoScheduler(@NonNull Callable callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function function = onInitIoHandler;
        return function == null ? callRequireNonNull(callable) : applyRequireNonNull(function, callable);
    }

    @NonNull
    public static Scheduler initNewThreadScheduler(@NonNull Callable callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function function = onInitNewThreadHandler;
        return function == null ? callRequireNonNull(callable) : applyRequireNonNull(function, callable);
    }

    @NonNull
    public static Scheduler initSingleScheduler(@NonNull Callable callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function function = onInitSingleHandler;
        return function == null ? callRequireNonNull(callable) : applyRequireNonNull(function, callable);
    }

    static boolean isBug(Throwable th) {
        return (th instanceof OnErrorNotImplementedException) || (th instanceof MissingBackpressureException) || (th instanceof IllegalStateException) || (th instanceof NullPointerException) || (th instanceof IllegalArgumentException) || (th instanceof CompositeException);
    }

    public static boolean isFailOnNonBlockingScheduler() {
        return failNonBlockingScheduler;
    }

    public static boolean isLockdown() {
        return lockdown;
    }

    public static void lockdown() {
        lockdown = true;
    }

    @NonNull
    public static Completable onAssembly(@NonNull Completable completable) {
        Function function = onCompletableAssembly;
        return function != null ? (Completable) apply(function, completable) : completable;
    }

    @NonNull
    public static Flowable onAssembly(@NonNull Flowable flowable) {
        Function function = onFlowableAssembly;
        return function != null ? (Flowable) apply(function, flowable) : flowable;
    }

    @NonNull
    public static Maybe onAssembly(@NonNull Maybe maybe) {
        Function function = onMaybeAssembly;
        return function != null ? (Maybe) apply(function, maybe) : maybe;
    }

    @NonNull
    public static Observable onAssembly(@NonNull Observable observable) {
        Function function = onObservableAssembly;
        return function != null ? (Observable) apply(function, observable) : observable;
    }

    @NonNull
    public static Single onAssembly(@NonNull Single single) {
        Function function = onSingleAssembly;
        return function != null ? (Single) apply(function, single) : single;
    }

    @NonNull
    public static ConnectableFlowable onAssembly(@NonNull ConnectableFlowable connectableFlowable) {
        Function function = onConnectableFlowableAssembly;
        return function != null ? (ConnectableFlowable) apply(function, connectableFlowable) : connectableFlowable;
    }

    @NonNull
    public static ConnectableObservable onAssembly(@NonNull ConnectableObservable connectableObservable) {
        Function function = onConnectableObservableAssembly;
        return function != null ? (ConnectableObservable) apply(function, connectableObservable) : connectableObservable;
    }

    @NonNull
    @Beta
    public static ParallelFlowable onAssembly(@NonNull ParallelFlowable parallelFlowable) {
        Function function = onParallelAssembly;
        return function != null ? (ParallelFlowable) apply(function, parallelFlowable) : parallelFlowable;
    }

    public static boolean onBeforeBlocking() {
        BooleanSupplier booleanSupplier = onBeforeBlocking;
        if (booleanSupplier == null) {
            return false;
        }
        try {
            return booleanSupplier.getAsBoolean();
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @NonNull
    public static Scheduler onComputationScheduler(@NonNull Scheduler scheduler) {
        Function function = onComputationHandler;
        return function == null ? scheduler : (Scheduler) apply(function, scheduler);
    }

    public static void onError(@NonNull Throwable th) {
        Consumer consumer = errorHandler;
        if (th == null) {
            th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        } else if (!isBug(th)) {
            th = new UndeliverableException(th);
        }
        if (consumer != null) {
            try {
                consumer.accept(th);
                return;
            } catch (Throwable th2) {
                th2.printStackTrace();
                uncaught(th2);
            }
        }
        th.printStackTrace();
        uncaught(th);
    }

    @NonNull
    public static Scheduler onIoScheduler(@NonNull Scheduler scheduler) {
        Function function = onIoHandler;
        return function == null ? scheduler : (Scheduler) apply(function, scheduler);
    }

    @NonNull
    public static Scheduler onNewThreadScheduler(@NonNull Scheduler scheduler) {
        Function function = onNewThreadHandler;
        return function == null ? scheduler : (Scheduler) apply(function, scheduler);
    }

    @NonNull
    public static Runnable onSchedule(@NonNull Runnable runnable) {
        ObjectHelper.requireNonNull(runnable, "run is null");
        Function function = onScheduleHandler;
        return function == null ? runnable : (Runnable) apply(function, runnable);
    }

    @NonNull
    public static Scheduler onSingleScheduler(@NonNull Scheduler scheduler) {
        Function function = onSingleHandler;
        return function == null ? scheduler : (Scheduler) apply(function, scheduler);
    }

    @NonNull
    public static CompletableObserver onSubscribe(@NonNull Completable completable, @NonNull CompletableObserver completableObserver) {
        BiFunction biFunction = onCompletableSubscribe;
        return biFunction != null ? (CompletableObserver) apply(biFunction, completable, completableObserver) : completableObserver;
    }

    @NonNull
    public static MaybeObserver onSubscribe(@NonNull Maybe maybe, @NonNull MaybeObserver maybeObserver) {
        BiFunction biFunction = onMaybeSubscribe;
        return biFunction != null ? (MaybeObserver) apply(biFunction, maybe, maybeObserver) : maybeObserver;
    }

    @NonNull
    public static Observer onSubscribe(@NonNull Observable observable, @NonNull Observer observer) {
        BiFunction biFunction = onObservableSubscribe;
        return biFunction != null ? (Observer) apply(biFunction, observable, observer) : observer;
    }

    @NonNull
    public static SingleObserver onSubscribe(@NonNull Single single, @NonNull SingleObserver singleObserver) {
        BiFunction biFunction = onSingleSubscribe;
        return biFunction != null ? (SingleObserver) apply(biFunction, single, singleObserver) : singleObserver;
    }

    @NonNull
    public static Subscriber onSubscribe(@NonNull Flowable flowable, @NonNull Subscriber subscriber) {
        BiFunction biFunction = onFlowableSubscribe;
        return biFunction != null ? (Subscriber) apply(biFunction, flowable, subscriber) : subscriber;
    }

    public static void reset() {
        setErrorHandler(null);
        setScheduleHandler(null);
        setComputationSchedulerHandler(null);
        setInitComputationSchedulerHandler(null);
        setIoSchedulerHandler(null);
        setInitIoSchedulerHandler(null);
        setSingleSchedulerHandler(null);
        setInitSingleSchedulerHandler(null);
        setNewThreadSchedulerHandler(null);
        setInitNewThreadSchedulerHandler(null);
        setOnFlowableAssembly(null);
        setOnFlowableSubscribe(null);
        setOnObservableAssembly(null);
        setOnObservableSubscribe(null);
        setOnSingleAssembly(null);
        setOnSingleSubscribe(null);
        setOnCompletableAssembly(null);
        setOnCompletableSubscribe(null);
        setOnConnectableFlowableAssembly(null);
        setOnConnectableObservableAssembly(null);
        setOnMaybeAssembly(null);
        setOnMaybeSubscribe(null);
        setOnParallelAssembly(null);
        setFailOnNonBlockingScheduler(false);
        setOnBeforeBlocking(null);
    }

    public static void setComputationSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onComputationHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setErrorHandler(@Nullable Consumer consumer) {
        if (!lockdown) {
            errorHandler = consumer;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setFailOnNonBlockingScheduler(boolean z) {
        if (!lockdown) {
            failNonBlockingScheduler = z;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setInitComputationSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onInitComputationHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setInitIoSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onInitIoHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setInitNewThreadSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onInitNewThreadHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setInitSingleSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onInitSingleHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setIoSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onIoHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setNewThreadSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onNewThreadHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnBeforeBlocking(@Nullable BooleanSupplier booleanSupplier) {
        if (!lockdown) {
            onBeforeBlocking = booleanSupplier;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnCompletableAssembly(@Nullable Function function) {
        if (!lockdown) {
            onCompletableAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnCompletableSubscribe(@Nullable BiFunction biFunction) {
        if (!lockdown) {
            onCompletableSubscribe = biFunction;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnConnectableFlowableAssembly(@Nullable Function function) {
        if (!lockdown) {
            onConnectableFlowableAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnConnectableObservableAssembly(@Nullable Function function) {
        if (!lockdown) {
            onConnectableObservableAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnFlowableAssembly(@Nullable Function function) {
        if (!lockdown) {
            onFlowableAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnFlowableSubscribe(@Nullable BiFunction biFunction) {
        if (!lockdown) {
            onFlowableSubscribe = biFunction;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnMaybeAssembly(@Nullable Function function) {
        if (!lockdown) {
            onMaybeAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnMaybeSubscribe(@Nullable BiFunction biFunction) {
        if (!lockdown) {
            onMaybeSubscribe = biFunction;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnObservableAssembly(@Nullable Function function) {
        if (!lockdown) {
            onObservableAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnObservableSubscribe(@Nullable BiFunction biFunction) {
        if (!lockdown) {
            onObservableSubscribe = biFunction;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    @Beta
    public static void setOnParallelAssembly(@Nullable Function function) {
        if (!lockdown) {
            onParallelAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnSingleAssembly(@Nullable Function function) {
        if (!lockdown) {
            onSingleAssembly = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setOnSingleSubscribe(@Nullable BiFunction biFunction) {
        if (!lockdown) {
            onSingleSubscribe = biFunction;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setScheduleHandler(@Nullable Function function) {
        if (!lockdown) {
            onScheduleHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    public static void setSingleSchedulerHandler(@Nullable Function function) {
        if (!lockdown) {
            onSingleHandler = function;
            return;
        }
        throw new IllegalStateException("Plugins can't be changed anymore");
    }

    static void uncaught(@NonNull Throwable th) {
        Thread currentThread = Thread.currentThread();
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
    }

    static void unlock() {
        lockdown = false;
    }
}
