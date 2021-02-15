package com.airbnb.lottie.model.content;

import android.graphics.Paint.Join;

public enum ShapeStroke$LineJoinType {
    MITER,
    ROUND,
    BEVEL;

    public Join Oo0o0oo() {
        int i = C0108O0000o0O.O00oOo0[ordinal()];
        if (i == 1) {
            return Join.BEVEL;
        }
        if (i == 2) {
            return Join.MITER;
        }
        if (i != 3) {
            return null;
        }
        return Join.ROUND;
    }
}
