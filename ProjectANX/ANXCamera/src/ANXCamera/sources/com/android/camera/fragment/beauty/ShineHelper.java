package com.android.camera.fragment.beauty;

import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.OnShineChangedProtocol;

public class ShineHelper {
    public static void clearBeauty() {
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (miBeautyProtocol != null) {
            miBeautyProtocol.clearBeauty();
        }
    }

    public static void onBeautyChanged() {
        OnShineChangedProtocol onShineChangedProtocol = (OnShineChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(234);
        if (onShineChangedProtocol != null) {
            onShineChangedProtocol.onShineChanged(false, 239);
        }
    }

    public static void onShineStateChanged() {
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (miBeautyProtocol != null) {
            miBeautyProtocol.onStateChanged();
        }
    }

    public static void onVideoBokehRatioChanged() {
        OnShineChangedProtocol onShineChangedProtocol = (OnShineChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(234);
        if (onShineChangedProtocol != null) {
            onShineChangedProtocol.onShineChanged(false, 243);
        }
    }

    public static void onVideoBokehWithRetentionChanged() {
        OnShineChangedProtocol onShineChangedProtocol = (OnShineChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(234);
        if (onShineChangedProtocol != null) {
            onShineChangedProtocol.onShineChanged(false, 244);
        }
    }

    public static void onVideoFilterChanged() {
        OnShineChangedProtocol onShineChangedProtocol = (OnShineChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(234);
        if (onShineChangedProtocol != null) {
            onShineChangedProtocol.onShineChanged(false, 196);
        }
    }

    public static void resetBeauty() {
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (miBeautyProtocol != null) {
            miBeautyProtocol.resetBeauty();
        }
    }
}
