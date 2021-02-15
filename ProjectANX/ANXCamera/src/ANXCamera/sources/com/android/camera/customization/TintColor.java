package com.android.camera.customization;

import android.graphics.Color;
import android.text.TextUtils;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TintColor {
    private static final String COLOR_BLUE = "#9895FF";
    private static final String COLOR_CYAN = "#75E9E3";
    private static final String COLOR_GREEN = "#0199FF";
    private static final String COLOR_RED = "#F5C89D";
    private static final String COLOR_YELLOW = "#FFCE16";
    public static final String KEY_TINT_COLOR = "pref_tint_color";
    private static final String LOG_TAG = "TintColor";
    private static List sAvailableColors = availableColors();
    private final String color;
    private final int name;
    private final int previewCH;
    private final int previewEN;

    public TintColor(String str, int i, int i2, int i3) {
        this.color = str;
        this.name = i;
        this.previewCH = i2;
        this.previewEN = i3;
    }

    private static List availableColors() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new TintColor(COLOR_YELLOW, R.string.custom_tint_color_light_orange, R.drawable.custom_tint_preview_yellow_ch, R.drawable.custom_tint_preview_yellow_en));
        arrayList.add(new TintColor("#FFC396", R.string.custom_tint_color_champagne, R.drawable.custom_tint_preview_brown_ch, R.drawable.custom_tint_preview_brown_en));
        arrayList.add(new TintColor(COLOR_BLUE, R.string.custom_tint_color_lilac, R.drawable.custom_tint_preview_purple_ch, R.drawable.custom_tint_preview_purple_en));
        arrayList.add(new TintColor("#30C0FF", R.string.custom_tint_color_sky_blue, R.drawable.custom_tint_preview_blue_ch, R.drawable.custom_tint_preview_blue_en));
        arrayList.add(new TintColor(COLOR_CYAN, R.string.custom_tint_color_sky_cyan, R.drawable.custom_tint_preview_green_ch, R.drawable.custom_tint_preview_green_en));
        return arrayList;
    }

    public static List getAvailableColors() {
        return sAvailableColors;
    }

    public static boolean isYellowTintColor() {
        return COLOR_YELLOW.equals(((TintColor) sAvailableColors.get(readColorId())).color);
    }

    public static void persistColorId(int i) {
        DataRepository.dataItemGlobal().editor().putInt(KEY_TINT_COLOR, i).apply();
    }

    public static int readColorId() {
        return DataRepository.dataItemGlobal().getInt(KEY_TINT_COLOR, 0);
    }

    public static int tintColor() {
        return ((TintColor) sAvailableColors.get(readColorId())).color();
    }

    public int color() {
        return Color.parseColor(this.color);
    }

    public int name() {
        return this.name;
    }

    public int previewResource() {
        return TextUtils.equals(Locale.getDefault().getLanguage(), Locale.CHINESE.getLanguage()) ? this.previewCH : this.previewEN;
    }
}
