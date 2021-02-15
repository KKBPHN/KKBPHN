package com.android.camera2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.xiaomi.camera.core.PictureInfo;

public class PortraitDepthMapExif {
    private int cameraPreferredMode;
    @Nullable
    private int[] coordinatesOfOfTheRegionUnderWatermarks;
    private int depthLength;
    @NonNull
    private int[] dulCameraWaterMarkLocation;
    @Nullable
    private byte[] dulWaterMark;
    private boolean isBokehFrontCamera;
    private boolean isCinematicAspectRatio;
    private boolean isFrontMirror;
    private boolean isSupportZeroDegreeOrientationImage;
    private byte[] jpeg;
    @Nullable
    private byte[] jpegDataOfTheRegionUnderWatermarks;
    private int lightingPattern;
    private PictureInfo pictureInfo;
    private int portraitLightingVersioin;
    private int rawLength;
    private int rotation;
    private long timeStamp;
    @Nullable
    private byte[] timeWaterMark;
    @NonNull
    private int[] timeWaterMarkLocation;

    public class Builder {
        /* access modifiers changed from: private */
        public int cameraPreferredMode;
        /* access modifiers changed from: private */
        @Nullable
        public int[] coordinatesOfOfTheRegionUnderWatermarks;
        /* access modifiers changed from: private */
        public int depthLength;
        /* access modifiers changed from: private */
        @NonNull
        public int[] dulCameraWaterMarkLocation;
        /* access modifiers changed from: private */
        @Nullable
        public byte[] dulWaterMark;
        /* access modifiers changed from: private */
        public boolean isBokehFrontCamera;
        /* access modifiers changed from: private */
        public boolean isCinematicAspectRatio;
        /* access modifiers changed from: private */
        public boolean isFrontMirror;
        /* access modifiers changed from: private */
        public boolean isSupportZeroDegreeOrientationImage;
        /* access modifiers changed from: private */
        public byte[] jpeg;
        /* access modifiers changed from: private */
        @Nullable
        public byte[] jpegDataOfTheRegionUnderWatermarks;
        /* access modifiers changed from: private */
        public int lightingPattern;
        /* access modifiers changed from: private */
        public PictureInfo pictureInfo;
        /* access modifiers changed from: private */
        public int portraitLightingVersioin;
        /* access modifiers changed from: private */
        public int rawLength;
        /* access modifiers changed from: private */
        public int rotation;
        /* access modifiers changed from: private */
        public long timeStamp;
        /* access modifiers changed from: private */
        @Nullable
        public byte[] timeWaterMark;
        /* access modifiers changed from: private */
        @NonNull
        public int[] timeWaterMarkLocation;

        public PortraitDepthMapExif build() {
            return new PortraitDepthMapExif(this);
        }

        public Builder cameraPreferredMode(int i) {
            this.cameraPreferredMode = i;
            return this;
        }

        public Builder coordinatesOfOfTheRegionUnderWatermarks(@Nullable int[] iArr) {
            this.coordinatesOfOfTheRegionUnderWatermarks = iArr;
            return this;
        }

        public Builder depthLength(int i) {
            this.depthLength = i;
            return this;
        }

        public Builder dulCameraWaterMarkLocation(@NonNull int[] iArr) {
            this.dulCameraWaterMarkLocation = iArr;
            return this;
        }

        public Builder dulWaterMark(@Nullable byte[] bArr) {
            this.dulWaterMark = bArr;
            return this;
        }

        public Builder isBokehFrontCamera(boolean z) {
            this.isBokehFrontCamera = z;
            return this;
        }

        public Builder isCinematicAspectRatio(boolean z) {
            this.isCinematicAspectRatio = z;
            return this;
        }

        public Builder isFrontMirror(boolean z) {
            this.isFrontMirror = z;
            return this;
        }

        public Builder isSupportZeroDegreeOrientationImage(boolean z) {
            this.isSupportZeroDegreeOrientationImage = z;
            return this;
        }

        public Builder jpeg(byte[] bArr) {
            this.jpeg = bArr;
            return this;
        }

        public Builder jpegDataOfTheRegionUnderWatermarks(@Nullable byte[] bArr) {
            this.jpegDataOfTheRegionUnderWatermarks = bArr;
            return this;
        }

        public Builder lightingPattern(int i) {
            this.lightingPattern = i;
            return this;
        }

        public Builder pictureInfo(PictureInfo pictureInfo2) {
            this.pictureInfo = pictureInfo2;
            return this;
        }

        public Builder portraitLightingVersioin(int i) {
            this.portraitLightingVersioin = i;
            return this;
        }

        public Builder rawLength(int i) {
            this.rawLength = i;
            return this;
        }

        public Builder rotation(int i) {
            this.rotation = i;
            return this;
        }

        public Builder timeStamp(long j) {
            this.timeStamp = j;
            return this;
        }

        public Builder timeWaterMark(@Nullable byte[] bArr) {
            this.timeWaterMark = bArr;
            return this;
        }

        public Builder timeWaterMarkLocation(@NonNull int[] iArr) {
            this.timeWaterMarkLocation = iArr;
            return this;
        }
    }

    private PortraitDepthMapExif(Builder builder) {
        this.jpeg = builder.jpeg;
        this.dulWaterMark = builder.dulWaterMark;
        this.dulCameraWaterMarkLocation = builder.dulCameraWaterMarkLocation;
        this.timeWaterMark = builder.timeWaterMark;
        this.timeWaterMarkLocation = builder.timeWaterMarkLocation;
        this.jpegDataOfTheRegionUnderWatermarks = builder.jpegDataOfTheRegionUnderWatermarks;
        this.coordinatesOfOfTheRegionUnderWatermarks = builder.coordinatesOfOfTheRegionUnderWatermarks;
        this.rotation = builder.rotation;
        this.lightingPattern = builder.lightingPattern;
        this.isFrontMirror = builder.isFrontMirror;
        this.isBokehFrontCamera = builder.isBokehFrontCamera;
        this.isCinematicAspectRatio = builder.isCinematicAspectRatio;
        this.pictureInfo = builder.pictureInfo;
        this.rawLength = builder.rawLength;
        this.depthLength = builder.depthLength;
        this.timeStamp = builder.timeStamp;
        this.portraitLightingVersioin = builder.portraitLightingVersioin;
        this.isSupportZeroDegreeOrientationImage = builder.isSupportZeroDegreeOrientationImage;
        this.cameraPreferredMode = builder.cameraPreferredMode;
    }

    public int getCameraPreferredMode() {
        return this.cameraPreferredMode;
    }

    @Nullable
    public int[] getCoordinatesOfOfTheRegionUnderWatermarks() {
        return this.coordinatesOfOfTheRegionUnderWatermarks;
    }

    public int getDepthLength() {
        return this.depthLength;
    }

    @NonNull
    public int[] getDulCameraWaterMarkLocation() {
        return this.dulCameraWaterMarkLocation;
    }

    @Nullable
    public byte[] getDulWaterMark() {
        return this.dulWaterMark;
    }

    public byte[] getJpeg() {
        return this.jpeg;
    }

    @Nullable
    public byte[] getJpegDataOfTheRegionUnderWatermarks() {
        return this.jpegDataOfTheRegionUnderWatermarks;
    }

    public int getLightingPattern() {
        return this.lightingPattern;
    }

    public PictureInfo getPictureInfo() {
        return this.pictureInfo;
    }

    public int getPortraitLightingVersioin() {
        return this.portraitLightingVersioin;
    }

    public int getRawLength() {
        return this.rawLength;
    }

    public int getRotation() {
        return this.rotation;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    @Nullable
    public byte[] getTimeWaterMark() {
        return this.timeWaterMark;
    }

    @NonNull
    public int[] getTimeWaterMarkLocation() {
        return this.timeWaterMarkLocation;
    }

    public boolean isBokehFrontCamera() {
        return this.isBokehFrontCamera;
    }

    public boolean isCinematicAspectRatio() {
        return this.isCinematicAspectRatio;
    }

    public boolean isFrontMirror() {
        return this.isFrontMirror;
    }

    public boolean isSupportZeroDegreeOrientationImage() {
        return this.isSupportZeroDegreeOrientationImage;
    }
}
