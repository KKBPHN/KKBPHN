package com.android.camera2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.ViewTreeObserver.OnWindowAttachListener;
import com.android.camera.log.Log;

public final class DetachableClickListener implements OnClickListener, OnCancelListener {
    /* access modifiers changed from: private */
    public static final String TAG = "DetachableClickListener";
    /* access modifiers changed from: private */
    public OnClickListener delegateOrNull;
    /* access modifiers changed from: private */
    public OnCancelListener mCancelListener;

    private DetachableClickListener(OnClickListener onClickListener, OnCancelListener onCancelListener) {
        this.delegateOrNull = onClickListener;
        this.mCancelListener = onCancelListener;
    }

    public static DetachableClickListener wrap(OnClickListener onClickListener, OnCancelListener onCancelListener) {
        return new DetachableClickListener(onClickListener, onCancelListener);
    }

    public void clearOnDetach(Dialog dialog) {
        dialog.getWindow().getDecorView().getViewTreeObserver().addOnWindowAttachListener(new OnWindowAttachListener() {
            public void onWindowAttached() {
                Log.v(DetachableClickListener.TAG, "dialog attach to window");
            }

            public void onWindowDetached() {
                DetachableClickListener.this.delegateOrNull = null;
                DetachableClickListener.this.mCancelListener = null;
            }
        });
    }

    public void onCancel(DialogInterface dialogInterface) {
        OnCancelListener onCancelListener = this.mCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        OnClickListener onClickListener = this.delegateOrNull;
        if (onClickListener != null) {
            onClickListener.onClick(dialogInterface, i);
        }
    }
}
