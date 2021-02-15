package com.bumptech.glide.load.resource.file;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.File;

public class FileDecoder implements ResourceDecoder {
    public Resource decode(@NonNull File file, int i, int i2, @NonNull Options options) {
        return new FileResource(file);
    }

    public boolean handles(@NonNull File file, @NonNull Options options) {
        return true;
    }
}
