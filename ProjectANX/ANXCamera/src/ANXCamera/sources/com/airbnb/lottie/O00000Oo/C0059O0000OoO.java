package com.airbnb.lottie.O00000oO;

/* renamed from: com.airbnb.lottie.O00000oO.O0000OoO reason: case insensitive filesystem */
public class C0059O0000OoO {
    private float scaleX;
    private float scaleY;

    public C0059O0000OoO() {
        this(1.0f, 1.0f);
    }

    public C0059O0000OoO(float f, float f2) {
        this.scaleX = f;
        this.scaleY = f2;
    }

    public boolean equals(float f, float f2) {
        return this.scaleX == f && this.scaleY == f2;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void set(float f, float f2) {
        this.scaleX = f;
        this.scaleY = f2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getScaleX());
        sb.append("x");
        sb.append(getScaleY());
        return sb.toString();
    }
}
