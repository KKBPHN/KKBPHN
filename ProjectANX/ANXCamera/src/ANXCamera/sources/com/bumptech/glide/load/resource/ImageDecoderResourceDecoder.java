package com.bumptech.glide.load.resource;

import android.annotation.SuppressLint;
import android.graphics.ColorSpace;
import android.graphics.ColorSpace.Named;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.DecodeException;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.OnPartialImageListener;
import android.graphics.ImageDecoder.Source;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.Size;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;

@RequiresApi(api = 28)
public abstract class ImageDecoderResourceDecoder implements ResourceDecoder {
    private static final String TAG = "ImageDecoder";
    final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();

    public abstract Resource decode(Source source, int i, int i2, OnHeaderDecodedListener onHeaderDecodedListener);

    @Nullable
    public final Resource decode(@NonNull Source source, int i, int i2, @NonNull Options options) {
        final DecodeFormat decodeFormat = (DecodeFormat) options.get(Downsampler.DECODE_FORMAT);
        final DownsampleStrategy downsampleStrategy = (DownsampleStrategy) options.get(DownsampleStrategy.OPTION);
        boolean z = options.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null && ((Boolean) options.get(Downsampler.ALLOW_HARDWARE_CONFIG)).booleanValue();
        final boolean z2 = z;
        final PreferredColorSpace preferredColorSpace = (PreferredColorSpace) options.get(Downsampler.PREFERRED_COLOR_SPACE);
        final int i3 = i;
        final int i4 = i2;
        AnonymousClass1 r1 = new OnHeaderDecodedListener() {
            @SuppressLint({"Override"})
            public void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source) {
                Named named;
                boolean z = true;
                if (ImageDecoderResourceDecoder.this.hardwareConfigState.isHardwareConfigAllowed(i3, i4, z2, false)) {
                    imageDecoder.setAllocator(3);
                } else {
                    imageDecoder.setAllocator(1);
                }
                if (decodeFormat == DecodeFormat.PREFER_RGB_565) {
                    imageDecoder.setMemorySizePolicy(0);
                }
                imageDecoder.setOnPartialImageListener(new OnPartialImageListener() {
                    public boolean onPartialImage(@NonNull DecodeException decodeException) {
                        return false;
                    }
                });
                Size size = imageInfo.getSize();
                int i = i3;
                if (i == Integer.MIN_VALUE) {
                    i = size.getWidth();
                }
                int i2 = i4;
                if (i2 == Integer.MIN_VALUE) {
                    i2 = size.getHeight();
                }
                float scaleFactor = downsampleStrategy.getScaleFactor(size.getWidth(), size.getHeight(), i, i2);
                int round = Math.round(((float) size.getWidth()) * scaleFactor);
                int round2 = Math.round(((float) size.getHeight()) * scaleFactor);
                String str = ImageDecoderResourceDecoder.TAG;
                if (Log.isLoggable(str, 2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Resizing from [");
                    sb.append(size.getWidth());
                    String str2 = "x";
                    sb.append(str2);
                    sb.append(size.getHeight());
                    sb.append("] to [");
                    sb.append(round);
                    sb.append(str2);
                    sb.append(round2);
                    sb.append("] scaleFactor: ");
                    sb.append(scaleFactor);
                    Log.v(str, sb.toString());
                }
                imageDecoder.setTargetSize(round, round2);
                int i3 = VERSION.SDK_INT;
                if (i3 >= 28) {
                    if (preferredColorSpace != PreferredColorSpace.DISPLAY_P3 || imageInfo.getColorSpace() == null || !imageInfo.getColorSpace().isWideGamut()) {
                        z = false;
                    }
                    if (z) {
                        named = Named.DISPLAY_P3;
                        imageDecoder.setTargetColorSpace(ColorSpace.get(named));
                    }
                } else if (i3 < 26) {
                    return;
                }
                named = Named.SRGB;
                imageDecoder.setTargetColorSpace(ColorSpace.get(named));
            }
        };
        return decode(source, i, i2, (OnHeaderDecodedListener) r1);
    }

    public final boolean handles(@NonNull Source source, @NonNull Options options) {
        return true;
    }
}
