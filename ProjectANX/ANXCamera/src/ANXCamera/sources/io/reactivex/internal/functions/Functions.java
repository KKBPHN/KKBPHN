package io.reactivex.internal.functions;

import io.reactivex.Notification;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
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
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscription;

public final class Functions {
    static final Predicate ALWAYS_FALSE = new FalsePredicate();
    static final Predicate ALWAYS_TRUE = new TruePredicate();
    public static final Action EMPTY_ACTION = new EmptyAction();
    static final Consumer EMPTY_CONSUMER = new EmptyConsumer();
    public static final LongConsumer EMPTY_LONG_CONSUMER = new EmptyLongConsumer();
    public static final Runnable EMPTY_RUNNABLE = new EmptyRunnable();
    public static final Consumer ERROR_CONSUMER = new ErrorConsumer();
    static final Function IDENTITY = new Identity();
    static final Comparator NATURAL_COMPARATOR = new NaturalObjectComparator();
    static final Callable NULL_SUPPLIER = new NullCallable();
    public static final Consumer ON_ERROR_MISSING = new OnErrorMissingConsumer();
    public static final Consumer REQUEST_MAX = new MaxRequestSubscription();

    final class ActionConsumer implements Consumer {
        final Action action;

        ActionConsumer(Action action2) {
            this.action = action2;
        }

        public void accept(Object obj) {
            this.action.run();
        }
    }

    final class Array2Func implements Function {
        final BiFunction f;

        Array2Func(BiFunction biFunction) {
            this.f = biFunction;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 2) {
                return this.f.apply(objArr[0], objArr[1]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 2 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class Array3Func implements Function {
        final Function3 f;

        Array3Func(Function3 function3) {
            this.f = function3;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 3) {
                return this.f.apply(objArr[0], objArr[1], objArr[2]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 3 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class Array4Func implements Function {
        final Function4 f;

        Array4Func(Function4 function4) {
            this.f = function4;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 4) {
                return this.f.apply(objArr[0], objArr[1], objArr[2], objArr[3]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 4 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class Array5Func implements Function {
        private final Function5 f;

        Array5Func(Function5 function5) {
            this.f = function5;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 5) {
                return this.f.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 5 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class Array6Func implements Function {
        final Function6 f;

        Array6Func(Function6 function6) {
            this.f = function6;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 6) {
                return this.f.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 6 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class Array7Func implements Function {
        final Function7 f;

        Array7Func(Function7 function7) {
            this.f = function7;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 7) {
                return this.f.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 7 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class Array8Func implements Function {
        final Function8 f;

        Array8Func(Function8 function8) {
            this.f = function8;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 8) {
                return this.f.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6], objArr[7]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 8 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class Array9Func implements Function {
        final Function9 f;

        Array9Func(Function9 function9) {
            this.f = function9;
        }

        public Object apply(Object[] objArr) {
            if (objArr.length == 9) {
                return this.f.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6], objArr[7], objArr[8]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 9 expected but got ");
            sb.append(objArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    final class ArrayListCapacityCallable implements Callable {
        final int capacity;

        ArrayListCapacityCallable(int i) {
            this.capacity = i;
        }

        public List call() {
            return new ArrayList(this.capacity);
        }
    }

    final class BooleanSupplierPredicateReverse implements Predicate {
        final BooleanSupplier supplier;

        BooleanSupplierPredicateReverse(BooleanSupplier booleanSupplier) {
            this.supplier = booleanSupplier;
        }

        public boolean test(Object obj) {
            return !this.supplier.getAsBoolean();
        }
    }

    final class CastToClass implements Function {
        final Class clazz;

        CastToClass(Class cls) {
            this.clazz = cls;
        }

        public Object apply(Object obj) {
            return this.clazz.cast(obj);
        }
    }

    final class ClassFilter implements Predicate {
        final Class clazz;

        ClassFilter(Class cls) {
            this.clazz = cls;
        }

        public boolean test(Object obj) {
            return this.clazz.isInstance(obj);
        }
    }

    final class EmptyAction implements Action {
        EmptyAction() {
        }

        public void run() {
        }

        public String toString() {
            return "EmptyAction";
        }
    }

    final class EmptyConsumer implements Consumer {
        EmptyConsumer() {
        }

        public void accept(Object obj) {
        }

        public String toString() {
            return "EmptyConsumer";
        }
    }

    final class EmptyLongConsumer implements LongConsumer {
        EmptyLongConsumer() {
        }

        public void accept(long j) {
        }
    }

    final class EmptyRunnable implements Runnable {
        EmptyRunnable() {
        }

        public void run() {
        }

        public String toString() {
            return "EmptyRunnable";
        }
    }

    final class EqualsPredicate implements Predicate {
        final Object value;

        EqualsPredicate(Object obj) {
            this.value = obj;
        }

        public boolean test(Object obj) {
            return ObjectHelper.equals(obj, this.value);
        }
    }

    final class ErrorConsumer implements Consumer {
        ErrorConsumer() {
        }

        public void accept(Throwable th) {
            RxJavaPlugins.onError(th);
        }
    }

    final class FalsePredicate implements Predicate {
        FalsePredicate() {
        }

        public boolean test(Object obj) {
            return false;
        }
    }

    final class FutureAction implements Action {
        final Future future;

        FutureAction(Future future2) {
            this.future = future2;
        }

        public void run() {
            this.future.get();
        }
    }

    enum HashSetCallable implements Callable {
        INSTANCE;

        public Set call() {
            return new HashSet();
        }
    }

    final class Identity implements Function {
        Identity() {
        }

        public Object apply(Object obj) {
            return obj;
        }

        public String toString() {
            return "IdentityFunction";
        }
    }

    final class JustValue implements Callable, Function {
        final Object value;

        JustValue(Object obj) {
            this.value = obj;
        }

        public Object apply(Object obj) {
            return this.value;
        }

        public Object call() {
            return this.value;
        }
    }

    final class ListSorter implements Function {
        final Comparator comparator;

        ListSorter(Comparator comparator2) {
            this.comparator = comparator2;
        }

        public List apply(List list) {
            Collections.sort(list, this.comparator);
            return list;
        }
    }

    final class MaxRequestSubscription implements Consumer {
        MaxRequestSubscription() {
        }

        public void accept(Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }
    }

    enum NaturalComparator implements Comparator {
        INSTANCE;

        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    }

    final class NaturalObjectComparator implements Comparator {
        NaturalObjectComparator() {
        }

        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    }

    final class NotificationOnComplete implements Action {
        final Consumer onNotification;

        NotificationOnComplete(Consumer consumer) {
            this.onNotification = consumer;
        }

        public void run() {
            this.onNotification.accept(Notification.createOnComplete());
        }
    }

    final class NotificationOnError implements Consumer {
        final Consumer onNotification;

        NotificationOnError(Consumer consumer) {
            this.onNotification = consumer;
        }

        public void accept(Throwable th) {
            this.onNotification.accept(Notification.createOnError(th));
        }
    }

    final class NotificationOnNext implements Consumer {
        final Consumer onNotification;

        NotificationOnNext(Consumer consumer) {
            this.onNotification = consumer;
        }

        public void accept(Object obj) {
            this.onNotification.accept(Notification.createOnNext(obj));
        }
    }

    final class NullCallable implements Callable {
        NullCallable() {
        }

        public Object call() {
            return null;
        }
    }

    final class OnErrorMissingConsumer implements Consumer {
        OnErrorMissingConsumer() {
        }

        public void accept(Throwable th) {
            RxJavaPlugins.onError(new OnErrorNotImplementedException(th));
        }
    }

    final class TimestampFunction implements Function {
        final Scheduler scheduler;
        final TimeUnit unit;

        TimestampFunction(TimeUnit timeUnit, Scheduler scheduler2) {
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public Timed apply(Object obj) {
            return new Timed(obj, this.scheduler.now(this.unit), this.unit);
        }
    }

    final class ToMapKeySelector implements BiConsumer {
        private final Function keySelector;

        ToMapKeySelector(Function function) {
            this.keySelector = function;
        }

        public void accept(Map map, Object obj) {
            map.put(this.keySelector.apply(obj), obj);
        }
    }

    final class ToMapKeyValueSelector implements BiConsumer {
        private final Function keySelector;
        private final Function valueSelector;

        ToMapKeyValueSelector(Function function, Function function2) {
            this.valueSelector = function;
            this.keySelector = function2;
        }

        public void accept(Map map, Object obj) {
            map.put(this.keySelector.apply(obj), this.valueSelector.apply(obj));
        }
    }

    final class ToMultimapKeyValueSelector implements BiConsumer {
        private final Function collectionFactory;
        private final Function keySelector;
        private final Function valueSelector;

        ToMultimapKeyValueSelector(Function function, Function function2, Function function3) {
            this.collectionFactory = function;
            this.valueSelector = function2;
            this.keySelector = function3;
        }

        public void accept(Map map, Object obj) {
            Object apply = this.keySelector.apply(obj);
            Collection collection = (Collection) map.get(apply);
            if (collection == null) {
                collection = (Collection) this.collectionFactory.apply(apply);
                map.put(apply, collection);
            }
            collection.add(this.valueSelector.apply(obj));
        }
    }

    final class TruePredicate implements Predicate {
        TruePredicate() {
        }

        public boolean test(Object obj) {
            return true;
        }
    }

    private Functions() {
        throw new IllegalStateException("No instances!");
    }

    public static Consumer actionConsumer(Action action) {
        return new ActionConsumer(action);
    }

    public static Predicate alwaysFalse() {
        return ALWAYS_FALSE;
    }

    public static Predicate alwaysTrue() {
        return ALWAYS_TRUE;
    }

    public static Function castFunction(Class cls) {
        return new CastToClass(cls);
    }

    public static Callable createArrayList(int i) {
        return new ArrayListCapacityCallable(i);
    }

    public static Callable createHashSet() {
        return HashSetCallable.INSTANCE;
    }

    public static Consumer emptyConsumer() {
        return EMPTY_CONSUMER;
    }

    public static Predicate equalsWith(Object obj) {
        return new EqualsPredicate(obj);
    }

    public static Action futureAction(Future future) {
        return new FutureAction(future);
    }

    public static Function identity() {
        return IDENTITY;
    }

    public static Predicate isInstanceOf(Class cls) {
        return new ClassFilter(cls);
    }

    public static Callable justCallable(Object obj) {
        return new JustValue(obj);
    }

    public static Function justFunction(Object obj) {
        return new JustValue(obj);
    }

    public static Function listSorter(Comparator comparator) {
        return new ListSorter(comparator);
    }

    public static Comparator naturalComparator() {
        return NaturalComparator.INSTANCE;
    }

    public static Comparator naturalOrder() {
        return NATURAL_COMPARATOR;
    }

    public static Action notificationOnComplete(Consumer consumer) {
        return new NotificationOnComplete(consumer);
    }

    public static Consumer notificationOnError(Consumer consumer) {
        return new NotificationOnError(consumer);
    }

    public static Consumer notificationOnNext(Consumer consumer) {
        return new NotificationOnNext(consumer);
    }

    public static Callable nullSupplier() {
        return NULL_SUPPLIER;
    }

    public static Predicate predicateReverseFor(BooleanSupplier booleanSupplier) {
        return new BooleanSupplierPredicateReverse(booleanSupplier);
    }

    public static Function timestampWith(TimeUnit timeUnit, Scheduler scheduler) {
        return new TimestampFunction(timeUnit, scheduler);
    }

    public static Function toFunction(BiFunction biFunction) {
        ObjectHelper.requireNonNull(biFunction, "f is null");
        return new Array2Func(biFunction);
    }

    public static Function toFunction(Function3 function3) {
        ObjectHelper.requireNonNull(function3, "f is null");
        return new Array3Func(function3);
    }

    public static Function toFunction(Function4 function4) {
        ObjectHelper.requireNonNull(function4, "f is null");
        return new Array4Func(function4);
    }

    public static Function toFunction(Function5 function5) {
        ObjectHelper.requireNonNull(function5, "f is null");
        return new Array5Func(function5);
    }

    public static Function toFunction(Function6 function6) {
        ObjectHelper.requireNonNull(function6, "f is null");
        return new Array6Func(function6);
    }

    public static Function toFunction(Function7 function7) {
        ObjectHelper.requireNonNull(function7, "f is null");
        return new Array7Func(function7);
    }

    public static Function toFunction(Function8 function8) {
        ObjectHelper.requireNonNull(function8, "f is null");
        return new Array8Func(function8);
    }

    public static Function toFunction(Function9 function9) {
        ObjectHelper.requireNonNull(function9, "f is null");
        return new Array9Func(function9);
    }

    public static BiConsumer toMapKeySelector(Function function) {
        return new ToMapKeySelector(function);
    }

    public static BiConsumer toMapKeyValueSelector(Function function, Function function2) {
        return new ToMapKeyValueSelector(function2, function);
    }

    public static BiConsumer toMultimapKeyValueSelector(Function function, Function function2, Function function3) {
        return new ToMultimapKeyValueSelector(function3, function2, function);
    }
}
