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
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.observers.BlockingFirstObserver;
import io.reactivex.internal.observers.BlockingLastObserver;
import io.reactivex.internal.observers.ForEachWhileObserver;
import io.reactivex.internal.observers.FutureObserver;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.internal.operators.flowable.FlowableFromObservable;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureError;
import io.reactivex.internal.operators.observable.BlockingObservableIterable;
import io.reactivex.internal.operators.observable.BlockingObservableLatest;
import io.reactivex.internal.operators.observable.BlockingObservableMostRecent;
import io.reactivex.internal.operators.observable.BlockingObservableNext;
import io.reactivex.internal.operators.observable.ObservableAllSingle;
import io.reactivex.internal.operators.observable.ObservableAmb;
import io.reactivex.internal.operators.observable.ObservableAnySingle;
import io.reactivex.internal.operators.observable.ObservableBlockingSubscribe;
import io.reactivex.internal.operators.observable.ObservableBuffer;
import io.reactivex.internal.operators.observable.ObservableBufferBoundary;
import io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier;
import io.reactivex.internal.operators.observable.ObservableBufferExactBoundary;
import io.reactivex.internal.operators.observable.ObservableBufferTimed;
import io.reactivex.internal.operators.observable.ObservableCache;
import io.reactivex.internal.operators.observable.ObservableCollectSingle;
import io.reactivex.internal.operators.observable.ObservableCombineLatest;
import io.reactivex.internal.operators.observable.ObservableConcatMap;
import io.reactivex.internal.operators.observable.ObservableConcatMapCompletable;
import io.reactivex.internal.operators.observable.ObservableConcatMapEager;
import io.reactivex.internal.operators.observable.ObservableCountSingle;
import io.reactivex.internal.operators.observable.ObservableCreate;
import io.reactivex.internal.operators.observable.ObservableDebounce;
import io.reactivex.internal.operators.observable.ObservableDebounceTimed;
import io.reactivex.internal.operators.observable.ObservableDefer;
import io.reactivex.internal.operators.observable.ObservableDelay;
import io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther;
import io.reactivex.internal.operators.observable.ObservableDematerialize;
import io.reactivex.internal.operators.observable.ObservableDetach;
import io.reactivex.internal.operators.observable.ObservableDistinct;
import io.reactivex.internal.operators.observable.ObservableDistinctUntilChanged;
import io.reactivex.internal.operators.observable.ObservableDoAfterNext;
import io.reactivex.internal.operators.observable.ObservableDoFinally;
import io.reactivex.internal.operators.observable.ObservableDoOnEach;
import io.reactivex.internal.operators.observable.ObservableDoOnLifecycle;
import io.reactivex.internal.operators.observable.ObservableElementAtMaybe;
import io.reactivex.internal.operators.observable.ObservableElementAtSingle;
import io.reactivex.internal.operators.observable.ObservableEmpty;
import io.reactivex.internal.operators.observable.ObservableError;
import io.reactivex.internal.operators.observable.ObservableFilter;
import io.reactivex.internal.operators.observable.ObservableFlatMap;
import io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable;
import io.reactivex.internal.operators.observable.ObservableFlatMapMaybe;
import io.reactivex.internal.operators.observable.ObservableFlatMapSingle;
import io.reactivex.internal.operators.observable.ObservableFlattenIterable;
import io.reactivex.internal.operators.observable.ObservableFromArray;
import io.reactivex.internal.operators.observable.ObservableFromCallable;
import io.reactivex.internal.operators.observable.ObservableFromFuture;
import io.reactivex.internal.operators.observable.ObservableFromIterable;
import io.reactivex.internal.operators.observable.ObservableFromPublisher;
import io.reactivex.internal.operators.observable.ObservableFromUnsafeSource;
import io.reactivex.internal.operators.observable.ObservableGenerate;
import io.reactivex.internal.operators.observable.ObservableGroupBy;
import io.reactivex.internal.operators.observable.ObservableGroupJoin;
import io.reactivex.internal.operators.observable.ObservableHide;
import io.reactivex.internal.operators.observable.ObservableIgnoreElements;
import io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable;
import io.reactivex.internal.operators.observable.ObservableInternalHelper;
import io.reactivex.internal.operators.observable.ObservableInterval;
import io.reactivex.internal.operators.observable.ObservableIntervalRange;
import io.reactivex.internal.operators.observable.ObservableJoin;
import io.reactivex.internal.operators.observable.ObservableJust;
import io.reactivex.internal.operators.observable.ObservableLastMaybe;
import io.reactivex.internal.operators.observable.ObservableLastSingle;
import io.reactivex.internal.operators.observable.ObservableLift;
import io.reactivex.internal.operators.observable.ObservableMap;
import io.reactivex.internal.operators.observable.ObservableMapNotification;
import io.reactivex.internal.operators.observable.ObservableMaterialize;
import io.reactivex.internal.operators.observable.ObservableNever;
import io.reactivex.internal.operators.observable.ObservableObserveOn;
import io.reactivex.internal.operators.observable.ObservableOnErrorNext;
import io.reactivex.internal.operators.observable.ObservableOnErrorReturn;
import io.reactivex.internal.operators.observable.ObservablePublish;
import io.reactivex.internal.operators.observable.ObservablePublishSelector;
import io.reactivex.internal.operators.observable.ObservableRange;
import io.reactivex.internal.operators.observable.ObservableRangeLong;
import io.reactivex.internal.operators.observable.ObservableReduceMaybe;
import io.reactivex.internal.operators.observable.ObservableReduceSeedSingle;
import io.reactivex.internal.operators.observable.ObservableReduceWithSingle;
import io.reactivex.internal.operators.observable.ObservableRepeat;
import io.reactivex.internal.operators.observable.ObservableRepeatUntil;
import io.reactivex.internal.operators.observable.ObservableRepeatWhen;
import io.reactivex.internal.operators.observable.ObservableReplay;
import io.reactivex.internal.operators.observable.ObservableRetryBiPredicate;
import io.reactivex.internal.operators.observable.ObservableRetryPredicate;
import io.reactivex.internal.operators.observable.ObservableRetryWhen;
import io.reactivex.internal.operators.observable.ObservableSampleTimed;
import io.reactivex.internal.operators.observable.ObservableSampleWithObservable;
import io.reactivex.internal.operators.observable.ObservableScalarXMap;
import io.reactivex.internal.operators.observable.ObservableScan;
import io.reactivex.internal.operators.observable.ObservableScanSeed;
import io.reactivex.internal.operators.observable.ObservableSequenceEqualSingle;
import io.reactivex.internal.operators.observable.ObservableSerialized;
import io.reactivex.internal.operators.observable.ObservableSingleMaybe;
import io.reactivex.internal.operators.observable.ObservableSingleSingle;
import io.reactivex.internal.operators.observable.ObservableSkip;
import io.reactivex.internal.operators.observable.ObservableSkipLast;
import io.reactivex.internal.operators.observable.ObservableSkipLastTimed;
import io.reactivex.internal.operators.observable.ObservableSkipUntil;
import io.reactivex.internal.operators.observable.ObservableSkipWhile;
import io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import io.reactivex.internal.operators.observable.ObservableSwitchIfEmpty;
import io.reactivex.internal.operators.observable.ObservableSwitchMap;
import io.reactivex.internal.operators.observable.ObservableTake;
import io.reactivex.internal.operators.observable.ObservableTakeLast;
import io.reactivex.internal.operators.observable.ObservableTakeLastOne;
import io.reactivex.internal.operators.observable.ObservableTakeLastTimed;
import io.reactivex.internal.operators.observable.ObservableTakeUntil;
import io.reactivex.internal.operators.observable.ObservableTakeUntilPredicate;
import io.reactivex.internal.operators.observable.ObservableTakeWhile;
import io.reactivex.internal.operators.observable.ObservableThrottleFirstTimed;
import io.reactivex.internal.operators.observable.ObservableTimeInterval;
import io.reactivex.internal.operators.observable.ObservableTimeout;
import io.reactivex.internal.operators.observable.ObservableTimeoutTimed;
import io.reactivex.internal.operators.observable.ObservableTimer;
import io.reactivex.internal.operators.observable.ObservableToList;
import io.reactivex.internal.operators.observable.ObservableToListSingle;
import io.reactivex.internal.operators.observable.ObservableUnsubscribeOn;
import io.reactivex.internal.operators.observable.ObservableUsing;
import io.reactivex.internal.operators.observable.ObservableWindow;
import io.reactivex.internal.operators.observable.ObservableWindowBoundary;
import io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector;
import io.reactivex.internal.operators.observable.ObservableWindowBoundarySupplier;
import io.reactivex.internal.operators.observable.ObservableWindowTimed;
import io.reactivex.internal.operators.observable.ObservableWithLatestFrom;
import io.reactivex.internal.operators.observable.ObservableWithLatestFromMany;
import io.reactivex.internal.operators.observable.ObservableZip;
import io.reactivex.internal.operators.observable.ObservableZipIterable;
import io.reactivex.internal.util.ArrayListSupplier;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.HashMapSupplier;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.SafeObserver;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public abstract class Observable implements ObservableSource {

    /* renamed from: io.reactivex.Observable$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$reactivex$BackpressureStrategy = new int[BackpressureStrategy.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$io$reactivex$BackpressureStrategy[BackpressureStrategy.DROP.ordinal()] = 1;
            $SwitchMap$io$reactivex$BackpressureStrategy[BackpressureStrategy.LATEST.ordinal()] = 2;
            $SwitchMap$io$reactivex$BackpressureStrategy[BackpressureStrategy.MISSING.ordinal()] = 3;
            try {
                $SwitchMap$io$reactivex$BackpressureStrategy[BackpressureStrategy.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable amb(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableAmb(null, iterable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable ambArray(ObservableSource... observableSourceArr) {
        ObjectHelper.requireNonNull(observableSourceArr, "sources is null");
        int length = observableSourceArr.length;
        return length == 0 ? empty() : length == 1 ? wrap(observableSourceArr[0]) : RxJavaPlugins.onAssembly((Observable) new ObservableAmb(observableSourceArr, null));
    }

    public static int bufferSize() {
        return Flowable.bufferSize();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, ObservableSource observableSource7, ObservableSource observableSource8, ObservableSource observableSource9, Function9 function9) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        ObjectHelper.requireNonNull(observableSource9, "source9 is null");
        return combineLatest(Functions.toFunction(function9), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8, observableSource9);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, ObservableSource observableSource7, ObservableSource observableSource8, Function8 function8) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        return combineLatest(Functions.toFunction(function8), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, ObservableSource observableSource7, Function7 function7) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        return combineLatest(Functions.toFunction(function7), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, Function6 function6) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        return combineLatest(Functions.toFunction(function6), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, Function5 function5) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        return combineLatest(Functions.toFunction(function5), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, Function4 function4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return combineLatest(Functions.toFunction(function4), bufferSize(), observableSource, observableSource2, observableSource3, observableSource4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, Function3 function3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return combineLatest(Functions.toFunction(function3), bufferSize(), observableSource, observableSource2, observableSource3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource observableSource, ObservableSource observableSource2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return combineLatest(Functions.toFunction(biFunction), bufferSize(), observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(Function function, int i, ObservableSource... observableSourceArr) {
        return combineLatest(observableSourceArr, function, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(Iterable iterable, Function function) {
        return combineLatest(iterable, function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(Iterable iterable, Function function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(null, iterable, function, i << 1, false);
        return RxJavaPlugins.onAssembly((Observable) observableCombineLatest);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource[] observableSourceArr, Function function) {
        return combineLatest(observableSourceArr, function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatest(ObservableSource[] observableSourceArr, Function function, int i) {
        ObjectHelper.requireNonNull(observableSourceArr, "sources is null");
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(observableSourceArr, null, function, i << 1, false);
        return RxJavaPlugins.onAssembly((Observable) observableCombineLatest);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatestDelayError(Function function, int i, ObservableSource... observableSourceArr) {
        return combineLatestDelayError(observableSourceArr, function, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatestDelayError(Iterable iterable, Function function) {
        return combineLatestDelayError(iterable, function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatestDelayError(Iterable iterable, Function function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(null, iterable, function, i << 1, true);
        return RxJavaPlugins.onAssembly((Observable) observableCombineLatest);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatestDelayError(ObservableSource[] observableSourceArr, Function function) {
        return combineLatestDelayError(observableSourceArr, function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable combineLatestDelayError(ObservableSource[] observableSourceArr, Function function, int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(function, "combiner is null");
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(observableSourceArr, null, function, i << 1, true);
        return RxJavaPlugins.onAssembly((Observable) observableCombineLatest);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concat(ObservableSource observableSource) {
        return concat(observableSource, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concat(ObservableSource observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Observable) new ObservableConcatMap(observableSource, Functions.identity(), i, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concat(ObservableSource observableSource, ObservableSource observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return concatArray(observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concat(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return concatArray(observableSource, observableSource2, observableSource3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concat(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return concatArray(observableSource, observableSource2, observableSource3, observableSource4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concat(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return fromIterable(iterable).concatMapDelayError(Functions.identity(), bufferSize(), false);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatArray(ObservableSource... observableSourceArr) {
        return observableSourceArr.length == 0 ? empty() : observableSourceArr.length == 1 ? wrap(observableSourceArr[0]) : RxJavaPlugins.onAssembly((Observable) new ObservableConcatMap(fromArray(observableSourceArr), Functions.identity(), bufferSize(), ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatArrayDelayError(ObservableSource... observableSourceArr) {
        return observableSourceArr.length == 0 ? empty() : observableSourceArr.length == 1 ? wrap(observableSourceArr[0]) : concatDelayError((ObservableSource) fromArray(observableSourceArr));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatArrayEager(int i, int i2, ObservableSource... observableSourceArr) {
        return fromArray(observableSourceArr).concatMapEagerDelayError(Functions.identity(), i, i2, false);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatArrayEager(ObservableSource... observableSourceArr) {
        return concatArrayEager(bufferSize(), bufferSize(), observableSourceArr);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatDelayError(ObservableSource observableSource) {
        return concatDelayError(observableSource, bufferSize(), true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatDelayError(ObservableSource observableSource, int i, boolean z) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableConcatMap(observableSource, Functions.identity(), i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatDelayError(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return concatDelayError((ObservableSource) fromIterable(iterable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatEager(ObservableSource observableSource) {
        return concatEager(observableSource, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatEager(ObservableSource observableSource, int i, int i2) {
        ObjectHelper.requireNonNull(Integer.valueOf(i), "maxConcurrency is null");
        ObjectHelper.requireNonNull(Integer.valueOf(i2), "prefetch is null");
        return wrap(observableSource).concatMapEager(Functions.identity(), i, i2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatEager(Iterable iterable) {
        return concatEager(iterable, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable concatEager(Iterable iterable, int i, int i2) {
        ObjectHelper.requireNonNull(Integer.valueOf(i), "maxConcurrency is null");
        ObjectHelper.requireNonNull(Integer.valueOf(i2), "prefetch is null");
        return fromIterable(iterable).concatMapEagerDelayError(Functions.identity(), i, i2, false);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable create(ObservableOnSubscribe observableOnSubscribe) {
        ObjectHelper.requireNonNull(observableOnSubscribe, "source is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableCreate(observableOnSubscribe));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable defer(Callable callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDefer(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    private Observable doOnEach(Consumer consumer, Consumer consumer2, Action action, Action action2) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(action2, "onAfterTerminate is null");
        ObservableDoOnEach observableDoOnEach = new ObservableDoOnEach(this, consumer, consumer2, action, action2);
        return RxJavaPlugins.onAssembly((Observable) observableDoOnEach);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable empty() {
        return RxJavaPlugins.onAssembly(ObservableEmpty.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable error(Throwable th) {
        ObjectHelper.requireNonNull(th, "e is null");
        return error(Functions.justCallable(th));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable error(Callable callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableError(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable fromArray(Object... objArr) {
        ObjectHelper.requireNonNull(objArr, "items is null");
        return objArr.length == 0 ? empty() : objArr.length == 1 ? just(objArr[0]) : RxJavaPlugins.onAssembly((Observable) new ObservableFromArray(objArr));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable fromCallable(Callable callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFromCallable(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable fromFuture(Future future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFromFuture(future, 0, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable fromFuture(Future future, long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFromFuture(future, j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Observable fromFuture(Future future, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, j, timeUnit).subscribeOn(scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Observable fromFuture(Future future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable fromIterable(Iterable iterable) {
        ObjectHelper.requireNonNull(iterable, "source is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFromIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable fromPublisher(Publisher publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFromPublisher(publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable generate(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "generator  is null");
        return generate(Functions.nullSupplier(), ObservableInternalHelper.simpleGenerator(consumer), Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable generate(Callable callable, BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator  is null");
        return generate(callable, ObservableInternalHelper.simpleBiGenerator(biConsumer), Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable generate(Callable callable, BiConsumer biConsumer, Consumer consumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator  is null");
        return generate(callable, ObservableInternalHelper.simpleBiGenerator(biConsumer), consumer);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable generate(Callable callable, BiFunction biFunction) {
        return generate(callable, biFunction, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable generate(Callable callable, BiFunction biFunction, Consumer consumer) {
        ObjectHelper.requireNonNull(callable, "initialState is null");
        ObjectHelper.requireNonNull(biFunction, "generator  is null");
        ObjectHelper.requireNonNull(consumer, "disposeState is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableGenerate(callable, biFunction, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Observable interval(long j, long j2, TimeUnit timeUnit) {
        return interval(j, j2, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Observable interval(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableInterval observableInterval = new ObservableInterval(Math.max(0, j), Math.max(0, j2), timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable) observableInterval);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Observable interval(long j, TimeUnit timeUnit) {
        return interval(j, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Observable interval(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return interval(j, j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Observable intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit) {
        return intervalRange(j, j2, j3, j4, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Observable intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit, Scheduler scheduler) {
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
                ObservableIntervalRange observableIntervalRange = new ObservableIntervalRange(j, j7, Math.max(0, j6), Math.max(0, j4), timeUnit, scheduler);
                return RxJavaPlugins.onAssembly((Observable) observableIntervalRange);
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj) {
        ObjectHelper.requireNonNull(obj, "The item is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableJust(obj));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        return fromArray(obj, obj2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        return fromArray(obj, obj2, obj3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3, Object obj4) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        return fromArray(obj, obj2, obj3, obj4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        ObjectHelper.requireNonNull(obj7, "The seventh item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6, obj7);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
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

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9) {
        ObjectHelper.requireNonNull(obj, "The first item is null");
        ObjectHelper.requireNonNull(obj2, "The second item is null");
        ObjectHelper.requireNonNull(obj3, "The third item is null");
        ObjectHelper.requireNonNull(obj4, "The fourth item is null");
        ObjectHelper.requireNonNull(obj5, "The fifth item is null");
        ObjectHelper.requireNonNull(obj6, "The sixth item is null");
        ObjectHelper.requireNonNull(obj7, "The seventh item is null");
        ObjectHelper.requireNonNull(obj8, "The eighth item is null");
        ObjectHelper.requireNonNull(obj9, "The ninth item is null");
        return fromArray(obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable just(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10) {
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

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), false, Integer.MAX_VALUE, bufferSize());
        return RxJavaPlugins.onAssembly((Observable) observableFlatMap);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(ObservableSource observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), false, i, bufferSize());
        return RxJavaPlugins.onAssembly((Observable) observableFlatMap);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(ObservableSource observableSource, ObservableSource observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return fromArray(observableSource, observableSource2).flatMap(Functions.identity(), false, 2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return fromArray(observableSource, observableSource2, observableSource3).flatMap(Functions.identity(), false, 3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return fromArray(observableSource, observableSource2, observableSource3, observableSource4).flatMap(Functions.identity(), false, 4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(Iterable iterable) {
        return fromIterable(iterable).flatMap(Functions.identity());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(Iterable iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable merge(Iterable iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), false, i, i2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeArray(int i, int i2, ObservableSource... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), false, i, i2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeArray(ObservableSource... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), observableSourceArr.length);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeArrayDelayError(int i, int i2, ObservableSource... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), true, i, i2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeArrayDelayError(ObservableSource... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), true, observableSourceArr.length);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), true, Integer.MAX_VALUE, bufferSize());
        return RxJavaPlugins.onAssembly((Observable) observableFlatMap);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(ObservableSource observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), true, i, bufferSize());
        return RxJavaPlugins.onAssembly((Observable) observableFlatMap);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(ObservableSource observableSource, ObservableSource observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return fromArray(observableSource, observableSource2).flatMap(Functions.identity(), true, 2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return fromArray(observableSource, observableSource2, observableSource3).flatMap(Functions.identity(), true, 3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return fromArray(observableSource, observableSource2, observableSource3, observableSource4).flatMap(Functions.identity(), true, 4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(Iterable iterable) {
        return fromIterable(iterable).flatMap(Functions.identity(), true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(Iterable iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable mergeDelayError(Iterable iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i, i2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable never() {
        return RxJavaPlugins.onAssembly(ObservableNever.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable range(int i, int i2) {
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
                return RxJavaPlugins.onAssembly((Observable) new ObservableRange(i, i2));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable rangeLong(long j, long j2) {
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
                return RxJavaPlugins.onAssembly((Observable) new ObservableRangeLong(j, j2));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(ObservableSource observableSource, ObservableSource observableSource2) {
        return sequenceEqual(observableSource, observableSource2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(ObservableSource observableSource, ObservableSource observableSource2, int i) {
        return sequenceEqual(observableSource, observableSource2, ObjectHelper.equalsPredicate(), i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(ObservableSource observableSource, ObservableSource observableSource2, BiPredicate biPredicate) {
        return sequenceEqual(observableSource, observableSource2, biPredicate, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Single sequenceEqual(ObservableSource observableSource, ObservableSource observableSource2, BiPredicate biPredicate, int i) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(biPredicate, "isEqual is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Single) new ObservableSequenceEqualSingle(observableSource, observableSource2, biPredicate, i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable switchOnNext(ObservableSource observableSource) {
        return switchOnNext(observableSource, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable switchOnNext(ObservableSource observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSwitchMap(observableSource, Functions.identity(), i, false));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable switchOnNextDelayError(ObservableSource observableSource) {
        return switchOnNextDelayError(observableSource, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable switchOnNextDelayError(ObservableSource observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSwitchMap(observableSource, Functions.identity(), i, true));
    }

    private Observable timeout0(long j, TimeUnit timeUnit, ObservableSource observableSource, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableTimeoutTimed observableTimeoutTimed = new ObservableTimeoutTimed(this, j, timeUnit, scheduler, observableSource);
        return RxJavaPlugins.onAssembly((Observable) observableTimeoutTimed);
    }

    private Observable timeout0(ObservableSource observableSource, Function function, ObservableSource observableSource2) {
        ObjectHelper.requireNonNull(function, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableTimeout(this, observableSource, function, observableSource2));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Observable timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Observable timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableTimer(Math.max(j, 0), timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable unsafeCreate(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "source is null");
        ObjectHelper.requireNonNull(observableSource, "onSubscribe is null");
        if (!(observableSource instanceof Observable)) {
            return RxJavaPlugins.onAssembly((Observable) new ObservableFromUnsafeSource(observableSource));
        }
        throw new IllegalArgumentException("unsafeCreate(Observable) should be upgraded");
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable using(Callable callable, Function function, Consumer consumer) {
        return using(callable, function, consumer, true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable using(Callable callable, Function function, Consumer consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "sourceSupplier is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableUsing(callable, function, consumer, z));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable wrap(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "source is null");
        return observableSource instanceof Observable ? RxJavaPlugins.onAssembly((Observable) observableSource) : RxJavaPlugins.onAssembly((Observable) new ObservableFromUnsafeSource(observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, ObservableSource observableSource7, ObservableSource observableSource8, ObservableSource observableSource9, Function9 function9) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        ObjectHelper.requireNonNull(observableSource9, "source9 is null");
        return zipArray(Functions.toFunction(function9), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8, observableSource9);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, ObservableSource observableSource7, ObservableSource observableSource8, Function8 function8) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        ObjectHelper.requireNonNull(observableSource8, "source8 is null");
        return zipArray(Functions.toFunction(function8), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, ObservableSource observableSource7, Function7 function7) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        ObjectHelper.requireNonNull(observableSource7, "source7 is null");
        return zipArray(Functions.toFunction(function7), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, ObservableSource observableSource6, Function6 function6) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        ObjectHelper.requireNonNull(observableSource6, "source6 is null");
        return zipArray(Functions.toFunction(function6), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, ObservableSource observableSource5, Function5 function5) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        ObjectHelper.requireNonNull(observableSource5, "source5 is null");
        return zipArray(Functions.toFunction(function5), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, Function4 function4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return zipArray(Functions.toFunction(function4), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, Function3 function3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return zipArray(Functions.toFunction(function3), false, bufferSize(), observableSource, observableSource2, observableSource3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), false, bufferSize(), observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, BiFunction biFunction, boolean z) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, bufferSize(), observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, ObservableSource observableSource2, BiFunction biFunction, boolean z, int i) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, i, observableSource, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(ObservableSource observableSource, Function function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableToList(observableSource, 16).flatMap(ObservableInternalHelper.zipIterable(function)));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zip(Iterable iterable, Function function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObservableZip observableZip = new ObservableZip(null, iterable, function, bufferSize(), false);
        return RxJavaPlugins.onAssembly((Observable) observableZip);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zipArray(Function function, boolean z, int i, ObservableSource... observableSourceArr) {
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableZip observableZip = new ObservableZip(observableSourceArr, null, function, i, z);
        return RxJavaPlugins.onAssembly((Observable) observableZip);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static Observable zipIterable(Iterable iterable, Function function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableZip observableZip = new ObservableZip(null, iterable, function, i, z);
        return RxJavaPlugins.onAssembly((Observable) observableZip);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single all(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableAllSingle(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable ambWith(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return ambArray(this, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single any(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableAnySingle(this, predicate));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Object as(@NonNull ObservableConverter observableConverter) {
        ObjectHelper.requireNonNull(observableConverter, "converter is null");
        return observableConverter.apply(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingFirst() {
        BlockingFirstObserver blockingFirstObserver = new BlockingFirstObserver();
        subscribe((Observer) blockingFirstObserver);
        Object blockingGet = blockingFirstObserver.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingFirst(Object obj) {
        BlockingFirstObserver blockingFirstObserver = new BlockingFirstObserver();
        subscribe((Observer) blockingFirstObserver);
        Object blockingGet = blockingFirstObserver.blockingGet();
        return blockingGet != null ? blockingGet : obj;
    }

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

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingIterable(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return new BlockingObservableIterable(this, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingLast() {
        BlockingLastObserver blockingLastObserver = new BlockingLastObserver();
        subscribe((Observer) blockingLastObserver);
        Object blockingGet = blockingLastObserver.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingLast(Object obj) {
        BlockingLastObserver blockingLastObserver = new BlockingLastObserver();
        subscribe((Observer) blockingLastObserver);
        Object blockingGet = blockingLastObserver.blockingGet();
        return blockingGet != null ? blockingGet : obj;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingLatest() {
        return new BlockingObservableLatest(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingMostRecent(Object obj) {
        return new BlockingObservableMostRecent(this, obj);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Iterable blockingNext() {
        return new BlockingObservableNext(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingSingle() {
        Object blockingGet = singleElement().blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Object blockingSingle(Object obj) {
        return single(obj).blockingGet();
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe() {
        ObservableBlockingSubscribe.subscribe(this);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Observer observer) {
        ObservableBlockingSubscribe.subscribe(this, observer);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer consumer) {
        ObservableBlockingSubscribe.subscribe(this, consumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer consumer, Consumer consumer2) {
        ObservableBlockingSubscribe.subscribe(this, consumer, consumer2, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer consumer, Consumer consumer2, Action action) {
        ObservableBlockingSubscribe.subscribe(this, consumer, consumer2, action);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(int i) {
        return buffer(i, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(int i, int i2) {
        return buffer(i, i2, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(int i, int i2, Callable callable) {
        ObjectHelper.verifyPositive(i, "count");
        ObjectHelper.verifyPositive(i2, "skip");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableBuffer(this, i, i2, callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(int i, Callable callable) {
        return buffer(i, i, callable);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable buffer(long j, long j2, TimeUnit timeUnit) {
        return buffer(j, j2, timeUnit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, j2, timeUnit, scheduler, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, Callable callable) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        ObservableBufferTimed observableBufferTimed = new ObservableBufferTimed(this, j, j2, timeUnit2, scheduler2, callable2, Integer.MAX_VALUE, false);
        return RxJavaPlugins.onAssembly((Observable) observableBufferTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable buffer(long j, TimeUnit timeUnit) {
        return buffer(j, timeUnit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable buffer(long j, TimeUnit timeUnit, int i) {
        return buffer(j, timeUnit, Schedulers.computation(), i);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable buffer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, timeUnit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        return buffer(j, timeUnit, scheduler, i, ArrayListSupplier.asCallable(), false);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i, Callable callable, boolean z) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "count");
        ObservableBufferTimed observableBufferTimed = new ObservableBufferTimed(this, j, j, timeUnit2, scheduler2, callable2, i2, z);
        return RxJavaPlugins.onAssembly((Observable) observableBufferTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(ObservableSource observableSource) {
        return buffer(observableSource, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(ObservableSource observableSource, int i) {
        ObjectHelper.verifyPositive(i, "initialCapacity");
        return buffer(observableSource, Functions.createArrayList(i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(ObservableSource observableSource, Function function) {
        return buffer(observableSource, function, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(ObservableSource observableSource, Function function, Callable callable) {
        ObjectHelper.requireNonNull(observableSource, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableBufferBoundary(this, observableSource, function, callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(ObservableSource observableSource, Callable callable) {
        ObjectHelper.requireNonNull(observableSource, "boundary is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableBufferExactBoundary(this, observableSource, callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(Callable callable) {
        return buffer(callable, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable buffer(Callable callable, Callable callable2) {
        ObjectHelper.requireNonNull(callable, "boundarySupplier is null");
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableBufferBoundarySupplier(this, callable, callable2));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable cache() {
        return ObservableCache.from(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable cacheWithInitialCapacity(int i) {
        return ObservableCache.from(this, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable cast(Class cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single collect(Callable callable, BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(callable, "initialValueSupplier is null");
        ObjectHelper.requireNonNull(biConsumer, "collector is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableCollectSingle(this, callable, biConsumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single collectInto(Object obj, BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(obj, "initialValue is null");
        return collect(Functions.justCallable(obj), biConsumer);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable compose(ObservableTransformer observableTransformer) {
        ObjectHelper.requireNonNull(observableTransformer, "composer is null");
        return wrap(observableTransformer.apply(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMap(Function function) {
        return concatMap(function, 2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMap(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Observable) new ObservableConcatMap(this, function, i, ErrorMode.IMMEDIATE));
        }
        Object call = ((ScalarCallable) this).call();
        return call == null ? empty() : ObservableScalarXMap.scalarXMap(call, function);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Completable concatMapCompletable(Function function) {
        return concatMapCompletable(function, 2);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Completable concatMapCompletable(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Completable) new ObservableConcatMapCompletable(this, function, i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapDelayError(Function function) {
        return concatMapDelayError(function, bufferSize(), true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapDelayError(Function function, int i, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            return call == null ? empty() : ObservableScalarXMap.scalarXMap(call, function);
        }
        return RxJavaPlugins.onAssembly((Observable) new ObservableConcatMap(this, function, i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapEager(Function function) {
        return concatMapEager(function, Integer.MAX_VALUE, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapEager(Function function, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        ObservableConcatMapEager observableConcatMapEager = new ObservableConcatMapEager(this, function, ErrorMode.IMMEDIATE, i, i2);
        return RxJavaPlugins.onAssembly((Observable) observableConcatMapEager);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapEagerDelayError(Function function, int i, int i2, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        ObservableConcatMapEager observableConcatMapEager = new ObservableConcatMapEager(this, function, z ? ErrorMode.END : ErrorMode.BOUNDARY, i, i2);
        return RxJavaPlugins.onAssembly((Observable) observableConcatMapEager);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapEagerDelayError(Function function, boolean z) {
        return concatMapEagerDelayError(function, Integer.MAX_VALUE, bufferSize(), z);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapIterable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFlattenIterable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatMapIterable(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return concatMap(ObservableInternalHelper.flatMapIntoIterable(function), i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable concatWith(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return concat((ObservableSource) this, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single contains(Object obj) {
        ObjectHelper.requireNonNull(obj, "element is null");
        return any(Functions.equalsWith(obj));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single count() {
        return RxJavaPlugins.onAssembly((Single) new ObservableCountSingle(this));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable debounce(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable debounce(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableDebounceTimed observableDebounceTimed = new ObservableDebounceTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable) observableDebounceTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable debounce(Function function) {
        ObjectHelper.requireNonNull(function, "debounceSelector is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDebounce(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable defaultIfEmpty(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultItem is null");
        return switchIfEmpty(just(obj));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableDelay observableDelay = new ObservableDelay(this, j, timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Observable) observableDelay);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable delay(long j, TimeUnit timeUnit, boolean z) {
        return delay(j, timeUnit, Schedulers.computation(), z);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable delay(ObservableSource observableSource, Function function) {
        return delaySubscription(observableSource).delay(function);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable delay(Function function) {
        ObjectHelper.requireNonNull(function, "itemDelay is null");
        return flatMap(ObservableInternalHelper.itemDelay(function));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription(timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable delaySubscription(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDelaySubscriptionOther(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable dematerialize() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableDematerialize(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable distinct(Function function) {
        return distinct(function, Functions.createHashSet());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable distinct(Function function, Callable callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDistinct(this, function, callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable distinctUntilChanged(BiPredicate biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "comparer is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDistinctUntilChanged(this, Functions.identity(), biPredicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable distinctUntilChanged(Function function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDistinctUntilChanged(this, function, ObjectHelper.equalsPredicate()));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doAfterNext(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onAfterNext is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDoAfterNext(this, consumer));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doAfterTerminate(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDoFinally(this, action));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnComplete(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnDispose(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), action);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnEach(Observer observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        return doOnEach(ObservableInternalHelper.observerOnNext(observer), ObservableInternalHelper.observerOnError(observer), ObservableInternalHelper.observerOnComplete(observer), Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnEach(Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "consumer is null");
        return doOnEach(Functions.notificationOnNext(consumer), Functions.notificationOnError(consumer), Functions.notificationOnComplete(consumer), Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnError(Consumer consumer) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        return doOnEach(emptyConsumer, consumer, action, action);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnLifecycle(Consumer consumer, Action action) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        ObjectHelper.requireNonNull(action, "onDispose is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableDoOnLifecycle(this, consumer, action));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnNext(Consumer consumer) {
        Consumer emptyConsumer = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        return doOnEach(consumer, emptyConsumer, action, action);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnSubscribe(Consumer consumer) {
        return doOnLifecycle(consumer, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable doOnTerminate(Action action) {
        ObjectHelper.requireNonNull(action, "onTerminate is null");
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(action), action, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe elementAt(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Maybe) new ObservableElementAtMaybe(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single elementAt(long j, Object obj) {
        if (j >= 0) {
            ObjectHelper.requireNonNull(obj, "defaultItem is null");
            return RxJavaPlugins.onAssembly((Single) new ObservableElementAtSingle(this, j, obj));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single elementAtOrError(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Single) new ObservableElementAtSingle(this, j, null));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable filter(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFilter(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single first(Object obj) {
        return elementAt(0, obj);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe firstElement() {
        return elementAt(0);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single firstOrError() {
        return elementAtOrError(0);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function) {
        return flatMap(function, false);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, int i) {
        return flatMap(function, false, i, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, BiFunction biFunction) {
        return flatMap(function, biFunction, false, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, BiFunction biFunction, int i) {
        return flatMap(function, biFunction, false, i, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, BiFunction biFunction, boolean z) {
        return flatMap(function, biFunction, z, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, BiFunction biFunction, boolean z, int i) {
        return flatMap(function, biFunction, z, i, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, BiFunction biFunction, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return flatMap(ObservableInternalHelper.flatMapWithCombiner(function, biFunction), z, i, i2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, Function function2, Callable callable) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((ObservableSource) new ObservableMapNotification(this, function, function2, callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, Function function2, Callable callable, int i) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((ObservableSource) new ObservableMapNotification(this, function, function2, callable), i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, boolean z) {
        return flatMap(function, z, Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, boolean z, int i) {
        return flatMap(function, z, i, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMap(Function function, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "bufferSize");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            return call == null ? empty() : ObservableScalarXMap.scalarXMap(call, function);
        }
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(this, function, z, i, i2);
        return RxJavaPlugins.onAssembly((Observable) observableFlatMap);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function function) {
        return flatMapCompletable(function, false);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new ObservableFlatMapCompletableCompletable(this, function, z));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMapIterable(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFlattenIterable(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMapIterable(Function function, BiFunction biFunction) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return flatMap(ObservableInternalHelper.flatMapIntoIterable(function), biFunction, false, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMapMaybe(Function function) {
        return flatMapMaybe(function, false);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMapMaybe(Function function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFlatMapMaybe(this, function, z));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMapSingle(Function function) {
        return flatMapSingle(function, false);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable flatMapSingle(Function function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableFlatMapSingle(this, function, z));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEach(Consumer consumer) {
        return subscribe(consumer);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate predicate) {
        return forEachWhile(predicate, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate predicate, Consumer consumer) {
        return forEachWhile(predicate, consumer, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate predicate, Consumer consumer, Action action) {
        ObjectHelper.requireNonNull(predicate, "onNext is null");
        ObjectHelper.requireNonNull(consumer, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ForEachWhileObserver forEachWhileObserver = new ForEachWhileObserver(predicate, consumer, action);
        subscribe((Observer) forEachWhileObserver);
        return forEachWhileObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable groupBy(Function function) {
        return groupBy(function, Functions.identity(), false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable groupBy(Function function, Function function2) {
        return groupBy(function, function2, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable groupBy(Function function, Function function2, boolean z) {
        return groupBy(function, function2, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable groupBy(Function function, Function function2, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableGroupBy observableGroupBy = new ObservableGroupBy(this, function, function2, i, z);
        return RxJavaPlugins.onAssembly((Observable) observableGroupBy);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable groupBy(Function function, boolean z) {
        return groupBy(function, Functions.identity(), z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable groupJoin(ObservableSource observableSource, Function function, Function function2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        ObjectHelper.requireNonNull(function, "leftEnd is null");
        ObjectHelper.requireNonNull(function2, "rightEnd is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        ObservableGroupJoin observableGroupJoin = new ObservableGroupJoin(this, observableSource, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Observable) observableGroupJoin);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable hide() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableHide(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly((Completable) new ObservableIgnoreElementsCompletable(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable join(ObservableSource observableSource, Function function, Function function2, BiFunction biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        ObjectHelper.requireNonNull(function, "leftEnd is null");
        ObjectHelper.requireNonNull(function2, "rightEnd is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        ObservableJoin observableJoin = new ObservableJoin(this, observableSource, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Observable) observableJoin);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single last(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableLastSingle(this, obj));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe lastElement() {
        return RxJavaPlugins.onAssembly((Maybe) new ObservableLastMaybe(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single lastOrError() {
        return RxJavaPlugins.onAssembly((Single) new ObservableLastSingle(this, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable lift(ObservableOperator observableOperator) {
        ObjectHelper.requireNonNull(observableOperator, "onLift is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableLift(this, observableOperator));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable map(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableMap(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable materialize() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableMaterialize(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable mergeWith(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return merge((ObservableSource) this, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable observeOn(Scheduler scheduler, boolean z) {
        return observeOn(scheduler, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable observeOn(Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Observable) new ObservableObserveOn(this, scheduler, z, i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable ofType(Class cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return filter(Functions.isInstanceOf(cls)).cast(cls);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable onErrorResumeNext(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "next is null");
        return onErrorResumeNext(Functions.justFunction(observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable onErrorResumeNext(Function function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableOnErrorNext(this, function, false));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable onErrorReturn(Function function) {
        ObjectHelper.requireNonNull(function, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableOnErrorReturn(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable onErrorReturnItem(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return onErrorReturn(Functions.justFunction(obj));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable onExceptionResumeNext(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "next is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableOnErrorNext(this, Functions.justFunction(observableSource), true));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableDetach(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable publish(Function function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservablePublishSelector(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final ConnectableObservable publish() {
        return ObservablePublish.create(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe reduce(BiFunction biFunction) {
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly((Maybe) new ObservableReduceMaybe(this, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single reduce(Object obj, BiFunction biFunction) {
        ObjectHelper.requireNonNull(obj, "seed is null");
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableReduceSeedSingle(this, obj, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single reduceWith(Callable callable, BiFunction biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableReduceWithSingle(this, callable, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable repeat() {
        return repeat(Long.MAX_VALUE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable repeat(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? empty() : RxJavaPlugins.onAssembly((Observable) new ObservableRepeat(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("times >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable repeatUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableRepeatUntil(this, booleanSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable repeatWhen(Function function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableRepeatWhen(this, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable replay(Function function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), function);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable replay(Function function, int i) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i), function);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable replay(Function function, int i, long j, TimeUnit timeUnit) {
        return replay(function, i, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable replay(Function function, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i, j, timeUnit, scheduler), function);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable replay(Function function, int i, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i), ObservableInternalHelper.replayFunction(function, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable replay(Function function, long j, TimeUnit timeUnit) {
        return replay(function, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable replay(Function function, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, j, timeUnit, scheduler), function);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable replay(Function function, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), ObservableInternalHelper.replayFunction(function, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final ConnectableObservable replay() {
        return ObservableReplay.createFrom(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final ConnectableObservable replay(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.create((ObservableSource) this, i);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableObservable replay(int i, long j, TimeUnit timeUnit) {
        return replay(i, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableObservable replay(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, j, timeUnit, scheduler, i);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableObservable replay(int i, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return ObservableReplay.observeOn(replay(i), scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableObservable replay(long j, TimeUnit timeUnit) {
        return replay(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableObservable replay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final ConnectableObservable replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.observeOn(replay(), scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable retry() {
        return retry(Long.MAX_VALUE, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable retry(long j) {
        return retry(j, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable retry(long j, Predicate predicate) {
        if (j >= 0) {
            ObjectHelper.requireNonNull(predicate, "predicate is null");
            return RxJavaPlugins.onAssembly((Observable) new ObservableRetryPredicate(this, j, predicate));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("times >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable retry(BiPredicate biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableRetryBiPredicate(this, biPredicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable retry(Predicate predicate) {
        return retry(Long.MAX_VALUE, predicate);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable retryUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return retry(Long.MAX_VALUE, Functions.predicateReverseFor(booleanSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable retryWhen(Function function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableRetryWhen(this, function));
    }

    @SchedulerSupport("none")
    public final void safeSubscribe(Observer observer) {
        ObjectHelper.requireNonNull(observer, "s is null");
        if (observer instanceof SafeObserver) {
            subscribe(observer);
        } else {
            subscribe((Observer) new SafeObserver(observer));
        }
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable sample(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable sample(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableSampleTimed observableSampleTimed = new ObservableSampleTimed(this, j, timeUnit, scheduler, false);
        return RxJavaPlugins.onAssembly((Observable) observableSampleTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable sample(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableSampleTimed observableSampleTimed = new ObservableSampleTimed(this, j, timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Observable) observableSampleTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable sample(long j, TimeUnit timeUnit, boolean z) {
        return sample(j, timeUnit, Schedulers.computation(), z);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable sample(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sampler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSampleWithObservable(this, observableSource, false));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable sample(ObservableSource observableSource, boolean z) {
        ObjectHelper.requireNonNull(observableSource, "sampler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSampleWithObservable(this, observableSource, z));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable scan(BiFunction biFunction) {
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableScan(this, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable scan(Object obj, BiFunction biFunction) {
        ObjectHelper.requireNonNull(obj, "seed is null");
        return scanWith(Functions.justCallable(obj), biFunction);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable scanWith(Callable callable, BiFunction biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableScanSeed(this, callable, biFunction));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable serialize() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableSerialized(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable share() {
        return publish().refCount();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single single(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableSingleSingle(this, obj));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe singleElement() {
        return RxJavaPlugins.onAssembly((Maybe) new ObservableSingleMaybe(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single singleOrError() {
        return RxJavaPlugins.onAssembly((Single) new ObservableSingleSingle(this, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable skip(long j) {
        return j <= 0 ? RxJavaPlugins.onAssembly(this) : RxJavaPlugins.onAssembly((Observable) new ObservableSkip(this, j));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable skip(long j, TimeUnit timeUnit) {
        return skipUntil(timer(j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable skip(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipUntil(timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable skipLast(int i) {
        if (i >= 0) {
            return i == 0 ? RxJavaPlugins.onAssembly(this) : RxJavaPlugins.onAssembly((Observable) new ObservableSkipLast(this, i));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(i);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable skipLast(long j, TimeUnit timeUnit) {
        return skipLast(j, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable skipLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return skipLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableSkipLastTimed observableSkipLastTimed = new ObservableSkipLastTimed(this, j, timeUnit, scheduler, i << 1, z);
        return RxJavaPlugins.onAssembly((Observable) observableSkipLastTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable skipLast(long j, TimeUnit timeUnit, boolean z) {
        return skipLast(j, timeUnit, Schedulers.trampoline(), z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable skipUntil(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSkipUntil(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable skipWhile(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSkipWhile(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable sorted() {
        return toList().toObservable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable sorted(Comparator comparator) {
        ObjectHelper.requireNonNull(comparator, "sortFunction is null");
        return toList().toObservable().map(Functions.listSorter(comparator)).flatMapIterable(Functions.identity());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable startWith(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return concatArray(observableSource, this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable startWith(Iterable iterable) {
        return concatArray(fromIterable(iterable), this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable startWith(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return concatArray(just(obj), this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable startWithArray(Object... objArr) {
        Observable fromArray = fromArray(objArr);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer) {
        return subscribe(consumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2) {
        return subscribe(consumer, consumer2, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2, Action action) {
        return subscribe(consumer, consumer2, action, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer consumer, Consumer consumer2, Action action, Consumer consumer3) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(consumer3, "onSubscribe is null");
        LambdaObserver lambdaObserver = new LambdaObserver(consumer, consumer2, action, consumer3);
        subscribe((Observer) lambdaObserver);
        return lambdaObserver;
    }

    @SchedulerSupport("none")
    public final void subscribe(Observer observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        try {
            Observer onSubscribe = RxJavaPlugins.onSubscribe(this, observer);
            ObjectHelper.requireNonNull(onSubscribe, "Plugin returned null Observer");
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

    public abstract void subscribeActual(Observer observer);

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observer subscribeWith(Observer observer) {
        subscribe(observer);
        return observer;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable switchIfEmpty(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableSwitchIfEmpty(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable switchMap(Function function) {
        return switchMap(function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable switchMap(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Observable) new ObservableSwitchMap(this, function, i, false));
        }
        Object call = ((ScalarCallable) this).call();
        return call == null ? empty() : ObservableScalarXMap.scalarXMap(call, function);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable switchMapDelayError(Function function) {
        return switchMapDelayError(function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable switchMapDelayError(Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Observable) new ObservableSwitchMap(this, function, i, true));
        }
        Object call = ((ScalarCallable) this).call();
        return call == null ? empty() : ObservableScalarXMap.scalarXMap(call, function);
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    @SchedulerSupport("none")
    public final Observable switchMapSingle(@NonNull Function function) {
        return ObservableInternalHelper.switchMapSingle(this, function);
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    @SchedulerSupport("none")
    public final Observable switchMapSingleDelayError(@NonNull Function function) {
        return ObservableInternalHelper.switchMapSingleDelayError(this, function);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable take(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Observable) new ObservableTake(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable take(long j, TimeUnit timeUnit) {
        return takeUntil((ObservableSource) timer(j, timeUnit));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable take(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeUntil((ObservableSource) timer(j, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable takeLast(int i) {
        if (i >= 0) {
            return i == 0 ? RxJavaPlugins.onAssembly((Observable) new ObservableIgnoreElements(this)) : i == 1 ? RxJavaPlugins.onAssembly((Observable) new ObservableTakeLastOne(this)) : RxJavaPlugins.onAssembly((Observable) new ObservableTakeLast(this, i));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(i);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable takeLast(long j, long j2, TimeUnit timeUnit) {
        return takeLast(j, j2, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, j2, timeUnit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        long j3 = j;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (j3 >= 0) {
            ObservableTakeLastTimed observableTakeLastTimed = new ObservableTakeLastTimed(this, j, j2, timeUnit, scheduler, i, z);
            return RxJavaPlugins.onAssembly((Observable) observableTakeLastTimed);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable takeLast(long j, TimeUnit timeUnit) {
        return takeLast(j, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable takeLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return takeLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        return takeLast(Long.MAX_VALUE, j, timeUnit, scheduler, z, i);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable takeLast(long j, TimeUnit timeUnit, boolean z) {
        return takeLast(j, timeUnit, Schedulers.trampoline(), z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable takeUntil(ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableTakeUntil(this, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable takeUntil(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableTakeUntilPredicate(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable takeWhile(Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableTakeWhile(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver test() {
        TestObserver testObserver = new TestObserver();
        subscribe((Observer) testObserver);
        return testObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver test(boolean z) {
        TestObserver testObserver = new TestObserver();
        if (z) {
            testObserver.dispose();
        }
        subscribe((Observer) testObserver);
        return testObserver;
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable throttleFirst(long j, TimeUnit timeUnit) {
        return throttleFirst(j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable throttleFirst(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableThrottleFirstTimed observableThrottleFirstTimed = new ObservableThrottleFirstTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable) observableThrottleFirstTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable throttleLast(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable throttleLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return sample(j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable throttleWithTimeout(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable throttleWithTimeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return debounce(j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeInterval(TimeUnit timeUnit) {
        return timeInterval(timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeInterval(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableTimeInterval(this, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, null, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable timeout(long j, TimeUnit timeUnit, ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(j, timeUnit, observableSource, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, null, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable timeout(long j, TimeUnit timeUnit, Scheduler scheduler, ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(j, timeUnit, observableSource, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeout(ObservableSource observableSource, Function function) {
        ObjectHelper.requireNonNull(observableSource, "firstTimeoutIndicator is null");
        return timeout0(observableSource, function, null);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeout(ObservableSource observableSource, Function function, ObservableSource observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "firstTimeoutIndicator is null");
        ObjectHelper.requireNonNull(observableSource2, "other is null");
        return timeout0(observableSource, function, observableSource2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeout(Function function) {
        return timeout0(null, function, null);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timeout(Function function, ObservableSource observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(null, function, observableSource);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timestamp(TimeUnit timeUnit) {
        return timestamp(timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable timestamp(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(timeUnit, scheduler));
    }

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

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable toFlowable(BackpressureStrategy backpressureStrategy) {
        FlowableFromObservable flowableFromObservable = new FlowableFromObservable(this);
        int i = AnonymousClass1.$SwitchMap$io$reactivex$BackpressureStrategy[backpressureStrategy.ordinal()];
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? flowableFromObservable.onBackpressureBuffer() : RxJavaPlugins.onAssembly((Flowable) new FlowableOnBackpressureError(flowableFromObservable)) : flowableFromObservable : flowableFromObservable.onBackpressureLatest() : flowableFromObservable.onBackpressureDrop();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Future toFuture() {
        return (Future) subscribeWith(new FutureObserver());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toList() {
        return toList(16);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toList(int i) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Single) new ObservableToListSingle((ObservableSource) this, i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toList(Callable callable) {
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Single) new ObservableToListSingle((ObservableSource) this, callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMap(Function function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMap(Function function, Function function2) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(function, function2));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMap(Function function, Function function2, Callable callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.requireNonNull(callable, "mapSupplier is null");
        return collect(callable, Functions.toMapKeyValueSelector(function, function2));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function) {
        return toMultimap(function, Functions.identity(), HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function, Function function2) {
        return toMultimap(function, function2, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function, Function function2, Callable callable) {
        return toMultimap(function, function2, callable, ArrayListSupplier.asFunction());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toMultimap(Function function, Function function2, Callable callable, Function function3) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.requireNonNull(callable, "mapSupplier is null");
        ObjectHelper.requireNonNull(function3, "collectionFactory is null");
        return collect(callable, Functions.toMultimapKeyValueSelector(function, function2, function3));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList() {
        return toSortedList(Functions.naturalOrder());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList(int i) {
        return toSortedList(Functions.naturalOrder(), i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList(Comparator comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single toSortedList(Comparator comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(i).map(Functions.listSorter(comparator));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableUnsubscribeOn(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(long j) {
        return window(j, j, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(long j, long j2) {
        return window(j, j2, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(long j, long j2, int i) {
        ObjectHelper.verifyPositive(j, "count");
        ObjectHelper.verifyPositive(j2, "skip");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableWindow observableWindow = new ObservableWindow(this, j, j2, i);
        return RxJavaPlugins.onAssembly((Observable) observableWindow);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable window(long j, long j2, TimeUnit timeUnit) {
        return window(j, j2, timeUnit, Schedulers.computation(), bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, j2, timeUnit, scheduler, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, int i) {
        long j3 = j;
        ObjectHelper.verifyPositive(j, "timespan");
        long j4 = j2;
        ObjectHelper.verifyPositive(j4, "timeskip");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        ObservableWindowTimed observableWindowTimed = new ObservableWindowTimed(this, j3, j4, timeUnit2, scheduler2, Long.MAX_VALUE, i2, false);
        return RxJavaPlugins.onAssembly((Observable) observableWindowTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable window(long j, TimeUnit timeUnit) {
        return window(j, timeUnit, Schedulers.computation(), Long.MAX_VALUE, false);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable window(long j, TimeUnit timeUnit, long j2) {
        return window(j, timeUnit, Schedulers.computation(), j2, false);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable window(long j, TimeUnit timeUnit, long j2, boolean z) {
        return window(j, timeUnit, Schedulers.computation(), j2, z);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable window(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, timeUnit, scheduler, Long.MAX_VALUE, false);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2) {
        return window(j, timeUnit, scheduler, j2, false);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z) {
        return window(j, timeUnit, scheduler, j2, z, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z, int i) {
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        long j3 = j2;
        ObjectHelper.verifyPositive(j3, "count");
        ObservableWindowTimed observableWindowTimed = new ObservableWindowTimed(this, j, j, timeUnit2, scheduler2, j3, i2, z);
        return RxJavaPlugins.onAssembly((Observable) observableWindowTimed);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(ObservableSource observableSource) {
        return window(observableSource, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(ObservableSource observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "boundary is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Observable) new ObservableWindowBoundary(this, observableSource, i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(ObservableSource observableSource, Function function) {
        return window(observableSource, function, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(ObservableSource observableSource, Function function, int i) {
        ObjectHelper.requireNonNull(observableSource, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Observable) new ObservableWindowBoundarySelector(this, observableSource, function, i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(Callable callable) {
        return window(callable, bufferSize());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable window(Callable callable, int i) {
        ObjectHelper.requireNonNull(callable, "boundary is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Observable) new ObservableWindowBoundarySupplier(this, callable, i));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable withLatestFrom(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, ObservableSource observableSource4, Function5 function5) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(observableSource3, "o3 is null");
        ObjectHelper.requireNonNull(observableSource4, "o4 is null");
        ObjectHelper.requireNonNull(function5, "combiner is null");
        return withLatestFrom(new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4}, Functions.toFunction(function5));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable withLatestFrom(ObservableSource observableSource, ObservableSource observableSource2, ObservableSource observableSource3, Function4 function4) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(observableSource3, "o3 is null");
        ObjectHelper.requireNonNull(function4, "combiner is null");
        return withLatestFrom(new ObservableSource[]{observableSource, observableSource2, observableSource3}, Functions.toFunction(function4));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable withLatestFrom(ObservableSource observableSource, ObservableSource observableSource2, Function3 function3) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(function3, "combiner is null");
        return withLatestFrom(new ObservableSource[]{observableSource, observableSource2}, Functions.toFunction(function3));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable withLatestFrom(ObservableSource observableSource, BiFunction biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableWithLatestFrom(this, biFunction, observableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable withLatestFrom(Iterable iterable, Function function) {
        ObjectHelper.requireNonNull(iterable, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableWithLatestFromMany((ObservableSource) this, iterable, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable withLatestFrom(ObservableSource[] observableSourceArr, Function function) {
        ObjectHelper.requireNonNull(observableSourceArr, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableWithLatestFromMany((ObservableSource) this, observableSourceArr, function));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable zipWith(ObservableSource observableSource, BiFunction biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return zip(this, observableSource, biFunction);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable zipWith(ObservableSource observableSource, BiFunction biFunction, boolean z) {
        return zip((ObservableSource) this, observableSource, biFunction, z);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable zipWith(ObservableSource observableSource, BiFunction biFunction, boolean z, int i) {
        return zip((ObservableSource) this, observableSource, biFunction, z, i);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable zipWith(Iterable iterable, BiFunction biFunction) {
        ObjectHelper.requireNonNull(iterable, "other is null");
        ObjectHelper.requireNonNull(biFunction, "zipper is null");
        return RxJavaPlugins.onAssembly((Observable) new ObservableZipIterable(this, iterable, biFunction));
    }
}
