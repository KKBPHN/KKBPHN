package com.xiaomi.camera.isp;

import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Size;
import com.xiaomi.camera.imagecodec.OutputConfiguration;

public class IspInterfaceInfo {
    private static final String TAG = "IspInterfaceInfo";
    private boolean mInitialized = true;
    private IspInterfaceIO mInputOutput;
    private IspInterface mIspInterface;
    private ImageReader mPicImageReader;
    private ImageReader mTuningImageReader;
    private ImageReader mYuvImageReader;

    public IspInterfaceInfo(@NonNull IspInterface ispInterface, @NonNull IspInterfaceIO ispInterfaceIO, @NonNull ImageReader imageReader, @Nullable ImageReader imageReader2, @Nullable ImageReader imageReader3) {
        this.mIspInterface = ispInterface;
        this.mInputOutput = ispInterfaceIO;
        this.mPicImageReader = imageReader;
        this.mYuvImageReader = imageReader2;
        this.mTuningImageReader = imageReader3;
    }

    public IspInterface getIspInterface() {
        return this.mIspInterface;
    }

    public ImageReader getPicImageReader() {
        return this.mPicImageReader;
    }

    public OutputConfiguration getPicOutputConfiguration() {
        return this.mInputOutput.getPicOutputConfiguration();
    }

    public Size getRawInputConfiguration() {
        return this.mInputOutput.getRawInputSize();
    }

    public ImageReader getYuvImageReader() {
        return this.mYuvImageReader;
    }

    public Size getYuvInputConfiguration() {
        return this.mInputOutput.getYuvInputSize();
    }

    public OutputConfiguration getYuvOutputConfiguration() {
        return this.mInputOutput.getYuvOutputConfiguration();
    }

    public boolean isValid() {
        return this.mInitialized;
    }

    public void release() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("release: E. ");
        sb.append(this);
        Log.d(str, sb.toString());
        this.mInitialized = false;
        ImageReader imageReader = this.mPicImageReader;
        if (imageReader != null) {
            imageReader.close();
            this.mPicImageReader = null;
        }
        ImageReader imageReader2 = this.mYuvImageReader;
        if (imageReader2 != null) {
            imageReader2.close();
            this.mYuvImageReader = null;
        }
        ImageReader imageReader3 = this.mTuningImageReader;
        if (imageReader3 != null) {
            imageReader3.close();
            this.mTuningImageReader = null;
        }
        IspInterface ispInterface = this.mIspInterface;
        if (ispInterface != null) {
            ispInterface.release();
            this.mIspInterface = null;
        }
        this.mInputOutput = null;
        Log.d(TAG, "release: X");
    }
}
