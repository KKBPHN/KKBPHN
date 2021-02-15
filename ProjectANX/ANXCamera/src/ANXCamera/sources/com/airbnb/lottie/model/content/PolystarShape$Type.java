package com.airbnb.lottie.model.content;

public enum PolystarShape$Type {
    STAR(1),
    POLYGON(2);
    
    private final int value;

    private PolystarShape$Type(int i) {
        this.value = i;
    }

    public static PolystarShape$Type O000OOo(int i) {
        PolystarShape$Type[] values;
        for (PolystarShape$Type polystarShape$Type : values()) {
            if (polystarShape$Type.value == i) {
                return polystarShape$Type;
            }
        }
        return null;
    }
}
