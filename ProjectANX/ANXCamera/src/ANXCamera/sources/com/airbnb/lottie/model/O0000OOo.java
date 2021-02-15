package com.airbnb.lottie.model;

public class O0000OOo {
    private static String CARRIAGE_RETURN = "\r";
    public final float O000oO0O;
    public final float O00oO0;
    private final String name;

    public O0000OOo(String str, float f, float f2) {
        this.name = str;
        this.O00oO0 = f2;
        this.O000oO0O = f;
    }

    public boolean O00oOoOo(String str) {
        if (this.name.equalsIgnoreCase(str)) {
            return true;
        }
        if (this.name.endsWith(CARRIAGE_RETURN)) {
            String str2 = this.name;
            if (str2.substring(0, str2.length() - 1).equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
