package com.android.camera.data.observeable;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import com.android.camera.fragment.vv.VVWorkspaceItem;
import io.reactivex.functions.Consumer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VMProcessing extends VMBase {
    public static final int EXECUTE_COMBINE = 7;
    public static final int EXECUTE_CONCAT = 3;
    public static final int PREVIEW_ERROR = 4;
    public static final int PREVIEW_PAUSE = 5;
    public static final int PREVIEW_PLAYING = 6;
    public static final int RECORDING_DONE = 2;
    public static final int RECORDING_INCHOATE = 1;
    public static final int SAVE_COMPLETE = 8;
    public static final int SAVE_ERROR = 9;
    private VVWorkspaceItem mCurrentWorkspaceItem;
    public RxData mRxProcessingState = new RxData(Integer.valueOf(1));
    public RxData mRxRecordingState;

    @Retention(RetentionPolicy.SOURCE)
    public @interface VmpState {
    }

    /* access modifiers changed from: protected */
    public boolean achieveEndOfCycle() {
        return ((Integer) this.mRxProcessingState.get()).intValue() == 8 || ((Integer) this.mRxProcessingState.get()).intValue() == 9;
    }

    public int getCurrentState() {
        return ((Integer) this.mRxProcessingState.get()).intValue();
    }

    public VVWorkspaceItem getCurrentWorkspaceItem(String str, String str2) {
        if (this.mCurrentWorkspaceItem == null) {
            this.mCurrentWorkspaceItem = VVWorkspaceItem.createNew(str, str2);
        }
        return this.mCurrentWorkspaceItem;
    }

    public void postState(int i) {
    }

    public void reset() {
        rollbackData();
    }

    /* access modifiers changed from: protected */
    public void rollbackData() {
        this.mRxProcessingState.setSilently(Integer.valueOf(1));
    }

    public void setCurrentWorkspaceItem(VVWorkspaceItem vVWorkspaceItem) {
        this.mCurrentWorkspaceItem = vVWorkspaceItem;
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
