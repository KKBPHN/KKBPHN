package com.android.zxing;

import android.media.Image;
import com.android.camera.Util;
import com.android.camera.log.Log;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Decoder {
    public static final String TAG = "Decoder";
    long mDecodeAutoInterval = Util.getSampleTime();
    Disposable mDecodeDisposable;
    int mDecodeMaxCount = -1;
    volatile boolean mDecoding;
    AtomicInteger mDecodingCount = new AtomicInteger(0);
    boolean mEnable = true;
    private long mLastDecodeTime;
    PublishSubject mSubjects;

    public abstract void init(int i);

    /* access modifiers changed from: protected */
    public boolean isNeedImage() {
        return false;
    }

    public boolean needPreviewFrame() {
        if (!this.mEnable || !this.mDecoding) {
            return false;
        }
        if (this.mDecodeMaxCount != -1 && this.mDecodingCount.get() >= this.mDecodeMaxCount) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastDecodeTime < this.mDecodeAutoInterval) {
            return false;
        }
        this.mLastDecodeTime = currentTimeMillis;
        this.mDecodingCount.getAndIncrement();
        StringBuilder sb = new StringBuilder();
        sb.append("preview frame buffer received... mDecodingCount = ");
        sb.append(this.mDecodingCount.get());
        Log.v(TAG, sb.toString());
        return true;
    }

    public void onPreviewFrame(Image image) {
    }

    public abstract void onPreviewFrame(PreviewImage previewImage);

    public void quit() {
        this.mEnable = false;
    }

    public abstract void reset();

    public void setEnable(boolean z) {
        this.mEnable = z;
    }

    public abstract void startDecode();

    public void stopDecode() {
        Log.d(TAG, "Stop decode.");
        Disposable disposable = this.mDecodeDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mDecodeDisposable.dispose();
        }
        this.mDecoding = false;
    }
}
