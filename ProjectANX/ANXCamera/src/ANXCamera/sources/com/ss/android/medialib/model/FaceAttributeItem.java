package com.ss.android.medialib.model;

import androidx.annotation.Keep;

@Keep
public class FaceAttributeItem {
    String category;
    float label;
    float score;

    public String getCategory() {
        return this.category;
    }

    public float getLabel() {
        return this.label;
    }

    public float getScore() {
        return this.score;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public void setLabel(float f) {
        this.label = f;
    }

    public void setScore(float f) {
        this.score = f;
    }
}
