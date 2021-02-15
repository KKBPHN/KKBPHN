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
import io.reactivex.internal.fuseable.FuseToMaybe;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.internal.observers.BlockingMultiObserver;
import io.reactivex.internal.observers.ConsumerSingleObserver;
import io.reactivex.internal.observers.FutureSingleObserver;
import io.reactivex.internal.operators.completable.CompletableFromSingle;
import io.reactivex.internal.operators.completable.CompletableToFlowable;
import io.reactivex.internal.operators.flowable.FlowableConcatMap;
import io.reactivex.internal.operators.flowable.FlowableConcatMapPublisher;
import io.reactivex.internal.operators.flowable.FlowableFlatMapPublisher;
import io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import io.reactivex.internal.operators.maybe.MaybeFilterSingle;
import io.reactivex.internal.operators.maybe.MaybeFromSingle;
import io.reactivex.internal.operators.observable.ObservableConcatMap;
import io.reactivex.internal.operators.observable.ObservableSingleSingle;
import io.reactivex.internal.operators.single.SingleAmb;
import io.reactivex.internal.operators.single.SingleCache;
import io.reactivex.internal.operators.single.SingleContains;
import io.reactivex.internal.operators.single.SingleCreate;
import io.reactivex.internal.operators.single.SingleDefer;
import io.reactivex.internal.operators.single.SingleDelay;
import io.reactivex.internal.operators.single.SingleDelayWithCompletable;
import io.reactivex.internal.operators.single.SingleDelayWithObservable;
import io.reactivex.internal.operators.single.SingleDelayWithPublisher;
import io.reactivex.internal.operators.single.SingleDelayWithSingle;
import io.reactivex.internal.operators.single.SingleDetach;
import io.reactivex.internal.operators.single.SingleDoAfterSuccess;
import io.reactivex.internal.operators.single.SingleDoAfterTerminate;
import io.reactivex.internal.operators.single.SingleDoFinally;
import io.reactivex.internal.operators.single.SingleDoOnDispose;
import io.reactivex.internal.operators.single.SingleDoOnError;
import io.reactivex.internal.operators.single.SingleDoOnEvent;
import io.reactivex.internal.operators.single.SingleDoOnSubscribe;
import io.reactivex.internal.operators.single.SingleDoOnSuccess;
import io.reactivex.internal.operators.single.SingleEquals;
import io.reactivex.internal.operators.single.SingleError;
import io.reactivex.internal.operators.single.SingleFlatMap;
import io.reactivex.internal.operators.single.SingleFlatMapCompletable;
import io.reactivex.internal.operators.single.SingleFlatMapIterableFlowable;
import io.reactivex.internal.operators.single.SingleFlatMapIterableObservable;
import io.reactivex.internal.operators.single.SingleFlatMapMaybe;
import io.reactivex.internal.operators.single.SingleFromCallable;
import io.reactivex.internal.operators.single.SingleFromPublisher;
import io.reactivex.internal.operators.single.SingleFromUnsafeSource;
import io.reactivex.internal.operators.single.SingleHide;
import io.reactivex.internal.operators.single.SingleInternalHelper;
import io.reactivex.internal.operators.single.SingleJust;
import io.reactivex.internal.operators.single.SingleLift;
import io.reactivex.internal.operators.single.SingleMap;
import io.reactivex.internal.operators.single.SingleNever;
import io.reactivex.internal.operators.single.SingleObserveOn;
import io.reactivex.internal.operators.single.SingleOnErrorReturn;
import io.reactivex.internal.operators.single.SingleResumeNext;
import io.reactivex.internal.operators.single.SingleSubscribeOn;
import io.reactivex.internal.operators.single.SingleTakeUntil;
import io.reactivex.internal.operators.single.SingleTimeout;
import io.reactivex.internal.operators.single.SingleTimer;
import io.reactivex.internal.operators.single.SingleToFlowable;
import io.reactivex.internal.operators.single.SingleToObservable;
import io.reactivex.internal.operators.single.SingleUnsubscribeOn;
import io.reactivex.internal.operators.single.SingleUsing;
import io.reactivex.internal.operators.single.SingleZipArray;
import io.reactivex.internal.operators.single.SingleZipIterable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public abstract class Single implements SingleSource {
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single amb(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Single) new SingleAmb(null, iterable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single ambArray(SingleSource... singleSourceArr) {
        return singleSourceArr.length == 0 ? error(SingleInternalHelper.emptyThrower()) : singleSourceArr.length == 1 ? wrap(singleSourceArr[0]) : RxJavaPlugins.onAssembly((Single) new SingleAmb(singleSourceArr, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(SingleSource singleSource, SingleSource singleSource2) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        return concat((Publisher) Flowable.fromArray(singleSource, singleSource2));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        return concat((Publisher) Flowable.fromArray(singleSource, singleSource2, singleSource3));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        return concat((Publisher) Flowable.fromArray(singleSource, singleSource2, singleSource3, singleSource4));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Iterable iterable) {
        return concat((Publisher) Flowable.fromIterable(iterable));
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
        return RxJavaPlugins.onAssembly((Flowable) new FlowableConcatMapPublisher(publisher, SingleInternalHelper.toFlowable(), i, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concat(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableConcatMap(observableSource, SingleInternalHelper.toObservable(), 2, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArray(SingleSource... singleSourceArr) {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableConcatMap(Flowable.fromArray(singleSourceArr), SingleInternalHelper.toFlowable(), 2, ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single create(SingleOnSubscribe singleOnSubscribe) {
        ObjectHelper.requireNonNull(singleOnSubscribe, "source is null");
        return RxJavaPlugins.onAssembly((Single) new SingleCreate(singleOnSubscribe));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single defer(Callable callable) {
        ObjectHelper.requireNonNull(callable, "singleSupplier is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDefer(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single equals(SingleSource singleSource, SingleSource singleSource2) {
        ObjectHelper.requireNonNull(singleSource, "first is null");
        ObjectHelper.requireNonNull(singleSource2, "second is null");
        return RxJavaPlugins.onAssembly((Single) new SingleEquals(singleSource, singleSource2));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single error(Throwable th) {
        ObjectHelper.requireNonNull(th, "error is null");
        return error(Functions.justCallable(th));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single error(Callable callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Single) new SingleError(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single fromCallable(Callable callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly((Single) new SingleFromCallable(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single fromFuture(Future future) {
        return toSingle(Flowable.fromFuture(future));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single fromFuture(Future future, long j, TimeUnit timeUnit) {
        return toSingle(Flowable.fromFuture(future, j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Single fromFuture(Future future, long j, TimeUnit timeUnit, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Single fromFuture(Future future, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single fromObservable(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "observableSource is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableSingleSingle(observableSource, null));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single fromPublisher(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Single) new SingleFromPublisher(publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single just(Object obj) {
        ObjectHelper.requireNonNull(obj, "value is null");
        return RxJavaPlugins.onAssembly((Single) new SingleJust(obj));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(SingleSource singleSource, SingleSource singleSource2) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        return merge((Publisher) Flowable.fromArray(singleSource, singleSource2));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        return merge((Publisher) Flowable.fromArray(singleSource, singleSource2, singleSource3));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        return merge((Publisher) Flowable.fromArray(singleSource, singleSource2, singleSource3, singleSource4));
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
        ObjectHelper.requireNonNull(publisher, "sources is null");
        FlowableFlatMapPublisher flowableFlatMapPublisher = new FlowableFlatMapPublisher(publisher, SingleInternalHelper.toFlowable(), false, Integer.MAX_VALUE, Flowable.bufferSize());
        return RxJavaPlugins.onAssembly((Flowable) flowableFlatMapPublisher);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single merge(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "source is null");
        return RxJavaPlugins.onAssembly((Single) new SingleFlatMap(singleSource, Functions.identity()));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(SingleSource singleSource, SingleSource singleSource2) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        return mergeDelayError((Publisher) Flowable.fromArray(singleSource, singleSource2));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        return mergeDelayError((Publisher) Flowable.fromArray(singleSource, singleSource2, singleSource3));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        return mergeDelayError((Publisher) Flowable.fromArray(singleSource, singleSource2, singleSource3, singleSource4));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Iterable iterable) {
        return mergeDelayError((Publisher) Flowable.fromIterable(iterable));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "sources is null");
        FlowableFlatMapPublisher flowableFlatMapPublisher = new FlowableFlatMapPublisher(publisher, SingleInternalHelper.toFlowable(), true, Integer.MAX_VALUE, Flowable.bufferSize());
        return RxJavaPlugins.onAssembly((Flowable) flowableFlatMapPublisher);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single never() {
        return RxJavaPlugins.onAssembly(SingleNever.INSTANCE);
    }

    private Single timeout0(long j, TimeUnit timeUnit, Scheduler scheduler, SingleSource singleSource) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        SingleTimeout singleTimeout = new SingleTimeout(this, j, timeUnit, scheduler, singleSource);
        return RxJavaPlugins.onAssembly((Single) singleTimeout);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Single timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Single timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single) new SingleTimer(j, timeUnit, scheduler));
    }

    private static Single toSingle(Flowable flowable) {
        return RxJavaPlugins.onAssembly((Single) new FlowableSingleSingle(flowable, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single unsafeCreate(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "onSubscribe is null");
        if (!(singleSource instanceof Single)) {
            return RxJavaPlugins.onAssembly((Single) new SingleFromUnsafeSource(singleSource));
        }
        throw new IllegalArgumentException("unsafeCreate(Single) should be upgraded");
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single using(Callable callable, Function function, Consumer consumer) {
        return using(callable, function, consumer, true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single using(Callable callable, Function function, Consumer consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "singleFunction is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Single) new SingleUsing(callable, function, consumer, z));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single wrap(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "source is null");
        return singleSource instanceof Single ? RxJavaPlugins.onAssembly((Single) singleSource) : RxJavaPlugins.onAssembly((Single) new SingleFromUnsafeSource(singleSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4, SingleSource singleSource5, SingleSource singleSource6, SingleSource singleSource7, SingleSource singleSource8, SingleSource singleSource9, Function9 function9) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        ObjectHelper.requireNonNull(singleSource7, "source7 is null");
        ObjectHelper.requireNonNull(singleSource8, "source8 is null");
        ObjectHelper.requireNonNull(singleSource9, "source9 is null");
        return zipArray(Functions.toFunction(function9), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6, singleSource7, singleSource8, singleSource9);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4, SingleSource singleSource5, SingleSource singleSource6, SingleSource singleSource7, SingleSource singleSource8, Function8 function8) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        ObjectHelper.requireNonNull(singleSource7, "source7 is null");
        ObjectHelper.requireNonNull(singleSource8, "source8 is null");
        return zipArray(Functions.toFunction(function8), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6, singleSource7, singleSource8);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4, SingleSource singleSource5, SingleSource singleSource6, SingleSource singleSource7, Function7 function7) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        ObjectHelper.requireNonNull(singleSource7, "source7 is null");
        return zipArray(Functions.toFunction(function7), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6, singleSource7);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4, SingleSource singleSource5, SingleSource singleSource6, Function6 function6) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        return zipArray(Functions.toFunction(function6), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4, SingleSource singleSource5, Function5 function5) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        return zipArray(Functions.toFunction(function5), singleSource, singleSource2, singleSource3, singleSource4, singleSource5);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, SingleSource singleSource4, Function4 function4) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        return zipArray(Functions.toFunction(function4), singleSource, singleSource2, singleSource3, singleSource4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, SingleSource singleSource3, Function3 function3) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        return zipArray(Functions.toFunction(function3), singleSource, singleSource2, singleSource3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(SingleSource singleSource, SingleSource singleSource2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), singleSource, singleSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zip(Iterable iterable, Function function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Single) new SingleZipIterable(iterable, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single zipArray(Function function, SingleSource... singleSourceArr) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(singleSourceArr, "sources is null");
        return singleSourceArr.length == 0 ? error((Throwable) new NoSuchElementException()) : RxJavaPlugins.onAssembly((Single) new SingleZipArray(singleSourceArr, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single ambWith(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return ambArray(this, singleSource);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Object as(@NonNull SingleConverter singleConverter) {
        ObjectHelper.requireNonNull(singleConverter, "converter is null");
        return singleConverter.apply(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingGet() {
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((SingleObserver) blockingMultiObserver);
        return blockingMultiObserver.blockingGet();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single cache() {
        return RxJavaPlugins.onAssembly((Single) new SingleCache(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single cast(Class cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single compose(SingleTransformer singleTransformer) {
        ObjectHelper.requireNonNull(singleTransformer, "transformer is null");
        return wrap(singleTransformer.apply(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatWith(SingleSource singleSource) {
        return concat((SingleSource) this, singleSource);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single contains(Object obj) {
        return contains(obj, ObjectHelper.equalsPredicate());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single contains(Object obj, BiPredicate biPredicate) {
        ObjectHelper.requireNonNull(obj, "value is null");
        ObjectHelper.requireNonNull(biPredicate, "comparer is null");
        return RxJavaPlugins.onAssembly((Single) new SingleContains(this, obj, biPredicate));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("custom")
    public final Single delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        SingleDelay singleDelay = new SingleDelay(this, j, timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Single) singleDelay);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("io.reactivex:computation")
    public final Single delay(long j, TimeUnit timeUnit, boolean z) {
        return delay(j, timeUnit, Schedulers.computation(), z);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription((ObservableSource) Observable.timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single delaySubscription(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDelayWithCompletable(this, completableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single delaySubscription(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDelayWithObservable(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single delaySubscription(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDelayWithSingle(this, singleSource));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single delaySubscription(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDelayWithPublisher(this, publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doAfterSuccess(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "doAfterSuccess is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoAfterSuccess(this, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doAfterTerminate(Action action) {
        ObjectHelper.requireNonNull(action, "onAfterTerminate is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoAfterTerminate(this, action));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoFinally(this, action));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doOnDispose(Action action) {
        ObjectHelper.requireNonNull(action, "onDispose is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoOnDispose(this, action));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doOnError(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onError is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoOnError(this, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doOnEvent(BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "onEvent is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoOnEvent(this, biConsumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doOnSubscribe(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoOnSubscribe(this, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single doOnSuccess(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onSuccess is null");
        return RxJavaPlugins.onAssembly((Single) new SingleDoOnSuccess(this, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe filter(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe) new MaybeFilterSingle(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single flatMap(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Single) new SingleFlatMap(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new SingleFlatMapCompletable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe flatMapMaybe(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe) new SingleFlatMapMaybe(this, function));
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

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flattenAsFlowable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable) new SingleFlatMapIterableFlowable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flattenAsObservable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable) new SingleFlatMapIterableObservable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single hide() {
        return RxJavaPlugins.onAssembly((Single) new SingleHide(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single lift(SingleOperator singleOperator) {
        ObjectHelper.requireNonNull(singleOperator, "onLift is null");
        return RxJavaPlugins.onAssembly((Single) new SingleLift(this, singleOperator));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single map(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Single) new SingleMap(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable mergeWith(SingleSource singleSource) {
        return merge(this, singleSource);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single) new SingleObserveOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single onErrorResumeNext(Single single) {
        ObjectHelper.requireNonNull(single, "resumeSingleInCaseOfError is null");
        return onErrorResumeNext(Functions.justFunction(single));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single onErrorResumeNext(Function function) {
        ObjectHelper.requireNonNull(function, "resumeFunctionInCaseOfError is null");
        return RxJavaPlugins.onAssembly((Single) new SingleResumeNext(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single onErrorReturn(Function function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Single) new SingleOnErrorReturn(this, function, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single onErrorReturnItem(Object obj) {
        ObjectHelper.requireNonNull(obj, "value is null");
        return RxJavaPlugins.onAssembly((Single) new SingleOnErrorReturn(this, null, obj));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Single onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Single) new SingleDetach(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable repeat() {
        return toFlowable().repeat();
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
    public final Single retry() {
        return toSingle(toFlowable().retry());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single retry(long j) {
        return toSingle(toFlowable().retry(j));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Single retry(long j, Predicate predicate) {
        return toSingle(toFlowable().retry(j, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single retry(BiPredicate biPredicate) {
        return toSingle(toFlowable().retry(biPredicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single retry(Predicate predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single retryWhen(Function function) {
        return toSingle(toFlowable().retryWhen(function));
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "onCallback is null");
        BiConsumerSingleObserver biConsumerSingleObserver = new BiConsumerSingleObserver(biConsumer);
        subscribe((SingleObserver) biConsumerSingleObserver);
        return biConsumerSingleObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer) {
        return subscribe(consumer, Functions.ON_ERROR_MISSING);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2) {
        ObjectHelper.requireNonNull(consumer, "onSuccess is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ConsumerSingleObserver consumerSingleObserver = new ConsumerSingleObserver(consumer, consumer2);
        subscribe((SingleObserver) consumerSingleObserver);
        return consumerSingleObserver;
    }

    @SchedulerSupport("none")
    public final void subscribe(SingleObserver singleObserver) {
        ObjectHelper.requireNonNull(singleObserver, "subscriber is null");
        SingleObserver onSubscribe = RxJavaPlugins.onSubscribe(this, singleObserver);
        ObjectHelper.requireNonNull(onSubscribe, "subscriber returned by the RxJavaPlugins hook is null");
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

    public abstract void subscribeActual(@NonNull SingleObserver singleObserver);

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single) new SingleSubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final SingleObserver subscribeWith(SingleObserver singleObserver) {
        subscribe(singleObserver);
        return singleObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single takeUntil(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return takeUntil((Publisher) new CompletableToFlowable(completableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single takeUntil(SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return takeUntil((Publisher) new SingleToFlowable(singleSource));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single takeUntil(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Single) new SingleTakeUntil(this, publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver test() {
        TestObserver testObserver = new TestObserver();
        subscribe((SingleObserver) testObserver);
        return testObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver test(boolean z) {
        TestObserver testObserver = new TestObserver();
        if (z) {
            testObserver.cancel();
        }
        subscribe((SingleObserver) testObserver);
        return testObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, Schedulers.computation(), null);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, scheduler, null);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single timeout(long j, TimeUnit timeUnit, Scheduler scheduler, SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return timeout0(j, timeUnit, scheduler, singleSource);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single timeout(long j, TimeUnit timeUnit, SingleSource singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return timeout0(j, timeUnit, Schedulers.computation(), singleSource);
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

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable toCompletable() {
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromSingle(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable toFlowable() {
        return this instanceof FuseToFlowable ? ((FuseToFlowable) this).fuseToFlowable() : RxJavaPlugins.onAssembly((Flowable) new SingleToFlowable(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Future toFuture() {
        return (Future) subscribeWith(new FutureSingleObserver());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe toMaybe() {
        return this instanceof FuseToMaybe ? ((FuseToMaybe) this).fuseToMaybe() : RxJavaPlugins.onAssembly((Maybe) new MaybeFromSingle(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable toObservable() {
        return this instanceof FuseToObservable ? ((FuseToObservable) this).fuseToObservable() : RxJavaPlugins.onAssembly((Observable) new SingleToObservable(this));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("custom")
    public final Single unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single) new SingleUnsubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single zipWith(SingleSource singleSource, BiFunction biFunction) {
        return zip(this, singleSource, biFunction);
    }
}
