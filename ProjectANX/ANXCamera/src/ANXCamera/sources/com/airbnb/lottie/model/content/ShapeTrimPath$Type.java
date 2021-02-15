package com.airbnb.lottie.model.content;

public enum ShapeTrimPath$Type {
    SIMULTANEOUSLY,
    INDIVIDUALLY;

    public static ShapeTrimPath$Type O000OOo0(int i) {
        if (i == 1) {
            return SIMULTANEOUSLY;
        }
        if (i == 2) {
            return INDIVIDUALLY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown trim path type ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
}
