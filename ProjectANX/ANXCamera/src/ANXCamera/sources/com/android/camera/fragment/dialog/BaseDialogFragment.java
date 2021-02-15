package com.android.camera.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.android.camera.Camera;
import com.android.camera.Display;
import com.android.camera.Util;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;

public class BaseDialogFragment extends DialogFragment implements OnKeyListener, HandleBackTrace {
    protected static final int STATE_HIDE = -1;
    protected static final int STATE_SHOW = 1;
    private GestureDetector gesture;
    private DismissCallback mDismissCallback;

    public interface DismissCallback {
        void onDismiss();
    }

    class MyOnGestureListener extends SimpleOnGestureListener {
        private MyOnGestureListener() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            BaseDialogFragment.this.onBackEvent(5);
            return super.onScroll(motionEvent, motionEvent2, f, f2);
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            BaseDialogFragment.this.onBackEvent(5);
            return super.onSingleTapUp(motionEvent);
        }
    }

    public /* synthetic */ boolean O00000Oo(View view, MotionEvent motionEvent) {
        return this.gesture.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void adjustViewHeight(View view) {
        Rect displayRect = Util.getDisplayRect(0);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.width = displayRect.width();
        marginLayoutParams.height = displayRect.height();
        marginLayoutParams.topMargin = displayRect.top;
    }

    public boolean canProvide() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void initViewOnTouchListener(View view) {
        this.gesture = new GestureDetector(getActivity(), new MyOnGestureListener());
        view.setOnTouchListener(new O000000o(this));
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setGravity(48);
            window.setLayout(-1, -1);
            LayoutParams attributes = window.getAttributes();
            attributes.type = 1;
            window.setAttributes(attributes);
            if (Display.isContentViewExtendToTopEdges()) {
                CompatibilityUtils.setCutoutModeShortEdges(window);
            }
        }
    }

    public boolean onBackEvent(int i) {
        Camera camera = (Camera) getActivity();
        if (camera != null) {
            camera.showNewNotification();
            camera.getCameraScreenNail().drawBlackFrame(false);
        }
        return false;
    }

    public void onDismiss(@NonNull DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DismissCallback dismissCallback = this.mDismissCallback;
        if (dismissCallback != null) {
            dismissCallback.onDismiss();
        }
    }

    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return false;
    }

    public void setDismissCallback(DismissCallback dismissCallback) {
        this.mDismissCallback = dismissCallback;
    }
}
