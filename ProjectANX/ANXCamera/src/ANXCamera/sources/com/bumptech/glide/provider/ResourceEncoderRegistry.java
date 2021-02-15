package com.bumptech.glide.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry {
    private final List encoders = new ArrayList();

    final class Entry {
        final ResourceEncoder encoder;
        private final Class resourceClass;

        Entry(@NonNull Class cls, @NonNull ResourceEncoder resourceEncoder) {
            this.resourceClass = cls;
            this.encoder = resourceEncoder;
        }

        /* access modifiers changed from: 0000 */
        public boolean handles(@NonNull Class cls) {
            return this.resourceClass.isAssignableFrom(cls);
        }
    }

    public synchronized void append(@NonNull Class cls, @NonNull ResourceEncoder resourceEncoder) {
        this.encoders.add(new Entry(cls, resourceEncoder));
    }

    @Nullable
    public synchronized ResourceEncoder get(@NonNull Class cls) {
        int size = this.encoders.size();
        for (int i = 0; i < size; i++) {
            Entry entry = (Entry) this.encoders.get(i);
            if (entry.handles(cls)) {
                return entry.encoder;
            }
        }
        return null;
    }

    public synchronized void prepend(@NonNull Class cls, @NonNull ResourceEncoder resourceEncoder) {
        this.encoders.add(0, new Entry(cls, resourceEncoder));
    }
}
