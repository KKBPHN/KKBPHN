package com.bumptech.glide.load.data;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.data.DataRewinder.Factory;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataRewinderRegistry {
    private static final Factory DEFAULT_FACTORY = new Factory() {
        @NonNull
        public DataRewinder build(@NonNull Object obj) {
            return new DefaultRewinder(obj);
        }

        @NonNull
        public Class getDataClass() {
            throw new UnsupportedOperationException("Not implemented");
        }
    };
    private final Map rewinders = new HashMap();

    final class DefaultRewinder implements DataRewinder {
        private final Object data;

        DefaultRewinder(@NonNull Object obj) {
            this.data = obj;
        }

        public void cleanup() {
        }

        @NonNull
        public Object rewindAndGet() {
            return this.data;
        }
    }

    @NonNull
    public synchronized DataRewinder build(@NonNull Object obj) {
        Factory factory;
        Preconditions.checkNotNull(obj);
        factory = (Factory) this.rewinders.get(obj.getClass());
        if (factory == null) {
            Iterator it = this.rewinders.values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Factory factory2 = (Factory) it.next();
                if (factory2.getDataClass().isAssignableFrom(obj.getClass())) {
                    factory = factory2;
                    break;
                }
            }
        }
        if (factory == null) {
            factory = DEFAULT_FACTORY;
        }
        return factory.build(obj);
    }

    public synchronized void register(@NonNull Factory factory) {
        this.rewinders.put(factory.getDataClass(), factory);
    }
}
