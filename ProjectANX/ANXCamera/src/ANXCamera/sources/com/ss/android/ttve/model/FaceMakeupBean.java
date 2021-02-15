package com.ss.android.ttve.model;

public class FaceMakeupBean {
    private float mLipStickIntensity;
    private float mNasolabialIntensity;
    private float mPouchIntensity;
    private String mResPath;
    private float mblusherIntensity;

    public FaceMakeupBean() {
        this("", 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public FaceMakeupBean(String str, float f, float f2) {
        this.mResPath = str;
        this.mLipStickIntensity = f;
        this.mblusherIntensity = f2;
    }

    public FaceMakeupBean(String str, float f, float f2, float f3, float f4) {
        this(str, f, f2);
        this.mNasolabialIntensity = f3;
        this.mPouchIntensity = f4;
    }

    public float getBlusherIntensity() {
        return this.mblusherIntensity;
    }

    public float getLipStickIntensity() {
        return this.mLipStickIntensity;
    }

    public float getNasolabialIntensity() {
        return this.mNasolabialIntensity;
    }

    public float getPouchIntensity() {
        return this.mPouchIntensity;
    }

    public String getResPath() {
        return this.mResPath;
    }

    public void setBlusherIntensity(float f) {
        this.mblusherIntensity = f;
    }

    public void setLipStickIntensity(float f) {
        this.mLipStickIntensity = f;
    }

    public void setNasolabialIntensity(float f) {
        this.mNasolabialIntensity = f;
    }

    public void setPouchIntensity(float f) {
        this.mPouchIntensity = f;
    }

    public void setResPath(String str) {
        this.mResPath = str;
    }
}
