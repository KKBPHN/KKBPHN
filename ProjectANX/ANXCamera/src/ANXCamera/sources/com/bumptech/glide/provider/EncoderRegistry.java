package com.bumptech.glide.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.List;

public class EncoderRegistry {
    private final List encoders = new ArrayList();

    final class Entry {
        private final Class dataClass;
        final Encoder encoder;

        Entry(@NonNull Class cls, @NonNull Encoder encoder2) {
            this.dataClass = cls;
            this.encoder = encoder2;
        }

        /* access modifiers changed from: 0000 */
        public boolean handles(@NonNull Class cls) {
            return this.dataClass.isAssignableFrom(cls);
        }
    }

    public synchronized void append(@NonNull Class cls, @NonNull Encoder encoder) {
        this.encoders.add(new Entry(cls, encoder));
    }

    @Nullable
    public synchronized Encoder getEncoder(@NonNull Class cls) {
        for (Entry entry : this.encoders) {
            if (entry.handles(cls)) {
                return entry.encoder;
            }
        }
        return null;
    }

    public synchronized void prepend(@NonNull Class cls, @NonNull Encoder encoder) {
        this.encoders.add(0, new Entry(cls, encoder));
    }
}
