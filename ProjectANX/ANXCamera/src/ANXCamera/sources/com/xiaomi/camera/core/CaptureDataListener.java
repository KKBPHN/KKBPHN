package com.xiaomi.camera.core;

import android.media.Image;
import androidx.annotation.NonNull;

public interface CaptureDataListener {
    void onCaptureDataAvailable(@NonNull CaptureData captureData);

    void onOriginalImageClosed(Image image);
}
