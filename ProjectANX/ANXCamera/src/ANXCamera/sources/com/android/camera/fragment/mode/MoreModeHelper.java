package com.android.camera.fragment.mode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Size;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;

public class MoreModeHelper {
    private static final String TAG = "MoreModeHelper";

    public static int getHeaderHeightForNormal(Context context, int i, int i2, int i3) {
        if (isLandscapeMode()) {
            return 0;
        }
        if (i != 3 && i != 0) {
            return 0;
        }
        int min = Math.min(Display.getMaxViewFinderRect().height(), Display.getDisplayRect(DataRepository.dataItemRunning().getUiStyle()).height()) - context.getResources().getDimensionPixelOffset(i == 3 ? R.dimen.mode_item_margin_header : R.dimen.mode_more_bottom_margin);
        boolean z = true;
        int i4 = (i3 / i2) + (i3 % i2 == 0 ? 0 : 1);
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        if (i != 3) {
            z = false;
        }
        int moreModeTabRow = Display.getMoreModeTabRow(uiStyle, z);
        if (i4 >= moreModeTabRow) {
            if (i == 3) {
                return 0;
            }
            i4 = moreModeTabRow;
        }
        int modeHeight = getModeHeight(context, i);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getHeaderHeightForNormal ");
        int i5 = min - (i4 * modeHeight);
        sb.append(i5);
        sb.append(", type = ");
        sb.append(i);
        sb.append(", preLine = ");
        sb.append(i2);
        sb.append(", size = ");
        sb.append(i3);
        Log.d(str, sb.toString());
        return Math.max(i5, 0);
    }

    public static int getModeHeight(Context context, int i) {
        if (i == 0) {
            return Display.getMoreModeTabMarginVer(DataRepository.dataItemRunning().getUiStyle(), false) + context.getResources().getDimensionPixelOffset(R.dimen.mode_item_width);
        }
        return Display.getMoreModeTabMarginVer(DataRepository.dataItemRunning().getUiStyle(), true) + context.getResources().getDimensionPixelOffset(R.dimen.mode_icon_size_new);
    }

    public static int getPanelWidth(Context context, boolean z) {
        return (!Display.fitDisplayFull(1.3333333f) || !z) ? (Util.getScreenWidth(context) - Display.getStartMargin()) - Display.getEndMargin() : ((Util.getScreenWidth(context) - Display.getStartMargin()) - Display.getEndMargin()) - (context.getResources().getDimensionPixelOffset(R.dimen.mode_list_hor_padding_popup) * 2);
    }

    public static Rect getRegion(Context context, int i, int i2, int i3, int i4, int i5) {
        if (i != 3 && i != 0) {
            return new Rect();
        }
        int screenWidth = Util.getScreenWidth(context);
        Resources resources = context.getResources();
        int dimensionPixelSize = i == 3 ? resources.getDimensionPixelSize(R.dimen.mode_icon_size_new) : resources.getDimensionPixelSize(R.dimen.mode_icon_size);
        int dimensionPixelSize2 = i == 3 ? context.getResources().getDimensionPixelSize(R.dimen.mode_icon_size_new) : context.getResources().getDimensionPixelSize(R.dimen.mode_item_width);
        int i6 = (dimensionPixelSize2 - dimensionPixelSize) / 2;
        int i7 = i4 % i3;
        int i8 = i4 / i3;
        int dimensionPixelSize3 = ((screenWidth - (i2 * dimensionPixelSize2)) - (context.getResources().getDimensionPixelSize(i == 0 ? R.dimen.mode_list_hor_margin_normal : R.dimen.mode_list_hor_margin_normal_new) * 2)) / (i2 * 2);
        int headerHeightForNormal = getHeaderHeightForNormal(context, i, i2, i5) + context.getResources().getDimensionPixelOffset(i == 3 ? R.dimen.mode_item_margin_header : R.dimen.mode_more_bottom_margin);
        Size size = new Size(dimensionPixelSize2 + (dimensionPixelSize3 * 2), getModeHeight(context, i));
        int width = dimensionPixelSize3 + (size.getWidth() * i7);
        int height = headerHeightForNormal + (size.getHeight() * i8);
        return new Rect(width, height, i6 + width + dimensionPixelSize, dimensionPixelSize + height);
    }

    public static int getRow4PopupStyle(int i) {
        return (!Display.fitDisplayFull(1.3333333f) && i > 9) ? 4 : 3;
    }

    public static int getTopMarginForNormal(Context context, int i) {
        int i2 = 0;
        if (isLandscapeMode()) {
            return 0;
        }
        if (i == 3) {
            i2 = context.getResources().getDimensionPixelSize(R.dimen.mode_item_margin_header);
        }
        return i2;
    }

    public static boolean isFooter4PopupStyle(int i, int i2) {
        boolean z = false;
        int moreModeTabCol = Display.getMoreModeTabCol(DataRepository.dataItemRunning().getUiStyle(), false);
        int i3 = i2 % moreModeTabCol;
        if (i3 == 0) {
            if (i > (i2 - moreModeTabCol) - 1) {
                z = true;
            }
            return z;
        }
        if (i > (i2 - i3) - 1) {
            z = true;
        }
        return z;
    }

    private static boolean isLandscapeMode() {
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        return baseDelegate != null && (baseDelegate.getAnimationComposite().getTargetDegree() == 90 || baseDelegate.getAnimationComposite().getTargetDegree() == 270) && Display.fitDisplayFull(1.3333333f);
    }
}
