package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public interface TransitionFactory {
    Transition build(DataSource dataSource, boolean z);
}
