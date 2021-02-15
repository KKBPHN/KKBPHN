package com.miui.internal.widget;

import android.content.Context;
import android.graphics.Outline;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import java.util.Locale;
import miui.util.AttributeResolver;
import miui.widget.DropDownPopupWindow.ContainerController;

public class DropDownPopupWindowHelper {
    private static final String TAG = "DropDownPopupWindowHelper";

    public static void changeWindowBackground(View view, float f) {
        if (view != null) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.flags |= 2;
                layoutParams.dimAmount = f;
                ((WindowManager) view.getContext().getSystemService("window")).updateViewLayout(view, layoutParams);
            }
        } else {
            Log.w(TAG, "can't change window dim with null view");
        }
    }

    public static int getAnimationStyle() {
        if (DeviceHelper.FEATURE_WHOLE_ANIM) {
            return R.style.Animation_PopupWindow_DropDown;
        }
        return 0;
    }

    public static int getHeight() {
        return -2;
    }

    public static int getPopupElevation(Context context) {
        return AttributeResolver.resolveDimensionPixelSize(context, R.attr.popupWindowElevation);
    }

    public static int getPopupMinWidth(Context context) {
        return AttributeResolver.resolveDimensionPixelSize(context, R.attr.popupWindowMinWidth);
    }

    public static float getPopupShadowAlpha(Context context) {
        return AttributeResolver.resolveFloat(context, R.attr.popupWindowShadowAlpha, 0.0f);
    }

    public static View getRealContentView(View view, View view2) {
        return view2;
    }

    public static int getRealHeight(Context context, View view) {
        return getHeight();
    }

    public static int getShowX(int[] iArr, int i) {
        return !isInRtl() ? iArr[0] : iArr[0] + i;
    }

    public static int getShowY(int[] iArr, int i) {
        return iArr[1];
    }

    public static int getWidth() {
        return -2;
    }

    private static boolean isInRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    private static int measureListViewWidth(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(0, 0);
        int count = adapter.getCount();
        int i = 0;
        int i2 = 0;
        View view = null;
        for (int i3 = 0; i3 < count; i3++) {
            int itemViewType = adapter.getItemViewType(i3);
            if (itemViewType != i2) {
                view = null;
                i2 = itemViewType;
            }
            view = adapter.getView(i3, view, listView);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            int measuredWidth = view.getMeasuredWidth();
            if (measuredWidth > i) {
                i = measuredWidth;
            }
        }
        return i;
    }

    public static int setupContentView(FrameLayout frameLayout, final View view, int i, int i2, ContainerController containerController) {
        int i3;
        if (view == null) {
            return getWidth();
        }
        if (i > 0) {
            view.setElevation((float) i);
        }
        view.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                if (view.getWidth() != 0 && view.getHeight() != 0) {
                    outline.setAlpha(DropDownPopupWindowHelper.getPopupShadowAlpha(view.getContext()));
                    if (view.getBackground() != null) {
                        view.getBackground().getOutline(outline);
                    }
                }
            }
        });
        if (view instanceof ListView) {
            i3 = measureListViewWidth((ListView) view);
        } else {
            view.measure(0, 0);
            i3 = view.getMeasuredWidth();
        }
        if (i3 < i2) {
            i3 = i2;
        }
        return i3;
    }
}
