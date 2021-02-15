package io.reactivex.internal.operators.observable;

import io.reactivex.Emitter;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.single.SingleToObservable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public final class ObservableInternalHelper {

    final class BufferedReplayCallable implements Callable {
        private final int bufferSize;
        private final Observable parent;

        BufferedReplayCallable(Observable observable, int i) {
            this.parent = observable;
            this.bufferSize = i;
        }

        public ConnectableObservable call() {
            return this.parent.replay(this.bufferSize);
        }
    }

    final class BufferedTimedReplayCallable implements Callable {
        private final int bufferSize;
        private final Observable parent;
        private final Scheduler scheduler;
        private final long time;
        private final TimeUnit unit;

        BufferedTimedReplayCallable(Observable observable, int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.parent = observable;
            this.bufferSize = i;
            this.time = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public ConnectableObservable call() {
            return this.parent.replay(this.bufferSize, this.time, this.unit, this.scheduler);
        }
    }

    enum ErrorMapperFilter implements Function, Predicate {
        INSTANCE;

        public Throwable apply(Notification notification) {
            return notification.getError();
        }

        public boolean test(Notification notification) {
            return notification.isOnError();
        }
    }

    final class FlatMapIntoIterable implements Function {
        private final Function mapper;

        FlatMapIntoIterable(Function function) {
            this.mapper = function;
        }

        public ObservableSource apply(Object obj) {
            Object apply = this.mapper.apply(obj);
            ObjectHelper.requireNonNull(apply, "The mapper returned a null Iterable");
            return new ObservableFromIterable((Iterable) apply);
        }
    }

    final class FlatMapWithCombinerInner implements Function {
        private final BiFunction combiner;
        private final Object t;

        FlatMapWithCombinerInner(BiFunction biFunction, Object obj) {
            this.combiner = biFunction;
            this.t = obj;
        }

        public Object apply(Object obj) {
            return this.combiner.apply(this.t, obj);
        }
    }

    final class FlatMapWithCombinerOuter implements Function {
        private final BiFunction combiner;
        private final Function mapper;

        FlatMapWithCombinerOuter(BiFunction biFunction, Function function) {
            this.combiner = biFunction;
            this.mapper = function;
        }

        public ObservableSource apply(Object obj) {
            Object apply = this.mapper.apply(obj);
            ObjectHelper.requireNonNull(apply, "The mapper returned a null ObservableSource");
            return new ObservableMap((ObservableSource) apply, new FlatMapWithCombinerInner(this.combiner, obj));
        }
    }

    final class ItemDelayFunction implements Function {
        final Function itemDelay;

        ItemDelayFunction(Function function) {
            this.itemDelay = function;
        }

        public ObservableSource apply(Object obj) {
            Object apply = this.itemDelay.apply(obj);
            ObjectHelper.requireNonNull(apply, "The itemDelay returned a null ObservableSource");
            return new ObservableTake((ObservableSource) apply, 1).map(Functions.justFunction(obj)).defaultIfEmpty(obj);
        }
    }

    enum MapToInt implements Function {
        INSTANCE;

        public Object apply(Object obj) {
            return Integer.valueOf(0);
        }
    }

    final class ObservableMapper implements Function {
        final Function mapper;

        ObservableMapper(Function function) {
            this.mapper = function;
        }

        public Observable apply(Object obj) {
            Object apply = this.mapper.apply(obj);
            ObjectHelper.requireNonNull(apply, "The mapper returned a null SingleSource");
            return RxJavaPlugins.onAssembly((Observable) new SingleToObservable((SingleSource) apply));
        }
    }

    final class ObserverOnComplete implements Action {
        final Observer observer;

        ObserverOnComplete(Observer observer2) {
            this.observer = observer2;
        }

        public void run() {
            this.observer.onComplete();
        }
    }

    final class ObserverOnError implements Consumer {
        final Observer observer;

        ObserverOnError(Observer observer2) {
            this.observer = observer2;
        }

        public void accept(Throwable th) {
            this.observer.onError(th);
        }
    }

    final class ObserverOnNext implements Consumer {
        final Observer observer;

        ObserverOnNext(Observer observer2) {
            this.observer = observer2;
        }

        public void accept(Object obj) {
            this.observer.onNext(obj);
        }
    }

    final class RepeatWhenOuterHandler implements Function {
        private final Function handler;

        RepeatWhenOuterHandler(Function function) {
            this.handler = function;
        }

        public ObservableSource apply(Observable observable) {
            return (ObservableSource) this.handler.apply(observable.map(MapToInt.INSTANCE));
        }
    }

    final class ReplayCallable implements Callable {
        private final Observable parent;

        ReplayCallable(Observable observable) {
            this.parent = observable;
        }

        public ConnectableObservable call() {
            return this.parent.replay();
        }
    }

    final class ReplayFunction implements Function {
        private final Scheduler scheduler;
        private final Function selector;

        ReplayFunction(Function function, Scheduler scheduler2) {
            this.selector = function;
            this.scheduler = scheduler2;
        }

        public ObservableSource apply(Observable observable) {
            Object apply = this.selector.apply(observable);
            ObjectHelper.requireNonNull(apply, "The selector returned a null ObservableSource");
            return Observable.wrap((ObservableSource) apply).observeOn(this.scheduler);
        }
    }

    final class RetryWhenInner implements Function {
        private final Function handler;

        RetryWhenInner(Function function) {
            this.handler = function;
        }

        public ObservableSource apply(Observable observable) {
            return (ObservableSource) this.handler.apply(observable.takeWhile(ErrorMapperFilter.INSTANCE).map(ErrorMapperFilter.INSTANCE));
        }
    }

    final class SimpleBiGenerator implements BiFunction {
        final BiConsumer consumer;

        SimpleBiGenerator(BiConsumer biConsumer) {
            this.consumer = biConsumer;
        }

        public Object apply(Object obj, Emitter emitter) {
            this.consumer.accept(obj, emitter);
            return obj;
        }
    }

    final class SimpleGenerator implements BiFunction {
        final Consumer consumer;

        SimpleGenerator(Consumer consumer2) {
            this.consumer = consumer2;
        }

        public Object apply(Object obj, Emitter emitter) {
            this.consumer.accept(emitter);
            return obj;
        }
    }

    final class TimedReplayCallable implements Callable {
        private final Observable parent;
        private final Scheduler scheduler;
        private final long time;
        private final TimeUnit unit;

        TimedReplayCallable(Observable observable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.parent = observable;
            this.time = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public ConnectableObservable call() {
            return this.parent.replay(this.time, this.unit, this.scheduler);
        }
    }

    final class ZipIterableFunction implements Function {
        private final Function zipper;

        ZipIterableFunction(Function function) {
            this.zipper = function;
        }

        public ObservableSource apply(List list) {
            return Observable.zipIterable(list, this.zipper, false, Observable.bufferSize());
        }
    }

    private ObservableInternalHelper() {
        throw new IllegalStateException("No instances!");
    }

    private static Function convertSingleMapperToObservableMapper(Function function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return new ObservableMapper(function);
    }

    public static Function flatMapIntoIterable(Function function) {
        return new FlatMapIntoIterable(function);
    }

    public static Function flatMapWithCombiner(Function function, BiFunction biFunction) {
        return new FlatMapWithCombinerOuter(biFunction, function);
    }

    public static Function itemDelay(Function function) {
        return new ItemDelayFunction(function);
    }

    public static Action observerOnComplete(Observer observer) {
        return new ObserverOnComplete(observer);
    }

    public static Consumer observerOnError(Observer observer) {
        return new ObserverOnError(observer);
    }

    public static Consumer observerOnNext(Observer observer) {
        return new ObserverOnNext(observer);
    }

    public static Function repeatWhenHandler(Function function) {
        return new RepeatWhenOuterHandler(function);
    }

    public static Callable replayCallable(Observable observable) {
        return new ReplayCallable(observable);
    }

    public static Callable replayCallable(Observable observable, int i) {
        return new BufferedReplayCallable(observable, i);
    }

    public static Callable replayCallable(Observable observable, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        BufferedTimedReplayCallable bufferedTimedReplayCallable = new BufferedTimedReplayCallable(observable, i, j, timeUnit, scheduler);
        return bufferedTimedReplayCallable;
    }

    public static Callable replayCallable(Observable observable, long j, TimeUnit timeUnit, Scheduler scheduler) {
        TimedReplayCallable timedReplayCallable = new TimedReplayCallable(observable, j, timeUnit, scheduler);
        return timedReplayCallable;
    }

    public static Function replayFunction(Function function, Scheduler scheduler) {
        return new ReplayFunction(function, scheduler);
    }

    public static Function retryWhenHandler(Function function) {
        return new RetryWhenInner(function);
    }

    public static BiFunction simpleBiGenerator(BiConsumer biConsumer) {
        return new SimpleBiGenerator(biConsumer);
    }

    public static BiFunction simpleGenerator(Consumer consumer) {
        return new SimpleGenerator(consumer);
    }

    public static Observable switchMapSingle(Observable observable, Function function) {
        return observable.switchMap(convertSingleMapperToObservableMapper(function), 1);
    }

    public static Observable switchMapSingleDelayError(Observable observable, Function function) {
        return observable.switchMapDelayError(convertSingleMapperToObservableMapper(function), 1);
    }

    public static Function zipIterable(Function function) {
        return new ZipIterableFunction(function);
    }
}
