package com.miui.internal.log.message;

import android.util.Log;

public abstract class AbstractMessage implements Message {
    private static final String TAG = "AbstractMessage";
    private boolean mRecycled;

    public abstract void format(Appendable appendable);

    public abstract Throwable getThrowable();

    public boolean isRecycled() {
        return this.mRecycled;
    }

    public abstract void onRecycle();

    public void prepareForReuse() {
        this.mRecycled = false;
    }

    public void recycle() {
        if (this.mRecycled) {
            Log.w(TAG, "Recycle message twice");
            return;
        }
        onRecycle();
        this.mRecycled = true;
        MessageFactory.recycle(this);
    }
}
