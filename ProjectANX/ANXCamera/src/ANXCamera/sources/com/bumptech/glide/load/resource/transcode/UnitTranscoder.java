package com.bumptech.glide.load.resource.transcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public class UnitTranscoder implements ResourceTranscoder {
    private static final UnitTranscoder UNIT_TRANSCODER = new UnitTranscoder();

    public static ResourceTranscoder get() {
        return UNIT_TRANSCODER;
    }

    @Nullable
    public Resource transcode(@NonNull Resource resource, @NonNull Options options) {
        return resource;
    }
}
