package com.miui.internal.variable.v16;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import com.miui.internal.util.TaggingDrawableHelper;
import com.miui.internal.variable.Android_View_View_class;
import com.miui.internal.variable.VariableExceptionHandler;
import com.miui.internal.view.menu.ContextMenuBuilder;
import miui.reflect.Field;
import miui.reflect.Method;

public class Android_View_View_class_common extends Android_View_View_class {
    private static Class ListenerInfoClass;
    private static Method createSnapshot;
    private static final Method getContextMenuInfo = Method.of(View.class, "getContextMenuInfo", "()Landroid/view/ContextMenu$ContextMenuInfo;");
    private static final Field mKeyedTags = Field.of(View.class, "mKeyedTags", "Landroid/util/SparseArray;");
    private static final Field mLeft;
    private static final Field mListenerInfo = Field.of(View.class, "mListenerInfo", "Landroid/view/View$ListenerInfo;");
    private static final Field mOldHeightMeasureSpec;
    private static final Field mOldWidthMeasureSpec;
    private static Field mOnCreateContextMenuListener;
    private static final Field mPrivateFlags;
    private static final Field mRight;
    private static final Field mScrollX;
    private static final Field mScrollY;
    private static final Method onCreateContextMenu = Method.of(View.class, "onCreateContextMenu", "(Landroid/view/ContextMenu;)V");

    static {
        String str = "I";
        mPrivateFlags = Field.of(View.class, "mPrivateFlags", str);
        mOldWidthMeasureSpec = Field.of(View.class, "mOldWidthMeasureSpec", str);
        mOldHeightMeasureSpec = Field.of(View.class, "mOldHeightMeasureSpec", str);
        mScrollX = Field.of(View.class, "mScrollX", str);
        mScrollY = Field.of(View.class, "mScrollY", str);
        mLeft = Field.of(View.class, "mLeft", str);
        mRight = Field.of(View.class, "mRight", str);
        try {
            createSnapshot = Method.of(View.class, "createSnapshot", "(Landroid/graphics/Bitmap$Config;IZ)Landroid/graphics/Bitmap;");
        } catch (Exception unused) {
            createSnapshot = null;
        }
    }

    private int getOldHeightMeasureSpec(View view) {
        try {
            return mOldHeightMeasureSpec.getInt(view);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mOldHeightMeasureSpec", e);
            return 0;
        }
    }

    private int getOldWidthMeasureSpec(View view) {
        try {
            return mOldWidthMeasureSpec.getInt(view);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mOldWidthMeasureSpec", e);
            return 0;
        }
    }

    private int getPrivateFlags(View view) {
        try {
            return mPrivateFlags.getInt(view);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mPrivateFlags", e);
            return 0;
        }
    }

    private void setPrivateFlags(View view, int i) {
        try {
            mPrivateFlags.set((Object) view, i);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mPrivateFlags", e);
        }
    }

    /* access modifiers changed from: protected */
    public void attachSpecialMethods() {
    }

    public void buildProxy() {
        attachMethod("refreshDrawableState", "()V");
        attachMethod("onCreateDrawableState", "(I)[I");
        attachMethod("createContextMenu", "(Landroid/view/ContextMenu;)V");
        attachConstructor("(Landroid/content/Context;Landroid/util/AttributeSet;I)V");
        attachConstructor("(Landroid/content/Context;Landroid/util/AttributeSet;)V");
        attachSpecialMethods();
    }

    /* access modifiers changed from: protected */
    public int[] callOriginalOnCreateDrawableState(long j, View view, int i) {
        return originalOnCreateDrawableState(j, view, i);
    }

    /* access modifiers changed from: protected */
    public void callOriginalRefreshDrawableState(long j, View view) {
        originalRefreshDrawableState(j, view);
    }

    public Bitmap createSnapshot(View view, Config config, int i, boolean z) {
        Method method = createSnapshot;
        if (method != null) {
            try {
                return (Bitmap) method.invokeObject(View.class, view, config, Integer.valueOf(i), Boolean.valueOf(z));
            } catch (Exception e) {
                VariableExceptionHandler.getInstance().onThrow("invoke setLayoutInScreenEnabled failed", e);
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleRefreshDrawableState(0, null);
        handleOnCreateDrawableState(0, null, 0);
        handleCreateContextMenu(0, null, null);
        handle_init_(0, null, null, null, 0);
        handle_init_(0, null, null, null);
    }

    /* access modifiers changed from: protected */
    public void handleCreateContextMenu(long j, View view, ContextMenu contextMenu) {
        if (contextMenu instanceof ContextMenuBuilder) {
            if (ListenerInfoClass == null) {
                try {
                    ListenerInfoClass = Class.forName("android.view.View$ListenerInfo");
                    mOnCreateContextMenuListener = Field.of(ListenerInfoClass, "mOnCreateContextMenuListener", "Landroid/view/View$OnCreateContextMenuListener;");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            ContextMenuInfo contextMenuInfo = (ContextMenuInfo) getContextMenuInfo.invokeObject(view.getClass(), view, new Object[0]);
            ContextMenuBuilder contextMenuBuilder = (ContextMenuBuilder) contextMenu;
            contextMenuBuilder.setCurrentMenuInfo(contextMenuInfo);
            onCreateContextMenu.invoke(view.getClass(), view, contextMenu);
            Object obj = mListenerInfo.get(view);
            if (obj != null) {
                OnCreateContextMenuListener onCreateContextMenuListener = (OnCreateContextMenuListener) mOnCreateContextMenuListener.get(obj);
                if (onCreateContextMenuListener != null) {
                    onCreateContextMenuListener.onCreateContextMenu(contextMenu, view, contextMenuInfo);
                }
            }
            contextMenuBuilder.setCurrentMenuInfo(null);
            if (view.getParent() != null) {
                view.getParent().createContextMenu(contextMenu);
            }
        } else {
            originalCreateContextMenu(j, view, contextMenu);
        }
    }

    /* access modifiers changed from: protected */
    public int[] handleOnCreateDrawableState(long j, View view, int i) {
        return onCreateDrawableState(view, callOriginalOnCreateDrawableState(j, view, 0), i);
    }

    /* access modifiers changed from: protected */
    public void handleRefreshDrawableState(long j, View view) {
        callOriginalRefreshDrawableState(j, view);
        TaggingDrawableHelper.onDrawableStateChange(view);
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, View view, Context context, AttributeSet attributeSet) {
        original_init_(j, view, context, attributeSet);
        TaggingDrawableHelper.initViewSequenceStates(view, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, View view, Context context, AttributeSet attributeSet, int i) {
        original_init_(j, view, context, attributeSet, i);
        TaggingDrawableHelper.initViewSequenceStates(view, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void originalCreateContextMenu(long j, View view, ContextMenu contextMenu) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_View_class.originalCreateContextMenu(long, View, ContextMenu)");
    }

    /* access modifiers changed from: protected */
    public int[] originalOnCreateDrawableState(long j, View view, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_View_class.originalOnCreateDrawableState(long, View, int)");
    }

    /* access modifiers changed from: protected */
    public void originalRefreshDrawableState(long j, View view) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_View_class.originalRefreshDrawableState(long, View)");
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, View view, Context context, AttributeSet attributeSet) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_View_class.original_init_(long, View, Context, AttributeSet)");
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, View view, Context context, AttributeSet attributeSet, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_View_class.original_init_(long, View, Context, AttributeSet, int)");
    }

    public void relayout(View view) {
        if (view.getHeight() != 0 && view.getWidth() != 0) {
            int privateFlags = getPrivateFlags(view);
            view.forceLayout();
            view.measure(getOldWidthMeasureSpec(view), getOldHeightMeasureSpec(view));
            view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            setPrivateFlags(view, privateFlags);
        }
    }

    public void setImportantForAccessibilityNoHideDescendants(View view) {
    }

    public void setLeftDirectly(View view, int i) {
        try {
            mLeft.set((Object) view, i);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mScrollY", e);
        }
    }

    public void setRightDirectly(View view, int i) {
        try {
            mRight.set((Object) view, i);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mScrollY", e);
        }
    }

    public void setScrollXDirectly(View view, int i) {
        try {
            mScrollX.set((Object) view, i);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mScrollX", e);
        }
    }

    public void setScrollYDirectly(View view, int i) {
        try {
            mScrollY.set((Object) view, i);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.view.View.mScrollY", e);
        }
    }
}
