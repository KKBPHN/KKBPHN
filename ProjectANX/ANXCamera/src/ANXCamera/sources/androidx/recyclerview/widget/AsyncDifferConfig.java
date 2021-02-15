package androidx.recyclerview.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class AsyncDifferConfig {
    @NonNull
    private final Executor mBackgroundThreadExecutor;
    @NonNull
    private final ItemCallback mDiffCallback;
    @Nullable
    private final Executor mMainThreadExecutor;

    public final class Builder {
        private static Executor sDiffExecutor = null;
        private static final Object sExecutorLock = new Object();
        private Executor mBackgroundThreadExecutor;
        private final ItemCallback mDiffCallback;
        @Nullable
        private Executor mMainThreadExecutor;

        public Builder(@NonNull ItemCallback itemCallback) {
            this.mDiffCallback = itemCallback;
        }

        @NonNull
        public AsyncDifferConfig build() {
            if (this.mBackgroundThreadExecutor == null) {
                synchronized (sExecutorLock) {
                    if (sDiffExecutor == null) {
                        sDiffExecutor = Executors.newFixedThreadPool(2);
                    }
                }
                this.mBackgroundThreadExecutor = sDiffExecutor;
            }
            return new AsyncDifferConfig(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback);
        }

        @NonNull
        public Builder setBackgroundThreadExecutor(Executor executor) {
            this.mBackgroundThreadExecutor = executor;
            return this;
        }

        @NonNull
        @RestrictTo({Scope.LIBRARY})
        public Builder setMainThreadExecutor(Executor executor) {
            this.mMainThreadExecutor = executor;
            return this;
        }
    }

    AsyncDifferConfig(@Nullable Executor executor, @NonNull Executor executor2, @NonNull ItemCallback itemCallback) {
        this.mMainThreadExecutor = executor;
        this.mBackgroundThreadExecutor = executor2;
        this.mDiffCallback = itemCallback;
    }

    @NonNull
    public Executor getBackgroundThreadExecutor() {
        return this.mBackgroundThreadExecutor;
    }

    @NonNull
    public ItemCallback getDiffCallback() {
        return this.mDiffCallback;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY})
    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }
}
