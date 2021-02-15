package io.reactivex.internal.operators.flowable;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableInternalHelper {

    final class BufferedReplayCallable implements Callable {
        private final int bufferSize;
        private final Flowable parent;

        BufferedReplayCallable(Flowable flowable, int i) {
            this.parent = flowable;
            this.bufferSize = i;
        }

        public ConnectableFlowable call() {
            return this.parent.replay(this.bufferSize);
        }
    }

    final class BufferedTimedReplay implements Callable {
        private final int bufferSize;
        private final Flowable parent;
        private final Scheduler scheduler;
        private final long time;
        private final TimeUnit unit;

        BufferedTimedReplay(Flowable flowable, int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.parent = flowable;
            this.bufferSize = i;
            this.time = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public ConnectableFlowable call() {
            return this.parent.replay(this.bufferSize, this.time, this.unit, this.scheduler);
        }
    }

    final class FlatMapIntoIterable implements Function {
        private final Function mapper;

        FlatMapIntoIterable(Function function) {
            this.mapper = function;
        }

        public Publisher apply(Object obj) {
            Object apply = this.mapper.apply(obj);
            ObjectHelper.requireNonNull(apply, "The mapper returned a null Iterable");
            return new FlowableFromIterable((Iterable) apply);
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

        public Publisher apply(Object obj) {
            Object apply = this.mapper.apply(obj);
            ObjectHelper.requireNonNull(apply, "The mapper returned a null Publisher");
            return new FlowableMapPublisher((Publisher) apply, new FlatMapWithCombinerInner(this.combiner, obj));
        }
    }

    final class ItemDelayFunction implements Function {
        final Function itemDelay;

        ItemDelayFunction(Function function) {
            this.itemDelay = function;
        }

        public Publisher apply(Object obj) {
            Object apply = this.itemDelay.apply(obj);
            ObjectHelper.requireNonNull(apply, "The itemDelay returned a null Publisher");
            return new FlowableTakePublisher((Publisher) apply, 1).map(Functions.justFunction(obj)).defaultIfEmpty(obj);
        }
    }

    final class ReplayCallable implements Callable {
        private final Flowable parent;

        ReplayCallable(Flowable flowable) {
            this.parent = flowable;
        }

        public ConnectableFlowable call() {
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

        public Publisher apply(Flowable flowable) {
            Object apply = this.selector.apply(flowable);
            ObjectHelper.requireNonNull(apply, "The selector returned a null Publisher");
            return Flowable.fromPublisher((Publisher) apply).observeOn(this.scheduler);
        }
    }

    public enum RequestMax implements Consumer {
        INSTANCE;

        public void accept(Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
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

    final class SubscriberOnComplete implements Action {
        final Subscriber subscriber;

        SubscriberOnComplete(Subscriber subscriber2) {
            this.subscriber = subscriber2;
        }

        public void run() {
            this.subscriber.onComplete();
        }
    }

    final class SubscriberOnError implements Consumer {
        final Subscriber subscriber;

        SubscriberOnError(Subscriber subscriber2) {
            this.subscriber = subscriber2;
        }

        public void accept(Throwable th) {
            this.subscriber.onError(th);
        }
    }

    final class SubscriberOnNext implements Consumer {
        final Subscriber subscriber;

        SubscriberOnNext(Subscriber subscriber2) {
            this.subscriber = subscriber2;
        }

        public void accept(Object obj) {
            this.subscriber.onNext(obj);
        }
    }

    final class TimedReplay implements Callable {
        private final Flowable parent;
        private final Scheduler scheduler;
        private final long time;
        private final TimeUnit unit;

        TimedReplay(Flowable flowable, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.parent = flowable;
            this.time = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public ConnectableFlowable call() {
            return this.parent.replay(this.time, this.unit, this.scheduler);
        }
    }

    final class ZipIterableFunction implements Function {
        private final Function zipper;

        ZipIterableFunction(Function function) {
            this.zipper = function;
        }

        public Publisher apply(List list) {
            return Flowable.zipIterable(list, this.zipper, false, Flowable.bufferSize());
        }
    }

    private FlowableInternalHelper() {
        throw new IllegalStateException("No instances!");
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

    public static Callable replayCallable(Flowable flowable) {
        return new ReplayCallable(flowable);
    }

    public static Callable replayCallable(Flowable flowable, int i) {
        return new BufferedReplayCallable(flowable, i);
    }

    public static Callable replayCallable(Flowable flowable, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        BufferedTimedReplay bufferedTimedReplay = new BufferedTimedReplay(flowable, i, j, timeUnit, scheduler);
        return bufferedTimedReplay;
    }

    public static Callable replayCallable(Flowable flowable, long j, TimeUnit timeUnit, Scheduler scheduler) {
        TimedReplay timedReplay = new TimedReplay(flowable, j, timeUnit, scheduler);
        return timedReplay;
    }

    public static Function replayFunction(Function function, Scheduler scheduler) {
        return new ReplayFunction(function, scheduler);
    }

    public static BiFunction simpleBiGenerator(BiConsumer biConsumer) {
        return new SimpleBiGenerator(biConsumer);
    }

    public static BiFunction simpleGenerator(Consumer consumer) {
        return new SimpleGenerator(consumer);
    }

    public static Action subscriberOnComplete(Subscriber subscriber) {
        return new SubscriberOnComplete(subscriber);
    }

    public static Consumer subscriberOnError(Subscriber subscriber) {
        return new SubscriberOnError(subscriber);
    }

    public static Consumer subscriberOnNext(Subscriber subscriber) {
        return new SubscriberOnNext(subscriber);
    }

    public static Function zipIterable(Function function) {
        return new ZipIterableFunction(function);
    }
}
