package com.miui.internal.variable.v16;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.miui.internal.R;
import com.miui.internal.util.TaggingDrawableHelper;

public class Android_Widget_ListView_class extends com.miui.internal.variable.Android_Widget_ListView_class {
    /* access modifiers changed from: protected */
    public void attachSpecialMethod() {
        attachConstructor("(Landroid/content/Context;Landroid/util/AttributeSet;I)V");
    }

    public void buildProxy() {
        attachMethod("layoutChildren", "()V");
        attachMethod("fillGap", "(Z)V");
        attachSpecialMethod();
    }

    /* access modifiers changed from: protected */
    public void callOriginalFillGap(long j, ListView listView, boolean z) {
        originalFillGap(j, listView, z);
    }

    /* access modifiers changed from: protected */
    public void callOriginalLayoutChildren(long j, ListView listView) {
        originalLayoutChildren(j, listView);
    }

    /* access modifiers changed from: protected */
    public void doInit(ListView listView, Context context) {
        if (listView.getPaddingLeft() == 0 && listView.getPaddingRight() == 0) {
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.listview_horizontal_padding);
            if (dimensionPixelSize != 0) {
                listView.setPadding(dimensionPixelSize, listView.getPaddingTop(), dimensionPixelSize, listView.getPaddingBottom());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleLayoutChildren(0, null);
        handleFillGap(0, null, false);
        handle_init_(0, null, null, null, 0);
    }

    /* access modifiers changed from: protected */
    public void handleFillGap(long j, ListView listView, boolean z) {
        callOriginalFillGap(j, listView, z);
        TaggingDrawableHelper.tagChildSequenceState(listView);
    }

    /* access modifiers changed from: protected */
    public void handleLayoutChildren(long j, ListView listView) {
        callOriginalLayoutChildren(j, listView);
        TaggingDrawableHelper.tagChildSequenceState(listView);
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, ListView listView, Context context, AttributeSet attributeSet, int i) {
        original_init_(j, listView, context, attributeSet, i);
        doInit(listView, context);
    }

    /* access modifiers changed from: protected */
    public void originalFillGap(long j, ListView listView, boolean z) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_ListView_class.originalFillGap(long, ListView, boolean)");
    }

    /* access modifiers changed from: protected */
    public void originalLayoutChildren(long j, ListView listView) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_ListView_class.originalLayoutChildren(long, ListView)");
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, ListView listView, Context context, AttributeSet attributeSet, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_ListView_class.original_init_(long, ListView, Context, AttributeSet, int)");
    }
}
