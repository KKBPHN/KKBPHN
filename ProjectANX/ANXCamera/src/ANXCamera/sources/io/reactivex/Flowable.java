package io.reactivex;

import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.Beta;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.flowables.ConnectableFlowable;
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
import io.reactivex.functions.LongConsumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.operators.flowable.BlockingFlowableIterable;
import io.reactivex.internal.operators.flowable.BlockingFlowableLatest;
import io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent;
import io.reactivex.internal.operators.flowable.BlockingFlowableNext;
import io.reactivex.internal.operators.flowable.FlowableAllSingle;
import io.reactivex.internal.operators.flowable.FlowableAmb;
import io.reactivex.internal.operators.flowable.FlowableAnySingle;
import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe;
import io.reactivex.internal.operators.flowable.FlowableBuffer;
import io.reactivex.internal.operators.flowable.FlowableBufferBoundary;
import io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier;
import io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary;
import io.reactivex.internal.operators.flowable.FlowableBufferTimed;
import io.reactivex.internal.operators.flowable.FlowableCache;
import io.reactivex.internal.operators.flowable.FlowableCollectSingle;
import io.reactivex.internal.operators.flowable.FlowableCombineLatest;
import io.reactivex.internal.operators.flowable.FlowableConcatArray;
import io.reactivex.internal.operators.flowable.FlowableConcatMap;
import io.reactivex.internal.operators.flowable.FlowableConcatMapEager;
import io.reactivex.internal.operators.flowable.FlowableConcatMapEagerPublisher;
import io.reactivex.internal.operators.flowable.FlowableCountSingle;
import io.reactivex.internal.operators.flowable.FlowableCreate;
import io.reactivex.internal.operators.flowable.FlowableDebounce;
import io.reactivex.internal.operators.flowable.FlowableDebounceTimed;
import io.reactivex.internal.operators.flowable.FlowableDefer;
import io.reactivex.internal.operators.flowable.FlowableDelay;
import io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther;
import io.reactivex.internal.operators.flowable.FlowableDematerialize;
import io.reactivex.internal.operators.flowable.FlowableDetach;
import io.reactivex.internal.operators.flowable.FlowableDistinct;
import io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged;
import io.reactivex.internal.operators.flowable.FlowableDoAfterNext;
import io.reactivex.internal.operators.flowable.FlowableDoFinally;
import io.reactivex.internal.operators.flowable.FlowableDoOnEach;
import io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle;
import io.reactivex.internal.operators.flowable.FlowableElementAtMaybe;
import io.reactivex.internal.operators.flowable.FlowableElementAtSingle;
import io.reactivex.internal.operators.flowable.FlowableEmpty;
import io.reactivex.internal.operators.flowable.FlowableError;
import io.reactivex.internal.operators.flowable.FlowableFilter;
import io.reactivex.internal.operators.flowable.FlowableFlatMap;
import io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable;
import io.reactivex.internal.operators.flowable.FlowableFlatMapMaybe;
import io.reactivex.internal.operators.flowable.FlowableFlatMapSingle;
import io.reactivex.internal.operators.flowable.FlowableFlattenIterable;
import io.reactivex.internal.operators.flowable.FlowableFromArray;
import io.reactivex.internal.operators.flowable.FlowableFromCallable;
import io.reactivex.internal.operators.flowable.FlowableFromFuture;
import io.reactivex.internal.operators.flowable.FlowableFromIterable;
import io.reactivex.internal.operators.flowable.FlowableFromPublisher;
import io.reactivex.internal.operators.flowable.FlowableGenerate;
import io.reactivex.internal.operators.flowable.FlowableGroupBy;
import io.reactivex.internal.operators.flowable.FlowableGroupJoin;
import io.reactivex.internal.operators.flowable.FlowableHide;
import io.reactivex.internal.operators.flowable.FlowableIgnoreElements;
import io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper.RequestMax;
import io.reactivex.internal.operators.flowable.FlowableInterval;
import io.reactivex.internal.operators.flowable.FlowableIntervalRange;
import io.reactivex.internal.operators.flowable.FlowableJoin;
import io.reactivex.internal.operators.flowable.FlowableJust;
import io.reactivex.internal.operators.flowable.FlowableLastMaybe;
import io.reactivex.internal.operators.flowable.FlowableLastSingle;
import io.reactivex.internal.operators.flowable.FlowableLift;
import io.reactivex.internal.operators.flowable.FlowableLimit;
import io.reactivex.internal.operators.flowable.FlowableMap;
import io.reactivex.internal.operators.flowable.FlowableMapNotification;
import io.reactivex.internal.operators.flowable.FlowableMaterialize;
import io.reactivex.internal.operators.flowable.FlowableNever;
import io.reactivex.internal.operators.flowable.FlowableObserveOn;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;
import io.reactivex.internal.operators.flowable.FlowableOnErrorNext;
import io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
import io.reactivex.internal.operators.flowable.FlowablePublish;
import io.reactivex.internal.operators.flowable.FlowablePublishMulticast;
import io.reactivex.internal.operators.flowable.FlowableRange;
import io.reactivex.internal.operators.flowable.FlowableRangeLong;
import io.reactivex.internal.operators.flowable.FlowableReduceMaybe;
import io.reactivex.internal.operators.flowable.FlowableReduceSeedSingle;
import io.reactivex.internal.operators.flowable.FlowableReduceWithSingle;
import io.reactivex.internal.operators.flowable.FlowableRepeat;
import io.reactivex.internal.operators.flowable.FlowableRepeatUntil;
import io.reactivex.internal.operators.flowable.FlowableRepeatWhen;
import io.reactivex.internal.operators.flowable.FlowableReplay;
import io.reactivex.internal.operators.flowable.FlowableRetryBiPredicate;
import io.reactivex.internal.operators.flowable.FlowableRetryPredicate;
import io.reactivex.internal.operators.flowable.FlowableRetryWhen;
import io.reactivex.internal.operators.flowable.FlowableSamplePublisher;
import io.reactivex.internal.operators.flowable.FlowableSampleTimed;
import io.reactivex.internal.operators.flowable.FlowableScalarXMap;
import io.reactivex.internal.operators.flowable.FlowableScan;
import io.reactivex.internal.operators.flowable.FlowableScanSeed;
import io.reactivex.internal.operators.flowable.FlowableSequenceEqualSingle;
import io.reactivex.internal.operators.flowable.FlowableSerialized;
import io.reactivex.internal.operators.flowable.FlowableSingleMaybe;
import io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import io.reactivex.internal.operators.flowable.FlowableSkip;
import io.reactivex.internal.operators.flowable.FlowableSkipLast;
import io.reactivex.internal.operators.flowable.FlowableSkipLastTimed;
import io.reactivex.internal.operators.flowable.FlowableSkipUntil;
import io.reactivex.internal.operators.flowable.FlowableSkipWhile;
import io.reactivex.internal.operators.flowable.FlowableSubscribeOn;
import io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty;
import io.reactivex.internal.operators.flowable.FlowableSwitchMap;
import io.reactivex.internal.operators.flowable.FlowableTake;
import io.reactivex.internal.operators.flowable.FlowableTakeLast;
import io.reactivex.internal.operators.flowable.FlowableTakeLastOne;
import io.reactivex.internal.operators.flowable.FlowableTakeLastTimed;
import io.reactivex.internal.operators.flowable.FlowableTakeUntil;
import io.reactivex.internal.operators.flowable.FlowableTakeUntilPredicate;
import io.reactivex.internal.operators.flowable.FlowableTakeWhile;
import io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;
import io.reactivex.internal.operators.flowable.FlowableTimeInterval;
import io.reactivex.internal.operators.flowable.FlowableTimeout;
import io.reactivex.internal.operators.flowable.FlowableTimeoutTimed;
import io.reactivex.internal.operators.flowable.FlowableTimer;
import io.reactivex.internal.operators.flowable.FlowableToListSingle;
import io.reactivex.internal.operators.flowable.FlowableUnsubscribeOn;
import io.reactivex.internal.operators.flowable.FlowableUsing;
import io.reactivex.internal.operators.flowable.FlowableWindow;
import io.reactivex.internal.operators.flowable.FlowableWindowBoundary;
import io.reactivex.internal.operators.flowable.FlowableWindowBoundarySelector;
import io.reactivex.internal.operators.flowable.FlowableWindowBoundarySupplier;
import io.reactivex.internal.operators.flowable.FlowableWindowTimed;
import io.reactivex.internal.operators.flowable.FlowableWithLatestFrom;
import io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany;
import io.reactivex.internal.operators.flowable.FlowableZip;
import io.reactivex.internal.operators.flowable.FlowableZipIterable;
import io.reactivex.internal.operators.observable.ObservableFromPublisher;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.internal.subscribers.BlockingFirstSubscriber;
import io.reactivex.internal.subscribers.BlockingLastSubscriber;
import io.reactivex.internal.subscribers.ForEachWhileSubscriber;
import io.reactivex.internal.subscribers.FutureSubscriber;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import io.reactivex.internal.subscribers.StrictSubscriber;
import io.reactivex.internal.util.ArrayListSupplier;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.HashMapSupplier;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.SafeSubscriber;
import io.reactivex.subscribers.TestSubscriber;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public abstract class Flowable implements Publisher {
    static final int BUFFER_SIZE = Math.max(1, Integer.getInteger("rx2.buffer-size", 128).intValue());

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable amb(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableAmb(null, iterable));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable ambArray(Publisher... publisherArr) {
        ObjectHelper.requireNonNull(publisherArr, "sources is null");
        int length = publisherArr.length;
        return length == 0 ? empty() : length == 1 ? fromPublisher(publisherArr[0]) : RxJavaPlugins.onAssembly((Flowable) new FlowableAmb(publisherArr, null));
    }

    public static int bufferSize() {
        return BUFFER_SIZE;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Function function, Publisher... publisherArr) {
        return combineLatest(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Iterable iterable, Function function) {
        return combineLatest(iterable, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Iterable iterable, Function function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableCombineLatest(iterable, function, i, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return combineLatest(Functions.toFunction(biFunction), publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, Publisher publisher3, Function3 function3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return combineLatest(Functions.toFunction(function3), publisher, publisher2, publisher3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Function4 function4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return combineLatest(Functions.toFunction(function4), publisher, publisher2, publisher3, publisher4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Function5 function5) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        return combineLatest(Functions.toFunction(function5), publisher, publisher2, publisher3, publisher4, publisher5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Function6 function6) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        return combineLatest(Functions.toFunction(function6), publisher, publisher2, publisher3, publisher4, publisher5, publisher6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Publisher publisher7, Function7 function7) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        return combineLatest(Functions.toFunction(function7), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Publisher publisher7, Publisher publisher8, Function8 function8) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        return combineLatest(Functions.toFunction(function8), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Publisher publisher7, Publisher publisher8, Publisher publisher9, Function9 function9) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        ObjectHelper.requireNonNull(publisher9, "source9 is null");
        return combineLatest(Functions.toFunction(function9), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8, publisher9);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher[] publisherArr, Function function) {
        return combineLatest(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatest(Publisher[] publisherArr, Function function, int i) {
        ObjectHelper.requireNonNull(publisherArr, "sources is null");
        if (publisherArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableCombineLatest(publisherArr, function, i, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatestDelayError(Function function, int i, Publisher... publisherArr) {
        return combineLatestDelayError(publisherArr, function, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatestDelayError(Function function, Publisher... publisherArr) {
        return combineLatestDelayError(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatestDelayError(Iterable iterable, Function function) {
        return combineLatestDelayError(iterable, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatestDelayError(Iterable iterable, Function function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableCombineLatest(iterable, function, i, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatestDelayError(Publisher[] publisherArr, Function function) {
        return combineLatestDelayError(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable combineLatestDelayError(Publisher[] publisherArr, Function function, int i) {
        ObjectHelper.requireNonNull(publisherArr, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return publisherArr.length == 0 ? empty() : RxJavaPlugins.onAssembly((Flowable) new FlowableCombineLatest(publisherArr, function, i, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return fromIterable(iterable).concatMapDelayError(Functions.identity(), 2, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Publisher publisher) {
        return concat(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Publisher publisher, int i) {
        return fromPublisher(publisher).concatMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Publisher publisher, Publisher publisher2) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return concatArray(publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Publisher publisher, Publisher publisher2, Publisher publisher3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return concatArray(publisher, publisher2, publisher3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concat(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return concatArray(publisher, publisher2, publisher3, publisher4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArray(Publisher... publisherArr) {
        return publisherArr.length == 0 ? empty() : publisherArr.length == 1 ? fromPublisher(publisherArr[0]) : RxJavaPlugins.onAssembly((Flowable) new FlowableConcatArray(publisherArr, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArrayDelayError(Publisher... publisherArr) {
        return publisherArr.length == 0 ? empty() : publisherArr.length == 1 ? fromPublisher(publisherArr[0]) : RxJavaPlugins.onAssembly((Flowable) new FlowableConcatArray(publisherArr, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArrayEager(int i, int i2, Publisher... publisherArr) {
        ObjectHelper.requireNonNull(publisherArr, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(new FlowableFromArray(publisherArr), Functions.identity(), i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatArrayEager(Publisher... publisherArr) {
        return concatArrayEager(bufferSize(), bufferSize(), publisherArr);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatDelayError(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return fromIterable(iterable).concatMapDelayError(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatDelayError(Publisher publisher) {
        return concatDelayError(publisher, bufferSize(), true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatDelayError(Publisher publisher, int i, boolean z) {
        return fromPublisher(publisher).concatMapDelayError(Functions.identity(), i, z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatEager(Iterable iterable) {
        return concatEager(iterable, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatEager(Iterable iterable, int i, int i2) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(new FlowableFromIterable(iterable), Functions.identity(), i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatEager(Publisher publisher) {
        return concatEager(publisher, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable concatEager(Publisher publisher, int i, int i2) {
        ObjectHelper.requireNonNull(publisher, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        FlowableConcatMapEagerPublisher flowableConcatMapEagerPublisher = new FlowableConcatMapEagerPublisher(publisher, Functions.identity(), i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable) flowableConcatMapEagerPublisher);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable create(FlowableOnSubscribe flowableOnSubscribe, BackpressureStrategy backpressureStrategy) {
        ObjectHelper.requireNonNull(flowableOnSubscribe, "source is null");
        ObjectHelper.requireNonNull(backpressureStrategy, "mode is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableCreate(flowableOnSubscribe, backpressureStrategy));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable defer(Callable callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDefer(callable));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    private Flowable doOnEach(Consumer consumer, Consumer consumer2, Action action, Action action2) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(action2, "onAfterTerminate is null");
        FlowableDoOnEach flowableDoOnEach = new FlowableDoOnEach(this, consumer, consumer2, action, action2);
        return RxJavaPlugins.onAssembly((Flowable) flowableDoOnEach);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable empty() {
        return RxJavaPlugins.onAssembly(FlowableEmpty.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable error(Throwable th) {
        ObjectHelper.requireNonNull(th, "throwable is null");
        return error(Functions.justCallable(th));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable error(Callable callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableError(callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable fromArray(Object... objArr) {
        ObjectHelper.requireNonNull(objArr, "items is null");
        return objArr.length == 0 ? empty() : objArr.length == 1 ? just(objArr[0]) : RxJavaPlugins.onAssembly((Flowable) new FlowableFromArray(objArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable fromCallable(Callable callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFromCallable(callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable fromFuture(Future future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFromFuture(future, 0, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable fromFuture(Future future, long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFromFuture(future, j, timeUnit));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Flowable fromFuture(Future future, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, j, timeUnit).subscribeOn(scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Flowable fromFuture(Future future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable fromIterable(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "source is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFromIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable fromPublisher(Publisher publisher) {
        if (publisher instanceof Flowable) {
            return RxJavaPlugins.onAssembly((Flowable) publisher);
        }
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFromPublisher(publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable generate(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "generator is null");
        return generate(Functions.nullSupplier(), FlowableInternalHelper.simpleGenerator(consumer), Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable generate(Callable callable, BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator is null");
        return generate(callable, FlowableInternalHelper.simpleBiGenerator(biConsumer), Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable generate(Callable callable, BiConsumer biConsumer, Consumer consumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator is null");
        return generate(callable, FlowableInternalHelper.simpleBiGenerator(biConsumer), consumer);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable generate(Callable callable, BiFunction biFunction) {
        return generate(callable, biFunction, Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable generate(Callable callable, BiFunction biFunction, Consumer consumer) {
        ObjectHelper.requireNonNull(callable, "initialState is null");
        ObjectHelper.requireNonNull(biFunction, "generator is null");
        ObjectHelper.requireNonNull(consumer, "disposeState is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableGenerate(callable, biFunction, consumer));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable interval(long j, long j2, TimeUnit timeUnit) {
        return interval(j, j2, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Flowable interval(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableInterval flowableInterval = new FlowableInterval(Math.max(0, j), Math.max(0, j2), timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable) flowableInterval);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable interval(long j, TimeUnit timeUnit) {
        return interval(j, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Flowable interval(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return interval(j, j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit) {
        return intervalRange(j, j2, j3, j4, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Flowable intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit, Scheduler scheduler) {
        long j5 = j2;
        long j6 = j3;
        TimeUnit timeUnit2 = timeUnit;
        Scheduler scheduler2 = scheduler;
        int i = (j5 > 0 ? 1 : (j5 == 0 ? 0 : -1));
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(j5);
            throw new IllegalArgumentException(sb.toString());
        } else if (i == 0) {
            return empty().delay(j6, timeUnit2, scheduler2);
        } else {
            long j7 = j + (j5 - 1);
            if (j <= 0 || j7 >= 0) {
                ObjectHelper.requireNonNull(timeUnit2, "unit is null");
                ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
                FlowableIntervalRange flowableIntervalRange = new FlowableIntervalRange(j, j7, Math.max(0, j6), Math.max(0, j4), timeUnit, scheduler);
                return RxJavaPlugins.onAssembly((Flowable) flowableIntervalRange);
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableJust(obj));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        return fromArray(obj, obj2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        return fromArray(obj, obj2, obj3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3, Object obj4) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        return fromArray(obj, obj2, obj3, obj4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        ObjectHelper.requireNonNull(obj7, "The seventh item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6, obj7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        ObjectHelper.requireNonNull(obj7, "The seventh item is null");
        ObjectHelper.requireNonNull(obj8, "The eighth item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        ObjectHelper.requireNonNull(obj7, "The seventh item is null");
        ObjectHelper.requireNonNull(obj8, "The eighth item is null");
        ObjectHelper.requireNonNull(obj9, "The ninth is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        ObjectHelper.requireNonNull(obj7, "The seventh item is null");
        ObjectHelper.requireNonNull(obj8, "The eighth item is null");
        ObjectHelper.requireNonNull(obj9, "The ninth item is null");
        ObjectHelper.requireNonNull(obj10, "The tenth item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Iterable iterable) {
        return fromIterable(iterable).flatMap(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Iterable iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Iterable iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), false, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Publisher publisher) {
        return merge(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Publisher publisher, int i) {
        return fromPublisher(publisher).flatMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Publisher publisher, Publisher publisher2) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return fromArray(publisher, publisher2).flatMap(Functions.identity(), false, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Publisher publisher, Publisher publisher2, Publisher publisher3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return fromArray(publisher, publisher2, publisher3).flatMap(Functions.identity(), false, 3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable merge(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return fromArray(publisher, publisher2, publisher3, publisher4).flatMap(Functions.identity(), false, 4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeArray(int i, int i2, Publisher... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), false, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeArray(Publisher... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), publisherArr.length);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeArrayDelayError(int i, int i2, Publisher... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), true, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeArrayDelayError(Publisher... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), true, publisherArr.length);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Iterable iterable) {
        return fromIterable(iterable).flatMap(Functions.identity(), true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Iterable iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Iterable iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher) {
        return mergeDelayError(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher, int i) {
        return fromPublisher(publisher).flatMap(Functions.identity(), true, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher, Publisher publisher2) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return fromArray(publisher, publisher2).flatMap(Functions.identity(), true, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher, Publisher publisher2, Publisher publisher3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return fromArray(publisher, publisher2, publisher3).flatMap(Functions.identity(), true, 3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable mergeDelayError(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return fromArray(publisher, publisher2, publisher3, publisher4).flatMap(Functions.identity(), true, 4);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable never() {
        return RxJavaPlugins.onAssembly(FlowableNever.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable range(int i, int i2) {
        if (i2 < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(i2);
            throw new IllegalArgumentException(sb.toString());
        } else if (i2 == 0) {
            return empty();
        } else {
            if (i2 == 1) {
                return just(Integer.valueOf(i));
            }
            if (((long) i) + ((long) (i2 - 1)) <= 2147483647L) {
                return RxJavaPlugins.onAssembly((Flowable) new FlowableRange(i, i2));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable rangeLong(long j, long j2) {
        int i = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(j2);
            throw new IllegalArgumentException(sb.toString());
        } else if (i == 0) {
            return empty();
        } else {
            if (j2 == 1) {
                return just(Long.valueOf(j));
            }
            long j3 = (j2 - 1) + j;
            if (j <= 0 || j3 >= 0) {
                return RxJavaPlugins.onAssembly((Flowable) new FlowableRangeLong(j, j2));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(Publisher publisher, Publisher publisher2) {
        return sequenceEqual(publisher, publisher2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(Publisher publisher, Publisher publisher2, int i) {
        return sequenceEqual(publisher, publisher2, ObjectHelper.equalsPredicate(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(Publisher publisher, Publisher publisher2, BiPredicate biPredicate) {
        return sequenceEqual(publisher, publisher2, biPredicate, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(Publisher publisher, Publisher publisher2, BiPredicate biPredicate, int i) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(biPredicate, "isEqual is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Single) new FlowableSequenceEqualSingle(publisher, publisher2, biPredicate, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable switchOnNext(Publisher publisher) {
        return fromPublisher(publisher).switchMap(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable switchOnNext(Publisher publisher, int i) {
        return fromPublisher(publisher).switchMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable switchOnNextDelayError(Publisher publisher) {
        return switchOnNextDelayError(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable switchOnNextDelayError(Publisher publisher, int i) {
        return fromPublisher(publisher).switchMapDelayError(Functions.identity(), i);
    }

    private Flowable timeout0(long j, TimeUnit timeUnit, Publisher publisher, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableTimeoutTimed flowableTimeoutTimed = new FlowableTimeoutTimed(this, j, timeUnit, scheduler, publisher);
        return RxJavaPlugins.onAssembly((Flowable) flowableTimeoutTimed);
    }

    private Flowable timeout0(Publisher publisher, Function function, Publisher publisher2) {
        ObjectHelper.requireNonNull(function, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableTimeout(this, publisher, function, publisher2));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Flowable timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableTimer(Math.max(0, j), timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable unsafeCreate(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "onSubscribe is null");
        if (!(publisher instanceof Flowable)) {
            return RxJavaPlugins.onAssembly((Flowable) new FlowableFromPublisher(publisher));
        }
        throw new IllegalArgumentException("unsafeCreate(Flowable) should be upgraded");
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable using(Callable callable, Function function, Consumer consumer) {
        return using(callable, function, consumer, true);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable using(Callable callable, Function function, Consumer consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "sourceSupplier is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableUsing(callable, function, consumer, z));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Iterable iterable, Function function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        FlowableZip flowableZip = new FlowableZip(null, iterable, function, bufferSize(), false);
        return RxJavaPlugins.onAssembly((Flowable) flowableZip);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Function function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        return fromPublisher(publisher).toList().flatMapPublisher(FlowableInternalHelper.zipIterable(function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), false, bufferSize(), publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, BiFunction biFunction, boolean z) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, bufferSize(), publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, BiFunction biFunction, boolean z, int i) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, i, publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, Publisher publisher3, Function3 function3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return zipArray(Functions.toFunction(function3), false, bufferSize(), publisher, publisher2, publisher3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Function4 function4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return zipArray(Functions.toFunction(function4), false, bufferSize(), publisher, publisher2, publisher3, publisher4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Function5 function5) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        return zipArray(Functions.toFunction(function5), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Function6 function6) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        return zipArray(Functions.toFunction(function6), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Publisher publisher7, Function7 function7) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        return zipArray(Functions.toFunction(function7), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Publisher publisher7, Publisher publisher8, Function8 function8) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        return zipArray(Functions.toFunction(function8), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zip(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Publisher publisher5, Publisher publisher6, Publisher publisher7, Publisher publisher8, Publisher publisher9, Function9 function9) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        ObjectHelper.requireNonNull(publisher9, "source9 is null");
        return zipArray(Functions.toFunction(function9), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8, publisher9);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zipArray(Function function, boolean z, int i, Publisher... publisherArr) {
        if (publisherArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableZip flowableZip = new FlowableZip(publisherArr, null, function, i, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableZip);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Flowable zipIterable(Iterable iterable, Function function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableZip flowableZip = new FlowableZip(null, iterable, function, i, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableZip);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single all(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single) new FlowableAllSingle(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable ambWith(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return ambArray(this, publisher);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single any(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single) new FlowableAnySingle(this, predicate));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Object as(@NonNull FlowableConverter flowableConverter) {
        ObjectHelper.requireNonNull(flowableConverter, "converter is null");
        return flowableConverter.apply(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingFirst() {
        BlockingFirstSubscriber blockingFirstSubscriber = new BlockingFirstSubscriber();
        subscribe((FlowableSubscriber) blockingFirstSubscriber);
        Object blockingGet = blockingFirstSubscriber.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingFirst(Object obj) {
        BlockingFirstSubscriber blockingFirstSubscriber = new BlockingFirstSubscriber();
        subscribe((FlowableSubscriber) blockingFirstSubscriber);
        Object blockingGet = blockingFirstSubscriber.blockingGet();
        return blockingGet != null ? blockingGet : obj;
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingForEach(Consumer consumer) {
        Iterator it = blockingIterable().iterator();
        while (it.hasNext()) {
            try {
                consumer.accept(it.next());
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                ((Disposable) it).dispose();
                throw ExceptionHelper.wrapOrThrow(th);
            }
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingIterable(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return new BlockingFlowableIterable(this, i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingLast() {
        BlockingLastSubscriber blockingLastSubscriber = new BlockingLastSubscriber();
        subscribe((FlowableSubscriber) blockingLastSubscriber);
        Object blockingGet = blockingLastSubscriber.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingLast(Object obj) {
        BlockingLastSubscriber blockingLastSubscriber = new BlockingLastSubscriber();
        subscribe((FlowableSubscriber) blockingLastSubscriber);
        Object blockingGet = blockingLastSubscriber.blockingGet();
        return blockingGet != null ? blockingGet : obj;
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingLatest() {
        return new BlockingFlowableLatest(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingMostRecent(Object obj) {
        return new BlockingFlowableMostRecent(this, obj);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingNext() {
        return new BlockingFlowableNext(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingSingle() {
        return singleOrError().blockingGet();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingSingle(Object obj) {
        return single(obj).blockingGet();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe() {
        FlowableBlockingSubscribe.subscribe(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer consumer) {
        FlowableBlockingSubscribe.subscribe(this, consumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer consumer, Consumer consumer2) {
        FlowableBlockingSubscribe.subscribe(this, consumer, consumer2, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer consumer, Consumer consumer2, Action action) {
        FlowableBlockingSubscribe.subscribe(this, consumer, consumer2, action);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Subscriber subscriber) {
        FlowableBlockingSubscribe.subscribe(this, subscriber);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(int i) {
        return buffer(i, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(int i, int i2) {
        return buffer(i, i2, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(int i, int i2, Callable callable) {
        ObjectHelper.verifyPositive(i, "count");
        ObjectHelper.verifyPositive(i2, "skip");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableBuffer(this, i, i2, callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(int i, Callable callable) {
        return buffer(i, i, callable);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable buffer(long j, long j2, TimeUnit timeUnit) {
        return buffer(j, j2, timeUnit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, j2, timeUnit, scheduler, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, Callable callable) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        FlowableBufferTimed flowableBufferTimed = new FlowableBufferTimed(this, j, j2, timeUnit2, scheduler2, callable2, Integer.MAX_VALUE, false);
        return RxJavaPlugins.onAssembly((Flowable) flowableBufferTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable buffer(long j, TimeUnit timeUnit) {
        return buffer(j, timeUnit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable buffer(long j, TimeUnit timeUnit, int i) {
        return buffer(j, timeUnit, Schedulers.computation(), i);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable buffer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, timeUnit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        return buffer(j, timeUnit, scheduler, i, ArrayListSupplier.asCallable(), false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i, Callable callable, boolean z) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "count");
        FlowableBufferTimed flowableBufferTimed = new FlowableBufferTimed(this, j, j, timeUnit2, scheduler2, callable2, i2, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableBufferTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(Flowable flowable, Function function) {
        return buffer(flowable, function, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(Flowable flowable, Function function, Callable callable) {
        ObjectHelper.requireNonNull(flowable, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableBufferBoundary(this, flowable, function, callable));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(Callable callable) {
        return buffer(callable, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(Callable callable, Callable callable2) {
        ObjectHelper.requireNonNull(callable, "boundaryIndicatorSupplier is null");
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableBufferBoundarySupplier(this, callable, callable2));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(Publisher publisher) {
        return buffer(publisher, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(Publisher publisher, int i) {
        ObjectHelper.verifyPositive(i, "initialCapacity");
        return buffer(publisher, Functions.createArrayList(i));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable buffer(Publisher publisher, Callable callable) {
        ObjectHelper.requireNonNull(publisher, "boundaryIndicator is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableBufferExactBoundary(this, publisher, callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable cache() {
        return cacheWithInitialCapacity(16);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable cacheWithInitialCapacity(int i) {
        ObjectHelper.verifyPositive(i, "initialCapacity");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableCache(this, i));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable cast(Class cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single collect(Callable callable, BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(callable, "initialItemSupplier is null");
        ObjectHelper.requireNonNull(biConsumer, "collector is null");
        return RxJavaPlugins.onAssembly((Single) new FlowableCollectSingle(this, callable, biConsumer));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single collectInto(Object obj, BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(obj, "initialItem is null");
        return collect(Functions.justCallable(obj), biConsumer);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable compose(FlowableTransformer flowableTransformer) {
        ObjectHelper.requireNonNull(flowableTransformer, "composer is null");
        return fromPublisher(flowableTransformer.apply(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMap(Function function) {
        return concatMap(function, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMap(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Flowable) new FlowableConcatMap(this, function, i, ErrorMode.IMMEDIATE));
        }
        Object call = ((ScalarCallable) this).call();
        return call == null ? empty() : FlowableScalarXMap.scalarXMap(call, function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapDelayError(Function function) {
        return concatMapDelayError(function, 2, true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapDelayError(Function function, int i, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            return call == null ? empty() : FlowableScalarXMap.scalarXMap(call, function);
        }
        return RxJavaPlugins.onAssembly((Flowable) new FlowableConcatMap(this, function, i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapEager(Function function) {
        return concatMapEager(function, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapEager(Function function, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(this, function, i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapEagerDelayError(Function function, int i, int i2, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(this, function, i, i2, z ? ErrorMode.END : ErrorMode.BOUNDARY);
        return RxJavaPlugins.onAssembly((Flowable) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapEagerDelayError(Function function, boolean z) {
        return concatMapEagerDelayError(function, bufferSize(), bufferSize(), z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapIterable(Function function) {
        return concatMapIterable(function, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatMapIterable(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFlattenIterable(this, function, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable concatWith(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return concat((Publisher) this, publisher);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single contains(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return any(Functions.equalsWith(obj));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single count() {
        return RxJavaPlugins.onAssembly((Single) new FlowableCountSingle(this));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable debounce(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable debounce(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableDebounceTimed flowableDebounceTimed = new FlowableDebounceTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable) flowableDebounceTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable debounce(Function function) {
        ObjectHelper.requireNonNull(function, "debounceIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDebounce(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable defaultIfEmpty(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return switchIfEmpty(just(obj));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableDelay flowableDelay = new FlowableDelay(this, Math.max(0, j), timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableDelay);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable delay(long j, TimeUnit timeUnit, boolean z) {
        return delay(j, timeUnit, Schedulers.computation(), z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable delay(Function function) {
        ObjectHelper.requireNonNull(function, "itemDelayIndicator is null");
        return flatMap(FlowableInternalHelper.itemDelay(function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable delay(Publisher publisher, Function function) {
        return delaySubscription(publisher).delay(function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription(timer(j, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable delaySubscription(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDelaySubscriptionOther(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable dematerialize() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDematerialize(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable distinct(Function function) {
        return distinct(function, Functions.createHashSet());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable distinct(Function function, Callable callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDistinct(this, function, callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable distinctUntilChanged(BiPredicate biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "comparer is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDistinctUntilChanged(this, Functions.identity(), biPredicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable distinctUntilChanged(Function function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDistinctUntilChanged(this, function, ObjectHelper.equalsPredicate()));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doAfterNext(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onAfterNext is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDoAfterNext(this, consumer));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doAfterTerminate(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDoFinally(this, action));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnCancel(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, action);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnComplete(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnEach(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "consumer is null");
        return doOnEach(Functions.notificationOnNext(consumer), Functions.notificationOnError(consumer), Functions.notificationOnComplete(consumer), Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnEach(Subscriber subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");
        return doOnEach(FlowableInternalHelper.subscriberOnNext(subscriber), FlowableInternalHelper.subscriberOnError(subscriber), FlowableInternalHelper.subscriberOnComplete(subscriber), Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnError(Consumer consumer) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        return doOnEach(emptyConsumer, consumer, action, action);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnLifecycle(Consumer consumer, LongConsumer longConsumer, Action action) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        ObjectHelper.requireNonNull(longConsumer, "onRequest is null");
        ObjectHelper.requireNonNull(action, "onCancel is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDoOnLifecycle(this, consumer, longConsumer, action));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnNext(Consumer consumer) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        return doOnEach(consumer, emptyConsumer, action, action);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnRequest(LongConsumer longConsumer) {
        return doOnLifecycle(Functions.emptyConsumer(), longConsumer, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnSubscribe(Consumer consumer) {
        return doOnLifecycle(consumer, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable doOnTerminate(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(action), action, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe elementAt(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Maybe) new FlowableElementAtMaybe(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single elementAt(long j, Object obj) {
        if (j >= 0) {
            ObjectHelper.requireNonNull(obj, "defaultItem is null");
            return RxJavaPlugins.onAssembly((Single) new FlowableElementAtSingle(this, j, obj));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single elementAtOrError(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Single) new FlowableElementAtSingle(this, j, null));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable filter(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFilter(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single first(Object obj) {
        return elementAt(0, obj);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe firstElement() {
        return elementAt(0);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single firstOrError() {
        return elementAtOrError(0);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function) {
        return flatMap(function, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, int i) {
        return flatMap(function, false, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, BiFunction biFunction) {
        return flatMap(function, biFunction, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, BiFunction biFunction, int i) {
        return flatMap(function, biFunction, false, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, BiFunction biFunction, boolean z) {
        return flatMap(function, biFunction, z, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, BiFunction biFunction, boolean z, int i) {
        return flatMap(function, biFunction, z, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, BiFunction biFunction, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "bufferSize");
        return flatMap(FlowableInternalHelper.flatMapWithCombiner(function, biFunction), z, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, Function function2, Callable callable) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((Publisher) new FlowableMapNotification(this, function, function2, callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, Function function2, Callable callable, int i) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((Publisher) new FlowableMapNotification(this, function, function2, callable), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, boolean z) {
        return flatMap(function, z, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, boolean z, int i) {
        return flatMap(function, z, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMap(Function function, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "bufferSize");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            return call == null ? empty() : FlowableScalarXMap.scalarXMap(call, function);
        }
        FlowableFlatMap flowableFlatMap = new FlowableFlatMap(this, function, z, i, i2);
        return RxJavaPlugins.onAssembly((Flowable) flowableFlatMap);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function function) {
        return flatMapCompletable(function, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Completable) new FlowableFlatMapCompletableCompletable(this, function, z, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapIterable(Function function) {
        return flatMapIterable(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapIterable(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFlattenIterable(this, function, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapIterable(Function function, BiFunction biFunction) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(function), biFunction, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapIterable(Function function, BiFunction biFunction, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(function), biFunction, false, bufferSize(), i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapMaybe(Function function) {
        return flatMapMaybe(function, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapMaybe(Function function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFlatMapMaybe(this, function, z, i));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapSingle(Function function) {
        return flatMapSingle(function, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable flatMapSingle(Function function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableFlatMapSingle(this, function, z, i));
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEach(Consumer consumer) {
        return subscribe(consumer);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate predicate) {
        return forEachWhile(predicate, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate predicate, Consumer consumer) {
        return forEachWhile(predicate, consumer, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate predicate, Consumer consumer, Action action) {
        ObjectHelper.requireNonNull(predicate, "onNext is null");
        ObjectHelper.requireNonNull(consumer, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ForEachWhileSubscriber forEachWhileSubscriber = new ForEachWhileSubscriber(predicate, consumer, action);
        subscribe((FlowableSubscriber) forEachWhileSubscriber);
        return forEachWhileSubscriber;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable groupBy(Function function) {
        return groupBy(function, Functions.identity(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable groupBy(Function function, Function function2) {
        return groupBy(function, function2, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable groupBy(Function function, Function function2, boolean z) {
        return groupBy(function, function2, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable groupBy(Function function, Function function2, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableGroupBy flowableGroupBy = new FlowableGroupBy(this, function, function2, i, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableGroupBy);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable groupBy(Function function, boolean z) {
        return groupBy(function, Functions.identity(), z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable groupJoin(Publisher publisher, Function function, Function function2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        ObjectHelper.requireNonNull(function, "leftEnd is null");
        ObjectHelper.requireNonNull(function2, "rightEnd is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        FlowableGroupJoin flowableGroupJoin = new FlowableGroupJoin(this, publisher, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Flowable) flowableGroupJoin);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable hide() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableHide(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly((Completable) new FlowableIgnoreElementsCompletable(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable join(Publisher publisher, Function function, Function function2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        ObjectHelper.requireNonNull(function, "leftEnd is null");
        ObjectHelper.requireNonNull(function2, "rightEnd is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        FlowableJoin flowableJoin = new FlowableJoin(this, publisher, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Flowable) flowableJoin);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single last(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultItem");
        return RxJavaPlugins.onAssembly((Single) new FlowableLastSingle(this, obj));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe lastElement() {
        return RxJavaPlugins.onAssembly((Maybe) new FlowableLastMaybe(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single lastOrError() {
        return RxJavaPlugins.onAssembly((Single) new FlowableLastSingle(this, null));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable lift(FlowableOperator flowableOperator) {
        ObjectHelper.requireNonNull(flowableOperator, "lifter is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableLift(this, flowableOperator));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable limit(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Flowable) new FlowableLimit(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable map(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableMap(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable materialize() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableMaterialize(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable mergeWith(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return merge((Publisher) this, publisher);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable observeOn(Scheduler scheduler, boolean z) {
        return observeOn(scheduler, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable observeOn(Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableObserveOn(this, scheduler, z, i));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable ofType(Class cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return filter(Functions.isInstanceOf(cls)).cast(cls);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer() {
        return onBackpressureBuffer(bufferSize(), false, true);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer(int i) {
        return onBackpressureBuffer(i, false, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer(int i, Action action) {
        return onBackpressureBuffer(i, false, false, action);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer(int i, boolean z) {
        return onBackpressureBuffer(i, z, false);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer(int i, boolean z, boolean z2) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableOnBackpressureBuffer flowableOnBackpressureBuffer = new FlowableOnBackpressureBuffer(this, i, z2, z, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Flowable) flowableOnBackpressureBuffer);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer(int i, boolean z, boolean z2, Action action) {
        ObjectHelper.requireNonNull(action, "onOverflow is null");
        ObjectHelper.verifyPositive(i, "capacity");
        FlowableOnBackpressureBuffer flowableOnBackpressureBuffer = new FlowableOnBackpressureBuffer(this, i, z2, z, action);
        return RxJavaPlugins.onAssembly((Flowable) flowableOnBackpressureBuffer);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer(long j, Action action, BackpressureOverflowStrategy backpressureOverflowStrategy) {
        ObjectHelper.requireNonNull(backpressureOverflowStrategy, "strategy is null");
        ObjectHelper.verifyPositive(j, "capacity");
        FlowableOnBackpressureBufferStrategy flowableOnBackpressureBufferStrategy = new FlowableOnBackpressureBufferStrategy(this, j, action, backpressureOverflowStrategy);
        return RxJavaPlugins.onAssembly((Flowable) flowableOnBackpressureBufferStrategy);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureBuffer(boolean z) {
        return onBackpressureBuffer(bufferSize(), z, true);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureDrop() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableOnBackpressureDrop(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureDrop(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onDrop is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableOnBackpressureDrop(this, consumer));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onBackpressureLatest() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableOnBackpressureLatest(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onErrorResumeNext(Function function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableOnErrorNext(this, function, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onErrorResumeNext(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "next is null");
        return onErrorResumeNext(Functions.justFunction(publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onErrorReturn(Function function) {
        ObjectHelper.requireNonNull(function, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableOnErrorReturn(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onErrorReturnItem(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return onErrorReturn(Functions.justFunction(obj));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onExceptionResumeNext(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "next is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableOnErrorNext(this, Functions.justFunction(publisher), true));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableDetach(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @Beta
    @SchedulerSupport("none")
    public final ParallelFlowable parallel() {
        return ParallelFlowable.from(this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @Beta
    @SchedulerSupport("none")
    public final ParallelFlowable parallel(int i) {
        ObjectHelper.verifyPositive(i, "parallelism");
        return ParallelFlowable.from(this, i);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @Beta
    @SchedulerSupport("none")
    public final ParallelFlowable parallel(int i, int i2) {
        ObjectHelper.verifyPositive(i, "parallelism");
        ObjectHelper.verifyPositive(i2, "prefetch");
        return ParallelFlowable.from(this, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable publish(Function function) {
        return publish(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable publish(Function function, int i) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable) new FlowablePublishMulticast(this, function, i, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final ConnectableFlowable publish() {
        return publish(bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final ConnectableFlowable publish(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return FlowablePublish.create(this, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable rebatchRequests(int i) {
        return observeOn(ImmediateThinScheduler.INSTANCE, true, i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe reduce(BiFunction biFunction) {
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly((Maybe) new FlowableReduceMaybe(this, biFunction));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single reduce(Object obj, BiFunction biFunction) {
        ObjectHelper.requireNonNull(obj, "seed is null");
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly((Single) new FlowableReduceSeedSingle(this, obj, biFunction));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single reduceWith(Callable callable, BiFunction biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly((Single) new FlowableReduceWithSingle(this, callable, biFunction));
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
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? empty() : RxJavaPlugins.onAssembly((Flowable) new FlowableRepeat(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("times >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable repeatUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableRepeatUntil(this, booleanSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable repeatWhen(Function function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableRepeatWhen(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable replay(Function function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable replay(Function function, int i) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, i), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable replay(Function function, int i, long j, TimeUnit timeUnit) {
        return replay(function, i, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable replay(Function function, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, i, j, timeUnit, scheduler), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable replay(Function function, int i, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, i), FlowableInternalHelper.replayFunction(function, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable replay(Function function, long j, TimeUnit timeUnit) {
        return replay(function, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable replay(Function function, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, j, timeUnit, scheduler), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable replay(Function function, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), FlowableInternalHelper.replayFunction(function, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final ConnectableFlowable replay() {
        return FlowableReplay.createFrom(this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final ConnectableFlowable replay(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return FlowableReplay.create(this, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable replay(int i, long j, TimeUnit timeUnit) {
        return replay(i, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableFlowable replay(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        String str = "bufferSize";
        ObjectHelper.verifyPositive(i, str);
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, str);
        return FlowableReplay.create(this, j, timeUnit, scheduler, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableFlowable replay(int i, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(i), scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable replay(long j, TimeUnit timeUnit) {
        return replay(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableFlowable replay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.create(this, j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableFlowable replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(), scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable retry() {
        return retry(Long.MAX_VALUE, Functions.alwaysTrue());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable retry(long j) {
        return retry(j, Functions.alwaysTrue());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable retry(long j, Predicate predicate) {
        if (j >= 0) {
            ObjectHelper.requireNonNull(predicate, "predicate is null");
            return RxJavaPlugins.onAssembly((Flowable) new FlowableRetryPredicate(this, j, predicate));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("times >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable retry(BiPredicate biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableRetryBiPredicate(this, biPredicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable retry(Predicate predicate) {
        return retry(Long.MAX_VALUE, predicate);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable retryUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return retry(Long.MAX_VALUE, Functions.predicateReverseFor(booleanSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable retryWhen(Function function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableRetryWhen(this, function));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final void safeSubscribe(Subscriber subscriber) {
        ObjectHelper.requireNonNull(subscriber, "s is null");
        if (subscriber instanceof SafeSubscriber) {
            subscribe((FlowableSubscriber) (SafeSubscriber) subscriber);
        } else {
            subscribe((FlowableSubscriber) new SafeSubscriber(subscriber));
        }
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable sample(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable sample(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableSampleTimed flowableSampleTimed = new FlowableSampleTimed(this, j, timeUnit, scheduler, false);
        return RxJavaPlugins.onAssembly((Flowable) flowableSampleTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable sample(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableSampleTimed flowableSampleTimed = new FlowableSampleTimed(this, j, timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableSampleTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable sample(long j, TimeUnit timeUnit, boolean z) {
        return sample(j, timeUnit, Schedulers.computation(), z);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable sample(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "sampler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSamplePublisher(this, publisher, false));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable sample(Publisher publisher, boolean z) {
        ObjectHelper.requireNonNull(publisher, "sampler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSamplePublisher(this, publisher, z));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable scan(BiFunction biFunction) {
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableScan(this, biFunction));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable scan(Object obj, BiFunction biFunction) {
        ObjectHelper.requireNonNull(obj, "seed is null");
        return scanWith(Functions.justCallable(obj), biFunction);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable scanWith(Callable callable, BiFunction biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableScanSeed(this, callable, biFunction));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable serialize() {
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSerialized(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable share() {
        return publish().refCount();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single single(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single) new FlowableSingleSingle(this, obj));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe singleElement() {
        return RxJavaPlugins.onAssembly((Maybe) new FlowableSingleMaybe(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single singleOrError() {
        return RxJavaPlugins.onAssembly((Single) new FlowableSingleSingle(this, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable skip(long j) {
        return j <= 0 ? RxJavaPlugins.onAssembly(this) : RxJavaPlugins.onAssembly((Flowable) new FlowableSkip(this, j));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable skip(long j, TimeUnit timeUnit) {
        return skipUntil(timer(j, timeUnit));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable skip(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipUntil(timer(j, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable skipLast(int i) {
        if (i >= 0) {
            return i == 0 ? RxJavaPlugins.onAssembly(this) : RxJavaPlugins.onAssembly((Flowable) new FlowableSkipLast(this, i));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(i);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable skipLast(long j, TimeUnit timeUnit) {
        return skipLast(j, timeUnit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable skipLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return skipLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableSkipLastTimed flowableSkipLastTimed = new FlowableSkipLastTimed(this, j, timeUnit, scheduler, i << 1, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableSkipLastTimed);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable skipLast(long j, TimeUnit timeUnit, boolean z) {
        return skipLast(j, timeUnit, Schedulers.computation(), z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable skipUntil(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSkipUntil(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable skipWhile(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSkipWhile(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable sorted() {
        return toList().toFlowable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable sorted(Comparator comparator) {
        ObjectHelper.requireNonNull(comparator, "sortFunction");
        return toList().toFlowable().map(Functions.listSorter(comparator)).flatMapIterable(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable startWith(Iterable iterable) {
        return concatArray(fromIterable(iterable), this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable startWith(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return concatArray(just(obj), this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable startWith(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return concatArray(publisher, this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable startWithArray(Object... objArr) {
        Flowable fromArray = fromArray(objArr);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer) {
        return subscribe(consumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2) {
        return subscribe(consumer, consumer2, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2, Action action) {
        return subscribe(consumer, consumer2, action, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2, Action action, Consumer consumer3) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(consumer3, "onSubscribe is null");
        LambdaSubscriber lambdaSubscriber = new LambdaSubscriber(consumer, consumer2, action, consumer3);
        subscribe((FlowableSubscriber) lambdaSubscriber);
        return lambdaSubscriber;
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @Beta
    @SchedulerSupport("none")
    public final void subscribe(FlowableSubscriber flowableSubscriber) {
        ObjectHelper.requireNonNull(flowableSubscriber, "s is null");
        try {
            Subscriber onSubscribe = RxJavaPlugins.onSubscribe(this, (Subscriber) flowableSubscriber);
            ObjectHelper.requireNonNull(onSubscribe, "Plugin returned null Subscriber");
            subscribeActual(onSubscribe);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
            NullPointerException nullPointerException = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            nullPointerException.initCause(th);
            throw nullPointerException;
        }
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void subscribe(Subscriber subscriber) {
        if (subscriber instanceof FlowableSubscriber) {
            subscribe((FlowableSubscriber) subscriber);
            return;
        }
        ObjectHelper.requireNonNull(subscriber, "s is null");
        subscribe((FlowableSubscriber) new StrictSubscriber(subscriber));
    }

    public abstract void subscribeActual(Subscriber subscriber);

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable subscribeOn(@NonNull Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return subscribeOn(scheduler, !(this instanceof FlowableCreate));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable subscribeOn(@NonNull Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSubscribeOn(this, scheduler, z));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Subscriber subscribeWith(Subscriber subscriber) {
        subscribe(subscriber);
        return subscriber;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable switchIfEmpty(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableSwitchIfEmpty(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable switchMap(Function function) {
        return switchMap(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable switchMap(Function function, int i) {
        return switchMap0(function, i, false);
    }

    /* access modifiers changed from: 0000 */
    public Flowable switchMap0(Function function, int i, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Flowable) new FlowableSwitchMap(this, function, i, z));
        }
        Object call = ((ScalarCallable) this).call();
        return call == null ? empty() : FlowableScalarXMap.scalarXMap(call, function);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable switchMapDelayError(Function function) {
        return switchMapDelayError(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable switchMapDelayError(Function function, int i) {
        return switchMap0(function, i, true);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable take(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Flowable) new FlowableTake(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable take(long j, TimeUnit timeUnit) {
        return takeUntil((Publisher) timer(j, timeUnit));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable take(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeUntil((Publisher) timer(j, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable takeLast(int i) {
        if (i >= 0) {
            return i == 0 ? RxJavaPlugins.onAssembly((Flowable) new FlowableIgnoreElements(this)) : i == 1 ? RxJavaPlugins.onAssembly((Flowable) new FlowableTakeLastOne(this)) : RxJavaPlugins.onAssembly((Flowable) new FlowableTakeLast(this, i));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(i);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable takeLast(long j, long j2, TimeUnit timeUnit) {
        return takeLast(j, j2, timeUnit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, j2, timeUnit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        long j3 = j;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (j3 >= 0) {
            FlowableTakeLastTimed flowableTakeLastTimed = new FlowableTakeLastTimed(this, j, j2, timeUnit, scheduler, i, z);
            return RxJavaPlugins.onAssembly((Flowable) flowableTakeLastTimed);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable takeLast(long j, TimeUnit timeUnit) {
        return takeLast(j, timeUnit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable takeLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return takeLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        return takeLast(Long.MAX_VALUE, j, timeUnit, scheduler, z, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable takeLast(long j, TimeUnit timeUnit, boolean z) {
        return takeLast(j, timeUnit, Schedulers.computation(), z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable takeUntil(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "stopPredicate is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableTakeUntilPredicate(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable takeUntil(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableTakeUntil(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable takeWhile(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableTakeWhile(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestSubscriber test() {
        TestSubscriber testSubscriber = new TestSubscriber();
        subscribe((FlowableSubscriber) testSubscriber);
        return testSubscriber;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestSubscriber test(long j) {
        TestSubscriber testSubscriber = new TestSubscriber(j);
        subscribe((FlowableSubscriber) testSubscriber);
        return testSubscriber;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestSubscriber test(long j, boolean z) {
        TestSubscriber testSubscriber = new TestSubscriber(j);
        if (z) {
            testSubscriber.cancel();
        }
        subscribe((FlowableSubscriber) testSubscriber);
        return testSubscriber;
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable throttleFirst(long j, TimeUnit timeUnit) {
        return throttleFirst(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable throttleFirst(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableThrottleFirstTimed flowableThrottleFirstTimed = new FlowableThrottleFirstTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable) flowableThrottleFirstTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable throttleLast(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable throttleLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return sample(j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable throttleWithTimeout(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable throttleWithTimeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return debounce(j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeInterval(TimeUnit timeUnit) {
        return timeInterval(timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeInterval(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableTimeInterval(this, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, null, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, null, scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable timeout(long j, TimeUnit timeUnit, Scheduler scheduler, Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return timeout0(j, timeUnit, publisher, scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable timeout(long j, TimeUnit timeUnit, Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return timeout0(j, timeUnit, publisher, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeout(Function function) {
        return timeout0(null, function, null);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeout(Function function, Flowable flowable) {
        ObjectHelper.requireNonNull(flowable, "other is null");
        return timeout0(null, function, flowable);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeout(Publisher publisher, Function function) {
        ObjectHelper.requireNonNull(publisher, "firstTimeoutIndicator is null");
        return timeout0(publisher, function, null);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timeout(Publisher publisher, Function function, Publisher publisher2) {
        ObjectHelper.requireNonNull(publisher, "firstTimeoutSelector is null");
        ObjectHelper.requireNonNull(publisher2, "other is null");
        return timeout0(publisher, function, publisher2);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timestamp(TimeUnit timeUnit) {
        return timestamp(timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable timestamp(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object to(Function function) {
        try {
            ObjectHelper.requireNonNull(function, "converter is null");
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Future toFuture() {
        return (Future) subscribeWith(new FutureSubscriber());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toList() {
        return RxJavaPlugins.onAssembly((Single) new FlowableToListSingle(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toList(int i) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Single) new FlowableToListSingle(this, Functions.createArrayList(i)));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toList(Callable callable) {
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Single) new FlowableToListSingle(this, callable));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMap(Function function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(function));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMap(Function function, Function function2) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(function, function2));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMap(Function function, Function function2, Callable callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        return collect(callable, Functions.toMapKeyValueSelector(function, function2));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function) {
        return toMultimap(function, Functions.identity(), HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function, Function function2) {
        return toMultimap(function, function2, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function, Function function2, Callable callable) {
        return toMultimap(function, function2, callable, ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function, Function function2, Callable callable, Function function3) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.requireNonNull(callable, "mapSupplier is null");
        ObjectHelper.requireNonNull(function3, "collectionFactory is null");
        return collect(callable, Functions.toMultimapKeyValueSelector(function, function2, function3));
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable toObservable() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableFromPublisher(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList() {
        return toSortedList(Functions.naturalComparator());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList(int i) {
        return toSortedList(Functions.naturalComparator(), i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList(Comparator comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList(Comparator comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(i).map(Functions.listSorter(comparator));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableUnsubscribeOn(this, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(long j) {
        return window(j, j, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(long j, long j2) {
        return window(j, j2, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(long j, long j2, int i) {
        ObjectHelper.verifyPositive(j2, "skip");
        ObjectHelper.verifyPositive(j, "count");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableWindow flowableWindow = new FlowableWindow(this, j, j2, i);
        return RxJavaPlugins.onAssembly((Flowable) flowableWindow);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable window(long j, long j2, TimeUnit timeUnit) {
        return window(j, j2, timeUnit, Schedulers.computation(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, j2, timeUnit, scheduler, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, int i) {
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        long j3 = j;
        ObjectHelper.verifyPositive(j, "timespan");
        long j4 = j2;
        ObjectHelper.verifyPositive(j4, "timeskip");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        FlowableWindowTimed flowableWindowTimed = new FlowableWindowTimed(this, j3, j4, timeUnit2, scheduler2, Long.MAX_VALUE, i2, false);
        return RxJavaPlugins.onAssembly((Flowable) flowableWindowTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable window(long j, TimeUnit timeUnit) {
        return window(j, timeUnit, Schedulers.computation(), Long.MAX_VALUE, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable window(long j, TimeUnit timeUnit, long j2) {
        return window(j, timeUnit, Schedulers.computation(), j2, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable window(long j, TimeUnit timeUnit, long j2, boolean z) {
        return window(j, timeUnit, Schedulers.computation(), j2, z);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable window(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, timeUnit, scheduler, Long.MAX_VALUE, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2) {
        return window(j, timeUnit, scheduler, j2, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z) {
        return window(j, timeUnit, scheduler, j2, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Flowable window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z, int i) {
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        long j3 = j2;
        ObjectHelper.verifyPositive(j3, "count");
        FlowableWindowTimed flowableWindowTimed = new FlowableWindowTimed(this, j, j, timeUnit2, scheduler2, j3, i2, z);
        return RxJavaPlugins.onAssembly((Flowable) flowableWindowTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(Callable callable) {
        return window(callable, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(Callable callable, int i) {
        ObjectHelper.requireNonNull(callable, "boundaryIndicatorSupplier is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableWindowBoundarySupplier(this, callable, i));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(Publisher publisher) {
        return window(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(Publisher publisher, int i) {
        ObjectHelper.requireNonNull(publisher, "boundaryIndicator is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableWindowBoundary(this, publisher, i));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(Publisher publisher, Function function) {
        return window(publisher, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable window(Publisher publisher, Function function, int i) {
        ObjectHelper.requireNonNull(publisher, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableWindowBoundarySelector(this, publisher, function, i));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable withLatestFrom(Iterable iterable, Function function) {
        ObjectHelper.requireNonNull(iterable, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableWithLatestFromMany(this, iterable, function));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable withLatestFrom(Publisher publisher, BiFunction biFunction) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableWithLatestFrom(this, biFunction, publisher));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable withLatestFrom(Publisher publisher, Publisher publisher2, Function3 function3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return withLatestFrom(new Publisher[]{publisher, publisher2}, Functions.toFunction(function3));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable withLatestFrom(Publisher publisher, Publisher publisher2, Publisher publisher3, Function4 function4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return withLatestFrom(new Publisher[]{publisher, publisher2, publisher3}, Functions.toFunction(function4));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable withLatestFrom(Publisher publisher, Publisher publisher2, Publisher publisher3, Publisher publisher4, Function5 function5) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return withLatestFrom(new Publisher[]{publisher, publisher2, publisher3, publisher4}, Functions.toFunction(function5));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable withLatestFrom(Publisher[] publisherArr, Function function) {
        ObjectHelper.requireNonNull(publisherArr, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableWithLatestFromMany(this, publisherArr, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable zipWith(Iterable iterable, BiFunction biFunction) {
        ObjectHelper.requireNonNull(iterable, "other is null");
        ObjectHelper.requireNonNull(biFunction, "zipper is null");
        return RxJavaPlugins.onAssembly((Flowable) new FlowableZipIterable(this, iterable, biFunction));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable zipWith(Publisher publisher, BiFunction biFunction) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return zip(this, publisher, biFunction);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable zipWith(Publisher publisher, BiFunction biFunction, boolean z) {
        return zip((Publisher) this, publisher, biFunction, z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable zipWith(Publisher publisher, BiFunction biFunction, boolean z, int i) {
        return zip((Publisher) this, publisher, biFunction, z, i);
    }
}
