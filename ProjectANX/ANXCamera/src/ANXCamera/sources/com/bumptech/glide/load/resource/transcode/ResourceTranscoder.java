package com.bumptech.glide.load.resource.transcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceTranscoder {
    @Nullable
    Resource transcode(@NonNull Resource resource, @NonNull Options options);
}
