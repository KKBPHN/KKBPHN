package io.reactivex;

import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.functions.Function9;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.internal.observers.BlockingMultiObserver;
import io.reactivex.internal.operators.flowable.FlowableConcatMapPublisher;
import io.reactivex.internal.operators.flowable.FlowableFlatMapPublisher;
import io.reactivex.internal.operators.maybe.MaybeAmb;
import io.reactivex.internal.operators.maybe.MaybeCache;
import io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
import io.reactivex.internal.operators.maybe.MaybeConcatArray;
import io.reactivex.internal.operators.maybe.MaybeConcatArrayDelayError;
import io.reactivex.internal.operators.maybe.MaybeConcatIterable;
import io.reactivex.internal.operators.maybe.MaybeContains;
import io.reactivex.internal.operators.maybe.MaybeCount;
import io.reactivex.internal.operators.maybe.MaybeCreate;
import io.reactivex.internal.operators.maybe.MaybeDefer;
import io.reactivex.internal.operators.maybe.MaybeDelay;
import io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher;
import io.reactivex.internal.operators.maybe.MaybeDelaySubscriptionOtherPublisher;
import io.reactivex.internal.operators.maybe.MaybeDetach;
import io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess;
import io.reactivex.internal.operators.maybe.MaybeDoFinally;
import io.reactivex.internal.operators.maybe.MaybeDoOnEvent;
import io.reactivex.internal.operators.maybe.MaybeEmpty;
import io.reactivex.internal.operators.maybe.MaybeEqualSingle;
import io.reactivex.internal.operators.maybe.MaybeError;
import io.reactivex.internal.operators.maybe.MaybeErrorCallable;
import io.reactivex.internal.operators.maybe.MaybeFilter;
import io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector;
import io.reactivex.internal.operators.maybe.MaybeFlatMapCompletable;
import io.reactivex.internal.operators.maybe.MaybeFlatMapIterableFlowable;
import io.reactivex.internal.operators.maybe.MaybeFlatMapIterableObservable;
import io.reactivex.internal.operators.maybe.MaybeFlatMapNotification;
import io.reactivex.internal.operators.maybe.MaybeFlatMapSingle;
import io.reactivex.internal.operators.maybe.MaybeFlatMapSingleElement;
import io.reactivex.internal.operators.maybe.MaybeFlatten;
import io.reactivex.internal.operators.maybe.MaybeFromAction;
import io.reactivex.internal.operators.maybe.MaybeFromCallable;
import io.reactivex.internal.operators.maybe.MaybeFromCompletable;
import io.reactivex.internal.operators.maybe.MaybeFromFuture;
import io.reactivex.internal.operators.maybe.MaybeFromRunnable;
import io.reactivex.internal.operators.maybe.MaybeFromSingle;
import io.reactivex.internal.operators.maybe.MaybeHide;
import io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable;
import io.reactivex.internal.operators.maybe.MaybeIsEmptySingle;
import io.reactivex.internal.operators.maybe.MaybeJust;
import io.reactivex.internal.operators.maybe.MaybeLift;
import io.reactivex.internal.operators.maybe.MaybeMap;
import io.reactivex.internal.operators.maybe.MaybeMergeArray;
import io.reactivex.internal.operators.maybe.MaybeNever;
import io.reactivex.internal.operators.maybe.MaybeObserveOn;
import io.reactivex.internal.operators.maybe.MaybeOnErrorComplete;
import io.reactivex.internal.operators.maybe.MaybeOnErrorNext;
import io.reactivex.internal.operators.maybe.MaybeOnErrorReturn;
import io.reactivex.internal.operators.maybe.MaybePeek;
import io.reactivex.internal.operators.maybe.MaybeSubscribeOn;
import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmpty;
import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;
import io.reactivex.internal.operators.maybe.MaybeTakeUntilMaybe;
import io.reactivex.internal.operators.maybe.MaybeTakeUntilPublisher;
import io.reactivex.internal.operators.maybe.MaybeTimeoutMaybe;
import io.reactivex.internal.operators.maybe.MaybeTimeoutPublisher;
import io.reactivex.internal.operators.maybe.MaybeTimer;
import io.reactivex.internal.operators.maybe.MaybeToFlowable;
import io.reactivex.internal.operators.maybe.MaybeToObservable;
import io.reactivex.internal.operators.maybe.MaybeToPublisher;
import io.reactivex.internal.operators.maybe.MaybeToSingle;
import io.reactivex.internal.operators.maybe.MaybeUnsafeCreate;
import io.reactivex.internal.operators.maybe.MaybeUnsubscribeOn;
import io.reactivex.internal.operators.maybe.MaybeUsing;
import io.reactivex.internal.operators.maybe.MaybeZipArray;
import io.reactivex.internal.operators.maybe.MaybeZipIterable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public abstract class Maybe implements MaybeSource {
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe amb(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeAmb(null, iterable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe ambArray(MaybeSource... maybeSourceArr) {
        return maybeSourceArr.length == 0 ? empty() : maybeSourceArr.length == 1 ? wrap(maybeSourceArr[0]) : RxJavaPlugins.onAssembly((Maybe) new MaybeAmb(maybeSourceArr, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(MaybeSource maybeSource, MaybeSource maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return concatArray(maybeSource, maybeSource2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return concatArray(maybeSource, maybeSource2, maybeSource3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return concatArray(maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Flowable) new MaybeConcatIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Publisher publisher) {
        return concat(publisher, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Publisher publisher, int i) {
        ObjectHelper.requireNonNull(publisher, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableConcatMapPublisher(publisher, MaybeToPublisher.instance(), i, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArray(MaybeSource... maybeSourceArr) {
        ObjectHelper.requireNonNull(maybeSourceArr, "sources is null");
        if (maybeSourceArr.length == 0) {
            return Flowable.empty();
        }
        return RxJavaPlugins.onAssembly(maybeSourceArr.length == 1 ? new MaybeToFlowable(maybeSourceArr[0]) : new MaybeConcatArray(maybeSourceArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArrayDelayError(MaybeSource... maybeSourceArr) {
        if (maybeSourceArr.length == 0) {
            return Flowable.empty();
        }
        return RxJavaPlugins.onAssembly(maybeSourceArr.length == 1 ? new MaybeToFlowable(maybeSourceArr[0]) : new MaybeConcatArrayDelayError(maybeSourceArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArrayEager(MaybeSource... maybeSourceArr) {
        return Flowable.fromArray(maybeSourceArr).concatMapEager(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatDelayError(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return Flowable.fromIterable(iterable).concatMapDelayError(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatDelayError(Publisher publisher) {
        return Flowable.fromPublisher(publisher).concatMapDelayError(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatEager(Iterable iterable) {
        return Flowable.fromIterable(iterable).concatMapEager(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatEager(Publisher publisher) {
        return Flowable.fromPublisher(publisher).concatMapEager(MaybeToPublisher.instance());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe create(MaybeOnSubscribe maybeOnSubscribe) {
        ObjectHelper.requireNonNull(maybeOnSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeCreate(maybeOnSubscribe));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe defer(Callable callable) {
        ObjectHelper.requireNonNull(callable, "maybeSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeDefer(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe empty() {
        return RxJavaPlugins.onAssembly((Maybe) MaybeEmpty.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe error(Throwable th) {
        ObjectHelper.requireNonNull(th, "exception is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeError(th));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe error(Callable callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeErrorCallable(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe fromAction(Action action) {
        ObjectHelper.requireNonNull(action, "run is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFromAction(action));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe fromCallable(Callable callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFromCallable(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe fromCompletable(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "completableSource is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFromCompletable(completableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe fromFuture(Future future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFromFuture(future, 0, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe fromFuture(Future future, long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFromFuture(future, j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe fromRunnable(Runnable runnable) {
        ObjectHelper.requireNonNull(runnable, "run is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFromRunnable(runnable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe fromSingle(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "singleSource is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFromSingle(singleSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe just(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeJust(obj));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(MaybeSource maybeSource, MaybeSource maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return mergeArray(maybeSource, maybeSource2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return mergeArray(maybeSource, maybeSource2, maybeSource3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return mergeArray(maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Iterable iterable) {
        return merge((Publisher) Flowable.fromIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Publisher publisher) {
        return merge(publisher, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Publisher publisher, int i) {
        ObjectHelper.requireNonNull(publisher, "source is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        FlowableFlatMapPublisher flowableFlatMapPublisher = new FlowableFlatMapPublisher(publisher, MaybeToPublisher.instance(), false, i, Flowable.bufferSize());
        return RxJavaPlugins.onAssembly((Flowable) flowableFlatMapPublisher);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe merge(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "source is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFlatten(maybeSource, Functions.identity()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeArray(MaybeSource... maybeSourceArr) {
        ObjectHelper.requireNonNull(maybeSourceArr, "sources is null");
        if (maybeSourceArr.length == 0) {
            return Flowable.empty();
        }
        return RxJavaPlugins.onAssembly(maybeSourceArr.length == 1 ? new MaybeToFlowable(maybeSourceArr[0]) : new MaybeMergeArray(maybeSourceArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeArrayDelayError(MaybeSource... maybeSourceArr) {
        return maybeSourceArr.length == 0 ? Flowable.empty() : Flowable.fromArray(maybeSourceArr).flatMap(MaybeToPublisher.instance(), true, maybeSourceArr.length);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(MaybeSource maybeSource, MaybeSource maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return mergeArrayDelayError(maybeSource, maybeSource2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return mergeArrayDelayError(maybeSource, maybeSource2, maybeSource3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return mergeArrayDelayError(maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Iterable iterable) {
        return Flowable.fromIterable(iterable).flatMap(MaybeToPublisher.instance(), true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher) {
        return Flowable.fromPublisher(publisher).flatMap(MaybeToPublisher.instance(), true);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher, int i) {
        return Flowable.fromPublisher(publisher).flatMap(MaybeToPublisher.instance(), true, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe never() {
        return RxJavaPlugins.onAssembly((Maybe) MaybeNever.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(MaybeSource maybeSource, MaybeSource maybeSource2) {
        return sequenceEqual(maybeSource, maybeSource2, ObjectHelper.equalsPredicate());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(MaybeSource maybeSource, MaybeSource maybeSource2, BiPredicate biPredicate) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(biPredicate, "isEqual is null");
        return RxJavaPlugins.onAssembly((Single) new MaybeEqualSingle(maybeSource, maybeSource2, biPredicate));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Maybe timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Maybe timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeTimer(Math.max(0, j), timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe unsafeCreate(MaybeSource maybeSource) {
        if (!(maybeSource instanceof Maybe)) {
            ObjectHelper.requireNonNull(maybeSource, "onSubscribe is null");
            return RxJavaPlugins.onAssembly((Maybe) new MaybeUnsafeCreate(maybeSource));
        }
        throw new IllegalArgumentException("unsafeCreate(Maybe) should be upgraded");
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe using(Callable callable, Function function, Consumer consumer) {
        return using(callable, function, consumer, true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe using(Callable callable, Function function, Consumer consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "sourceSupplier is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeUsing(callable, function, consumer, z));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe wrap(MaybeSource maybeSource) {
        if (maybeSource instanceof Maybe) {
            return RxJavaPlugins.onAssembly((Maybe) maybeSource);
        }
        ObjectHelper.requireNonNull(maybeSource, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeUnsafeCreate(maybeSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4, MaybeSource maybeSource5, MaybeSource maybeSource6, MaybeSource maybeSource7, MaybeSource maybeSource8, MaybeSource maybeSource9, Function9 function9) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        ObjectHelper.requireNonNull(maybeSource7, "source7 is null");
        ObjectHelper.requireNonNull(maybeSource8, "source8 is null");
        ObjectHelper.requireNonNull(maybeSource9, "source9 is null");
        return zipArray(Functions.toFunction(function9), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6, maybeSource7, maybeSource8, maybeSource9);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4, MaybeSource maybeSource5, MaybeSource maybeSource6, MaybeSource maybeSource7, MaybeSource maybeSource8, Function8 function8) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        ObjectHelper.requireNonNull(maybeSource7, "source7 is null");
        ObjectHelper.requireNonNull(maybeSource8, "source8 is null");
        return zipArray(Functions.toFunction(function8), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6, maybeSource7, maybeSource8);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4, MaybeSource maybeSource5, MaybeSource maybeSource6, MaybeSource maybeSource7, Function7 function7) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        ObjectHelper.requireNonNull(maybeSource7, "source7 is null");
        return zipArray(Functions.toFunction(function7), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6, maybeSource7);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4, MaybeSource maybeSource5, MaybeSource maybeSource6, Function6 function6) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        return zipArray(Functions.toFunction(function6), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4, MaybeSource maybeSource5, Function5 function5) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        return zipArray(Functions.toFunction(function5), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, MaybeSource maybeSource4, Function4 function4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return zipArray(Functions.toFunction(function4), maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3, Function3 function3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return zipArray(Functions.toFunction(function3), maybeSource, maybeSource2, maybeSource3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(MaybeSource maybeSource, MaybeSource maybeSource2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), maybeSource, maybeSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zip(Iterable iterable, Function function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeZipIterable(iterable, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Maybe zipArray(Function function, MaybeSource... maybeSourceArr) {
        ObjectHelper.requireNonNull(maybeSourceArr, "sources is null");
        if (maybeSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "zipper is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeZipArray(maybeSourceArr, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe ambWith(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return ambArray(this, maybeSource);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Object as(@NonNull MaybeConverter maybeConverter) {
        ObjectHelper.requireNonNull(maybeConverter, "converter is null");
        return maybeConverter.apply(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingGet() {
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((MaybeObserver) blockingMultiObserver);
        return blockingMultiObserver.blockingGet();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingGet(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultValue is null");
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((MaybeObserver) blockingMultiObserver);
        return blockingMultiObserver.blockingGet(obj);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe cache() {
        return RxJavaPlugins.onAssembly((Maybe) new MaybeCache(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe cast(Class cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe compose(MaybeTransformer maybeTransformer) {
        ObjectHelper.requireNonNull(maybeTransformer, "transformer is null");
        return wrap(maybeTransformer.apply(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe concatMap(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFlatten(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatWith(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return concat((MaybeSource) this, maybeSource);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single contains(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return RxJavaPlugins.onAssembly((Single) new MaybeContains(this, obj));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single count() {
        return RxJavaPlugins.onAssembly((Single) new MaybeCount(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe defaultIfEmpty(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return switchIfEmpty((MaybeSource) just(obj));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        MaybeDelay maybeDelay = new MaybeDelay(this, Math.max(0, j), timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Maybe) maybeDelay);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe delay(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "delayIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeDelayOtherPublisher(this, publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription(Flowable.timer(j, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe delaySubscription(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeDelaySubscriptionOtherPublisher(this, publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doAfterSuccess(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "doAfterSuccess is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeDoAfterSuccess(this, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doAfterTerminate(Action action) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Consumer emptyConsumer3 = Functions.emptyConsumer();
        Action action2 = Functions.EMPTY_ACTION;
        ObjectHelper.requireNonNull(action, "onAfterTerminate is null");
        MaybePeek maybePeek = new MaybePeek(this, emptyConsumer, emptyConsumer2, emptyConsumer3, action2, action, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeDoFinally(this, action));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doOnComplete(Action action) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Consumer emptyConsumer3 = Functions.emptyConsumer();
        ObjectHelper.requireNonNull(action, "onComplete is null");
        Action action2 = action;
        Action action3 = Functions.EMPTY_ACTION;
        MaybePeek maybePeek = new MaybePeek(this, emptyConsumer, emptyConsumer2, emptyConsumer3, action2, action3, action3);
        return RxJavaPlugins.onAssembly((Maybe) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doOnDispose(Action action) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Consumer emptyConsumer3 = Functions.emptyConsumer();
        Action action2 = Functions.EMPTY_ACTION;
        ObjectHelper.requireNonNull(action, "onDispose is null");
        MaybePeek maybePeek = new MaybePeek(this, emptyConsumer, emptyConsumer2, emptyConsumer3, action2, action2, action);
        return RxJavaPlugins.onAssembly((Maybe) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doOnError(Consumer consumer) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        ObjectHelper.requireNonNull(consumer, "onError is null");
        Consumer consumer2 = consumer;
        Action action = Functions.EMPTY_ACTION;
        MaybePeek maybePeek = new MaybePeek(this, emptyConsumer, emptyConsumer2, consumer2, action, action, action);
        return RxJavaPlugins.onAssembly((Maybe) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doOnEvent(BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "onEvent is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeDoOnEvent(this, biConsumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doOnSubscribe(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        Consumer consumer2 = consumer;
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        MaybePeek maybePeek = new MaybePeek(this, consumer2, emptyConsumer, emptyConsumer2, action, action, action);
        return RxJavaPlugins.onAssembly((Maybe) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe doOnSuccess(Consumer consumer) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        Consumer consumer2 = consumer;
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        MaybePeek maybePeek = new MaybePeek(this, emptyConsumer, consumer2, emptyConsumer2, action, action, action);
        return RxJavaPlugins.onAssembly((Maybe) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe filter(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFilter(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe flatMap(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFlatten(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe flatMap(Function function, BiFunction biFunction) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFlatMapBiSelector(this, function, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe flatMap(Function function, Function function2, Callable callable) {
        ObjectHelper.requireNonNull(function, "onSuccessMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFlatMapNotification(this, function, function2, callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new MaybeFlatMapCompletable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMapObservable(Function function) {
        return toObservable().flatMap(function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapPublisher(Function function) {
        return toFlowable().flatMap(function);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single flatMapSingle(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Single) new MaybeFlatMapSingle(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe flatMapSingleElement(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFlatMapSingleElement(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flattenAsFlowable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable) new MaybeFlatMapIterableFlowable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flattenAsObservable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable) new MaybeFlatMapIterableObservable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe hide() {
        return RxJavaPlugins.onAssembly((Maybe) new MaybeHide(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable ignoreElement() {
        return RxJavaPlugins.onAssembly((Completable) new MaybeIgnoreElementCompletable(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single isEmpty() {
        return RxJavaPlugins.onAssembly((Single) new MaybeIsEmptySingle(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe lift(MaybeOperator maybeOperator) {
        ObjectHelper.requireNonNull(maybeOperator, "onLift is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeLift(this, maybeOperator));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe map(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeMap(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable mergeWith(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return merge((MaybeSource) this, maybeSource);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeObserveOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe ofType(Class cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return filter(Functions.isInstanceOf(cls)).cast(cls);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onErrorComplete() {
        return onErrorComplete(Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onErrorComplete(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeOnErrorComplete(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onErrorResumeNext(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "next is null");
        return onErrorResumeNext(Functions.justFunction(maybeSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onErrorResumeNext(Function function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeOnErrorNext(this, function, true));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onErrorReturn(Function function) {
        ObjectHelper.requireNonNull(function, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeOnErrorReturn(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onErrorReturnItem(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return onErrorReturn(Functions.justFunction(obj));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onExceptionResumeNext(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "next is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeOnErrorNext(this, Functions.justFunction(maybeSource), false));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Maybe) new MaybeDetach(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable repeat() {
        return repeat(Long.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable repeat(long j) {
        return toFlowable().repeat(j);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable repeatUntil(BooleanSupplier booleanSupplier) {
        return toFlowable().repeatUntil(booleanSupplier);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable repeatWhen(Function function) {
        return toFlowable().repeatWhen(function);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe retry() {
        return retry(Long.MAX_VALUE, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe retry(long j) {
        return retry(j, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe retry(long j, Predicate predicate) {
        return toFlowable().retry(j, predicate).singleElement();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe retry(BiPredicate biPredicate) {
        return toFlowable().retry(biPredicate).singleElement();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe retry(Predicate predicate) {
        return retry(Long.MAX_VALUE, predicate);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe retryUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return retry(Long.MAX_VALUE, Functions.predicateReverseFor(booleanSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe retryWhen(Function function) {
        return toFlowable().retryWhen(function).singleElement();
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer) {
        return subscribe(consumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2) {
        return subscribe(consumer, consumer2, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2, Action action) {
        ObjectHelper.requireNonNull(consumer, "onSuccess is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        return (Disposable) subscribeWith(new MaybeCallbackObserver(consumer, consumer2, action));
    }

    @SchedulerSupport("none")
    public final void subscribe(MaybeObserver maybeObserver) {
        ObjectHelper.requireNonNull(maybeObserver, "observer is null");
        MaybeObserver onSubscribe = RxJavaPlugins.onSubscribe(this, maybeObserver);
        ObjectHelper.requireNonNull(onSubscribe, "observer returned by the RxJavaPlugins hook is null");
        try {
            subscribeActual(onSubscribe);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            NullPointerException nullPointerException = new NullPointerException("subscribeActual failed");
            nullPointerException.initCause(th);
            throw nullPointerException;
        }
    }

    public abstract void subscribeActual(MaybeObserver maybeObserver);

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeSubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final MaybeObserver subscribeWith(MaybeObserver maybeObserver) {
        subscribe(maybeObserver);
        return maybeObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe switchIfEmpty(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeSwitchIfEmpty(this, maybeSource));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Single switchIfEmpty(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return RxJavaPlugins.onAssembly((Single) new MaybeSwitchIfEmptySingle(this, singleSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe takeUntil(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeTakeUntilMaybe(this, maybeSource));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe takeUntil(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeTakeUntilPublisher(this, publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver test() {
        TestObserver testObserver = new TestObserver();
        subscribe((MaybeObserver) testObserver);
        return testObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver test(boolean z) {
        TestObserver testObserver = new TestObserver();
        if (z) {
            testObserver.cancel();
        }
        subscribe((MaybeObserver) testObserver);
        return testObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe timeout(long j, TimeUnit timeUnit) {
        return timeout(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe timeout(long j, TimeUnit timeUnit, MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return timeout(j, timeUnit, Schedulers.computation(), maybeSource);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout((MaybeSource) timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe timeout(long j, TimeUnit timeUnit, Scheduler scheduler, MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "fallback is null");
        return timeout((MaybeSource) timer(j, timeUnit, scheduler), maybeSource);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe timeout(MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeTimeoutMaybe(this, maybeSource, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe timeout(MaybeSource maybeSource, MaybeSource maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(maybeSource2, "fallback is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeTimeoutMaybe(this, maybeSource, maybeSource2));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe timeout(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeTimeoutPublisher(this, publisher, null));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe timeout(Publisher publisher, MaybeSource maybeSource) {
        ObjectHelper.requireNonNull(publisher, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(maybeSource, "fallback is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeTimeoutPublisher(this, publisher, maybeSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object to(Function function) {
        try {
            ObjectHelper.requireNonNull(function, "convert is null");
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable toFlowable() {
        return this instanceof FuseToFlowable ? ((FuseToFlowable) this).fuseToFlowable() : RxJavaPlugins.onAssembly((Flowable) new MaybeToFlowable(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable toObservable() {
        return this instanceof FuseToObservable ? ((FuseToObservable) this).fuseToObservable() : RxJavaPlugins.onAssembly((Observable) new MaybeToObservable(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSingle() {
        return RxJavaPlugins.onAssembly((Single) new MaybeToSingle(this, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSingle(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultValue is null");
        return RxJavaPlugins.onAssembly((Single) new MaybeToSingle(this, obj));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeUnsubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe zipWith(MaybeSource maybeSource, BiFunction biFunction) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return zip(this, maybeSource, biFunction);
    }
}
