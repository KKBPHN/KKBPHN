package com.ss.android.ugc.effectmanager.common.utils;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.ss.android.ugc.effectmanager.common.EffectConstants;
import java.io.File;
import java.util.regex.Pattern;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class EffectCacheKeyGenerator {
    private EffectCacheKeyGenerator() {
    }

    public static Pattern generateCategoryCachePattern(String str, String str2, int i, int i2, int i3) {
        StringBuilder sb = new StringBuilder();
        sb.append(EffectConstants.KEY_EFFECT_CHANNEL);
        sb.append(str2);
        sb.append("(.*)");
        return Pattern.compile(sb.toString());
    }

    public static String generateCategoryEffectKey(String str, String str2, int i, int i2, int i3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(File.separator);
        sb.append(EffectConstants.KEY_EFFECT_CHANNEL);
        sb.append(str2);
        sb.append(i);
        String str3 = "_";
        sb.append(str3);
        sb.append(i2);
        sb.append(str3);
        sb.append(i3);
        return sb.toString();
    }

    public static String generateCategoryVersionKey(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(File.separator);
        sb.append(EffectConstants.KEY_CATEGORY_VERSION);
        sb.append(str2);
        return sb.toString();
    }

    public static String generatePanelInfoKey(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(File.separator);
        sb.append(EffectConstants.KEY_EFFECT_CHANNEL);
        sb.append(str);
        return sb.toString();
    }

    public static String generatePanelInfoVersionKey(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(File.separator);
        sb.append("effect_version");
        return sb.toString();
    }

    public static String generatePanelKey(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(EffectConstants.KEY_EFFECT_CHANNEL);
        sb.append(str2);
        sb.append(str);
        return sb.toString();
    }

    public static Pattern generatePattern(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(EffectConstants.KEY_EFFECT_CHANNEL);
        sb.append(str);
        sb.append("(.*)");
        return Pattern.compile(sb.toString());
    }
}
