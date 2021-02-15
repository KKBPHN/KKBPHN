package com.airbnb.lottie.model;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.List;

@RestrictTo({Scope.LIBRARY})
public class O00000o {
    private final List O00oO00O;
    private final char O00oO0OO;
    private final String fontFamily;
    private final double size;
    private final String style;
    private final double width;

    public O00000o(List list, char c, double d, double d2, String str, String str2) {
        this.O00oO00O = list;
        this.O00oO0OO = c;
        this.size = d;
        this.width = d2;
        this.style = str;
        this.fontFamily = str2;
    }

    public static int O000000o(char c, String str, String str2) {
        return ((((0 + c) * 31) + str.hashCode()) * 31) + str2.hashCode();
    }

    public List O00Oo() {
        return this.O00oO00O;
    }

    /* access modifiers changed from: 0000 */
    public double getSize() {
        return this.size;
    }

    /* access modifiers changed from: 0000 */
    public String getStyle() {
        return this.style;
    }

    public double getWidth() {
        return this.width;
    }

    public int hashCode() {
        return O000000o(this.O00oO0OO, this.fontFamily, this.style);
    }
}
