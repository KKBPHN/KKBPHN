package com.miui.internal.variable.v16;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import com.miui.internal.util.TaggingDrawableHelper;
import miui.reflect.Field;

public class Android_View_ViewGroup_class extends com.miui.internal.variable.Android_View_ViewGroup_class {
    protected static final int[] DESCENDANT_FOCUSABILITY_FLAGS;
    protected static final int[] R_styleable_ViewGroup;
    protected static final int R_styleable_ViewGroup_addStatesFromChildren;
    protected static final int R_styleable_ViewGroup_alwaysDrawnWithCache;
    protected static final int R_styleable_ViewGroup_animateLayoutChanges;
    protected static final int R_styleable_ViewGroup_animationCache;
    protected static final int R_styleable_ViewGroup_clipChildren;
    protected static final int R_styleable_ViewGroup_clipToPadding;
    protected static final int R_styleable_ViewGroup_descendantFocusability;
    protected static final int R_styleable_ViewGroup_layoutAnimation;
    protected static final int R_styleable_ViewGroup_persistentDrawingCache;
    protected static final int R_styleable_ViewGroup_splitMotionEvents;
    protected static final Class com_android_internal_R_styleable = getClass("com.android.internal.R$styleable");

    static {
        String str = "I";
        R_styleable_ViewGroup_clipChildren = Field.of(com_android_internal_R_styleable, "ViewGroup_clipChildren", str).getInt(null);
        R_styleable_ViewGroup_clipToPadding = Field.of(com_android_internal_R_styleable, "ViewGroup_clipToPadding", str).getInt(null);
        R_styleable_ViewGroup_animationCache = Field.of(com_android_internal_R_styleable, "ViewGroup_animationCache", str).getInt(null);
        R_styleable_ViewGroup_persistentDrawingCache = Field.of(com_android_internal_R_styleable, "ViewGroup_persistentDrawingCache", str).getInt(null);
        R_styleable_ViewGroup_addStatesFromChildren = Field.of(com_android_internal_R_styleable, "ViewGroup_addStatesFromChildren", str).getInt(null);
        R_styleable_ViewGroup_alwaysDrawnWithCache = Field.of(com_android_internal_R_styleable, "ViewGroup_alwaysDrawnWithCache", str).getInt(null);
        R_styleable_ViewGroup_layoutAnimation = Field.of(com_android_internal_R_styleable, "ViewGroup_layoutAnimation", str).getInt(null);
        R_styleable_ViewGroup_animateLayoutChanges = Field.of(com_android_internal_R_styleable, "ViewGroup_animateLayoutChanges", str).getInt(null);
        R_styleable_ViewGroup_descendantFocusability = Field.of(com_android_internal_R_styleable, "ViewGroup_descendantFocusability", str).getInt(null);
        R_styleable_ViewGroup_splitMotionEvents = Field.of(com_android_internal_R_styleable, "ViewGroup_splitMotionEvents", str).getInt(null);
        String str2 = "[I";
        R_styleable_ViewGroup = (int[]) Field.of(com_android_internal_R_styleable, "ViewGroup", str2).get(null);
        DESCENDANT_FOCUSABILITY_FLAGS = (int[]) Field.of(ViewGroup.class, "DESCENDANT_FOCUSABILITY_FLAGS", str2).get(null);
    }

    public static Class getClass(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void attachPrivateMethods() {
        String str = "(Landroid/content/Context;Landroid/util/AttributeSet;)V";
        attachMethod("initFromAttributes", str);
        attachConstructor("(Landroid/content/Context;Landroid/util/AttributeSet;I)V");
        attachConstructor(str);
    }

    public void buildProxy() {
        attachMethod("addInArray", "(Landroid/view/View;I)V");
        String str = "removeFromArray";
        attachMethod(str, "(I)V");
        attachMethod(str, "(II)V");
        attachMethod("onChildVisibilityChanged", "(Landroid/view/View;II)V");
        attachMethod("resolveLayoutDirection", "()Z");
        attachPrivateMethods();
    }

    /* access modifiers changed from: protected */
    public void callOriginalAddInArray(long j, ViewGroup viewGroup, View view, int i) {
        originalAddInArray(j, viewGroup, view, i);
    }

    /* access modifiers changed from: protected */
    public void callOriginalOnChildVisibilityChanged(long j, ViewGroup viewGroup, View view, int i, int i2) {
        originalOnChildVisibilityChanged(j, viewGroup, view, i, i2);
    }

    /* access modifiers changed from: protected */
    public void callOriginalRemoveFromArray(long j, ViewGroup viewGroup, int i) {
        originalRemoveFromArray(j, viewGroup, i);
    }

    /* access modifiers changed from: protected */
    public void callOriginalRemoveFromArray(long j, ViewGroup viewGroup, int i, int i2) {
        originalRemoveFromArray(j, viewGroup, i, i2);
    }

    /* access modifiers changed from: protected */
    public boolean callOriginalResolveLayoutDirection(long j, ViewGroup viewGroup) {
        return originalResolveLayoutDirection(j, viewGroup);
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i) {
        original_init_(j, viewGroup, context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleAddInArray(0, null, null, 0);
        handleRemoveFromArray(0, null, 0);
        handleRemoveFromArray(0, null, 0, 0);
        handleOnChildVisibilityChanged(0, null, null, 0, 0);
        handleResolveLayoutDirection(0, null);
        handleInitFromAttributes(0, null, null, null);
        handle_init_(0, null, null, null, 0);
        handle_init_(0, null, null, null);
    }

    /* access modifiers changed from: protected */
    public void handleAddInArray(long j, ViewGroup viewGroup, View view, int i) {
        callOriginalAddInArray(j, viewGroup, view, i);
        if (!(viewGroup instanceof AbsListView)) {
            TaggingDrawableHelper.tagChildSequenceState(viewGroup);
        }
    }

    /* access modifiers changed from: protected */
    public void handleInitFromAttributes(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet) {
    }

    /* access modifiers changed from: protected */
    public void handleOnChildVisibilityChanged(long j, ViewGroup viewGroup, View view, int i, int i2) {
        callOriginalOnChildVisibilityChanged(j, viewGroup, view, i, i2);
        TaggingDrawableHelper.tagChildSequenceState(viewGroup);
    }

    /* access modifiers changed from: protected */
    public void handleRemoveFromArray(long j, ViewGroup viewGroup, int i) {
        callOriginalRemoveFromArray(j, viewGroup, i);
        if (!(viewGroup instanceof AbsListView)) {
            TaggingDrawableHelper.tagChildSequenceState(viewGroup);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRemoveFromArray(long j, ViewGroup viewGroup, int i, int i2) {
        callOriginalRemoveFromArray(j, viewGroup, i, i2);
        if (!(viewGroup instanceof AbsListView)) {
            TaggingDrawableHelper.tagChildSequenceState(viewGroup);
        }
    }

    /* access modifiers changed from: protected */
    public boolean handleResolveLayoutDirection(long j, ViewGroup viewGroup) {
        int layoutDirection = viewGroup.getLayoutDirection();
        boolean callOriginalResolveLayoutDirection = callOriginalResolveLayoutDirection(j, viewGroup);
        if (layoutDirection != viewGroup.getLayoutDirection()) {
            TaggingDrawableHelper.tagChildSequenceState(viewGroup);
        }
        return callOriginalResolveLayoutDirection;
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet) {
        handle_init_(j, viewGroup, context, attributeSet, 0);
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i) {
        callOriginal_init_(j, viewGroup, context, attributeSet, i);
        initFromAttributes(viewGroup, context, attributeSet, i, 0);
    }

    /* access modifiers changed from: protected */
    public void initFromAttributes(ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R_styleable_ViewGroup, i, i2);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i3 = 0; i3 < indexCount; i3++) {
            processAttribute(viewGroup, obtainStyledAttributes.getIndex(i3), obtainStyledAttributes, context);
        }
        obtainStyledAttributes.recycle();
        TaggingDrawableHelper.initTagChildSequenceState(viewGroup, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void originalAddInArray(long j, ViewGroup viewGroup, View view, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.originalAddInArray(long, ViewGroup, View, int)");
    }

    /* access modifiers changed from: protected */
    public void originalInitFromAttributes(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.originalInitFromAttributes(long, ViewGroup, Context, AttributeSet)");
    }

    /* access modifiers changed from: protected */
    public void originalOnChildVisibilityChanged(long j, ViewGroup viewGroup, View view, int i, int i2) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.originalOnChildVisibilityChanged(long, ViewGroup, View, int, int)");
    }

    /* access modifiers changed from: protected */
    public void originalRemoveFromArray(long j, ViewGroup viewGroup, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.originalRemoveFromArray(long, ViewGroup, int)");
    }

    /* access modifiers changed from: protected */
    public void originalRemoveFromArray(long j, ViewGroup viewGroup, int i, int i2) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.originalRemoveFromArray(long, ViewGroup, int, int)");
    }

    /* access modifiers changed from: protected */
    public boolean originalResolveLayoutDirection(long j, ViewGroup viewGroup) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.originalResolveLayoutDirection(long, ViewGroup)");
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.original_init_(long, ViewGroup, Context, AttributeSet)");
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_View_ViewGroup_class.original_init_(long, ViewGroup, Context, AttributeSet, int)");
    }

    /* access modifiers changed from: protected */
    public void processAttribute(ViewGroup viewGroup, int i, TypedArray typedArray, Context context) {
        if (i == R_styleable_ViewGroup_clipChildren) {
            viewGroup.setClipChildren(typedArray.getBoolean(i, true));
        } else if (i == R_styleable_ViewGroup_clipToPadding) {
            viewGroup.setClipToPadding(typedArray.getBoolean(i, true));
        } else if (i == R_styleable_ViewGroup_animationCache) {
            viewGroup.setAnimationCacheEnabled(typedArray.getBoolean(i, true));
        } else if (i == R_styleable_ViewGroup_persistentDrawingCache) {
            viewGroup.setPersistentDrawingCache(typedArray.getInt(i, 2));
        } else if (i == R_styleable_ViewGroup_addStatesFromChildren) {
            viewGroup.setAddStatesFromChildren(typedArray.getBoolean(i, false));
        } else if (i == R_styleable_ViewGroup_alwaysDrawnWithCache) {
            viewGroup.setAlwaysDrawnWithCacheEnabled(typedArray.getBoolean(i, true));
        } else if (i == R_styleable_ViewGroup_layoutAnimation) {
            int resourceId = typedArray.getResourceId(i, -1);
            if (resourceId > 0) {
                viewGroup.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(context, resourceId));
            }
        } else if (i == R_styleable_ViewGroup_descendantFocusability) {
            viewGroup.setDescendantFocusability(DESCENDANT_FOCUSABILITY_FLAGS[typedArray.getInt(i, 0)]);
        } else if (i == R_styleable_ViewGroup_splitMotionEvents) {
            viewGroup.setMotionEventSplittingEnabled(typedArray.getBoolean(i, false));
        } else if (i == R_styleable_ViewGroup_animateLayoutChanges && typedArray.getBoolean(i, false)) {
            viewGroup.setLayoutTransition(new LayoutTransition());
        }
    }
}
