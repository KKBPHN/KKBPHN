package com.bumptech.glide.util.pool;

import androidx.annotation.NonNull;

public abstract class StateVerifier {
    private static final boolean DEBUG = false;

    class DebugStateVerifier extends StateVerifier {
        private volatile RuntimeException recycledAtStackTraceException;

        DebugStateVerifier() {
            super();
        }

        /* access modifiers changed from: 0000 */
        public void setRecycled(boolean z) {
            this.recycledAtStackTraceException = z ? new RuntimeException("Released") : null;
        }

        public void throwIfRecycled() {
            if (this.recycledAtStackTraceException != null) {
                throw new IllegalStateException("Already released", this.recycledAtStackTraceException);
            }
        }
    }

    class DefaultStateVerifier extends StateVerifier {
        private volatile boolean isReleased;

        DefaultStateVerifier() {
            super();
        }

        public void setRecycled(boolean z) {
            this.isReleased = z;
        }

        public void throwIfRecycled() {
            if (this.isReleased) {
                throw new IllegalStateException("Already released");
            }
        }
    }

    private StateVerifier() {
    }

    @NonNull
    public static StateVerifier newInstance() {
        return new DefaultStateVerifier();
    }

    public abstract void setRecycled(boolean z);

    public abstract void throwIfRecycled();
}
