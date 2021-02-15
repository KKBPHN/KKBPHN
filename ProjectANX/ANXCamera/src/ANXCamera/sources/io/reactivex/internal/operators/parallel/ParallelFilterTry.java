package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelFilterTry extends ParallelFlowable {
    final BiFunction errorHandler;
    final Predicate predicate;
    final ParallelFlowable source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelFilterTry$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$reactivex$parallel$ParallelFailureHandling = new int[ParallelFailureHandling.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$io$reactivex$parallel$ParallelFailureHandling[ParallelFailureHandling.RETRY.ordinal()] = 1;
            $SwitchMap$io$reactivex$parallel$ParallelFailureHandling[ParallelFailureHandling.SKIP.ordinal()] = 2;
            try {
                $SwitchMap$io$reactivex$parallel$ParallelFailureHandling[ParallelFailureHandling.STOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    abstract class BaseFilterSubscriber implements ConditionalSubscriber, Subscription {
        boolean done;
        final BiFunction errorHandler;
        final Predicate predicate;
        Subscription s;

        BaseFilterSubscriber(Predicate predicate2, BiFunction biFunction) {
            this.predicate = predicate2;
            this.errorHandler = biFunction;
        }

        public final void cancel() {
            this.s.cancel();
        }

        public final void onNext(Object obj) {
            if (!tryOnNext(obj) && !this.done) {
                this.s.request(1);
            }
        }

        public final void request(long j) {
            this.s.request(j);
        }
    }

    final class ParallelFilterConditionalSubscriber extends BaseFilterSubscriber {
        final ConditionalSubscriber actual;

        ParallelFilterConditionalSubscriber(ConditionalSubscriber conditionalSubscriber, Predicate predicate, BiFunction biFunction) {
            super(predicate, biFunction);
            this.actual = conditionalSubscriber;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r2v1 */
        /* JADX WARNING: type inference failed for: r2v2, types: [long] */
        /* JADX WARNING: type inference failed for: r2v4, types: [long] */
        /* JADX WARNING: type inference failed for: r2v5, types: [boolean] */
        /* JADX WARNING: type inference failed for: r2v6 */
        /* JADX WARNING: type inference failed for: r2v7 */
        /* JADX WARNING: type inference failed for: r2v8 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v1
  assigns: []
  uses: []
  mth insns count: 47
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0040  */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(Object obj) {
            ? r2;
            int i;
            if (!this.done) {
                ? r22 = 0;
                do {
                    boolean z = true;
                    try {
                        r2 = r22;
                        ? test = this.predicate.test(obj);
                        r2 = test;
                        if (test == 0 || !this.actual.tryOnNext(obj)) {
                            z = false;
                        }
                        return z;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(new CompositeException(th, th));
                    }
                } while (i != 1);
                if (i != 2) {
                    cancel();
                    if (i != 3) {
                        onError(th);
                        return false;
                    }
                    onComplete();
                }
                return false;
            }
            return false;
        }
    }

    final class ParallelFilterSubscriber extends BaseFilterSubscriber {
        final Subscriber actual;

        ParallelFilterSubscriber(Subscriber subscriber, Predicate predicate, BiFunction biFunction) {
            super(predicate, biFunction);
            this.actual = subscriber;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r2v1 */
        /* JADX WARNING: type inference failed for: r2v2, types: [long] */
        /* JADX WARNING: type inference failed for: r2v4, types: [long] */
        /* JADX WARNING: type inference failed for: r2v5, types: [boolean] */
        /* JADX WARNING: type inference failed for: r2v6 */
        /* JADX WARNING: type inference failed for: r2v7 */
        /* JADX WARNING: type inference failed for: r2v8 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v1
  assigns: []
  uses: []
  mth insns count: 45
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x003c  */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(Object obj) {
            ? r2;
            int i;
            if (!this.done) {
                ? r22 = 0;
                do {
                    try {
                        r2 = r22;
                        ? test = this.predicate.test(obj);
                        r2 = test;
                        if (test == 0) {
                            return false;
                        }
                        this.actual.onNext(obj);
                        return true;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        cancel();
                        onError(new CompositeException(th, th));
                    }
                } while (i != 1);
                if (i != 2) {
                    cancel();
                    if (i != 3) {
                        onError(th);
                        return false;
                    }
                    onComplete();
                }
                return false;
            }
            return false;
        }
    }

    public ParallelFilterTry(ParallelFlowable parallelFlowable, Predicate predicate2, BiFunction biFunction) {
        this.source = parallelFlowable;
        this.predicate = predicate2;
        this.errorHandler = biFunction;
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            Subscriber[] subscriberArr2 = new Subscriber[length];
            for (int i = 0; i < length; i++) {
                ConditionalSubscriber conditionalSubscriber = subscriberArr[i];
                if (conditionalSubscriber instanceof ConditionalSubscriber) {
                    subscriberArr2[i] = new ParallelFilterConditionalSubscriber(conditionalSubscriber, this.predicate, this.errorHandler);
                } else {
                    subscriberArr2[i] = new ParallelFilterSubscriber(conditionalSubscriber, this.predicate, this.errorHandler);
                }
            }
            this.source.subscribe(subscriberArr2);
        }
    }
}
