package com.miui.internal.analytics;

import java.util.Random;
import java.util.regex.Pattern;

public class Item {
    private Pattern mIdRegEx;
    private String mPolicy;
    private long mProbability;
    private Random mRandom = new Random();
    private Pattern mVersionRegEx;

    public Item(String str, String str2, String str3, long j) {
        this.mIdRegEx = Pattern.compile(str);
        this.mPolicy = str2;
        this.mVersionRegEx = Pattern.compile(str3);
        this.mProbability = j;
    }

    public String getPolicy() {
        return this.mPolicy;
    }

    public boolean idMatches(String str) {
        return this.mIdRegEx.matcher(str).matches();
    }

    public boolean isDispatch(String str) {
        return this.mProbability >= ((long) (this.mRandom.nextInt(100) + 1)) && this.mVersionRegEx.matcher(str).matches();
    }
}
