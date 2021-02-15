package com.android.camera.data.observeable;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.functions.Consumer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class FilmDreamProcessing extends VMBase {
    public static final int EXECUTE_COMBINE = 6;
    public static final int EXECUTE_CONCAT = 2;
    public static final int FORCE_EXIT_WHEN_RECORDING = 10;
    public static final int PREVIEW_ERROR = 3;
    public static final int PREVIEW_PAUSE = 4;
    public static final int PREVIEW_PLAYING = 5;
    public static final int RECORDING_DONE = 9;
    public static final int RECORDING_INCHOATE = 1;
    public static final int SAVE_COMPLETE = 7;
    public static final int SAVE_ERROR = 8;
    public static final int STATE_IDLE = 0;
    public RxData mRxProcessingState = new RxData(Integer.valueOf(0));
    public RxData mRxRecordingState;
    private List mTempVideoList;
    private long mTotalTime;

    @Retention(RetentionPolicy.SOURCE)
    public @interface VmpState {
    }

    /* access modifiers changed from: protected */
    public boolean achieveEndOfCycle() {
        return false;
    }

    public int getCurrentState() {
        return ((Integer) this.mRxProcessingState.get()).intValue();
    }

    public List getTempVideoList() {
        if (this.mTempVideoList == null) {
            this.mTempVideoList = new ArrayList();
        }
        return this.mTempVideoList;
    }

    public long getTotalTime() {
        return this.mTotalTime;
    }

    public void postState(int i) {
    }

    public void reset() {
        List list = this.mTempVideoList;
        if (list != null) {
            list.clear();
        }
        this.mTotalTime = 0;
        rollbackData();
    }

    /* access modifiers changed from: protected */
    public void rollbackData() {
        this.mRxProcessingState.setSilently(Integer.valueOf(0));
    }

    public void setTotalTime(long j) {
        this.mTotalTime = j;
    }

    public void startObservable(LifecycleOwner lifecycleOwner, Consumer consumer) {
        this.mRxProcessingState.observable(lifecycleOwner).subscribe(consumer);
    }

    @MainThread
    public void updateState(int i) {
        this.mRxProcessingState.set(Integer.valueOf(i));
        judge();
    }
}
