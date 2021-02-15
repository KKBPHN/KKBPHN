package com.miui.internal.util;

import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.miui.internal.variable.Android_Graphics_Drawable_Drawable_class;
import com.miui.internal.variable.Android_Graphics_Drawable_Drawable_class.Factory;
import com.miui.internal.variable.Android_View_ViewGroup_class;
import com.miui.internal.variable.Android_View_View_class;
import miui.R;
import miui.util.ViewUtils;
import miui.view.OnTaggingDrawableState;

public class TaggingDrawableHelper {
    static int[] CHILD_SEQUENCE_STATE = {R.attr.children_sequence_state};
    private static final Android_Graphics_Drawable_Drawable_class DrawableClass = Factory.getInstance().get();
    private static final Android_View_View_class ViewClass = Android_View_View_class.Factory.getInstance().get();
    private static final Android_View_ViewGroup_class ViewGroupClass = Android_View_ViewGroup_class.Factory.getInstance().get();
    private static final ThreadLocal sRect = new ThreadLocal();

    private TaggingDrawableHelper() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002c, code lost:
        if (r5 != false) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0034, code lost:
        if (r5 != false) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003d, code lost:
        if (r5 != false) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0043, code lost:
        if (r5 != false) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return miui.R.attr.state_last_h;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getOrientationState(ViewGroup viewGroup, int i, boolean z) {
        boolean z2;
        boolean z3;
        if (viewGroup instanceof OnTaggingDrawableState) {
            OnTaggingDrawableState onTaggingDrawableState = (OnTaggingDrawableState) viewGroup;
            z3 = onTaggingDrawableState.sameWithNeighbour(viewGroup, i, z, false);
            z2 = onTaggingDrawableState.sameWithNeighbour(viewGroup, i, z, true);
        } else {
            z3 = sameWithNeighbour(viewGroup, i, z, false);
            z2 = sameWithNeighbour(viewGroup, i, z, true);
        }
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(viewGroup);
        if (z3) {
            if (z2) {
                return z ? R.attr.state_middle_h : R.attr.state_middle_v;
            }
            if (isLayoutRtl) {
            }
            return R.attr.state_last_v;
        } else if (!z2) {
            return z ? R.attr.state_single_h : R.attr.state_single_v;
        } else {
            if (isLayoutRtl) {
            }
            return R.attr.state_first_v;
        }
        return R.attr.state_first_h;
    }

    public static void initTagChildSequenceState(ViewGroup viewGroup, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = viewGroup.getContext().obtainStyledAttributes(attributeSet, CHILD_SEQUENCE_STATE);
        boolean z = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
        ViewGroupClass.setTagChildrenSequenceState(viewGroup, z);
    }

    public static void initViewSequenceStates(View view, AttributeSet attributeSet) {
        if (!ViewClass.hasInitViewSequenceStates(view)) {
            ViewClass.setHasInitViewSequenceStates(view, true);
            TypedArray obtainStyledAttributes = view.getContext().obtainStyledAttributes(attributeSet, R.styleable.DrawableStates);
            int length = R.styleable.DrawableStates.length;
            int i = R.attr.state_single_h;
            int i2 = R.attr.state_single_v;
            int i3 = i;
            int i4 = 0;
            while (i4 < length) {
                if (obtainStyledAttributes.getBoolean(i4, false)) {
                    if (i4 == R.styleable.DrawableStates_state_first_h || i4 == R.styleable.DrawableStates_state_middle_h || i4 == R.styleable.DrawableStates_state_last_h || i4 == R.styleable.DrawableStates_state_single_h) {
                        i3 = R.styleable.DrawableStates[i4];
                    } else if (i4 == R.styleable.DrawableStates_state_first_v || i4 == R.styleable.DrawableStates_state_middle_v || i4 == R.styleable.DrawableStates_state_last_v || i4 == R.styleable.DrawableStates_state_single_v) {
                        i2 = R.styleable.DrawableStates[i4];
                    }
                }
                i4++;
            }
            obtainStyledAttributes.recycle();
            ViewClass.setHorizontalState(view, i3);
            ViewClass.setVerticalState(view, i2);
        }
    }

    private static boolean isSameBackground(View view, View view2) {
        boolean z = false;
        if (!(view == null || view2 == null)) {
            Drawable background = view.getBackground();
            Drawable background2 = view2.getBackground();
            if (background == background2) {
                return true;
            }
            if (!(background == null || background2 == null)) {
                int id = DrawableClass.getId(background);
                int id2 = DrawableClass.getId(background2);
                if (id != 0 && id == id2) {
                    z = true;
                }
            }
        }
        return z;
    }

    public static void onDrawableStateChange(View view) {
        int i;
        int i2;
        int i3;
        Drawable background = view.getBackground();
        if (background != null && background.isStateful()) {
            ConstantState constantState = background.getConstantState();
            if (background.isStateful() && constantState != null && (constantState instanceof DrawableContainerState) && ((DrawableContainerState) constantState).getConstantPadding() == null) {
                Rect rect = (Rect) sRect.get();
                if (rect == null) {
                    rect = new Rect();
                    sRect.set(rect);
                }
                if (!background.getPadding(rect)) {
                    return;
                }
                if (rect.left != view.getPaddingLeft() || rect.top != view.getPaddingTop() || rect.right != view.getPaddingRight() || rect.bottom != view.getPaddingBottom()) {
                    if (DrawableClass.getLayoutDirection(background) != 1) {
                        i = rect.left;
                        i2 = rect.top;
                        i3 = rect.right;
                    } else {
                        i = rect.right;
                        i2 = rect.top;
                        i3 = rect.left;
                    }
                    view.setPadding(i, i2, i3, rect.bottom);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00a0, code lost:
        if (r3 == 0) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a4, code lost:
        if (r3 == 1) goto L_0x00a8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean sameWithNeighbour(ViewGroup viewGroup, int i, boolean z, boolean z2) {
        View view;
        View view2;
        boolean z3 = true;
        if (viewGroup instanceof AbsListView) {
            AbsListView absListView = (AbsListView) viewGroup;
            int numColumns = absListView instanceof GridView ? ((GridView) absListView).getNumColumns() : 1;
            int childCount = absListView.getChildCount();
            if (z) {
                int i2 = i % numColumns;
                if (numColumns == 1) {
                    return false;
                }
                if (z2) {
                    if (i2 + 1 >= numColumns) {
                        return false;
                    }
                    int i3 = i + 1;
                    if (i3 < childCount) {
                        return isSameBackground(absListView.getChildAt(i), absListView.getChildAt(i3));
                    }
                    return false;
                } else if (i2 - 1 >= 0) {
                    return isSameBackground(absListView.getChildAt(i), absListView.getChildAt(i - 1));
                } else {
                    return false;
                }
            } else {
                int firstVisiblePosition = absListView.getFirstVisiblePosition();
                Adapter adapter = absListView.getAdapter();
                int count = absListView.getCount();
                if (z2) {
                    int i4 = numColumns + i;
                    if (i4 < childCount) {
                        view = absListView.getChildAt(i);
                        view2 = absListView.getChildAt(i4);
                        return isSameBackground(view, view2);
                    }
                    int i5 = i4 + firstVisiblePosition;
                    if (i5 >= count || adapter.getItemViewType(i + firstVisiblePosition) != adapter.getItemViewType(i5)) {
                        return false;
                    }
                } else {
                    int i6 = i - numColumns;
                    if (i6 >= 0) {
                        view = absListView.getChildAt(i);
                        view2 = absListView.getChildAt(i6);
                        return isSameBackground(view, view2);
                    }
                    int i7 = i6 + firstVisiblePosition;
                    if (i7 < 0 || adapter.getItemViewType(i + firstVisiblePosition) != adapter.getItemViewType(i7)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            boolean z4 = viewGroup instanceof LinearLayout;
            if (z4) {
                int orientation = ((LinearLayout) viewGroup).getOrientation();
                if (z) {
                }
                if (!z) {
                }
            }
            z3 = false;
            if (z3 || (!z4 && z)) {
                return sameWithNeighbourInLinear(viewGroup, i, z2);
            }
            return false;
        }
    }

    private static boolean sameWithNeighbourInLinear(ViewGroup viewGroup, int i, boolean z) {
        int i2 = -1;
        int i3 = z ? 1 : -1;
        if (z) {
            i2 = viewGroup.getChildCount();
        }
        View childAt = viewGroup.getChildAt(i);
        View view = null;
        while (true) {
            i += i3;
            if (i == i2) {
                break;
            }
            View childAt2 = viewGroup.getChildAt(i);
            if (childAt2.getVisibility() != 8) {
                view = childAt2;
                break;
            }
        }
        return (view == null || view.getVisibility() == 4 || !isSameBackground(view, childAt)) ? false : true;
    }

    public static void tagChildSequenceState(ViewGroup viewGroup) {
        if (ViewGroupClass.getTagChildrenSequenceState(viewGroup)) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt.getVisibility() == 0) {
                    tagView(childAt, getOrientationState(viewGroup, i, true), getOrientationState(viewGroup, i, false));
                }
            }
        }
    }

    private static void tagView(View view, int i, int i2) {
        int horizontalState = ViewClass.getHorizontalState(view);
        int verticalState = ViewClass.getVerticalState(view);
        if (horizontalState != i || verticalState != i2) {
            ViewClass.setHorizontalState(view, i);
            ViewClass.setVerticalState(view, i2);
            int paddingLeft = view.getPaddingLeft();
            int paddingTop = view.getPaddingTop();
            int paddingRight = view.getPaddingRight();
            int paddingBottom = view.getPaddingBottom();
            view.refreshDrawableState();
            if (paddingLeft != view.getPaddingLeft() || paddingTop != view.getPaddingTop() || paddingRight != view.getPaddingRight() || paddingBottom != view.getPaddingBottom()) {
                ViewClass.relayout(view);
            }
        }
    }
}
