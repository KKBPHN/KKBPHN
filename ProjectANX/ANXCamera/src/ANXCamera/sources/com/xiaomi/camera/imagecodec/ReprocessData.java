package com.xiaomi.camera.imagecodec;

import android.media.Image;
import android.support.annotation.NonNull;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.ArrayList;
import java.util.List;

public class ReprocessData {
    private static final int DEFAULT_JPEG_QUALITY = 100;
    public static int REPROCESS_FUNCTION_NONE = 0;
    public static int REPROCESS_FUNCTION_RAW_MFNR = 1;
    public static int REPROCESS_FUNCTION_RAW_MFNR_2ND = 2;
    public static int REPROCESS_FUNCTION_RAW_SUPERNIGHT = 3;
    public static int REPROCESS_FUNCTION_RAW_SUPERNIGHT_2ND = 4;
    private int[] mCropRegion;
    private DataStatusCallback mDataStatusCallback;
    private String mImageTag;
    private boolean mIsFrontCamera;
    private boolean mIsFrontMirror;
    private boolean mIsMainImageFromPool;
    private boolean mIsTuningImageFromPool;
    private int mJpegQuality;
    private boolean mKeepTuningImage;
    private ArrayList mMainImageList;
    private int mOutputFormat;
    private int mOutputHeight;
    private int mOutputWidth;
    private int mRawInputHeight;
    private int mRawInputWidth;
    private int mReprocessFunctionType;
    private OnDataAvailableListener mResultListener;
    private boolean mRotateOrientationToZero;
    private ICustomCaptureResult mTotalCaptureResult;
    private ArrayList mTuningImageList;
    private int mYuvInputHeight;
    private int mYuvInputWidth;

    public interface DataStatusCallback {
        void onImageClosed(List list);
    }

    public interface OnDataAvailableListener {
        void onError(String str, String str2);

        void onJpegAvailable(byte[] bArr, String str);

        void onJpegImageAvailable(Image image, String str, boolean z);

        void onTuningImageAvailable(Image image, String str, boolean z);

        void onYuvAvailable(Image image, String str, boolean z);
    }

    public ReprocessData(@NonNull Image image, @NonNull String str, @NonNull ICustomCaptureResult iCustomCaptureResult, boolean z, int i, int i2, int i3, @NonNull OnDataAvailableListener onDataAvailableListener) {
        this.mJpegQuality = 100;
        this.mMainImageList = new ArrayList(1);
        this.mMainImageList.add(image);
        this.mImageTag = str;
        this.mTotalCaptureResult = iCustomCaptureResult;
        this.mIsFrontCamera = z;
        this.mOutputWidth = i;
        this.mOutputHeight = i2;
        this.mOutputFormat = i3;
        this.mResultListener = onDataAvailableListener;
        this.mJpegQuality = 100;
    }

    public ReprocessData(@NonNull ArrayList arrayList, @NonNull String str, @NonNull ICustomCaptureResult iCustomCaptureResult, boolean z, int i, int i2, int i3, @NonNull OnDataAvailableListener onDataAvailableListener) {
        this.mJpegQuality = 100;
        this.mMainImageList = arrayList;
        this.mImageTag = str;
        this.mTotalCaptureResult = iCustomCaptureResult;
        this.mIsFrontCamera = z;
        this.mOutputWidth = i;
        this.mOutputHeight = i2;
        this.mOutputFormat = i3;
        this.mResultListener = onDataAvailableListener;
    }

    public int[] getCropRegion() {
        return this.mCropRegion;
    }

    public DataStatusCallback getDataStatusCallback() {
        return this.mDataStatusCallback;
    }

    public String getImageTag() {
        return this.mImageTag;
    }

    public int getJpegQuality() {
        return this.mJpegQuality;
    }

    public ArrayList getMainImage() {
        return this.mMainImageList;
    }

    public int getOutputFormat() {
        return this.mOutputFormat;
    }

    public int getOutputHeight() {
        return this.mOutputHeight;
    }

    public int getOutputWidth() {
        return this.mOutputWidth;
    }

    public int getRawInputHeight() {
        return this.mRawInputHeight;
    }

    public int getRawInputWidth() {
        return this.mRawInputWidth;
    }

    public int getReprocessFuntionType() {
        return this.mReprocessFunctionType;
    }

    public OnDataAvailableListener getResultListener() {
        return this.mResultListener;
    }

    public ICustomCaptureResult getTotalCaptureResult() {
        return this.mTotalCaptureResult;
    }

    public ArrayList getTuningImage() {
        return this.mTuningImageList;
    }

    public int getYuvInputHeight() {
        return this.mYuvInputHeight;
    }

    public int getYuvInputWidth() {
        return this.mYuvInputWidth;
    }

    public boolean isFrontCamera() {
        return this.mIsFrontCamera;
    }

    public boolean isFrontMirror() {
        return this.mIsFrontMirror;
    }

    public boolean isImageFromPool() {
        return this.mIsMainImageFromPool;
    }

    public boolean isKeepTuningImage() {
        return this.mKeepTuningImage;
    }

    public boolean isRotateOrientationToZero() {
        return this.mRotateOrientationToZero;
    }

    public boolean isTuningImageFromPool() {
        return this.mIsTuningImageFromPool;
    }

    public void setCropRegion(int[] iArr) {
        this.mCropRegion = iArr;
    }

    public void setDataStatusCallback(DataStatusCallback dataStatusCallback) {
        this.mDataStatusCallback = dataStatusCallback;
    }

    public void setFrontMirror(boolean z) {
        this.mIsFrontMirror = z;
    }

    public void setImageFromPool(boolean z) {
        this.mIsMainImageFromPool = z;
    }

    public void setJpegQuality(int i) {
        if (i < 1 || i > 100) {
            this.mJpegQuality = 100;
        } else {
            this.mJpegQuality = i;
        }
    }

    public void setKeepTuningImage(boolean z) {
        this.mKeepTuningImage = z;
    }

    public ArrayList setMainImage(Image image) {
        ArrayList arrayList = this.mMainImageList;
        this.mMainImageList = new ArrayList(1);
        this.mMainImageList.add(image);
        return arrayList;
    }

    public ArrayList setMainImage(ArrayList arrayList) {
        ArrayList arrayList2 = this.mMainImageList;
        this.mMainImageList = arrayList;
        return arrayList2;
    }

    public void setRawInputSize(int i, int i2) {
        this.mRawInputWidth = i;
        this.mRawInputHeight = i2;
    }

    public void setReprocessFunctionType(int i) {
        this.mReprocessFunctionType = i;
    }

    public void setRotateOrientationToZero(boolean z) {
        this.mRotateOrientationToZero = z;
    }

    public ArrayList setTuningImage(Image image) {
        ArrayList arrayList = this.mTuningImageList;
        this.mTuningImageList = new ArrayList(1);
        this.mTuningImageList.add(image);
        return arrayList;
    }

    public ArrayList setTuningImage(ArrayList arrayList) {
        ArrayList arrayList2 = this.mTuningImageList;
        this.mTuningImageList = arrayList;
        return arrayList2;
    }

    public void setTuningImageFromPool(boolean z) {
        this.mIsTuningImageFromPool = z;
    }

    public void setYuvInputSize(int i, int i2) {
        this.mYuvInputWidth = i;
        this.mYuvInputHeight = i2;
    }
}
