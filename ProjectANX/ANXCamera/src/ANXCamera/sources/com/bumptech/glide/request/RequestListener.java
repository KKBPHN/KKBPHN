package com.bumptech.glide.request;

import androidx.annotation.Nullable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

public interface RequestListener {
    boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target target, boolean z);

    boolean onResourceReady(Object obj, Object obj2, Target target, DataSource dataSource, boolean z);
}
