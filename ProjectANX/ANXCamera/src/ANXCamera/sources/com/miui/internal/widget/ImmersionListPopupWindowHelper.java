package com.miui.internal.widget;

import android.content.Context;
import android.graphics.Outline;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.miui.internal.R;
import miui.util.AttributeResolver;

public class ImmersionListPopupWindowHelper {
    private static final String TAG = "ImmersionListPopupWindowHelper";

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

    public static int getPopupElevation(Context context) {
        return AttributeResolver.resolveDimensionPixelSize(context, R.attr.popupWindowElevation);
    }

    /* access modifiers changed from: private */
    public static float getPopupShadowAlpha(Context context) {
        return AttributeResolver.resolveFloat(context, R.attr.popupWindowShadowAlpha, 0.0f);
    }

    public static void setPopupShadowAlpha(View view) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                if (view.getWidth() != 0 && view.getHeight() != 0) {
                    outline.setAlpha(ImmersionListPopupWindowHelper.getPopupShadowAlpha(view.getContext()));
                    if (view.getBackground() != null) {
                        view.getBackground().getOutline(outline);
                    }
                }
            }
        });
    }
}
