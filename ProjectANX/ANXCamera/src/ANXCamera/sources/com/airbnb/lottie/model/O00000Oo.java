package com.airbnb.lottie.model;

import androidx.annotation.ColorInt;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY})
public class O00000Oo {
    public final DocumentData$Justification O00o;
    public final String O00o0ooo;
    public final boolean O00oO000;
    public final int O00oO0O0;
    public final float baselineShift;
    @ColorInt
    public final int color;
    public final float lineHeight;
    public final float size;
    @ColorInt
    public final int strokeColor;
    public final float strokeWidth;
    public final String text;

    public O00000Oo(String str, String str2, float f, DocumentData$Justification documentData$Justification, int i, float f2, float f3, @ColorInt int i2, @ColorInt int i3, float f4, boolean z) {
        this.text = str;
        this.O00o0ooo = str2;
        this.size = f;
        this.O00o = documentData$Justification;
        this.O00oO0O0 = i;
        this.lineHeight = f2;
        this.baselineShift = f3;
        this.color = i2;
        this.strokeColor = i3;
        this.strokeWidth = f4;
        this.O00oO000 = z;
    }

    public int hashCode() {
        long floatToRawIntBits = (long) Float.floatToRawIntBits(this.lineHeight);
        return (((((((((int) (((float) (((this.text.hashCode() * 31) + this.O00o0ooo.hashCode()) * 31)) + this.size)) * 31) + this.O00o.ordinal()) * 31) + this.O00oO0O0) * 31) + ((int) (floatToRawIntBits ^ (floatToRawIntBits >>> 32)))) * 31) + this.color;
    }
}
