package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class DrawableCrossFadeFactory implements TransitionFactory {
    private final int duration;
    private final boolean isCrossFadeEnabled;
    private DrawableCrossFadeTransition resourceTransition;

    public class Builder {
        private static final int DEFAULT_DURATION_MS = 300;
        private final int durationMillis;
        private boolean isCrossFadeEnabled;

        public Builder() {
            this(300);
        }

        public Builder(int i) {
            this.durationMillis = i;
        }

        public DrawableCrossFadeFactory build() {
            return new DrawableCrossFadeFactory(this.durationMillis, this.isCrossFadeEnabled);
        }

        public Builder setCrossFadeEnabled(boolean z) {
            this.isCrossFadeEnabled = z;
            return this;
        }
    }

    protected DrawableCrossFadeFactory(int i, boolean z) {
        this.duration = i;
        this.isCrossFadeEnabled = z;
    }

    private Transition getResourceTransition() {
        if (this.resourceTransition == null) {
            this.resourceTransition = new DrawableCrossFadeTransition(this.duration, this.isCrossFadeEnabled);
        }
        return this.resourceTransition;
    }

    public Transition build(DataSource dataSource, boolean z) {
        return dataSource == DataSource.MEMORY_CACHE ? NoTransition.get() : getResourceTransition();
    }
}
