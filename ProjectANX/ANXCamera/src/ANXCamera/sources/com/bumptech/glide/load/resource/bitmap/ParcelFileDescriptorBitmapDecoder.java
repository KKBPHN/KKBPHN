package com.bumptech.glide.load.resource.bitmap;

import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;

@RequiresApi(21)
public final class ParcelFileDescriptorBitmapDecoder implements ResourceDecoder {
    private final Downsampler downsampler;

    public ParcelFileDescriptorBitmapDecoder(Downsampler downsampler2) {
        this.downsampler = downsampler2;
    }

    @Nullable
    public Resource decode(@NonNull ParcelFileDescriptor parcelFileDescriptor, int i, int i2, @NonNull Options options) {
        return this.downsampler.decode(parcelFileDescriptor, i, i2, options);
    }

    public boolean handles(@NonNull ParcelFileDescriptor parcelFileDescriptor, @NonNull Options options) {
        return this.downsampler.handles(parcelFileDescriptor);
    }
}
