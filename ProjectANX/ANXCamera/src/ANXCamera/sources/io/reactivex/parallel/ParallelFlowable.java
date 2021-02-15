package io.reactivex.parallel;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.Beta;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.LongConsumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.parallel.ParallelCollect;
import io.reactivex.internal.operators.parallel.ParallelConcatMap;
import io.reactivex.internal.operators.parallel.ParallelDoOnNextTry;
import io.reactivex.internal.operators.parallel.ParallelFilter;
import io.reactivex.internal.operators.parallel.ParallelFilterTry;
import io.reactivex.internal.operators.parallel.ParallelFlatMap;
import io.reactivex.internal.operators.parallel.ParallelFromArray;
import io.reactivex.internal.operators.parallel.ParallelFromPublisher;
import io.reactivex.internal.operators.parallel.ParallelJoin;
import io.reactivex.internal.operators.parallel.ParallelMap;
import io.reactivex.internal.operators.parallel.ParallelMapTry;
import io.reactivex.internal.operators.parallel.ParallelPeek;
import io.reactivex.internal.operators.parallel.ParallelReduce;
import io.reactivex.internal.operators.parallel.ParallelReduceFull;
import io.reactivex.internal.operators.parallel.ParallelRunOn;
import io.reactivex.internal.operators.parallel.ParallelSortedJoin;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.ListAddBiConsumer;
import io.reactivex.internal.util.MergerBiFunction;
import io.reactivex.internal.util.SorterFunction;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Comparator;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

@Beta
public abstract class ParallelFlowable {
    @CheckReturnValue
    public static ParallelFlowable from(@NonNull Publisher publisher) {
        return from(publisher, Runtime.getRuntime().availableProcessors(), Flowable.bufferSize());
    }

    @CheckReturnValue
    public static ParallelFlowable from(@NonNull Publisher publisher, int i) {
        return from(publisher, i, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public static ParallelFlowable from(@NonNull Publisher publisher, int i, int i2) {
        ObjectHelper.requireNonNull(publisher, "source");
        ObjectHelper.verifyPositive(i, "parallelism");
        ObjectHelper.verifyPositive(i2, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelFromPublisher(publisher, i, i2));
    }

    @CheckReturnValue
    @NonNull
    public static ParallelFlowable fromArray(@NonNull Publisher... publisherArr) {
        if (publisherArr.length != 0) {
            return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelFromArray(publisherArr));
        }
        throw new IllegalArgumentException("Zero publishers not supported");
    }

    @CheckReturnValue
    @NonNull
    @Experimental
    public final Object as(@NonNull ParallelFlowableConverter parallelFlowableConverter) {
        ObjectHelper.requireNonNull(parallelFlowableConverter, "converter is null");
        return parallelFlowableConverter.apply(this);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable collect(@NonNull Callable callable, @NonNull BiConsumer biConsumer) {
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        ObjectHelper.requireNonNull(biConsumer, "collector is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelCollect(this, callable, biConsumer));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable compose(@NonNull ParallelTransformer parallelTransformer) {
        ObjectHelper.requireNonNull(parallelTransformer, "composer is null");
        return RxJavaPlugins.onAssembly(parallelTransformer.apply(this));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable concatMap(@NonNull Function function) {
        return concatMap(function, 2);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable concatMap(@NonNull Function function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelConcatMap(this, function, i, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable concatMapDelayError(@NonNull Function function, int i, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelConcatMap(this, function, i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable concatMapDelayError(@NonNull Function function, boolean z) {
        return concatMapDelayError(function, 2, z);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doAfterNext(@NonNull Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onAfterNext is null");
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        ParallelPeek parallelPeek = new ParallelPeek(this, emptyConsumer, consumer, emptyConsumer2, action, action, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doAfterTerminated(@NonNull Action action) {
        ObjectHelper.requireNonNull(action, "onAfterTerminate is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doOnCancel(@NonNull Action action) {
        ObjectHelper.requireNonNull(action, "onCancel is null");
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Consumer emptyConsumer3 = Functions.emptyConsumer();
        Action action2 = Functions.EMPTY_ACTION;
        ParallelPeek parallelPeek = new ParallelPeek(this, emptyConsumer, emptyConsumer2, emptyConsumer3, action2, action2, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, action);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doOnComplete(@NonNull Action action) {
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doOnError(@NonNull Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onError is null");
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        ParallelPeek parallelPeek = new ParallelPeek(this, emptyConsumer, emptyConsumer2, consumer, action, action, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doOnNext(@NonNull Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        ParallelPeek parallelPeek = new ParallelPeek(this, consumer, emptyConsumer, emptyConsumer2, action, action, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    @Experimental
    public final ParallelFlowable doOnNext(@NonNull Consumer consumer, @NonNull BiFunction biFunction) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(biFunction, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelDoOnNextTry(this, consumer, biFunction));
    }

    @CheckReturnValue
    @NonNull
    @Experimental
    public final ParallelFlowable doOnNext(@NonNull Consumer consumer, @NonNull ParallelFailureHandling parallelFailureHandling) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(parallelFailureHandling, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelDoOnNextTry(this, consumer, parallelFailureHandling));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doOnRequest(@NonNull LongConsumer longConsumer) {
        ObjectHelper.requireNonNull(longConsumer, "onRequest is null");
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Consumer emptyConsumer3 = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        ParallelPeek parallelPeek = new ParallelPeek(this, emptyConsumer, emptyConsumer2, emptyConsumer3, action, action, Functions.emptyConsumer(), longConsumer, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable doOnSubscribe(@NonNull Consumer consumer) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        Consumer emptyConsumer = Functions.emptyConsumer();
        Consumer emptyConsumer2 = Functions.emptyConsumer();
        Consumer emptyConsumer3 = Functions.emptyConsumer();
        Action action = Functions.EMPTY_ACTION;
        ParallelPeek parallelPeek = new ParallelPeek(this, emptyConsumer, emptyConsumer2, emptyConsumer3, action, action, consumer, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelPeek);
    }

    @CheckReturnValue
    public final ParallelFlowable filter(@NonNull Predicate predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelFilter(this, predicate));
    }

    @CheckReturnValue
    @Experimental
    public final ParallelFlowable filter(@NonNull Predicate predicate, @NonNull BiFunction biFunction) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        ObjectHelper.requireNonNull(biFunction, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelFilterTry(this, predicate, biFunction));
    }

    @CheckReturnValue
    @Experimental
    public final ParallelFlowable filter(@NonNull Predicate predicate, @NonNull ParallelFailureHandling parallelFailureHandling) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        ObjectHelper.requireNonNull(parallelFailureHandling, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelFilterTry(this, predicate, parallelFailureHandling));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable flatMap(@NonNull Function function) {
        return flatMap(function, false, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable flatMap(@NonNull Function function, boolean z) {
        return flatMap(function, z, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable flatMap(@NonNull Function function, boolean z, int i) {
        return flatMap(function, z, i, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable flatMap(@NonNull Function function, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        ParallelFlatMap parallelFlatMap = new ParallelFlatMap(this, function, z, i, i2);
        return RxJavaPlugins.onAssembly((ParallelFlowable) parallelFlatMap);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable map(@NonNull Function function) {
        ObjectHelper.requireNonNull(function, "mapper");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelMap(this, function));
    }

    @CheckReturnValue
    @NonNull
    @Experimental
    public final ParallelFlowable map(@NonNull Function function, @NonNull BiFunction biFunction) {
        ObjectHelper.requireNonNull(function, "mapper");
        ObjectHelper.requireNonNull(biFunction, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelMapTry(this, function, biFunction));
    }

    @CheckReturnValue
    @NonNull
    @Experimental
    public final ParallelFlowable map(@NonNull Function function, @NonNull ParallelFailureHandling parallelFailureHandling) {
        ObjectHelper.requireNonNull(function, "mapper");
        ObjectHelper.requireNonNull(parallelFailureHandling, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelMapTry(this, function, parallelFailureHandling));
    }

    public abstract int parallelism();

    @CheckReturnValue
    @NonNull
    public final Flowable reduce(@NonNull BiFunction biFunction) {
        ObjectHelper.requireNonNull(biFunction, "reducer");
        return RxJavaPlugins.onAssembly((Flowable) new ParallelReduceFull(this, biFunction));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable reduce(@NonNull Callable callable, @NonNull BiFunction biFunction) {
        ObjectHelper.requireNonNull(callable, "initialSupplier");
        ObjectHelper.requireNonNull(biFunction, "reducer");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelReduce(this, callable, biFunction));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable runOn(@NonNull Scheduler scheduler) {
        return runOn(scheduler, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable runOn(@NonNull Scheduler scheduler, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable) new ParallelRunOn(this, scheduler, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Flowable sequential() {
        return sequential(Flowable.bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable sequential(int i) {
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable) new ParallelJoin(this, i, false));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable sequentialDelayError() {
        return sequentialDelayError(Flowable.bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable sequentialDelayError(int i) {
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable) new ParallelJoin(this, i, true));
    }

    @CheckReturnValue
    @NonNull
    public final Flowable sorted(@NonNull Comparator comparator) {
        return sorted(comparator, 16);
    }

    @CheckReturnValue
    @NonNull
    public final Flowable sorted(@NonNull Comparator comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Flowable) new ParallelSortedJoin(reduce(Functions.createArrayList((i / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)), comparator));
    }

    public abstract void subscribe(@NonNull Subscriber[] subscriberArr);

    @CheckReturnValue
    @NonNull
    public final Object to(@NonNull Function function) {
        try {
            ObjectHelper.requireNonNull(function, "converter is null");
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @CheckReturnValue
    @NonNull
    public final Flowable toSortedList(@NonNull Comparator comparator) {
        return toSortedList(comparator, 16);
    }

    @CheckReturnValue
    @NonNull
    public final Flowable toSortedList(@NonNull Comparator comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly(reduce(Functions.createArrayList((i / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)).reduce(new MergerBiFunction(comparator)));
    }

    /* access modifiers changed from: protected */
    public final boolean validate(@NonNull Subscriber[] subscriberArr) {
        int parallelism = parallelism();
        if (subscriberArr.length == parallelism) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("parallelism = ");
        sb.append(parallelism);
        sb.append(", subscribers = ");
        sb.append(subscriberArr.length);
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException(sb.toString());
        for (Subscriber error : subscriberArr) {
            EmptySubscription.error(illegalArgumentException, error);
        }
        return false;
    }
}
