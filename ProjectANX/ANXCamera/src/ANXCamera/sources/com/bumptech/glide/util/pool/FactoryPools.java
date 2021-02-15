package com.bumptech.glide.util.pool;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.util.Pools.Pool;
import androidx.core.util.Pools.SimplePool;
import androidx.core.util.Pools.SynchronizedPool;
import java.util.ArrayList;
import java.util.List;

public final class FactoryPools {
    private static final int DEFAULT_POOL_SIZE = 20;
    private static final Resetter EMPTY_RESETTER = new Resetter() {
        public void reset(@NonNull Object obj) {
        }
    };
    private static final String TAG = "FactoryPools";

    public interface Factory {
        Object create();
    }

    final class FactoryPool implements Pool {
        private final Factory factory;
        private final Pool pool;
        private final Resetter resetter;

        FactoryPool(@NonNull Pool pool2, @NonNull Factory factory2, @NonNull Resetter resetter2) {
            this.pool = pool2;
            this.factory = factory2;
            this.resetter = resetter2;
        }

        public Object acquire() {
            Object acquire = this.pool.acquire();
            if (acquire == null) {
                acquire = this.factory.create();
                String str = FactoryPools.TAG;
                if (Log.isLoggable(str, 2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Created new ");
                    sb.append(acquire.getClass());
                    Log.v(str, sb.toString());
                }
            }
            if (acquire instanceof Poolable) {
                ((Poolable) acquire).getVerifier().setRecycled(false);
            }
            return acquire;
        }

        public boolean release(@NonNull Object obj) {
            if (obj instanceof Poolable) {
                ((Poolable) obj).getVerifier().setRecycled(true);
            }
            this.resetter.reset(obj);
            return this.pool.release(obj);
        }
    }

    public interface Poolable {
        @NonNull
        StateVerifier getVerifier();
    }

    public interface Resetter {
        void reset(@NonNull Object obj);
    }

    private FactoryPools() {
    }

    @NonNull
    private static Pool build(@NonNull Pool pool, @NonNull Factory factory) {
        return build(pool, factory, emptyResetter());
    }

    @NonNull
    private static Pool build(@NonNull Pool pool, @NonNull Factory factory, @NonNull Resetter resetter) {
        return new FactoryPool(pool, factory, resetter);
    }

    @NonNull
    private static Resetter emptyResetter() {
        return EMPTY_RESETTER;
    }

    @NonNull
    public static Pool simple(int i, @NonNull Factory factory) {
        return build(new SimplePool(i), factory);
    }

    @NonNull
    public static Pool threadSafe(int i, @NonNull Factory factory) {
        return build(new SynchronizedPool(i), factory);
    }

    @NonNull
    public static Pool threadSafeList() {
        return threadSafeList(20);
    }

    @NonNull
    public static Pool threadSafeList(int i) {
        return build(new SynchronizedPool(i), new Factory() {
            @NonNull
            public List create() {
                return new ArrayList();
            }
        }, new Resetter() {
            public void reset(@NonNull List list) {
                list.clear();
            }
        });
    }
}
