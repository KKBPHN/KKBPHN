package com.miui.internal.variable.v16;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AbsSeekBar;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSeekBar.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSeekBar.Interface;
import miui.reflect.Field;

public class Android_Widget_AbsSeekBar_class extends com.miui.internal.variable.Android_Widget_AbsSeekBar_class implements Overridable {
    protected static final Field mIsDragging = Field.of(AbsSeekBar.class, "mIsDragging", "Z");
    protected static final Field mThumb = Field.of(AbsSeekBar.class, "mThumb", "Landroid/graphics/drawable/Drawable;");
    protected static final Field mThumbOffset = Field.of(AbsSeekBar.class, "mThumbOffset", "I");
    private Interface mImpl = new Interface() {
        public boolean onTouchEvent(AbsSeekBar absSeekBar, MotionEvent motionEvent) {
            return Android_Widget_AbsSeekBar_class.this.handleOnTouchEvent(0, absSeekBar, motionEvent);
        }
    };
    private Interface mOriginal;

    private boolean isInScrollingContainer(View view) {
        ViewParent parent = view.getParent();
        while (parent != null && (parent instanceof ViewGroup)) {
            if (((ViewGroup) parent).shouldDelayChildPressedState()) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        attachMethod("onTouchEvent", "(Landroid/view/MotionEvent;)Z");
    }

    /* access modifiers changed from: protected */
    public boolean callOriginalOnTouchEvent(long j, AbsSeekBar absSeekBar, MotionEvent motionEvent) {
        Interface interfaceR = this.mOriginal;
        return interfaceR != null ? interfaceR.onTouchEvent(absSeekBar, motionEvent) : originalOnTouchEvent(j, absSeekBar, motionEvent);
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleOnTouchEvent(0, null, null);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0096, code lost:
        if (r11.getAction() == 2) goto L_0x009a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handleOnTouchEvent(long j, AbsSeekBar absSeekBar, MotionEvent motionEvent) {
        Drawable drawable = (Drawable) mThumb.get(absSeekBar);
        if (drawable == null) {
            return callOriginalOnTouchEvent(j, absSeekBar, motionEvent);
        }
        boolean z = mIsDragging.getBoolean(absSeekBar);
        if (!z && ((AccessibilityManager) absSeekBar.getContext().getSystemService("accessibility")).isEnabled()) {
            return callOriginalOnTouchEvent(j, absSeekBar, motionEvent);
        }
        int paddingLeft = absSeekBar.getPaddingLeft() - mThumbOffset.getInt(absSeekBar);
        int intrinsicWidth = (drawable.getBounds().left + paddingLeft) - (drawable.getIntrinsicWidth() / 2);
        int intrinsicWidth2 = drawable.getBounds().right + paddingLeft + (drawable.getIntrinsicWidth() / 2);
        boolean z2 = true;
        boolean z3 = motionEvent.getX() > ((float) intrinsicWidth) && motionEvent.getX() < ((float) intrinsicWidth2);
        if (isInScrollingContainer(absSeekBar)) {
            if (z3 && motionEvent.getAction() == 0) {
                absSeekBar.setPressed(true);
            } else if (!z && (motionEvent.getAction() == 1 || motionEvent.getAction() == 3)) {
                absSeekBar.setPressed(false);
                if (!z2 || z3) {
                    return callOriginalOnTouchEvent(j, absSeekBar, motionEvent);
                }
                return false;
            } else if (!z) {
            }
        }
        z2 = z;
        if (!z2) {
        }
        return callOriginalOnTouchEvent(j, absSeekBar, motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public boolean originalOnTouchEvent(long j, AbsSeekBar absSeekBar, MotionEvent motionEvent) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_AbsSeekBar_class.originalOnTouchEvent(long, AbsSeekBar, MotionEvent)");
    }
}
